package tekton.type.biological;

import java.util.Arrays;

import arc.math.Mathf;
import arc.struct.IntSet;
import arc.util.Nullable;
import mindustry.Vars;
import mindustry.gen.Building;
import tekton.type.biological.Vein.VeinBuild;

public interface BiopowerConsumer {
    float[] sideBiopower();
    float biopowerRequirement();
	float biopower();
    float calculateBiopower(float[] sideBiopower, @Nullable IntSet cameFrom);
	
	public default float calculateBiopower(Building building, float[] sideBiopower, @Nullable IntSet cameFrom) {
        Arrays.fill(sideBiopower, 0f);
        if(cameFrom != null) cameFrom.clear();

        float biopowerAmmount = 0f;

        for(var build : building.proximity) {
            if(build != null && build.team == building.team && build instanceof BiopowerBlock graviter) {

                boolean split = build.block instanceof Vein cond && cond.splitBiopower;
                
                if(!build.block.rotate || (!split && (building.relativeTo(build) + 2) % 4 == build.rotation) || (split && building.relativeTo(build) != build.rotation)) { //TODO hacky

                    if(!(build instanceof VeinBuild hc && hc.cameFrom.contains(building.id()))) {

                        float diff = (Math.min(Math.abs(build.x - building.x), Math.abs(build.y - building.y)) / Vars.tilesize);

                        int contactPoints = Math.min((int)(building.block.size / 2f + build.block.size / 2f - diff), Math.min(build.block.size, building.block.size));

                        float add = graviter.biopower() / build.block.size * contactPoints;
                        if(split) {
                            add /= 3f;
                        }

                        sideBiopower[Mathf.mod(building.relativeTo(build), 4)] += add;
                        biopowerAmmount += add;
                    }
                    
                    if(cameFrom != null) {
                        cameFrom.add(build.id);
                        if(build instanceof VeinBuild gc) {
                            cameFrom.addAll(gc.cameFrom);
                        }
                    }
                    
                    if(graviter instanceof VeinBuild cond) {
                        cond.updateBiopower();
                    }
                }
            }
        }
        return biopowerAmmount;
    }
}
