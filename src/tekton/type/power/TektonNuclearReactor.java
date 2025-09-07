package tekton.type.power;

import static mindustry.Vars.*;

import arc.Core;
import arc.Events;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.struct.EnumSet;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.content.Liquids;
import mindustry.entities.Effect;
import mindustry.game.EventType.Trigger;
import mindustry.gen.Sounds;
import mindustry.graphics.*;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.ui.Bar;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.Env;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tekton.content.*;

public class TektonNuclearReactor extends PowerGenerator {
    public final int timerFuel = timers++;
	
	public float heating = 0.005f;
    public float itemDuration = 120;
    public float fuelThreshold = 0.35f;
    public float heatThreshold = 0.35f;
    public float coolantThreshold = 0.2f;
    public float overheatEfficiency = 1f;
    
    public Color heatColor = Pal.lightFlame;
    public Color hotColor = Color.valueOf("ff9575a3");

    public float alpha = 0.9f, glowScale = 10f, glowIntensity = 0.5f;
    public float statusDuration = 60f * 180f;
    
    public Item fuelItem = TektonItems.uranium;
    public Liquid coolantLiquid = Liquids.water;
    public Effect generateEffect = Fx.none;

    public TextureRegion lightsRegion;
    public TextureRegion overheatRegion;
	
	public TektonNuclearReactor(String name) {
		super(name);
        itemCapacity = 30;
        liquidCapacity = 30;
        hasItems = true;
        hasLiquids = true;
        rebuildable = false;
        schematicPriority = -5;
        flags = EnumSet.of(BlockFlag.reactor, BlockFlag.generator);
        envEnabled = Env.any;

        explosionShake = 6f;
        explosionShakeDuration = 16f;

        explosionRadius = 19;
        explosionDamage = 1250 * 4;

        explodeEffect = TektonFx.nuclearExplosion.wrap(Color.valueOf("bbd658"));
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

        addBar("instability", (TektonNuclearReactorBuild entity) -> new Bar("bar.instability", Pal.negativeStat, () -> entity.heat));
    }
	
	@Override
    public void setStats(){
        super.setStats();
        
        stats.add(Stat.productionTime, itemDuration / 60f, StatUnit.seconds);
        stats.add(Stat.maxEfficiency, overheatEfficiency * 200f, StatUnit.percent);
    }
	
	public class TektonNuclearReactorBuild extends GeneratorBuild {
		public float heat = 0f;
        public float smoothLight;
        public float flash;
        public float totalProgress;
		
		@Override
        public double sense(LAccess sensor){
            if(sensor == LAccess.heat) return heat;
            return super.sense(sensor);
        }
		
        @Override
        public void updateTile(){
            int fuel = items.get(fuelItem);
            float fullness = (float)fuel / itemCapacity;
            float coolant = liquids.get(coolantLiquid);
            float coolantFullness = coolant / liquidCapacity;
            productionEfficiency = fullness * (fullness > fuelThreshold ? 1f : 0f);
            
            if (fullness >= fuelThreshold) {
            	if(timer(timerFuel, itemDuration / timeScale)) {
                    consume();
                    if (generateEffect != Fx.none && generateEffect != null)
                    		generateEffect.at(this);
                }
            	if (coolantFullness >= coolantThreshold) {
                	heat -= (heating / 2f) * ((coolantFullness - coolantThreshold) + 0.1f) * timeScale;
                }
            	else {
                	heat += heating * (coolantThreshold - coolantFullness) * timeScale;
                }
            }
            else {
            	heat -= (heating / 2f) * (coolantFullness + 0.1f) * timeScale;
            }
            
            if(heat >= heatThreshold) {
            	float smoke = 0.1f + (heat / 1f);
                if(Mathf.chance(smoke / 20.0 * delta())) {
                    Fx.reactorsmoke.at
                    (x + Mathf.range(size * tilesize / 2f),
                    y + Mathf.range(size * tilesize / 2f));
                }
            }

            totalProgress += productionEfficiency * Time.delta * timeScale * (1f + (heat * overheatEfficiency));
            heat = Mathf.clamp(heat);
            
            if(heat >= 0.999f){
                Events.fire(Trigger.thoriumReactorOverheat);
                kill();
            }
        };
		
		@Override
        public void draw() {
            super.draw();
            
        	float layer = Layer.blockAdditive + 0.1f;
            float z = Draw.z();
            if(layer > 0)
            	Draw.z(layer);

            Draw.blend(Blending.additive);
            Draw.color(Color.clear, hotColor, heat);
            Draw.alpha(0.3f);
            Draw.rect(overheatRegion, x, y);
            Draw.blend();
            
            if (heat > heatThreshold) {
            	if(heat <= 0.001f) return;
                Draw.blend(Blending.additive);
                Draw.color(heatColor);
                Draw.alpha((Mathf.absin(totalProgress(), glowScale, alpha) * glowIntensity + 1f - glowIntensity) * heat * alpha);
                Draw.rect(region, x, y, (rotate ? rotdeg() : 0f));
                Draw.blend();
            }
            
            if(heat > heatThreshold){
                flash += (1f + ((heat - heatThreshold) / (1f - heatThreshold)) * 5.4f) * Time.delta;
                Draw.color(Color.red, Color.yellow, Mathf.absin(flash, 9f, 1f));
                Draw.alpha(0.3f);
                Draw.rect(lightsRegion, x, y);
            }

            Draw.z(z);
            Draw.blend();
            Draw.reset();
		};
		
		@Override
        public float totalProgress() {
            return totalProgress;
        }
        
        @Override
        public float getPowerProduction() {
            return (productionEfficiency + (heat * overheatEfficiency)) * powerProduction;
        }
		
		@Override
        public void drawLight() {
            float fract = productionEfficiency;
            smoothLight = Mathf.lerpDelta(smoothLight, fract, 0.08f);
            Drawf.light(x, y, (90f + Mathf.absin(5, 5f)) * smoothLight, Tmp.c1.set(lightColor).lerp(Color.scarlet, heat), 0.6f * smoothLight);
        }

        @Override
        public boolean shouldExplode() {
            return super.shouldExplode() && (items.get(fuelItem) >= itemCapacity / 3f || heat >= 0.5f);
        }

        @Override
        public void kill() {
        	super.kill();
        	int fuel = items.get(fuelItem);
            float fullness = (float)fuel / itemCapacity;
        	TektonDamage.createRadiationArea(this, statusDuration, (float)explosionRadius * fullness * (float)tilesize);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(heat);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            heat = read.f();
        }
        
        @Override
        public boolean canPickup() {
            return false;
        }
	}
}
