package tekton;

import java.util.Arrays;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
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
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.ui.Bar;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.draw.*;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.Env;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tekton.content.TektonColor;
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
    public float fuelConsumption = 6f / 60f;
    public float warmupSpeed = 0.001f;
	public int requiredGravity = 48;
	public int minGravity = requiredGravity / 2;
	public int maxGravity = requiredGravity;
	public int minimalCoolant = 2;
	
	public float ticksToExplosion = 5f * 60f;
	public float explosionThreshold = 0.5f;

    public TextureRegion lightsRegion;
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
        explodeEffect = Fx.impactReactorExplosion;
        explodeSound = Sounds.explosionbig;
	}
	
	@Override
    public void load(){
        super.load();
        
        lightsRegion = Core.atlas.find(name + "-lights");
        overheatRegion = Core.atlas.find(name + "-overheat");
    }
	
	@Override
    public void setBars(){
        super.setBars();

        addBar("power", (GeneratorBuild entity) -> new Bar(
        		() -> Core.bundle.format("bar.poweroutput", Strings.fixed(Math.max(entity.getPowerProduction() - consPower.usage, 0) * 60 * entity.timeScale(), 1)),
        		() -> Pal.powerBar,
        		() -> entity.productionEfficiency));
        addBar("instability", (FusionReactorBuild entity) -> new Bar("bar.instability", Pal.negativeStat, () -> entity.instability));
        addBar("gravity", (FusionReactorBuild entity) -> new Bar(
        		() -> Core.bundle.format("bar.gravityPercent", Math.min(maxGravity, entity.gravity), (float)((maxGravity / requiredGravity) * 100)), 
        		() -> TektonColor.gravityColor, 
				() -> (float)entity.gravity / (float)requiredGravity));
    }
	
	@Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.maxEfficiency, (maxGravity / requiredGravity) * 100f, StatUnit.percent);
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
        public double sense(LAccess sensor){
            if(sensor == LAccess.heat) return instability;
            return super.sense(sensor);
        }
		
        @Override
        public void updateTile(){
            //super.updateTile();
        	
            gravity = (int)calculateGravity(sideGravity);
            float currentCoolant = items.get(coolantItem);
            float currentFuel = liquids.get(fuelLiquid);
            float gravityFrac = Math.min((float)gravity, (float)maxGravity) / (float)requiredGravity;
            float fuelFulness = currentFuel / liquidCapacity;
            boolean cool = false;
    		boolean prevOut = getPowerProduction() <= consPower.requestedPower(this);
    		
            Log.info(efficiency + ", " + power.status);
            
            if (gravity >= minGravity) {
            	liquids.remove(fuelLiquid, fuelConsumption);
                
            	if (efficiency >= 0.9999f && power.status >= 0.99f && currentFuel >= 0.01f) {

                    warmup = Mathf.lerpDelta(warmup, 1f, warmupSpeed * timeScale);
                    if(Mathf.equal(warmup, 1f, 0.005f)){
                        warmup = 1f;
                    }

                    if(!prevOut && (getPowerProduction() > consPower.requestedPower(this))){
                        Events.fire(Trigger.impactPower);
                    }
                    
                    if (currentCoolant > 0)
                    	timer += delta();
                    
                    if (timer >= itemDuration / gravityFrac) {
                    	timer = 0;
            			items.remove(new ItemStack(coolantItem, 1));
                    	consume();
                    }
                    
            		if (currentCoolant <= minimalCoolant) {
            			instability += (1f / ticksToExplosion) * (1f + (currentCoolant / itemCapacity)) * gravityFrac;
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
        	productionEfficiency = Mathf.pow(warmup * fuelFulness, 5f) * gravityFrac;
        	
        	//totalProgress += productionEfficiency * Time.delta;
        	totalProgress += warmup * Time.delta * gravityFrac;
        	
        	instability = Mathf.clamp(instability);
        	
        	if(instability >= 0.999f){
                Events.fire(Trigger.thoriumReactorOverheat);
                kill();
                return;
            }
        	if (cool) {
    			instability -= (1f / ticksToExplosion);
    		}
        }
        
        @Override
        public float ambientVolume(){
            return warmup;
        }
        
        @Override
        public float warmup(){
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
        public float[] sideGravity(){
            return sideGravity;
        }

        @Override
        public float gravityRequirement(){
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
        public void drawLight() {
            float fract = productionEfficiency;
            smoothLight = Mathf.lerpDelta(smoothLight, fract, 0.08f);
            Drawf.light(x, y, (90f + Mathf.absin(5, 5f)) * smoothLight, Tmp.c1.set(lightColor).lerp(Color.scarlet, instability), 0.6f * smoothLight);
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
        
        public float calculateGravity(float[] sideGravity, @Nullable IntSet cameFrom){
            Arrays.fill(sideGravity, 0f);
            if(cameFrom != null) cameFrom.clear();

            float gravityAmmount = 0f;

            for(var build : proximity){
                if(build != null && build.team == team && build instanceof GravityBlock graviter && build != this){
                    boolean split = build.block instanceof GravityConductor cond && cond.splitGravity;
                    // non-routers must face us, routers must face away - next to a redirector, they're forced to face away due to cycles anyway
                    if(!build.block.rotate || (!split && (relativeTo(build) + 2) % 4 == build.rotation) || (split && relativeTo(build) != build.rotation)){ //TODO hacky

                        //if there's a cycle, ignore its gravity
                        if(!(build instanceof GravityConductorBuild hc && hc.cameFrom.contains(id()))){
                            //x/y coordinate difference across point of contact
                            float diff = (Math.min(Math.abs(build.x - x), Math.abs(build.y - y)) / Vars.tilesize);
                            //number of points that this block had contact with
                            int contactPoints = Math.min((int)(block.size/2f + build.block.size/2f - diff), Math.min(build.block.size, block.size));

                            //gravity is distributed across building size
                            float add = graviter.gravity() / build.block.size * contactPoints;
                            if(split){
                                //gravity routers split gravity across 3 surfaces
                                add /= 3f;
                            }

                            sideGravity[Mathf.mod(relativeTo(build), 4)] += add;
                            gravityAmmount += add;
                        }

                        //register traversed cycles
                        if(cameFrom != null){
                            cameFrom.add(build.id);
                            if(build instanceof GravityConductorBuild gc){
                                cameFrom.addAll(gc.cameFrom);
                            }
                        }

                        //massive hack but I don't really care anymore
                        if(graviter instanceof GravityConductorBuild cond){
                            cond.updateGravity();
                        }
                    }
                }
            }
            return gravityAmmount;
        }
	}
}
