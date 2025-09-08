package tekton.type.bullets;

import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Log;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.StatusEffects;
import mindustry.entities.Damage;
import mindustry.entities.Fires;
import mindustry.entities.Units;
import mindustry.entities.bullet.ArtilleryBulletType;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.world.blocks.defense.Wall;
import tekton.math.TekMath;
import tekton.type.biological.BiologicalBlock;

import static mindustry.Vars.*;

public class HomingArtilleryBulletType extends ArtilleryBulletType {
    public float explodeRange = 30f, explodeDelay = 5f, flakDelay = 0f, flakInterval = 6f;
	
	public HomingArtilleryBulletType(float speed, float damage, String bulletSprite){
        super(speed, damage, bulletSprite);
    }

    public HomingArtilleryBulletType(float speed, float damage){
        this(speed, damage, "shell");
    }

    public HomingArtilleryBulletType(){
        this(1f, 1f, "shell");
    }
    
    @Override
    public void update(Bullet b){
        super.update(b);

        //don't check for targets if primed to explode
        /*if(b.time >= flakDelay && b.fdata >= 0 && b.timer(2, flakInterval)){
        	Vars.indexer.allBuildings(b.x, b.y, explodeRange, other -> {
                if(b.team != other.team){
                    //mark as primed
                    b.fdata = -1f;
                    Time.run(explodeDelay, () -> {
                        //explode
                        if(b.fdata < 0){
                            b.time = b.lifetime;
                        }
                    });
                }
            });
        }*/
    }
    
    @Override
    public void createSplashDamage(Bullet b, float x, float y){
        if(splashDamageRadius > 0 && !b.absorbed){
            /*Damage.damage(b.team, x, y, splashDamageRadius, splashDamage * b.damageMultiplier(), splashDamagePierce, collidesAir, collidesGround, scaledSplashDamage, b);*/

            if(status != StatusEffects.none){
                Damage.status(b.team, x, y, splashDamageRadius, status, statusDuration, collidesAir, collidesGround);
            }

            /*if(heals()){
                indexer.eachBlock(b.team, x, y, splashDamageRadius, Building::damaged, other -> {
                    healEffect.at(other.x, other.y, 0f, healColor, other.block);
                    other.heal(healPercent / 100f * other.maxHealth() + healAmount);
                });
            }*/
            
            Units.nearbyEnemies(b.team, x, y, splashDamageRadius, other -> {
                if(other.team != b.team && other.hittable() && ((other.isGrounded() && collidesGround) || (other.isFlying() && collidesAir)) && TekMath.insideDiamond(x,  y, other.x, other.y, splashDamageRadius)) {
                	var dst = TekMath.insideDiamondDst(x, y, other.x, other.y);
                	var dmg = splashDamage * Mathf.clamp((1.05f - (dst / splashDamageRadius)));
            		Log.info((1f - (dst / splashDamageRadius)));
                    other.damage(dmg, splashDamagePierce);
                    if(status != StatusEffects.none){
                    	other.apply(status, statusDuration);
                    }
                }
            });
            
            indexer.eachBlock(null, x, y, splashDamageRadius, other -> other.team != b.team && TekMath.insideDiamond(x,  y, other.x, other.y, splashDamageRadius), other -> {
            	var dst = TekMath.insideDiamondDst(x, y, other.x, other.y);
            	var dmg = splashDamage * Mathf.clamp((1.05f - (dst / splashDamageRadius)));
        		Log.info((1f - (dst / splashDamageRadius)));
                other.damage(dmg);
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
