package tekton.content;

import arc.graphics.Color;
import mindustry.content.Liquids;
import mindustry.type.CellLiquid;
import mindustry.type.Liquid;
import tekton.Tekton;

public class TektonLiquids {
	public static Liquid ammonia, oxygen, methane, dicyanogen, liquidMethane, metazotoplasm, acid, cobweb;
	
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
            coolant = false;
			gas = true;
			temperature = 0.5f;
			heatCapacity = 0.6f;
			viscosity = 0.1f;
			flammability = 0.1f;
			explosiveness = 0.5f;
			alwaysUnlocked = false;
		}};

        var add = 0.1f;
		methane = new Liquid("methane", TektonColor.methane.cpy().add(add, add, add)) {{
            coolant = false;
			gas = true;
			temperature = 0.3f;
			heatCapacity = 0.6f;
			viscosity = 0.1f;
			flammability = 0.9f;
			explosiveness = 0.5f;
			alwaysUnlocked = true;
            lightColor = TektonColor.methaneGas.cpy().a(0.3f);
            gasColor = TektonColor.methaneGas;
            effect = TektonStatusEffects.tarredInMethane;
		}};
		
		dicyanogen = new Liquid("dicyanogen", TektonColor.dicyanogen.cpy().add(add, add, add)) {{
            coolant = false;
			gas = true;
			temperature = 0.5f;
			heatCapacity = 0.6f;
			viscosity = 0.1f;
			flammability = 2f;
			explosiveness = 1f;
			alwaysUnlocked = true;
            lightColor = TektonColor.dicyanogen.cpy().a(0.3f);
            gasColor = TektonColor.dicyanogen;
            //effect = TektonStatusEffects.tarredInMethane;
		}};
		
		liquidMethane = new Liquid("liquid-methane", TektonColor.liquidMethane) {{ //this one only exists because of freezer
        	hidden = Tekton.hideContent;
            coolant = false;
			gas = false;
			temperature = 0.1f;
			heatCapacity = 0.8f;
			viscosity = 0.6f;
			alwaysUnlocked = false;
			hidden = true;
            boilPoint = 0.15f;
            gasColor = TektonColor.methane;
			canStayOn.addAll(Liquids.water);
            effect = TektonStatusEffects.tarredInMethane;
		}};
		
		acid = new Liquid("acid", TektonColor.acid) {{
        	hidden = false;
            coolant = false;
			gas = false;
			temperature = 1f;
            viscosity = 0.6f;
            effect = TektonStatusEffects.acidified;
            lightColor = Color.valueOf("82d629").a(0.3f);
            boilPoint = 0.9f;
			alwaysUnlocked = false;
			canStayOn.addAll(Liquids.water);
		}};
		
		metazotoplasm = new CellLiquid("metazotoplasm", TektonColor.metazotoplasm){{
        	hidden = true;
            coolant = false;
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
            canStayOn.addAll(Liquids.water, Liquids.oil, Liquids.cryofluid, methane, ammonia);
			alwaysUnlocked = false;
            colorFrom = Color.valueOf("999999");
            colorTo = Color.valueOf("55ff55");
        }};
        
        cobweb = new Liquid("cobweb", TektonColor.cobweb) {{
            coolant = false;
        	hidden = Tekton.hideContent;
            coolant = false;
			gas = false;
			temperature = 0.25f;
            viscosity = 0.9f;
            effect = TektonStatusEffects.cobwebbed;
            lightColor = Color.valueOf("d6d6d6").a(0.3f);
            boilPoint = 0.9f;
			alwaysUnlocked = false;
			canStayOn.addAll(Liquids.water, methane);
		}};
        
        Liquids.water.canStayOn.addAll(ammonia, methane);
	}
}
