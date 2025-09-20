package tekton.type.dependent;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.entities.part.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import tekton.type.ai.DistanceMissileAI;
import tekton.type.ai.MinionAI;
import mindustry.entities.bullet.BulletType;

import static mindustry.Vars.*;

public class DependentBulletType extends BulletType {
	public @Nullable Bullet create(
	        @Nullable Entityc owner, @Nullable Entityc shooter, Team team, float x, float y, float angle, float damage, float velocityScl,
	        float lifetimeScl, Object data, @Nullable Mover mover, float aimX, float aimY, @Nullable Teamc target
	    ){
	        //angle += angleOffset + Mathf.range(randomAngleOffset);

	        if(!Mathf.chance(createChance)) return null;
	        if(ignoreSpawnAngle) angle = 0;
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
	                if(spawned.controller() instanceof MinionAI ai){
	                    if(shooter instanceof Unit unit){
	                        ai.shooter = unit;
	                    }
	                    if(shooter instanceof ControlBlock control){
	                        ai.shooter = control.unit();
	                    }
	                }
	                if (spawned.controller() instanceof DistanceMissileAI ai) {
	                	if(shooter instanceof Unit unit){
	                        ai.shooter = unit;
	                    }
	                    if(shooter instanceof ControlBlock control){
	                        ai.shooter = control.unit();
	                    }
	                }
	                spawned.add();
	            }
	            //Since bullet init is never called, handle killing shooter here
	            if(killShooter && owner instanceof Healthc h && !h.dead()) h.kill();

	            //no bullet returned
	            return null;
	        }
	        return null;
	    }
}
