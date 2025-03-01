package tekton;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.content.Liquids;
import mindustry.game.EventType.Trigger;
import mindustry.graphics.Pal;
import mindustry.type.*;
import mindustry.ui.Bar;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;

public class UnstableCrafter extends GenericCrafter {
	public float heatPerCraft = 1f;
    public float coolantPower = 0.5f;
	
	public Liquid coolantLiquid = Liquids.water;
	public float coolantComsuption = 20f;

	public UnstableCrafter(String name) {
		super(name);
	}
	
	@Override
    public void setBars(){
        super.setBars();
        
        addBar("heat", (UnstableCrafterBuild entity) -> new Bar("bar.heat", Pal.turretHeat, () -> entity.heat));
    }
	
	@Override
    public void setStats(){
        //super.setStats();
        stats.timePeriod = craftTime;
        
        stats.add(Stat.size, "@x@", size, size);

        if(synthetic()){
            stats.add(Stat.health, health, StatUnit.none);
            if(armor > 0){
                stats.add(Stat.armor, armor, StatUnit.none);
            }
        }

        if(canBeBuilt() && requirements.length > 0){
            stats.add(Stat.buildTime, buildCost / 60, StatUnit.seconds);
            stats.add(Stat.buildCost, StatValues.items(false, requirements));
        }

        if(instantTransfer){
            stats.add(Stat.maxConsecutive, 2, StatUnit.none);
        }

        for(var c : consumers){
            c.display(stats);
        }

        //Note: Power stats are added by the consumers.
        if(hasLiquids) stats.add(Stat.liquidCapacity, liquidCapacity, StatUnit.liquidUnits);
        if(hasItems && itemCapacity > 0) stats.add(Stat.itemCapacity, itemCapacity, StatUnit.items);
        
        if((hasItems && itemCapacity > 0) || outputItems != null){
            stats.add(Stat.productionTime, craftTime / 60f, StatUnit.seconds);
        }

        if(outputItems != null){
            stats.add(Stat.output, StatValues.items(craftTime, outputItems));
        }

        if(outputLiquids != null){
            stats.add(Stat.output, StatValues.liquids(1f, outputLiquids));
        }
    }

	public class UnstableCrafterBuild extends GenericCrafterBuild {
		public float heat = 0f;
		
		@Override
        public void updateTile(){
            float coolant = liquids.get(coolantLiquid);
            float coolantFullness = coolant / liquidCapacity;
            
            if(efficiency > 0){

                progress += getProgressIncrease(craftTime);
                warmup = Mathf.approachDelta(warmup, warmupTarget(), warmupSpeed);

                //continuously output based on efficiency
                if(outputLiquids != null){
                    float inc = getProgressIncrease(1f);
                    for(var output : outputLiquids){
                        handleLiquid(this, output.liquid, Math.min(output.amount * inc, liquidCapacity - liquids.get(output.liquid)));
                    }
                }

                if(wasVisible && Mathf.chanceDelta(updateEffectChance)){
                    updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size * 4));
                }
            }else{
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }

            //TODO may look bad, revert to edelta() if so
            totalProgress += warmup * Time.delta;
            
            if(progress >= 1f) {
            	if (coolantFullness <= 0.01f) {
                	heat += heatPerCraft;
            	}
                craft();
            }
        	if (coolantFullness >= 0.01f) {
        		liquids.remove(coolantLiquid, Math.min(0, coolantComsuption));
        	}
            dumpOutputs();
            
            heat = Mathf.clamp(heat);
            
            if(heat >= 0.999f){
                //Events.fire(Trigger.thoriumReactorOverheat);
                kill();
            }
        }
	}
}
