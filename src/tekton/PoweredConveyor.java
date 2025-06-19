package tekton;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.util.Log;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.meta.BlockStatus;

import static mindustry.Vars.*;

public class PoweredConveyor extends Conveyor {
    private static final float itemSpace = 0.4f;
    private static final int capacity = 3;
    
    public float enhancedSpeed = 0.14f;
    public Color glowColor = Pal.accent;
    public TextureRegion[] glowRegions;

    public float alpha = 0.6f, glowScale = 10f, glowIntensity = 0.5f;
    
    protected boolean changedAlphaState = false;
    protected float currentAlpha = 0f;

	public PoweredConveyor(String name) {
		super(name);
		hasPower = true;
		conductivePower = true;
		consumesPower = true;
	}
	
	@Override
	public void load() {
        super.load();
		glowRegions = new TextureRegion[5];
		for (int i = 0; i < glowRegions.length; i++) {
			glowRegions[i] = Core.atlas.find(name + "-glow-" + i);
		}
	}
	
	public void setAlphaGlowProgress() {
		changedAlphaState = true;
		currentAlpha = (Mathf.absin(Time.time, glowScale, alpha) * glowIntensity + 1f - glowIntensity) * alpha;
	}
	
	public class PoweredConveyorBuild extends ConveyorBuild {
		public float currentSpeed = 1f;
		
		@Override
		public float efficiency() {
			efficiency = 1f;
			return 1f;
		}
		
		@Override
        public BlockStatus status() {
            float balance = power.status;
            if(balance > 0.001f) return BlockStatus.active;
            return BlockStatus.noInput;
        }
		
		@Override
        public void updateTile(){
			efficiency = 1f;
            minitem = 1f;
            mid = 0;
            
            currentSpeed = Mathf.lerp(speed, enhancedSpeed, power.status);

            //skip updates if possible
            if(len == 0 && Mathf.equal(timeScale, 1f)){
                clogHeat = 0f;
                //sleep();
                return;
            }

            float nextMax = aligned ? 1f - Math.max(itemSpace - nextc.minitem, 0) : 1f;
            float moved = currentSpeed * edelta();

            for(int i = len - 1; i >= 0; i--){
                float nextpos = (i == len - 1 ? 100f : ys[i + 1]) - itemSpace;
                float maxmove = Mathf.clamp(nextpos - ys[i], 0, moved);

                ys[i] += maxmove;

                if(ys[i] > nextMax) ys[i] = nextMax;
                if(ys[i] > 0.5 && i > 0) mid = i - 1;
                xs[i] = Mathf.approach(xs[i], 0, moved*2);

                if(ys[i] >= 1f && pass(ids[i])){
                    //align X position if passing forwards
                    if(aligned){
                        nextc.xs[nextc.lastInserted] = xs[i];
                    }
                    //remove last item
                    items.remove(ids[i], len - i);
                    len = Math.min(i, len);
                }else if(ys[i] < minitem){
                    minitem = ys[i];
                }
            }

            if(minitem < itemSpace + (blendbits == 1 ? 0.3f : 0f)){
                clogHeat = Mathf.approachDelta(clogHeat, 1f, 1f / 60f);
            }else{
                clogHeat = 0f;
            }

            changedAlphaState = false;
            //noSleep();
        }
		
		@Override
        public void draw(){
            if (!changedAlphaState)
            	setAlphaGlowProgress();
            int frame = enabled && clogHeat <= 0.5f ? (int)(((Time.time * currentSpeed * 8f * timeScale)) % 4) : 0;
            float z = Draw.z();
            //draw extra conveyors facing this one for non-square tiling purposes
            Draw.z(Layer.blockUnder);
            for(int i = 0; i < 4; i++){
                if((blending & (1 << i)) != 0){
                    int dir = rotation - i;
                    float rot = i == 0 ? rotation * 90 : (dir)*90;

                    Draw.rect(sliced(regions[0][frame], i != 0 ? SliceMode.bottom : SliceMode.top), x + Geometry.d4x(dir) * tilesize*0.75f, y + Geometry.d4y(dir) * tilesize*0.75f, rot);
                }
            }

            Draw.z(Layer.block - 0.2f);

            Draw.rect(regions[blendbits][frame], x, y, tilesize * blendsclx, tilesize * blendscly, rotation * 90);
            Draw.blend(Blending.additive);
            Draw.color(glowColor);
            Draw.alpha(power.status > 0.001f ? currentAlpha : 0f);
        	Draw.rect(glowRegions[blendbits], x, y, tilesize * blendsclx, tilesize * blendscly, rotation * 90);
            Draw.color();
            Draw.blend();
            Draw.z(Layer.block - 0.1f);
            float layer = Layer.block - 0.1f, wwidth = world.unitWidth(), wheight = world.unitHeight(), scaling = 0.01f;

            for(int i = 0; i < len; i++){
                Item item = ids[i];
                Tmp.v1.trns(rotation * 90, tilesize, 0);
                Tmp.v2.trns(rotation * 90, -tilesize / 2f, xs[i] * tilesize / 2f);

                float
                ix = (x + Tmp.v1.x * ys[i] + Tmp.v2.x),
                iy = (y + Tmp.v1.y * ys[i] + Tmp.v2.y);

                //keep draw position deterministic.
                Draw.z(layer + (ix / wwidth + iy / wheight) * scaling);
                Draw.rect(item.fullIcon, ix, iy, itemSize, itemSize);
            }

            Draw.z(z);
            Draw.reset();
        }
		
		@Override
        public void unitOn(Unit unit){
			if(/*!pushUnits || */clogHeat > 0.5f || !enabled) return;

            //noSleep();

            float mspeed = currentSpeed * tilesize * 55f;
            float centerSpeed = 0.1f;
            float centerDstScl = 3f;
            float tx = Geometry.d4x(rotation), ty = Geometry.d4y(rotation);

            float centerx = 0f, centery = 0f;

            if(Math.abs(tx) > Math.abs(ty)){
                centery = Mathf.clamp((y - unit.y()) / centerDstScl, -centerSpeed, centerSpeed);
                if(Math.abs(y - unit.y()) < 1f) centery = 0f;
            }else{
                centerx = Mathf.clamp((x - unit.x()) / centerDstScl, -centerSpeed, centerSpeed);
                if(Math.abs(x - unit.x()) < 1f) centerx = 0f;
            }

            if(len * itemSpace < 0.9f){
                unit.impulse((tx * mspeed + centerx) * delta(), (ty * mspeed + centery) * delta());
            }
        }
	}
}
