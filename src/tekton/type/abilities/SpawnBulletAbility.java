package tekton.type.abilities;

import arc.math.Mathf;
import mindustry.entities.abilities.Ability;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Unit;
import tekton.type.bullets.EmptyBulletType;

public class SpawnBulletAbility extends Ability {
	
	public BulletType bullet = new EmptyBulletType();
	public int quantity = 1;
	
	public SpawnBulletAbility() {
		display = false;
	}
	
	@Override
    public void death(Unit unit){
		for (int i = 1; i <= quantity; i++)
			bullet.create(unit, unit.team, unit.x, unit.y, 0f);
    }
}
