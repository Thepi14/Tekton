package tekton.type.bullets;

import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.util.Nullable;
import arc.util.Tmp;
import mindustry.content.Fx;
import mindustry.entities.*;
import mindustry.entities.bullet.LiquidBulletType;
import mindustry.gen.*;
import mindustry.type.Liquid;
import mindustry.type.StatusEffect;

import static mindustry.Vars.*;

public class DoubleLiquidBulletType extends LiquidBulletType {
	public Liquid secondLiquid;
	public StatusEffect secondStatus;
	
	public DoubleLiquidBulletType(@Nullable Liquid liquid1, @Nullable Liquid liquid2){
        super(liquid1);
        
        if(liquid1 != null){
            this.liquid = liquid1;
            this.status = liquid1.effect;
            hitColor = liquid1.color;
            lightColor = liquid1.lightColor;
            lightOpacity = liquid1.lightColor.a;
        }
        
        if (liquid2 != null) {
            secondLiquid = liquid2;
        	secondStatus = secondLiquid.effect;
        	
        	var col2 = secondLiquid.color.cpy().mul(0.3f);
        	hitColor = hitColor.cpy().mul(0.8f).add(col2);
        	hitColor.a = 1f;
        	col2 = secondLiquid.lightColor.cpy().mul(0.3f);
            lightColor = lightColor.cpy().mul(0.8f).add(col2);
            
            lightOpacity = (lightOpacity + secondLiquid.lightColor.a) / 1.8f;
        }

        ammoMultiplier = 1f;
        lifetime = 34f;
        statusDuration = 60f * 2f;
        despawnEffect = Fx.none;
        hitEffect = Fx.hitLiquid;
        smokeEffect = Fx.none;
        shootEffect = Fx.none;
        drag = 0.001f;
        knockback = 0.55f;
        displayAmmoMultiplier = false;
    }
	
	@Override
    public void draw(Bullet b){
        super.draw(b);
        if(liquid.willBoil()){
            Draw.color(hitColor, Tmp.c3.set(hitColor).a(0.4f), b.time / Mathf.randomSeed(b.id, boilTime));
            Fill.circle(b.x, b.y, orbSize * (b.fin() * 1.1f + 1f));
        }else{
            Draw.color(hitColor, Color.white, b.fout() / 100f);
            Fill.circle(b.x, b.y, orbSize);
        }

        Draw.reset();
    }

	@Override
    public void despawned(Bullet b){
        if(!liquid.willBoil()){
            hitEffect.at(b.x, b.y, b.rotation(), hitColor);
        }
    }
	
	@Override
    public void hit(Bullet b, float hitx, float hity){
        hitEffect.at(hitx, hity, hitColor);
        Puddles.deposit(world.tileWorld(hitx, hity), liquid, puddleSize);

        if(liquid.temperature <= 0.5f && liquid.flammability < 0.3f){
            float intensity = 400f * puddleSize/6f;
            Fires.extinguish(world.tileWorld(hitx, hity), intensity);
            for(Point2 p : Geometry.d4){
                Fires.extinguish(world.tileWorld(hitx + p.x * tilesize, hity + p.y * tilesize), intensity);
            }
        }
    }
	
	public void hitEntity(Bullet b, Hitboxc entity, float health){
        super.hitEntity(b, entity, health);

        if(entity instanceof Unit unit && !unit.dead){
            unit.apply(secondStatus, statusDuration);
        }
    }
}
