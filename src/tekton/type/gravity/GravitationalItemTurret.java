package tekton.type.gravity;

import java.util.Arrays;

import arc.Core;
import arc.math.Mathf;
import arc.struct.IntSet;
import arc.util.Nullable;
import mindustry.Vars;
import mindustry.ui.Bar;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tekton.content.TektonColor;
import tekton.content.TektonStat;
import tekton.type.gravity.GravitationalTurret.GravitationalTurretBuild;
import tekton.type.gravity.GravityConductor.GravityConductorBuild;

public class GravitationalItemTurret extends ItemTurret {
	public int minGravity = 8;
	public int maxGravity = 16;

	public GravitationalItemTurret(String name) {
		super(name);
	}
	
	@Override
    public void setBars() {
        super.setBars();
        addBar("gravity", (GravitationalItemTurretBuild entity) -> new Bar(
        		() -> Core.bundle.format("bar.gravityPercent", (int)(Math.abs(entity.gravity) + 0.01f), (float)(entity.gravityFrac() * 100)), 
        		() -> TektonColor.gravityColor, 
				() -> ((float)Math.abs(entity.gravity)) / (float)minGravity));
    }
	
	@Override
    public void setStats(){
        super.setStats();
        
        stats.add(Stat.maxEfficiency, (maxGravity / minGravity) * 100f, StatUnit.percent);
        stats.add(TektonStat.gravityUse, minGravity, TektonStat.gravityPower);

        //stats.remove(Stat.reload);
        stats.remove(Stat.inaccuracy);
    }
	
	public class GravitationalItemTurretBuild extends ItemTurretBuild implements GravityConsumer {
		public int gravity = 0;
        public float[] sideGravity = new float[4];
        
        @Override
        public void updateTile(){
            super.updateTile();
            gravity = (int)calculateGravity(sideGravity);
        }
        
        @Override
        public float warmup(){
            return super.warmup() * Mathf.clamp(gravityFrac());
        }

        @Override
        public float efficiency(){
        	return super.efficiency() * gravityFrac();
        }
        
        @Override
        public boolean hasAmmo(){
            return super.hasAmmo() && gravity >= minGravity;
        }

        @Override
        public float[] sideGravity(){
            return sideGravity;
        }

        @Override
        public float gravityRequirement(){
            return minGravity;
        }
        
        public float gravityFrac() {
        	return ((float)gravity() / (float)minGravity);
        }

		@Override
		public float gravity() {
			return Math.min(gravity, maxGravity);
		}
        
        public float calculateGravity(float[] sideGravity){
            return calculateGravity(sideGravity, null);
        }
		
		public float calculateGravity(float[] sideGravity, @Nullable IntSet cameFrom){
            Arrays.fill(sideGravity, 0f);
            if(cameFrom != null) cameFrom.clear();

            float gravityAmmount = 0f;

            for(var build : proximity){
                if(build != null && build.team == team && build instanceof GravityBlock graviter){

                    boolean split = build.block instanceof GravityConductor cond && cond.splitGravity;
                    // non-routers must face us, routers must face away - next to a redirector, they're forced to face away due to cycles anyway
                    if(!build.block.rotate || (!split && (relativeTo(build) + 2) % 4 == build.rotation) || (split && relativeTo(build) != build.rotation)){ //TODO hacky

                        //if there's a cycle, ignore its gravity
                        if(!(build instanceof GravityConductorBuild hc && hc.cameFrom.contains(id()))){
                            //x/y coordinate difference across point of contact
                            float diff = (Math.min(Math.abs(build.x - x), Math.abs(build.y - y)) / Vars.tilesize);
                            //number of points that this block had contact with
                            int contactPoints = Math.min((int)(block.size/2f + build.block.size/2f - diff), Math.min(build.block.size, block.size));

                            //gravity is distributed across building size
                            float add = graviter.gravity() / build.block.size * contactPoints;
                            if(split){
                                //gravity routers split gravity across 3 surfaces
                                add /= 3f;
                            }

                            sideGravity[Mathf.mod(relativeTo(build), 4)] += add;
                            gravityAmmount += add;
                        }

                        //register traversed cycles
                        if(cameFrom != null){
                            cameFrom.add(build.id);
                            if(build instanceof GravityConductorBuild gc){
                                cameFrom.addAll(gc.cameFrom);
                            }
                        }

                        //massive hack but I don't really care anymore
                        if(graviter instanceof GravityConductorBuild cond){
                            cond.updateGravity();
                        }
                    }
                }
            }
            return gravityAmmount;
        }
	}
}
