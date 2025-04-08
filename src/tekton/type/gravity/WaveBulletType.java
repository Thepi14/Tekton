package tekton.type.gravity;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;

import arc.graphics.Blending;
import arc.graphics.Color;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Log;
import arc.util.Tmp;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.effect.WaveEffect;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import tekton.content.TektonColor;

public class WaveBulletType extends BulletType {
	public float circleDeegres = 90f;
	public int linePoints = 32;
	public float minRadius = 20f;
	public float strokeThickness = 5f;
	public float damageInterval = 3f;
	public float interval = 10f;
	public float waveSpeed = 2f;
	
	public Effect waveEffect = new Effect();
	public Effect unitWaveEffect = Fx.none;
	
	public WaveBulletType(){
        super();
    }
	
	public WaveBulletType(float speed, float damage){
        super(0, damage);
        hittable = absorbable = false;
        pierce = pierceBuilding = true;
        lightColor = hitColor = TektonColor.gravityColor;
        lightOpacity = 0.6f;
        hitEffect = despawnEffect = trailEffect = shootEffect = smokeEffect = Fx.none;
        hitSound = despawnSound = Sounds.none;
        range = 200f;
        knockback = 10f;
        ammoMultiplier = 1f;
        waveSpeed = speed;
        lightRadius *= 2f;
    }
	
    @Override
    public void load(){
        super.load();
        waveEffect = new Effect(lifetime, e -> {
        	color(e.color.a(1f));
        	//blend(Blending.additive);
            
            stroke(e.fout() * strokeThickness);
            float deg = circleDeegres / linePoints;
            float currentSize = (e.time * waveSpeed) + minRadius;
            
            for (int i = -linePoints / 2; i < linePoints / 2; i++) {
            	var x = e.x + ((Mathf.cosDeg((deg * i) + e.rotation) * currentSize));
            	var y = e.y + (Mathf.sinDeg((deg * i) + e.rotation) * currentSize);
                line(
                		x, y, 
                		e.x + ((Mathf.cosDeg((deg * (i + 1)) + e.rotation) * currentSize)), e.y + (Mathf.sinDeg((deg * (i + 1)) + e.rotation) * currentSize));
                Drawf.light(x, y, lightRadius, lightColor, lightOpacity * e.fout());
            }
            
            color();
        	//blend();
        });
    }
	
	private float maxRadius;
    
    @Override
    public void init(Bullet b) {
    	super.init(b);
        waveEffect.at(b.x, b.y, b.rotation(), hitColor);
    	maxRadius = lifetime * waveSpeed;
        waveEffect.clip = maxRadius * 2f;
    }
    
    @Override
    public void update(Bullet b) {
        float currentSize = (b.time * waveSpeed) + minRadius;
        
        if (b.timer.get(5, damageInterval)) {
        	Units.nearbyEnemies(b.team, b.x, b.y, currentSize, other -> {
            	var angle = Mathf.atan2(other.x - b.x, other.y - b.y);
            	var absAngle = angle + 0f;
            	if (angle < 0f)
            		absAngle = angle + (360f * Mathf.degRad);
            	var abs = Math.abs(b.rotation() - (absAngle * Mathf.radDeg));
            	//Log.info(Mathf.floor(abs) + ", bull: " + Mathf.floor(b.rotation()) + ", unit: " + Mathf.floor(angle * Mathf.radDeg));
                if(other.team != b.team && other.hittable() && ((other.type.flying && collidesAir) || (!other.type.flying && collidesGround)) &&
                		!Mathf.within(other.x, other.y, currentSize - strokeThickness) && abs < circleDeegres / 2f){
                	var direction = new Vec2(Mathf.cos(angle), Mathf.sin(angle)).scl((knockback * 80f * b.fout()));
                    unitWaveEffect.at(other.x, other.y, angle * Mathf.radDeg, hitColor, new GravWaveEffectContainer(other, knockback * b.fout(), waveSpeed * 0.8f));
                    other.damage(damage);
                    other.apply(status, statusDuration);
                    Tmp.v3.set(direction);
                    other.impulse(Tmp.v3);
                }
            });
        }
    }
    
	@Override
    public void draw(Bullet b){
        
    }

	@Override
	public void drawLight(Bullet b){
        //if(lightOpacity <= 0f || lightRadius <= 0f) return;
    }
	
	public class GravWaveEffectContainer {
		public Unit unit;
		public float waveSize;
		public float waveSpeed;
		
		public GravWaveEffectContainer() {}
		
		public GravWaveEffectContainer(Unit unit, float waveSize, float waveSpeed) {
			super();
			this.unit = unit;
			this.waveSize = waveSize;
			this.waveSpeed = waveSpeed;
		}
	}
}
