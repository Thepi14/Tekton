package tekton;

import static tekton.content.TektonFx.colorDebris;

import arc.graphics.Color;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import tekton.content.TektonColor;

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

	public static void DrawAcidDebris(float x, float y, int size) {
		DrawColorDebris(x, y, size, TektonColor.acid.cpy());
	}

	public static void DrawAmmoniaDebris(float x, float y, int size) {
		DrawColorDebris(x, y, size, TektonColor.ammonia.cpy());
	}

	public static void DrawColorDebris(float x, float y, int size, Color color) {
		colorDebris.at(x, y, size, color);
	}
}
