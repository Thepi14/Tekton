package tekton.type.abilities;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Bullet;
import mindustry.gen.Groups;
import mindustry.gen.Unit;

public class DodgeAbility extends Ability {
	public float dodgeSpeed = 1f, dodgeRadiusOffset = 40f;
	
    public DodgeAbility(){
        display = false;
    }
	
    public DodgeAbility(float dodgeSpeed){
        display = false;
        this.dodgeSpeed = dodgeSpeed;
    }

	@Override
	public void update(Unit unit)
	{
		var radius = unit.hitSize * 2 + dodgeRadiusOffset;
		Seq<Bullet> bullets = new Seq<Bullet>();
		Vec2 pos = new Vec2(unit.x, unit.y);
		
		Groups.bullet.intersect(unit.x - (radius / 2f), unit.y - (radius / 2f), radius, radius, b -> {
            if(b.team != unit.team && b.type.speed != 0 && pos.dst(b.x, b.y) < radius - b.hitSize && ((unit.isGrounded() && b.type.collidesGround) || (unit.isFlying() && b.type.collidesAir))){
            	bullets.add(b);
            }
        });
		
		if (bullets.size > 0) {
			Bullet closest = bullets.get(0);
			
			for (var b : bullets) {
				if (b != closest && pos.dst(b.x, b.y) < pos.dst(closest.x, closest.y))
					closest = b;
			}
			
			float direction = closest.angleTo(pos);
			
			unit.moveAt(new Vec2((float)Math.cos(direction * Mathf.degRad), (float)Math.sin(direction * Mathf.degRad)), dodgeSpeed);
		}
	}
}
