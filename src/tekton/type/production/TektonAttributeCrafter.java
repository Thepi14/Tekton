package tekton.type.production;
import mindustry.world.blocks.production.AttributeCrafter;

//I created this class by one single reason: When the player tries to place the Atmospheric Methane Concentrator, 
//it displays 100% efficiency, but when placed, it acquires one more 100%, amounting to 200% efficiency, this 
//happens because the attribute.env() is only accounted in the building, but not on the placing section, 
//i hope Mindustry fixes it.

public class TektonAttributeCrafter extends AttributeCrafter {
	public float efficiencyAdd = -1f;
	
	public TektonAttributeCrafter(String name) {
		super(name);
	}

	public class TektonAttributeCrafterBuild extends AttributeCrafterBuild{
		@Override
        public float efficiencyMultiplier(){
            return baseEfficiency + Math.min(maxBoost, boostScale * attrsum) + attribute.env() + efficiencyAdd;
        }
    }
}
