package tekton.type.abilities;

import static mindustry.Vars.tilesize;

import arc.Core;
import arc.graphics.Color;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.util.Strings;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.entities.effect.ParticleEffect;
import mindustry.gen.Unit;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonStatusEffects;

public class RadiationFieldAbility extends Ability {
    public float duration = 300f, reload = 180f, range = 20, damage = 20, unitDamageScale = 2f, buildingEfficiencyMultiplier = 0.8f;

    public boolean parentizeEffects, effectSizeParam = true;
    public boolean onShoot = false;
    public Effect applyEffect = TektonFx.biologicalPulse;
    public Effect buildingApplyEffect = new ParticleEffect() {{
    	particles = 1;
    	line = true;
    	lifetime = 15;
    	length = 15;
    	lenFrom = 3;
    	lenTo = 0;
    	strokeFrom = 1;
    	strokeTo = 0;
    	colorFrom = Color.white;
    	colorTo = TektonColor.acid;
    }};
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

    @Override
    public void update(Unit unit){
        timer += Time.delta;

		if (Mathf.randomBoolean(effectChance)) {
			areaEffect.at(new Vec2(unit.x + (Mathf.random(range) * Mathf.cosDeg(Mathf.random(360f))), unit.y + (Mathf.random(range) * Mathf.sinDeg(Mathf.random(360f)))));
		}

        if(timer >= reload && (!onShoot || unit.isShooting)){
            Units.nearby(null, unit.x, unit.y, range, other -> {
            	if (other.hittable() && other != unit) {
                    if(other.team != unit.team && !other.isImmune(TektonStatusEffects.radioactiveContamination)) {
                        other.damage(damage * unitDamageScale);
                        other.apply(TektonStatusEffects.radioactiveContamination, duration);
                        applyEffect.at(other, parentizeEffects);
                    }
                    else if (other.isImmune(TektonStatusEffects.radioactiveContamination) && !other.isImmune(TektonStatusEffects.radiationAbsorption)) {
                        other.apply(TektonStatusEffects.radiationAbsorption, duration);
                        applyEffect.at(other, parentizeEffects);
                    }
                }
            });

            Vars.indexer.allBuildings(unit.x, unit.y, range, other -> {
                if(unit.team != other.team) {
                    other.applySlowdown(buildingEfficiencyMultiplier, reload);
                    other.damage(damage);
                    buildingApplyEffect.at(other, parentizeEffects);
                }
            });

            float x = unit.x + Angles.trnsx(unit.rotation, effectY, effectX), y = unit.y + Angles.trnsy(unit.rotation, effectY, effectX);
            activeEffect.at(x, y, effectSizeParam ? range : unit.rotation, color, parentizeEffects ? unit : null);

            timer = 0f;
        }
    }
}
