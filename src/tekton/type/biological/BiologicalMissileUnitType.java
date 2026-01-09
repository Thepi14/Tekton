package tekton.type.biological;

import static arc.graphics.g2d.Draw.color;

import arc.graphics.g2d.Fill;
import mindustry.content.StatusEffects;
import mindustry.entities.Effect;
import mindustry.entities.abilities.LiquidExplodeAbility;
import mindustry.entities.abilities.RegenAbility;
import mindustry.type.StatusEffect;
import mindustry.type.ammo.PowerAmmoType;
import mindustry.type.unit.MissileUnitType;
import mindustry.world.meta.Env;
import tekton.Tekton;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonLiquids;
import tekton.content.TektonMissileUnitType;
import tekton.content.TektonStatusEffects;
import tekton.type.abilities.ColorDebrisAbility;

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
        ammoType = new PowerAmmoType(0f);
        engineSize = 0f;
        itemCapacity = 0;
        fallEffect = new Effect(110, e -> { //TODO is it really necessary?
            color(TektonColor.acid.cpy(), TektonColor.methane.cpy(), e.rotation);
            Fill.circle(e.x, e.y, e.fout() * 3.5f);
        });
        deathExplosionEffect = TektonFx.biologicalDynamicExplosion;
	}
}
