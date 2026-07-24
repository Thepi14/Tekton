package tekton.type.production;

import arc.Core;
import arc.util.Log;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.graphics.Pal;
import mindustry.ui.*;
import mindustry.world.blocks.production.AttributeCrafter;
import mindustry.world.consumers.ConsumeLiquidBase;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;
import mindustry.Vars;
import mindustry.core.*;

public class AttributeCrafterBoosted extends AttributeCrafter {
	public boolean lerpDeltaBoost = false;
	public float liquidBoostIntensity = 2f;
	public boolean dynamicBoostPowerUse = true;
	public float basePowerUse;

	public AttributeCrafterBoosted(String name) {
		super(name);

	}

    @Override
    public void init(){
        if(dynamicBoostPowerUse){
            basePowerUse = consPower != null ? consPower.usage : 0f;
            consumePowerDynamic(basePowerUse, (AttributeCrafterBoostedBuild build) -> build.efficiencyScale());
        }

        super.init();
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.remove(Stat.booster);
        if(liquidBoostIntensity > 1f && findConsumer(f -> f instanceof ConsumeLiquidBase && f.booster) instanceof ConsumeLiquidBase consBase){
            stats.remove(Stat.booster);
            stats.add(Stat.booster,
                StatValues.speedBoosters("{0}" + StatUnit.timesSpeed.localized(),
                consBase.amount * liquidBoostIntensity, liquidBoostIntensity, false, r -> consumesLiquid(r))
            );
        }
    }
    
    public void setBars(){
        super.setBars();
    	
    	if(consPower != null){
            removeBar("power");
            
            addBar("power", entity -> new Bar(
                () -> { var build = (AttributeCrafterBoostedBuild)entity; return Core.bundle.get("stat.poweruse") + ": " + UI.formatAmount((int)((build.currentLiquidBoost > liquidBoostIntensity - 1.002f ? 1f : build.currentLiquidBoost) * basePowerUse * 60f)); },
                () -> Pal.powerBar,
                () -> entity.power.status)
            );
        }
    }

	public class AttributeCrafterBoostedBuild extends AttributeCrafterBuild {
		public float currentLiquidBoost = 1f;

        @Override
        public void updateTile() {
        	totalProgress += (warmup * Time.delta * currentLiquidBoost);

        	for (var liquid : Vars.content.liquids()) {
            	if (block.consumesLiquid(liquid) && findConsumer(f -> f instanceof ConsumeLiquidBase && f.booster) instanceof ConsumeLiquidBase consBase) {
            		var amount = liquids.get(liquid);
            		if (lerpDeltaBoost) {
                		if (amount > 0.0001f) {
                			currentLiquidBoost = Mathf.lerpDelta(currentLiquidBoost, liquidBoostIntensity - 1f, .5f);
                		}
                		else {
                			currentLiquidBoost = Mathf.lerpDelta(currentLiquidBoost, 0f, .5f);
                		}
            		}
            		else {
            			float lerp = amount / liquidCapacity;
            			currentLiquidBoost = Mathf.lerp(0f, liquidBoostIntensity - 1f, lerp > 0.998f ? 1f : lerp);
            		}
            	}
        	}

        	super.updateTile();

        	//efficiency *= currentLiquidBoost;
        }

        @Override
        public float efficiencyScale() {
            return (scaleLiquidConsumption ? efficiencyMultiplier() : super.efficiencyScale()) + currentLiquidBoost;
        }
	}
}
