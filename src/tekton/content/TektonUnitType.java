package tekton.content;

import static mindustry.Vars.tilesize;

import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.ammo.ItemAmmoType;
import mindustry.world.meta.Env;

public class TektonUnitType extends UnitType {
	public boolean customFogRadius = false;
	public float fogRadiusMultipliyer = 0.9f;
	
	public TektonUnitType(String name) {
        super(name);
        mechLegColor = outlineColor = TektonColor.tektonOutlineColor;
        envDisabled = Env.none;
        //immunities.add(TektonStatusEffects.radiationAbsorption);
        ammoType = new ItemAmmoType(TektonItems.iron);
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
