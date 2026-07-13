package tekton.type.payloads;

import static tekton.content.TektonBlocks.setPayloadRegions;

import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.type.Item;
import mindustry.world.blocks.units.UnitAssembler;
import mindustry.world.meta.Env;

public class TektonUnitAssembler extends UnitAssembler {
	public int itemConsumption = 1;
	public float itemDuration = 60f;

	public TektonUnitAssembler(String name) {
		super(name);
		envEnabled = Env.any;
	}

    @Override
    public void load(){
        super.load();
        setPayloadRegions(this, regionSuffix);
    }

    public class TektonUnitAssemblerBuild extends UnitAssemblerBuild {
    	private float itemTimer = 0f;

    	@Override
        public void updateTile(){
    		super.updateTile();

            Item item = items.first();

            if (item == null) {
            	efficiency = 0f;
            	return;
            }

            efficiency *= items.get(item) > 0 ? 1f : 0f;

            itemTimer += edelta() * efficiency;

            if (itemTimer >= itemDuration) {
                items.remove(item, itemConsumption);
                itemTimer = 0f;
            }
        }

    	@Override
    	public void checkTier(){
            modules.sort(b -> b.tier());
            int max = 0;
            for(int i = 0; i < modules.size; i++){
                var mod = modules.get(i);
                if(mod.tier() >= max || mod.tier() == max + 1){
                    max = mod.tier();
                }
            }
            currentTier = max;
        }

    	@Override
        public void write(Writes write){
            super.write(write);

            write.f(itemTimer);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);

            itemTimer = read.f();
        }
    }
}
