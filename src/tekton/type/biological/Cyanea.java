package tekton.type.biological;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.lineAngle;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;
import static mindustry.Vars.net;
import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

import arc.Core;
import arc.audio.Sound;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Pixmaps;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.PixmapRegion;
import arc.graphics.g2d.TextureAtlas.AtlasRegion;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Nullable;
import arc.util.Strings;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Puddles;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.effect.ExplosionEffect;
import mindustry.entities.pattern.ShootPattern;
import mindustry.entities.units.WeaponMount;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.MultiPacker.PageType;
import mindustry.graphics.Pal;
import mindustry.type.Liquid;
import mindustry.type.UnitType;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.UnitTetherBlock;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;
import tekton.Drawt;
import tekton.Tekton;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonLiquids;
import tekton.content.TektonStatusEffects;
import tekton.content.TektonVars;
import tekton.type.biological.Nest.NestBuild;
import tekton.type.biological.Vein.VeinBuild;
import tekton.type.bullets.EmptyBulletType;
import tekton.type.bullets.TektonEmpBulletType;

//yeah i dont like hard coding but this time it makes some sense
public class Cyanea extends Block implements BiologicalBlock {
	public final int timerSpawn = timers++;
	public final int timerShoot = timers++;
	
	public Seq<UnitType> creatureTypes;
	
    public Color glowColor = TektonColor.ammonia.cpy();
    public Color damageColor = TektonColor.ammonia.cpy();
    public Color hitColor = Pal.surge;
    
	public Color regenColor = TektonColor.ammonia.cpy();
	public Effect regenEffect = TektonFx.buildingBiologicalRegeneration.wrap(regenColor);
	
	public float alpha = 0.9f, glowScale = 15f, glowIntensity = 0.5f, shadowAlpha = 0.2f;
    public float shadowOffset = 6f;
    
    public int explosionDamage = 0;
    public Effect explodeEffect = TektonFx.cyaneaExplosion;
    public Effect spawnEffect = Fx.none;
    public Effect shootEffect = Fx.none;
    public Sound explodeSound = Sounds.explosionbig;
    
    public float minBiopower = 0f;
    public float reload = 120f;
    public Sound shootSound = Sounds.cannon;
    public BulletType bullet = new EmptyBulletType();
	public float enemyDetectionRadiusMultiplier = 2f * tilesize;
	public Effect chainEffect = Fx.chainEmp.wrap(Pal.surge.cpy());
	public float empDamageMultiplier = 100f;
    
    public int shieldHealth = 1000000000;
    public int explosionPuddles = 15;
    public float explosionPuddleAmount = 140f;
    public @Nullable Liquid explosionPuddleLiquid = TektonLiquids.ammonia;
    public float explosionMinWarmup = 0f;
    
    public float explosionShake = 1f, explosionShakeDuration = 6f;
    
    public boolean hasGlow = true;
    public boolean glowAnimation = true;
    public boolean drawBase = true;
    public String basePrefix = "nest-";
    public int shells = 4;
    public float shellRotationOffset = 0f;
    public float shellOpeningSpeed = 0.05f;
    public float shellOpening = 3.5f;
    public TextureRegion baseRegion;
    public TextureRegion[] shellRegions;
    public TextureRegion[] shellOutlineRegions;
    public TextureRegion glowRegion;
    public TextureRegion upperShadowRegion;
    public TextureRegion previewRegion;
	
	public Cyanea(String name) {
		super(name);
		
		health = 9000;
		armor = 20;
		size = 3;

		creatureTypes = new Seq<UnitType>();
		unitCapModifier = 5;
        envEnabled |= Env.space;
		buildVisibility = BuildVisibility.sandboxOnly;
		
		lightColor = glowColor;
		lightRadius = 4f;
		emitLight = true;
		update = true;
        solid = true;
        squareSprite = false;
        
		hasPower = true;
        consumesPower = false;
		outputsPower = true;
		
		drawTeamOverlay = false;
		customShadow = false;
		createRubble = drawCracks = false;
        sync = true;
		
		unitCapModifier = 5;
        envEnabled |= Env.space;
        
        outlineIcon = true;
        outlineColor = TektonColor.tektonOutlineColor;
        baseExplosiveness = 5f;
        
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

		basePrefix = "uranium-";
		damageColor = glowColor = regenColor = lightColor = TektonColor.ammonia.cpy();
		regenEffect = TektonFx.buildingBiologicalRegeneration.wrap(regenColor);
		destroyEffect = TektonFx.biologicalAmmoniaDynamicExplosion;
		
		clipSize = 1000f * tilesize * 2;
	}
	
	@Override
	public void load() {
		super.load();
		
        baseRegion = Core.atlas.find("tekton-" + basePrefix + "block-" + size);
		glowRegion = Core.atlas.find(name + "-glow");
        upperShadowRegion = Core.atlas.find(name + "-upper-shadow");
        previewRegion = Core.atlas.find(name + "-preview");
        
        shellRegions = new TextureRegion[shells];
        shellOutlineRegions = new TextureRegion[shells];
        for (int i = 0; i < shells; i++) {
            shellRegions[i] = Core.atlas.find(name + "-shell-" + i);
            shellOutlineRegions[i] = Core.atlas.find(name + "-shell-outline-" + i);
        }
        
	}
	
	@Override
    public void setBars(){
        super.setBars();
        
        /*if(hasPower && outputsPower){
            addBar("power", (NestBuild entity) -> new Bar(
            () -> Core.bundle.format("bar.poweroutput",
            Strings.fixed(entity.getPowerProduction() * 60 * entity.timeScale(), 1)),
            () -> TektonColor.acid,
            () -> entity.efficiency));
        }*/

        //TODO show number
        addBar("biopower", (CyaneaBuild entity) -> new Bar(
        		() -> Core.bundle.format("bar.gravity", (int)(Math.abs(entity.biopower) + 0.01f)), 
        		() -> TektonColor.ammonia, 
        		() -> entity.biopower / entity.maxBiopower));
        
        addBar("spawn", (CyaneaBuild entity) -> new Bar(
                () -> Core.bundle.format("respawn",
                		Strings.autoFixed((int)(entity.spawnProgress / entity.currentSpawnTimer * 100f), 3) + "%"),
                () -> Pal.power,
                () -> entity.spawnProgress / entity.currentSpawnTimer));
    }
	
	@Override
    public TextureRegion[] icons(){
		if (previewRegion.found())
			return new TextureRegion[]{previewRegion};
        return new TextureRegion[]{baseRegion, region};
    }
	
	protected float detectionDelay = 5f;
	
	public class CyaneaBuild extends Building implements BiopowerConsumer, UnitTetherBlock {
        protected IntSeq readCreatures = new IntSeq();
        
        public boolean dead = true;
        protected float detectionDelayProgress = 0f;
		public float biopower = 0f, previousBiopower = 0f;
        public float[] sideBiopower = new float[4];
        public IntSet cameFrom = new IntSet();
        public long lastBiopowerUpdate = -1;
        public boolean enemiesClose = false;
        
        public float openingProgress = 0f, spawnProgress = 0f, shootProgress = 0f;
		protected float currentSpawnTimer = 0f, currentShootTimer = 0f;
		
        public float maxBiopower = 0;
		public Seq<Unit> spawnedCreatures = new Seq<Unit>();
		private int updateDelay = 10, currentUpdateDelay = 0;
		
		/*@Override
		public Building create(Block clock, Team team) {
			return super.create(block, team);
		}*/
        
        @Override
        public void updateTile() {
        	super.updateTile();
        	updateBiopower();
        	
        	currentUpdateDelay++;
        	
        	if (currentUpdateDelay > updateDelay) {
            	if (biopower > maxBiopower)
            		maxBiopower = biopower;
                
            	if (biopower > minBiopower) {
            		health = shieldHealth;
            		dead = false;
            	}
            	else if (!dead) {
            		health = block.health;
                    createExplosion();
            		dead = true;
            	}
        	}

        	if (!dead) {
        		detectionDelayProgress += Time.delta * timeScale * shellOpeningSpeed;
            	
                if (detectionDelayProgress > detectionDelay) {
                	detectionDelayProgress = 0f;
                	
                	enemiesClose = false;
    	            Vars.indexer.allBuildings(x, y, detectionRadius(), other -> {
    	                if(team != other.team) {
    	                	enemiesClose = true;
    	                }
    	            });
                    Units.nearbyEnemies(team, x, y, detectionRadius(), other -> {
                        if(other.team != team && other.hittable()) {
    	                	enemiesClose = true;
                        }
                    });
                }
        		
        		openingProgress = Mathf.lerpDelta(openingProgress, !enemiesClose ? 0f : 1f, shellOpeningSpeed);
            	currentShootTimer = reload * Math.max(0.5f, biopowerFrac());
            	if (enemiesClose) {
                	if (maxBiopower >= 1) {
                    	shootProgress = timer.getTime(timerShoot);
                    	//spawnProgress = timer.getTime(timerSpawn);
                    	
                    	if (timer(timerShoot, currentShootTimer)) {
                    		shootEffect.at(this);
                    		shootSound.at(this);
                    		
                    		for (int i = 0; i < Math.min(15, maxBiopower); i++) {
                    			if (bullet != null) {
                    				var b = bullet.copy();
                    				b.homingRange = detectionRadius() / 2f;
                            		b.create(this, team, x, y, Mathf.range(360f), 1f, maxBiopower / 2f);
                    			}
                    		}
                            
                    		shootProgress = 0f;
                        }
                	}
                	if (maxBiopower >= 10) {
                		if (previousBiopower > biopower) {
                			empAttack();
                		}
                	}
            	}
        	}
        	
        	previousBiopower = biopower;
        }
        
        public void empAttack() {
        	float rad = detectionRadius();
            
            new Effect(50f, 100f, e -> {
                /*e.scaled(7f, b -> {
                    color(Pal.surge, b.fout());
                    Fill.circle(e.x, e.y, rad);
                });*/

                color(Pal.surge);
                stroke(e.fout() * 3f);
                Lines.circle(e.x, e.y, rad);

                int points = 8;
                float offset = Mathf.randomSeed(e.id, 360f);
                /*for(int i = 0; i < points; i++){
                    float angle = i* 360f / points + offset;
                    //for(int s : Mathf.zeroOne){
                        Drawf.tri(e.x + Angles.trnsx(angle, rad), e.y + Angles.trnsy(angle, rad), 6f, 50f * e.fout(), angle);
                    //}
                }*/

                Fill.circle(e.x, e.y, 12f * e.fout());
                color();
                Fill.circle(e.x, e.y, 6f * e.fout());
                Drawf.light(e.x, e.y, rad * 1.6f, Pal.surge, e.fout());
            }).at(this);
            
            Effect hitPowerEffect = new Effect(40, e -> {
                color(e.color);
                stroke(e.fout() * 1.6f);

                randLenVectors(e.id, 18, e.finpow() * 27f, e.rotation, 360f, (x, y) -> {
                    float ang = Mathf.angle(x, y);
                    lineAngle(e.x + x, e.y + y, ang, e.fout() * 6 + 1f);
                });
            });
            
            Vars.indexer.allBuildings(x, y, rad, other -> {
            	TektonFx.debugGreenSquare.at(other.x, other.y);
                if(team != other.team) {
                	var absorber = Damage.findAbsorber(team, x, y, other.x, other.y);
                    if(absorber != null) {
                        other = absorber;
                    }
                    hitPowerEffect.at(other.x, other.y, this.angleTo(other), hitColor);
                	chainEffect.at(x, y, 0, hitColor, other);
                	
                	
                	other.damage(empDamageMultiplier * biopowerMul());
                    other.applySlowdown(0.7f, 120f);
                }
            });
            Units.nearbyEnemies(team, x, y, rad, other -> {
                if(other.team != team && other.hittable()) {
                    var absorber = Damage.findAbsorber(team, x, y, other.x, other.y);
                    if(absorber != null) {
                        return;
                    }
                    hitPowerEffect.at(other.x, other.y, this.angleTo(other), hitColor);
                	chainEffect.at(x, y, 0, hitColor, other);
                	
                	TektonFx.debugRedSquare.at(other.x, other.y);
                	
                	other.damage(empDamageMultiplier * biopowerMul());
                    other.apply(TektonStatusEffects.shortCircuit, 120f);
                }
            });
        }
        
        @Override
        public void draw() {
        	float layer = Layer.blockAdditive;
            float z = Draw.z();
            
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
                Draw.rect(upperShadowRegion, x - shadowOffset, y - shadowOffset);
            }
            
            if (region.found()) {
                Draw.z(Layer.block + 0.01f);
                Draw.color(col);
                Draw.alpha(1f);
                Draw.rect(region, x, y);
            }
            
            //Draw.z(Layer.block - 0.005f);
            //Draw.rect(region, x, y);
            
            if (hasGlow) {
            	Draw.z(Layer.turretHeat);
                Draw.blend(Blending.additive);
                Draw.color(glowColor);
                Draw.alpha(glowAnimation ? currentGlow() * alpha : alpha);
                Draw.rect(glowRegion, x, y);
                Draw.blend();
            }
            
            float rotShell = shellRotationOffset + (360f / shells);
            for (int i = 0; i < shells; i++) {
                Draw.z(Layer.turretHeat);
                Draw.color(outlineColor);
                Draw.alpha(1f);
            	Draw.rect(shellOutlineRegions[i], x + Angles.trnsx(rotShell * i, shellOpening * openingProgress), y + Angles.trnsy(rotShell * i, shellOpening * openingProgress));
            	
                Draw.z(Layer.turretHeat + 0.01f);
                Draw.color(col);
                Draw.alpha(1f);
            	Draw.rect(shellRegions[i], x + Angles.trnsx(rotShell * i, shellOpening * openingProgress), y + Angles.trnsy(rotShell * i, shellOpening * openingProgress));
            }
            
            Drawf.circles(x, y, detectionRadius());
            
            Draw.z(z);
            Draw.color();
            Draw.blend();
            Draw.reset();
		};
		
		public float detectionRadius() {
			return (1f + maxBiopower) * enemyDetectionRadiusMultiplier;
		}
		
    	@Override
        public float fogRadius() {
            return fogRadius * maxBiopower;
        }
    	
    	public float biopowerMul() {
    		return (1f + maxBiopower) - biopower;
    	}
    	
    	public float biopowerFrac() {
    		return biopower / maxBiopower;
    	}
		
		public float currentGlow() {
			return (Mathf.absin(totalProgress(), glowScale, alpha) * glowIntensity + 1f - glowIntensity) * biopower == 0f ? 0f : 1f;
		}

        public void updateBiopower() {
            if(lastBiopowerUpdate == Vars.state.updateId) return;

            lastBiopowerUpdate = Vars.state.updateId;
            biopower = calculateBiopower(sideBiopower, cameFrom);
        }
        
        @Override
        public float[] sideBiopower() {
            return sideBiopower;
        }
        
        @Override
        public float biopowerRequirement() {
            return TektonVars.visualMaxGravity;
        }
        
        @Override
        public float biopower() {
            return biopower;
        }
        
        @Override
        public float calculateBiopower(float[] sideBiopower, @Nullable IntSet cameFrom) {
            return calculateBiopower(this, sideBiopower, cameFrom);
        }
        
        @Override
        public float warmup() {
            return biopower;
        }
        
        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(openingProgress);
            write.f(maxBiopower);
            write.bool(dead);
            
            write.s(spawnedCreatures.size);
            for(var unit : spawnedCreatures) {
                write.i(unit.id);
            }
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            openingProgress = read.f();
            enemiesClose = openingProgress > 0.5f;
            maxBiopower = read.f();
            dead = read.bool();

            int count = read.s();
            readCreatures.clear();
            for(int i = 0; i < count; i++) {
            	readCreatures.add(read.i());
            }
        }
        
        public void spawned(int id) {
            if(net.client()){
            	readCreatures.set(0, id);
            }
        }
        
        @Override
        public void onDestroyed() {
            super.onDestroyed();
			Drawt.DrawAmmoniaDebris(x, y, size);
        }
        
        public void createExplosion() {
            if(explosionDamage > 0){
                Damage.damage(team, x, y, detectionRadius(), explosionDamage);
            }

            if (explodeEffect != Fx.none)
            	explodeEffect.at(this);
            if (explodeSound != Sounds.none)
            	explodeSound.at(this);
            
        	if(explosionPuddleLiquid != null){
                for(int i = 0; i < explosionPuddles; i++){
                    Tmp.v1.trns(Mathf.random(360f), Mathf.random(detectionRadius() / 2f));
                    Tile tile = world.tileWorld(x + Tmp.v1.x, y + Tmp.v1.y);
                    Puddles.deposit(tile, explosionPuddleLiquid, explosionPuddleAmount);
                }
            }
        }
	}
}
