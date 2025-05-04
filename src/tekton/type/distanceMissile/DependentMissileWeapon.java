package tekton.type.distanceMissile;

import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Tmp;
import mindustry.ai.types.MissileAI;
import mindustry.entities.Effect;
import mindustry.entities.Mover;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Entityc;
import mindustry.gen.Unit;
import mindustry.type.Weapon;

public class DependentMissileWeapon extends Weapon {
	
	public float weaponRange = 0;
	
	public DependentMissileWeapon(String name){
        this.name = name;
    }

    public DependentMissileWeapon(){
        this("");
    }
    
    @Override
    public float range(){
        return weaponRange;
    }
}
