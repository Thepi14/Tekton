package tekton.type.biological;

import arc.graphics.g2d.Fill;
import mindustry.entities.Effect;
import mindustry.world.meta.Env;
import tekton.Tekton;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonMissileUnitType;
import arc.graphics.*;
import arc.graphics.g2d.*;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;

public class BiologicalMissileUnitType extends TektonMissileUnitType implements BiologicalUnit {

	public BiologicalMissileUnitType(String name) {
		super(name);
        drawCell = Tekton.drawBiologicalUnitsCell;
		createScorch = false;
		createWreck = false;
		useUnitCap = false;
		drawBuildBeam = false;
        envDisabled = Env.space | Env.scorching;
        lightColor = TektonColor.acid.cpy();
        lightOpacity = 0.35f;
        //abilities.add(new AcidBloodDebrisAbility()); //would be strange if a missile regenerated
        //immunities.addAll(BiologicalUnit.getDefaultImmunities());
        engineSize = 0f;
        itemCapacity = 0;
        fallEffect = new Effect(110, e -> { //TODO is it really necessary?
            color(TektonColor.acid.cpy(), TektonColor.methane.cpy(), e.rotation);
            Fill.circle(e.x, e.y, e.fout() * 3.5f);
        });
        deathExplosionEffect = TektonFx.biologicalDynamicExplosion;
	}
}
