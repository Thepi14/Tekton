package tekton.type.abilities;

import static mindustry.Vars.*;
import static tekton.Drawt.*;

import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.type.bullets.*;
import arc.graphics.Color;
import arc.math.Mathf;

public class ColorDebrisAbility extends Ability {
	public Color color = TektonColor.acid;
	
	public ColorDebrisAbility() {
		display = false;
	}
	
	public ColorDebrisAbility(Color color) {
		display = false;
		this.color = color;
	}
	
	@Override
    public void death(Unit unit){
        if(headless || unit.tileOn() == null) return;

        final float size = Mathf.clamp(unit.hitSize / 4f, 0, 9), ux = unit.x(), uy = unit.y();

        if (!unit.tileOn().floor().isLiquid)
        	DrawColorDebris(ux, uy, (int)size, color);
    }
}
