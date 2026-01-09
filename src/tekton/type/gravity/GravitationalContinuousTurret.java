package tekton.type.gravity;

import arc.Core;
import arc.math.Angles;
import arc.math.Mathf;
import arc.struct.IntSet;
import arc.util.Nullable;
import arc.util.Tmp;
import mindustry.ui.Bar;
import mindustry.world.blocks.defense.turrets.ContinuousTurret;
import mindustry.world.meta.BlockStatus;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tekton.content.TektonColor;
import tekton.content.TektonStat;
import tekton.type.gravity.GravitationalTurret.GravitationalTurretBuild;

public class GravitationalContinuousTurret extends ContinuousTurret {
	public int minGravity = 8;
	public int maxGravity = 16;
    public boolean scaleDamageEfficiency = false;
	
	public GravitationalContinuousTurret(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void setBars() {
        super.setBars();
        addBar("gravity", (GravitationalContinuousTurretBuild entity) -> new Bar(
        		() -> Core.bundle.format("bar.gravityPercent", (int)(Math.abs(entity.gravity) + 0.01f), (float)(entity.gravityFrac() * 100)), 
        		() -> TektonColor.gravityColor, 
				() -> entity.gravityFrac()));
    }
	
	@Override
    public void setStats() {
        super.setStats();
        
        stats.add(Stat.maxEfficiency, (maxGravity / minGravity) * 100f, StatUnit.percent);
        stats.add(TektonStat.gravityUse, minGravity, TektonStat.gravityPower);
    }

	public class GravitationalContinuousTurretBuild extends ContinuousTurretBuild implements GravityConsumer {
		public int gravity = 0;
        public float[] sideGravity = new float[4];
        
        @Override
        public void updateTile() {
            gravity = (int)calculateGravity(sideGravity);
            unit.ammo(power.status * unit.type().ammoCapacity * gravityFrac());
            
            super.updateTile();
        }
		
		@Override
		protected void updateBullet(BulletEntry entry){
            float
                bulletX = x + Angles.trnsx(rotation - 90, shootX + entry.x, shootY + entry.y),
                bulletY = y + Angles.trnsy(rotation - 90, shootX + entry.x, shootY + entry.y),
                angle = rotation + entry.rotation;

            entry.bullet.rotation(angle);
            entry.bullet.set(bulletX, bulletY);

            //target length of laser
            float shootLength = Math.min(dst(targetPos), range);
            //current length of laser
            float curLength = dst(entry.bullet.aimX, entry.bullet.aimY);
            //resulting length of the bullet (smoothed)
            float resultLength = Mathf.approachDelta(curLength, shootLength, aimChangeSpeed);
            //actual aim end point based on length
            Tmp.v1.trns(rotation, lastLength = resultLength).add(x, y);

            entry.bullet.aimX = Tmp.v1.x;
            entry.bullet.aimY = Tmp.v1.y;
            if(scaleDamageEfficiency){
                entry.bullet.damage = entry.bullet.type.damage * Math.min(efficiency, 1f) * timeScale * entry.bullet.damageMultiplier();
            }

            if(isShooting() && hasAmmo()){
                entry.bullet.time = entry.bullet.lifetime * entry.bullet.type.optimalLifeFract * Math.min(shootWarmup, efficiency);
                entry.bullet.keepAlive = true;
            }
        }
        
        @Override
        public BlockStatus status() {
            float balance = power.status;
            if(balance > 0.001f && hasAmmo() && !isShooting()) return BlockStatus.noOutput;
            if(balance > 0.001f && hasAmmo() && isShooting()) return BlockStatus.active;
            return BlockStatus.noInput;
        }
        
        @Override
        public boolean hasAmmo(){
            return super.hasAmmo() && gravityFrac() > 0.001f;
        }
        
        @Override
        public float warmup(){
            return shootWarmup;
        }
        
        @Override
        public void updateEfficiencyMultiplier() {
        	super.updateEfficiencyMultiplier();
            efficiency *= gravityFrac();
        }

        @Override
        public float[] sideGravity() {
            return sideGravity;
        }

        @Override
        public float gravityRequirement() {
            return minGravity;
        }
        
        public float gravityFrac() {
        	return ((float)gravity() / (float)minGravity);
        }
        
        public float calculateGravity(float[] sideGravity) {
            return calculateGravity(sideGravity, null);
        }
        
		@Override
		public float calculateGravity(float[] sideGravity, @Nullable IntSet cameFrom) {
			return calculateGravity(this, sideGravity, cameFrom);
		}

		@Override
		public float gravity() {
			return Math.min(gravity, maxGravity);
		}
	}
}
