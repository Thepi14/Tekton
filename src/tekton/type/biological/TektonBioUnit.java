package tekton.type.biological;

import static mindustry.Vars.tilesize;

import arc.graphics.g2d.Fill;
import mindustry.entities.Effect;
import mindustry.entities.abilities.LiquidExplodeAbility;
import mindustry.entities.abilities.RegenAbility;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.meta.Env;
import tekton.Tekton;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonLiquids;
import tekton.type.abilities.ColorDebrisAbility;

import arc.graphics.*;
import arc.graphics.g2d.*;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;

public class TektonBioUnit extends UnitType implements BiologicalUnit {
	public boolean customFogRadius = false;

	public TektonBioUnit(String name) {
		super(name);
        drawCell = Tekton.drawBiologicalUnitsCell;
		createScorch = false;
		useUnitCap = false;
		drawBuildBeam = false;
		hidden = Tekton.hideContent;
        researchCostMultiplier = 0f;
        outlineColor = TektonColor.tektonOutlineColor;
        envDisabled = Env.space | Env.scorching;
        lightColor = TektonColor.acid;
        lightOpacity = 0.35f;
        abilities.addAll(
        		new LiquidExplodeAbility() {{
        			liquid = TektonLiquids.acid;
	        	}},
        		new RegenAbility() {{
                    //fully regen in 180 seconds
                    percentAmount = 1f / (180f * 60f) * 100f;
                }},
        		new ColorDebrisAbility());
        immunities.addAll(BiologicalUnit.getDefaultImmunities());
        engineSize = 0f;
        itemCapacity = 0;
        fallEffect = new Effect(110, e -> {
            color(TektonColor.acid.cpy(), TektonColor.methane.cpy(), e.rotation);
            Fill.circle(e.x, e.y, e.fout() * 3.5f);
        });
        deathExplosionEffect = TektonFx.biologicalDynamicExplosion;
	}

	@Override
    public void init() {
        super.init();
        if (customFogRadius) {
            lightRadius = fogRadius * 0.8f * tilesize;
            return;
        }
        float maxWeaponRange = 0;
        for (Weapon weapon : weapons) {
            if (weapon.range() > maxWeaponRange) {
                maxWeaponRange = weapon.range();
            }
        }
        /*LiquidExplodeAbility acidAbility = (LiquidExplodeAbility)abilities.find((ability) -> { return ability instanceof LiquidExplodeAbility; });
        acidAbility.amount = 140f;
        if (acidAbility.liquid == TektonLiquids.acid)
        	acidAbility.radScale = Math.max(1.2f, hitSize / 10f);
        else
        	acidAbility.radScale = Math.max(1f, hitSize / 17f);*/
        fogRadius = maxWeaponRange / 6f;
    }
}
