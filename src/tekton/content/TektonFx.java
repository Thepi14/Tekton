package tekton.content;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Liquids;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.entities.effect.ExplosionEffect;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.ParticleEffect;
import mindustry.entities.effect.WaveEffect;
import mindustry.entities.effect.WrapEffect;
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
    
    public static float time() { return TektonVars.time(); };

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
    	colorFrom = Color.white;
    	colorTo = Pal.lancerLaser;
    }},
    
	electricSurgePulse = new ParticleEffect() {{
    	particles = 6;
    	line = true;
    	lifetime = 15;
    	length = 15;
    	lenFrom = 3;
    	lenTo = 0;
    	strokeFrom = 1;
    	strokeTo = 0;
    	colorFrom = Color.white;
    	colorTo = Pal.surge;
    }},
    
    electricPulseBig = new MultiEffect() {{
    	effects = new Effect[]{
    			new WaveEffect() {{
        	    	sizeFrom = 2;
        	    	sizeTo = 14;
        	    	lifetime = 10;
        	    	strokeFrom = 2;
        	    	strokeTo = 0;
        	    	colorFrom = Color.white;
        	    	colorTo = Pal.lancerLaser;
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
        			colorFrom = Color.white;
        			colorTo = Pal.lancerLaser;
        		}}
    	};
    }},
    
	electricSurgePulseBig = new MultiEffect() {{
    	effects = new Effect[]{
    			new WaveEffect() {{
        	    	sizeFrom = 2;
        	    	sizeTo = 14;
        	    	lifetime = 10;
        	    	strokeFrom = 2;
        	    	strokeTo = 0;
        	    	colorFrom = Color.white;
        	    	colorTo = Pal.surge;
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
        			colorFrom = Color.white;
        			colorTo = Pal.surge;
        		}}
    	};
    }},
    	    
    
    electricExplosionShoot = new ExplosionEffect(){{
        lifetime = 40f;
        waveStroke = 4f;
        waveColor = sparkColor = smokeColor = Pal.lancerLaser;
        waveRad = 15f;
        smokeSize = 5f;
        smokes = 8;
        smokeSizeBase = 0f;
        sparks = 8;
        sparkRad = 40f;
        sparkLen = 4f;
        sparkStroke = 3f;
    }},
    
	electricLaserCharge = new Effect(80f, 100f, e -> {
        color(Pal.lancerLaser);
        stroke(e.fin() * 2f);
        Lines.circle(e.x, e.y, 4f + e.fout() * 100f);

        Fill.circle(e.x, e.y, e.fin() * 20);

        randLenVectors(e.id, 20, 40f * e.fout(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fin() * 5f);
            Drawf.light(e.x + x, e.y + y, e.fin() * 15f, Pal.lancerLaser, 0.7f);
        });

        color();

        Fill.circle(e.x, e.y, e.fin() * 10);
        Drawf.light(e.x, e.y, e.fin() * 20f, Pal.lancerLaser, 0.7f);
    }).followParent(true).rotWithParent(true),
	
	neurosporaContaminationSapped = new Effect(40f, e -> {
        color(TektonColor.neurospora);

        randLenVectors(e.id, 2, 1f + e.fin() * 2f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fslope() * 1.1f);
        });
    }),
	
	pointShockwave = new Effect(20, e -> {
		if (e.data instanceof Float size) {
	        color(e.color);
	        stroke(e.fout() * 2f);
	        Lines.circle(e.x, e.y, e.finpow() * e.rotation);
	        randLenVectors(e.id + 1, 8, (size / tilesize) * e.finpow(), (x, y) ->
	            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f));
		}
    }),
	
	slowDownWave = new Effect(320f, e -> {
        color(e.color);
        stroke(e.fout() * 2f);
        Lines.square(e.x, e.y, e.finpow() * e.rotation);
        randLenVectors(e.id + 1, 5, 1f + 23f * e.finpow(), (x, y) ->
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f));
    }),
	
	slowDownDomeHitEffect = new Effect(10f, e -> {
		if (e.data instanceof Bullet target) {
	        color(e.color);

	        stroke(1.5f * e.fout());
	        
	        Lines.square(e.x, e.y, 0.2f + (target.hitSize * 1.7f * target.fout()), e.rotation + (time() * 1f));

	        Drawf.light(e.x, e.y, 20f, e.color, 0.6f * e.fout());
		}
    }),
	
	buildingBiologicalRegeneration = new Effect(35f, e -> {
        color(e.color, e.color.cpy().mul(1.2f), e.fin());

        randLenVectors(e.id, 4, 17f * e.fin(), (x, y) -> {
        	Fill.circle(e.x + x, e.y + y, e.fslope() * 1.5f + 0.5f);
        });
    }),
	
	cobwebbed = new Effect(80f, e -> {
        color(TektonColor.cobweb);
        alpha(Mathf.clamp(e.fin() * 2f));

        Fill.circle(e.x, e.y, e.fout());
    }),
	
	arteryPowerTransfer = new Effect(140f, e -> {
        e.lifetime = Mathf.randomSeed(e.id, 120f, 200f);

        if(!(e.data instanceof Position to)) return;

        Tmp.v2.set(to).sub(e.x, e.y).nor().rotate90(1).scl(Mathf.randomSeedRange(e.id, 1f) * 25f);

        Tmp.bz2.set(Tmp.v1.set(e.x, e.y), Tmp.v2.add(e.x, e.y), Tmp.v3.set(to));

        Tmp.bz2.valueAt(Tmp.v4, e.fout());

        color(e.color);
        Fill.circle(Tmp.v4.x, Tmp.v4.y, e.fslope() * 2f + 0.1f);
    }).followParent(false).rotWithParent(false),
	
	biologicalDynamicExplosion = new Effect(30, 500f, b -> {
        float intensity = b.rotation;
        float baseLifetime = 26f + intensity * 15f;
        b.lifetime = 43f + intensity * 35f;

        color(TektonColor.acid.cpy());
        alpha(0.9f);
        for(int i = 0; i < 4; i++){
            rand.setSeed(b.id*2 + i);
            float lenScl = rand.random(0.4f, 1f);
            int fi = i;
            b.scaled(b.lifetime * lenScl, e -> {
                randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int)(3f * intensity), 14f * intensity, (x, y, in, out) -> {
                    float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                    Fill.circle(e.x + x, e.y + y, fout * ((2f + intensity) * 1.8f));
                });
            });
        }

        b.scaled(baseLifetime, e -> {
            e.scaled(5 + intensity * 2.5f, i -> {
                stroke((3.1f + intensity/5f) * i.fout());
                Lines.circle(e.x, e.y, (3f + i.fin() * 14f) * intensity);
                Drawf.light(e.x, e.y, i.fin() * 14f * 2f * intensity, TektonColor.acid.cpy(), 0.9f * e.fout());
            });

            color(TektonColor.acid.cpy(), TektonColor.methane.cpy(), Color.gray, e.fin());
            stroke((1.7f * e.fout()) * (1f + (intensity - 1f) / 2f));

            Draw.z(Layer.effect + 0.001f);
            randLenVectors(e.id + 1, e.finpow() + 0.001f, (int)(9 * intensity), 40f * intensity, (x, y, in, out) -> {
                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + out * 4 * (3f + intensity));
                Drawf.light(e.x + x, e.y + y, (out * 4 * (3f + intensity)) * 3.5f, Draw.getColor(), 0.8f);
            });
        });
    }),
	
	biologicalAmmoniaDynamicExplosion = new Effect(30, 500f, b -> {
        float intensity = b.rotation;
        float baseLifetime = 26f + intensity * 15f;
        b.lifetime = 43f + intensity * 35f;

        color(TektonColor.ammonia.cpy());
        alpha(0.9f);
        for(int i = 0; i < 4; i++){
            rand.setSeed(b.id*2 + i);
            float lenScl = rand.random(0.4f, 1f);
            int fi = i;
            b.scaled(b.lifetime * lenScl, e -> {
                randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int)(3f * intensity), 14f * intensity, (x, y, in, out) -> {
                    float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                    Fill.circle(e.x + x, e.y + y, fout * ((2f + intensity) * 1.8f));
                });
            });
        }

        b.scaled(baseLifetime, e -> {
            e.scaled(5 + intensity * 2.5f, i -> {
                stroke((3.1f + intensity/5f) * i.fout());
                Lines.circle(e.x, e.y, (3f + i.fin() * 14f) * intensity);
                Drawf.light(e.x, e.y, i.fin() * 14f * 2f * intensity, TektonColor.ammonia.cpy(), 0.9f * e.fout());
            });

            color(TektonColor.ammonia.cpy(), TektonColor.methane.cpy(), Color.gray, e.fin());
            stroke((1.7f * e.fout()) * (1f + (intensity - 1f) / 2f));

            Draw.z(Layer.effect + 0.001f);
            randLenVectors(e.id + 1, e.finpow() + 0.001f, (int)(9 * intensity), 40f * intensity, (x, y, in, out) -> {
                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + out * 4 * (3f + intensity));
                Drawf.light(e.x + x, e.y + y, (out * 4 * (3f + intensity)) * 3.5f, Draw.getColor(), 0.8f);
            });
        });
    }),
	
	cyaneaExplosion = new Effect(30, 500f, b -> {
        float size = 5f;
        if (b.data instanceof Float s) {
        	size += s;
        }
        final float intensity = size;
        float baseLifetime = 25f + intensity * 11f;
        b.lifetime = 50f + intensity * 65f;
        color(Pal.surge);
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
                    Drawf.light(e.x + x, e.y + y, rad * 2.5f, Pal.reactorPurple, 0.5f);
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

            color(Pal.lighterOrange, Pal.reactorPurple, e.fin());
            stroke((2f * e.fout()));

            Draw.z(Layer.effect + 0.001f);
            randLenVectors(e.id + 1, e.finpow() + 0.001f, (int)(8 * intensity), 28f * intensity, (x, y, in, out) -> {
                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + out * 4 * (4f + intensity));
                Drawf.light(e.x + x, e.y + y, (out * 4 * (3f + intensity)) * 3.5f, Draw.getColor(), 0.8f);
            });
        });
    }),
	
	biologicalFallingEgg = new Effect(60f, e -> {
        String regionName = "tekton-egg";
        TextureRegion region = Core.atlas.find(regionName);
        float scale = region.scale;
        float rw = (region.width * scale) / 4f, rh = (region.height * scale) / 4f;
        
        float 
        		tx = e.x + 50f + Mathf.randomSeedRange(e.id, 20f),
        		ty = e.y + 160f + Mathf.randomSeedRange(e.id, 60f),
        		cx = Mathf.lerp(tx, e.x, e.fin()),
				cy = Mathf.lerp(ty, e.y, e.fin()),
        		rotation = (Mathf.atan2(cx - e.x, cy - e.y) * Mathf.radDeg) - 90f;
        
        Draw.z(Layer.effect + 1f);
        if (Mathf.chance(e.id * 8) && Mathf.chance(e.id) && !Vars.state.isPaused())
	        new Effect(110, f -> {
	            color(TektonColor.acid.cpy().a(f.color.a), TektonColor.methane.cpy().a(f.color.a), f.rotation);
	            Fill.circle(f.x, f.y, f.fout() * 3.5f);
	        }).wrap(Color.white.cpy().a(e.fin() + 0.1f)).at(cx + Mathf.range(7f), cy + Mathf.range(7f));
        
        Draw.alpha(e.fin());
        
        Draw.rect(region, cx, cy, rw, rh, rotation);
        
        //Fill.light(cx, cy, 10, 25f, Color.white, Color.white);
        
        Draw.z();
        Draw.color();

        Draw.reset();
        if (e.fin() >= 0.99f && !Vars.state.isPaused()) {
        	new ExplosionEffect() {{
                lifetime = 40f;
                waveStroke = 4f;
                waveColor = sparkColor = smokeColor = TektonColor.acid.cpy();
                waveRad = 15f;
                smokeSize = 5f;
                smokes = 8;
                smokeSizeBase = 0f;
                sparks = 8;
                sparkRad = 40f;
                sparkLen = 4f;
                sparkStroke = 3f;
            }}.at(e.x, e.y);
        }
    }),
	
	shootBig = new Effect(9, e -> {
        color(e.color.cpy(), Color.gray, e.fin());
        float w = 1.2f + 7 * e.fout();
        Drawf.tri(e.x, e.y, w, 25f * e.fout(), e.rotation);
        Drawf.tri(e.x, e.y, w, 4f * e.fout(), e.rotation + 180f);
    }),
	
	shootBig2 = new Effect(11, e -> {
        color(e.color.cpy(), Color.gray, e.fin());
        float w = 1.2f +9 * e.fout();
        Drawf.tri(e.x, e.y, w, 32f * e.fout(), e.rotation);
        Drawf.tri(e.x, e.y, w, 3f * e.fout(), e.rotation + 180f);
    }),
	
	shootBigSmoke = new Effect(17f, e -> {
        color(e.color.cpy().mul(1.2f), e.color, Color.gray, e.fin());

        randLenVectors(e.id, 8, e.finpow() * 19f, e.rotation, 10f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 2f + 0.2f);
        });
    }),

    shootBigSmoke2 = new Effect(18f, e -> {
        color(e.color.cpy().mul(1.2f), e.color, Color.gray, e.fin());

        randLenVectors(e.id, 9, e.finpow() * 23f, e.rotation, 20f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 2.4f + 0.2f);
        });
    }),
    
	blastExplosionColor = new Effect(22, e -> {
        color(e.color.cpy().mul(1.2f));

        e.scaled(6, i -> {
            stroke(3f * i.fout());
            Lines.circle(e.x, e.y, 3f + i.fin() * 15f);
        });

        color(Color.gray);

        randLenVectors(e.id, 5, 2f + 23f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 4f + 0.5f);
        });

        color(e.color);
        stroke(e.fout());

        randLenVectors(e.id + 1, 4, 1f + 23f * e.finpow(), (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f);
        });

        Drawf.light(e.x, e.y, 45f, e.color, 0.8f * e.fout());
    }),
	
	blastExplosionColorSlow = new Effect(60, e -> {
        color(e.color.cpy().mul(1.2f));

        e.scaled(6, i -> {
            stroke(3f * i.fout());
            Lines.circle(e.x, e.y, 3f + i.fin() * 15f);
        });

        color(e.color.cpy());

        randLenVectors(e.id, 5, 2f + 23f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 4f + 0.5f);
        });

        color(e.color);
        stroke(e.fout());

        randLenVectors(e.id + 1, 4, 1f + 23f * e.finpow(), (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f);
        });

        Drawf.light(e.x, e.y, 45f, e.color, 0.8f * e.fout());
    }),
	
	massiveExplosionColor = new Effect(30, e -> {
        color(e.color);

        e.scaled(7, i -> {
            stroke(3f * i.fout());
            Lines.circle(e.x, e.y, 4f + i.fin() * 30f);
        });

        color(Color.gray);

        randLenVectors(e.id, 8, 2f + 30f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 4f + 0.5f);
        });

        color(e.color);
        stroke(e.fout());

        randLenVectors(e.id + 1, 6, 1f + 29f * e.finpow(), (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 4f);
        });

        Drawf.light(e.x, e.y, 50f, e.color, 0.8f * e.fout());
    }),
	
	regenParticleHydrogen = new Effect(100f, e -> {
        color(Pal.heal);

        Fill.square(e.x, e.y, e.fslope() * 1.7f + 0.14f, 45f);
    }),
    
	regenParticleOxygen = new Effect(140f, e -> {
        color(Pal.heal);

        Fill.square(e.x, e.y, e.fslope() * 2f + 0.17f, 0f);
    }),
    
	instShoot = new Effect(24f, e -> {
        e.scaled(10f, b -> {
            color(Color.white, b.color, b.fin());
            stroke(b.fout() * 3f + 0.2f);
            Lines.circle(b.x, b.y, b.fin() * 50f);
        });

        color(e.color);

        for(int i : Mathf.signs){
            Drawf.tri(e.x, e.y, 13f * e.fout(), 85f, e.rotation + 90f * i);
            Drawf.tri(e.x, e.y, 13f * e.fout(), 50f, e.rotation + 20f * i);
        }

        Drawf.light(e.x, e.y, 180f, e.color, 0.9f * e.fout());
    }),
	
	instHit = new Effect(20f, 200f, e -> {
        color(e.color);

        for(int i = 0; i < 2; i++){
            color(i == 0 ? e.color : e.color);

            float m = i == 0 ? 1f : 0.5f;

            for(int j = 0; j < 5; j++){
                float rot = e.rotation + Mathf.randomSeedRange(e.id + j, 50f);
                float w = 23f * e.fout() * m;
                Drawf.tri(e.x, e.y, w, (80f + Mathf.randomSeedRange(e.id + j, 40f)) * m, rot);
                Drawf.tri(e.x, e.y, w, 20f * m, rot + 180f);
            }
        }

        e.scaled(10f, c -> {
            color(e.color);
            stroke(c.fout() * 2f + 0.2f);
            Lines.circle(e.x, e.y, c.fin() * 30f);
        });

        e.scaled(12f, c -> {
            color(e.color);
            randLenVectors(e.id, 25, 5f + e.fin() * 80f, e.rotation, 60f, (x, y) -> {
                Fill.square(e.x + x, e.y + y, c.fout() * 3f, 45f);
            });
        });
    }),
	
	instBomb = new Effect(15f, 100f, e -> {
        color(e.color);
        stroke(e.fout() * 4f);
        Lines.circle(e.x, e.y, 4f + e.finpow() * 20f);

        for(int i = 0; i < 4; i++){
            Drawf.tri(e.x, e.y, 6f, 80f * e.fout(), i*90 + 45);
        }

        color();
        for(int i = 0; i < 4; i++){
            Drawf.tri(e.x, e.y, 3f, 30f * e.fout(), i*90 + 45);
        }

        Drawf.light(e.x, e.y, 150f, e.color, 0.9f * e.fout());
    }),
	
	sparks = new Effect(30f, 20f, e -> {
        rand.setSeed(e.id + 1);
		color(e.color);
        stroke(e.fout() * 2f);
        float circleRad = 0.3f + e.finpow() * e.rotation;
        float range = 5.5f;
        float ex = rand.range(range);
        float ey = rand.range(range);

        for(int i = 0; i < 4; i++){
            Drawf.tri(e.x + ex, e.y + ey, 5f, e.rotation * 1.5f * e.fout(), i*90);
        }

        color();
        for(int i = 0; i < 4; i++){
            Drawf.tri(e.x + ex, e.y + ey, 2.5f, e.rotation * 1.45f / 3f * e.fout(), i*90);
        }

        Drawf.light(e.x + ex, e.y + ey, circleRad * 1.6f, Pal.heal, e.fout());
    }),
    
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
	    }) 
		{{followParent = false;}},
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
		})
	    {{followParent = false;}}
	)
	{{followParent = false;}},
	
	techSmoke = new MultiEffect(
			new Effect(40f, 160f, e -> {
		        color(Pal.techBlue);
		        stroke(e.fout() * 3f);
		        float circleRad = 6f + e.finpow() * 60f;
		        Lines.circle(e.x, e.y, circleRad);
		
		        rand.setSeed(e.id);
		        for(int i = 0; i < 16; i++){
		            float angle = rand.random(360f);
		            float lenRand = rand.random(0.5f, 1f);
		            Lines.lineAngle(e.x, e.y, angle, e.foutpow() * 50f * rand.random(1f, 0.6f) + 2f, e.finpow() * 70f * lenRand + 6f);
		        }
		    }) 
			{{followParent = false;}},
			new Effect(100f, 250f, b -> {
		        float intensity = 2.5f;
		
		        color(Pal.techBlue, 0.7f);
		        for(int i = 0; i < 4; i++){
		            rand.setSeed(b.id*2 + i);
		            float lenScl = rand.random(0.5f, 1f);
		            int fi = i;
		            b.scaled(b.lifetime * lenScl, e -> {
		                randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int)(2.9f * intensity), 20f * intensity, (x, y, in, out) -> {
		                    float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
		                    float rad = fout * ((2f + intensity) * 1.74f);
		
		                    Fill.circle(e.x + x, e.y + y, rad);
		                    Drawf.light(e.x + x, e.y + y, rad * 2f, b.color, 0.5f);
		                });
		            });
		        }
			})
		    {{followParent = false;}},
		    new Effect(240f, 100f, e -> {
		        color(e.color);
		        float scl = 20f;
		        float circleRad = 4f + e.finpow() * scl;

		        for(int i = 0; i < 4; i++){
		            Drawf.tri(e.x, e.y, 6f, scl * 1.5f * e.fout(), i*90);
		        }

		        color();
		        for(int i = 0; i < 4; i++){
		            Drawf.tri(e.x, e.y, 3f, scl * 1.45f / 3f * e.fout(), i*90);
		        }

		        Drawf.light(e.x, e.y, circleRad * 1.6f, e.color, e.fout());
		    })
		    {{followParent = false;}}
		)
		{{followParent = false;}},
	
	techCharge = new Effect(80f, 80f, e -> {
        color(Pal.techBlue);
        stroke(e.fin() * 2f);
        Lines.circle(e.x, e.y, 4f + e.fout() * 50f);

        Fill.circle(e.x, e.y, e.fin() * 10f);

        randLenVectors(e.id, 20, 40f * e.fout(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fin() * 5f);
            Drawf.light(e.x + x, e.y + y, e.fin() * 15f, Pal.techBlue, 0.7f);
        });

        color();

        Fill.circle(e.x, e.y, e.fin() * 10);
        Drawf.light(e.x, e.y, e.fin() * 20f, Pal.techBlue, 0.7f);
    }).followParent(true).rotWithParent(true),
	
	weaponLockEffect = new Effect(40f, e -> {
        color(Pal.techBlue, Pal.techBlue, e.fin());
        
        stroke(1f * e.fslope());
        Lines.square(e.x, e.y, 7f, 45f);
        Drawf.light(e.x, e.y, 9f, e.color, e.fout() * 0.7f);
    }),
	
	waveTechTrail = new Effect(20f, e -> {
        Draw.z(Layer.bullet - 0.0001f);
        color(Pal.techBlue);
        //Lines.beginLine();
        stroke(2f * e.fslope());
        
        var width = 10f;
        float 
        	x1 = e.x + (Mathf.cosDeg(e.rotation + 90f) * width * e.fout()), 
        	y1 = e.y + (Mathf.sinDeg(e.rotation + 90f) * width * e.fout()), 
        	x2 = e.x + (Mathf.cosDeg(e.rotation + 90f*-1f) * width * e.fout()), 
        	y2 = e.y + (Mathf.sinDeg(e.rotation + 90f*-1f) * width * e.fout());
        
        line(x1, y1, x2, y2);
        
        color();
    	//blend();
        Draw.z();
    }),
	
	techDamageBlock = new MultiEffect(new Effect(60f, e -> {
        if(!(e.data instanceof Block block)) return;
        
        mixcol(e.color, 1f);
        alpha(e.fout());
        Draw.rect(block.fullIcon, e.x, e.y);
    }), new Effect(120f, e -> {
        if(!(e.data instanceof Block block)) return;
        
        color(Pal.techBlue);
        alpha(1f);
        stroke(1.7f * e.fslope());
        Lines.square(e.x, e.y, (block.size * tilesize) / 2f, 45f);
    })),
	
	concentrationChargeEffect = new Effect(60f, 100f, e -> {
    	TektonFx.rand.setSeed(e.id + 1);
		color(e.color);
        stroke(e.fin() * 2f);
        float circleRad = 0.3f + e.finpow() * e.rotation;
        float range = 1f;
        float ex = TektonFx.rand.range(range);
        float ey = TektonFx.rand.range(range);

        for(int i = 0; i < 4; i++){
            Drawf.tri(e.x + ex, e.y + ey, 5f, 20f * e.fin(), i*90);
        }

        color();
        for(int i = 0; i < 4; i++){
            Drawf.tri(e.x + ex, e.y + ey, 2.5f, 20f / 3f * e.fin(), i*90);
        }

        Drawf.light(e.x + ex, e.y + ey, circleRad * 1.6f, e.color, e.fin());
    }),
	
	methanespark = new Effect(18, e -> {
        randLenVectors(e.id, 5, e.fin() * 8f, (x, y) -> {
            color(TektonColor.methane, Color.gray, e.fin());
            Fill.circle(e.x + x, e.y + y, e.fout() * 4f /2f);
        });
    }),
	
	oxygenCombustionSmoke = new Effect(240f, e -> {
        color(TektonColor.oxygen);
        alpha(0.5f);

        rand.setSeed(e.id);
        for(int i = 0; i < 4; i++){
            float len = rand.random(8f), rot = rand.range(15f) + e.rotation;

            e.scaled(e.lifetime * rand.random(0.3f, 1f), b -> {
                v.trns(rot, len * b.finpow());
                Fill.circle(e.x + v.x, e.y + v.y, 2f * b.fslope() + 0.2f);
            });
        }
    }),
	
	acidGenerateSmoke = new Effect(300f, e -> {
        color(TektonColor.acid.cpy().mul(1.1f));
        alpha(0.6f);

        rand.setSeed(e.id);
        for(int i = 0; i < 6; i++){
            float len = rand.random(10f), rot = rand.range(45f) + e.rotation;

            e.scaled(e.lifetime * rand.random(0.3f, 1f), b -> {
                v.trns(rot, len * b.finpow());
                Fill.circle(e.x + v.x, e.y + v.y, 2f * b.fslope() + 0.2f);
            });
        }
    }),
	
	nuclearSmoke = new Effect(240f, e -> {
        color(Liquids.water.gasColor);
        alpha(0.4f);

        rand.setSeed(e.id);
        for(int i = 0; i < 5; i++){
            float len = rand.random(10f), rot = rand.range(120f) + e.rotation;

            e.scaled(e.lifetime * rand.random(0.3f, 1f), b -> {
                v.trns(rot, len * b.finpow());
                Fill.circle(e.x + v.x, e.y + v.y, 3f * b.fslope() + 0.2f);
            });
        }
    }),
    
	nuclearExplosion = new Effect(30, 700f, b -> {
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
	
	nuclearFusionExplosion = new Effect(30, 1500f, b -> {
        float intensity = 15f;
        float baseLifetime = 25f + intensity * 15f;
        b.lifetime = 50f + intensity * 64f;

        color(Pal.lighterOrange);
        alpha(0.8f);
        for(int i = 0; i < 5; i++){
            rand.setSeed(b.id*2 + i);
            float lenScl = rand.random(0.25f, 1f);
            int fi = i;
            b.scaled(b.lifetime * lenScl, e -> {
                randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int)(2.8f * intensity), 25f * intensity, (x, y, in, out) -> {
                    float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                    float rad = fout * ((2f + intensity) * 2.35f);

                    Fill.circle(e.x + x, e.y + y, rad);
                    Drawf.light(e.x + x, e.y + y, rad * 2.6f, Pal.lighterOrange, 0.7f);
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

            color(Color.white, Pal.lighterOrange, e.fin());
            stroke((2f * e.fout()));

            Draw.z(Layer.effect + 0.001f);
            randLenVectors(e.id + 1, e.finpow() + 0.001f, (int)(8 * intensity), 30f * intensity, (x, y, in, out) -> {
                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + out * 4 * (4f + intensity));
                Drawf.light(e.x + x, e.y + y, (out * 4 * (3f + intensity)) * 3.5f, Draw.getColor(), 0.8f);
            });
        });
    }),
	
	tempestChain = new Effect(30f, 300f, e -> {
                if(!(e.data instanceof Position p)) return;
                float tx = p.getX(), ty = p.getY(), dst = Mathf.dst(e.x, e.y, tx, ty);
                Tmp.v1.set(p).sub(e.x, e.y).nor();

                float normx = Tmp.v1.x, normy = Tmp.v1.y;
                float range = 10f;
                int links = Mathf.ceil(dst / range);
                float spacing = dst / links;

                Lines.stroke(5f * e.fout());
                Draw.color(Pal.lightishOrange, e.color, e.fin());
                Fill.circle(e.x, e.y, e.fout() * 6f);

                Lines.beginLine();

                Lines.linePoint(e.x, e.y);

                rand.setSeed(e.id);

                for(int i = 0; i < links; i++){
                    float nx, ny;
                    if(i == links - 1){
                        nx = tx;
                        ny = ty;
                        Fill.circle(e.x, e.y, e.fout() * 6f);
                    }else{
                        float len = (i + 1) * spacing;
                        Tmp.v1.setToRandomDirection(rand).scl(range/2f);
                        nx = e.x + normx * len + Tmp.v1.x;
                        ny = e.y + normy * len + Tmp.v1.y;
                    }

                    Lines.linePoint(nx, ny);
                }

                Lines.endLine();
            }).followParent(false).rotWithParent(false),

    blockExplosionSmoke = new Effect(30, e -> {
        color(Color.gray);

        randLenVectors(e.id, 6, 4f + 30f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 3f);
            Fill.circle(e.x + x / 2f, e.y + y / 2f, e.fout());
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
    	colorTo = TektonColor.acid;
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
			    	colorTo = TektonColor.acid;
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
			    	colorTo = TektonColor.acid;
    			}}
			};
    }},
	
	shootColorBig = new Effect(10, e -> {
        color(Color.white, e.color, e.fin());
        float w = 1.3f + 10 * e.fout();
        Drawf.tri(e.x, e.y, w, 35f * e.fout(), e.rotation);
        Drawf.tri(e.x, e.y, w, 6f * e.fout(), e.rotation + 180f);
    }),
	
	shootColorSmokeBig = new Effect(70f, e -> {
        rand.setSeed(e.id);
        for(int i = 0; i < 13; i++){
            v.trns(e.rotation + rand.range(30f), rand.random(e.finpow() * 40f));
            e.scaled(e.lifetime * rand.random(0.3f, 1f), b -> {
                color(Color.white, e.color, b.fin());
                Fill.circle(e.x + v.x, e.y + v.y, b.fout() * 3.4f + 0.3f);
            });
        }
    }),
	
	colorDebris = new Effect(3600f, 300f, e -> {
		if(headless) return;
        rand.setSeed(e.id * 2);
        TextureRegion region = Core.atlas.find("scorch-" + (int)e.rotation + "-" + rand.random(2));
        var z = Draw.z();
        Draw.z(Layer.debris);
        Draw.color(e.color.cpy().mul(0.85f).a(e.foutpow()));
        Draw.blend(Blending.normal);
        Draw.rect(region, e.x, e.y, Mathf.randomSeed(e.id, 0f, 360f));
        Draw.blend(Blending.additive);
        //Draw.rect(region, e.x, e.y, e.rotation);
        Draw.z(z);
        Draw.blend();
        Draw.color();
        Draw.reset();
    }),
    
    incinerateHydrogen = new Effect(34, e -> {
        randLenVectors(e.id, 4, e.finpow() * 5f, (x, y) -> {
            color(Pal.techBlue, Color.gray, e.fin());
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
    }).followParent(false).rotWithParent(false),
	
	teamColorDespawn = new Effect(60f, e -> {
        if(!(e.data instanceof Unit select) || select.type == null) return;

        float scl = e.fout(Interp.pow2Out);
        float p = Draw.scl;
        Draw.scl *= scl;

        mixcol(select.team.color, 1f);
        rect(select.type.fullIcon, select.x, select.y, select.rotation - 90f);
        reset();

        Draw.scl = p;
    }),
	
	debugRedSquare = new Effect(1200f, e -> {
        color(Color.red);
        Lines.stroke(1f);
        Lines.square(e.x,  e.y, 3f);
        reset();
    }),
	
	debugGreenSquare = new Effect(1200f, e -> {
        color(Color.green);
        var z = z();
        z(z + 0.0001f);
        Lines.stroke(1f);
        Lines.square(e.x,  e.y, 3f);
        z(z);
        reset();
    })

    ;
    
	public static void load() {
		biologicalFallingEgg.clip = 10000f;
		concentrationChargeEffect.followParent = true;
	}
	
	private static Effect copyEffect(Effect effect) {
		var Ceffect = effect;
		Ceffect.followParent(true);
		Ceffect.rotWithParent(true);
		return Ceffect;
	}
}
