package tekton;

import mindustry.core.World;
import mindustry.entities.Fires;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Fire;
import mindustry.graphics.Pal;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.ui.Bar;
import mindustry.ui.MultiReqImage;
import mindustry.ui.ReqImage;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.turrets.LiquidTurret;
import mindustry.world.consumers.ConsumeItemFilter;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;
import mindustry.world.meta.Stats;
import mindustry.world.modules.ItemModule;
import tekton.TektonNuclearReactor.TektonNuclearReactorBuild;

import static mindustry.Vars.*;

import arc.func.Boolf;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;

public class ItemLiquidTurret extends LiquidTurret {
	
	public Item itemAmmo;

	public ItemLiquidTurret(String name) {
		super(name);
	}
	
	@Override
    public void setStats(){
        super.setStats();
        
        stats.add(Stat.input, itemAmmo);
    }
	
	@Override
    public void setBars(){
        super.setBars();

        addBar("ammo", (ItemLiquidTurretBuild entity) -> new Bar("stat.ammo", Pal.ammo, () -> (float)entity.items.get(itemAmmo) / entity.itemMax));
    }

	@Override
    public void init(){
        consume(new ConsumeItemFilter(i -> 
        		{
        			return i.id == itemAmmo.id;
	        	}
	        ) {
	        	@Override
	            public void build(Building build, Table table){
	                MultiReqImage image = new MultiReqImage();
	                image.add(new ReqImage(new Image(itemAmmo.uiIcon), () -> { return true; }));
	                table.add(image).size(8 * 4);
	            }
	
	            @Override
	            public float efficiency(Building build){
	            	return super.efficiency(build);
	            }
	
	            @Override
	            public void display(Stats stats){
	                //don't display
	            }
	        }
		);
        
        super.init();
    }
	
	public class ItemLiquidTurretBuild extends LiquidTurretBuild {
		
		public float itemMax = 1f;
		public int itemMaxTicks = 30;
		public int currentItemProgress = 0;
		
		@Override
		public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {
	        items = new ItemModule();
	        itemMax = itemCapacity;
	        return super.init(tile, team, shouldAdd, rotation);
		}
		
        @Override
        public boolean shouldActiveSound(){
            return wasShooting && enabled;
        }

        /*@Override
        public void updateTile(){
            unit.ammo(unit.type().ammoCapacity * liquids.currentAmount() / liquidCapacity);
            
            super.updateTile();
        }*/

        @Override
        public BulletType useAmmo(){
            if(cheating()) return ammoTypes.get(liquids.current());
            BulletType type = ammoTypes.get(liquids.current());
            liquids.remove(liquids.current(), 1f / type.ammoMultiplier);
            
            currentItemProgress++;
            if (currentItemProgress >= itemMaxTicks) {
            	currentItemProgress = 0;
                items.remove(new ItemStack(itemAmmo, 1));
            }
            
            return type;
        }

        @Override
        public BulletType peekAmmo(){
            return ammoTypes.get(liquids.current());
        }

        @Override
        public boolean hasAmmo(){
            return ammoTypes.get(liquids.current()) != null && liquids.currentAmount() >= 1f / ammoTypes.get(liquids.current()).ammoMultiplier && items.has(itemAmmo) && items.get(itemAmmo) > 0;
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            return item == itemAmmo && items.get(itemAmmo) < itemCapacity;
        }
        
        @Override
        public int getMaximumAccepted(Item item){
            return itemCapacity;
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            return ammoTypes.get(liquid) != null &&
                (liquids.current() == liquid ||
                ((!ammoTypes.containsKey(liquids.current()) || liquids.get(liquids.current()) <= 1f / ammoTypes.get(liquids.current()).ammoMultiplier + 0.001f)));
        }
        
        @Override
        public void write(Writes write){
            super.write(write);
            write.i(currentItemProgress);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            currentItemProgress = read.i();
        }
    }
}
