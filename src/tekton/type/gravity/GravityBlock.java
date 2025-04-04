package tekton.type.gravity;

import arc.struct.IntSet;
import arc.util.Nullable;

public interface GravityBlock {
	float gravity();
    /** @return heat as a fraction of max heat */
    float gravityFrac();
    float calculateGravity(float[] sideGravity, @Nullable IntSet cameFrom);
}