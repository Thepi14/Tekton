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
import mindustry.world.meta.BlockGroup;
import tekton.content.TektonColor;
import tekton.content.TektonVars;
import tekton.type.draw.DrawGravityInput;
import tekton.type.draw.DrawGravityOutput;
import tekton.type.gravity.GravitationalTurret.GravitationalTurretBuild;
import tekton.type.gravity.GravityConductor.GravityConductorBuild;

public class GravityConductor extends Block {
	public float visualMaxHeat = 15f;
    public DrawBlock drawer = new DrawDefault();
    public boolean splitGravity = false;

    public GravityConductor(String name){
        super(name);
        group = BlockGroup.heat;
        
        update = solid = rotate = true;
        rotateDraw = false;
        drawArrow = true;
        replaceable = true;
        //alwaysReplace = true;
        size = 2;
        
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
            return calculateGravity(this, sideGravity, cameFrom);
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

		@Override
		public float calculateGravity(Building building, float[] sideGravity, IntSet cameFrom) {
			return GravityBlock.super.calculateGravity(building, sideGravity, cameFrom);
		}
    }
}
