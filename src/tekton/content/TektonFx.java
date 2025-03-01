package tekton.content;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.Fx;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.ParticleEffect;
import mindustry.entities.effect.WaveEffect;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import static mindustry.Vars.*;

public class TektonFx {
	public static final Rand rand = new Rand();
    public static final Vec2 v = new Vec2();

    public static final Effect 
    
    electricPulse = new ParticleEffect() {{
    	particles = 6;
    	line = true;
    	lifetime = 15;
    	length = 15;
    	lenFrom = 3;
    	lenTo = 0;
    	strokeFrom = 1;
    	strokeTo = 0;
    	colorFrom = Color.valueOf("ffffff");
    	colorTo = Color.valueOf("4a9eff");
    }},
    
    electricPulseBig = new MultiEffect() {{
    	effects = new Effect[]{
    			new WaveEffect() {{
        	    	sizeFrom = 2;
        	    	sizeTo = 14;
        	    	lifetime = 10;
        	    	strokeFrom = 2;
        	    	strokeTo = 0;
        	    	colorFrom = Color.valueOf("ffffff");
        	    	colorTo = Color.valueOf("4a9eff");
        	    }},
        		new ParticleEffect() {{
        			particles = 6;
        			line = true;
        			lifetime = 15;
        			length = 15;
        			lenFrom = 3;
        			lenTo = 0;
        			strokeFrom = 1;
        			strokeTo = 0;
        			colorFrom = Color.valueOf("ffffff");
        			colorTo = Color.valueOf("4a9eff");
        		}}
    	};
    }},
    
	lancerLaserCharge = copyEffect(Fx.lancerLaserCharge),
	lancerLaserChargeBegin = copyEffect(Fx.lancerLaserChargeBegin),
	
	phalanxSmoke = new MultiEffect(
	new Effect(30f, 160f, e -> {
        color(Pal.sapBullet);
        stroke(e.fout() * 3f);
        float circleRad = 6f + e.finpow() * 60f;
        Lines.circle(e.x, e.y, circleRad);

        rand.setSeed(e.id);
        for(int i = 0; i < 16; i++){
            float angle = rand.random(360f);
            float lenRand = rand.random(0.5f, 1f);
            Lines.lineAngle(e.x, e.y, angle, e.foutpow() * 50f * rand.random(1f, 0.6f) + 2f, e.finpow() * 70f * lenRand + 6f);
        }
    }) {{followParent = false;}},
	new Effect(300f, 300f, b -> {
        float intensity = 3f;

        color(Pal.sapBullet, 0.7f);
        for(int i = 0; i < 4; i++){
            rand.setSeed(b.id*2 + i);
            float lenScl = rand.random(0.5f, 1f);
            int fi = i;
            b.scaled(b.lifetime * lenScl, e -> {
                randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int)(2.9f * intensity), 22f * intensity, (x, y, in, out) -> {
                    float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                    float rad = fout * ((2f + intensity) * 2.35f);

                    Fill.circle(e.x + x, e.y + y, rad);
                    Drawf.light(e.x + x, e.y + y, rad * 2.5f, b.color, 0.5f);
                });
            });
        }
    }) {{followParent = false;}})
	
	{{followParent = false;}},
    
	nuclearExplosion = new Effect(30, 500f, b -> {
        float intensity = 6.8f;
        float baseLifetime = 25f + intensity * 11f;
        b.lifetime = 50f + intensity * 65f;

        color(b.color);
        alpha(0.7f);
        for(int i = 0; i < 4; i++){
            rand.setSeed(b.id*2 + i);
            float lenScl = rand.random(0.4f, 1f);
            int fi = i;
            b.scaled(b.lifetime * lenScl, e -> {
                randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int)(2.9f * intensity), 22f * intensity, (x, y, in, out) -> {
                    float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                    float rad = fout * ((2f + intensity) * 2.35f);

                    Fill.circle(e.x + x, e.y + y, rad);
                    Drawf.light(e.x + x, e.y + y, rad * 2.5f, b.color, 0.5f);
                });
            });
        }

        b.scaled(baseLifetime, e -> {
            Draw.color();
            e.scaled(5 + intensity * 2f, i -> {
                stroke((3.1f + intensity/5f) * i.fout());
                Lines.circle(e.x, e.y, (3f + i.fin() * 14f) * intensity);
                Drawf.light(e.x, e.y, i.fin() * 14f * 2f * intensity, Color.white, 0.9f * e.fout());
            });

            color(Pal.lighterOrange, b.color, e.fin());
            stroke((2f * e.fout()));

            Draw.z(Layer.effect + 0.001f);
            randLenVectors(e.id + 1, e.finpow() + 0.001f, (int)(8 * intensity), 28f * intensity, (x, y, in, out) -> {
                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + out * 4 * (4f + intensity));
                Drawf.light(e.x + x, e.y + y, (out * 4 * (3f + intensity)) * 3.5f, Draw.getColor(), 0.8f);
            });
        });
    }),
	
	biologicalPulse = new ParticleEffect() {{
    	particles = 6;
    	line = true;
    	lifetime = 15;
    	length = 15;
    	lenFrom = 3;
    	lenTo = 0;
    	strokeFrom = 1;
    	strokeTo = 0;
    	colorFrom = Color.valueOf("ffffff");
    	colorTo = TektonLiquids.acid.color;
    }},
	
	biologicalPulseBig = new MultiEffect() {{
    	effects = new Effect[]{
    			new WaveEffect() {{
        	    	sizeFrom = 2;
        	    	sizeTo = 17;
        	    	lifetime = 12;
        	    	strokeFrom = 3;
        	    	strokeTo = 0;
			    	colorFrom = Color.valueOf("ffffff");
			    	colorTo = TektonLiquids.acid.color;
        	    }},
    			new ParticleEffect() {{
			    	particles = 10;
			    	line = true;
			    	lifetime = 18;
			    	length = 35;
			    	lenFrom = 5;
			    	lenTo = 0;
			    	strokeFrom = 2;
			    	strokeTo = 0;
			    	colorFrom = Color.valueOf("ffffff");
			    	colorTo = TektonLiquids.acid.color;
    			}}
			};
    }},
    
    incinerateHydrogen = new Effect(34, e -> {
        randLenVectors(e.id, 4, e.finpow() * 5f, (x, y) -> {
            color(Pal.slagOrange, Color.gray, e.fin());
            Fill.circle(e.x + x, e.y + y, e.fout() * 1.7f);
        });
    }),
    
	chainTesla = new Effect(30f, 300f, e -> {
        if(!(e.data instanceof Position p)) return;
        float tx = p.getX(), ty = p.getY(), dst = Mathf.dst(e.x, e.y, tx, ty);
        Tmp.v1.set(p).sub(e.x, e.y).nor();

        float normx = Tmp.v1.x, normy = Tmp.v1.y;
        float range = 6f;
        int links = Mathf.ceil(dst / range);
        float spacing = dst / links;

        Lines.stroke(4f * e.fout());
        Draw.color(Color.white, e.color, e.fin());

        Lines.beginLine();

        Lines.linePoint(e.x, e.y);

        rand.setSeed(e.id);

        for(int i = 0; i < links; i++){
            float nx, ny;
            if(i == links - 1){
                nx = tx;
                ny = ty;
            }else{
                float len = (i + 1) * spacing;
                Tmp.v1.setToRandomDirection(rand).scl(range/2f);
                nx = e.x + normx * len + Tmp.v1.x;
                ny = e.y + normy * len + Tmp.v1.y;
            }

            Lines.linePoint(nx, ny);
        }

        Lines.endLine();
    }).followParent(false).rotWithParent(false)
    
    ;
    
	public static void load(){
		
	}
	
	private static Effect copyEffect(Effect effect) {
		var Ceffect = effect;
		Ceffect.followParent(true);
		Ceffect.rotWithParent(true);
		return Ceffect;
	}
}
