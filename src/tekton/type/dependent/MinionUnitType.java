package tekton.type.dependent;

import static mindustry.Vars.tilesize;

import mindustry.entities.Effect;
import mindustry.type.UnitType;
import mindustry.world.meta.Env;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonItems;
import tekton.content.TektonMissileUnitType;

public class MinionUnitType extends UnitType implements DependentType {
	public float maxDistance = 40f * tilesize;

	public Effect despawnEffect = TektonFx.teamColorDespawn;

	public MinionUnitType(String name) {
        super(name);
        mechLegColor = outlineColor = TektonColor.tektonOutlineColor;
        envDisabled = Env.none;
        immunities.addAll(TektonMissileUnitType.defaultImmunities);
        fogRadius = 0f;
    }

	@Override
	public float maxDistance() {
		return maxDistance;
	}
}
