package tekton.type.draw;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
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
            if(layer > 0) Draw.z(layer);
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
