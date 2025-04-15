package tekton.type.draw;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.DrawBlock;
import tekton.content.TektonColor;
import tekton.type.gravity.GravityConsumer;

public class DrawGravityInput extends DrawBlock {
	public String suffix = "-gravity";
    public Color gravityColor = TektonColor.gravityColor;
    public float gravityPulse = 0.3f, gravityPulseScl = 10f;
    public boolean drawSides = true;

    public TextureRegion gravity;

    public DrawGravityInput(String suffix){
        this.suffix = suffix;
    }

    public DrawGravityInput(){
    	
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list){
    }

    @Override
    public void draw(Building build){
        Draw.z(Layer.blockAdditive);
        
        if(build instanceof GravityConsumer hc){
            float[] side = hc.sideGravity();
            if (drawSides)
	            for(int i = 0; i < 4; i++){
	                if(side[i] > 0){
	                    Draw.blend(Blending.additive);
	                    Draw.color(gravityColor, side[i] / hc.gravityRequirement() * (gravityColor.a * (1f - gravityPulse + Mathf.absin(gravityPulseScl, gravityPulse))));
	                    Draw.rect(gravity, build.x, build.y, i * 90f);
	                    Draw.blend();
	                    Draw.color();
	                }
	            }
            else {
            	Draw.blend(Blending.additive);
                Draw.color(gravityColor, hc.gravityRequirement() * (gravityColor.a * (1f - gravityPulse + Mathf.absin(gravityPulseScl, gravityPulse))));
                Draw.rect(gravity, build.x, build.y, build.rotation * 90f);
                Draw.blend();
                Draw.color();
            }
        }
        
        Draw.z(Layer.block);
        
        Draw.reset();
    }

    @Override
    public void load(Block block){
    	gravity = Core.atlas.find(block.name + suffix);
    }
}
