package tekton.type.defense;

import static mindustry.Vars.indexer;
import static mindustry.Vars.player;
import static mindustry.Vars.state;
import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.EnumSet;
import arc.struct.IntFloatMap;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tekton.Drawt;
import tekton.math.TekMath;

public class Regenerator extends Block{
    private static final IntSet taken = new IntSet();
    private static final IntFloatMap mendMap = new IntFloatMap();
    private static long lastUpdateFrame = -1;

    public int range = 14;
    //per frame
    public float healPercent = 12f / 60f;
    public float optionalMultiplier = 2f;
    public float optionalUseTime = 60f * 8f;

    public DrawBlock drawer = new DrawDefault();

    public float effectChance = 0.003f;
    public Color baseColor = Pal.accent;
    public Effect effect = Fx.regenParticle;

    public Regenerator(String name){
        super(name);
        solid = true;
        update = true;
        group = BlockGroup.projectors;
        hasPower = true;
        hasItems = true;
        emitLight = true;
        suppressable = true;
        envEnabled |= Env.space;
        rotateDraw = false;
        flags = EnumSet.of(BlockFlag.repair);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);

        final float ex = (x * tilesize) + offset, ey = (y * tilesize) + offset;

        Drawt.dashDiamond(baseColor, ex, ey, (range / 2f) * tilesize);
        indexer.eachBlock(player.team(), Tmp.r1.setCentered(ex, ey, range * tilesize), b -> TekMath.insideDiamond(ex, ey, b.x(), b.y(), (range / 2f) * tilesize), t -> {
            Drawf.selected(t, Tmp.c1.set(baseColor).a(Mathf.absin(4f, 1f)));
        });
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public boolean outputsItems(){
        return false;
    }

    @Override
    public TextureRegion[] icons(){
        return drawer.finalIcons(this);
    }

    @Override
    public void load(){
        super.load();
        drawer.load(this);
    }

    @Override
    public void setStats(){
        stats.timePeriod = optionalUseTime;
        super.setStats();

        stats.add(Stat.repairTime, (int)(1f / (healPercent / 100f) / 60f), StatUnit.seconds);
        stats.add(Stat.range, range, StatUnit.blocks);
        stats.add(Stat.boostEffect, optionalMultiplier, StatUnit.timesSpeed);

        /*if(findConsumer(c -> c instanceof ConsumeItems) instanceof ConsumeItems cons){
            stats.remove(Stat.booster);
            stats.add(Stat.booster, StatValues.itemBoosters(
                "{0}" + StatUnit.timesSpeed.localized(),
                stats.timePeriod, optionalMultiplier, 0f,
                cons.items)
            );
        }*/
    }

    public class RegeneratorBuild extends Building{
        public Seq<Building> targets = new Seq<>();
        public int lastChange = -2;
        public float warmup, totalTime, optionalTimer;
        public boolean anyTargets = false;
        public boolean didRegen = false;

        public void updateTargets(){
            targets.clear();
            taken.clear();
            indexer.eachBlock(team, Tmp.r1.setCentered(x, y, range * tilesize), b -> TekMath.insideDiamond(x, y, b.x(), b.y(), (range / 2f) * tilesize), targets::add);
        }

        @Override
        public void updateTile(){
            if(lastChange != world.tileChanges){
                lastChange = world.tileChanges;
                updateTargets();
            }

            warmup = Mathf.approachDelta(warmup, didRegen ? 1f : 0f, 1f / 70f);
            totalTime += warmup * Time.delta;
            didRegen = false;
            anyTargets = false;

            //no healing when suppressed
            if(checkSuppression()){
                return;
            }

            anyTargets = targets.contains(b -> b.damaged());

            if(efficiency > 0){
                if((optionalTimer += Time.delta * optionalEfficiency) >= optionalUseTime){
                    consume();
                    optionalTimer = 0f;
                }

                float healAmount = Mathf.lerp(1f, optionalMultiplier, optionalEfficiency) * healPercent;

                //use Math.max to prevent stacking
                for(var build : targets){
                    if(!build.damaged() || build.isHealSuppressed()) {
						continue;
					}

                    didRegen = true;

                    int pos = build.pos();

                    float value = mendMap.get(pos);
                    mendMap.put(pos, Math.min(Math.max(value, healAmount * edelta() * build.block.health / 100f), build.block.health - build.health));

                    if(value <= 0 && Mathf.chanceDelta(effectChance * build.block.size * build.block.size)){
                        effect.at(build.x + Mathf.range(build.block.size * tilesize/2f - 1f), build.y + Mathf.range(build.block.size * tilesize/2f - 1f));
                    }
                }
            }

            if(lastUpdateFrame != state.updateId){
                lastUpdateFrame = state.updateId;

                for(var entry : mendMap.entries()){
                    var build = world.build(entry.key);
                    if(build != null) {
                        build.heal(entry.value);
                        build.recentlyHealed();
                    }
                }
                mendMap.clear();
            }
        }

        @Override
        public boolean shouldConsume(){
            return anyTargets;
        }

        @Override
        public void drawSelect(){
            super.drawSelect();

            Drawt.dashDiamond(baseColor, x, y, (range / 2f) * tilesize);
            for(var target : targets){
                Drawf.selected(target, Tmp.c1.set(baseColor).a(Mathf.absin(4f, 1f)));
            }
        }

        @Override
        public float warmup(){
            return warmup;
        }

        @Override
        public float totalProgress(){
            return totalTime;
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
    }
}