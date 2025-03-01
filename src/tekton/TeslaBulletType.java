package tekton;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.lineAngle;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;

import arc.graphics.Color;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Log;
import mindustry.content.Fx;
import mindustry.entities.*;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.ParticleEffect;
import mindustry.entities.effect.WaveEffect;
import mindustry.gen.*;

public class TeslaBulletType extends BulletType{
     public float trailSpacing = 10f;
     
	 public Effect chainEffect = Fx.chainEmp.wrap(Color.valueOf("ff4545")),
	 applyEffect = Fx.titanExplosion.wrap(Color.valueOf("ffaaaa"));
	 public int chains = 3;
     
     public TeslaBulletType(){
         removeAfterPierce = false;
         speed = 0f;
         hitEffect = new MultiEffect() {{
         	effects = new Effect[]{
        			new WaveEffect() {{
            	    	sizeFrom = 2;
            	    	sizeTo = 14;
            	    	lifetime = 10;
            	    	strokeFrom = 2;
            	    	strokeTo = 0;
            	    	colorFrom = Color.valueOf("ffffff");
            	    	colorTo = Color.valueOf("ff4545");
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
            			colorTo = Color.valueOf("ff4545");
            		}}
        	};
        }};
        maxRange = 100f;
		 despawnEffect = Fx.none;
         lifetime = 20f;
         impact = true;
         keepVelocity = false;
         collides = false;
         pierce = true;
         hittable = false;
         absorbable = true;
         optimalLifeFract = 0.5f;
         shootEffect = smokeEffect = Fx.none;
         despawnHit = true;

         //just make it massive, users of this bullet can adjust as necessary
         drawSize = 1000f;
     }

    @Override
    public void init(Bullet b){
        super.init(b);
        var pos = new Vec2(b.x, b.y);
        var maxDistX = pos.dst(b.aimX, b.aimY) >= maxRange ? b.x + (Mathf.cosDeg(b.rotation()) * maxRange) : b.aimX;
        var maxDistY = pos.dst(b.aimX, b.aimY) >= maxRange ? b.y + (Mathf.sinDeg(b.rotation()) * maxRange) : b.aimY;
        var absorber = Damage.findAbsorber(b.team, b.x, b.y, maxDistX, maxDistY);
        
        if(absorber != null && absorbable){
        	hitAt(b, absorber.x, absorber.y);
        }
        else {
        	hitAt(b, maxDistX, maxDistY);
        }
        
        Effect.shake(hitShake, hitShake, b);
    }
    
    public void hitAt(Bullet b, float posX, float posY) {
        hitSound.at(b.x, b.y, hitSoundPitch, hitSoundVolume);
        hitSound.at(posX, posY, hitSoundPitch, hitSoundVolume);
        applyEffect.at(posX, posY, b.rotation(), hitColor);
    	hitEffect.at(b.x, b.y, b.rotation(), hitColor);
    	
    	var bulletPos = EmptyBulletType.empty.create(b, posX, posY, b.rotation());
    	for (var i = 0; i < chains; i++) {
            chainEffect.at(b.x, b.y, 0, hitColor, bulletPos);
    	}
    	
        Damage.collidePoint(b, b.team, hitEffect, posX, posY);

        for(int i = 0; i < lightning; i++){
            Lightning.create(b, lightningColor, lightningDamage < 0 ? damage : lightningDamage, posX, posY, b.rotation() + Mathf.range(lightningCone/2) + lightningAngle, lightningLength + Mathf.random(lightningLengthRand));
        }
        
        createPuddles(b, posX, posY);
        createIncend(b, posX, posY);
        createUnits(b, posX, posY);

        createSplashDamage(b, posX, posY);
    }
    
    public void hit(Bullet b, float x, float y){
    	
    }
}
