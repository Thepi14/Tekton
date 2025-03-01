package tekton;

import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.meta.Env;

import static tekton.content.TektonBlocks.setPayloadRegions;

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
