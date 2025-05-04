package tekton.type.distanceMissile;

import arc.math.geom.Vec2;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.Time;
import arc.util.io.*;
import ent.anno.Annotations.Import;
import ent.anno.Annotations.MethodPriority;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.TimedKillUnit;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import tekton.content.TektonUnits;

public class DistanceMissileUnitEntity extends TimedKillUnit {
	float maxDistance = 10f * Vars.tilesize, distance = 0f;
	private Vec2 initialPos;
	private boolean firstUpdate = true;
    
	@Override
    public int classId() {
        return TektonUnits.mapMissileDist;
    }
	
	public static DistanceMissileUnitEntity create() {
        return new DistanceMissileUnitEntity();
    }
	
	protected DistanceMissileUnitEntity() {
    }

    //called last so pooling and removal happens then.
    @MethodPriority(100)
    @Override
    public void update(){
    	super.update();
    	if (firstUpdate) {
    		firstUpdate = false;
    		initialPos = new Vec2(getX(), getY());
    		//DistanceMissileUnitType is needed for maxDistance to be set.
    		if (this.type instanceof DistanceMissileUnitType)
    			maxDistance = ((DistanceMissileUnitType)this.type).maxDistance;
    		return;
    	}
    	
        distance = new Vec2(getX(), getY()).dst(initialPos);

    	if(distance >= maxDistance || time > lifetime){
    		kill();
        }
    }
    
    @Override
    public float range() {
		return maxDistance;
    }

    @Override
    public float fin(){
        return ((distance / maxDistance) + (time / lifetime)) / 2f;
    }
    
    @Override
    public void write(Writes arg0) {
    	super.write(arg0);
    	arg0.bool(firstUpdate);
    	arg0.f(initialPos.x);
    	arg0.f(initialPos.y);
    	arg0.f(maxDistance);
    }

    @Override
    public void read(Reads arg0) {
    	super.read(arg0);
    	firstUpdate = arg0.bool();
    	var x = arg0.f();
    	var y = arg0.f();
    	initialPos = new Vec2(x, y);
    	maxDistance = arg0.f();
    }
}
