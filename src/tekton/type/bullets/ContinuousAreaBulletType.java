package tekton.type.bullets;

import arc.Events;
import arc.audio.Sound;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.ContinuousBulletType;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Posc;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;

//length = radius in this bullet
public class ContinuousAreaBulletType extends ContinuousBulletType {
	public static final int hitEffectIntervalTimer = 2, trailIntervalTimer = 3;
	
    public float fadeTime = 16f;
    public float lightStroke = 40f;
	public float strokeFrom = 0f, strokeTo = 3f, shapeRotationSpeed = 1f, spikeOuterRadius = 0, lineStrokeFrom = 0f, lineStrokeTo = 3f, linesRotationSpeed = -1f, linesInnerSize = -16f, linesOuterSize = 16f;
	public int shapeSides = 8, lines = 0;
	public boolean circle = true;
	public Color colorFrom = new Color(1f, 0f, 0f, 1f), colorTo = new Color(1f, 1f, 1f, 1f);
    public Interp radiusInterp = Interp.slope, strokeInterp = Interp.slope;
    public boolean hitEffectAtOwner = true;
    
    public float oscScl = 1.2f, oscMag = 0.02f;
    
    public boolean drawFlare = false;
    public Color flareColor = hitColor;
    public float flareWidth = 3f, flareInnerScl = 0.5f, flareLength = 40f, flareInnerLenScl = 0.5f, flareLayer = Layer.bullet - 0.0001f, flareRotSpeed = 1.2f;
    public boolean rotateFlare = false;
	
	public float hitEffectInterval = 10f;
	public float hitEffectSpread = 0, hitEffectSizeScaleSpread = 0;
	
	public Sound loopSound = Sounds.none;
	public float loopSoundVolume = 1f;
	 
	public ContinuousAreaBulletType() {
        super();
        collidesGround = collidesTiles = collides = false;
        hitEffect = despawnEffect = trailEffect = Fx.none;
        optimalLifeFract = 0.5f;
        lifetime = 16f;
        impact = false;
        setDefaults = false;
        despawnHit = false;
	}

    @Override
    public void update(Bullet b) {
    	super.update(b);

    	if (loopSound != Sounds.none)
    		Vars.control.sound.loop(loopSound, b, loopSoundVolume * b.fin());
    }

	@Override
    public void applyDamage(Bullet b){
        float damage = b.damage;
        if(timescaleDamage && b.owner instanceof Building build)
             b.damage *= build.timeScale();
        
        boolean hitEffectNow = b.timer(hitEffectIntervalTimer, hitEffectInterval);
        var data = b.data;
        
        b.data = false;
        
        Units.nearbyEnemies(b.team, b.x, b.y, currentRadius(b) + 1f, other -> {
        	if(other.team != b.team && other.hittable() && ((other.isFlying() && collidesAir) || (other.isGrounded() && collidesGround))) {
        		if(pierceArmor){
            		other.damagePierce(damage);
                }else if(armorMultiplier != 1){
                	other.damageArmorMult(damage, armorMultiplier);
                }else{
                	other.damage(damage);
                }
        		
        		if(hitEffectNow){
            		b.data = true;
        			if (hitEffect != Fx.none) {
        				Vec2 pos;
        				
        				if (b.owner instanceof Posc posOwner && hitEffectAtOwner) {
        					pos = new Vec2(posOwner.x(), posOwner.y());
        				}
        				else {
        					pos = new Vec2(b.x, b.y);
        				}
        				
        				hitEffect.at(
        						pos.x + Angles.trnsx(Mathf.range(180f), Mathf.random(hitEffectSpread + hitEffectSizeScaleSpread * other.hitSize)), 
        						pos.y + Angles.trnsy(Mathf.range(180f), Mathf.random(hitEffectSpread + hitEffectSizeScaleSpread * other.hitSize)), 
        						other.rotation(), hitColor, other);
        			}
                }
        		
            	var angle = Mathf.atan2(other.x - b.x, other.y - b.y);
            	var direction = new Vec2(Mathf.cos(angle), Mathf.sin(angle)).scl((knockback * 60f * b.fout()));
                other.impulse(direction);
                other.apply(status, statusDuration);
        	}
        });
        
        if ((boolean)b.data == true)
        	hitSound.at(b, hitSoundVolume);
        
        b.data = data;
        
        b.damage = damage;
    }
	
	@Override
	public void draw(Bullet b) {
        Color mix = Tmp.c1.set(colorFrom).lerp(colorTo, b.fin());
        
        float rot = shapeRotationSpeed * Time.time;
        
        Draw.color(mix);
        Lines.stroke(Mathf.lerp(strokeFrom, strokeTo, currentStrokeInterp(b)));
        
        if (circle)
        	Lines.circle(b.x, b.y, currentRadius(b));
        else {
        	Vec2[] verts = new Vec2[shapeSides * 2];
    		float rotOffset = 0f, cradius = currentRadius(b);
    		
        	for (int i = 0; i < verts.length; i += 2) {
        		rotOffset += (360f / verts.length) * 2f;
        		verts[i] = new Vec2(Angles.trnsx(rot + rotOffset, cradius), Angles.trnsy(rot + rotOffset, cradius));
        	}
        	
        	rotOffset = 360f / verts.length;
        	cradius = currentRadiusInterp(b) * spikeOuterRadius;
    		Vec2 first = new Vec2(), second = new Vec2();
        	
        	for (int i = 1; i < verts.length; i += 2) {
        		rotOffset += (360f / verts.length) * 2f;
        		
    			first = verts[i - 1];
        		
        		if (i + 1 < verts.length)
        			second = verts[i + 1];
        		else
        			second = verts[0];

        		verts[i] = new Vec2(((first.x + second.x) * .5f) + Angles.trnsx(rot + rotOffset, cradius), ((first.y + second.y) * .5f) + Angles.trnsy(rot + rotOffset, cradius));
        	}
        	
        	Lines.poly(verts, b.x, b.y, 1f);
        }

    	float inner = currentRadius(b) + (linesInnerSize * b.fout()), outer = currentRadius(b) + (linesOuterSize * b.fout());
        Lines.stroke(Mathf.lerp(lineStrokeFrom, lineStrokeTo, currentStrokeInterp(b)));
    	float lrot = linesRotationSpeed * Time.time;
        for (int i = 0; i < lines; i++) {
        	float clrot = ((360f / lines) * i) + lrot;
            Lines.line(b.x + Angles.trnsx(clrot, inner), b.y + Angles.trnsy(clrot, inner), b.x + Angles.trnsx(clrot, outer), b.y + Angles.trnsy(clrot, outer));
        }

        float sin = Mathf.sin(Time.time, oscScl, oscMag);
        
        if(drawFlare){
        	Draw.color(flareColor);
            Draw.z(flareLayer);

            float angle = Time.time * flareRotSpeed + (rotateFlare ? b.rotation() : 0f);

            for(int i = 0; i < 4; i++){
                Drawf.tri(b.x, b.y, flareWidth, flareLength * (currentRadiusInterp(b) + sin), i*90 + 45 + angle);
            }

            Draw.color();
            for(int i = 0; i < 4; i++){
                Drawf.tri(b.x, b.y, flareWidth * flareInnerScl, flareLength * flareInnerLenScl * (currentRadiusInterp(b) + sin), i*90 + 45 + angle);
            }
        }
        
        if (b.timer(trailIntervalTimer, trailInterval)) {
        	if (b.owner instanceof Posc pos)
        		trailEffect.at(pos.x(), pos.y(), b.rotation(), trailColor, b.owner);
        	else
        		trailEffect.at(b.x(), b.y(), b.rotation(), trailColor, b.owner);
        }

        Draw.reset();
	}
    
    public float currentRadiusInterp(Bullet b) {
    	return b.fin(radiusInterp);
    }
    
    public float currentStrokeInterp(Bullet b) {
    	return b.fin(strokeInterp);
    }

    @Override
    protected float calculateRange() {
        return splashDamageRadius;
    }

    public float currentRadius(Bullet b) {
        return splashDamageRadius * currentRadiusInterp(b);
    }

    @Override
    public float estimateDPS() {
        if(!continuous) return super.estimateDPS();
        return damage * 60f / damageInterval;
    }
}
