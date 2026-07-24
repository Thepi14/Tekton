package tekton.type.bullets;

import arc.graphics.Color;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Lightning;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.ParticleEffect;
import mindustry.entities.effect.WaveEffect;
import mindustry.gen.Bullet;
import tekton.content.TektonColor;

public class HomingTeslaBulletType extends BulletType{
	 public Effect chainEffect = Fx.chainEmp.wrap(TektonColor.redShootColorLightning),
	 applyEffect = Fx.titanExplosion.wrap(Color.valueOf("ffaaaa"));
	 public int chains = 3;
	 
    public float lengthRand = 100f;

    public HomingTeslaBulletType() {
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
		 despawnHit = false;
		 fragOnDespawn = false;
		 setDefaults = false;

		 //just make it massive, users of this bullet can adjust as necessary
		 drawSize = 1000f;
    }

   @Override
   public void init(Bullet b){
       super.init(b);
       var maxDistX = b.x + Angles.trnsx(b.rotation(), maxRange - Mathf.range(0f, lengthRand));
       var maxDistY = b.y + Angles.trnsy(b.rotation(), maxRange - Mathf.range(0f, lengthRand));
	   var absorber = Damage.findAbsorber(b.team, b.x, b.y, maxDistX, maxDistY);

       if(absorber != null &&absorbable){
           hitAt(b, absorber.x, absorber.y);
       }
       else {
    	   hitAt(b, maxDistX, maxDistY);
       }

       Effect.shake(hitShake, hitShake, b);
   }

   @Override
   public void hit(Bullet b, float x, float y){}

   public void hitAt(Bullet b, float x, float y){
	   var posX = x;
	   var posY = y;
	   var bPos = new Vec2(x, y);

	   Seq<Vec2> positions = new Seq<>();

	   if (collidesGround) {
		   Vars.indexer.allBuildings(posX, posY, homingRange, other -> {
	           if(b.team != other.team){
	        	   positions.add(new Vec2(other.x, other.y));
	           }
	       });
	   	}

	   Units.nearbyEnemies(b.team, posX, posY, homingRange, other -> {
		   if(other.team != b.team && other.hittable() && ((other.isGrounded() && collidesGround) || (other.isFlying() && collidesAir))){
			   if(b.team != other.team){
				   positions.add(new Vec2(other.x, other.y));
	           }
		   }
       });

	   Vec2 fPos = bPos;
	   if (positions.size > 0) {
		   fPos = new Vec2(-10000f, -10000f);
		   for (var pos : positions) {
			   if (pos.dst(bPos) < fPos.dst(bPos)) {
				fPos = pos;
			}
		   }
	   }

	   posX = fPos.x; //posX += Mathf.range(-lengthRand / 4f, lengthRand / 4f);
	   posY = fPos.y; //posY += Mathf.range(-lengthRand / 4f, lengthRand / 4f);

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

       if (fragBullet != null) {
		fragBullet.create(b, b.team, posX, posY, b.rotation());
	}

       createSplashDamage(b, posX, posY);
   }
}
