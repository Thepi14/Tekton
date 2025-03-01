package tekton;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.power.PowerGenerator.GeneratorBuild;
import mindustry.world.draw.DrawBlock;
import tekton.TektonNuclearReactor.TektonNuclearReactorBuild;

public class DrawTurbine extends DrawBlock{
    public TextureRegion region;
    public String suffix = "";
    public boolean drawPlan = true;
    public boolean buildingRotate = false;
    public float rotateSpeed, x, y, rotation;
    /** Any number <=0 disables layer changes. */
    public float layer = -1;

    public DrawTurbine(String suffix){
        this.suffix = suffix;
    }

    public DrawTurbine(String suffix, float rotateSpeed){
        this.suffix = suffix;
        this.rotateSpeed = rotateSpeed;
    }

    public DrawTurbine(){
    }

    @Override
    public void draw(Building build){
    	if (!(build instanceof GeneratorBuild || build instanceof TektonNuclearReactorBuild))
    		return;
        float z = Draw.z();
        if(layer > 0) Draw.z(layer);
        Drawf.spinSprite(region, build.x + x, build.y + y, ((GeneratorBuild)build).productionEfficiency * rotateSpeed + rotation + (buildingRotate ? build.rotdeg() : 0));
        Draw.z(z);
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list){
        if(!drawPlan) return;
        Draw.rect(region, plan.drawx(), plan.drawy(), (buildingRotate ? plan.rotation * 90f : 0));
    }

    @Override
    public TextureRegion[] icons(Block block){
        return new TextureRegion[]{region};
    }

    @Override
    public void load(Block block){
        region = Core.atlas.find(block.name + suffix);
    }
}
