package tekton.math;

import arc.math.Mathf;
import arc.math.geom.Vec2;

public final class TekMath {
	public static boolean insideDiamond(float x, float y, float dx, float dy, float size) {
		var tx = (x - dx);
		var ty = (y - dy);

		tx = tx < 0f ? tx * -1f : tx;
		ty = ty < 0f ? ty * -1f : ty;

		return tx + ty <= size;
	}

	public static float insideDiamondDst(float x, float y, float dx, float dy) {
		var tx = (x - dx);
		var ty = (y - dy);

		tx = tx < 0f ? tx * -1f : tx;
		ty = ty < 0f ? ty * -1f : ty;

		return (tx + ty);
	}

	public static int pow2(int number) {
		return Mathf.pow(number, 2);
	}

    public static Vec2 nearestPointOnFiniteLine(Vec2 start, Vec2 end, Vec2 pnt)
    {
        var line = new Vec2(end.x - start.x, end.y - start.y);
        float len = line.x * line.x + line.y * line.y;
        
        line = line.nor();

        var v = new Vec2(pnt.x - start.x, pnt.y - start.y);
        float d = v.dot(line);
        d = Mathf.clamp(d, 0f, len);
        return new Vec2(start.x + line.x * d, start.y + line.y * d);
    }
}
