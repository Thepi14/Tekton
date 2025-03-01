package tekton;

import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;

public class EmptyBulletType extends BulletType{
	
	public static EmptyBulletType empty = new EmptyBulletType();

    public EmptyBulletType(){
        reflectable = absorbable = hittable = collidesGround = collidesAir = collidesTiles = false;
        speed = 0f;
        keepVelocity = false;
        hitEffect = despawnEffect = shootEffect = smokeEffect = Fx.none;
    }
}
