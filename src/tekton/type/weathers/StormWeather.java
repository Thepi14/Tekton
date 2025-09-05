package tekton.type.weathers;

import static mindustry.Vars.*;

import arc.audio.Sound;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Bullet;
import mindustry.gen.Entityc;
import mindustry.gen.Groups;
import mindustry.gen.Sounds;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.gen.WeatherState;
import mindustry.type.weather.ParticleWeather;
import mindustry.type.weather.RainWeather;
import mindustry.world.Tile;
import mindustry.world.Tiles;
import tekton.content.TektonFx;
import tekton.content.TektonSounds;
import tekton.math.TekMath;
import tekton.type.defense.LightningAbsorber;
import tekton.type.power.LightningRod;
import tekton.type.power.LightningRod.LightningRodBuild;

public class StormWeather extends ParticleWeather {
	/** big numbers causes amargeddon */
	public float 
			lightningChance = 0.01f, 
			unitLightningChance = 0.0001f, 
			lightningSplashDamage = 50f, 
			lightningSplashDamageRadius = 60f, 
			lightningsDamage = 60f, 
			lightningVolume = 2f, 
			lightningHitSoundPitchMin = 0.8f, 
			lightningHitSoundPitchMax = 1f, 
			lightningShake = 8f, 
			lightningLightRadius = 70f;
	
	public int lightningRays = 12, lightningLengthMin = 6, lightningRandExtension = 16;
	
	public Color 
			lightningsColor = Color.valueOf("ffe047"),
			hitsColor = Color.valueOf("ffea82"),
			lightningLightColor = Color.valueOf("ffea82");
	
	public Sound lightningHitSound = TektonSounds.lightningstrike;
	public int minLines = 6, maxLines = 12;
	public boolean absorbableByLightningRods = true;
	
	public Effect 
		lightningHitEffect = Fx.titanExplosion, 
		lightningBoltEffect = 
		new Effect(30f, 300f, e -> {
			rand.setSeed(e.id);
			float size = rand.random(100f, 180f);
			int points = (int)size / 15;
			float pointMul = size / points;
			
	        Lines.stroke(10f * e.fout());
	        Draw.color(lightningsColor, Color.white, e.fin());
	        
	        Lines.beginLine();
	        
	        Lines.linePoint(e.x, e.y);
	        float bi = rand.range(5f);
	        for (int i = 1; i < points; i++) {
	        	bi = (bi + rand.range(7f * i)) / 2f;
	            Lines.linePoint(e.x + bi, (e.y + (i * pointMul)) + rand.range(15f));
	        }
	        
	        Lines.endLine();
	    }).followParent(false).rotWithParent(false);
	
	public BasicBulletType lightningBullet;
	
	private float trand = 0f;

	public StormWeather(String name) {
		super(name);
	}

	@Override
    public void update(WeatherState state){
		if (lightningBullet == null) {
			lightningBullet = new BasicBulletType(0f, 0f) {{
				/*lifetime = 20f;
				width = height = 20f;*/
				instantDisappear = true;
				
				splashDamage = lightningSplashDamage;
				splashDamageRadius = lightningSplashDamageRadius;
				scaledSplashDamage = true;
				
				lightning = lightningRays;
	            lightningLength = lightningLengthMin;
	            lightningLengthRand = lightningRandExtension;
	            lightningDamage = lightningsDamage;
	            
	            hitColor = hitsColor;
	            lightningColor = lightningsColor;
	            lightRadius = lightningLightRadius;
	            lightColor = lightningLightColor;

	            hitShake = despawnShake = lightningShake;
				absorbable = false;
	            reflectable = false;
	            hittable = false;
	            pierce = true;
	            pierceBuilding = true;
	            pierceArmor = true;
	            
	            hitSoundPitch = 1f;
	            hitSoundVolume = lightningVolume;
	            
	            despawnSound = hitSound = lightningHitSound;
	            
	            despawnEffect = hitEffect = lightningHitEffect;
	            
				buildingDamageMultiplier = 0.5f;
			}};
		}
		super.update(state);
		if (Groups.unit.isEmpty() || Vars.state.isPaused())
			return;
		lightningBullet.hitSoundPitch = Mathf.random(lightningHitSoundPitchMin, lightningHitSoundPitchMax);
		
		trand = Mathf.random(0f, 100f) / 100f;
		var randX = Mathf.random(0, Vars.world.width()) * Vars.tilesize;
		var randY = Mathf.random(0, Vars.world.height()) * Vars.tilesize;
		if (trand <= lightningChance) {
			createLightning(randX, randY);
		}
		for(Unit unit : Groups.unit){
			if (!unit.hittable())
				continue;
			trand = Mathf.random(0f, 100f) / 100f;
			//Log.info("Lightning Created: " + (trand <= lightningChance) + ". Chance: " + trand);
			if (trand <= unitLightningChance) {
				createLightning(unit.x, unit.y);
				break;
			}
        }
    }
	
	private void createLightning(float x, float y) {
		if (absorbableByLightningRods) {
			var pos = new Vec2(x, y);
			for (Tile tile : Vars.world.tiles) {
				if (tile.build != null)
					if (tile.build instanceof LightningAbsorber rod) {
						float bx = tile.build.getX(), by = tile.build.getY();
						if (tile.block() instanceof LightningRod rodBlock) {
							if (rodBlock.circleArea) {
								if (pos.dst(new Vec2(bx, by)) <= rod.lightningProtectionRadius()) {
									rod.absorbLightning();
									lightningBoltEffect.at(bx, by);
									lightningBullet.hitSound.at(bx, by);
									return;
								}
							}
							else if (rodBlock.squareArea) {
								if ((bx <= x + rod.lightningProtectionRadius() && by <= y + rod.lightningProtectionRadius()) || 
									(bx >= x - rod.lightningProtectionRadius() && by >= y - rod.lightningProtectionRadius())) {
									rod.absorbLightning();
									lightningBoltEffect.at(bx, by);
									lightningBullet.hitSound.at(bx, by);
									return;
								}
							}
							else if (rodBlock.diamondArea) {
								if (TekMath.insideDiamond(x, y, bx, by, rod.lightningProtectionRadius())) {
									rod.absorbLightning();
									lightningBoltEffect.at(bx, by);
									lightningBullet.hitSound.at(bx, by);
									return;
								}
							}
						}
						else {
							if (pos.dst(new Vec2(bx, by)) <= rod.lightningProtectionRadius()) {
								rod.absorbLightning();
								lightningBoltEffect.at(bx, by);
								lightningBullet.hitSound.at(bx, by);
								return;
							}
						}
					}
			}
		}
		//yeah, i don't like it too.
		lightningBullet.create(Groups.unit.first(), Team.derelict, x, y, 0f);
		lightningBoltEffect.at(x, y);
		//Log.info("Lightning Created, pos: " + x + ", " + y);
	}
}
