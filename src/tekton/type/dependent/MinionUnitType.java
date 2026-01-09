package tekton.type.dependent;

import static mindustry.Vars.tilesize;

import mindustry.Vars;
import mindustry.entities.*;
import mindustry.entities.effect.*;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.ammo.ItemAmmoType;
import mindustry.world.meta.Env;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonItems;
import tekton.content.TektonMissileUnitType;
import tekton.content.TektonStatusEffects;

public class MinionUnitType extends UnitType implements DependentType {
	public float maxDistance = 40f * tilesize;
	
	public Effect despawnEffect = TektonFx.teamColorDespawn;
	
	public MinionUnitType(String name) {
        super(name);
        mechLegColor = outlineColor = TektonColor.tektonOutlineColor;
        envDisabled = Env.none;
        immunities.addAll(TektonMissileUnitType.defaultImmunities);
        ammoType = new ItemAmmoType(TektonItems.iron);
        fogRadius = 0f;
    }

	@Override
	public float maxDistance() {
		return maxDistance;
	}
}
