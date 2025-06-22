package tekton.type.biological;

import static arc.graphics.g2d.Draw.color;
import static mindustry.Vars.*;

import arc.Core;
import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Structs;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.content.Fx;
import mindustry.core.Renderer;
import mindustry.core.World;
import mindustry.entities.Effect;
import mindustry.entities.Puddles;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.Edges;
import mindustry.world.Tile;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.meta.Env;
import tekton.Drawt;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonLiquids;
import tekton.type.biological.Nest.NestBuild;

public class BioNode extends PowerNode implements BiologicalBlock {
    public Color damageColor = TektonLiquids.acid.color.cpy();
    
    public float alpha = 0.9f, glowScale = 15f, glowIntensity = 0.5f, shadowAlpha = 0.2f;
    public float growScale = 20f, growIntensity = 0.2f;
    public Color glowColor = TektonColor.acid.cpy();
    public float glowMag = 0.6f, glowScl = 8f;
    
	public boolean sameBlockConnection = false;
	
	public float regenReload = 100f;
	public float healPercent = 1f;
	public Color regenColor = Pal.heal;
	public Effect regenEffect = TektonFx.buildingBiologicalRegeneration.wrap(regenColor);
	
	public Color effectColor = TektonColor.acid.cpy();
	public float effectReload = 100f;
	
    public boolean drawBase = true;
    public int upperVariants = 0;
    public float nestShadowOffset = 3f;
    
    public String basePrefix = "nest-";
    public TextureRegion baseRegion;
    public TextureRegion glowRegion;
    public TextureRegion upperShadowRegion;
    public TextureRegion[] upperShadowRegionVariants;
	
	public BioNode(String name) {
		super(name);
		fogRadius = 3;
		emitLight = true;
		outlineIcon = true;
		drawTeamOverlay = false;
		createRubble = drawCracks = false;
        outlineColor = TektonColor.tektonOutlineColor;
        envEnabled |= Env.space;
        lightRadius = 25f;
		lightColor = glowColor;
		destroyEffect = TektonFx.biologicalDynamicExplosion;
        
		update = true;
		alwaysUnlocked = false;
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
        upperShadowRegion = Core.atlas.find(name + "-upper-shadow");
	}
	
	public class BioNodeBuild extends PowerNodeBuild {
		public boolean needRegen = false;
		public float regenCharge = Mathf.random(regenReload);
		public float totalProgress = Mathf.random(1000f);
		public float effectReloadProgress = Mathf.random(effectReload);;
		
		@Override
		public void updateTile() {
			super.updateTile();

			efficiency = power.graph.getSatisfaction();
            totalProgress += Time.delta * timeScale * efficiency;
            effectReloadProgress += Time.delta * timeScale * efficiency;
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
            
            TextureRegion shad = upperVariants == 0 ? upperShadowRegion : upperShadowRegionVariants[Mathf.randomSeed(tile.pos(), 0, Math.max(0, upperShadowRegionVariants.length - 1))];
            
            if (baseRegion.found() && drawBase) {
            	Draw.z(Layer.block - 0.011f);
                Draw.color(col);
                Draw.alpha(1f);
                Draw.rect(baseRegion, x, y, baseRegion.width * region.scl(), baseRegion.height * region.scl(), 0);
            }
            
            if (shad.found()){
                Draw.z(Layer.block - 0.01f);
                Draw.color(Color.white);
                Draw.alpha(shadowAlpha);
                Draw.rect(shad, x - nestShadowOffset, y - nestShadowOffset, xsize, ysize, rot);
            }
            
            Draw.z(Layer.block + 0.01f);
            Draw.color(col);
            Draw.alpha(1f);
            Draw.rect(region, x, y, xsize, ysize, rot);
            
            if (layer > 0)
            	Draw.z(layer);
            
            Draw.blend(Blending.additive);
            Draw.color(glowColor);
            Draw.alpha(currentGlow() * efficiency * alpha);
            Draw.rect(glowRegion, x, y, xsize, ysize, rot);
            Draw.blend();
            Draw.color();
			
            if(Mathf.zero(Renderer.laserOpacity) || isPayload() || team == Team.derelict) return;

            Draw.z(Layer.power);
            setupColor(power.graph.getSatisfaction());

            for(int i = 0; i < power.links.size; i++){
                Building link = world.build(power.links.get(i));

                if(!linkValid(this, link, true)) continue;

                if(link.block instanceof PowerNode && link.id >= id) continue;
                
                float ex = x + Mathf.range(block.size * tilesize / 2f), ey = y + Mathf.range(block.size * tilesize / 2f);
                float lx = link.x + Mathf.range(link.block.size * tilesize / 2f), ly = link.y + Mathf.range(link.block.size * tilesize / 2f);
                
                if (effectReloadProgress >= effectReload && !state.isPaused()) {
                    if (link.getPowerProduction() > 0f) {
                        TektonFx.arteryPowerTransfer.at(ex, ey, 0f, effectColor, link);
                    }
                    else if (link.block instanceof PowerNode) {
                    	int which = Mathf.random(1);
                    	if (which == 0)
                    		TektonFx.arteryPowerTransfer.at(ex, ey, 0f, effectColor, link);
                    	else
                    		TektonFx.arteryPowerTransfer.at(lx, ly, 0f, effectColor, this);
                    }
                    else {
                    	TektonFx.arteryPowerTransfer.at(lx, ly, 0f, effectColor, this);
                    }
                }
            }
            
            if (effectReloadProgress >= effectReload)
            	effectReloadProgress = Mathf.range(effectReload / 2f);
            
            Draw.z(z);
            Draw.color();
            Draw.blend();
            Draw.reset();
        }
		
		@Override
        public void drawLight() {
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
        
        @Override
        public void onDestroyed() {
            super.onDestroyed();
			Tile tile = world.tileWorld(x + Tmp.v1.x, y + Tmp.v1.y);
            Puddles.deposit(tile, TektonLiquids.acid, 140f * efficiency);
			Drawt.DrawAcidDebris(x, y, Mathf.random(4) * 90, size);
        }
		
		protected void getPotentialLinks(Tile tile, Team team, Cons<Building> others){
	        if(!autolink) return;

	        Boolf<Building> valid = other -> other != null && other.tile != tile && other.block.connectedPower && other.power != null &&
	            (other.block.outputsPower || other.block.consumesPower || other.block instanceof PowerNode) &&
	            overlaps(tile.x * tilesize + offset, tile.y * tilesize + offset, other.tile, laserRange * tilesize) && other.team == team &&
	            !graphs.contains(other.power.graph) &&
	            //!PowerNode.insulated(tile, other.tile) &&
	            !(other instanceof PowerNodeBuild obuild && obuild.power.links.size >= ((PowerNode)obuild.block).maxNodes) &&
	            !Structs.contains(Edges.getEdges(size), p -> { //do not link to adjacent buildings
	                var t = world.tile(tile.x + p.x, tile.y + p.y);
	                return t != null && t.build == other;
	            });

	        tempBuilds.clear();
	        graphs.clear();

	        //add conducting graphs to prevent double link
	        for(var p : Edges.getEdges(size)){
	            Tile other = tile.nearby(p);
	            if(other != null && other.team() == team && other.build != null && other.build.power != null){
	                graphs.add(other.build.power.graph);
	            }
	        }

	        if(tile.build != null && tile.build.power != null){
	            graphs.add(tile.build.power.graph);
	        }

	        var worldRange = laserRange * tilesize;
	        var tree = team.data().buildingTree;
	        if(tree != null){
	            tree.intersect(tile.worldx() - worldRange, tile.worldy() - worldRange, worldRange * 2, worldRange * 2, build -> {
	                if(valid.get(build) && !tempBuilds.contains(build)){
	                    tempBuilds.add(build);
	                }
	            });
	        }

	        tempBuilds.sort((a, b) -> {
	            int type = -Boolean.compare(a.block instanceof PowerNode, b.block instanceof PowerNode);
	            if(type != 0) return type;
	            return Float.compare(a.dst2(tile), b.dst2(tile));
	        });

	        returnInt = 0;

	        tempBuilds.each(valid, t -> {
	            if(returnInt ++ < maxNodes){
	                graphs.add(t.power.graph);
	                others.get(t);
	            }
	        });
	    }
		
	    public boolean linkValid(Building tile, Building link, boolean checkMaxNodes){
	        if(tile == link || link == null || !link.block.hasPower || !link.block.connectedPower || tile.team != link.team || (sameBlockConnection && tile.block != link.block)) return false;

	        if(overlaps(tile, link, laserRange * tilesize) || (link.block instanceof PowerNode node && overlaps(link, tile, node.laserRange * tilesize))){
	            if(checkMaxNodes && link.block instanceof PowerNode node){
	                return link.power.links.size < node.maxNodes || link.power.links.contains(tile.pos());
	            }
	            return true;
	        }
	        return false;
	    }
	}
}
