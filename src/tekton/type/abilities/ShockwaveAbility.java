package tekton.type.abilities;

import arc.Core;
import arc.Events;
import arc.audio.Sound;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import arc.scene.ui.layout.*;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.game.EventType.Trigger;
import mindustry.gen.Bullet;
import mindustry.gen.Groups;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.unit.MissileUnitType;
import tekton.content.TektonFx;
import mindustry.graphics.*;

import static mindustry.Vars.*;

public class ShockwaveAbility extends Ability {
	protected float reloadCounter = 0f, checkCounter = 0f;
	protected Seq<Bullet> targets = new Seq<>();
	protected Seq<Unit> unitTargets = new Seq<>();
	protected float heat = 0f;
    public TextureRegion heatRegion;
    
    public float posY = 0f;
    
    public float range = 160f;
    public float reload = 60f * 4f;
    public float bulletDamage = 100f;
    public float shake = 2f;
    public float minDamage = 0;
    public float checkTimer = 8f;
    public Sound shootSound = Sounds.bang;
    public Color waveColor = Pal.techBlue, shapeColor = Pal.techBlue, shapeHeatColor = Color.white, heatColor = Pal.techBlue;
    public float cooldownMultiplier = 1f;
    public Effect hitEffect = Fx.hitSquaresColor;
    public Effect waveEffect = TektonFx.pointShockwave;

    //TODO switch to drawers eventually or something
    public float shapeRotateSpeed = 1f, shapeRadius = 3f;
    public int shapeSides = 4;
    
    @Override
    public void addStats(Table t) {
        super.addStats(t);
        t.add(Core.bundle.format("bullet.range", Strings.autoFixed(range / tilesize, 2)));
        t.row();
        t.add(abilityStat("cooldown", Strings.autoFixed(reload / 60f, 2)));
        t.row();
        t.add(abilityStat("damage", Strings.autoFixed(bulletDamage, 7)));
        if (minDamage > 0) {
            t.row();
            t.add(abilityStat("minimumdamagetoactivate", Strings.autoFixed(minDamage, 7)));
        }
    }
    
    public String abilityStat(String stat, Object... values) {
        return Core.bundle.format("ability.stat." + stat, values);
    }
	
	@Override
    public void update(Unit unit) {
		boolean check = false;
		if ((checkCounter += Time.delta) >= checkTimer) {
			checkCounter = 0f;
			check = true;
		}
		
		if((reloadCounter += Time.delta) >= reload && check) {
            targets.clear();
            unitTargets.clear();
            Groups.bullet.intersect(unit.x - range, unit.y - range, range * 2, range * 2, b -> {
                if(b.team != unit.team && b.type.hittable) {
                    targets.add(b);
                }
            });
            
            float checkDamage = 0f;
            for(var target : targets) {
            	var totalDamage = target.type.damage + target.type.splashDamage;
            	checkDamage = totalDamage > checkDamage ? totalDamage : checkDamage;
            }
            Units.nearbyEnemies(unit.team, unit.x, unit.y, range, other -> {
                if(unit.team != other.team && other.hittable() && other.isFlying() && other.type instanceof MissileUnitType) {
                	unitTargets.add(other);
                }
            });

            if((checkDamage >= minDamage && targets.size > 0) || unitTargets.size > 0) {
                heat = 1f;
                reloadCounter = 0f;
                waveEffect.at(unit.x, unit.y, range, waveColor, Float.valueOf((range / tilesize) / 2f));
                shootSound.at(unit, 1f);
                Effect.shake(shake, shake, unit);

                for(var target : targets) {
                    if(target.damage > bulletDamage) {
                        target.damage -= bulletDamage;
                    } else {
                        target.remove();
                    }
                    hitEffect.at(target.x, target.y, waveColor);
                }
                for(var target : unitTargets) {
                    target.damage(bulletDamage);
                    hitEffect.at(target.x, target.y, waveColor);
                }
            }
        }

        heat = Mathf.clamp(heat - Time.delta / reload * cooldownMultiplier);
    }
	
    public float warmup() {
        return heat;
    }
    
	@Override
    public void draw(Unit unit) {
        super.draw(unit);
        
        if(heatRegion == null){
        	heatRegion = Core.atlas.find(unit.type.name + "-heat", unit.type.region);
        }
        
        float x = Mathf.cosDeg(unit.rotation) * posY, y = Mathf.sinDeg(unit.rotation) * posY;
        Drawf.additive(heatRegion, heatColor, heat, unit.x, unit.y, unit.rotation - 90f, Layer.effect);

        Draw.z(Layer.effect);
        Draw.color(shapeColor, shapeHeatColor, Mathf.pow(heat, 2f));
        Fill.poly(unit.x + x, unit.y + y, shapeSides, shapeRadius, Time.time * shapeRotateSpeed);
        Draw.color();
    }
}
