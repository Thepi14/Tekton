package tekton;

import arc.util.io.Writes;
import arc.math.Mathf;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Unit;
import mindustry.type.unit.MissileUnitType;
import arc.math.geom.Position;
import arc.math.geom.Vec2;

public class DistanceMissileUnitType extends MissileUnitType {
	public float maxDistance = 43f * Vars.tilesize;
	
	public DistanceMissileUnitType(String name) {
		super(name);
    	constructor = DistanceMissileUnitEntity::create;
		lifetime = 3.5f * 60f;
    	}

    @Override
    public Unit spawn(Position pos) {
    	return super.spawn(pos);
    }

    @Override
    public Unit spawn(Position pos, Team team) {
    	return super.spawn(pos);
    }

    @Override
    public void update(Unit unit) {
        super.update(unit);
    }
    
    @Override
    public void init() {
        super.init();
    }
}
