package tekton.content;

import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.ammo.ItemAmmoType;
import mindustry.type.ammo.PowerAmmoType;
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
        ammoType = new PowerAmmoType();
        outlineColor = TektonColor.tektonOutlineColor;
    }
}
