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
        		() -> Core.bundle.format("bar.gravityPercent", (int)(Math.abs(entity.gravity) + 0.01f), (float)(entity.efficiency() * 100)), 
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
	
	public class GravitationalTurretBuild extends PowerTurretBuild implements GravityConsumer {
		public int gravity = 0;
        public float[] sideGravity = new float[4];
        
    	@Override
        public float estimateDps(){
            if(!hasAmmo()) return 0f;
            return shootType.damage * 60f / (shootType instanceof WaveBulletType c ? c.damageInterval : 5f);
        }
    	
    	/*@Override
        public BlockStatus status(){
            float balance = power.graph.getPowerBalance();
            if(balance > 0f) return BlockStatus.active;
            if(balance < 0f && power.graph.getLastPowerStored() > 0) return BlockStatus.noOutput;
            return BlockStatus.noInput;
        }*/
        
        @Override
        public void updateTile(){
            super.updateTile();
            unit.ammo(power.status * unit.type().ammoCapacity * (hasAmmo() ? 1f : 0f));
            gravity = (int)calculateGravity(sideGravity);
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
        public float warmup(){
            return shootWarmup * Mathf.clamp(gravityFrac());
        }
        
        @Override
        public float efficiency(){
        	return gravityFrac();
        }
        
        public float calculateGravity(float[] sideGravity){
            return calculateGravity(sideGravity, null);
        }
        
        @Override
        protected float baseReloadSpeed(){
            return super.baseReloadSpeed() * efficiency();
        }
        
        @Override
        public double sense(LAccess sensor){
            return switch(sensor){
                case ammo -> power.status * (hasAmmo() ? 1f : 0f);
                case ammoCapacity -> 1;
                default -> super.sense(sensor);
            };
        }
        
        @Override
        public boolean hasAmmo(){
            return gravity >= minGravity;
        }

        @Override
        public float activeSoundVolume(){
            return shootWarmup * (hasAmmo() ? 1f : 0f);
        }

        @Override
        public boolean shouldActiveSound(){
            return shootWarmup * (hasAmmo() ? 1f : 0f) > 0.01f && loopSound != Sounds.none;
        }

		@Override
		public float gravity() {
			return Math.min(gravity, maxGravity);
		}
        
		@Override
		public float calculateGravity(float[] sideGravity, @Nullable IntSet cameFrom) {
			return calculateGravity(this, sideGravity, cameFrom);
		}
	}
}
