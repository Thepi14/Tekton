package tekton.content;

import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.meta.Env;

public class TektonUnitType extends UnitType {
	public boolean customFogRadius = false;
	public float fogRadiusMultipliyer = 0.9f;

	public TektonUnitType(String name) {
        super(name);
        mechLegColor = outlineColor = TektonColor.tektonOutlineColor;
        envDisabled = Env.none;
        itemCapacity = 0;
        //immunities.add(TektonStatusEffects.radiationAbsorption);
    }

	@Override
    public void init() {
        super.init();
        if (customFogRadius) {
            return;
        }
        float maxWeaponRange = 0;
        for (Weapon weapon : weapons) {
            if (weapon.range() > maxWeaponRange) {
                maxWeaponRange = weapon.range();
            }
        }
        fogRadius = (maxWeaponRange / 8f) * fogRadiusMultipliyer;
    }
}
