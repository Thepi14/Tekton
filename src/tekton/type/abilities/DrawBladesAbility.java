package tekton.type.abilities;

import static mindustry.Vars.world;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.graphics.Trail;
import mindustry.type.UnitType;
import mindustry.world.blocks.environment.Floor;
import tekton.content.TektonVars;
import tekton.math.IDObj;
import tekton.math.RSeq;
import tekton.math.Vec2F;

public class DrawBladesAbility extends Ability {
	public float rotationSpeed = 0.3f;
	public float offset = 2f;
	public int blades = 16;
	public int sides = 3;
	public Color glowColor = Pal.surge;
	
	public float speedChange = 6f, speedChangeSpeed = 0.02f;

	public String bladeSuffix = "-blade";
	private float currentRotation = 0f, currentSpeedChange = 0f, alpha = 1f;
	private TextureRegion bladeRegion;
	private TextureRegion bladeGlowRegion;
	
	public DrawBladesAbility() {
    	display = false;
    }
	
	public DrawBladesAbility(float rotationSpeed, float offset, int blades, int sides, Color glowColor) {
    	display = false;
    	this.rotationSpeed = rotationSpeed;
    	this.offset = offset;
    	this.blades = blades;
    	this.sides = sides;
    	this.glowColor = glowColor;
    }
	
	@Override
	public void init(UnitType type) {
		bladeRegion = Core.atlas.find(type.name + bladeSuffix);
		bladeGlowRegion = Core.atlas.find(type.name + bladeSuffix + "-glow");
	}
	
	@Override
	public void update(Unit unit) {
        super.update(unit);
        currentSpeedChange = Mathf.lerpDelta(currentSpeedChange, unit.moving() ? speedChange : 1f, speedChangeSpeed);
        currentRotation += Time.delta * rotationSpeed * currentSpeedChange;
        alpha = Mathf.lerpDelta(alpha, unit.dead() ? 0f : 1f, 0.08f);
	}
	
	@Override
    public void draw(Unit unit) {
    	var z = Draw.z();
    	
        if (bladeRegion != null) {
        	if (bladeRegion.found()) {
        		for (int i = 0; i < blades; i++) {
        			var rot = ((360f / (float)blades) * i);
        			var bladeRot = rot - currentRotation + unit.rotation;
        			var bladeOffset = (Math.max(0f, Mathf.cosDeg(bladeRot * (float)sides)) * (offset)) + ((1f - alpha) * 8f);
        			
        	    	Draw.z(Layer.flyingUnit - 0.01f);
                	Draw.blend();
        	    	Draw.color(Color.white.cpy().a(alpha));
        	    	float x = unit.x + Angles.trnsx(rot + 90f + currentRotation, bladeOffset), y = unit.y + Angles.trnsy(rot + 90f + currentRotation, bladeOffset), rotation = rot + currentRotation;
                    Draw.rect(bladeRegion, x, y, rotation);
                	if (bladeGlowRegion.found()) {
                    	Draw.z(Layer.flyingUnit - 0.005f);
                    	Draw.blend(Blending.additive);
            	    	Draw.color(glowColor.cpy().a(alpha));
                    	Draw.rect(bladeGlowRegion, x, y, rotation);
                	}
        		}
        	}
        }

    	Draw.blend();
    	Draw.color();
        Draw.z(z);
	}
}
