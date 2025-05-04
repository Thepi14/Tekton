package tekton.type.biological;

import static arc.graphics.g2d.Draw.color;
import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.core.Renderer;
import mindustry.entities.Effect;
import mindustry.entities.Puddles;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.Tile;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.blocks.power.LightBlock.LightBuild;
import tekton.Drawt;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonLiquids;
import tekton.type.biological.Nest.NestBuild;
import tekton.type.defense.TeamLight;

public class GlowPod extends TeamLight implements BiologicalBlock {
    public Color damageColor = TektonLiquids.acid.color.cpy();
    
    public float alpha = 0.9f, glowScale = 15f, glowIntensity = 0.5f, shadowAlpha = 0.2f;
    public float growScale = 20f, growIntensity = 0.2f;
    public Color glowColor = TektonLiquids.acid.color.cpy();
    public float glowMag = 0.6f, glowScl = 8f;
	
	public float regenReload = 100f;
	public float healPercent = 1f;
	public Color regenColor = Pal.heal;
	public Effect regenEffect = TektonFx.buildingBiologicalRegeneration.wrap(regenColor);
    
    public boolean drawBase = true;
    public float nestShadowOffset = 3f;
    
    public String basePrefix = "nest-";
    public TextureRegion baseRegion;
    public TextureRegion lightRegion;
    public TextureRegion glowRegion;
    public TextureRegion upperShadowRegion;

	public GlowPod(String name) {
		super(name);
		outlineIcon = true;
        outlineColor = TektonColor.tektonOutlineColor;
        lightColor = TektonColor.acid;
		drawTeamOverlay = false;
		createRubble = drawCracks = false;
		destroyEffect = TektonFx.biologicalDynamicExplosion;
	}
	
	@Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{baseRegion, region};
    }
	
	@Override
	public void load() {
		super.load();

        baseRegion = Core.atlas.find("tekton-" + basePrefix + "block-" + size);
		glowRegion = Core.atlas.find(name + "-glow");
		lightRegion = Core.atlas.find(name + "-light");
        upperShadowRegion = Core.atlas.find(name + "-upper-shadow");
	}
	
	public class GlowPodBuild extends TeamLightBuild {
		public boolean needRegen = false;
		public float regenCharge = Mathf.random(regenReload);
		public float totalProgress = Mathf.random(1000f);

		@Override
		public void updateTile() {
			super.updateTile();

            totalProgress += Time.delta * timeScale * efficiency;
			needRegen = damaged() && efficiency > 0.1f;
			
			if (needRegen)
				regenCharge += Time.delta * efficiency;
			
			if (regenCharge >= regenReload && needRegen) {
				regenCharge = 0f;
				heal(maxHealth() * (healPercent) / 100f);
				recentlyHealed();
				regenEffect.at(x + Mathf.range(block.size * tilesize/2f - 1f), y + Mathf.range(block.size * tilesize/2f - 1f));
			}
		}
		
		@Override
        public void draw() {
        	float layer = Layer.blockAdditive;
            float z = Draw.z();
            
            float 
            xsize = currentGrow() * region.width * region.scl(),
    		ysize = currentGrow() * region.height * region.scl(),
    		rot = Mathf.sin(Time.time + x, 50f, 0.5f) + Mathf.sin(Time.time - y, 65f, 0.9f) + Mathf.sin(Time.time + y - x, 85f, 0.9f);
            
			Color col = damageColor.cpy().lerp(Color.white, health / maxHealth);
            
            if (baseRegion.found() && drawBase) {
            	Draw.z(Layer.block - 0.011f);
                Draw.color(col);
                Draw.alpha(1f);
                Draw.rect(baseRegion, x, y, baseRegion.width * region.scl(), baseRegion.height * region.scl(), 0);
            }
            
            if (upperShadowRegion.found()){
                Draw.z(Layer.block - 0.01f);
                Draw.color(Color.white);
                Draw.alpha(shadowAlpha);
                Draw.rect(upperShadowRegion, x - nestShadowOffset, y - nestShadowOffset, xsize, ysize, rot);
            }
            
            Draw.z(Layer.block + 0.01f);
            Draw.color(col);
            Draw.alpha(1f);
            Draw.rect(region, x, y, xsize, ysize, rot);

            Draw.color(Color.white);
            Draw.alpha(efficiency);
            Draw.rect(lightRegion, x, y, xsize, ysize, rot);
            
            Draw.alpha(1f);
            
            if (layer > 0)
            	Draw.z(layer);
            
            Draw.blend(Blending.additive);
            Draw.color(glowColor);
            Draw.alpha(currentGlow() * efficiency * alpha);
            Draw.rect(glowRegion, x, y, xsize, ysize, rot);
            Draw.blend();
            
            Draw.z(z);
            Draw.color();
            Draw.blend();
            Draw.reset();
		}
        
        @Override
        public void onDestroyed() {
            super.onDestroyed();
			Tile tile = world.tileWorld(x + Tmp.v1.x, y + Tmp.v1.y);
            Puddles.deposit(tile, TektonLiquids.acid, 140f * efficiency);
			Drawt.DrawAcidDebris(x, y, Mathf.random(4) * 90, size);
			Drawt.DrawAcidDebris(x, y, Mathf.random(4) * 90, size);
        }
		
		@Override
        public void drawLight() {
			if (!emitLight)
				return;
            Drawf.light(x, y, (90f + Mathf.absin(5, 5f)) * currentGlow(), Tmp.c1.set(lightColor), 0.4f * currentGlow() * efficiency);
        }
		
		public float currentGrow() {
			return Mathf.absin(totalProgress(), growScale, alpha) * growIntensity + 1f - growIntensity;
		}
		
		public float currentGlow() {
			return Mathf.absin(totalProgress(), glowScale, alpha) * glowIntensity + 1f - glowIntensity;
		}
        
        @Override
        public float totalProgress() {
            return totalProgress;
        }
	}
}
