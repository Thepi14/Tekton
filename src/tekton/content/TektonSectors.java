package tekton.content;

import mindustry.type.*;
import mindustry.world.blocks.storage.CoreBlock;

import static tekton.content.TektonPlanets.*;

import arc.struct.Seq;

public class TektonSectors {
	public static SectorPreset satus, scintilla, proelium, river, lake/*, aequor*/;
	public static Seq<SectorPreset> all = new Seq<SectorPreset>();
	//63
	public static void load(){
		satus = new TektonSectorPreset("satus", tekton, 0){{
            alwaysUnlocked = true;
            overrideLaunchDefaults = true;
            addStartingItems = true;
            captureWave = 6;
            difficulty = 1;
            alwaysUnlocked = true;
            
            rules = r -> {
                //r.loadout = Seq.with();
				//r.loadout.addAll(ItemStack.with());
				r.hiddenBuildItems.clear();
				//r.attackMode = true;
				r.waves = true;
                //r.infiniteResources = true;
			};
        }};
        
        scintilla = new TektonSectorPreset("scintilla", tekton, 35){{
            difficulty = 1;
            rules = r -> {
				r.hiddenBuildItems.clear();
				r.attackMode = true;
				r.waves = false;
				r.placeRangeCheck = false;
			};
        }};
        
        proelium = new TektonSectorPreset("proelium", tekton, 37){{
            captureWave = 20;
            difficulty = 2;
            rules = r -> {
				r.hiddenBuildItems.clear();
				r.attackMode = false;
				r.waves = true;
			};
        }};
        
        river = new TektonSectorPreset("river", tekton, 36){{
            difficulty = 3;
            rules = r -> {
				r.hiddenBuildItems.clear();
				r.attackMode = true;
				r.waves = false;
				r.placeRangeCheck = false;
			};
        }};
        
        lake = new TektonSectorPreset("lake", tekton, 34){{
            difficulty = 7;
            rules = r -> {
				r.hiddenBuildItems.clear();
				r.attackMode = false;
				r.waves = true;
			};
        }};
        
        /*aequor = new TektonSectorPreset("aequor", tekton, 3){{
            difficulty = 2;
            rules = r -> {
				r.hiddenBuildItems.clear();
				r.attackMode = true;
				r.waves = false;
				r.placeRangeCheck = false;
			};
        }};*/
        
        all.addAll(new SectorPreset[] {satus, scintilla, river, proelium/*, aequor*/});
	}
	
	public static class TektonSectorPreset extends SectorPreset{
		
		public TektonSectorPreset(String name, Planet planet, int sector){
			super(name, planet, sector);
	        noLighting = true;
            overrideLaunchDefaults = false;
		}
	}
}
