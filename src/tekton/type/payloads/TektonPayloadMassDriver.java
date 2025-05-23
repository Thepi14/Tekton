package tekton.type.payloads;

import static tekton.content.TektonBlocks.setPayloadRegions;
import static tekton.content.TektonColor.*;

import arc.Core;
import arc.struct.IntSet;
import arc.util.Nullable;
import mindustry.ui.Bar;
import mindustry.world.blocks.payloads.Payload;
import mindustry.world.blocks.payloads.PayloadMassDriver;
import mindustry.world.meta.Env;
import tekton.content.TektonColor;
import tekton.type.gravity.GravityConsumer;
import tekton.type.gravity.GravitationalUnitAssembler.GravitationalUnitAssemblerBuild;

public class TektonPayloadMassDriver extends PayloadMassDriver {
	public int maxGravity = 12;

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
	
	@Override
    public void setBars() {
        super.setBars();
        addBar("gravity", (TektonPayloadDriverBuild entity) -> new Bar(
        		() -> Core.bundle.format("bar.gravityPercent", (int)(Math.abs(entity.gravity) + 0.01f), (int)(entity.gravityFrac() * 100)), 
        		() -> TektonColor.gravityColor, 
				() -> entity.gravityFrac()));
    }
    
    public class TektonPayloadDriverBuild extends PayloadDriverBuild implements GravityConsumer {
		public int gravity = 0;
        public float[] sideGravity = new float[4];
        
        @Override
        public void updateTile() {
        	gravity = Math.min((int)calculateGravity(sideGravity), maxGravity);
    		efficiency *= gravityFrac();
        		
    		super.updateTile();
        }
        
		@Override
		public float gravity() {
			return gravity;
		}
		
		@Override
		public float[] sideGravity() {
			return sideGravity;
		}
		
		@Override
		public float gravityRequirement() {
			return maxGravity;
		}
    	
        public float gravityFrac() {
        	return gravity() / gravityRequirement();
        }
		
		public float calculateGravity(float[] sideGravity) {
            return calculateGravity(sideGravity, null);
        }
		
		@Override
		public float calculateGravity(float[] sideGravity, @Nullable IntSet cameFrom) {
			return calculateGravity(this, sideGravity, cameFrom);
		}
    }
}
