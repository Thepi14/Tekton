package tekton.type.bullets;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.type.StatusEffect;

public class StatusEffectAreaBulletType extends BulletType {
	public float statusRadius = 4f * 80f;
	public Effect applyStatusEffect = Fx.none;
	public Effect areaEffect = Fx.none;
	public float effectChance = 0.1f;
	public StatusEffect[] statuses;
	
	public StatusEffectAreaBulletType() {
		super();
		damage = 0f;
		speed = 0;
		absorbable = false;
		hittable = false;
		collideFloor = false;
		collideTerrain = false;
		collidesTiles = false;
		collides = false;
		statusDuration = 60f;
		collidesGround = collidesAir = true;
		shootEffect = smokeEffect = hitEffect = despawnEffect = Fx.none;
		keepVelocity = false;
	}
	
	public StatusEffectAreaBulletType(float lifetime, float radius) {
		this();
		this.lifetime = lifetime;
		statusRadius = radius;
	}
	
	@Override
	public void update(Bullet b){
        updateTrail(b);
        updateHoming(b);
        updateWeaving(b);
        updateTrailEffects(b);
        updateBulletInterval(b);

		if (Mathf.randomBoolean(effectChance))
			areaEffect.at(new Vec2(b.x + (Mathf.random(statusRadius) * Mathf.cosDeg(Mathf.random(360f))), b.y + (Mathf.random(statusRadius) * Mathf.sinDeg(Mathf.random(360f)))));
        Units.nearbyEnemies(b.team, b.x, b.y, statusRadius, other -> {
			if(b.team != other.team && other.hittable() && ((collidesGround && other.isGrounded()) || (collidesAir && other.isFlying()))) {
				if (statuses != null && statuses.length > 0) {
					for (var status : statuses) {
						other.apply(status, statusDuration);
					}
				}
				else {
					other.apply(status, statusDuration);
				}
				applyStatusEffect.at(other);
            }
        });
    }
}
