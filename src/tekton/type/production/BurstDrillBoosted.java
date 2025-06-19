package tekton.type.production;

import arc.Core;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.production.BurstDrill;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import tekton.content.TektonLiquids;

//TODO Eliminate this class on the Build 149, or not
public class BurstDrillBoosted extends BurstDrill {
	public TextureRegion topBoostRegion;
    public float alpha = 0.9f, glowScale = 10f, glowIntensity = 0.5f;
    public Liquid boostLiquid = Liquids.water;
    public float boostConsumptionAmount = 2f / 60f;
	
    public BurstDrillBoosted(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
    
    @Override
    public void load() {
    	super.load();
    	topBoostRegion = Core.atlas.find(name + "-boost");
    	consumeLiquid(boostLiquid, boostConsumptionAmount).boost();
    }

	public class BurstDrillBoostedBuild extends BurstDrillBuild {
        //used so the lights don't fade out immediately
        public float smoothProgress = 0f;
        public float invertTime = 0f;

        @Override
        public void updateTile() {
        	
            if(dominantItem == null){
                return;
            }

            if(invertTime > 0f) invertTime -= delta() / invertedTime;

            if(timer(timerDump, dumpTime)){
                dump(items.has(dominantItem) ? dominantItem : null);
            }

            float drillTime = getDrillTime(dominantItem);

            smoothProgress = Mathf.lerpDelta(smoothProgress, progress / (drillTime - 20f), 0.1f);

            if(items.total() <= itemCapacity - dominantItems && dominantItems > 0 && efficiency > 0){
                warmup = Mathf.approachDelta(warmup, progress / drillTime, 0.01f);

                float speed = Mathf.lerp(1f, liquidBoostIntensity, optionalEfficiency) * efficiency;

                timeDrilled += speedCurve.apply(progress / drillTime) * speed;

                lastDrillSpeed = 1f / drillTime * speed * dominantItems;
                progress += delta() * speed;
            }else{
                warmup = Mathf.approachDelta(warmup, 0f, 0.01f);
                lastDrillSpeed = 0f;
                return;
            }

            if(dominantItems > 0 && progress >= drillTime && items.total() < itemCapacity){
                for(int i = 0; i < dominantItems; i++){
                    offload(dominantItem);
                }

                invertTime = 1f;
                progress %= drillTime;

                if(wasVisible){
                    Effect.shake(shake, shake, this);
                    drillSound.at(x, y, 1f + Mathf.range(drillSoundPitchRand), drillSoundVolume);
                    drillEffect.at(x + Mathf.range(drillEffectRnd), y + Mathf.range(drillEffectRnd), dominantItem.color);
                }
            }
        }

        @Override
        public void draw(){
            super.draw();
            if (!topBoostRegion.found())
            	return;
            Draw.blend(Blending.additive);
            Draw.color(heatColor);
            Draw.alpha((Mathf.absin(totalProgress(), glowScale, alpha) * glowIntensity + 1f - glowIntensity) * alpha * liquids.get(boostLiquid));
            Draw.rect(topBoostRegion, x, y);
            Draw.color();
            Draw.alpha(1f);
            Draw.blend();
            Draw.reset();
        }
    }
}