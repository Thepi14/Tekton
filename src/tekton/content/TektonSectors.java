package tekton.content;

import mindustry.type.*;
import mindustry.world.blocks.storage.CoreBlock;
import tekton.Tekton;

import static tekton.content.TektonPlanets.*;

import arc.struct.Seq;

public class TektonSectors {
	public static SectorPreset satus, middle, scintilla, proelium, river, pit, lake, transit, rainforest, cave, aequor;
	public static Seq<SectorPreset> all = new Seq<SectorPreset>();
	//92
	public static void load(){
		satus = new TektonSectorPreset("satus", tekton, 0){{
            difficulty = 1;
            alwaysUnlocked = true;
            overrideLaunchDefaults = true;
            addStartingItems = true;
            captureWave = 6;
            
            rules = r -> {
                //r.loadout = Seq.with();
				//r.loadout.addAll(ItemStack.with());
				r.hiddenBuildItems.clear();
				//r.attackMode = true;
				r.waves = true;
                //r.infiniteResources = true;
			};
        }};
        
        middle = new TektonSectorPreset("middle", tekton, 35){{
            difficulty = 1;
            captureWave = 10;
            rules = r -> {
				r.hiddenBuildItems.clear();
				r.waves = true;
			};
        }};
        
        scintilla = new TektonSectorPreset("scintilla", tekton, 37){{
            difficulty = 1;
            rules = r -> {
				r.hiddenBuildItems.clear();
				r.attackMode = true;
				r.waves = false;
			};
        }};
        
        proelium = new TektonSectorPreset("proelium", tekton, 40){{
            difficulty = 2;
            captureWave = 20;
            rules = r -> {
				r.hiddenBuildItems.clear();
				r.attackMode = false;
				r.waves = true;
			};
        }};
        
        river = new TektonSectorPreset("river", tekton, 39){{
            difficulty = 3;
            rules = r -> {
				r.hiddenBuildItems.clear();
				r.attackMode = true;
				r.waves = false;
			};
        }};
        
        pit = new TektonSectorPreset("pit", tekton, 15){{
            difficulty = 2;
            //captureWave = 20;
            rules = r -> {
				r.hiddenBuildItems.clear();
				r.attackMode = true;
				r.waves = true;
			};
        }};
        
        lake = new TektonSectorPreset("lake", tekton, 12){{
            difficulty = 3;
            rules = r -> {
				r.hiddenBuildItems.clear();
				r.attackMode = false;
				r.waves = true;
			};
        }};
        
        transit = new TektonSectorPreset("transit", tekton, 38){{
            difficulty = 4;
            rules = r -> {
				r.hiddenBuildItems.clear();
				r.waves = true;
			};
        }};
        
        rainforest = new TektonSectorPreset("rainforest", tekton, 52){{
            difficulty = 4;
            rules = r -> {
				r.hiddenBuildItems.clear();
				r.attackMode = true;
			};
        }};
        
        //beach
        
        cave = new TektonSectorPreset("cave", tekton, 63){{
            difficulty = 4;
            rules = r -> {
				r.hiddenBuildItems.clear();
				r.attackMode = true;
			};
        }};
        
        aequor = new TektonSectorPreset("aequor", tekton, 77){{
            difficulty = 4;
            rules = r -> {
				r.hiddenBuildItems.clear();
				r.attackMode = true;
				r.waves = false;
				r.placeRangeCheck = false;
			};
        }};
        
        all.addAll(new SectorPreset[] {satus, scintilla, river, proelium, lake/*, aequor*/});
        
        for (var sectorPreset : all) {
        	if (sectorPreset == satus)
        		continue;
        	sectorPreset.alwaysUnlocked = !Tekton.hideContent;
        }
	}
	
	public static class TektonSectorPreset extends SectorPreset{
		
		public TektonSectorPreset(String name, Planet planet, int sector){
			super(name, planet, sector);
	        noLighting = true;
            overrideLaunchDefaults = false;
            rules = r -> {
                r.placeRangeCheck = false;
            };
		}
	}
}
