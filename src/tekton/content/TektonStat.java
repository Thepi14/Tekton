package tekton.content;

import arc.Core;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;

public class TektonStat {
    public static final Stat
    	gravityUse = new Stat("gravityuse", StatCat.crafting), 
		gravityOutput = new Stat("gravityoutput", StatCat.crafting), 
		speedMultiplierFunction = new Stat("speedmultiplier", StatCat.function), 
		
		biologicalOrigin = new Stat("biologicalorigin", TektonStatCat.information), 
		lifeExpectancy = new Stat("lifeexpectancy", TektonStatCat.information), 
		maturationTime = new Stat("maturationtime", TektonStatCat.information), 
		maximumLifeSpan = new Stat("maximumlifespan", TektonStatCat.information)
		;
	
	public static final String naturalMarkup = "[#54d67d]", adaptedMarkup = "[#f25555]", modifiedMarkup = "[stat]", articialMarkup = "[#6c87fd]", unknownMarkup = "[violet]";
	public static final String[] markups = { naturalMarkup, adaptedMarkup, modifiedMarkup, articialMarkup, unknownMarkup };
	
	public static enum BiologicalOrigin {
		natural,
		adapted,
		modified,
		artificial,
		unknown
	};
	
	public static String GetUnitOriginString(BiologicalOrigin origin) {
		return markups[origin.ordinal()] + Core.bundle.get("origin." + origin.name()) + "[]";
	}
}