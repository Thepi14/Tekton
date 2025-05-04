package tekton.content;

import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import mindustry.game.Team;
import mindustry.gen.Bullet;
import mindustry.gen.Groups;
import mindustry.type.StatusEffect;
import tekton.type.bullets.*;

public final class TektonDamage {
	public static final float RADIATION_EFFECT_CHANCE = 0.005f;
	
	public static Bullet createRadiationArea(float x, float y, float lifetime, float radius) {
		var bullet = new RadiationAreaBullet(lifetime, radius);
		bullet.statusDuration = 60f * 5f;
		bullet.effectChance = RADIATION_EFFECT_CHANCE * radius;
		var ent = bullet.create(Groups.unit.first(), Team.derelict, x, y, Mathf.random(360f));
		return ent;
	}
	
	public static Bullet createRadiationArea(Vec2 position, float lifetime, float radius) {
		return createRadiationArea(position.x, position.y, lifetime, radius);
	}
	
	public static Bullet createRadiationArea(Position position, float lifetime, float radius) {
		return createRadiationArea(position.getX(), position.getY(), lifetime, radius);
	}
}
