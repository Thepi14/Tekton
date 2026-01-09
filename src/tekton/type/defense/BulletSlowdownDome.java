package tekton.type.defense;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType.Trigger;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Groups;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tekton.content.TektonFx;
import tekton.content.TektonStat;

import static mindustry.Vars.*;

public class BulletSlowdownDome extends Block {
    public int timerCheck = timers ++;
    
    //checking for bullets every frame is costly, so only do it at intervals even when ready.
    public float checkInterval = 3f;
	
	public float range = 20f * tilesize;
	public float strokeSize = 3f, waveEffectReload = 320f;
    public Color waveColor = Color.valueOf("83eff2"), heatColor = Pal.techBlue, shapeColor = Color.valueOf("83eff2");
    public Effect hitEffect = TektonFx.slowDownDomeHitEffect;
    public Effect waveEffect = TektonFx.slowDownWave;
    public float speedMultiplier = 0.5f;
    
    public float shapeRotateSpeed = 1f, shapeRadius = 6f;
    public int shapeSides = 4;
    public float warmupSpeed = 0.5f;

    public TextureRegion heatRegion;
    
    public DrawBlock drawer = new DrawDefault();
	
	public BulletSlowdownDome(String name) {
		super(name);
        update = true;
        solid = true;
	}

    @Override
    public void setStats(){
        super.setStats();
        
        stats.add(Stat.range, range / tilesize, StatUnit.blocks);
        stats.add(TektonStat.slowMultiplierFunction, speedMultiplier * 100, StatUnit.percent);
    }
    
    @Override
    public void load() {
    	super.load();
    	
    	drawer.load(this);
    	heatRegion = Core.atlas.find(name + "-heat");
        
		clipSize = waveEffect.clip = range * 2;
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);

        Drawf.dashSquare(waveColor, x * tilesize + offset, y * tilesize + offset, range);
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

	public class BulletSlowdownDomeBuild extends Building{
        public float heat = 0f, waveEffectProgress = 0f;
        public Seq<Bullet> targets = new Seq<>();
        
        @Override
        public void updateTile(){
            if(potentialEfficiency > 0 && (timer(timerCheck, checkInterval))){
                targets.clear();
                Groups.bullet.intersect(x - (range / 2f), y - (range / 2f), range, range, b -> {
                    if(b.team != team && b.type.hittable && b.type.speed != 0){
                        targets.add(b);
                    }
                });
                
                if(targets.size > 0){
                    for(var target : targets) {
                    	if (target == null) {
                    		targets.remove(target);
                    	}
                    	else {
                    		target.vel.scl(1f - ((1f - speedMultiplier) / checkInterval));
                    		hitEffect.at(target.x, target.y, target.rotation(), waveColor, target);
                    	}
                    }
                }
            }
            if (waveEffectProgress > waveEffectReload) {
            	waveEffect.at(x, y, range / 2f, waveColor);
            	waveEffectProgress = 0f;
            }
            
            waveEffectProgress += delta() * targets.size > 0 && efficiency > 0 ? 1f : 0f;
            heat = Mathf.lerpDelta(heat, targets.size > 0 && efficiency > 0 ? 1f : 0f, Time.delta * warmupSpeed);
        }
        
        @Override
        public boolean shouldConsume(){
            return targets.size > 0;
        }
        
        @Override
        public float warmup(){
            return heat;
        }
        
        @Override
        public void draw(){
            //super.draw();
            drawer.draw(this);
            
            Drawf.additive(heatRegion, heatColor, heat, x, y, 0f, Layer.blockAdditive);

            Draw.z(Layer.effect);
            Draw.color(shapeColor, waveColor, Mathf.pow(heat, 2f));
            Lines.stroke(strokeSize * heat);
            Lines.square(x, y, range / 2f);
            Fill.poly(x, y, shapeSides, shapeRadius * heat, Time.time * shapeRotateSpeed);
            Draw.color();
        }

        @Override
        public void drawLight(){
            super.drawLight();
            drawer.drawLight(this);
        }

        @Override
        public void drawSelect(){
            Drawf.dashSquare(waveColor, x, y, range);
        }
	}
}
