package tekton.type.abilities;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import arc.Core;
import arc.graphics.Color;
import arc.util.Strings;
import mindustry.content.Fx;
import mindustry.entities.abilities.Ability;
import mindustry.graphics.Pal;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonStatusEffects;
import tekton.type.biological.BiologicalBlock;
import mindustry.type.*;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatValues;

import static mindustry.Vars.*;

public class RadiationFieldAbility extends Ability {
    public float duration = 300f, reload = 180f, range = 20, damage = 20, unitDamageScale = 3f, buildingEfficiencyMultiplier = 0.8f;

    public boolean parentizeEffects, effectSizeParam = true;
    public boolean onShoot = false;
    public Effect applyEffect = TektonFx.biologicalPulse;
    public Effect activeEffect = Fx.overdriveWave;
    public Effect areaEffect = TektonStatusEffects.radioactiveContamination.effect;
	public float effectChance = 0.1f;
    public float effectX, effectY;
    public Color color = TektonColor.radiation;

    protected float timer;

    public RadiationFieldAbility(){}
    
    public RadiationFieldAbility(float duration, float reload, float range){
        this.duration = duration;
        this.reload = reload;
        this.range = range;
    }
    
    public String getBundle(){
        var type = getClass();
        return "ability." + (type.isAnonymousClass() ? type.getSuperclass() : type).getSimpleName().replace("Ability", "").toLowerCase();
    }

    @Override
    public void addStats(Table t){
        //super.addStats(t);
		float descriptionWidth = 350f;
        t.add(Core.bundle.get(getBundle() + ".description")).wrap().width(descriptionWidth);
        t.row();
        t.add(Core.bundle.format("bullet.range", Strings.autoFixed(range / tilesize, 2)));
        t.row();
        t.add(abilityStat("firingrate", Strings.autoFixed(60f / reload, 2)));
        t.row();
        t.add(abilityStat("damage", Strings.autoFixed(damage * unitDamageScale, 7)));
        t.row();
        t.add(Core.bundle.format("bullet.buildingdamage", Strings.autoFixed((damage / (damage * unitDamageScale)) * 100, 7)));
        if (buildingEfficiencyMultiplier < 1) {
            t.row();
            t.add(abilityStat("buildingefficiencymultiplier", Strings.autoFixed(buildingEfficiencyMultiplier * 100, 7)));
        }
        
    }
    
    public String abilityStat(String stat, Object... values) {
        return Core.bundle.format("ability.stat." + stat, values);
    }

    @Override
    public void update(Unit unit){
        timer += Time.delta;

		if (Mathf.randomBoolean(effectChance))
			areaEffect.at(new Vec2(unit.x + (Mathf.random(range) * Mathf.cosDeg(Mathf.random(360f))), unit.y + (Mathf.random(range) * Mathf.sinDeg(Mathf.random(360f)))));

        if(timer >= reload && (!onShoot || unit.isShooting)){
            Units.nearby(null, unit.x, unit.y, range, other -> {
            	if (other.hittable() && other != unit) {
                    if(other.team != unit.team) {
                        other.damage(damage * unitDamageScale);
                        other.apply(TektonStatusEffects.radioactiveContamination, duration);
                    }
                    else {
                        other.apply(TektonStatusEffects.radiationAbsorption, duration);
                    }
                    applyEffect.at(other, parentizeEffects);
                }
            });
            
            Vars.indexer.allBuildings(unit.x, unit.y, range, other -> {
                if(unit.team != other.team) {
                    other.applySlowdown(0.6f, reload);
                    other.damage(damage);
                    //TODO: too much effect
                    //applyEffect.at(other, parentizeEffects);
                }
            });

            float x = unit.x + Angles.trnsx(unit.rotation, effectY, effectX), y = unit.y + Angles.trnsy(unit.rotation, effectY, effectX);
            activeEffect.at(x, y, effectSizeParam ? range : unit.rotation, color, parentizeEffects ? unit : null);

            timer = 0f;
        }
    }
}
