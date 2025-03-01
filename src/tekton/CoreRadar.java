package tekton;

import static mindustry.Vars.*;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.io.*;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.*;
import mindustry.Vars;
import mindustry.ai.types.SuicideAI;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.effect.*;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.gen.UnitEntity;
import mindustry.graphics.*;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.ui.Bar;
import mindustry.world.*;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;
import mindustry.world.draw.DrawMulti;
import mindustry.world.meta.*;
import tekton.TektonNuclearReactor.TektonNuclearReactorBuild;
import tekton.content.TektonSounds;
import tekton.content.TektonStatusEffects;

public class CoreRadar extends Block {
    public float discoveryTime = 60f * 0.1f;
    public float rotateSpeed = 0.5f;
    public float maxRadius = 250f;
    public float timeBetweenChecks = 60 * 4f;
    public float laserScale = 0.7f;

    public TextureRegion laser;
    public TextureRegion laserEnd;
    
    public TextureRegion baseRegion;
    public TextureRegion baseTeamRegion;
    public TextureRegion glowRegion;
    public TextureRegion teamGlowRegion;
    
    public TextureRegion circleRegion;
    public TextureRegion circleOuterRegion;
    public TextureRegion lineRegion;
    
    private BulletType fogger;
    public float glowScl = 5f, glowMag = 0.4f;
	
	public CoreRadar(String name) {
		super(name);

        update = solid = true;
        flags = EnumSet.of(BlockFlag.hasFogRadius);
        outlineIcon = true;
        
        loopSound = Sounds.none;
        loopSoundVolume = 0f;
        
        fogger = new BulletType(0, 0) {{
			instantDisappear = true;
			hittable = reflectable = false;
			collides = false;
			lifetime = 1f;
            keepVelocity = false;
            maxRange = -1f;
			hitEffect = despawnEffect = Fx.none;
			spawnUnit = new UnitType("fogger"){{
	        	this.constructor = UnitEntity::create;
	        	immunities.add(TektonStatusEffects.foggerStatus);
				flying = true;
				this.fogRadius = 2;
                speed = 0f;
                maxRange = -1f;
                lifetime = 1f;
                health = 1;
    			hittable = false;
    			collides = collidesAir = collidesGround = collidesTiles = false;
    			//killable = false;
                isEnemy = false;
                targetable = false;
                logicControllable = playerControllable = false;
                allowedInPayloads = false;
    			fallEffect = fallEngineEffect = deathExplosionEffect = Fx.none;
    			outlines = false;
    			targetAir = targetGround = canAttack = true;
    			hidden = true;
    			hoverable = false;
    			drawBody = drawMinimap = drawCell = drawItems = false;
    			crashDamageMultiplier = 0f;
    			lightRadius = 0f;
    			engineSize = 0f;
    			itemCapacity = 0;
				aiController = SuicideAI::new;
    			weapons.add(new Weapon() {{
                    maxRange = 1000f;
    				display = false;
    				alwaysShooting = true;
    				shootSound = Sounds.none;
    				mirror = false;
    				rotate = true;
    				controllable = false;
    				autoTarget = true;
    				shootCone = 180f;
    				ignoreRotation = true;
    				bullet = new BasicBulletType(0, 0) {{
    					hittable = reflectable = false;
    					range = 1000f;
    					width = height = 0f;
                    	collidesTiles = false;
                    	collides = false;
                    	hitSound = Sounds.none;
                    	rangeOverride = 0;
                    	trailChance = -1;
                    	despawnHit = true;
                    	despawnEffect = Fx.none;
                    	hitColor = Pal.suppress;
                    	hitEffect = Fx.none;
                    	splashDamageRadius = 0;
                    	instantDisappear = true;
                    	splashDamage = 0;
                    	killShooter = true;
    				}};
    			}});
            }};
		}};
	}
	
	@Override
	public void init() {
        super.init();

		clipSize = (maxRadius) * tilesize * 2;
	}
	
	@Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.range, maxRadius, StatUnit.blocks);
    }
	
	@Override
    public void setBars(){
        super.setBars();

        addBar("instability", (CoreRadarBuild entity) -> new Bar("bar.range", Pal.berylShot, () -> Mathf.clamp(entity.currentRadius / maxRadius)));
    }
    
    @Override
    public void load() {
        super.load();
        
    	baseRegion = Core.atlas.find(name + "-base");
    	baseTeamRegion = Core.atlas.find(name + "-base-team");
		glowRegion = Core.atlas.find(name + "-glow");
		teamGlowRegion = Core.atlas.find(name + "-team-glow");
		
		circleRegion = Core.atlas.find(name + "-circle");
		circleOuterRegion = Core.atlas.find(name + "-circle-outer");
		lineRegion = Core.atlas.find(name + "-line");
		
		laser = Core.atlas.find(name + "-laser", "tekton-laser");
		laserEnd = Core.atlas.find(name + "-laser-end", "tekton-laser-end");
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{baseRegion, region};
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
        
        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, maxRadius * tilesize, Pal.accent);
    }

	public class CoreRadarBuild extends Building {
        public float progress;
        private float currentRadius = 0f;
        public float smoothEfficiency = 1f;
        public float totalProgress;
        private Seq<CoreBuild> analizedCores = new Seq<CoreBuild>();
        private Seq<CoreBuild> beforeCores = new Seq<CoreBuild>();
        private float timer = 0f;
        private boolean reachedMax = false;
        private boolean firstPinged = false;
        private boolean effectSet = false;
        private WaveEffect discoverEffect, radarFindingEffect;

        @Override
        public float fogRadius(){
            //return fogRadius * progress * smoothEfficiency;
        	return 0f;
        }
        
        @Override
        public void updateTile(){
        	if (!effectSet) {
        		
        		discoverEffect = new WaveEffect() {{
        			sizeFrom = 2;
        	    	sizeTo = 60;
        	    	lifetime = 40;
        	    	strokeFrom = 10;
        	    	strokeTo = 0;
        	    	colorTo = colorFrom = team.color.cpy();
        		}};
        		radarFindingEffect = new WaveEffect() {{
        			sizeFrom = 2;
        	    	sizeTo = 80;
        	    	lifetime = 60;
        	    	strokeFrom = 10;
        	    	strokeTo = 0;
        	    	colorTo = colorFrom = team.color.cpy();
        		}};
        		
        		effectSet = true;
        	}
            
            if (!firstPinged && efficiency > 0.001f) {
            	researchArea();
            	firstPinged = true;
            	return;
            }
            else if (currentRadius >= maxRadius && !reachedMax) {
            	reachedMax = true;
            	currentRadius = maxRadius;
            }
        	
            smoothEfficiency = Mathf.lerpDelta(smoothEfficiency, efficiency, 0.05f);

            timer += edelta() * efficiency;
            progress += efficiency * edelta() * rotateSpeed;
            totalProgress += efficiency * edelta() * rotateSpeed;
            progress = Mathf.clamp(progress);
            
            if (currentRadius < maxRadius && !reachedMax) {
                currentRadius += (edelta() / discoveryTime) * efficiency;
            }
            if (timer >= timeBetweenChecks) {
            	timer = 0f;
            	radarFindingEffect.at(x, y);
                researchArea();
            }
            
        }
        public void researchArea() {
        	/*Units.nearbyEnemies(Team.derelict, x, y, 2f, other -> {
                if(other.isImmune(TektonStatusEffects.foggerStatus)){
                	other.destroy();
                }
            });*/
        	beforeCores = analizedCores.copy();
        	analizedCores = new Seq<CoreBuild>();
        	Vars.indexer.allBuildings(x, y, currentRadius * tilesize, other -> {
            	if (other.team != team && other instanceof CoreBuild) {
            		CoreBuild core = (CoreBuild)other;
            		/*if (analizedCores.contains(core)) {
            			return;
            		}*/
                	analizedCores.add(core);
                	discoverEffect.at(core.x, core.y);
                	//BulletType.createBullet(fogger, team, core.x, core.y, 0f, 0f, 0f, 1f);
            	}
            });
        	if (analizedCores.size != beforeCores.size) {
            	TektonSounds.sonarping.at(x, y, 1.0f, 0.8f);
        	}
        	else {
            	TektonSounds.sonarloop.at(x, y, 1.0f, 1f);
        	}
        }

        @Override
        public boolean canPickup(){
            return false;
        }
        
        @Override
        public void drawSelect(){
            var z = Draw.z();
            Draw.z(Layer.fogOfWar + 0.1f);
            Drawf.dashCircle(x, y, currentRadius * tilesize, Pal.berylShot);
            Draw.z(z);
        }
        
        public void drawLaser(float x1, float y1, float x2, float y2, int size1, int size2){
        	Draw.color(this.team.color.cpy().mula(1.15f));
            Drawf.laser(laser, laserEnd, x1, y1, x2, y2, efficiency * laserScale * (1f - glowMag + Mathf.absin(glowScl, glowMag)));
            Draw.color();
            Draw.reset();
        }

		@Override
        public void draw(){
            var z = Draw.z();
            Draw.rect(baseRegion, x, y);
            Draw.color(this.team == Team.sharded ? Color.clear : this.team.color);
            Draw.rect(baseTeamRegion, x, y);
            Draw.color();
            Draw.z(Layer.turret);
            Draw.rect(region, x, y, rotateSpeed * totalProgress);
            Draw.color(this.team == Team.sharded ? Color.clear : this.team.color);
            Draw.rect(teamGlowRegion, x, y, rotateSpeed * totalProgress);
            Draw.color();
            Draw.z(z);
            
            Drawf.additive(glowRegion, this.team.color, 1f * (1f - glowMag + Mathf.absin(glowScl, glowMag)) * efficiency, x, y, rotateSpeed * totalProgress, Layer.turretHeat);
            
            Drawf.additive(circleRegion, this.team.color, efficiency, x, y, 0, Layer.power);
            Drawf.additive(circleOuterRegion, this.team.color, efficiency, x, y, 0, Layer.power);
            Drawf.additive(lineRegion, this.team.color, efficiency, x, y, rotateSpeed * totalProgress * -3f, Layer.power);
            
            Draw.z(Layer.power);
            if (efficiency > 0.001f)
	            for (var core : analizedCores) {
	            	drawLaser(x, y, core.x, core.y, size, size);
	            };

            Draw.z(z);
            Draw.reset();
        }
        
        @Override
        public float progress(){
            return progress;
        }

        @Override
        public void write(Writes write){
            super.write(write);

            write.f(currentRadius);
            write.f(timer);
            write.bool(reachedMax);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);

            currentRadius = read.f();
            timer = read.f();
            reachedMax = read.bool();
        }
	}
}
