package tekton;

import arc.util.Log;
import arc.util.Time;
import mindustry.game.Team;
import mindustry.mod.Mod;
import mindustry.type.UnitType;
import tekton.content.*;
import ent.anno.Annotations.*;
import tekton.EntityDefinitions;

public class Tekton extends Mod{
	
protected static boolean contentLoadComplete = false;
	
	public static final String MOD_RELEASES = "https://github.com/Thepi14/Tekton/releases";
	public static final String MOD_REPO = "Thepi14/Tekton";
	public static final String MOD_GITHUB_URL = "https://github.com/Thepi14/Tekton.git";
	public static final String MOD_NAME = "tekton";

	public static final boolean loadedComplete(){
		return contentLoadComplete;
	}
	
	public static String name(String name){
		return MOD_NAME + "-" + name;
	}
	
	public Tekton() {
		Log.info("Loaded Tekton constructor.");
	}
	
	public static UnitType myUnitType;
	
    @Override
    public void loadContent(){
		contentLoadComplete = false;
        
        Time.mark();
        TektonSounds.load();
        TektonStatusEffects.load();
        TektonItems.load();
        TektonLiquids.load();
        //TektonBullets.load();
        TektonUnits.load();
        TektonBlocks.load();
        TektonWeathers.load();
        TektonPlanets.load();
        TektonSectors.load();
        TektonTechTree.load();
        TektonFx.load();
        
        Team.blue.emoji = "hapax";
        Team.blue.name = "hapax";
        
		contentLoadComplete = true;
    }
}
