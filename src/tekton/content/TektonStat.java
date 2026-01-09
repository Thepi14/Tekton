package tekton.content;

import arc.Core;
import mindustry.gen.Iconc;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;
import mindustry.world.meta.StatUnit;

public class TektonStat {
    public static final StatCat
        none = new StatCat("none")
        ;
    public static final Stat
    	gravityUse = new Stat("gravityuse", StatCat.crafting),
		gravityOutput = new Stat("gravityoutput", StatCat.crafting),
		slowMultiplierFunction = new Stat("speedmultiplier", StatCat.function)
		; 
    public static final StatUnit
    	gravityPower = new StatUnit("gravitypower", "[blue]" + Iconc.waves + "[]")
    	;
}