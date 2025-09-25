package tekton.type.bullets;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;

import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Tmp;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.effect.WaveEffect;
import mindustry.game.Team;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import tekton.content.TektonColor;

public class WaveBulletType extends BulletType {
	public float circleDeegres = 90f;
	public int linePoints = 32;
	public float minRadius = 20f;
	public float strokeThickness = 5f;
	public float damageInterval = 3f;
	public float interval = 10f;
	public float waveSpeed = 2f;
	public boolean scaleDistance = true;
	
	public float healAmount = 1f;
	public boolean collidesEnemy = true, impulseTeam = false, statusTeam = false;
	
	public Effect waveEffect = new Effect();
	public Effect unitWaveEffect = Fx.none;
	
	public WaveBulletType(){
        super();
        hittable = absorbable = false;
        pierce = pierceBuilding = true;
        lightColor = hitColor = TektonColor.gravityColor;
        lightOpacity = 0.6f;
        hitEffect = despawnEffect = trailEffect = shootEffect = smokeEffect = Fx.none;
        hitSound = despawnSound = Sounds.none;
        range = 200f;
        knockback = 10f;
        ammoMultiplier = 1f;
        lightRadius *= 2f;
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
        lightRadius *= 2f;
        
        waveSpeed = speed;
    }
	
    @Override
    public void load(){
        super.load();
        waveEffect = new Effect(lifetime, e -> {
            Draw.z(Layer.bullet - 0.0001f);
        	color(e.color.cpy().a(1f));
        	//blend(Blending.additive);
            
            stroke(e.fout() * strokeThickness);
            float 
            deg = circleDeegres / linePoints,
            currentSize = (e.time * waveSpeed) + minRadius;
            
            for (int i = -linePoints / 2; i < linePoints / 2; i++) {
            	float 
            	x = e.x + (Mathf.cosDeg((deg * i) + e.rotation) * currentSize),
            	y = e.y + (Mathf.sinDeg((deg * i) + e.rotation) * currentSize);
                line(
                		x, y, 
                		e.x + (Mathf.cosDeg((deg * (i + 1)) + e.rotation) * currentSize), 
                		e.y + (Mathf.sinDeg((deg * (i + 1)) + e.rotation) * currentSize));
                Drawf.light(x, y, lightRadius, lightColor, lightOpacity * e.fout());
            }
            
            color();
        	//blend();
            Draw.z();
        });
    }
	
	private float maxRadius;
    
    @Override
    public void init(Bullet b) {
    	super.init(b);
    	//the actual visual representation of the wave
        waveEffect.at(b.x, b.y, b.rotation(), hitColor);
    	maxRadius = lifetime * waveSpeed;
        waveEffect.clip = maxRadius * 2f;
    }
    
    @Override
    public void update(Bullet b) {
        float currentSize = (b.time * waveSpeed) + minRadius;
        
        if (b.timer.get(5, damageInterval)) {
        	if (collidesEnemy)
	        	Units.nearbyEnemies(b.team, b.x, b.y, currentSize + 1f, other -> {
	            	var angle = Mathf.atan2(other.x - b.x, other.y - b.y);
	            	var absAngle = angle + 0f;
	            	if (angle < 0f)
	            		absAngle = angle + (360f * Mathf.degRad);
	            	var abs = Math.abs(b.rotation() - (absAngle * Mathf.radDeg));
	            	
	                if(other.team != b.team && other.hittable() && ((other.isFlying() && collidesAir) || (other.isGrounded() && collidesGround)) &&
	                		!Mathf.within(other.x - b.x, other.y - b.y, currentSize - 4f) && (abs < circleDeegres / 2f || circleDeegres >= 360f)) {
	                	
	                    unitWaveEffect.at(other.x, other.y, angle * Mathf.radDeg, hitColor, new GravWaveEffectContainer(other, knockback * b.fout(), waveSpeed * 0.8f));
	                    
	                    other.damage(damage);
	                    other.apply(status, statusDuration);
	                    
	                	var direction = new Vec2(Mathf.cos(angle), Mathf.sin(angle)).scl((knockback * 80f * b.fout()));
	                    other.impulse(direction);
	                }
	            });
        	//TODO maybe make only one iteration?
        	if (collidesTeam)
	        	Units.nearbyEnemies(Team.derelict, b.x, b.y, currentSize + 1f, other -> {
	        		var angle = Mathf.atan2(other.x - b.x, other.y - b.y);
	            	var absAngle = angle + 0f;
	            	if (angle < 0f)
	            		absAngle = angle + (360f * Mathf.degRad);
	            	var abs = Math.abs(b.rotation() - (absAngle * Mathf.radDeg));
	            	
	                if (other.team == b.team && ((other.isFlying() && collidesAir) || (other.isGrounded() && collidesGround)) &&
	                		!Mathf.within(other.x - b.x, other.y - b.y, currentSize - 4f) && (abs < circleDeegres / 2f || circleDeegres >= 360f)) {
	                	
	                	unitWaveEffect.at(other.x, other.y, angle * Mathf.radDeg, hitColor, new GravWaveEffectContainer(other, knockback * b.fout(), waveSpeed * 0.8f));
	                	
	                	if (healAmount > 0)
	                		other.heal(healAmount);
	                	
	                	if (statusTeam)
	                        other.apply(status, statusDuration);
	                	
	                	if (impulseTeam) {
	                    	var direction = new Vec2(Mathf.cos(angle), Mathf.sin(angle)).scl((knockback * 80f * b.fout()));
	                        other.impulse(direction);
	                	}
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
