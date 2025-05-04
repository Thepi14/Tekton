package tekton.type.minion;

import mindustry.entities.*;
import mindustry.entities.effect.*;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.ammo.ItemAmmoType;
import mindustry.world.meta.Env;
import tekton.content.TektonColor;
import tekton.content.TektonItems;
import tekton.content.TektonMissileUnitType;
import tekton.content.TektonStatusEffects;

public class MinionUnitType extends UnitType {
	public Effect despawnEffect = new WaveEffect() {{
		sides = 4;
		strokeFrom = 1.5f;
		strokeTo = 0.1f;
		sizeFrom = 2f;
		sizeTo = 7f;
		colorFrom = colorTo = Pal.lancerLaser;
	}};
	
	public MinionUnitType(String name) {
        super(name);
        mechLegColor = outlineColor = TektonColor.tektonOutlineColor;
        envDisabled = Env.none;
        immunities.addAll(TektonMissileUnitType.defaultImmunities);
        ammoType = new ItemAmmoType(TektonItems.iron);
        fogRadius = 0f;
    }
}
