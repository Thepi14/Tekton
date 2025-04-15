package tekton.type.gravity;

import java.util.Arrays;

import arc.Core;
import arc.math.*;
import arc.struct.IntSet;
import arc.util.Log;
import arc.util.io.*;
import mindustry.Vars;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.production.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import tekton.content.TektonColor;
import tekton.content.TektonStat;
import tekton.type.draw.DrawGravityOutput;
import tekton.type.gravity.GravityConductor.GravityConductorBuild;

public class GravityProducer extends GenericCrafter{
    public float gravityOutput = 10f;
    public float warmupRate = 0.15f;

    public GravityProducer(String name){
        super(name);
        group = BlockGroup.heat;

        drawer = new DrawMulti(new DrawDefault(), new DrawGravityOutput());
        rotateDraw = false;
        rotate = true;
        canOverdrive = false;
        drawArrow = true;
        replaceable = true;
        //alwaysReplace = true;
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(TektonStat.gravityOutput, gravityOutput, TektonStat.gravityPower);
    }

    @Override
    public void setBars(){
        super.setBars();

        addBar("gravity", (GravityProducerBuild entity) -> new Bar(
        		() -> Core.bundle.format(entity.gravity >= 0 ? "bar.gravity" : "bar.antiGravity", (int)(Math.abs(entity.gravity) + 0.01f)), 
        		() -> entity.gravity >= 0 ? TektonColor.gravityColor : TektonColor.antiGravityColor, 
				() -> entity.gravity / gravityOutput));
    }

    public class GravityProducerBuild extends GenericCrafterBuild implements GravityBlock{
        public float gravity;

        @Override
        public void updateTile(){
            super.updateTile();
            
            //gravity approaches target at the same speed regardless of efficiency
            gravity = Mathf.approachDelta(gravity, gravityOutput * efficiency, warmupRate * delta());
        }

        @Override
        public float gravityFrac(){
            return gravity / gravityOutput;
        }

        @Override
        public float gravity(){
            return gravity;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(gravity);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            gravity = read.f();
        }

		@Override
		public float calculateGravity(float[] sideGravity, IntSet cameFrom) {
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
