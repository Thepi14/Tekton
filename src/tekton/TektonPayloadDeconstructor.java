package tekton;

import static tekton.content.TektonBlocks.setPayloadRegions;

import mindustry.world.blocks.payloads.PayloadDeconstructor;
import mindustry.world.meta.Env;

public class TektonPayloadDeconstructor extends PayloadDeconstructor {

	public TektonPayloadDeconstructor(String name) {
		super(name);
		envEnabled = Env.any;
	}
	
	@Override
    public void load(){
        super.load();
        setPayloadRegions(this, regionSuffix);
    }
}
