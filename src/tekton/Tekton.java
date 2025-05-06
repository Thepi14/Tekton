package tekton;

import java.lang.reflect.Field;

import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import mindustry.game.Team;
import mindustry.mod.Mod;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.type.UnitType;
import mindustry.world.Block;
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
        
        Log.info(returnResourcesSize());
        
		contentLoadComplete = true;
    }
	
	public static int returnResourcesSize() {
		int currentTests = 2;
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
