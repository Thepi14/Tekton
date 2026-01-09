package tekton.type.biological;

import mindustry.Vars;
import tekton.type.ai.DistanceMissileAI;
import tekton.type.dependent.DependentType;

public class BiologicalDistanceMissileUnitType extends BiologicalMissileUnitType implements DependentType {
	public float maxDistance = 20f * Vars.tilesize;
	
	public BiologicalDistanceMissileUnitType(String name) {
		super(name);
		lifetime = 3.5f * 60f;
		controller = u -> new DistanceMissileAI();
	}

	@Override
	public float maxDistance() {
		return maxDistance;
	}
}
