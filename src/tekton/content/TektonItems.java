package tekton.content;

import arc.graphics.*;
import arc.struct.*;
import mindustry.content.Items;
import mindustry.type.*;

public class TektonItems {
	public static Item
	iron, zirconium, polycarbonate, tantalum, polytalum, uranium, nanoAlloy, 
	silica,
	cryogenicCompound;
	
	public static final Seq<Item> tektonItems = new Seq<>(), allItems = new Seq<>();
	
	public static void load(){
		iron = new Item("iron", Color.valueOf("9e9090")){{
			description = "It is a metal material, one of the most common elements in universe.";
            hardness = 1;
            cost = 0.8f;
            alwaysUnlocked = true;
        }};
        
        zirconium = new Item("zirconium", Color.valueOf("4a3f37")) {{
        	description = "It is a transition metal material, it's ductile, malleable and corrosion-resistant.";
        	hardness = 2;
            cost = 1.4f;
            alwaysUnlocked = false;
        }};
        
        silica = new Item("silica", Color.valueOf("e3beba")) {{
        	description = "Also known as silicon dioxide, it's a major constituent of sand. It is used in structural materials and microelectronics. Can be extracted from normal sand.";
        	hardness = 1;
            cost = 1f;
            alwaysUnlocked = false;
        }};
        
        polycarbonate = new Item("polycarbonate", Color.valueOf("bafff7")) {{
        	description = "A resistant, transparent plastic material.";
        	hardness = 2;
            cost = 2f;
            explosiveness = 0.15f;
            alwaysUnlocked = false;
        }};
        
        tantalum = new Item("tantalum", Color.valueOf("7c7ca3")) {{
        	description = "It is a transition metal material, it's ductile, very hard and highly corrosion-resistant.";
        	hardness = 3;
            cost = 3f;
            alwaysUnlocked = false;
        }};
        
        polytalum = new Item("polytalum", Color.valueOf("5f98a5")) {{
        	description = "Used in advanced units, insulation and fragmentation ammunition. It's more resistant than plastanium.";
        	hardness = 2;
            cost = 3.5f;
            explosiveness = 0.2f;
            alwaysUnlocked = false;
        }};
        
        uranium = new Item("uranium", Color.valueOf("7b8779")) {{
        	description = "It is a metal material, very dense and resistant. It's very radioactive.";
        	hardness = 4;
            cost = 3.8f;
            explosiveness = 0.35f;
            radioactivity = 3;
            alwaysUnlocked = false;
        }};
        
        nanoAlloy = new Item("nano-alloy", Color.valueOf("df882f")) {{
        	description = "It is a highly technological and strong material. Used in extremely advanced artifacts.";
        	hardness = 5;
            cost = 4.5f;
            radioactivity = 0.77f;
            charge = 0.5f;
            alwaysUnlocked = false;
        }};
        
        cryogenicCompound = new Item("cryogenic-compound", Color.valueOf("87ceeb")) {{
        	description = "It's a solidified cryogenic fluid compost alternative, extremely cold.";
        	hardness = 1;
            explosiveness = 0.5f;
            alwaysUnlocked = false;
        }};
        
        tektonItems.addAll(iron, zirconium, polycarbonate, tantalum, polytalum, uranium, nanoAlloy, silica, cryogenicCompound);
        allItems.add(tektonItems);
        allItems.addAll(Items.silicon, Items.sand, Items.graphite, Items.phaseFabric);
	}
}
