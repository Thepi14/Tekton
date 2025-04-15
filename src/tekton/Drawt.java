package tekton;

import arc.graphics.Color;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;

public final class Drawt {
	
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
}
