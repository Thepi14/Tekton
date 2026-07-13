package tekton.type.payloads;

import static tekton.content.TektonBlocks.setPayloadRegions;

import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.meta.Env;

public class TektonReconstructor extends Reconstructor {

	public TektonReconstructor(String name) {
		super(name);
		envEnabled = Env.any;
	}

    @Override
    public void load(){
        super.load();
        setPayloadRegions(this, regionSuffix);
    }
}
