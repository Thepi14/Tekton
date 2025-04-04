package tekton.type.gravity;

import java.util.Arrays;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import tekton.content.TektonColor;
import tekton.content.TektonVars;
import tekton.type.gravity.GravitationalTurret.GravitationalTurretBuild;
import tekton.type.gravity.GravityConductor.GravityConductorBuild;

public class GravityConductor extends Block {
	public float visualMaxHeat = 15f;
    public DrawBlock drawer = new DrawDefault();
    public boolean splitGravity = false;

    public GravityConductor(String name){
        super(name);
        update = solid = rotate = true;
        rotateDraw = false;
        size = 3;
        
        drawer = new DrawMulti(new DrawDefault(), new DrawGravityOutput(), new DrawGravityInput());
    }

    @Override
    public void setBars(){
        super.setBars();

        //TODO show number
        addBar("gravity", (GravityConductorBuild entity) -> new Bar(
        		() -> Core.bundle.format(entity.gravity >= 0 ? "bar.gravity" : "bar.antiGravity", (int)(Math.abs(entity.gravity) + 0.01f)), 
        		() -> entity.gravity >= 0 ? TektonColor.gravityColor : TektonColor.antiGravityColor, 
        		() -> entity.gravity / TektonVars.visualMaxGravity));
    }

    @Override
    public void load(){
        super.load();
        
        drawer.load(this);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public TextureRegion[] icons(){
        return drawer.finalIcons(this);
    }

    public class GravityConductorBuild extends Building implements GravityBlock, GravityConsumer{
        public float gravity = 0f;
        public float[] sideGravity = new float[4];
        public IntSet cameFrom = new IntSet();
        public long lastGravityUpdate = -1;

        @Override
        public void draw(){
            drawer.draw(this);
        }

        @Override
        public void drawLight(){
            super.drawLight();
            drawer.drawLight(this);
        }

        @Override
        public float[] sideGravity(){
            return sideGravity;
        }

        @Override
        public float gravityRequirement(){
            return TektonVars.visualMaxGravity;
        }

        @Override
        public void updateTile(){
        	updateGravity();
        }

        public void updateGravity(){
            if(lastGravityUpdate == Vars.state.updateId) return;

            lastGravityUpdate = Vars.state.updateId;
            gravity = calculateGravity(sideGravity, cameFrom);
        }

        @Override
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

        @Override
        public float warmup(){
            return gravity;
        }

        @Override
        public float gravity(){
            return gravity;
        }

        @Override
        public float gravityFrac(){
            return (gravity / TektonVars.visualMaxGravity) / (splitGravity ? 3f : 1);
        }
    }
}
