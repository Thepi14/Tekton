package tekton.type.payloads;

import static tekton.content.TektonBlocks.setPayloadRegions;

import mindustry.world.blocks.units.UnitAssemblerModule;
import mindustry.world.meta.Env;

public class TektonUnitAssemblerModule extends UnitAssemblerModule {

	public TektonUnitAssemblerModule(String name) {
		super(name);
		envEnabled = Env.any;
	}

    @Override
    public void load(){
        super.load();
        setPayloadRegions(this, regionSuffix);
    }
}
