package tekton;

import java.lang.reflect.Field;

import arc.Events;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.EventType.WorldLoadEvent;
import mindustry.game.Team;
import mindustry.graphics.MultiPacker;
import mindustry.mod.Mod;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.PlanetDialog;
import mindustry.world.Block;
import mindustry.world.meta.Env;
import tekton.content.TektonBlocks;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonItems;
import tekton.content.TektonLiquids;
import tekton.content.TektonLoadouts;
import tekton.content.TektonPlanets;
import tekton.content.TektonSectors;
import tekton.content.TektonSounds;
import tekton.content.TektonStatusEffects;
import tekton.content.TektonTechTree;
import tekton.content.TektonUnits;
import tekton.content.TektonVars;
import tekton.content.TektonWeathers;
import tekton.type.world.TektonEnv;
import tekton.type.world.TektonEnvRenderer;
import tekton.ui.TektonSettings;

public class Tekton extends Mod {

    public static String ID = "tekton";
	protected static boolean contentLoadComplete = false;

	public static final String MOD_RELEASES = "https://github.com/Thepi14/Tekton/releases";
	public static final String MOD_REPO = "Thepi14/Tekton";
	public static final String MOD_GITHUB_URL = "https://github.com/Thepi14/Tekton.git";
	public static final String MOD_NAME = "tekton";

	public static boolean hideContent = true;
	public static boolean drawBiologicalUnitsCell = false; //only made because the cell drawing system of mindustry is bugged (or is it? vsauce theme plays).
	public static boolean showDebug = false;

	public static short version = 157;

	public static MultiPacker packer;

	public static final boolean loadedComplete() {
		return contentLoadComplete;
	}

	public static String name(String name) {
		return MOD_NAME + "-" + name;
	}

	public Tekton() {
		Log.info("Loaded Tekton constructor.");
        packer = new MultiPacker();

		//the gambiarra must continue
		Events.on(WorldLoadEvent.class, e -> {
			TektonVars.clearAllLists();
		});
	}

	@Override
    public void init() {
        super.init();
        packer = new MultiPacker();
        TektonSettings.load();

    	PlanetDialog.debugSelect = !hideContent;
    }

    @Override
    public void loadContent() {
		contentLoadComplete = false;

        Time.mark();
        TektonSounds.load();
        TektonStatusEffects.load();
        TektonItems.load();
        TektonLiquids.load();
        //TektonBullets.load();
        TektonColor.load();
        TektonUnits.load();
        TektonBlocks.load();
        TektonWeathers.load();
        TektonPlanets.load();
        TektonSectors.load();
        TektonTechTree.load();
        TektonFx.load();
        TektonLoadouts.load();
        TektonEnvRenderer.load();

        Events.on(EventType.WorldLoadEndEvent.class, e -> {
        	//remove old fog
        	Vars.state.rules.weather.remove(t -> (t.weather == TektonWeathers.tektonFog));
        	/*if (Vars.state.rules.planet == TektonPlanets.tekton) {
        		Vars.state.rules.env = TektonEnv.methane | Env.terrestrial;
        	}*/
        });

        //Team.blue.emoji = "tekton-team-hapax";
        //Team.blue.name = "hapax";

        Log.info("Tekton loaded, non-hidden content: " + returnResourcesSize());

		contentLoadComplete = true;
    }

	public static int returnResourcesSize() {
		int environment = 55;
		int hidden = 5;
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
