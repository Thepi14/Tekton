package tekton.type.gravity;

import java.util.Arrays;

import arc.Core;
import arc.math.Mathf;
import arc.struct.IntSet;
import arc.util.Nullable;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.ui.Bar;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tekton.Tekton;
import tekton.content.TektonColor;
import tekton.content.TektonStat;
import tekton.type.gravity.GravityConductor.GravityConductorBuild;

public class GravityCrafter extends GenericCrafter {
	public int requiredGravity = 2;
	public int maxGravity = requiredGravity * 4;
	
	public GravityCrafter(String name) {
		super(name);
		
	}
	
	@Override
    public void setBars() {
        super.setBars();
        addBar("gravity", (GravityCrafterBuild entity) -> new Bar(
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

	public class GravityCrafterBuild extends GenericCrafterBuild implements GravityConsumer {
		public int gravity = 0;
        public float[] sideGravity = new float[4];
        
        @Override
        public void updateTile() {
        	super.updateTile();
        	gravity = Math.min((int)calculateGravity(sideGravity), maxGravity);
        }
        
        public float gravityFrac() {
        	return gravity() / gravityRequirement();
        }
        
        @Override
        public float efficiency() {
        	return efficiency * gravityFrac();
        }
        
        @Override
        public float warmupTarget(){
            return Mathf.clamp(gravityFrac());
        }

        @Override
        public float efficiencyScale(){
            return gravityFrac();
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
