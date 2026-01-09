package tekton.type.world;

import static mindustry.Vars.*;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.Texture;
import arc.graphics.Texture.TextureFilter;
import arc.graphics.Texture.TextureWrap;
import arc.graphics.g2d.Draw;
import arc.util.Tmp;
import mindustry.graphics.Layer;
import mindustry.type.Weather;
import tekton.content.TektonColor;

public class TektonEnvRenderer {
	public static void load() {
		renderer.addEnvRenderer(TektonEnv.methane, () -> {
            Texture tex = Core.assets.get("sprites/fog.png", Texture.class);
            tex.setWrap(TextureWrap.repeat);
            tex.setFilter(TextureFilter.linear);
            
            var opacity = 0.4f;
            var noiseOpacity = 0.15f;
            var color = TektonColor.liquidMethane.cpy().a(0.5f);

            var z = Draw.z();
            Draw.z(Layer.fogOfWar + 1);
            Draw.alpha(opacity);
            Draw.tint(color);
            Draw.rect(Core.atlas.white(), Core.camera.position.x, Core.camera.position.y, Core.camera.width, -Core.camera.height);
            Draw.alpha(1f);
            Draw.color();
            Draw.z(z);

            //TODO layer looks better? should not be conditional
            Draw.z(state.rules.fog ? Layer.fogOfWar + 1 : Layer.weather - 1);
            Weather.drawNoiseLayers(tex, color, 
            		1100f, //noiseScl
            		noiseOpacity,
            		0.05f, //baseSpeed
            		1f, //intensity
            		1f, //vwindx
            		0.01f, //vwindy
            		3, //layers
            		2f, //layerSpeedM
            		0.7f, //layerAlphaM
            		0.6f, //layerSclM
            		0.9f); //layerColorM
            Draw.reset();
        });
	}
}
