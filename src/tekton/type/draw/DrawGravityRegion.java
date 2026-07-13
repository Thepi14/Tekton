package tekton.type.draw;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;
import tekton.content.TektonColor;
import tekton.type.gravity.GravityConsumer;

public class DrawGravityRegion extends DrawBlock{
	public Color color = TektonColor.gravityColor;
    public float pulse = 0.3f, pulseScl = 10f;
    public float layer = Layer.blockAdditive;

    public TextureRegion gravity;
    public String suffix = "-glow";

    public DrawGravityRegion(float layer){
        this.layer = layer;
    }

    public DrawGravityRegion(String suffix){
        this.suffix = suffix;
    }

    public DrawGravityRegion(){
    }

    @Override
    public void draw(Building build){
        Draw.z(Layer.blockAdditive);

        if(build instanceof GravityConsumer hc && hc.gravity() > 0){
            float z = Draw.z();
            if(layer > 0) {
				Draw.z(layer);
			}
            Draw.blend(Blending.additive);
            Draw.color(color, Mathf.clamp(hc.gravity() / hc.gravityRequirement()) * (color.a * (1f - pulse + Mathf.absin(pulseScl, pulse))));
            Draw.rect(gravity, build.x, build.y);
            Draw.blend();
            Draw.color();
            Draw.z(z);
        }

        Draw.reset();
    }

    @Override
    public void load(Block block){
    	gravity = Core.atlas.find(block.name + suffix);
    }
}
