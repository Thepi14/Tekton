package tekton.type.abilities;

import arc.*;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.ui.layout.*;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.content.StatusEffects;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;

public class FireRateOverdriveAbility extends Ability {
	public float minBoost = 0.1f, maxBoost = 2f, boostIncrease = 0.04f, boostDecrease = 0.2f;
	public StatusEffect statusCondition = StatusEffects.slow;
	public Color heatColor = Pal.turretHeat.cpy();
	public TextureRegion heatRegion;
	public float heat = 0f;
	
	protected float currentBoost = 0f;
	
	/*private static int divisions = 200;
	public static Seq<StatusEffect> boosts;*/

    @Override
    public void addStats(Table t) {
        super.addStats(t);
        maxBoost = Math.min(maxBoost, 4f);
        t.add(Core.bundle.format("bullet.reload", Strings.autoFixed((int)(maxBoost * 100f), 3)));
        t.row();
        t.add(abilityStat("minreload", Strings.autoFixed((int)(minBoost * 100f), 3)));
    }
    
    public String abilityStat(String stat, Object... values) {
        return Core.bundle.format("ability.stat." + stat, values);
    }

	@Override
	public void init(UnitType type){
		/*if (boosts == null) {
			boosts = new Seq<StatusEffect>();
			float mul = 4f / (float)divisions;
			for (int i = 1; i <= divisions; i++) {
				var current = mul * i;
				boosts.add(new StatusEffect("rate-of-fire-boost-" + i) {{
					reloadMultiplier = current;
					hideDetails = true;
					show = false;
				}});
			}
		}*/
	}
	
	@Override
	public void update(Unit unit)
	{
		/*if (boosts == null || boosts.size == 0)
			return;*/
		if (unit.isShooting() && (unit.hasEffect(statusCondition) || statusCondition == StatusEffects.none)) {
			currentBoost += boostIncrease / 60f;
		}
		else {
			currentBoost -= boostDecrease / 60f;
		}
		
		currentBoost = Mathf.clamp(currentBoost, minBoost, maxBoost);
		heat = (currentBoost - minBoost) / (maxBoost - minBoost);
		
		//int currentIndex = Math.min((int)(currentBoost * (200 / 4)), 199);
		//Log.info(currentBoost + ", " + currentIndex + ", " + boosts.get(currentIndex).reloadMultiplier);
		//unit.apply(boosts.get(currentIndex));
		
		unit.reloadMultiplier *= currentBoost;
	}
	
	@Override
    public void draw(Unit unit) {
        super.draw(unit);
        
        if(heatRegion == null){
        	heatRegion = Core.atlas.find(unit.type.name + "-heat", unit.type.region);
        }
        
        Draw.blend(Blending.additive);
        Drawf.additive(heatRegion, heatColor, heat, unit.x, unit.y, unit.rotation - 90f, unit.isGrounded() ? unit.type.groundLayer + 0.01f : unit.type.lowAltitude ? Layer.flyingUnitLow + 0.01f : Layer.flyingUnit + 0.01f);
        Draw.color();
    }
}
