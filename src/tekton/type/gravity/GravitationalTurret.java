package tekton.type.gravity;

import java.util.Arrays;

import arc.Core;
import arc.math.Mathf;
import arc.struct.IntSet;
import arc.util.Nullable;
import mindustry.ui.Bar;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.meta.BlockStatus;
import mindustry.world.meta.Env;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tekton.content.*;
import mindustry.Vars;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Sounds;
import mindustry.logic.LAccess;
import tekton.type.bullets.WaveBulletType;
import tekton.type.gravity.GravityConductor.GravityConductorBuild;

public class GravitationalTurret extends PowerTurret {
	public int minGravity = 8;
	public int maxGravity = 16;
	
	public GravitationalTurret(String name) {
		super(name);

        coolantMultiplier = 1f;
        envEnabled |= Env.space;
        displayAmmoMultiplier = false;
        inaccuracy = 0f;
	}
	
	@Override
    public void setBars() {
        super.setBars();
        addBar("gravity", (GravitationalTurretBuild entity) -> new Bar(
        		() -> Core.bundle.format("bar.gravityPercent", (int)(Math.abs(entity.gravity) + 0.01f), (float)(entity.gravityFrac() * 100)), 
        		() -> TektonColor.gravityColor, 
				() -> entity.gravityFrac()));
    }
	
	@Override
    public void setStats() {
        super.setStats();
        
        stats.add(Stat.maxEfficiency, (maxGravity / minGravity) * 100f, StatUnit.percent);
        stats.add(TektonStat.gravityUse, minGravity, TektonStat.gravityPower);

        //stats.remove(Stat.reload);
        stats.remove(Stat.inaccuracy);
    }
	
	public class GravitationalTurretBuild extends TurretBuild implements GravityConsumer {
		public int gravity = 0;
        public float[] sideGravity = new float[4];
        
    	@Override
        public float estimateDps() {
            if(!hasAmmo()) return 0f;
            return shootType.damage * 60f / (shootType instanceof WaveBulletType c ? c.damageInterval : 5f);
        }
    	
    	@Override
        public BlockStatus status() {
            float balance = power.status;
            if(balance > 0.001f && gravityFrac() > 0.001f && !isShooting()) return BlockStatus.noOutput;
            if(balance > 0.001f && gravityFrac() > 0.001f && isShooting()) return BlockStatus.active;
            return BlockStatus.noInput;
        }
        
        @Override
        public void updateTile() {
            gravity = (int)calculateGravity(sideGravity);
            unit.ammo(power.status * unit.type().ammoCapacity * gravityFrac());
            
            super.updateTile();
        }

        @Override
        public float[] sideGravity() {
            return sideGravity;
        }

        @Override
        public float gravityRequirement() {
            return minGravity;
        }
        
        @Override
        public void updateEfficiencyMultiplier() {
        	super.updateEfficiencyMultiplier();
            efficiency *= gravityFrac();
        }
        
        public float gravityFrac() {
        	return ((float)gravity() / (float)minGravity);
        }
        
        @Override
        public float warmup(){
            return shootWarmup * Mathf.clamp(gravityFrac());
        }
        
        public float calculateGravity(float[] sideGravity) {
            return calculateGravity(sideGravity, null);
        }
        
        @Override
        public double sense(LAccess sensor) {
            return switch(sensor){
                case ammo -> Mathf.clamp(power.status * gravityFrac());
                case ammoCapacity -> 1;
                default -> super.sense(sensor);
            };
        }

        @Override
        public float activeSoundVolume() {
            return shootWarmup * gravityFrac();
        }

        @Override
        public boolean shouldActiveSound() {
            return shootWarmup > 0.01f && gravityFrac() > 0.001f && loopSound != Sounds.none && isShooting();
        }
        
        @Override
        public boolean shouldConsume(){
            return (isShooting() || reloadCounter < reload) && gravityFrac() > 0.001f;
        }

		@Override
		public float gravity() {
			return Math.min(gravity, maxGravity);
		}
        
		@Override
		public float calculateGravity(float[] sideGravity, @Nullable IntSet cameFrom) {
			return calculateGravity(this, sideGravity, cameFrom);
		}
		
		@Override
        public BulletType useAmmo() {
            //nothing used directly
            return shootType;
        }

        @Override
        public boolean hasAmmo() {
            //you can always rotate, but never shoot if there's no power
            return true;
        }

        @Override
        public BulletType peekAmmo() {
            return shootType;
        }
	}
}
