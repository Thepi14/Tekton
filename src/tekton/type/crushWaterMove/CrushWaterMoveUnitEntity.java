package tekton.type.crushWaterMove;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import static mindustry.Vars.*;
import mindustry.gen.UnitWaterMove;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import tekton.content.TektonUnits;

public class CrushWaterMoveUnitEntity extends UnitWaterMove {

	@Override
    public int classId() {
        return TektonUnits.mapCrushWaterMov;
    }
	
	public static CrushWaterMoveUnitEntity create() {
        return new CrushWaterMoveUnitEntity();
    }
	
	protected CrushWaterMoveUnitEntity() {
		super();
    }
	
	@Override
    public void update(){
    	super.update();
    }
	
	@Override
    public void moveAt(Vec2 vector, float acceleration){
		super.moveAt(vector, acceleration);
    }

    @Override
    public void approach(Vec2 vector){
    	super.approach(vector);
    }
}
