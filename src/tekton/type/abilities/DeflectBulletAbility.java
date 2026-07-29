package tekton.type.abilities;

import static arc.Core.settings;

import arc.Core;
import arc.audio.Sound;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.util.Strings;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Bullet;
import mindustry.gen.Groups;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import tekton.Tekton;

public class DeflectBulletAbility extends Ability {
    public float chanceDeflect = 0.6f;
    public Sound deflectSound = Sounds.none;
    public float radiusOffset = 16f;
    public float yOffset = 8f;

    @Override
    public void addStats(Table t) {
        super.addStats(t);
        if (chanceDeflect > 0f) {
        	t.add(abilityStat("deflectchance", Strings.autoFixed(chanceDeflect *100f, 2)));
        }
    }
    
	public void update(Unit unit) {
		float range = unit.hitSize + radiusOffset;
		Groups.bullet.intersect(unit.x - (range * .5f) + Angles.trnsx(unit.rotation(), yOffset), unit.y - (range * .5f) + Angles.trnsy(unit.rotation(), yOffset), range, range, b -> {
            if(b.team != unit.team && b.type.hittable && b.type.speed != 0 && new Vec2(b.x, b.y).dst(unit) < range * .5f){
            	tryDeflect(unit, b);
            }
        });
	}
	
    @Override
    public void draw(Unit unit) {
    	if (Tekton.showDebug || settings.getBool("drawhitboxes")) {
    		Draw.color(Color.cyan);
    		Lines.stroke(0.5f);
    		Lines.circle(unit.x + Angles.trnsx(unit.rotation(), yOffset), unit.y + Angles.trnsy(unit.rotation(), yOffset), (unit.hitSize + radiusOffset) * 0.5f);
    		Draw.color();
    	}
    }
	
	//shamelessly copied from Mindustry's Wall.java
	public boolean tryDeflect(Unit unit, Bullet bullet) {
		
		//deflect bullets if necessary
        if(chanceDeflect > 0f){
            //slow bullets are not deflected
            if(bullet.vel.len() <= 0.1f || !bullet.type.reflectable) return true;

            //bullet reflection chance depends on bullet damage
            if(!Mathf.chance(chanceDeflect)) return true;

            //make sound
            deflectSound.at(unit, Mathf.random(0.9f, 1.1f));

            //translate bullet back to where it was upon collision
            bullet.trns(-bullet.vel.x, -bullet.vel.y);

            float penX = Math.abs(unit.x - bullet.x), penY = Math.abs(unit.y - bullet.y);

            if(penX > penY){
                bullet.vel.x *= -1;
            }else{
                bullet.vel.y *= -1;
            }

            bullet.owner = unit;
            bullet.team = unit.team;
            bullet.time += 1f;
            
            return false;
        }
        
        return true;
	}
}
