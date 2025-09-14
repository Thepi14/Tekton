package tekton.type.production;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.struct.EnumSet;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Nullable;
import arc.util.Strings;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType.Trigger;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.liquid.Conduit.ConduitBuild;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;
import mindustry.world.draw.*;

import static mindustry.Vars.*;

public class LiquidConverter extends Block {

    /** Written to outputLiquids as a single-element array if outputLiquids is null. */
    public @Nullable LiquidStack outputLiquid;
    /** Overwrites outputLiquid if not null. */
    public @Nullable LiquidStack[] outputLiquids;
    
    public LiquidStack[] convertableLiquids;
    public float liquidConsumption = 10f / 60f;

    public float craftTime = 80;
    public Effect craftEffect = Fx.none;
    public Effect updateEffect = Fx.none;
    public float updateEffectChance = 0.04f;
    public float updateEffectSpread = 4f;
    public float warmupSpeed = 0.019f;
    
    public boolean dumpExtraLiquid = true;
    public boolean ignoreLiquidFullness = false;
    
    public int[] liquidOutputDirections = {-1};

    public DrawBlock drawer = new DrawDefault();

	public LiquidConverter(String name) {
		super(name);
        update = true;
        solid = true;
        hasItems = true;
        ambientSound = Sounds.machine;
        sync = true;
        ambientSoundVolume = 0.03f;
        flags = EnumSet.of(BlockFlag.factory);
        drawArrow = false;
        itemCapacity = 0;
        convertableLiquids = new LiquidStack[0];
	}
	
	@Override
    public void setBars(){
        super.setBars();

        //set up liquid bars for liquid outputs
        if(outputLiquids != null && outputLiquids.length > 0){
            //no need for dynamic liquid bar
            removeBar("liquid");

            //then display output buffer
            for(var stack : outputLiquids){
                addLiquidBar(stack.liquid);
            }
        }
        
        addBar("efficiency", (LiquidConverterBuild entity) -> new Bar(
		        () -> Core.bundle.format("bar.efficiency", (int)(entity.currentBoost * 100)),
		        () -> Pal.lightOrange,
		        () -> Math.min(entity.currentBoost, 1f)));
    }
	
	private int bId = 0;
	public float getBoost() {
		bId++;
		if (bId > convertableLiquids.length)
			bId = 0;
		return convertableLiquids[bId - 1].amount;
				
	}
	
	@Override
    public void setStats(){
        stats.timePeriod = craftTime;
        super.setStats();

        if(outputLiquids != null){
            stats.add(Stat.output, StatValues.liquids(1f, outputLiquids));
        }
        
        if(convertableLiquids.length > 1){
            stats.remove(Stat.booster);
            /*stats.add(Stat.booster, StatValues.boosters(liquidConsumption, liquidConsumption / 60f, getBoost() / liquidConsumption, false, r -> { 
            	for (int i = 0; i < convertableLiquids.length; i++) {
            		if (r == convertableLiquids[i].liquid) {
            			return true;
            		}
            } return false; }));*/
        	stats.add(Stat.booster,
	                StatValues.speedBoosters("{0}" + StatUnit.percent.localized(),
	    	        		liquidConsumption, 
	    	        		(liquidConsumption / getBoost()) * 100, 
	    	                false, 
	    	                r -> { 
	    	                	for (int i = 0; i < convertableLiquids.length; i++) {
	    	                		if (r == convertableLiquids[i].liquid) {
	    	                			return true;
	    	                		}
	    	                } return false; })
	    	            );
        }
    }
	
	@Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public TextureRegion[] icons(){
        return drawer.finalIcons(this);
    }

    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out){
        drawer.getRegionsToOutline(this, out);
    }

    @Override
    public void drawOverlay(float x, float y, int rotation){
        if(outputLiquids != null){
            for(int i = 0; i < outputLiquids.length; i++){
                int dir = liquidOutputDirections.length > i ? liquidOutputDirections[i] : -1;

                if(dir != -1){
                    Draw.rect(
                        outputLiquids[i].liquid.fullIcon,
                        x + Geometry.d4x(dir + rotation) * (size * tilesize / 2f + 4),
                        y + Geometry.d4y(dir + rotation) * (size * tilesize / 2f + 4),
                        8f, 8f
                    );
                }
            }
        }
    }
    
    /*@Override
    public boolean rotatedOutput(int fromX, int fromY, Tile destination){
        if(!(destination.build instanceof ConduitBuild)) return false;

        Building crafter = world.build(fromX, fromY);
        if(crafter == null) return false;
        int relative = Mathf.mod(crafter.relativeTo(destination) - crafter.rotation, 4);
        for(int dir : liquidOutputDirections){
            if(dir == -1 || dir == relative) return false;
        }

        return true;
    }*/

    @Override
    public void load(){
        super.load();

        drawer.load(this);
    }
	
	@Override
    public void init(){
        if(outputLiquids == null && outputLiquid != null){
            outputLiquids = new LiquidStack[]{outputLiquid};
        }
        //write back to outputLiquid, as it helps with sensing
        if(outputLiquid == null && outputLiquids != null && outputLiquids.length > 0){
            outputLiquid = outputLiquids[0];
        }
        outputsLiquid = outputLiquids != null;
        
        if(outputLiquids != null) hasLiquids = true;

        super.init();
    }
	
	public class LiquidConverterBuild extends Building {
        public float progress;
        public float totalProgress;
        public float warmup;
        public float currentBoost = 0f;

        @Override
        public void updateTile(){
        	defineCurrentBoost();
        	efficiency *= currentBoost;
        	
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
                    updateEffect.at(x + Mathf.range(size * updateEffectSpread), y + Mathf.range(size * updateEffectSpread));
                }
            }else{
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }

            //TODO may look bad, revert to edelta() if so
            totalProgress += warmup * edelta();

            if(progress >= 1f){
                craft();
            }

            dumpOutputs();
        }
        
        @Override
        public boolean shouldConsume(){
            if(outputLiquids != null && !ignoreLiquidFullness){
                boolean allFull = true;
                for(var output : outputLiquids){
                    if(liquids.get(output.liquid) >= liquidCapacity - 0.001f){
                        if(!dumpExtraLiquid){
                            return false;
                        }
                    }else{
                        //if there's still space left, it's not full for all liquids
                        allFull = false;
                    }
                }

                //if there is no space left for any liquid, it can't reproduce
                if(allFull){
                    return false;
                }
            }

            return enabled;
        }
        
        public void defineCurrentBoost() {
        	var newBoost = 0f;
            for (int i = 0; i < convertableLiquids.length; i++) {
            	var stack = convertableLiquids[i];
            	var currentAmount = liquids.get(convertableLiquids[i].liquid);
            	if (currentAmount > liquidConsumption) {
                	newBoost += stack.amount / liquidConsumption;
            	}
            }
            
            currentBoost = newBoost;
        }
        
        public void craft() {
            //consume();

            for (int i = 0; i < convertableLiquids.length; i++) {
            	var stack = convertableLiquids[i];
            	liquids.remove(stack.liquid, liquidConsumption * 60f);
            }

            if(wasVisible){
                craftEffect.at(x, y);
            }
            
            progress %= 1f;
        }

        public void dumpOutputs() {
        	if(outputLiquids != null){
                for(int i = 0; i < outputLiquids.length; i++){
                    int dir = liquidOutputDirections.length > i ? liquidOutputDirections[i] : -1;

                    dumpLiquid(outputLiquids[i].liquid, 2f, dir);
                }
            }
        }
        
        @Override
        public float getProgressIncrease(float baseTime){
            if(ignoreLiquidFullness){
                return super.getProgressIncrease(baseTime);
            }

            //limit progress increase by maximum amount of liquid it can produce
            float scaling = 1f, max = 1f;
            if(outputLiquids != null){
                max = 0f;
                for(var s : outputLiquids){
                    float value = (liquidCapacity - liquids.get(s.liquid)) / (s.amount * edelta());
                    scaling = Math.min(scaling, value);
                    max = Math.max(max, value);
                }
            }

            //when dumping excess take the maximum value instead of the minimum.
            return super.getProgressIncrease(baseTime) * (dumpExtraLiquid ? Math.min(max, 1f) : scaling);
        }

        @Override
        public void draw(){
            drawer.draw(this);
        }

        @Override
        public void drawLight(){
            super.drawLight();
            drawer.drawLight(this);
        }

        public float warmupTarget(){
            return 1f;
        }

        @Override
        public float warmup(){
            return warmup;
        }

        @Override
        public float totalProgress(){
            return totalProgress;
        }

        @Override
        public float progress(){
            return Mathf.clamp(progress);
        }

        @Override
        public boolean shouldAmbientSound(){
            return efficiency > 0;
        }

        @Override
        public int getMaximumAccepted(Item item){
            return 0;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(progress);
            write.f(warmup);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            progress = read.f();
            warmup = read.f();
        }
		
        @Override
        public double sense(LAccess sensor){
            if(sensor == LAccess.progress) return progress();
            //attempt to prevent wild total liquid fluctuation, at least for crafters
            if(sensor == LAccess.totalLiquids && outputLiquid != null) return liquids.get(outputLiquid.liquid);
            return super.sense(sensor);
        }
	}
}
