package tekton;

import java.lang.reflect.Field;
import java.util.Arrays;

import arc.Core;
import arc.Events;
import arc.math.Mathf;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.mod.Mod;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import tekton.content.*;
import tekton.type.gravity.GravityBlock;
import tekton.type.gravity.GravityConductor;
import tekton.type.gravity.GravityConductor.GravityConductorBuild;
import ent.anno.Annotations.*;
import tekton.EntityDefinitions;
import mindustry.type.*;

public class Tekton extends Mod{
	
	protected static boolean contentLoadComplete = false;
	
	public static final String MOD_RELEASES = "https://github.com/Thepi14/Tekton/releases";
	public static final String MOD_REPO = "Thepi14/Tekton";
	public static final String MOD_GITHUB_URL = "https://github.com/Thepi14/Tekton.git";
	public static final String MOD_NAME = "tekton";
	
	public static boolean hideContent = false;

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
            Time.runTask(5f * 60f, () -> {
                BaseDialog dialog = new BaseDialog("frog");
                var text = "";
                for (var item : TektonPlanets.tekton.defaultCore.requirements) {
                	text += item.item.name;
                	text += item.amount;
                }
                dialog.cont.add(text).row();
                //mod sprites are prefixed with the mod name (this mod is called 'example-java-mod' in its config)
                dialog.cont.image(Core.atlas.find("example-java-mod-frog")).pad(20f).row();
                dialog.cont.button("I see", dialog::hide).size(100f, 50f);
                dialog.show();
            });
        });*/
	}
	
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
        TektonLoadouts.load();
        
        Team.blue.emoji = "tekton-hapax";
        Team.blue.name = "hapax";
        
        Log.info("Tekton loaded, non-hidden content: " + returnResourcesSize());
        
		contentLoadComplete = true;
    }
	
	public static int returnResourcesSize() {
		int environment = 46;
		int hidden = 6;
		int currentTests = environment + hidden;
		Class<?> bloc = TektonBlocks.class;
		Seq<Field> blocFields = new Seq<>(bloc.getFields());
		blocFields.retainAll(f -> Block.class.equals(f.getType()));
		
		Class<?> uni = TektonUnits.class;
		Seq<Field> uniFields = new Seq<>(uni.getFields());
		uniFields.retainAll(f -> UnitType.class.equals(f.getType()));

		Class<?> ite = TektonItems.class;
		Seq<Field> iteFields = new Seq<>(ite.getFields());
		iteFields.retainAll(f -> Item.class.equals(f.getType()));

		Class<?> liq = TektonLiquids.class;
		Seq<Field> liqFields = new Seq<>(liq.getFields());
		liqFields.retainAll(f -> Liquid.class.equals(f.getType()));
		
		return blocFields.size + uniFields.size + iteFields.size + liqFields.size - currentTests;
	}
}
