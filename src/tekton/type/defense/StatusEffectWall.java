package tekton.type.defense;

import static mindustry.Vars.*;

import arc.math.Mathf;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.type.StatusEffect;
import mindustry.world.blocks.defense.Wall;

public class StatusEffectWall extends Wall {
	public StatusEffect status = StatusEffects.slow;
	public float statusRadius = 2f * tilesize;
	public float statusDuration = 40f;
	public float statusReload = 20f;
	public Effect statusEffect = Fx.none;
	public Effect applyStatusEffect = Fx.none;
	public boolean hitGround = true, hitAir = false;
	
	public StatusEffectWall(String name) {
		super(name);
        update = true;
	}
	
	public class StatusEffectWallBuild extends WallBuild {
		public float charge = 0f;
		
		@Override
		public void updateTile(){
			charge += Time.delta * timeScale * efficiency;
			if (charge >= statusReload) {
				charge = 0;
				Units.nearbyEnemies(team, x, y, statusRadius, other -> {
					if(team != other.team && other.hittable() && ((hitGround && other.isGrounded()) || (hitAir && other.isFlying()))) {
						other.apply(status, statusDuration);
						applyStatusEffect.at(other);
	                }
                });
				statusEffect.at(this);
			}
		}
		
        @Override
        public void write(Writes write){
            super.write(write);
            write.f(charge);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            charge = read.f();
        }
	}
}
