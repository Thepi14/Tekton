package tekton;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.graphics.Layer;
import tekton.content.TektonColor;
import tekton.type.gravity.*;

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
	
	public class GravitationalUnitAssemblerBuild extends UnitAssemblerBuild implements GravityConsumer {
		public int gravity = 0;
        public float[] sideGravity = new float[4];
        
        @Override
        public void updateTile() {
    		super.updateTile();
        	
    		efficiency *= gravity() / gravityRequirement();
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
	}
}
