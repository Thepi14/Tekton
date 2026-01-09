package tekton.type.power;

import static mindustry.Vars.tilesize;
import static tekton.content.TektonVars.*;

import java.util.Arrays;

import arc.Core;
import arc.Events;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.EnumSet;
import arc.struct.IntSet;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.Strings;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Liquids;
import mindustry.game.EventType.Trigger;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.ui.Bar;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.draw.*;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.BlockStatus;
import mindustry.world.meta.Env;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonItems;
import tekton.content.TektonStat;
import tekton.type.gravity.GravityBlock;
import tekton.type.gravity.GravityConductor;
import tekton.type.gravity.GravityConsumer;
import tekton.type.gravity.GravityConductor.GravityConductorBuild;

public class FusionReactor extends PowerGenerator {
    
    public Item coolantItem = TektonItems.cryogenicCompound;
    public Liquid fuelLiquid = Liquids.hydrogen;
    
    public float itemDuration = 120f;
    public float fuelConsumption = (6f / 60f) / 3f;
    public float warmupSpeed = 0.001f;
	public int requiredGravity = 48 * gravityMul;
	public int minGravity = requiredGravity / 2;
	public int minimalCoolant = 5;
	
	public float heatingPerTick = 0.01f;
	public float maxHeatingPerTick = 0.018f;
	public float explosionThreshold = 0.5f;

    public float alpha = 0.9f, glowScale = 10f, glowIntensity = 0.5f;

    public Color hotColor = Color.valueOf("ff9575a3");
    public Color heatColor = Pal.lightFlame;

    public TextureRegion overheatRegion;

	public FusionReactor(String name) {
		super(name);
		
		hasPower = true;
        hasLiquids = true;
        liquidCapacity = 30f;
        hasItems = true;
        outputsPower = consumesPower = true;
        flags = EnumSet.of(BlockFlag.reactor, BlockFlag.generator);
        lightRadius = 115f;
        emitLight = true;
        envEnabled = Env.any;
        rebuildable = false;
        schematicPriority = -5;
		
        drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawPlasma(), new DrawDefault());

        explosionShake = 10f;
        explosionShakeDuration = 26f;
        explosionDamage = 1900 * 4;
        explosionMinWarmup = 0.15f;
        explodeEffect = TektonFx.nuclearFusionExplosion;
        explodeSound = Sounds.explosionbig;
	}
	
	@Override
    public void load() {
        super.load();
        overheatRegion = Core.atlas.find(name + "-overheat");
    }
	
	@Override
    public void setBars() {
        super.setBars();

        addBar("power", (GeneratorBuild entity) -> new Bar(
        		() -> Core.bundle.format("bar.poweroutput", Strings.fixed(Math.max(entity.getPowerProduction() - consPower.usage, 0) * 60 * entity.timeScale(), 1)),
        		() -> Pal.powerBar,
        		() -> entity.productionEfficiency));
        addBar("instability", (FusionReactorBuild entity) -> new Bar("bar.instability", Pal.negativeStat, () -> entity.instability));
        addBar("gravity", (FusionReactorBuild entity) -> new Bar(
        		() -> Core.bundle.format("bar.gravityPercent", Math.min(requiredGravity, entity.gravity), (entity.gravityFrac()) * 100), 
        		() -> TektonColor.gravityColor, 
				() -> entity.gravityFrac()));
    }
	
	@Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.productionTime, itemDuration / 60f, StatUnit.seconds);
        //stats.add(Stat.maxEfficiency, (maxGravity / requiredGravity) * 100f, StatUnit.percent);
        stats.add(TektonStat.gravityUse, requiredGravity, TektonStat.gravityPower);
    }
	
	public class FusionReactorBuild extends GeneratorBuild implements GravityConsumer {
		public int gravity = 0;
		public float instability = 0f;
		
        public float smoothLight;
        public float flash;
        public float totalProgress, warmup;
        public float timer = 0f;
		
        public float[] sideGravity = new float[4];
        
		@Override
        public double sense(LAccess sensor) {
            if(sensor == LAccess.heat) return instability;
            return super.sense(sensor);
        }
		
        @Override
        public void updateTile() {
            //super.updateTile();
        	
            gravity = (int)calculateGravity(sideGravity);
            float currentCoolant = items.get(coolantItem);
            float currentFuel = liquids.get(fuelLiquid);
            float gravityFrac = Math.min((float)gravity, (float)requiredGravity) / (float)requiredGravity;
            float fuelFulness = Mathf.equal(currentFuel / liquidCapacity, 1f, 0.002f) ? 1f : currentFuel / liquidCapacity;
            boolean cool = false;
            float coolingMul = minimalCoolant / currentCoolant;
    		boolean prevOut = getPowerProduction() <= consPower.requestedPower(this);
    		
            //Log.info(warmup + ", " + fuelFulness + ", " + gravityFrac);
            
            efficiency = power.status >= 0.99f && currentFuel >= explosionThreshold ? 1f : 0f;
            
            if (gravity >= minGravity) {
            	if (efficiency >= 0.9999f) {
                	liquids.remove(fuelLiquid, fuelConsumption);
                	//consume();

                    warmup = Mathf.lerpDelta(warmup, 1f, warmupSpeed * timeScale);
                    if(Mathf.equal(warmup, 1f, 0.001f)){
                        warmup = 1f;
                    }

                    if(!prevOut && (getPowerProduction() > consPower.requestedPower(this))){
                        Events.fire(Trigger.impactPower);
                    }
                    
                    if (currentCoolant > 0)
                    	timer += delta() * timeScale;
                    
                    if (timer >= itemDuration / gravityFrac) {
                    	timer = 0;
            			items.remove(new ItemStack(coolantItem, 1));
                    }
                    
            		if (currentCoolant <= minimalCoolant) {
            			instability += Math.min(((1f + (currentCoolant / itemCapacity)) * gravityFrac * coolingMul) * heatingPerTick * timeScale * Time.delta, maxHeatingPerTick * Time.delta * timeScale);
            		}
            		else cool = true;
            	}
            	else{
                    warmup = Mathf.lerpDelta(warmup, 0f, 0.01f);
                }
            }
            else{
                warmup = Mathf.lerpDelta(warmup, 0f, 0.01f);
            }
        	
        	//productionEfficiency = fuelFulness;
        	productionEfficiency = Mathf.pow(warmup, 5f) * gravityFrac * fuelFulness;
        	
        	//totalProgress += productionEfficiency * Time.delta;
        	totalProgress += warmup * Time.delta * gravityFrac * fuelFulness;
        	
        	warmup = Mathf.clamp(warmup);
        	instability = Mathf.clamp(instability);
        	
        	if(instability > explosionThreshold) {
            	float smoke = 0.1f + (instability / 1f);
                if(Mathf.chance(smoke / 20.0 * delta())) {
                    Fx.reactorsmoke.at
                    (x + Mathf.range(size * tilesize / 2f),
                    y + Mathf.range(size * tilesize / 2f));
                }
            }
        	
        	if(instability >= 0.999f){
                Events.fire(Trigger.thoriumReactorOverheat);
                kill();
                return;
            }
        	if (cool) {
    			instability -= (currentCoolant / minimalCoolant) * heatingPerTick * Time.delta;
    		}
        }
        
        @Override
        public float ambientVolume() {
            return warmup;
        }
        
        @Override
        public float warmup() {
            return warmup;
        }
        
        @Override
        public float totalProgress() {
            return totalProgress;
        }

        @Override
        public float efficiency() {
            return efficiency;
        }

        @Override
        public float[] sideGravity() {
            return sideGravity;
        }

        @Override
        public float gravityRequirement() {
            return requiredGravity;
        }

		@Override
		public float gravity() {
			return gravity;
		}
        
        @Override
        public float getPowerProduction() {
            return productionEfficiency * powerProduction;
        }
        
        @Override
        public BlockStatus status() {
            float balance = power.status;
            //if(balance > 0.001f && hasAmmo() && !isShooting()) return BlockStatus.noOutput;
            if(balance > 0.001f && gravityFrac() > 0.001f && canConsume()) return BlockStatus.active;
            return BlockStatus.noInput;
        }
        
        @Override
        public void draw() {
            super.draw();
            
        	float layer = Layer.blockAdditive + 0.1f;
            float z = Draw.z();
            if(layer > 0)
            	Draw.z(layer);

            Draw.blend(Blending.additive);
            Draw.color(Color.clear, hotColor, instability);
            Draw.alpha(0.3f);
            Draw.rect(overheatRegion, x, y);
            Draw.blend();
            
            if (instability > explosionThreshold) {
            	if(instability <= 0.001f) return;
                Draw.blend(Blending.additive);
                Draw.color(heatColor);
                Draw.alpha((Mathf.absin(totalProgress(), glowScale, alpha) * glowIntensity + 1f - glowIntensity) * instability * alpha);
                Draw.rect(region, x, y, (rotate ? rotdeg() : 0f));
                Draw.blend();
            }
            
            /*if(instability > explosionThreshold){
                flash += (1f + ((instability - explosionThreshold) / (1f - explosionThreshold)) * 5.4f) * Time.delta;
                Draw.color(Color.red, Color.yellow, Mathf.absin(flash, 9f, 1f));
                Draw.alpha(0.3f);
                Draw.rect(lightsRegion, x, y);
            }*/

            Draw.z(z);
            Draw.blend();
            Draw.reset();
		};
		
		@Override
        public void drawLight() {
            smoothLight = Mathf.lerpDelta(smoothLight, warmup, 0.08f);
            Drawf.light(x, y, (90f + Mathf.absin(5, 5f)) * smoothLight, Tmp.c1.set(lightColor).lerp(Color.scarlet, instability), smoothLight);
        }

        @Override
        public boolean shouldExplode() {
            return super.shouldExplode() && (liquids.get(fuelLiquid) >= liquidCapacity * explosionThreshold || instability >= 0.65f);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(instability);
            write.f(timer);
            write.f(warmup);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            instability = read.f();
            timer = read.f();
            warmup = read.f();
        }
        
        @Override
        public boolean canPickup() {
            return false;
        }
        
        public float calculateGravity(float[] sideGravity){
            return calculateGravity(sideGravity, null);
        }

		@Override
		public float calculateGravity(float[] sideGravity, IntSet cameFrom) {
			return calculateGravity(this, sideGravity, cameFrom);
		}
        
        public float gravityFrac() {
        	return Math.min(gravity() / gravityRequirement(), 1f);
        }
	}
}
