package tekton.type.dependent;

import mindustry.Vars;
import tekton.content.TektonMissileUnitType;
import tekton.type.ai.DistanceMissileAI;

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
