package tekton.type.gravity;

import arc.struct.IntSet;
import arc.util.Nullable;

public interface GravityBlock {
	float gravity();
    /** @return gravity as a fraction of max gravity */
    float gravityFrac();
    float calculateGravity(float[] sideGravity, @Nullable IntSet cameFrom);
}