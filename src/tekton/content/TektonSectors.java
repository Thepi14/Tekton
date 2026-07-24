package tekton.content;

import static tekton.content.TektonPlanets.tekton;

import java.lang.reflect.Field;

import arc.Core;
import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.gen.Icon;
import mindustry.graphics.MultiPacker;
import mindustry.graphics.MultiPacker.PageType;
import mindustry.type.Planet;
import mindustry.type.SectorPreset;
import tekton.Tekton;

public class TektonSectors {
	public static SectorPreset 
	
	satus, middle, 
	scintilla, proelium, pit, 
	lake, river, infestation, 
	rainforest, beach, cave, aequor, 
	transit, colony, 
	radiation;
	
	public static Seq<SectorPreset> all = new Seq<SectorPreset>();
	
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
				//r.hiddenBuildItems.clear();
				//r.attackMode = true;
				r.waves = true;
                //r.infiniteResources = true;
			};
        }};

        middle = new TektonSectorPreset("middle", tekton, 35){{
            difficulty = 1;
            captureWave = 10;
            rules = r -> {
				//r.hiddenBuildItems.clear();
				r.waves = true;
			};
        }};

        scintilla = new TektonSectorPreset("scintilla", tekton, 37){{
            difficulty = 1;
            rules = r -> {
				//r.hiddenBuildItems.clear();
				r.attackMode = true;
				r.waves = false;
			};
        }};

        proelium = new TektonSectorPreset("proelium", tekton, 40){{
            difficulty = 2;
            captureWave = 20;
            rules = r -> {
				//r.hiddenBuildItems.clear();
				r.attackMode = false;
				r.waves = true;
			};
        }};

        pit = new TektonSectorPreset("pit", tekton, 15){{
            difficulty = 3;
            //captureWave = 20;
            rules = r -> {
				//r.hiddenBuildItems.clear();
				r.attackMode = true;
				r.waves = true;
			};
        }};

        lake = new TektonSectorPreset("lake", tekton, 12){{
            difficulty = 4;
            rules = r -> {
				//r.hiddenBuildItems.clear();
				r.attackMode = false;
				r.waves = true;
			};
        }};

        river = new TektonSectorPreset("river", tekton, 39){{
            difficulty = 3;
            rules = r -> {
				//r.hiddenBuildItems.clear();
				r.attackMode = true;
				r.waves = false;
			};
        }};
        
        infestation = new TektonSectorPreset("infestation", tekton, 23){{
            difficulty = 4;
            captureWave = 6;
            rules = r -> {
				//r.hiddenBuildItems.clear();
				//r.attackMode = false;
			};
        }};

        rainforest = new TektonSectorPreset("rainforest", tekton, 52){{
            difficulty = 5;
            rules = r -> {
				//r.hiddenBuildItems.clear();
				//r.attackMode = false;
			};
        }};

        transit = new TektonSectorPreset("transit", tekton, 19){{
            difficulty = 6;
            rules = r -> {
				//r.hiddenBuildItems.clear();
				r.waves = true;
			};
        }};

        beach = new TektonSectorPreset("beach", tekton, 91){{
            difficulty = 6;
            captureWave = 61;
            rules = r -> {
				//r.hiddenBuildItems.clear();
			};
        }};

        cave = new TektonSectorPreset("cave", tekton, 63){{
            difficulty = 6;
            rules = r -> {
				//r.hiddenBuildItems.clear();
				r.attackMode = true;
			};
        }};

        aequor = new TektonSectorPreset("aequor", tekton, 2){{
            difficulty = 6;
            rules = r -> {
				//r.hiddenBuildItems.clear();
				r.attackMode = true;
				r.waves = false;
			};
        }};
        
        colony = new TektonSectorPreset("colony", tekton, 78){{
            difficulty = 6;
            captureWave = 3;
            rules = r -> {
				//r.hiddenBuildItems.clear();
			};
        }};
        
        radiation = new TektonSectorPreset("radiation", tekton, 45){{
            difficulty = 6;
            captureWave = 3;
            rules = r -> {
				//r.hiddenBuildItems.clear();
			};
        }};

		Class<?> sectors = TektonSectors.class;
		Seq<Field> sectorFields = new Seq<>(sectors.getFields());
		sectorFields.retainAll(f -> SectorPreset.class.equals(f.getType()));
		for (var sector : sectorFields) {
			sector.setAccessible(true);
			try {
				if (sector.get(sectors) instanceof SectorPreset preset) {
					all.add(preset);
				}
			} catch (IllegalArgumentException e) { //java.
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

        for (var sectorPreset : all) {
        	if (sectorPreset == satus) {
				continue;
			}
        	sectorPreset.alwaysUnlocked = !Tekton.hideContent;
        }
	}

	public static class TektonSectorPreset extends SectorPreset{
		
		public boolean outline = true;
	    public int outlineRadius = 5;
		public Color outlineColor = TektonColor.tektonOutlineColor;

		public TektonSectorPreset(String name, Planet planet, int sector){
			super(name, planet, sector);
	        noLighting = true;
            overrideLaunchDefaults = false;
            rules = r -> {
                r.placeRangeCheck = false;
            };
		}

		//TODO: remove all of this once the game updates. TODO: (2) this doesn't work for some reason.
	    @Override
	    public void createIcons(MultiPacker packer){
	        super.createIcons(packer);

	        if(outline && Core.atlas.has("sector-" + name)){
	            makeOutline(PageType.ui, packer, Core.atlas.find("sector-" + name), false, outlineColor, outlineRadius);
	        }
	    }

	    @Override
	    public void loadIcon(){
	        if(Icon.terrain != null){
	            uiIcon = fullIcon = Core.atlas.find("sector-" + name, Icon.terrain.getRegion());
	        }
	    }
	}
}
