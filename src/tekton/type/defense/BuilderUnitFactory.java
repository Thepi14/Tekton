package tekton.type.defense;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import arc.struct.Queue;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.entities.Units;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.game.Teams.BlockPlan;
import mindustry.gen.Building;
import mindustry.gen.BuildingTetherPayloadUnit;
import mindustry.gen.BuildingTetherc;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.ui.Bar;
import mindustry.ui.Fonts;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.ConstructBlock.ConstructBuild;
import mindustry.world.blocks.UnitTetherBlock;
import tekton.Drawt;
import tekton.type.ai.RebuilderAI;

//import ent.anno.Annotations.*;
/*import mindustry.annotations.*;
import mindustry.annotations.util.*;
import mindustry.annotations.util.TypeIOResolver.*;*/

import static mindustry.Vars.*;

public class BuilderUnitFactory extends Block {
    public final int timerTarget = timers++, timerTarget2 = timers++;
    public int targetInterval = 7;

	public int maxUnits = 3;
	public float buildRadius = 30f * tilesize, unitBuildDistance = 100f;
	public float rotationMoveDelay = 60f * 3f, rotationSpeedPerTick = 15f / 60f;
	public UnitType unitType = UnitTypes.mono;
    public float unitBuildTime = 60f * 8f;

    public float polyStroke = 2f, polyRadius = 11f;
    public int polySides = 6;
    public float polyRotateSpeed = 1f;
    public Color polyColor = Pal.heal;

    public BuilderUnitFactory(String name) {
        super(name);

        solid = true;
        update = true;
        hasItems = false;
        hasLiquids = true;
        
        ambientSound = Sounds.respawning;
        lightColor = Pal.heal.cpy();
        //requirements = with(iron, 45, Items.silicon, 25);
    }
    
    @Override
    public void init(){
        super.init();

        unitType.aiController = RebuilderAI::new;
    }

    @Override
    public boolean outputsItems(){
        return false;
    }

    @Override
    public void setBars(){
        super.setBars();
        
        addBar("units", (BuilderUnitSourceBuild e) ->
            new Bar(
            () ->
            Core.bundle.format("bar.unitcap",
                Fonts.getUnicodeStr(unitType.name),
                e.units.size,
                maxUnits
            ),
            () -> Pal.power,
            () -> (float)e.units.size / maxUnits
        ));
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        return super.canPlaceOn(tile, team, rotation) && Units.canCreate(team, unitType);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
        
        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, buildRadius, Pal.accent);
        
        /*if(!Units.canCreate(Vars.player.team(), unitType)){
            drawPlaceText(Core.bundle.get("bar.cargounitcap"), x, y, valid);
        }*/
    }

    //@Remote(called = Loc.server)
    public static void unitTetherBlockSpawned(Tile tile, int id) {
    	if (net.client()) return;
        if(tile == null || !(tile.build instanceof UnitTetherBlock build)) return;
        build.spawned(id);
    }
	
	public class BuilderUnitSourceBuild extends Building implements UnitTetherBlock {
        protected IntSeq readUnits = new IntSeq();

        public Seq<Unit> units = new Seq<Unit>();
        public float buildProgress, totalProgress, rotationProgress = Mathf.range(360f);
        public float warmup, unitWarmup, moveDelayTimer = Mathf.range(rotationMoveDelay);
        
        public boolean missingUnit;
        public @Nullable Unit following;
        public @Nullable BlockPlan lastPlan;
        
        public Seq<Vec2> positions = new Seq<Vec2>();

        public void spawned(int id) {
            Fx.spawn.at(x, y);
            buildProgress = 0f;
            if(net.client()){
            	readUnits.set(0, id);
            }
        }
		
		@Override
        public void drawSelect() {
			super.drawSelect();
			Drawf.dashCircle(x + offset, y + offset, buildRadius, Pal.accent);
        }

        @Override
        public boolean shouldConsume() {
            return units.size < maxUnits && !isHealSuppressed();
        }

        @Override
        public void updateTile() {
        	if(!readUnits.isEmpty()){
                units.clear();
                readUnits.each(i -> {
                    var unit = Groups.unit.getByID(i);
                    if(unit != null){
                        units.add(unit);
                    }
                });
                readUnits.clear();
            }

            units.removeAll(u -> !u.isAdded() || u.dead);
        	
            if(checkSuppression()){
                efficiency = potentialEfficiency = 0f;
            }
            
            unitWarmup = Mathf.lerpDelta(unitWarmup, units.size < maxUnits ? efficiency : 0f, 0.1f);
            warmup = Mathf.approachDelta(warmup, efficiency, 1f / 60f);
            
            if (efficiency > 0f) {
                buildProgress += edelta() / unitBuildTime;
                totalProgress += edelta();
            }
            
            for (var unit : units) {
            	if(following != null){
                    //validate follower
                    if(!following.isValid() || !following.activelyBuilding()){
                        following = null;
                        unit.plans().clear();
                    }else{
                        //set to follower's first build plan, whatever that is
                        unit.plans().clear();
                        unit.plans().addFirst(following.buildPlan());
                        lastPlan = null;
                    }

                }else if(unit.buildPlan() == null && timer(timerTarget, targetInterval)){ //search for new stuff
                    Queue<BlockPlan> blocks = team.data().plans;
                    for(int i = 0; i < blocks.size; i++){
                        var block = blocks.get(i);
                        if(within(block.x * tilesize, block.y * tilesize, buildRadius)){
                        	//TODO change next update
                        	
                            var btype = content.block(block.block);
                            //var btype = block.block;

                            if(Build.validPlace(btype, unit.team(), block.x, block.y, block.rotation) && (state.rules.infiniteResources || team.rules().infiniteResources || team.items().has(btype.requirements, state.rules.buildCostMultiplier))){
                                unit.addBuild(new BuildPlan(block.x, block.y, block.rotation, content.block(block.block), block.config));
                                //unit.addBuild(new BuildPlan(block.x, block.y, block.rotation, block.block, block.config));
                                
                                //shift build plan to tail so next unit builds something else
                                blocks.addLast(blocks.removeIndex(i));
                                lastPlan = block;
                                break;
                            }
                        }
                    }

                    //still not building, find someone to mimic
                    if(unit.buildPlan() == null){
                        following = null;
                        Units.nearby(team, x, y, buildRadius, u -> {
                            if(following != null) return;

                            if(u.canBuild() && u.activelyBuilding()){
                                BuildPlan plan = u.buildPlan();

                                Building build = world.build(plan.x, plan.y);
                                if(build instanceof ConstructBuild && within(build, buildRadius)){
                                    following = u;
                                }
                            }
                        });
                    }
                }else if(unit.buildPlan() != null){ //validate building
                    BuildPlan req = unit.buildPlan();

                    //clear break plan if another player is breaking something
                    if(!req.breaking && timer.get(timerTarget2, 30f)){
                        for(Player player : team.data().players){
                            if(player.isBuilder() && player.unit().activelyBuilding() && player.unit().buildPlan().samePos(req) && player.unit().buildPlan().breaking){
                                unit.plans().removeFirst();
                                //remove from list of plans
                                team.data().plans.remove(p -> p.x == req.x && p.y == req.y);
                                return;
                            }
                        }
                    }

                    boolean valid =
                        !(lastPlan != null && lastPlan.removed) &&
                        ((req.tile() != null && req.tile().build instanceof ConstructBuild cons && cons.current == req.block) ||
                        (req.breaking ?
                        Build.validBreak(unit.team(), req.x, req.y) :
                        Build.validPlace(req.block, unit.team(), req.x, req.y, req.rotation)));

                    if(!valid){
                        //discard invalid request
                        unit.plans().removeFirst();
                        lastPlan = null;
                    }
                }
            	
            	unit.plans().remove(b -> {if (b != null) return b.build() == this; else return true; });

                unit.updateBuildLogic();
            }
            
            if(units.size < maxUnits && (buildProgress += edelta() / unitBuildTime) >= 1f && !net.client()){
                var unit = unitType.create(team);
                if(unit instanceof BuildingTetherc bt){
                    bt.building(this);
                }
                unit.set(x, y);
                unit.rotation = 90f;
                unit.add();

                Fx.spawn.at(unit);
                units.add(unit);
                buildProgress = 0f;
                
                Call.unitTetherBlockSpawned(tile, unit.id);
                moveDelayTimer = rotationMoveDelay + 1f;
            }

            rotationProgress += Time.delta;
            moveDelayTimer += Time.delta;
            
            if (moveDelayTimer >= rotationMoveDelay) {
            	rotationProgress += rotationSpeedPerTick * rotationMoveDelay;
            	moveDelayTimer = 0f;
            }
            positions.clear();
            for (int i = 1; i <= units.size; i++) {
            	Vec2 pos = new Vec2(
            			x + (Mathf.cosDeg((rotationProgress * rotationSpeedPerTick) - ((360f / units.size) * i)) * (buildRadius / 2f)), 
            			y + (Mathf.sinDeg((rotationProgress * rotationSpeedPerTick) - ((360f / units.size) * i)) * (buildRadius / 2f)));
            	positions.add(pos);
            }
            
        	int i = 0;
        	if (units.size > 0 && positions.size > 0)
            for (var unit : units) {
            	if (unit == null) break;
            	if (unit.controller() instanceof RebuilderAI tUnit) {
            		if (!unit.activelyBuilding()) {
                		tUnit.distance = 5f;
                		tUnit.keepDistance = false;
                		tUnit.arrival = true;
                		tUnit.targetPos	= positions.get(i);
                	}
                	else if (unit.activelyBuilding()) {
                		tUnit.distance = unitBuildDistance;
                		tUnit.keepDistance = true;
                		tUnit.arrival = false;
                		tUnit.targetPos	= new Vec2(unit.buildPlan().x, unit.buildPlan().y).scl(8f);
                	}
            	}
            	i++;
            }
            
            Mathf.clamp(buildProgress);
            Mathf.clamp(totalProgress);
        }

        @Override
        public void draw(){
            super.draw();
            
            if(units.size < maxUnits){
                Draw.draw(Layer.blockOver, () -> {
                    //TODO make sure it looks proper
                    Drawf.construct(this, unitType.fullIcon, 0f, buildProgress, warmup, totalProgress);
                });
            }else{
                Draw.z(Layer.bullet - 0.01f);
                Draw.color(polyColor);
                Lines.stroke(polyStroke);
                Lines.poly(x, y, polySides, polyRadius, Time.time * polyRotateSpeed);
                Draw.reset();
                Draw.z(Layer.block);
            }
            
            Draw.reset();
        }
		
		@Override
        public void drawLight() {
            Drawf.light(x, y, efficiency * size * 1.75f * tilesize, lightColor, efficiency);
        }

        @Override
        public float totalProgress(){
            return totalProgress;
        }

        @Override
        public float progress(){
            return buildProgress;
        }

        @Override
        public void write(Writes write){
            super.write(write);

            write.s(units.size);
            for(var unit : units){
                write.i(unit.id);
            }
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);

            int count = read.s();
            readUnits.clear();
            for(int i = 0; i < count; i++){
                readUnits.add(read.i());
            }
        }
		
		@Override
        public boolean canPickup() {
            return false;
        }
    }
}
