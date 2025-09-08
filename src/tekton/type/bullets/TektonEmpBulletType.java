package tekton.type.bullets;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import arc.math.Mathf;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;
import mindustry.graphics.Pal;
import mindustry.world.blocks.defense.Wall;
import tekton.type.biological.BiologicalBlock;
import tekton.type.defense.AdvancedWall;

public class TektonEmpBulletType extends BasicBulletType{
    public float radius = 100f;
    public float timeIncrease = 2.5f, timeDuration = 60f * 10f;
    public float powerDamageScl = 2f, powerSclDecrease = 0.2f;
    public Effect hitPowerEffect = new Effect(40, e -> {
        color(e.color);
        stroke(e.fout() * 1.6f);

        randLenVectors(e.id, 18, e.finpow() * 27f, e.rotation, 360f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            lineAngle(e.x + x, e.y + y, ang, e.fout() * 6 + 1f);
        });
    }), chainEffect = Fx.chainEmp.wrap(Pal.lancerLaser.cpy()), applyEffect = Fx.hitLancer;
    public boolean hitUnits = true;
    public float unitDamageScl = 0.7f;
    
    public TektonEmpBulletType() {
    	despawnEffect = hitEffect = Fx.none;
    }

    @Override
    public void hit(Bullet b, float x, float y){
        super.hit(b, x, y);

        if(!b.absorbed){
        	if (collidesGround)
	            Vars.indexer.allBuildings(x, y, radius, other -> {
	                if(b.team != other.team && other.power != null) {
	                    var absorber = Damage.findAbsorber(b.team, x, y, other.x, other.y);
	                    if(absorber != null && absorbable) {
	                        other = absorber;
	                    }
	
	                    if(other.power != null && other.power.graph.getLastPowerProduced() > 0f && !(other.block instanceof Wall) && !(other.block instanceof BiologicalBlock)) {
	                        other.applySlowdown(powerSclDecrease, timeDuration);
	                        other.damage(damage * powerDamageScl * buildingDamageMultiplier);
	                        hitPowerEffect.at(other.x, other.y, b.angleTo(other), hitColor);
	                        chainEffect.at(x, y, 0, hitColor, other);
	                    }
	                }
	            });

            if(hitUnits){
                Units.nearbyEnemies(b.team, x, y, radius, other -> {
                    if(other.team != b.team && other.hittable() && ((other.isGrounded() && collidesGround) || (other.isFlying() && collidesAir))) {
                        var absorber = Damage.findAbsorber(b.team, x, y, other.x, other.y);
                        if(absorber != null && absorbable) {
                            return;
                        }
                        hitPowerEffect.at(other.x, other.y, b.angleTo(other), hitColor);
                        chainEffect.at(x, y, 0, hitColor, other);
                        other.damage(damage * unitDamageScl);
                        other.apply(status, statusDuration);
                    }
                });
            }
        }
    }
}
