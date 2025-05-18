package tekton.type.bullets;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.content.Fx;
import mindustry.entities.Units;
import mindustry.gen.Bullet;
import mindustry.type.StatusEffect;
import tekton.content.TektonStatusEffects;

public class RadiationAreaBullet extends StatusEffectAreaBulletType {
	public RadiationAreaBullet() {
		super();
		statuses = new StatusEffect[] { TektonStatusEffects.radioactiveContamination, TektonStatusEffects.radiationAbsorption };
		areaEffect = TektonStatusEffects.radioactiveContamination.effect;
	}
	
	public RadiationAreaBullet(float lifetime, float radius) {
		super(lifetime, radius);
		statuses = new StatusEffect[] { TektonStatusEffects.radioactiveContamination, TektonStatusEffects.radiationAbsorption };
		areaEffect = TektonStatusEffects.radioactiveContamination.effect;
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
			if(b.team != other.team && ((collidesGround && other.isGrounded()) || (collidesAir && other.isFlying()))) {
				if (!other.isImmune(statuses[0]) && !other.isImmune(statuses[1])) {
					other.apply(statuses[0], statusDuration);
				}
				else {
					for (var status : statuses) {
						other.apply(status, statusDuration);
					}
				}
				applyStatusEffect.at(other);
            }
        });
    }
}
