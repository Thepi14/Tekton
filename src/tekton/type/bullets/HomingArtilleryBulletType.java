package tekton.type.bullets;

import arc.math.Angles;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.entities.Damage;
import mindustry.entities.Units;
import mindustry.entities.bullet.ArtilleryBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.world.blocks.defense.Wall;
import tekton.type.biological.BiologicalBlock;

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

        /*//don't check for targets if primed to explode
        if(b.time >= flakDelay && b.fdata >= 0 && b.timer(2, flakInterval)){
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
	public void updateHoming(Bullet b){
        if(homingPower > 0.0001f && b.time >= homingDelay){
            float angle = b.angleTo(b.aimX, b.aimY);
            b.vel.setAngle(Angles.moveToward(b.vel.angle(), angle, homingPower * Time.delta));
        }
    }
}
