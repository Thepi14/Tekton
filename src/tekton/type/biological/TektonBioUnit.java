package tekton.type.biological;

import static arc.graphics.g2d.Draw.color;

import arc.graphics.Color;
import static arc.graphics.g2d.Draw.*;
import arc.graphics.g2d.*;
import arc.math.*;
import static tekton.content.TektonFx.*;

import mindustry.content.StatusEffects;
import mindustry.entities.Effect;
import mindustry.entities.abilities.*;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.StatusEffect;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.ammo.*;
import mindustry.world.meta.Env;
import tekton.Tekton;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonLiquids;
import tekton.content.TektonStatusEffects;
import tekton.type.abilities.AcidBloodDebrisAbility;

import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import static mindustry.Vars.*;

public class TektonBioUnit extends UnitType {
	public boolean customFogRadius = false;
	
	public TektonBioUnit(String name) {
		super(name);
        //drawCell = false;
		createScorch = false;
		useUnitCap = false;
		drawBuildBeam = false;
		drawCell = false;
		hidden = Tekton.hideContent && false;
        researchCostMultiplier = 0f;
        outlineColor = TektonColor.tektonOutlineColor;
        envDisabled = Env.space | Env.scorching;
        lightColor = TektonColor.acid.cpy();
        lightOpacity = 0.35f;
        abilities.addAll(
        		new LiquidExplodeAbility() {{ 
        			liquid = TektonLiquids.acid;
	        	}},
        		new RegenAbility() {{
                    //fully regen in 180 seconds
                    percentAmount = 1f / (180f * 60f) * 100f;
                }},
        		new AcidBloodDebrisAbility());
        immunities.addAll(getDefaultImmunities());
        ammoType = new PowerAmmoType(0f);
        engineSize = 0f;
        itemCapacity = 0;
        fallEffect = new Effect(110, e -> {
            color(TektonColor.acid.cpy(), TektonColor.methane.cpy(), e.rotation);
            Fill.circle(e.x, e.y, e.fout() * 3.5f);
        });
        deathExplosionEffect = TektonFx.biologicalDynamicExplosion;
	}
	
	public static StatusEffect[] getDefaultImmunities() {
		return new StatusEffect[] { TektonStatusEffects.radioactiveContamination, 
        		TektonStatusEffects.wetInAcid, 
        		TektonStatusEffects.weaponLock,
        		TektonStatusEffects.shortCircuit, 
        		TektonStatusEffects.acidified, 
        		/*TektonStatusEffects.tarredInMethane,*/ 
        		TektonStatusEffects.neurosporaSlowed, 
        		StatusEffects.freezing };
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
