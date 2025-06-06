package tekton.type.defense;

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
import tekton.content.TektonSounds;
import tekton.content.TektonStatusEffects;
import tekton.type.power.TektonNuclearReactor.TektonNuclearReactorBuild;

public class CoreRadar extends Block {
    public float discoveryTime = 60f * 0.1f;
    public float rotateSpeed = 0.5f;
    public float lineRotateSpeed = -3f, strokeSize = 1.5f;
    public float maxRadius = 350f;
    public float timeBetweenChecks = 60 * 4f;
    public float laserScale = 0.7f;

    public TextureRegion laser;
    public TextureRegion laserEnd;
    
    public TextureRegion baseRegion;
    public TextureRegion baseTeamRegion;
    public TextureRegion glowRegion;
    public TextureRegion teamGlowRegion;
    
    public float innerCircleRad = 12f, outerCircleRad = 18f;
    
    public float glowScl = 5f, glowMag = 0.4f;
	
	public CoreRadar(String name) {
		super(name);

        update = solid = true;
        flags = EnumSet.of(BlockFlag.hasFogRadius);
        outlineIcon = true;
        
        loopSound = Sounds.none;
        loopSoundVolume = 0f;
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

        addBar("instability", (CoreRadarBuild entity) -> new Bar("stat.range", Pal.berylShot, () -> Mathf.clamp(entity.currentRadius / maxRadius)));
    }
    
    @Override
    public void load() {
        super.load();
        
    	baseRegion = Core.atlas.find(name + "-base");
    	baseTeamRegion = Core.atlas.find(name + "-base-team");
		glowRegion = Core.atlas.find(name + "-glow");
		teamGlowRegion = Core.atlas.find(name + "-team-glow");
		
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
        	beforeCores = analizedCores.copy();
        	analizedCores = new Seq<CoreBuild>();
        	Vars.indexer.allBuildings(x, y, currentRadius * tilesize, other -> {
            	if (other.team != team && other instanceof CoreBuild) {
            		CoreBuild core = (CoreBuild)other;
                	analizedCores.add(core);
                	discoverEffect.at(core.x, core.y);
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
            Draw.color(this.team == Team.sharded ? Color.clear : this.team.color.cpy());
            Draw.rect(baseTeamRegion, x, y);
            Draw.color();
            Draw.z(Layer.turret);
            Draw.rect(region, x, y, rotateSpeed * totalProgress);
            Draw.color(this.team == Team.sharded ? Color.clear : this.team.color.cpy());
            Draw.rect(teamGlowRegion, x, y, rotateSpeed * totalProgress);
            Drawf.additive(glowRegion, this.team.color, 1f * (1f - glowMag + Mathf.absin(glowScl, glowMag)) * efficiency, x, y, rotateSpeed * totalProgress, Layer.turretHeat);
            Draw.color();
            Draw.z(Layer.bullet - 0.0001f);
            
            Lines.stroke(strokeSize * efficiency, this.team.color.cpy().mul(1.3f).a(1f));
            Lines.circle(x, y, innerCircleRad);
            Lines.circle(x, y, outerCircleRad);
            Lines.lineAngle(x, y, rotateSpeed * totalProgress * lineRotateSpeed, (outerCircleRad) - 0.4f);
            
            
            Draw.z(Layer.power + 0.01f);
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
        public void drawLight(){
            Drawf.light(x, y, lightRadius * efficiency, this.team.color.cpy(), 0.8f);
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
