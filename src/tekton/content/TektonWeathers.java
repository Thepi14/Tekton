package tekton.content;

import arc.graphics.*;
import arc.util.*;
import mindustry.content.StatusEffects;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.type.weather.*;
import mindustry.world.meta.*;
import tekton.StormWeather;

public class TektonWeathers {
	public static Weather
	methaneRain,
	acidRain,
	electricStorm,
	darkSandStorm,
	cryoVolcanicSnow
	;
	
	public static void load(){
		methaneRain = new RainWeather("methane-rain"){{
	        attrs.set(Attribute.light, -0.2f);
	        attrs.set(TektonAttributes.methane, 0.2f);
	        status = TektonStatusEffects.tarredInMethane;
	        sound = Sounds.rain;
	        soundVol = 0.25f;
	        liquid = TektonLiquids.liquidMethane;
	        color = TektonLiquids.methane.color.cpy();
            duration = 8f * Time.toMinutes;
	    }};
	    
	    acidRain = new RainWeather("acid-rain"){{
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
	    	color = noiseColor = Color.valueOf("3d352f");
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
	    
	    darkSandStorm = new ParticleWeather("dark-sandstorm"){{
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
        
        cryoVolcanicSnow = new ParticleWeather("cryovolcanic-snow"){{
            particleRegion = "particle";
            sizeMax = 13f;
            sizeMin = 2.6f;
            density = 1200f;
            attrs.set(Attribute.light, -0.15f);
            attrs.set(Attribute.water, -0.1f);
	        attrs.set(TektonAttributes.methane, 0.1f);
	        
	        color = TektonItems.cryogenicCompound.color.cpy();
	        status = StatusEffects.freezing;

            sound = Sounds.windhowl;
            soundVol = 0f;
            soundVolOscMag = 1.5f;
            soundVolOscScl = 1100f;
            soundVolMin = 0.02f;
            
            duration = 7f * Time.toMinutes;
        }};
	}
}
