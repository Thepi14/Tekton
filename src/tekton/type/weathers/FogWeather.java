package tekton.type.weathers;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.Texture;
import arc.graphics.Texture.*;
import arc.graphics.g2d.Draw;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.gen.WeatherState;
import mindustry.graphics.Layer;
import mindustry.type.weather.ParticleWeather;

public class FogWeather extends ParticleWeather {
	public float opacity = 1f;
	public boolean drawColor = true;
	
	public FogWeather(String name) {
		super(name);
	}
	
	@Override
    public void drawOver(WeatherState state) {
        float windx, windy;
        if(useWindVector) {
            float speed = baseSpeed * state.intensity;
            windx = state.windVector.x * speed;
            windy = state.windVector.y * speed;
        }
        else {
            windx = this.xspeed;
            windy = this.yspeed;
        }
        
        if (drawColor) {
            var z = Draw.z();
            Draw.z(Layer.fogOfWar + 1);
            Draw.alpha(opacity);
            Draw.tint(color);
            Draw.rect(Core.atlas.white(), Core.camera.position.x, Core.camera.position.y, Core.camera.width, -Core.camera.height);
            Draw.alpha(1f);
            Draw.color();
            Draw.z(z);
        }

        if (drawNoise) {
            if (noise == null) {
                noise = Core.assets.get("sprites/" + noisePath + ".png", Texture.class);
                noise.setWrap(TextureWrap.repeat);
                noise.setFilter(TextureFilter.linear);
            }

            float sspeed = 1f, sscl = 1f, salpha = 1f, offset = 0f;
            Color col = Tmp.c1.set(noiseColor);
            for (int i = 0; i < noiseLayers; i++) {
                drawNoise(noise, noiseColor, noiseScale * sscl, opacity * salpha * opacityMultiplier, sspeed * (useWindVector ? 1f : baseSpeed), 1f, windx, windy, offset);
                sspeed *= noiseLayerSpeedM;
                salpha *= noiseLayerAlphaM;
                sscl *= noiseLayerSclM;
                offset += 0.29f;
                col.mul(noiseLayerColorM);
            }
        }

        if(drawParticles) {
            drawParticles(region, color, sizeMin, sizeMax, density, state.intensity, state.opacity, windx, windy, minAlpha, maxAlpha, sinSclMin, sinSclMax, sinMagMin, sinMagMax, randomParticleRotation);
        }
    }
	
	public static void drawNoise(Texture noise, Color color, float noisescl, float opacity, float baseSpeed, float intensity, float vwindx, float vwindy, float offset) {
        Draw.alpha(opacity);
        Draw.tint(color);
        
        float speed = baseSpeed * intensity;
        float windx = vwindx * speed, windy = vwindy * speed;
        
        float scale = 1f / noisescl;
        float scroll = Time.time * scale + offset;
        Tmp.tr1.texture = noise;
        Core.camera.bounds(Tmp.r1);
        Tmp.tr1.set(Tmp.r1.x * scale, Tmp.r1.y * scale, (Tmp.r1.x + Tmp.r1.width) * scale, (Tmp.r1.y + Tmp.r1.height) * scale);
        Tmp.tr1.scroll(-windx * scroll, -windy * scroll);
        Draw.rect(Tmp.tr1, Core.camera.position.x, Core.camera.position.y, Core.camera.width, -Core.camera.height);
    }
}
