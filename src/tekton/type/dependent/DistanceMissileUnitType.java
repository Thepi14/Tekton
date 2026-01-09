package tekton.type.dependent;

import arc.util.io.Writes;
import arc.math.Mathf;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Unit;
import mindustry.type.unit.MissileUnitType;
import tekton.content.TektonMissileUnitType;
import tekton.type.ai.DistanceMissileAI;
import arc.math.geom.Position;
import arc.math.geom.Vec2;

public class DistanceMissileUnitType extends TektonMissileUnitType implements DependentType {
	public float maxDistance = 43f * Vars.tilesize;
	
	public DistanceMissileUnitType(String name) {
		super(name);
		lifetime = 3.5f * 60f;
		controller = u -> new DistanceMissileAI();
	}

	@Override
	public float maxDistance() {
		return maxDistance;
	}
}
