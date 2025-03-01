package tekton;

import arc.*;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.struct.Seq;
import arc.util.Nullable;
import arc.util.Tmp;
import mindustry.entities.part.DrawPart;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.type.Weapon;
import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.graphics.*;

public class FramePart extends DrawPart{
    /** Appended to unit/weapon/block name and drawn. */
    public String suffix = "";
    /** Overrides suffix if set. */
    public @Nullable String name;
    /** Progress function for determining position/rotation. */
    public PartProgress progress = PartProgress.warmup;
    /** Progress function for heat alpha. */
    public PartProgress heatProgress = PartProgress.heat;
    /** If true, the base + outline regions are drawn. Set to false for heat-only regions. */
    public boolean drawRegion = true;
    /** If true, an outline is drawn under the part. */
    public boolean outline = true;
    /**Heat Color**/
    public Color heatColor = Pal.turretHeat;
    public float x, y, rotation;
    /** Number of frames to draw. */
    public int frames = 3;
    /** Ticks between frames. */
    public float interval = 5f;
    /** If it's a looped animation. */
    public boolean loop = true;
    /** If the framed part is always passing frames, only works with loop. */
    public boolean alwaysAnimate = false;
    public TextureRegion heat;
    public @Nullable String outlineRegionSuffix = null;
    public TextureRegion outlineRegion;
    public TextureRegion[] regions;
    public TextureRegion[] outlines = {};
    
    public float layer = -1, layerOffset = 0f, heatLayerOffset = 1f, turretHeatLayer = Layer.turretHeat;
    public float outlineLayerOffset = -0.001f;

    public FramePart() {
        outline = false;
    }
    
    public FramePart(String region) {
        this.suffix = region;
        outline = false;
    }

    @Override
    public void draw(PartParams params){
    	float prog = progress.getClamp(params);
        float hprog = heatProgress.getClamp(params);

        float counter = ((1f / interval) * Time.time);
        int currentFrame = loop ? (int)(counter % frames) : (int)(frames * prog);
    	currentFrame = currentFrame >= regions.length ? regions.length - 1 : currentFrame;
        var currentRegion = regions[(prog >= 0.01f || (alwaysAnimate && loop) ? currentFrame : 0)];
        
    	float z = Draw.z();
        float prevZ = Draw.z();
    	if(layer > 0) Draw.z(layer);
        //TODO 'under' should not be special cased like this...
        if(under && turretShading) Draw.z(z - 0.0001f);
        Draw.z(Draw.z() + layerOffset);
        
        var rot = params.rotation + rotation - 90;
        var rx = params.x + x;
        var ry = params.y + y;
        
        Draw.rect(currentRegion, rx, ry, rot);
        Draw.blend();
        
        if(heat.found()){
            heatColor.write(Tmp.c1).a(hprog * heatColor.a);
            Drawf.additive(heat, Tmp.c1, rx, ry, rot, turretShading ? turretHeatLayer : Draw.z() + heatLayerOffset);
        }
        
        if(outline && drawRegion){
            Draw.z(prevZ + outlineLayerOffset);
            if (outlineRegion == null) {
                Draw.rect(outlines[Math.min(currentFrame, regions.length - 1)], rx, ry, rot);
            }
            else {
            	Draw.rect(outlineRegion, rx, ry, rot);
            }
            Draw.z(prevZ);
        }
        
        Draw.reset();
        
        Draw.z(z);
    }

    @Override
    public void load(String name){
        String realName = this.name == null ? name + suffix : this.name;
		regions = new TextureRegion[frames];
        outlines = new TextureRegion[frames];
        if(drawRegion){
            for (int i = 1; i <= frames; i++) {
            	regions[i - 1] = Core.atlas.find(realName + i);
            }
        }
        if (outlineRegionSuffix != null) {
        	outlineRegion = Core.atlas.find(realName + outlineRegionSuffix);
        }
        else if (outline) {
        	for (int i = 1; i <= frames; i++) {
        		outlines[i - 1] = Core.atlas.find(realName + "-outline" + i);
            }
        }
        heat = Core.atlas.find(realName + "-heat");
    }
    
    @Override
    public void getOutlines(Seq<TextureRegion> out){
        if(outline && drawRegion){
            out.addAll(regions);
        }
    }
}
