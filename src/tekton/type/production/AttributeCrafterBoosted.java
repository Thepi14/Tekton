package tekton.type.production;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Liquids;
import mindustry.world.blocks.production.AttributeCrafter;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeLiquidBase;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;
import mindustry.world.modules.LiquidModule.LiquidConsumer;

public class AttributeCrafterBoosted extends AttributeCrafter {
	public float liquidBoostIntensity = 2f;
	
	public AttributeCrafterBoosted(String name) {
		super(name);
		
	}

    @Override
    public void setStats(){
        super.setStats();
        
        stats.remove(Stat.booster);
        if(liquidBoostIntensity != 1 && findConsumer(f -> f instanceof ConsumeLiquidBase && f.booster) instanceof ConsumeLiquidBase consBase){
            stats.remove(Stat.booster);
            stats.add(Stat.booster,
                StatValues.speedBoosters("{0}" + StatUnit.timesSpeed.localized(),
                consBase.amount, liquidBoostIntensity, false, r -> consumesLiquid(r))
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
            		if (amount > 0.0001f) {
            			currentLiquidBoost = Mathf.lerpDelta(currentLiquidBoost, liquidBoostIntensity - 1f, 0.5f);
            		}
            		else {
            			currentLiquidBoost = Mathf.lerpDelta(currentLiquidBoost, 0f, 0.5f);
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
