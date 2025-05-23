package tekton.type.gravity;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.IntSet;
import arc.util.Nullable;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.ui.Bar;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tekton.content.TektonColor;
import tekton.content.TektonStat;
import tekton.type.payloads.TektonUnitAssembler;

public class GravitationalUnitAssembler extends TektonUnitAssembler {
	public int maxGravity = 12;
	public Color gravityColor = TektonColor.gravityColor;
    public float pulse = 0.3f, pulseScl = 10f;
    
    public TextureRegion gravityRegion;

	public GravitationalUnitAssembler(String name) {
		super(name);
	}
	
	@Override
    public void load(){
        super.load();
        
        gravityRegion = Core.atlas.find(name + "-gravity");
    }
	
	@Override
    public void setBars() {
        super.setBars();
        addBar("gravity", (GravitationalUnitAssemblerBuild entity) -> new Bar(
        		() -> Core.bundle.format("bar.gravityPercent", (int)(Math.abs(entity.gravity) + 0.01f), (int)(entity.gravityFrac() * 100)), 
        		() -> TektonColor.gravityColor, 
				() -> entity.gravityFrac()));
    }
    
    @Override
    public void setStats(){
        super.setStats();
        
        stats.add(TektonStat.gravityUse, maxGravity, TektonStat.gravityPower);
    }
	
	public class GravitationalUnitAssemblerBuild extends TektonUnitAssemblerBuild implements GravityConsumer {
		public int gravity = 0;
        public float[] sideGravity = new float[4];
        
        @Override
        public void updateTile() {
        	gravity = Math.min((int)calculateGravity(sideGravity), maxGravity);
    		efficiency *= gravityFrac();
        		
    		super.updateTile();
        }
        
        @Override
        public void draw() {
        	super.draw();
        	
        	Draw.z(Layer.blockAdditive);
            
            float z = Draw.z();
            Draw.blend(Blending.additive);
            Draw.color(gravityColor, Mathf.clamp(gravity() / gravityRequirement()) * (gravityColor.a * (1f - pulse + Mathf.absin(pulseScl, pulse))));
            Draw.rect(gravityRegion, this.x, this.y);
            Draw.blend();
            Draw.color();
            Draw.z(z);
            
            Draw.reset();
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
