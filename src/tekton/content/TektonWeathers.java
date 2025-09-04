package tekton.content;

import arc.graphics.*;
import arc.util.*;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.type.weather.*;
import mindustry.world.meta.*;
import tekton.type.bullets.EmptyBulletType;
import tekton.type.weathers.*;

public class TektonWeathers {
	public static Weather
	tektonFog,
	methaneRain,
	darkSandstorm,
	acidRain,
	neurosporastorm,
	electricStorm,
	cryoVolcanicSnow,
	eggStorm
	;
	
	public static void load() {
		tektonFog = new FogWeather("fog"){{
            duration = 15f * Time.toMinutes;
            noiseLayers = 3;
            noiseLayerSclM = 0.8f;
            noiseLayerAlphaM = 0.7f;
            noiseLayerSpeedM = 2f;
            noiseLayerSclM = 0.6f;
            baseSpeed = 0.05f;
            color = noiseColor = TektonColor.methane.cpy().a(0.5f);
            opacity = 0.3f;
            noiseScale = 1100f;
            noisePath = "fog";
            drawParticles = false;
            drawNoise = true;
            useWindVector = false;
            xspeed = 1f;
            yspeed = 0.01f;
            attrs.set(Attribute.light, -1f);
            opacityMultiplier = 0.47f;
        }};
		
		methaneRain = new RainWeather("methane-rain") {{
	        attrs.set(Attribute.light, -0.2f);
	        attrs.set(TektonAttributes.methane, 0.2f);
	        status = TektonStatusEffects.tarredInMethane;
	        sound = Sounds.rain;
	        soundVol = 0.25f;
	        liquid = TektonLiquids.methane;
	        color = TektonColor.liquidMethane.cpy().mul(1.3f);
            duration = 8f * Time.toMinutes;
	    }};
	    
	    darkSandstorm = new ParticleWeather("dark-sandstorm") {{
            color = noiseColor = Color.valueOf("3d352f");
            particleRegion = "particle";
            drawNoise = true;
            useWindVector = true;
            sizeMax = 140f;
            sizeMin = 70f;
            minAlpha = 0f;
            maxAlpha = 0.4f;
            density = 1500f;
            baseSpeed = 5.4f;
            attrs.set(Attribute.light, -0.1f);
            attrs.set(Attribute.water, -0.1f);
	        attrs.set(TektonAttributes.methane, -0.1f);
	        attrs.set(TektonAttributes.silica, 0.2f);
            opacityMultiplier = 0.45f;
            force = 0.25f;
            sound = Sounds.wind;
            soundVol = 1f;
            
            duration = 4f * Time.toMinutes;
        }};
	    
	    neurosporastorm = new ParticleWeather("neurosporastorm") {{
            color = noiseColor = TektonColor.neurospora;
            particleRegion = "circle-small";
            drawNoise = true;
            statusGround = false;
            useWindVector = true;
            sizeMax = 5f;
            sizeMin = 2.5f;
            minAlpha = 0.1f;
            maxAlpha = 0.8f;
            density = 2000f;
            baseSpeed = 4.3f;
            attrs.set(TektonAttributes.methane, 0.1f);
            attrs.set(TektonAttributes.silica, 0.1f);
            attrs.set(Attribute.light, -0.15f);
            status = TektonStatusEffects.neurosporaSlowed;
            opacityMultiplier = 0.5f;
            force = 0.1f;
            sound = Sounds.wind;
            soundVol = 0.7f;
            duration = 7f * Time.toMinutes;
        }};
	    
	    acidRain = new RainWeather("acid-rain") {{
	        attrs.set(Attribute.light, -0.3f);
	        attrs.set(Attribute.water, -0.2f);
	        status = TektonStatusEffects.wetInAcid;
	        sound = Sounds.rain;
	        soundVol = 0.3f;
	        liquid = TektonLiquids.acid;
	        color = TektonLiquids.acid.color.cpy();
            duration = 5f * Time.toMinutes;
	    }};
	    
	    electricStorm = new StormWeather("electric-storm") {{
	    	color = noiseColor = Color.valueOf("ffe14a");
            particleRegion = "particle";
            drawNoise = true;
            useWindVector = true;
            sizeMax = 160f;
            sizeMin = 70f;
            minAlpha = 0f;
            maxAlpha = 0.25f;
            density = 1600f;
            baseSpeed = 6f;
            attrs.set(Attribute.light, -0.1f);
            attrs.set(Attribute.water, -0.1f);
	        attrs.set(TektonAttributes.methane, -0.1f);
	        attrs.set(TektonAttributes.silica, 0.2f);
            opacityMultiplier = 0.4f;
            force = 0.4f;
            sound = Sounds.wind;
            soundVol = 1.1f;
            duration = 2f * Time.toMinutes;

	        status = TektonStatusEffects.shortCircuit;
	    }};
        
        cryoVolcanicSnow = new ParticleWeather("cryovolcanic-snow") {{
            particleRegion = "particle";
            sizeMax = 13f;
            sizeMin = 2.6f;
            density = 1200f;
            attrs.set(Attribute.light, -0.15f);
            attrs.set(Attribute.water, -0.1f);
	        attrs.set(TektonAttributes.methane, 0.1f);
	        
	        color = TektonItems.cryogenicCompound.color.cpy().add(Color.white.cpy().mul(0.35f));
	        status = StatusEffects.freezing;

            sound = Sounds.windhowl;
            soundVol = 0f;
            soundVolOscMag = 1.5f;
            soundVolOscScl = 1100f;
            soundVolMin = 0.02f;
            
            duration = 7f * Time.toMinutes;
        }};
        
        eggStorm = new ObstaclesWeather("egg-storm") {{
        	color = noiseColor = TektonColor.acid;
            particleRegion = "circle-small";
            drawNoise = true;
            statusGround = false;
            useWindVector = true;
            sizeMax = 5f;
            sizeMin = 2.5f;
            minAlpha = 0.2f;
            maxAlpha = 0.8f;
            density = 2300f;
            baseSpeed = 5f;
            attrs.set(TektonAttributes.methane, 0.1f);
            attrs.set(TektonAttributes.silica, 0.1f);
            attrs.set(Attribute.light, -0.15f);
            status = TektonStatusEffects.wetInAcid;
            opacityMultiplier = 0.55f;
            force = 0.2f;
            sound = Sounds.wind;
            soundVol = 0.8f;
            duration = 4f * Time.toMinutes;
        	
        	obstacle = new EmptyBulletType() {{
        		lifetime = 1f;
        		splashDamage = 40f;
        		splashDamageRadius = 20f;
        		status = TektonStatusEffects.acidified;
        		statusDuration = 60f * 3f;
        		despawnUnit = TektonUnits.formicaEgg;
        		despawnUnitChance = 1f;
        		despawnUnitCount = 1;
        		despawnUnitRadius = 0.1f;
        	}};
        	obstacleFallEffect = TektonFx.biologicalFallingEgg;
        	obstacleTeam = Team.green;
        }};
	}
}
