package tekton.content;

import arc.graphics.Color;
import mindustry.content.Liquids;
import mindustry.type.CellLiquid;
import mindustry.type.Liquid;

public class TektonLiquids {
	public static Liquid ammonia, oxygen, methane, liquidMethane, metazotoplasm, acid;
	
	public static void load(){
		ammonia = new Liquid("ammonia", TektonColor.ammonia) {{
			gas = false;
			temperature = 0.3f;
			heatCapacity = 0.6f;
			viscosity = 0.8f;
			flammability = 0f;
			explosiveness = 0;
			alwaysUnlocked = false;
			canStayOn.addAll(Liquids.water);
            boilPoint = 0.8f;
		}};
		
		oxygen = new Liquid("oxygen", TektonColor.oxygen) {{
			gas = true;
			temperature = 0.5f;
			heatCapacity = 0.6f;
			viscosity = 0.1f;
			flammability = 0.1f;
			explosiveness = 0.5f;
			alwaysUnlocked = false;
		}};
		
		methane = new Liquid("methane", TektonColor.methane) {{
			gas = true;
			temperature = 0.5f;
			heatCapacity = 0.6f;
			viscosity = 0.1f;
			flammability = 0.9f;
			explosiveness = 0.5f;
			alwaysUnlocked = true;
            gasColor = TektonColor.methane;
            effect = TektonStatusEffects.tarredInMethane;
		}};
		
		liquidMethane = new Liquid("liquid-methane", TektonColor.liquidMethane) {{
			gas = false;
			temperature = 0.1f;
			heatCapacity = 0.8f;
			viscosity = 0.6f;
			alwaysUnlocked = true;
            boilPoint = 0.15f;
            gasColor = TektonColor.methane;
			canStayOn.addAll(Liquids.water);
            effect = TektonStatusEffects.tarredInMethane;
		}};
		
		acid = new Liquid("acid", Color.valueOf("82d629")) {{
            coolant = false;
			gas = false;
			temperature = 1f;
            viscosity = 0.3f;
            effect = TektonStatusEffects.acidified;
            lightColor = Color.valueOf("82d629").a(0.3f);
            boilPoint = 0.9f;
			alwaysUnlocked = false;
			canStayOn.addAll(Liquids.water);
		}};
		
		metazotoplasm = new CellLiquid("metazotoplasm", TektonColor.metazotoplasm){{
            heatCapacity = 1f;
            temperature = 0.25f;
            viscosity = 0.7f;
            flammability = 2f;
			explosiveness = 0.1f;
            capPuddles = false;
            spreadTarget = methane;
            moveThroughBlocks = true;
            incinerable = true;
            blockReactive = false;
            canStayOn.addAll(Liquids.water, Liquids.oil, Liquids.cryofluid, liquidMethane, ammonia);
			alwaysUnlocked = false;
            colorFrom = Color.valueOf("999999");
            colorTo = Color.valueOf("55ff55");
        }};
        
        Liquids.water.canStayOn.addAll(ammonia, liquidMethane);
	}
}
