package tekton.content;

import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.ammo.ItemAmmoType;
import mindustry.type.unit.TankUnitType;
import mindustry.world.meta.Env;

public class TektonTankUnitType extends TektonUnitType {
	public TektonTankUnitType(String name) {
        super(name);
        squareShape = true;
        omniMovement = false;
        rotateMoveFirst = true;
        rotateSpeed = 1.3f;
        envDisabled = Env.none;
        speed = 0.8f;
        ammoType = new ItemAmmoType(TektonItems.iron);
        outlineColor = TektonColor.tektonOutlineColor;
    }

    @Override
    public void init() {
        super.init();
        float maxWeaponRange = 0;
        for (Weapon weapon : weapons) {
            if (weapon.range() > maxWeaponRange) {
                maxWeaponRange = weapon.range();
            }
        }
        fogRadius = maxWeaponRange / 8;
    }
}
