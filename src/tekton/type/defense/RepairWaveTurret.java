package tekton.type.defense;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import tekton.content.TektonColor;
import tekton.type.bullets.WaveBulletType;

import static mindustry.Vars.*;

public class RepairWaveTurret extends Block{
    static final Rect rect = new Rect();
    static final Rand rand = new Rand();

    public int timerTarget = timers++;

    public float bulletOffset = 7f;
    public float repairAmount = 1.4f;
    public float repairRadius = 100f;
    public float repairAngle = 90f;
    public WaveBulletType bullet;
    public float reload = 8f;
    public float powerUse;
    public boolean acceptCoolant = false;

    public float coolantUse = 0.5f;
    /** Effect displayed when coolant is used. */
    public Effect coolEffect = Fx.fuelburn;
    /** How much healing is increased by with heat capacity. */
    public float coolantMultiplier = 1f;

    public TextureRegion baseRegion;
    public TextureRegion heatRegion;

    public Color heatColor = Color.valueOf("98ffa9");

    public RepairWaveTurret(String name){
        super(name);
        update = true;
        solid = true;
        flags = EnumSet.of(BlockFlag.repair);
        hasPower = true;
        outlineIcon = true;
        //yeah, this isn't the same thing, but it's close enough
        group = BlockGroup.projectors;
        
        bullet = new WaveBulletType() {{
        	//range = repairRadius;
        	circleDeegres = repairAngle;
        	minRadius = 5f;
        	linePoints = 24;
            lightColor = hitColor = Pal.heal;
            collidesAir = collidesGround = collidesTeam = true;
            healAmount = (repairAmount / damageInterval) / 5f;
        }};

        envEnabled |= Env.space;
    }

    @Override
    public void setStats(){
        super.setStats();
        stats.add(Stat.range, repairRadius / tilesize, StatUnit.blocks);
        stats.add(Stat.repairSpeed, (repairAmount / reload) * 60f, StatUnit.perSecond);

        if(acceptCoolant){
            stats.remove(Stat.booster);
            stats.add(Stat.booster, StatValues.speedBoosters(Core.bundle.get("bar.strength"), coolantUse, coolantMultiplier, true, this::consumesLiquid));
        }
    }

    @Override
    public void load() {
    	super.load();
    	
    	baseRegion = Core.atlas.find(name + "-base", "block-" + size);
    	heatRegion = Core.atlas.find(name + "-heat");
    }

    @Override
    public void init(){
        if(acceptCoolant){
            hasLiquids = true;
            consume(new ConsumeCoolant(coolantUse)).optional(true, true);
        }

        consumePowerCond(powerUse, (RepairTurretBuild entity) -> entity.target != null);
        updateClipRadius(repairRadius + tilesize);
        
        super.init();
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);

        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, repairRadius, Pal.accent);
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{baseRegion, region};
    }
    
    //TODO implement rotBlock interface next update
    public class RepairTurretBuild extends Building implements Ranged/*, RotBlock*/ {
        public Unit target;
        public Vec2 offset = new Vec2(), lastEnd = new Vec2();
        public float strength, rotation = 90;
        public float reloadTimer = 0f;

        /*@Override
        public float buildRotation(){
            return rotation;
        }*/

        @Override
        public void draw(){
            Draw.rect(baseRegion, x, y);

            Draw.z(Layer.turret);
            Drawf.shadow(region, x - (size / 2f), y - (size / 2f), rotation - 90);
            Draw.rect(region, x, y, rotation - 90);
            Draw.blend(Blending.additive);
            Draw.color(heatColor);
            Draw.alpha(strength);
            Draw.rect(heatRegion, x, y, rotation - 90);
            Draw.blend();
            Draw.color();
            
            Draw.reset();
        }

        @Override
        public void drawSelect(){
            Drawf.dashCircle(x, y, repairRadius, Pal.accent);
        }

        @Override
        public void updateTile(){
            float multiplier = 1f;
            if(acceptCoolant){
                multiplier = 1f + liquids.current().heatCapacity * coolantMultiplier * optionalEfficiency;
            }

            if(target != null && (target.dead() || target.dst(this) - target.hitSize/2f > repairRadius || target.health() >= target.maxHealth())){
                target = null;
            }

            if(target == null){
                offset.setZero();
            }

            if (target != null && efficiency > 0) {
                float angle = Angles.angle(x, y, target.x + offset.x, target.y + offset.y);
                
                if(reloadTimer > reload){
                    float nx = x + (Mathf.cosDeg(rotation) * bulletOffset), ny = y + (Mathf.sinDeg(rotation) * bulletOffset);
                    if(Angles.angleDist(angle, rotation) < bullet.circleDeegres / 2f){
                        bullet.create(this, team, nx, ny, rotation);
                        reloadTimer = 0f;
                    }
                }
                
                rotation = Mathf.slerpDelta(rotation, angle, 0.5f * efficiency * timeScale * Time.delta);
            }

            strength = Mathf.lerpDelta(strength, target != null && efficiency > 0 ? 1f : 0f, Time.delta * 0.5f);
            reloadTimer += Time.delta * efficiency;

            if(timer(timerTarget, 20)){
                rect.setSize(repairRadius * 2).setCenter(x, y);
                target = Units.closest(team, x, y, repairRadius, Unit::damaged);
            }
        }

        @Override
        public boolean shouldConsume(){
            return target != null && enabled;
        }

        @Override
        public BlockStatus status(){
            return Mathf.equal(potentialEfficiency, 0f, 0.01f) ? BlockStatus.noInput : super.status();
        }

        @Override
        public float range(){
            return repairRadius;
        }

        @Override
        public void write(Writes write){
            super.write(write);

            write.f(rotation);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);

            if(revision >= 1){
                rotation = read.f();
            }
        }

        @Override
        public byte version(){
            return 1;
        }
    }
}