package tekton.type.dependent;

import static mindustry.Vars.net;

import arc.math.Mathf;
import arc.util.Nullable;
import mindustry.entities.Mover;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Bullet;
import mindustry.gen.Entityc;
import mindustry.gen.Healthc;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.world.blocks.ControlBlock;

public class DependentBulletType extends BulletType {
	public @Nullable Bullet create(
	        @Nullable Entityc owner, @Nullable Entityc shooter, Team team, float x, float y, float angle, float damage, float velocityScl,
	        float lifetimeScl, Object data, @Nullable Mover mover, float aimX, float aimY, @Nullable Teamc target
	    ){
	        //angle += angleOffset + Mathf.range(randomAngleOffset);

	        if(!Mathf.chance(createChance)) {
				return null;
			}
	        if(ignoreSpawnAngle) {
				angle = 0;
			}
	        if(spawnUnit != null){
	            //don't spawn units clientside!
	            if(!net.client()){
	                Unit spawned = spawnUnit.create(team);
	                spawned.set(x, y);
	                spawned.rotation = angle;
	                //immediately spawn at top speed, since it was launched
	                if(spawnUnit.missileAccelTime <= 0f){
	                    spawned.vel.trns(angle, spawnUnit.speed);
	                }
	                //assign unit owner
	                if(spawned.controller() instanceof DependentAI ai){
	                    if(shooter instanceof Unit unit){
	                        ai.setShooter(unit);
	                    }
	                    if(shooter instanceof ControlBlock control){
	                        ai.setShooter(control.unit());
	                    }
	                }
	                spawned.add();
	            }
	            //Since bullet init is never called, handle killing shooter here
	            if(killShooter && owner instanceof Healthc h && !h.dead()) {
					h.kill();
				}

	            //no bullet returned
	            return null;
	        }
	        return null;
	    }
}
