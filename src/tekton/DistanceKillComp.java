package tekton;

import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.type.UnitType;
import tekton.content.*;
import ent.anno.proc.*;
import ent.anno.*;

//basically just TimedKillComp but based on distance and time.
@EntityComponent
abstract class DistanceKillComp implements Unitc, Entityc, Healthc, Scaled {
    @Import UnitType type;
    @Import Team team;
    
    //spawner unit cannot be read directly for technical reasons.
    public transient @Nullable Unit spawner;
    public int spawnerUnitId = -1;

    float time = 0f, lifetime = 3f * 60f;
	float maxDistance = 10f * Vars.tilesize, distance = 0f;
	
	/*@Override
	public int classId(){
		return EntityRegister.classID(1);
	}*/

    //called last so pooling and removal happens then.
    @MethodPriority(100)
    @Override
    public void update(){
    	var SpawnerVec = new Vec2(spawner.getX(), spawner.getY());
        distance = new Vec2(getX(), getY()).dst(SpawnerVec);
        time = Math.min(time + Time.delta, lifetime);

    	if(spawner == null || !spawner.isValid() || spawner.team != team || distance >= maxDistance || time >= lifetime){
    		kill();
        }else{
            spawnerUnitId = spawner.id;
        }
    }

    @Override
    public float fin(){
        return ((distance / maxDistance) + (time / lifetime)) / 2f;
    }
    
    @Override
    public void afterRead(){
        if(spawnerUnitId != -1) spawner = Groups.unit.getByID(spawnerUnitId);
        spawnerUnitId = -1;
    }

    @Override
    public void afterSync(){
        if(spawnerUnitId != -1) spawner = Groups.unit.getByID(spawnerUnitId);
        spawnerUnitId = -1;
    }
}