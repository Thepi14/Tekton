package tekton.type.payloads;

import static tekton.content.TektonBlocks.setPayloadRegions;

import mindustry.world.blocks.payloads.Constructor;
import mindustry.world.meta.Env;

public class TektonConstructor extends Constructor {

	public TektonConstructor(String name) {
		super(name);
		envEnabled = Env.any;
	}
	
	@Override
    public void load(){
        super.load();
        setPayloadRegions(this, regionSuffix);
    }
}
