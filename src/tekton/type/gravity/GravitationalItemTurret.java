package tekton.type.gravity;

import java.util.Arrays;

import arc.Core;
import arc.math.Mathf;
import arc.struct.IntSet;
import arc.util.Nullable;
import mindustry.Vars;
import mindustry.ui.Bar;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.meta.BlockStatus;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tekton.content.TektonColor;
import tekton.content.TektonStat;
import tekton.type.gravity.GravitationalTurret.GravitationalTurretBuild;
import tekton.type.gravity.GravityConductor.GravityConductorBuild;

public class GravitationalItemTurret extends ItemTurret {
	public int minGravity = 8;
	public int maxGravity = 16;

	public GravitationalItemTurret(String name) {
		super(name);
	}
	
	@Override
    public void setBars() {
        super.setBars();
        addBar("gravity", (GravitationalItemTurretBuild entity) -> new Bar(
        		() -> Core.bundle.format("bar.gravityPercent", (int)(Math.abs(entity.gravity) + 0.01f), (int)(entity.gravityFrac() * 100)), 
        		() -> TektonColor.gravityColor, 
				() -> ((float)Math.abs(entity.gravity)) / (float)minGravity));
    }
	
	@Override
    public void setStats(){
        super.setStats();
        
        stats.add(Stat.maxEfficiency, (maxGravity / minGravity) * 100f, StatUnit.percent);
        stats.add(TektonStat.gravityUse, minGravity, TektonStat.gravityPower);

        //stats.remove(Stat.reload);
        stats.remove(Stat.inaccuracy);
    }
	
	public class GravitationalItemTurretBuild extends ItemTurretBuild implements GravityConsumer {
		public int gravity = 0;
        public float[] sideGravity = new float[4];
        
        @Override
        public void updateTile(){
            gravity = (int)calculateGravity(sideGravity);
            super.updateTile();
        }
        
        @Override
        public BlockStatus status() {
            float balance = power.status;
            if(balance > 0.001f && hasAmmo() && !isShooting()) return BlockStatus.noOutput;
            if(balance > 0.001f && hasAmmo() && isShooting()) return BlockStatus.active;
            return BlockStatus.noInput;
        }
        
        @Override
        public float warmup(){
            return super.warmup() * Mathf.clamp(gravityFrac());
        }

        @Override
        public float efficiency(){
        	return super.efficiency() * gravityFrac();
        }
        
        @Override
        public boolean shouldConsume() {
            return super.shouldConsume() && gravityFrac() > 0.001f;
        }
        
        /*@Override
        public void updateEfficiencyMultiplier() {
        	super.updateEfficiencyMultiplier();
            efficiency *= Math.min(Math.max(gravityFrac(), cheating() ? 1f : 0f), maxGravity / minGravity);
        }*/
        
        @Override
        public boolean hasAmmo(){
            return super.hasAmmo() && gravity >= minGravity;
        }

        @Override
        public float[] sideGravity(){
            return sideGravity;
        }

        @Override
        public float gravityRequirement(){
            return minGravity;
        }
        
        public float gravityFrac() {
        	return ((float)gravity() / (float)minGravity);
        }

		@Override
		public float gravity() {
			return Math.min(gravity, maxGravity);
		}
        
        public float calculateGravity(float[] sideGravity){
            return calculateGravity(sideGravity, null);
        }
		
        @Override
		public float calculateGravity(float[] sideGravity, @Nullable IntSet cameFrom) {
			return calculateGravity(this, sideGravity, cameFrom);
		}
	}
}
