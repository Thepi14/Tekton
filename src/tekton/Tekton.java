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
	//public static final String SERVER = "175.178.22.6:6666", SERVER_ADDRESS = "175.178.22.6", SERVER_AUZ_NAME = "A";
	//public static final String EU_NH_SERVER = "Emphasize.cn:12510";

	public static final boolean loadedComplete(){
		return contentLoadComplete;
	}
	
	public static String name(String name){
		return MOD_NAME + "-" + name;
	}
	
	public Tekton() {
		Log.info("Loaded Tekton constructor.");

        //listen for game load event
        /*Events.on(ClientLoadEvent.class, e -> {
            //show dialog upon startup
            Time.runTask(500f, () -> {
                BaseDialog dialog = new BaseDialog("frog");
                dialog.cont.add("a").row();
                //mod sprites are prefixed with the mod name (this mod is called 'example-java-mod' in its config)
                dialog.cont.image(Core.atlas.find("tekton-frog")).pad(20f).row();
                dialog.cont.button("I see", dialog::hide).size(100f, 50f);
                dialog.show();
            });
        });*/
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
        
        /*Time.runTask(500f, () -> {
        	BaseDialog dialog = new BaseDialog("frog");
        	dialog.cont.add();
        	dialog.cont.button("I see", dialog::hide).size(100f, 50f);
        	dialog.show();
        });*/
        
		contentLoadComplete = true;
    }
}
