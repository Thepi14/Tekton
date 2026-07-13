package tekton.type.draw;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import mindustry.content.Items;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;

public class DrawItemLiquidRegion extends DrawBlock{
    public Liquid drawLiquid;
    public Item itemReference = Items.copper;
    public TextureRegion liquid;
    public String suffix = "-itemliquid";
    public float alpha = 1f;

    public DrawItemLiquidRegion(Liquid drawLiquid, Item itemReference){
        this.drawLiquid = drawLiquid;
        this.itemReference = itemReference;
    }

    public DrawItemLiquidRegion(){

    }

    @Override
    public void draw(Building build){
        Liquid drawn = drawLiquid != null ? drawLiquid : build.liquids.current();
        Drawf.liquid(liquid, build.x, build.y,
            (float)build.items.get(itemReference) / (float)build.block.itemCapacity * alpha,
            drawn.color
        );
    }

    @Override
    public void load(Block block){
        if(!block.hasItems){
            throw new RuntimeException("Block '" + block + "' has a DrawItemLiquidRegion, but hasItems is false! Make sure it is true.");
        }

        liquid = Core.atlas.find(block.name + suffix);
    }
}
