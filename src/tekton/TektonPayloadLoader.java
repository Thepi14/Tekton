package tekton;

import static tekton.content.TektonBlocks.setPayloadRegions;

import mindustry.world.blocks.payloads.PayloadLoader;
import mindustry.world.meta.Env;

public class TektonPayloadLoader extends PayloadLoader {
	
	public TektonPayloadLoader(String name) {
		super(name);
		envEnabled = Env.any;
	}

    @Override
    public void load(){
        super.load();
        setPayloadRegions(this, regionSuffix);
    }
}