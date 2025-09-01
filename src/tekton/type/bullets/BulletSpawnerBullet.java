package tekton.type.bullets;

import arc.audio.Sound;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.content.Fx;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;

public class BulletSpawnerBullet extends BulletType { //horrible name
	public float spawnRadius = 4f * 80f;
	public boolean randomRotation = true;
	public Sound spawnSound = null;
	
	public BulletSpawnerBullet() {
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
	
	public BulletSpawnerBullet(float lifetime, float radius) {
		this();
		this.lifetime = lifetime;
		spawnRadius = radius;
	}
	
	public BulletSpawnerBullet(float lifetime, float radius, BulletType SpawnBullet) {
		this(lifetime, radius);
		this.intervalBullet = SpawnBullet;
	}
	
	@Override
	public void update(Bullet b){
		if (intervalBullet != null)
			if (b.timer.get(5, bulletInterval)) {
				float x = b.x + (Mathf.random(spawnRadius) * Mathf.cosDeg(Mathf.random(360f))), y = b.y + (Mathf.random(spawnRadius) * Mathf.sinDeg(Mathf.random(360f)));
				intervalBullet.create(b, x, y, randomRotation ? Mathf.random(360f) : 0f);
				if (spawnSound != null)
					spawnSound.at(x, y);
			}
    }
}
