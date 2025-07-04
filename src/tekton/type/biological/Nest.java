package tekton.type.biological;

import static mindustry.Vars.*;

import arc.Core;
import arc.Events;
import arc.audio.Sound;
import arc.graphics.*;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mat;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.EnumSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.Strings;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.content.Fx.*;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Puddles;
import mindustry.entities.effect.*;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.BlockRenderer;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.*;
import mindustry.ui.Bar;
import mindustry.world.*;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tekton.Drawt;
import tekton.content.*;
import tekton.type.defense.AdvancedWall;

import static arc.graphics.g2d.Draw.color;
import static mindustry.Vars.*;

public class Nest extends Block implements BiologicalBlock {
	public final int timerSpawn = timers++;
	public Seq<UnitType> creatures;

	public float ticksToSpawn = 60f * 50f;
	public float ticksRandom = 60f * 20f;
	public boolean spawnOnDestroy = false;
	public boolean spawnOnCenter = false;
	
	public float alpha = 0.9f, glowScale = 15f, glowIntensity = 0.5f, shadowAlpha = 0.2f;
    public Color glowColor = TektonLiquids.acid.color.cpy();
    public Color damageColor = TektonLiquids.acid.color.cpy();
    public float growScale = 20f, growIntensity = 0.2f;
    public float powerProduction = 1;
    public float spawnSpeedMultipliyer = 2f;
    public float minRequiredTimeMultipliyer = 0.2f;
    
    public float recoil = 8f;
    public float recoilTime = 60f;
    public float recoilPow = 1.8f;
    
	public float regenReload = 100f;
	public float healPercent = 1f;
	public Color regenColor = Pal.heal;
	public Effect regenEffect = TektonFx.buildingBiologicalRegeneration.wrap(regenColor);
    
    public int explosionRadius = 12;
    public int explosionDamage = 0;
    public Effect explodeEffect = Fx.none;
    public Effect spawnEffect = Fx.none;
    public Sound explodeSound = Sounds.none;

    public int explosionPuddles = 15;
    public float explosionPuddleRange = tilesize * 3f;
    public float explosionPuddleAmount = 140f;
    public @Nullable Liquid explosionPuddleLiquid = TektonLiquids.acid;
    public float explosionMinWarmup = 0f;

    public float explosionShake = 1f, explosionShakeDuration = 6f;
    public boolean drawBase = true;
	public boolean growAnimation = true;
	public boolean glowAnimation = true;
    public int upperVariants = 0;
    public float nestShadowOffset = 3f;
    
    public String basePrefix = "nest-";
    public TextureRegion glowRegion;
    public TextureRegion baseRegion;
    public TextureRegion upperShadowRegion;
    public TextureRegion[] upperShadowRegionVariants;
	
	public Nest(String name) {
		super(name);
		unitCapModifier = 5;
        envEnabled |= Env.space;
		creatures = new Seq<UnitType>();
		buildVisibility = BuildVisibility.sandboxOnly;
		lightColor = glowColor;
		lightRadius = 4f;
		emitLight = true;
		update = true;
        solid = true;
		hasPower = true;
        consumesPower = false;
		outputsPower = true;
		customShadow = false;
		createRubble = drawCracks = false;
        sync = true;
        outlineIcon = true;
        outlineColor = TektonColor.tektonOutlineColor;
        baseExplosiveness = 5f;
		group = BlockGroup.units;
		flags = EnumSet.of(BlockFlag.hasFogRadius);
		size = 3;
		health = 100;
		armor = 1;
		drawTeamOverlay = false;
		spawnEffect = new ExplosionEffect() {{
			lifetime = 40f;
            waveStroke = 3f;
            waveColor = smokeColor = glowColor.cpy().a(1f);
            waveRad = 12f;
            smokeSize = 3.5f;
            smokes = 7;
            smokeSizeBase = 0f;
            sparks = 0;
            sparkColor = Color.clear;
            sparkRad = 40f;
            sparkLen = 3f;
            sparkStroke = 0f;
		}};
		hideDetails = true;
		destroyEffect = TektonFx.biologicalDynamicExplosion;
		alwaysUnlocked = false;
	}
	
	@Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.basePowerGeneration, powerProduction * 60f, StatUnit.powerSecond);
    }
	
	@Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{baseRegion, region};
    }
	
	@Override
    public void load(){
        super.load();
        
        baseRegion = Core.atlas.find("tekton-" + basePrefix + "block-" + size);
        glowRegion = Core.atlas.find(name + "-glow");
        upperShadowRegion = Core.atlas.find(name + "-upper-shadow");
    }
	
	@Override
    public void setBars(){
        super.setBars();
        if(hasPower && outputsPower){
            addBar("power", (NestBuild entity) -> new Bar(
            () -> Core.bundle.format("bar.poweroutput",
            Strings.fixed(entity.getPowerProduction() * 60 * entity.timeScale(), 1)),
            () -> TektonColor.acid.cpy(),
            () -> entity.efficiency));
        }
    }

    @Override
    public boolean outputsItems(){
        return false;
    }
	
    @Override
    public void drawShadow(Tile tile){ super.drawShadow(tile); }

	public class NestBuild extends Building implements BiologicalSpawner {
		public float currentRandom = 0f;
		public float totalProgress = 0f;
        public float smoothLight;
        public int currentIndex = 0;
        public float curRecoil = 0f;
		public boolean needRegen = false;
		public float regenCharge = Mathf.random(regenReload);
		public Seq<Vec2> spawnPositions = new Seq<Vec2>();
        
        @Override
        public void created() {
        	if (currentRandom == 0f) {
            	currentRandom = Mathf.random(-ticksRandom, ticksRandom);
            	var ftimer = Mathf.lerp(currentTimer(), currentTimer() / spawnSpeedMultipliyer, 1f - (health / maxHealth));
            	timer(timerSpawn, ftimer);
        	}
        	totalProgress = Mathf.random(0f, 1000f);
            super.created();
        }
        
        @Override
        public void updateTile() {
            totalProgress += Time.delta * timeScale * currentBoost();
            curRecoil = Mathf.approachDelta(curRecoil, 0, 1f / recoilTime);
            
            if (enabled) {
            	var ftimer = Mathf.lerp(currentTimer(), currentTimer() / spawnSpeedMultipliyer, 1f - (health / maxHealth));
                if (timer(timerSpawn, ftimer)) {
                	curRecoil = 1f;
            		currentRandom = Mathf.random(-ticksRandom, ticksRandom);
            		currentIndex = Mathf.random(creatures.size - 1);
            		spawnByCurrentIndex();
            		spawnEffect.at(this);
                }
            }
            
			needRegen = damaged();
			
			if (needRegen)
				regenCharge += Time.delta * timeScale * efficiency;
			
			if (regenCharge >= regenReload && needRegen) {
				regenCharge = 0f;
				heal(maxHealth() * (healPercent) / 100f);
				recentlyHealed();
				regenEffect.at(x + Mathf.range(block.size * tilesize/2f - 1f), y + Mathf.range(block.size * tilesize/2f - 1f));
			}
        }
        
        public float currentBoost() {
        	return (1f + ((1f - (health / maxHealth)) * spawnSpeedMultipliyer));
        }
        
        public float currentTimer() {
        	return ticksToSpawn + currentRandom;
        }
        
        public Unit spawn(UnitType unitType) {
        	if (spawnPositions.size == 0) {
        		int sx = tileX() - (size / 2) - size % 2, sy = tileY() - (size / 2) - size % 2;
        		for (int i = 0; i <= size + 1; i++) {
        			final Vec2 
        				v1 = new Vec2(sx, sy + i).scl(tilesize), 
    					v2 = new Vec2(sx + size + 1, sy + i).scl(tilesize), 
    					v3 = new Vec2(sx + i, sy).scl(tilesize), 
    					v4 = new Vec2(sx + i, sy + size + 1).scl(tilesize);
        			
        			if (!spawnPositions.contains((vec) -> { return v1.x == vec.x && v1.y == vec.y; }));
        				spawnPositions.add(v1);
        			if (!spawnPositions.contains((vec) -> { return v2.x == vec.x && v2.y == vec.y; }));
        				spawnPositions.add(v2);

        			if (!spawnPositions.contains((vec) -> { return v3.x == vec.x && v3.y == vec.y; }));
        				spawnPositions.add(v3);
        			if (!spawnPositions.contains((vec) -> { return v4.x == vec.x && v4.y == vec.y; }));
        				spawnPositions.add(v4);
        		}
        	}
        	Seq<Vec2> availablePositions = new Seq<Vec2>();
        	for (var pos : spawnPositions) {
        		if (!world.tile((int)(pos.x / tilesize), (int)(pos.y / tilesize)).solid()) {
        			availablePositions.add(pos);
        		}
        	}
        	if (availablePositions.size == 0) {
        		kill();
        		return null;
        	}
    		Vec2 position = spawnOnCenter ? new Vec2(x(), y()) : availablePositions.get(Mathf.random(availablePositions.size - 1)).add(new Vec2(Mathf.range(2.5f), Mathf.range(2.5f)));
    		if (!spawnOnCenter)
    			spawnEffect.at(position);
        	var creature = unitType.spawn(position, team());
        	creature.rotation = Mathf.atan2(creature.x - x, creature.y - y) * Mathf.radDeg;
        	creature.command().attackTarget = creature.command().findTarget(x, y, 1000f * tilesize, creature.type.targetAir, creature.type.targetGround);
        	
			return creature;
        }
        
        public Unit spawnByIndex(int index) {
        	return spawn(creatures.get(index));
        }
        
        public Unit spawnByCurrentIndex() {
        	return spawnByIndex(currentIndex);
        }
        
        @Override
        public void onDestroyed() {
            super.onDestroyed();
            createExplosion();
			Drawt.DrawAcidDebris(x, y, Mathf.random(4) * 90, size);
			if (spawnOnDestroy)
				spawnByCurrentIndex();
        }
        
        @Override
        public void draw() {
        	float layer = Layer.blockAdditive;
            float z = Draw.z();
            
            float 
            xsize = (growAnimation ? (currentGrow() * region.width * region.scl()) : region.width * region.scl()) - (Mathf.pow(curRecoil, recoilPow) * recoil),
    		ysize = (growAnimation ? (currentGrow() * region.height * region.scl()) : region.width * region.scl()) - (Mathf.pow(curRecoil, recoilPow) * recoil),
    		rot = growAnimation ? Mathf.sin(Time.time + x, 50f, 0.5f) + Mathf.sin(Time.time - y, 65f, 0.9f) + Mathf.sin(Time.time + y - x, 85f, 0.9f) : 0;
            
            Color col = damageColor.cpy().lerp(Color.white, health / maxHealth);
            
            TextureRegion shad = upperVariants == 0 ? upperShadowRegion : upperShadowRegionVariants[Mathf.randomSeed(tile.pos(), 0, Math.max(0, upperShadowRegionVariants.length - 1))];
            
            if (baseRegion.found() && drawBase) {
            	Draw.z(Layer.block - 0.011f);
                Draw.color(col);
                Draw.alpha(1f);
                Draw.rect(baseRegion, x, y, baseRegion.width * region.scl(), baseRegion.height * region.scl(), 0);
            }
            
            if (shad.found() && drawBase){
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
            Draw.alpha(glowAnimation ? currentGlow() * alpha : alpha);
            Draw.rect(glowRegion, x, y, xsize, ysize, rot);
            Draw.blend();
            
            Draw.z(z);
            Draw.color();
            Draw.blend();
            Draw.reset();
		};
		
		public void createExplosion() {
            if(explosionDamage > 0){
                Damage.damage(team, x, y, explosionRadius * tilesize, explosionDamage);
            }

            explodeEffect.at(this);
            explodeSound.at(this);

            if(explosionPuddleLiquid != null){
                for(int i = 0; i < explosionPuddles; i++){
                    Tmp.v1.trns(Mathf.random(360f), Mathf.random(explosionPuddleRange));
                    Tile tile = world.tileWorld(x + Tmp.v1.x, y + Tmp.v1.y);
                    Puddles.deposit(tile, explosionPuddleLiquid, explosionPuddleAmount);
                }
            }

            if(explosionShake > 0){
                Effect.shake(explosionShake, explosionShakeDuration, this);
            }
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
        public float getPowerProduction() {
            return powerProduction * currentBoost();
        }
		
		@Override
        public void drawLight() {
			if (!emitLight)
				return;
            smoothLight = currentGlow();
            Drawf.light(x, y, ((90f + Mathf.absin(5, 5f)) * smoothLight * lightRadius) / tilesize, Tmp.c1.set(lightColor), 0.4f * smoothLight * efficiency);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(currentRandom);
            write.i(currentIndex);
            write.f(regenCharge);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            currentRandom = read.f();
            currentIndex = read.i();
            regenCharge = read.f();
        }
        
        @Override
        public boolean canPickup() {
            return false;
        }
	}
}
