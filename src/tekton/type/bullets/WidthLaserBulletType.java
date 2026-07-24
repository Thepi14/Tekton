package tekton.type.bullets;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

import arc.audio.Sound;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Intersector;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.FloatSeq;
import arc.util.Log;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.core.World;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.ContinuousBulletType;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Healthc;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;

import static tekton.math.TekMath.*;

public class WidthLaserBulletType extends ContinuousBulletType {
    public Color[] colors = {Pal.lancerLaser.cpy().mul(1f, 1f, 1f, 0.4f), Pal.lancerLaser, Color.white};
    public Effect laserEffect = Fx.lancerLaserShootSmoke;
    public float startWidth = 24f;
    public float endWidth = 160f;
    
    public float lengthFalloff = 0.5f;
    public float sideLength = 29f, sideWidth = 0.7f;
    public float sideAngle = 90f;
    public float lightningSpacing = -1, lightningDelay = 0.1f, lightningAngleRand;
    
    public Effect capsEffect = Fx.none;
    public float capsEffectInterval = 10f;
    public Color capsEffectColor = Pal.lancerLaser;
    public boolean largeHit = false;
    
    public Sound loopSound = Sounds.none;
    public float loopSoundVolume = 1f;
    
    public boolean continuous = true;
    
    public WidthLaserBulletType(){
        this(1f);
    }
    
    public WidthLaserBulletType(float damage) {
        this.damage = damage;
        
        hitSize = 0;
        lifetime = 16f;
        despawnEffect = Fx.none;
        shootEffect = Fx.none;
        smokeEffect = Fx.none;
        hitEffect = Fx.hitLaserBlast;
        hitColor = colors[2];
        delayFrags = true;
        
        ammoMultiplier = 1f;
    }

    @Override
    public float estimateDPS(){
        if(!continuous) return super.estimateDPS();
        //assume firing duration is about 100 by default, may not be accurate there's no way of knowing in this method
        return damage * 100f / damageInterval;
    }
	
	@Override
    public void init(){
        super.init();

        drawSize = Math.max(drawSize, endWidth * 2f);
    }

    @Override
    public void update(Bullet b) {
    	super.update(b);
    	
    	if (loopSound != Sounds.none)
    		Vars.control.sound.loop(loopSound, b, loopSoundVolume);
    }

    @Override
    protected float calculateRange(){
        return Math.max(endWidth, maxRange);
    }
    
    public Rect getRect(Bullet b) {
        Vec2 
        	left = getLeftPos(b), 
    		right = getRightPos(b);
    	return new Rect((Math.min(left.x, right.x) + Math.max(left.x, right.x)) / 2f, 
    			(Math.min(left.y, right.y) + Math.max(left.y, right.y)) / 2f, 
    			Math.max(left.x, right.x) - Math.min(left.x, right.x), 
    			Math.max(left.y, right.y) - Math.min(left.y, right.y));
    }
    
    public float currentWidth(Bullet b) {
    	return Mathf.lerp(startWidth, endWidth, b.fin());
    }
    
    public float halfWidth(Bullet b) {
    	return currentWidth(b) * 0.5f;
    }
    
    public Vec2 getLeftPos(Bullet b) {
        float halfWidth = halfWidth(b);
    	return new Vec2(b.x + Angles.trnsx(b.rotation() + 90f, halfWidth), b.y + Angles.trnsy(b.rotation() + 90f, halfWidth));
    }
    
    public Vec2 getRightPos(Bullet b) {
        float halfWidth = halfWidth(b);
    	return new Vec2(b.x + Angles.trnsx(b.rotation() - 90f, halfWidth), b.y + Angles.trnsy(b.rotation() - 90f, halfWidth));
    }
	
	@Override
    public void draw(Bullet b){
        
        Vec2 
        	left = getLeftPos(b), 
    		right = getRightPos(b);
    	
        if(b.timer(2, capsEffectInterval)){
        	capsEffect.at(left.x, left.y, b.rotation(), capsEffectColor);
        	capsEffect.at(right.x, right.y, b.rotation(), capsEffectColor);
        }

        float f = Mathf.curve(b.fin(), 0f, 0.2f);
        float clength = length * f;
        float compound = 1f;
        
        for(Color color : colors){
            Draw.color(color);
            
            var falloff = (clength *= lengthFalloff);
            
            Lines.stroke(falloff * b.fout());
            Lines.line(left.x, left.y, right.x, right.y, false);
            
            Fill.circle(left.x, left.y, falloff * 0.75f * b.fout());
            Fill.circle(right.x, right.y, falloff * 0.75f * b.fout());
            
            //Drawf.tri(b.x + Tmp.v1.x, b.y + Tmp.v1.y, Lines.getStroke(), cwidth * 2f + width / 2f, b.rotation());

            //Fill.circle(b.x, b.y, 1f * cwidth * b.fout());
            
            /*for(int i : Mathf.signs){
                Drawf.tri(b.x, b.y, sideWidth * b.fout() * cwidth, sideLength * compound, b.rotation() + sideAngle * i);
            }*/

            compound *= lengthFalloff;
        }
        Draw.reset();

        Drawf.light(left.x, left.y, right.x, right.y, length * 1.4f * b.fout(), colors[0], 0.6f);
    }

	@Override
    public void applyDamage(Bullet b){
        float damage = b.damage;
        if(timescaleDamage && b.owner instanceof Building build){
             b.damage *= build.timeScale();
        }

        Vec2 
        	left = getLeftPos(b), 
    		right = getRightPos(b);
        float distance = left.dst(right);
        
        b.fdata = left.dst(right);
        
        var blaser = (WidthLaserBulletType)b.type;

        Units.nearbyEnemies(b.team, b.x, b.y, currentWidth(b), u -> {
            if(u.checkTarget(b.type.collidesAir, b.type.collidesGround) && u.hittable() && 
            		/*Intersector.intersectSegmentRectangle(left.x, left.y, right.x, right.y, hitrect) && */
            		nearestPointOnFiniteLine(left, right, new Vec2(u.x, u.y)).dst(new Vec2(u.x, u.y)) - (u.hitSize * 0.5f) < Math.max(length * 0.5f * b.fout(), length * 0.2f)){
            	//b.hitEntity(b, u, u instanceof Healthc h ? h.health() : 0f);
            	b.collision(u, b.x, b.y);
            }
        });
        
        //Damage.collideLine(b, b.team, b.x, b.y, b.rotation(), currentLength(b), largeHit, laserAbsorb, pierceCap);
        b.damage = damage;
    }

    @Override
    public float continuousDamage(){
        if(!continuous) return -1f;
        return damage / damageInterval * 60f;
    }

    @Override
    public void drawLight(Bullet b){
        //no light drawn here
    }
}
