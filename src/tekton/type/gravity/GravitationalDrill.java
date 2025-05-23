package tekton.type.gravity;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.IntSet;
import arc.util.Log;
import arc.util.Nullable;
import mindustry.graphics.Layer;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.blocks.production.Drill;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tekton.Tekton;
import tekton.content.TektonColor;
import tekton.content.TektonStat;

public class GravitationalDrill extends Drill {
	public int requiredGravity = 2;
	public int maxGravity = requiredGravity * 2;
	public TextureRegion gravityRegion;

	public GravitationalDrill(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void setBars() {
        super.setBars();
        addBar("gravity", (GravitationalDrillBuild entity) -> new Bar(
        		() -> Core.bundle.format("bar.gravityPercent", (int)(Math.abs(entity.gravity) + 0.01f), (int)(entity.gravityFrac() * 100)), 
        		() -> TektonColor.gravityColor, 
				() -> entity.gravityFrac()));
    }
    
    @Override
    public void setStats(){
        super.setStats();
        
        stats.add(Stat.maxEfficiency, (maxGravity / requiredGravity) * 100f, StatUnit.percent);
        stats.add(TektonStat.gravityUse, requiredGravity, TektonStat.gravityPower);
    }

    @Override
    public void load(){
    	super.load();
    	gravityRegion = Core.atlas.find(name + "-gravity");
    }
	
	public class GravitationalDrillBuild extends DrillBuild implements GravityConsumer {
	    public float gravityPulse = 0.3f, gravityPulseScl = 10f;
		public int gravity = 0;
        public float[] sideGravity = new float[4];
        private float gravBoost = 0f;
        
        @Override
        public void updateTile() {
        	gravity = Math.min((int)calculateGravity(sideGravity), maxGravity);
        	
        	if(timer(timerDump, dumpTime)){
                dump(dominantItem != null && items.has(dominantItem) ? dominantItem : null);
            }

            if(dominantItem == null){
                return;
            }
            
            gravBoost = Mathf.approachDelta(gravBoost, gravityFrac() >= 0.999f ? gravityFrac() : 0f, warmupSpeed);

            timeDrilled += (warmup * delta()) * gravBoost;

            float delay = getDrillTime(dominantItem);

            if(items.total() < itemCapacity && dominantItems > 0 && efficiency > 0) {
                float speed = Mathf.lerp(1f, liquidBoostIntensity + (gravBoost - 1f), optionalEfficiency) * efficiency;

                lastDrillSpeed = (speed * dominantItems * warmup) / delay;
                warmup = Mathf.approachDelta(warmup, speed, warmupSpeed);
                progress += delta() * dominantItems * speed * warmup;

                if(Mathf.chanceDelta(updateEffectChance * warmup))
                    updateEffect.at(x + Mathf.range(size * 2f), y + Mathf.range(size * 2f));
            }else {
                lastDrillSpeed = 0f;
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
                return;
            }

            if(dominantItems > 0 && progress >= delay && items.total() < itemCapacity) {
                int amount = (int)(progress / delay);
                for(int i = 0; i < amount; i++) {
                    offload(dominantItem);
                }

                progress %= delay;

                if(wasVisible && Mathf.chanceDelta(drillEffectChance * warmup)) drillEffect.at(x + Mathf.range(drillEffectRnd), y + Mathf.range(drillEffectRnd), dominantItem.color);
            }
        }
        
        @Override
        public void draw() {
        	super.draw();
        	
        	if (!gravityRegion.found())
        		return;
        	Draw.z(Layer.blockAdditive);
        	
        	float[] side = sideGravity();
        	for(int i = 0; i < 4; i++){
                if(side[i] > 0){
                    Draw.blend(Blending.additive);
                    Draw.color(TektonColor.gravityColor, side[i] / gravityRequirement() * (TektonColor.gravityColor.cpy().a * (1f - gravityPulse + Mathf.absin(gravityPulseScl, gravityPulse))));
                    Draw.rect(gravityRegion, x, y, i * 90f);
                    Draw.blend();
                    Draw.color();
                }
            }
            
            Draw.z(Layer.block);
        	
        	Draw.reset();
        }
        
        @Override
        public boolean shouldConsume(){
            return super.shouldConsume() && gravityFrac() >= 0.999f;
        }
        
        public float gravityFrac() {
        	return gravity() / gravityRequirement();
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
			return Math.min(gravity, maxGravity);
		}
		
        public float calculateGravity(float[] sideGravity){
            return calculateGravity(sideGravity, null);
        }
		
		public float calculateGravity(float[] sideGravity, @Nullable IntSet cameFrom) {
			return calculateGravity(this, sideGravity, cameFrom);
		}
	}
}
