package tekton.type.abilities;

import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;

public class SpeedDamageAbility extends Ability {
	public float speedMultiplier = 2f;
	
	public SpeedDamageAbility() {
		display = false;
	}
    
	@Override
	public void update(Unit unit)
	{
		if (unit.health > 0f)
			unit.speedMultiplier *= 1f + ((1f - (unit.health / unit.type.health)) * (speedMultiplier - 1f));
	}
}
