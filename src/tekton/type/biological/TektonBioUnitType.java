package tekton.type.biological;

import static mindustry.Vars.tilesize;

import arc.Core;
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

import tekton.content.TektonStat;
import tekton.content.TektonStatUnit;

import static tekton.content.TektonStat.*;
import tekton.type.abilities.ColorDebrisAbility;

import static arc.graphics.g2d.Draw.*;

public class TektonBioUnitType extends UnitType implements BiologicalUnit {
	public static final int unknownTimeThreshold = 100000 * 12;
	
	public boolean customFogRadius = false;
    
	public BiologicalOrigin origin = BiologicalOrigin.natural;
	public int lifeExpectancyMonths = 0;
	public int maturationTimeMonths = 0;
	public int maximumLifeSpanMonths = 0;

	public TektonBioUnitType(String name) {
		super(name);
        drawCell = Tekton.drawBiologicalUnitsCell;
		createScorch = false;
		useUnitCap = false;
		drawBuildBeam = false;
		hidden = false;
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
                    //fully regen in 300 seconds
                    percentAmount = 1f / (300f * 60f) * 100f;
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

    @Override
    public void setStats() {
    	super.setStats();
    	
    	stats.add(TektonStat.biologicalOrigin, GetUnitOriginString(origin));
    	
    	if (lifeExpectancyMonths >= unknownTimeThreshold) {
    		stats.add(TektonStat.lifeExpectancy, Core.bundle.get("unknown"));
    	}
    	else {
        	if (lifeExpectancyMonths > 12)
        		stats.add(TektonStat.lifeExpectancy, (int)(lifeExpectancyMonths / 12), TektonStatUnit.years);
        	if (lifeExpectancyMonths % 12 > 0)
        		stats.add(TektonStat.lifeExpectancy, lifeExpectancyMonths % 12, TektonStatUnit.months);
        	else if (lifeExpectancyMonths == 0)
        		stats.add(TektonStat.lifeExpectancy, Core.bundle.get("unit.lessthanamonth"));
    	}

    	if (maturationTimeMonths >= unknownTimeThreshold) {
    		stats.add(TektonStat.maturationTime, Core.bundle.get("unknown"));
    	}
    	else {
	    	if (maturationTimeMonths > 12)
	    		stats.add(TektonStat.maturationTime, (int)(maturationTimeMonths / 12), TektonStatUnit.years);
	    	if (maturationTimeMonths % 12 > 0)
	    		stats.add(TektonStat.maturationTime, maturationTimeMonths % 12, TektonStatUnit.months);
	    	else if (maturationTimeMonths == 0)
	    		stats.add(TektonStat.maturationTime, Core.bundle.get("unit.lessthanamonth"));
    	}

		if (maximumLifeSpanMonths >= unknownTimeThreshold) {
			stats.add(TektonStat.maximumLifeSpan, Core.bundle.get("unknown"));
		}
		else {
	    	if (maximumLifeSpanMonths > 12)
	    		stats.add(TektonStat.maximumLifeSpan, (int)(maximumLifeSpanMonths / 12), TektonStatUnit.years);
	    	if (maximumLifeSpanMonths % 12 > 0)
	    		stats.add(TektonStat.maximumLifeSpan, maximumLifeSpanMonths % 12, TektonStatUnit.months);
	    	else if (maximumLifeSpanMonths == 0)
	    		stats.add(TektonStat.maximumLifeSpan, Core.bundle.get("unit.lessthanamonth"));
		}
    }
}
