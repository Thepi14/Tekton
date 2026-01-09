package tekton.type.biological;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

import arc.Core;
import arc.audio.Sound;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.EnumSet;
import arc.struct.IntSet;
import arc.util.Nullable;
import arc.util.Strings;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Puddles;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.Liquid;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tekton.Drawt;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonLiquids;
import tekton.type.biological.Nest.NestBuild;

public class BioforceGenerator extends Block implements BiologicalBlock {
    public int biopowerOutput = 10;
    public float powerProduction = 1;
    public float liquidOutputAmount = 1f;
    public Liquid liquid = TektonLiquids.ammonia;
    public Color damageColor = TektonLiquids.acid.color.cpy();
    
    public float alpha = 0.9f, glowScale = 15f, glowIntensity = 0.5f, shadowAlpha = 0.2f;
    public float growScale = 20f, growIntensity = 0.2f;
    public Color glowColor = TektonLiquids.acid.color.cpy();
    public float glowMag = 0.6f, glowScl = 8f;
	
	public float regenReload = 100f;
	public float healPercent = 1f;
	public Color regenColor = TektonColor.ammonia.cpy();
	public Effect regenEffect = TektonFx.buildingBiologicalRegeneration.wrap(regenColor);

    public int explosionRadius = 12;
    public int explosionDamage = 0;
    public Effect explodeEffect = Fx.none;
    public Sound explodeSound = Sounds.none;
    
    public int explosionPuddles = 15;
    public float explosionPuddleRange = tilesize * 3f;
    public float explosionPuddleAmount = 140f;
    public float explosionMinWarmup = 0f;
    
    public float explosionShake = 1f, explosionShakeDuration = 6f;
    
    public boolean drawBase = true;
    public float shadowOffset = 3f;
    
    public String basePrefix = "nest-";
    public TextureRegion baseRegion;
    public TextureRegion glowRegion;
    public TextureRegion upperShadowRegion;

	public BioforceGenerator(String name) {
		super(name);
		
        envEnabled |= Env.space;
		buildVisibility = BuildVisibility.sandboxOnly;
		
		solid = true;
        sync = true;

        consumesPower = false;
		outputsPower = true;
        hasPower = true;
		conductivePower = true;
		
        hasLiquids = true;
		outputsLiquid = true;
		liquidCapacity = 60f;
		
        update = true;
        swapDiagonalPlacement = true;
        rotate = false;
		
		outlineIcon = true;
        outlineColor = TektonColor.tektonOutlineColor;
		emitLight = true;
        lightColor = TektonColor.acid;
		drawTeamOverlay = false;
		createRubble = drawCracks = false;
		destroyEffect = TektonFx.biologicalAmmoniaDynamicExplosion;
		alwaysUnlocked = false;
		group = BlockGroup.power;
        flags = EnumSet.of(BlockFlag.hasFogRadius);
        outputsLiquid = true;
        squareSprite = false;
		hideDetails = true;
		noUpdateDisabled = true;
		suppressable = false;
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
	
	@Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.basePowerGeneration, powerProduction * 60f, StatUnit.powerSecond);
    }
	
	@Override
    public void setBars(){
        super.setBars();
        if(hasPower && outputsPower){
            addBar("power", (BioforceGeneratorBuild entity) -> new Bar(
            () -> Core.bundle.format("bar.poweroutput",
            Strings.fixed(entity.getPowerProduction() * 60 * entity.timeScale(), 1)),
            () -> TektonColor.acid,
            () -> entity.efficiency));
        }
    }
	
	public class BioforceGeneratorBuild extends Building implements BiopowerBlock {
        public float biopower;
		public boolean needRegen = false;
		public float regenCharge = Mathf.random(regenReload);
		public float totalProgress = Mathf.random(1000f);

		@Override
		public void updateTile() {
			//super.updateTile();

            totalProgress += Time.delta * timeScale * efficiency;
			needRegen = damaged() && efficiency > 0.1f;
			
			biopower = biopowerOutput * timeScale * efficiency;
			
			liquids.set(liquid, liquidCapacity);
            dumpLiquid(liquid);
			
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
            
            if (upperShadowRegion.found()) {
                Draw.z(Layer.block - 0.01f);
                Draw.color(Color.white);
                Draw.alpha(shadowAlpha);
                Draw.rect(upperShadowRegion, x - shadowOffset, y - shadowOffset, xsize, ysize, rot);
            }
            
            Draw.z(Layer.block + 0.01f);
            Draw.color(col);
            Draw.alpha(1f);
            Draw.rect(region, x, y, xsize, ysize, rot);

            Draw.color(Color.white);
            Draw.alpha(efficiency);
            
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
            createExplosion();
			Drawt.DrawAmmoniaDebris(x, y, size);
        }
        
        public void createExplosion() {
            if(explosionDamage > 0){
                Damage.damage(team, x, y, explosionRadius * tilesize, explosionDamage);
            }

            if (explodeEffect != Fx.none)
            	explodeEffect.at(this);
            if (explodeSound != Sounds.none)
            	explodeSound.at(this);
            
        	if(liquid != null){
                for(int i = 0; i < explosionPuddles; i++){
                    Tmp.v1.trns(Mathf.random(360f), Mathf.random(explosionPuddleRange));
                    Tile tile = world.tileWorld(x + Tmp.v1.x, y + Tmp.v1.y);
                    Puddles.deposit(tile, liquid, explosionPuddleAmount);
                }
            }
        }
		
		@Override
        public void drawLight() {
			if (!emitLight)
				return;
            Drawf.light(x, y, (90f + Mathf.absin(5, 5f)) * currentGlow(), Tmp.c1.set(lightColor), 0.4f * currentGlow() * efficiency);
        }
        
        @Override
        public float getPowerProduction() {
            return powerProduction;
        }
		
		public float currentGrow() {
			return Mathf.absin(totalProgress(), growScale, alpha) * growIntensity + 1f - growIntensity;
		}
		
		public float currentGlow() {
			return Mathf.absin(totalProgress(), glowScale, alpha) * glowIntensity + 1f - glowIntensity;
		}
		
    	@Override
        public float fogRadius() {
            return fogRadius * efficiency;
        }
        
        @Override
        public float totalProgress() {
            return totalProgress;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(biopower);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            biopower = read.f();
        }

    	@Override
    	public float biopower() {
    		return biopower;
    	}

    	@Override
    	public float biopowerFrac() {
            return biopower / biopowerOutput;
    	}

    	@Override
    	public float calculateBiopower(float[] sideBiopower, IntSet cameFrom) {
    		return calculateBiopower(this, sideBiopower, cameFrom);
    	}
	}
}
