package tekton.type.payloads;

import mindustry.world.blocks.units.*;
import mindustry.world.meta.Env;

import static tekton.content.TektonBlocks.setPayloadRegions;

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
