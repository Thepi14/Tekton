package tekton.type.payloads;

import static tekton.content.TektonBlocks.setPayloadRegions;

import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.meta.Env;

public class TektonUnitFactory extends UnitFactory {

	public TektonUnitFactory(String name) {
		super(name);
		envEnabled = Env.any;
	}

    @Override
    public void load(){
        super.load();
        setPayloadRegions(this, regionSuffix);
    }
}
