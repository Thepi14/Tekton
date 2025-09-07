package tekton.type.draw;

import mindustry.content.Items;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.draw.DrawBlock;

public class DrawItemLiquidTile extends DrawBlock {
	public Liquid drawLiquid;
	public Item itemReference = Items.copper;
    public float padding;
    public float padLeft = -1, padRight = -1, padTop = -1, padBottom = -1;
    public float alpha = 1f;

    public DrawItemLiquidTile(Liquid drawLiquid, Item itemReference, float padding){
    	this(drawLiquid, itemReference);
        this.padding = padding;
    }

    public DrawItemLiquidTile(Liquid drawLiquid, Item itemReference){
        this.drawLiquid = drawLiquid;
        this.itemReference = itemReference;
    }

    public DrawItemLiquidTile(){
    }

    @Override
    public void draw(Building build){
        Liquid drawn = drawLiquid != null ? drawLiquid : build.liquids.current();
        LiquidBlock.drawTiledFrames(build.block.size, build.x, build.y, padLeft, padRight, padTop, padBottom, drawn, ((float)build.items.get(itemReference) / (float)build.block.itemCapacity) * alpha);
    }

    @Override
    public void load(Block block){
        if(padLeft < 0) padLeft = padding;
        if(padRight < 0) padRight = padding;
        if(padTop < 0) padTop = padding;
        if(padBottom < 0) padBottom = padding;
    }
}
