package tekton.type.draw;

import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import mindustry.gen.Building;
import mindustry.world.draw.DrawGlowRegion;

public class DrawGlowRegionOffset extends DrawGlowRegion  {
	public float offset = 0f;
	
	public DrawGlowRegionOffset(){
    }

    public DrawGlowRegionOffset(float layer){
        super(layer);
    }

    public DrawGlowRegionOffset(boolean rotate){
        super(rotate);
    }


    public DrawGlowRegionOffset(String suffix){
        super(suffix);
    }
	
	@Override
    public void draw(Building build){
        if(build.warmup() <= 0.001f) return;

        float z = Draw.z();
        if(layer > 0) Draw.z(layer);
        Draw.blend(blending);
        Draw.color(color);
        Draw.alpha((Mathf.absin(build.totalProgress() + offset, glowScale, alpha) * glowIntensity + 1f - glowIntensity) * build.warmup() * alpha);
        Draw.rect(region, build.x, build.y, build.totalProgress() * rotateSpeed + (rotate ? build.rotdeg() : 0f));
        Draw.reset();
        Draw.blend();
        Draw.z(z);
    }
}
