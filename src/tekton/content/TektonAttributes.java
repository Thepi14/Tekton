package tekton.content;

import arc.struct.*;
import mindustry.*;
import mindustry.world.meta.Attribute;

public class TektonAttributes {
	public static final Attribute 
	/** Methane content. Used for methane pump yield. */
	methane = Attribute.add("methane"), 
	/** Silica content. Used for silica fans */
	silica = Attribute.add("silica");
	;
}
