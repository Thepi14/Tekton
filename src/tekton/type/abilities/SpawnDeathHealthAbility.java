package tekton.type.abilities;

import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.abilities.Ability;
import mindustry.gen.*;
import mindustry.type.*;

public class SpawnDeathHealthAbility extends Ability { //only used on eggs anyways
	public UnitType unit;
    public int amount = 1, randAmount = 0;
    /** Random spread of units away from the spawned. */
    public float spread = 8f;
    /** If true, units spawned face outwards from the middle. */
    public boolean faceOutwards = true;
    protected float timer = 0f;
	public float lifetime = 60f * 4f;

    public SpawnDeathHealthAbility(UnitType unit, int amount, float spread){
    	display = false;
        this.unit = unit;
        this.amount = amount;
        this.spread = spread;
    }

    public SpawnDeathHealthAbility(){
    	display = false;
    }

    /*@Override
    public void addStats(Table t){
        super.addStats(t);
        t.add("[stat]" + (randAmount > 0 ? amount + "x-" + (amount + randAmount) : amount) + "x[] " + (unit.hasEmoji() ? unit.emoji() : "") + "[stat]" + unit.localizedName);
    }*/
    
    @Override
    public void update(Unit unit) {
    	timer += Time.delta;
    	if(!Vars.net.client()) {
	    	if (timer > lifetime) {
	    		timer = 0f;
	    		int spawned = amount + Mathf.random(randAmount);
	            for(int i = 0; i < spawned; i++) {
	                Tmp.v1.rnd(Mathf.random(spread));
	                var u = this.unit.spawn(unit.team, unit.x + Tmp.v1.x, unit.y + Tmp.v1.y);
	                
	                u.rotation = faceOutwards ? Tmp.v1.angle() : unit.rotation + Mathf.range(5f);
	            }
	    		unit.kill();
	    	}
    	}
    }
}
