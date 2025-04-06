package tekton.type.gravity;

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

public class DrawGravityOutput extends DrawBlock {
	public TextureRegion gravity, glow, top1, top2;

    public Color heatColor = TektonColor.gravityColor;
    public float gravityPulse = 0.3f, gravityPulseScl = 10f, glowMult = 1.2f;

    public int rotOffset = 0;
    public boolean drawGlow = true;

    public DrawGravityOutput(){}

    public DrawGravityOutput(int rotOffset, boolean drawGlow){
        this.rotOffset = rotOffset;
        this.drawGlow = drawGlow;
    }

    @Override
    public void draw(Building build){
        float rotdeg = (build.rotation + rotOffset) * 90;
        Draw.rect(Mathf.mod((build.rotation + rotOffset), 4) > 1 ? top2 : top1, build.x, build.y, rotdeg);

        if(build instanceof GravityBlock graviter && graviter.gravity() > 0){
            Draw.z(Layer.blockAdditive);
            Draw.blend(Blending.additive);
            Draw.color(heatColor, graviter.gravityFrac() * (heatColor.a * (1f - gravityPulse + Mathf.absin(gravityPulseScl, gravityPulse))));
            if(gravity.found()) Draw.rect(gravity, build.x, build.y, rotdeg);
            Draw.color(Draw.getColor().mul(glowMult));
            if(drawGlow && glow.found()) Draw.rect(glow, build.x, build.y);
            Draw.blend();
            Draw.color();
        }
        
        Draw.reset();
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list){
        Draw.rect(Mathf.mod((plan.rotation + rotOffset), 4) > 1 ? top2 : top1, plan.drawx(), plan.drawy(), (plan.rotation + rotOffset) * 90);
    }

    @Override
    public void load(Block block){
    	gravity = Core.atlas.find(block.name + "-gravity");
        glow = Core.atlas.find(block.name + "-glow");
        top1 = Core.atlas.find(block.name + "-top1");
        top2 = Core.atlas.find(block.name + "-top2");
    }
}
