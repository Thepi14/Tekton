package tekton.type.draw;

import arc.graphics.Color;
import arc.math.*;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.world.draw.DrawBlock;

//TODO: maybe remove?
public class DrawLight extends DrawBlock {
	public float lightRadius = 30f, lightSinScl = 10f, lightSinMag = 5f;
	public Color lightColor = Color.white;
	public float alpha = 0.65f;
	public boolean fixed = false;
	
	public DrawLight() {
		
	}
	
	public DrawLight(Color color) {
		lightColor = color;
	}
	
	public DrawLight(Color color, float radius) {
		lightColor = color;
		lightRadius = radius;
	}

    @Override
    public void drawLight(Building build){
    	if (build.efficiency >= 0.999f)
	    	if (!fixed) {
	    		Drawf.light(build.x, build.y, ((lightRadius + Mathf.absin(lightSinScl, lightSinMag)) * build.warmup()) + build.block.size, lightColor, alpha);
			}
	    	else {
	    		Drawf.light(build.x, build.y, (lightRadius * build.warmup()) + build.block.size, lightColor, alpha);
	    	}
    }
}
