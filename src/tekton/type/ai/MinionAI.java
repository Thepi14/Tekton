package tekton.type.ai;

import arc.math.*;
import arc.math.geom.Vec2;
import arc.util.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import tekton.type.dependent.DependentAI;
import tekton.type.dependent.DependentType;
import tekton.type.dependent.DistanceMissileUnitType;
import tekton.type.dependent.MinionUnitType;

import static mindustry.Vars.*;

public class MinionAI extends AIController implements DependentAI {
	 public @Nullable Unit shooter;
	 public float attackDistanceMul = 0.8f;

    /*@Override
    protected void resetTimers(){
        timer.reset(timerTarget, 5f);
    }*/

    @Override
    public void updateMovement(){
        unloadPayloads();

        if (unit.type instanceof DependentType typ) {
            float time = unit instanceof TimedKillc t ? t.time() : 1000000f;
            float distance = shooter != null ? new Vec2(shooter.x, shooter.y).dst(this.unit) : typ.maxDistance() + 1f;
            float distanceTarget = shooter != null ? new Vec2(unit.x, unit.y).dst(new Vec2(shooter.aimX, shooter.aimY)) : 0f;
            float attackDistance = unit.type.range * attackDistanceMul;
            
            //remove before missile kills itself
            if (distance > typ.maxDistance() || time >= unit.type.lifetime - 0.5f) {
            	if (unit.type instanceof MinionUnitType v) {
            		v.despawnEffect.at(unit);
            	}
            	unit.remove();
            	return;
            }

            if (shooter != null) {
            	if(time >= unit.type.homingDelay && !shooter.dead()){
    	            unit.lookAt(shooter.aimX, shooter.aimY);
    	        }
                
                unit.aimX = shooter.aimX;
                unit.aimY = shooter.aimY;
            	
            	if (distanceTarget >= attackDistance)
            		moveTo(new Vec2(shooter.aimX, shooter.aimY), attackDistance, 0.5f, true, null, true);
            	else
            		moveTo(new Vec2(shooter.aimX, shooter.aimY), attackDistance, 0.5f, true, null, false);
            }
            else {
            	unit.moveAt(vec.trns(unit.rotation, unit.type.missileAccelTime <= 0f ? unit.speed() : Mathf.pow(Math.min(time / unit.type.missileAccelTime, 1f), 2f) * unit.speed()));
            }
        }
        else
        	Log.info("not attached to a DependentType!");
    }
    
    public float prefSpeed(){
        return unit.speed();
    }
    
    public void moveTo(Vec2 target, float circleLength, float smooth, boolean keepDistance, @Nullable Vec2 offset, boolean arrive){
        if(target == null) return;
        
        float speed = prefSpeed();

        vec.set(target).sub(unit);

        float length = circleLength <= 0.001f ? 1f : Mathf.clamp((unit.dst(target) - circleLength) / smooth, -1f, 1f);

        vec.setLength(speed * length);

        if(arrive){
            Tmp.v3.set(-unit.vel.x / unit.type.accel * 2f, -unit.vel.y / unit.type.accel * 2f).add((target.getX() - unit.x), (target.getY() - unit.y));
            vec.add(Tmp.v3).limit(speed * length);
        }

        if(length < -0.5f){
            if(keepDistance){
                vec.rotate(180f);
            }else{
                vec.setZero();
            }
        }else if(length < 0){
            vec.setZero();
        }

        if(offset != null){
            vec.add(offset);
            vec.setLength(speed * length);
        }

        //ignore invalid movement values
        if(vec.isNaN() || vec.isInfinite() || vec.isZero()) return;

        if(!unit.type.omniMovement && unit.type.rotateMoveFirst){
            float angle = vec.angle();
            unit.lookAt(angle);
            if(Angles.within(unit.rotation, angle, 3f)){
                unit.movePref(vec);
            }
        }else{
            unit.movePref(vec);
        }
    }

    @Override
    public Teamc target(float x, float y, float range, boolean air, boolean ground){
        return Units.closestTarget(unit.team, x, y, range, u -> u.checkTarget(air, ground) && !(u instanceof TimedKillc), t -> ground && (!t.block.underBullets || (shooter != null && t == Vars.world.buildWorld(shooter.aimX, shooter.aimY))));
    }

    @Override
    public boolean retarget(){
        //more frequent retarget due to high speed. TODO won't this lag?
        return timer.get(timerTarget, 4f);
    }

	@Override
	public Unit shooter() {
		return shooter;
	}

	@Override
	public void setShooter(Unit unit) {
		shooter = unit;
	}
}
