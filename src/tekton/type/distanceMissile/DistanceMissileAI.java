package tekton.type.distanceMissile;

import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.ai.types.MissileAI;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.unit.*;

public class DistanceMissileAI extends MissileAI{

    /*@Override
    public void updateMovement(){
        unloadPayloads();

        float time = unit instanceof TimedKillc t ? t.time() : 1000000f;

        if(time >= unit.type.homingDelay && shooter != null && !shooter.dead()){
            unit.lookAt(shooter.aimX, shooter.aimY);
        }

        //move forward forever
        unit.moveAt(vec.trns(unit.rotation, unit.type.missileAccelTime <= 0f ? unit.speed() : Mathf.pow(Math.min(time / unit.type.missileAccelTime, 1f), 2f) * unit.speed()));

        var build = unit.buildOn();

        //kill instantly on enemy building contact
        if(build != null && build.team != unit.team && (build == target || !build.block.underBullets)){
            unit.kill();
        }
    }*/

    /*@Override
    public Teamc target(float x, float y, float range, boolean air, boolean ground){
        return Units.closestTarget(unit.team, x, y, 1f, 
        		
        		u -> u.checkTarget(air, ground) && 
        		//!(u.type instanceof MissileUnitType) && 
        		!(u.type instanceof DistanceMissileUnitType), 
        		
        		t -> ground && 
        		(!t.block.underBullets || (shooter != null 
        		&& t == Vars.world.buildWorld(shooter.aimX, shooter.aimY))));
    }

    @Override
    public boolean retarget(){
        //more frequent retarget due to high speed. TODO won't this lag?
        return timer.get(timerTarget, 4f);
    }*/
}