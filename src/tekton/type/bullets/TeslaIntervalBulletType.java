package tekton.type.bullets;

import arc.math.geom.Position;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.entities.Units;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;

public class TeslaIntervalBulletType extends BasicBulletType {
	public TeslaIntervalBulletType() {
		super();
		intervalBullet = new TeslaBulletType();
	}

	public TeslaIntervalBulletType(float speed, float damage) {
		super(speed, damage);
		intervalBullet = new TeslaBulletType();
	}

	public void updateBulletInterval(Bullet b) {
    	final Seq<Position> pos = new Seq<>();
        if(intervalBullet != null && b.time >= intervalDelay && b.timer.get(2, bulletInterval) && intervalBullet instanceof TeslaBulletType tesla) {
            Vars.indexer.allBuildings(b.x, b.y, tesla.maxRange, other -> {
                if(b.team != other.team) {
                	pos.add(b);
                }
            });
            Units.nearbyEnemies(b.team, b.x, b.y, tesla.maxRange, other -> {
                if(other.team != b.team && other.hittable()) {
                	pos.add(b);
                }
            });
        	pos.shuffle();
            for(int i = 0; i < intervalBullets; i++) {
            	intervalBullet.create(b, b.x, b.y, b.angleTo(pos.get(i)));
            }
        }
    }
}
