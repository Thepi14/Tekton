package tekton.type.defense;

import arc.struct.EnumSet;
import mindustry.world.blocks.power.LightBlock;
import mindustry.world.meta.BlockFlag;

public class TeamLight extends LightBlock {

	public TeamLight(String name) {
		super(name);
        flags = EnumSet.of(BlockFlag.hasFogRadius);
	}
    
    public class TeamLightBuild extends LightBuild {
    	@Override
        public float fogRadius(){
            return fogRadius * efficiency;
        }
    }
}
