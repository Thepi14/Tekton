package tekton.abilities;

import static mindustry.Vars.*;
import static tekton.Drawt.*;

import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import tekton.content.TektonFx;
import tekton.type.bullets.*;
import arc.math.Mathf;

public class AcidBloodDebrisAbility extends Ability {
	
	public AcidBloodDebrisAbility() {
		display = false;
	}
	
	@Override
    public void death(Unit unit){
        if(headless || unit.tileOn() == null) return;

        final float size = Mathf.clamp(unit.hitSize / 4f, 0, 9), rotation = Mathf.random(4) * 90, ux = unit.x(), uy = unit.y();

        if (!unit.tileOn().floor().isLiquid)
        	DrawAcidDebris(ux, uy, rotation, (int)size);
    }
}
