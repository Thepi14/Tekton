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
    public int gravityOutput = 10;
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
        		() -> TektonColor.gravityColor, 
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
			return calculateGravity(this, sideGravity, cameFrom);
		}
    }
}
