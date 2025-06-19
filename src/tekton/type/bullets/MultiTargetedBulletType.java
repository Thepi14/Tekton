package tekton.type.bullets;

import static mindustry.Vars.tilesize;

import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.*;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.*;
import mindustry.entities.bullet.ContinuousBulletType;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.blocks.defense.turrets.Turret.BulletEntry;

public class MultiTargetedBulletType extends ContinuousBulletType {
	public ContinuousBulletType bullet = new ContinuousBulletType();
	public Seq<Vec2> spawnPositions = new Seq<Vec2>().add(new Vec2());
	public Effect aimEffect = Fx.none;
	public float minAimRadius = 25f;

    public MultiTargetedBulletType(ContinuousBulletType bullet){
    	this.bullet = bullet;
    	despawnEffect = hitEffect = Fx.none;
    	speed = 0f;
    	collides = false;
    	shootEffect = smokeEffect = Fx.none;
    	damage = bullet.damage;
    	damageInterval = bullet.damageInterval;
    	hitColor = bullet.hitColor;
        buildingDamageMultiplier = bullet.buildingDamageMultiplier;
    }
    
    public MultiTargetedBulletType(ContinuousBulletType bullet, Seq<Vec2> spawnPositions){
    	this(bullet);
		this.spawnPositions = spawnPositions;
    	damage = bullet.damage * spawnPositions.size;
    }
    
    public MultiTargetedBulletType(ContinuousBulletType bullet, Seq<Vec2> spawnPositions, Effect aimEffect){
    	this(bullet, spawnPositions);
		this.aimEffect = aimEffect;
    }

    public MultiTargetedBulletType(){}
    
    @Override
    public float continuousDamage(){
        return damage / damageInterval * 60f;
    }

    @Override
    public float estimateDPS(){
        if(!continuous) return super.estimateDPS();
        //assume firing duration is about 100 by default, may not be accurate there's no way of knowing in this method
        //assume it pierces 3 blocks/units
        return (bullet.damage * 100f / bullet.damageInterval * 3f) * spawnPositions.size;
    }

    /*@Override
    protected float calculateRange(){
        float max = 0f;
        for(var b : bullets){
            max = Math.max(max, b.calculateRange());
        }
        return max;
    }*/

    @Override
    public void init(Bullet b) {
    	var data = new Seq<BulletEntry>();
        for (var pos : spawnPositions) {
        	var newPos = new Vec2(Angles.trnsx(b.rotation(), b.x + pos.x, b.y + pos.y), Angles.trnsy(b.rotation(), b.x + pos.x, b.y + pos.y));
        	float ang = (Mathf.atan2(b.aimX - newPos.x, b.aimY - newPos.y) * Mathf.radDeg);
        	
        	var bul = bullet.create(b, b.team, newPos.x, newPos.y, ang, damage * this.damage, 1f, 1f, data);
        	data.add(new BulletEntry(bul, newPos.x, newPos.y, ang, 0f));
        }
        b.data = data;
    }

    @Override
    public void update(Bullet b) {
    	var entries = new Seq<BulletEntry>();
    	/*if (Angles.angle(b.x, b.y, b.aimX, b.aimY) > b.rotation() + 10f || Angles.angle(b.x, b.y, b.aimX, b.aimY) < b.rotation() - 10f) {
    		b.aimX = b.x;
    		b.aimY = b.y;
    	}*/
    	if (new Vec2(b.aimX, b.aimY).dst(new Vec2(b.x, b.y)) < minAimRadius) {
    		b.aimX = b.x + Angles.trnsx(b.rotation(), minAimRadius);
    		b.aimY = b.y + Angles.trnsy(b.rotation(), minAimRadius);
    	}
    		
    	if (b.data instanceof Seq tdata) {
    		for (var bul : tdata) {
    			if (bul instanceof BulletEntry entry) {
    				entries.add(entry);
    			}
    		}
    	}
		updateBullets(b, entries);
    }
    
    protected void updateBullets(Bullet main, Seq<BulletEntry> entries){
    	int i = 0;
    	for (var entry : entries) {
    		var pos = spawnPositions.get(i);
    		var newPos = new Vec2(Angles.trnsx(main.rotation(), pos.x, pos.y), Angles.trnsy(main.rotation(), pos.x, pos.y));
	        float angle = (Mathf.atan2(main.aimX - (main.x + newPos.x), main.aimY - (main.y + newPos.y)) * Mathf.radDeg);
	
	        entry.bullet.rotation(angle);
	        entry.bullet.set(main.x + newPos.x, main.y + newPos.y);
	
	        entry.bullet.aimX = main.aimX;
	        entry.bullet.aimY = main.aimY;
	        
	        entry.bullet.damage = entry.bullet.type.damage * main.damageMultiplier();
	        entry.bullet.time = entry.bullet.lifetime * entry.bullet.type.optimalLifeFract * main.damageMultiplier();
	        entry.bullet.keepAlive = true;
	        
	        i++;
    	}
    }
    
    @Override
    public void draw(Bullet b){
    	//super.draw(b);
    	if (!Vars.headless && !Vars.state.isPaused())
    		aimEffect.at(b.aimX, b.aimY);
    }
}
