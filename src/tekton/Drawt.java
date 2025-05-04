package tekton;

import static mindustry.Vars.*;
import static mindustry.entities.Effect.*;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Log;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import tekton.content.TektonColor;
import static tekton.content.TektonFx.*;

public final class Drawt {
	
	public static void diamond(float x, float y, float radius) {
        Drawf.line(Pal.accent, x, y + radius, x + radius, y);
        Drawf.line(Pal.accent, x + radius, y, x, y - radius);
        Drawf.line(Pal.accent, x, y - radius, x - radius, y);
        Drawf.line(Pal.accent, x - radius, y, x, y + radius);
	}
	
	public static void diamond(Color color, float x, float y, float radius) {
        Drawf.line(color, x, y + radius, x + radius, y);
        Drawf.line(color, x + radius, y, x, y - radius);
        Drawf.line(color, x, y - radius, x - radius, y);
        Drawf.line(color, x - radius, y, x, y + radius);
	}
	
	public static void dashDiamond(float x, float y, float radius) {
        Drawf.dashLine(Pal.accent, x, y + radius, x + radius, y);
        Drawf.dashLine(Pal.accent, x + radius, y, x, y - radius);
        Drawf.dashLine(Pal.accent, x, y - radius, x - radius, y);
        Drawf.dashLine(Pal.accent, x - radius, y, x, y + radius);
	}
	
	public static void dashDiamond(Color color, float x, float y, float radius) {
        Drawf.dashLine(color, x, y + radius, x + radius, y);
        Drawf.dashLine(color, x + radius, y, x, y - radius);
        Drawf.dashLine(color, x, y - radius, x - radius, y);
        Drawf.dashLine(color, x - radius, y, x, y + radius);
	}
	
	public static void DrawAcidDebris(float x, float y, float rotation, int size) {
		biologicalDebris.at(x, y, rotation, size);
	}
}
