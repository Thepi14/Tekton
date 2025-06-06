package tekton.type.payloads;

import static tekton.content.TektonBlocks.setPayloadRegions;

import mindustry.world.blocks.payloads.PayloadUnloader;
import mindustry.world.meta.Env;

public class TektonPayloadUnloader extends PayloadUnloader {

	public TektonPayloadUnloader(String name) {
		super(name);
		envEnabled = Env.any;
	}

    @Override
    public void load(){
        super.load();
        setPayloadRegions(this, regionSuffix);
    }
}
