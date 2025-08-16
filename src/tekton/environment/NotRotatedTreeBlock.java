package tekton.environment;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.TreeBlock;

public class NotRotatedTreeBlock extends TreeBlock {

	public NotRotatedTreeBlock(String name) {
		super(name);
	}
	
	@Override
    public void drawBase(Tile tile){

        float
        x = tile.worldx(), y = tile.worldy(),
        w = region.width * region.scl(), h = region.height * region.scl(),
        scl = 30f, mag = 0.2f;

        TextureRegion shad = variants == 0 ? customShadowRegion : variantShadowRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantShadowRegions.length - 1))];

        if(shad.found()){
            Draw.z(Layer.power - 1);
            Draw.rect(shad, tile.worldx() + shadowOffset, tile.worldy() + shadowOffset, 0);
        }

        TextureRegion reg = variants == 0 ? region : variantRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1))];
        
        Draw.z(Layer.power + 1);
        Draw.rectv(reg, x, y, w, h, 0, vec -> vec.add(
        Mathf.sin(vec.y*3 + Time.time, scl, mag) + Mathf.sin(vec.x*3 - Time.time, 70, 0.8f),
        Mathf.cos(vec.x*3 + Time.time + 8, scl + 6f, mag * 1.1f) + Mathf.sin(vec.y*3 - Time.time, 50, 0.2f)
        ));
    }

    @Override
    public void drawShadow(Tile tile){}
}
