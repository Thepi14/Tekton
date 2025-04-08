package tekton;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import mindustry.content.Items;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;

public class DrawItemFulness extends DrawBlock {
    public Item item = Items.copper;
    public TextureRegion itemRegion;
    public String suffix = "-item";
    public float alpha = 1f;

    public DrawItemFulness(Item item){
        this.item = item;
    }

    public DrawItemFulness(){
    	
    }

    @Override
    public void draw(Building build){
    	Item drawn = item != null ? item : build.items.first();
        Drawf.liquid(itemRegion, build.x, build.y,
            (float)build.items.get(item) / (float)build.block.itemCapacity * alpha,
            drawn.color
        );
    }

    @Override
    public void load(Block block){
        if(!block.hasItems){
            throw new RuntimeException("Block '" + block + "' has a DrawItemLiquidRegion, but hasItems is false! Make sure it is true.");
        }

        itemRegion = Core.atlas.find(block.name + suffix);
    }
}
