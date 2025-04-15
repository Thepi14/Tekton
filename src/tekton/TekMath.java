package tekton;

import arc.math.Mathf;
import arc.util.Log;

public final class TekMath {
	public static boolean insideDiamond(float x, float y, float dx, float dy, float size) {
		var tx = (x - dx);
		var ty = (y - dy);
		
		tx = tx < 0f ? tx * -1f : tx;
		ty = ty < 0f ? ty * -1f : ty;
		
		return tx + ty <= size;
	}
}
