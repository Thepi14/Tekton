package tekton.type.draw;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.Liquid;
import mindustry.world.*;
import mindustry.world.blocks.liquid.LiquidBlock;
import mindustry.world.blocks.production.Pump.*;
import mindustry.world.draw.DrawBlock;

//TODO: what if
public class DrawPumpLiquidDynamic extends DrawBlock{
    public TextureRegion liquid;
    public float padding;
    public float padLeft = -1, padRight = -1, padTop = -1, padBottom = -1;

    @Override
    public void draw(Building build){
        if(!(build instanceof PumpBuild pump) || pump.liquidDrop == null) return;

        LiquidBlock.drawTiledFrames(build.block.size, build.x, build.y, padLeft, padRight, padTop, padBottom, pump.liquidDrop, build.liquids.get(pump.liquidDrop) / build.block.liquidCapacity);
    }

    @Override
    public void load(Block block){
        if(padLeft < 0) padLeft = padding;
        if(padRight < 0) padRight = padding;
        if(padTop < 0) padTop = padding;
        if(padBottom < 0) padBottom = padding;
        liquid = Core.atlas.find(block.name + "-liquid");
    }
}