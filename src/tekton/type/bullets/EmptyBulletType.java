package tekton.type.bullets;

import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;

public class EmptyBulletType extends BulletType{
	
	public static EmptyBulletType empty = new EmptyBulletType();

    public EmptyBulletType(){
    	super();
    	damage = 0f;
        reflectable = absorbable = hittable = collidesGround = collidesAir = collidesTiles = collides = false;
        speed = 0f;
        keepVelocity = false;
        hitEffect = despawnEffect = shootEffect = smokeEffect = Fx.none;
    }
}
