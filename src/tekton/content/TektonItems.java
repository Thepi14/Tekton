package tekton.content;

import arc.graphics.*;
import arc.struct.*;
import mindustry.content.Items;
import mindustry.type.*;

public class TektonItems {
	public static Item
	iron, zirconium, tantalum, polycarbonate, magnet, polytalum, uranium, nanoAlloy, 
	silica,
	cryogenicCompound;
	
	public static final Seq<Item> tektonItems = new Seq<>(), allItems = new Seq<>();
	
	public static void load(){
		iron = new Item("iron", Color.valueOf("8c857d")){{
            hardness = 1;
            cost = 0.8f;
            alwaysUnlocked = true;
        }};
        
        zirconium = new Item("zirconium", Color.valueOf("d98162")) {{
        	hardness = 2;
            cost = 1.4f;
            alwaysUnlocked = false;
        }};
        
        silica = new Item("silica", Color.valueOf("e3beba")) {{
        	hardness = 1;
            cost = 1f;
            alwaysUnlocked = false;
        }};
        
        polycarbonate = new Item("polycarbonate", Color.valueOf("238277")) {{
        	hardness = 1;
            cost = 2f;
            explosiveness = 0.15f;
            alwaysUnlocked = false;
        }};
        
        tantalum = new Item("tantalum", Color.valueOf("7c7ca3")) {{
        	hardness = 3;
            cost = 3f;
            alwaysUnlocked = false;
        }};
        
        magnet = new Item("magnet", Color.valueOf("cc5050")) {{
        	hardness = 1;
            cost = 3f;
            charge = 0.2f;
            alwaysUnlocked = false;
        }};
        
        polytalum = new Item("polytalum", Color.valueOf("5f98a5")) {{
        	hardness = 3;
            cost = 3.5f;
            explosiveness = 0.2f;
            alwaysUnlocked = false;
        }};
        
        uranium = new Item("uranium", Color.valueOf("50754d")) {{
        	hardness = 4;
            cost = 3.8f;
            explosiveness = 0.35f;
            radioactivity = 3;
            alwaysUnlocked = false;
        }};
        
        nanoAlloy = new Item("nano-alloy", Color.valueOf("df882f")) {{
        	hardness = 5;
            cost = 4.5f;
            radioactivity = 0.77f;
            charge = 0.5f;
            alwaysUnlocked = false;
        }};
        
        cryogenicCompound = new Item("cryogenic-compound", Color.valueOf("80e8ff")) {{
        	hardness = 1;
            explosiveness = 0.5f;
            alwaysUnlocked = false;
        }};
        
        tektonItems.addAll(iron, zirconium, polycarbonate, tantalum, magnet, polytalum, uranium, nanoAlloy, silica, cryogenicCompound);
        allItems.add(tektonItems);
        allItems.addAll(Items.silicon, Items.sand, Items.graphite, Items.phaseFabric);
	}
}
