package tekton.type.abilities;

import static mindustry.Vars.net;
import static mindustry.Vars.state;
import static mindustry.Vars.tilesize;

import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.world.Tile;

public class CrushAbility extends Ability {
    private boolean walked;

    public CrushAbility(){
        display = false;
    }
    
	@Override
	public void update(Unit unit)
	{
		walked = unit.moving();
		
		//calculate overlapping tiles so it slows down when going "over" walls
        int r = Math.max((int)(unit.hitSize * 0.6f / tilesize), 0);
        
        int solids = 0, total = (r*2+1)*(r*2+1);
        for(int dx = -r; dx <= r; dx++) {
            for(int dy = -r; dy <= r; dy++) {
                Tile t = Vars.world.tileWorld(unit.x + dx*tilesize, unit.y + dy*tilesize);
                if(t == null || t.solid()) {
                    solids++;
                }
                
                if(unit.type.crushDamage > 0 && !unit.disarmed && (walked || unit.deltaLen() >= 0.01f) && t != null
                    //damage radius is 1 tile smaller to prevent it from just touching walls as it passes
                    && Math.max(Math.abs(dx), Math.abs(dy)) <= r - 1) {

                    if(t.build != null && t.build.team != unit.team) {
                        t.build.damage(unit.team, unit.type.crushDamage * Time.delta * t.block().crushDamageMultiplier * state.rules.unitDamage(unit.team) * unit.speedMultiplier);
                    }
                }
            }
        }
        
        if(walked || net.client()) {
            walked = false;
        }
	}
}
