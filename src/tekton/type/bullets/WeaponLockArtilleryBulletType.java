package tekton.type.bullets;

import static mindustry.Vars.indexer;

import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.ArtilleryBulletType;
import mindustry.gen.Bullet;
import mindustry.world.blocks.defense.turrets.Turret;
import tekton.math.TekMath;
import tekton.type.biological.BiologicalBlock;

public class WeaponLockArtilleryBulletType extends ArtilleryBulletType {
    public Effect damageEffect = Fx.none;
    public float weaponLockDuration = 120f;

	public WeaponLockArtilleryBulletType(float speed, float damage, String bulletSprite){
        super(speed, damage, bulletSprite);
    }

    public WeaponLockArtilleryBulletType(float speed, float damage){
        this(speed, damage, "shell");
    }

    public WeaponLockArtilleryBulletType(){
        this(1f, 1f, "shell");
    }

    @Override
    public void update(Bullet b){
        super.update(b);
    }

    @Override
    public void createSplashDamage(Bullet b, float x, float y){
        if(splashDamageRadius > 0 && !b.absorbed){
            /*Damage.damage(b.team, x, y, splashDamageRadius, splashDamage * b.damageMultiplier(), splashDamagePierce, collidesAir, collidesGround, scaledSplashDamage, b);*/

            if(status != StatusEffects.none){
                Damage.status(b.team, x, y, splashDamageRadius, status, statusDuration, collidesAir, collidesGround);
            }

            Units.nearbyEnemies(b.team, x, y, splashDamageRadius, other -> {
                if(other.team != b.team && other.hittable() && ((other.isGrounded() && collidesGround) || (other.isFlying() && collidesAir)) && TekMath.insideDiamond(x,  y, other.x, other.y, splashDamageRadius)) {
                	var dst = TekMath.insideDiamondDst(x, y, other.x, other.y);
                	var dmg = splashDamage * Mathf.clamp((1.05f - (dst / splashDamageRadius)));
                    other.damage(dmg, splashDamagePierce);
                    if(status != StatusEffects.none) {
                    	other.apply(status, statusDuration);
                    }
                }
            });

            indexer.eachBlock(null, x, y, splashDamageRadius, other -> TekMath.insideDiamond(x,  y, other.x, other.y, splashDamageRadius), other -> {
            	if (other.team != b.team) {
                	var dst = TekMath.insideDiamondDst(x, y, other.x, other.y);
                	var dmg = splashDamage * Mathf.clamp((1.05f - ((dst - (other.hitSize() / 2f)) / splashDamageRadius)));

                    if (other.health > dmg && other.block instanceof Turret blc && !(blc instanceof BiologicalBlock)) {
                    	other.applySlowdown(0f, weaponLockDuration);
                    	damageEffect.at(other.x, other.y, 0f, hitColor, other.block);
                    }
                    other.damage(dmg);
            	}
        	});
        }
    }

    @Override
	public void updateHoming(Bullet b){
        if(homingPower > 0.0001f && b.time >= homingDelay){
            float angle = b.angleTo(b.aimX, b.aimY);
            b.vel.setAngle(Angles.moveToward(b.vel.angle(), angle, homingPower * Time.delta));
        }
    }
}
