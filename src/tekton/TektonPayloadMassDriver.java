package tekton;

import static tekton.content.TektonBlocks.setPayloadRegions;
import static tekton.content.TektonColor.*;

import mindustry.world.blocks.payloads.PayloadMassDriver;
import mindustry.world.meta.Env;

public class TektonPayloadMassDriver extends PayloadMassDriver {

	public TektonPayloadMassDriver(String name) {
		super(name);
		envEnabled = Env.any;
		outlineColor = tektonOutlineColor;
	}

    @Override
    public void load(){
        super.load();
        setPayloadRegions(this, regionSuffix);
    }
}
