package tekton.type.gravity;

import java.util.Arrays;

import arc.math.Mathf;
import arc.struct.IntSet;
import arc.util.Nullable;
import mindustry.Vars;
import mindustry.gen.Building;
import tekton.type.gravity.GravityConductor.GravityConductorBuild;

public interface GravityConsumer {
    float[] sideGravity();
    float gravityRequirement();
	float gravity();
    float calculateGravity(float[] sideGravity, @Nullable IntSet cameFrom);
	
	public default float calculateGravity(Building building, float[] sideGravity, @Nullable IntSet cameFrom) {
        Arrays.fill(sideGravity, 0f);
        if(cameFrom != null) cameFrom.clear();

        float gravityAmmount = 0f;

        for(var build : building.proximity) {
            if(build != null && build.team == building.team && build instanceof GravityBlock graviter) {

                boolean split = build.block instanceof GravityConductor cond && cond.splitGravity;
                
                if(!build.block.rotate || (!split && (building.relativeTo(build) + 2) % 4 == build.rotation) || (split && building.relativeTo(build) != build.rotation)) { //TODO hacky

                    if(!(build instanceof GravityConductorBuild hc && hc.cameFrom.contains(building.id()))) {

                        float diff = (Math.min(Math.abs(build.x - building.x), Math.abs(build.y - building.y)) / Vars.tilesize);

                        int contactPoints = Math.min((int)(building.block.size / 2f + build.block.size / 2f - diff), Math.min(build.block.size, building.block.size));

                        float add = graviter.gravity() / build.block.size * contactPoints;
                        if(split) {
                            add /= 3f;
                        }

                        sideGravity[Mathf.mod(building.relativeTo(build), 4)] += add;
                        gravityAmmount += add;
                    }
                    
                    if(cameFrom != null) {
                        cameFrom.add(build.id);
                        if(build instanceof GravityConductorBuild gc) {
                            cameFrom.addAll(gc.cameFrom);
                        }
                    }
                    
                    if(graviter instanceof GravityConductorBuild cond) {
                        cond.updateGravity();
                    }
                }
            }
        }
        return gravityAmmount;
    }
}
