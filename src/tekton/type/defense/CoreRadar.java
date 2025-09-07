package tekton.type.defense;

import static mindustry.Vars.*;

import arc.Core;
import arc.audio.Sound;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.Vec2;
import arc.struct.*;
import arc.util.Time;
import arc.util.Tmp;
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
    public float warmupSpeed = 0.1f;

    public TextureRegion laser;
    public TextureRegion laserEnd;
    
    public TextureRegion baseRegion;
    public TextureRegion baseTeamRegion;
    public TextureRegion glowRegion;
    public TextureRegion teamGlowRegion;
    public TextureRegion arrowRegion;
    
    public Sound sonarSound = TektonSounds.sonarloop, pingSound = TektonSounds.sonarping;
    
    public float arrowSpacing = 24f, arrowDistance = 44f, symbolDistance = 30f;
    
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

        stats.add(Stat.range, maxRadius * 2f, StatUnit.blocks);
    }
	
	@Override
    public void setBars(){
        super.setBars();

        addBar("range", (CoreRadarBuild entity) -> new Bar("stat.range", Pal.berylShot, () -> Mathf.clamp(entity.currentRadius / maxRadius)));
    }
    
    @Override
    public void load() {
        super.load();
        
    	baseRegion = Core.atlas.find(name + "-base");
    	baseTeamRegion = Core.atlas.find(name + "-base-team");
    	
		glowRegion = Core.atlas.find(name + "-glow");
		teamGlowRegion = Core.atlas.find(name + "-team-glow");
		
		arrowRegion = Core.atlas.find(name + "-arrow");
		
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
        private float currentRadius = 0f, warmup = 0f;
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
            warmup = Mathf.lerpDelta(warmup, efficiency, warmupSpeed);
            
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
        		pingSound.at(x, y, 1.0f, 0.3f);
        	}
        	else {
        		sonarSound.at(x, y, 1.0f, 0.3f);
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
        
        public void drawLaser(float x1, float y1, CoreBuild core) {
        	var color = team.color.cpy().mul(1.3f).a(1f);
        	Vec2 startPos = new Vec2(x1, y1);
        	float x2 = core.x, y2 = core.y;
        	float rotation = Angles.angle(x1, y1, x2, y2);
        	
        	Vec2 arrowOffset = new Vec2(Angles.trnsx(rotation, arrowDistance), Angles.trnsy(rotation, arrowDistance)).add(startPos),
        		symbolPos =	new Vec2(Angles.trnsx(rotation, symbolDistance), Angles.trnsy(rotation, symbolDistance)).add(startPos)
        		;
        	float offsetDist = arrowOffset.dst(new Vec2(x2, y2));
        	int totalArrows = (int)(offsetDist / arrowSpacing);
        	var z = Draw.z();
        	Draw.z(Layer.bullet - 0.0001f);
        	
        	if (core.block.size > 2) {
            	Lines.stroke(strokeSize * warmup, color);
            	Lines.poly(symbolPos.x, symbolPos.y, core.block.size, 5f, totalProgress);
            	
            	/*for (int i = 0; i < core.block.size; i++) {
            		
            	}*/
        	}

        	for (int i = 0; i < totalArrows; i++) {
        		var arrowPos = new Vec2(Angles.trnsx(rotation, arrowSpacing * i), Angles.trnsy(rotation, arrowSpacing * i)).add(arrowOffset);
            	float sizer = Mathf.clamp((0.6f + Mathf.cos(i - (totalProgress / 8f))), 0.6f, 1f) * warmup;
            	
        		Draw.z(Layer.bullet - 0.0001f);
        		Draw.color(color);
        		Draw.scl(sizer);
        		Draw.rect(arrowRegion, arrowPos.x, arrowPos.y, rotation - 90);
        		Draw.scl();
        		Draw.color();
        	}
        	Draw.z(z);

            Draw.reset();
        }

		@Override
        public void draw(){
            var z = Draw.z();
            Draw.rect(baseRegion, x, y);
            Draw.color(team == Team.sharded ? Color.clear : team.color.cpy());
            Draw.rect(baseTeamRegion, x, y);
            Draw.color();
            Draw.z(Layer.turret);
            Draw.rect(region, x, y, rotateSpeed * totalProgress);
            Draw.color(team == Team.sharded ? Color.clear : team.color.cpy());
            Draw.rect(teamGlowRegion, x, y, rotateSpeed * totalProgress);
            Drawf.additive(glowRegion, team.color, 1f * (1f - glowMag + Mathf.absin(glowScl, glowMag)) * warmup, x, y, rotateSpeed * totalProgress, Layer.turretHeat);
            Draw.z(Layer.bullet - 0.0001f);
            Draw.color();

            var color = team.color.cpy().mul(1.3f).a(1f);
            Lines.stroke(strokeSize * warmup, color);
            Lines.circle(x, y, innerCircleRad);
            Lines.circle(x, y, outerCircleRad);
            Lines.lineAngle(x, y, rotateSpeed * totalProgress * lineRotateSpeed, (outerCircleRad) - 0.4f);

            Draw.color(color);
            Fill.circle(x, y, strokeSize * warmup);
            Draw.color();
            
            Draw.z(Layer.power + 0.01f);
            if (efficiency > 0.001f && player.team() == team)
	            for (var core : analizedCores) {
	            	drawLaser(x, y, core);
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
            Drawf.light(x, y, lightRadius * efficiency, team.color.cpy(), 0.8f);
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
