package tekton.content;

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
        outlineColor = TektonColor.tektonOutlineColor;
    }
}
