package tekton.content;

import static tekton.content.TektonItems.*;

import arc.graphics.Color;
import mindustry.content.Items;
import mindustry.graphics.Pal;

public class TektonColor {
	public static Color 
	
	//liquids
	ammonia = Color.valueOf("e6cd43"),
	liquidMethane = Color.valueOf("515423"),
	methane = Color.valueOf("868a37"),
	oxygen = Color.valueOf("b48fff"),
	metazotoplasm = Color.valueOf("aaccaa"),
	zirconiumSpark = Color.valueOf("fdff8c"),
	
	tektonOutlineColor = Color.valueOf("3d352f"),
	
	gravityColor = Color.valueOf("639bff"),
	antiGravityColor = Color.valueOf("d95763"),
	
	neurospora = Color.valueOf("46617d"),
	acid = Color.valueOf("82d629"),
	cobweb = Color.valueOf("d6d6d6"),
	
	commonShootColor = Color.valueOf("fff6d4"),
	siliconShootColor = Pal.techBlue.cpy(),
	zirconiumShootColor,
	tantalumShootColor,
	polycarbonateShootColor,
	polytalumShootColor,
	magnetShootColor,
	cryogenicCompoundShootColor,
	uraniumShootColor,
	phaseFabricShootColor,
	nanoAlloyShootColor,
	redShootColor = Color.valueOf("ff4545"),
	redShootColorLightning = Color.valueOf("ff5959")
	;
	
	public static void load() { //waaaa
		zirconiumShootColor = TektonColor.zirconiumSpark.cpy();
		tantalumShootColor = tantalum.color.cpy().lerp(Color.white, 0.5f);
		polycarbonateShootColor = polycarbonate.color.cpy().lerp(Color.white, 0.1f);
		polytalumShootColor = polytalum.color.cpy().lerp(Color.white, 0.1f);
		magnetShootColor = magnet.color.cpy().lerp(Color.white, 0.2f);
		cryogenicCompoundShootColor = cryogenicCompound.color.cpy().lerp(Color.white, 0.2f);
		uraniumShootColor = uranium.color.cpy().lerp(TektonStatusEffects.radioactiveContamination.color, 0.4f).mul(1.2f);
		phaseFabricShootColor = Items.phaseFabric.color.cpy().lerp(Pal.thoriumPink, 0.1f);
		nanoAlloyShootColor = Pal.lightOrange;
	}
}
