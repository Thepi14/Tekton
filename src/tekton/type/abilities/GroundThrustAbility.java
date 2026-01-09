package tekton.type.abilities;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import arc.Core;
import arc.audio.Sound;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Pixmaps;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.PixmapRegion;
import arc.graphics.g2d.TextureAtlas.AtlasRegion;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.abilities.Ability;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.MultiPacker;
import mindustry.graphics.MultiPacker.PageType;
import mindustry.graphics.Pal;
import mindustry.graphics.Trail;
import mindustry.type.UnitType;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import tekton.Tekton;
import tekton.math.*;
import tekton.content.TektonVars;

import static mindustry.Vars.*;

public class GroundThrustAbility extends Ability {
	protected boolean firstUpdate = false;
	protected float warmup = 0f, reloadProgress = 0f, thrusterWarmup = 0f, mainThrusterWarmup = 0f, splashTimer = 0f;
	
	public float thrusterShake = 0.4f;
	public float thrusterDistance = 7f;
	public float thrusterYOffset = 0f;
    public float warmupSpeed = 0.1f, thrustWarmupSpeed = 0.05f, mainThrustWarmupSpeed = 0.05f;
	
	public BulletType thrustBulletType;
	public float thrustReload = 4f;
	public float thrusterBulletOffset = 32f;
    
    public float mainThrusterDistance = 16f;
    public float mainThrusterBulletOffset = 21f;
    public float mainThrusterMinSpeed = 0.05f;
	
	public float groundSpeedMultiplier = 0.5f;
	public float groundHealthMultiplier = 0.8f;
	public float groundDamageMultiplier = 0.8f;
	public float groundReloadMultiplier = 0.8f;
	
	public Effect 
	smokeEffect = new Effect(50, e -> {
		if (e.data instanceof Unit unit) {
	        color(Tmp.c1.set(e.color).mul(1.5f));
	        Fx.rand.setSeed(e.id);
	        for(int i = 0; i < 3; i++){
	            Fx.v.trns(e.rotation + Fx.rand.range(40f), Fx.rand.random(6f * e.finpow()));
	            Fill.circle(e.x + Fx.v.x + Fx.rand.range(4f), e.y + Fx.v.y + Fx.rand.range(4f), Math.min(e.fout(), e.fin() * e.lifetime / 8f) * unit.hitSize / 28f * 3f * Fx.rand.random(0.8f, 1.1f) + 0.3f);
	        }
		}
    }).layer(Layer.debris).followParent(false), 
	sparkEffect = Fx.none;
	
	public float thrustSoundVolume = 0.5f;
	public Sound thrustSound = Sounds.missileTrail;
	
	public Color heatColor = Pal.turretHeat;
    public TextureRegion thrusterRegion;
    public TextureRegion thrusterHeatRegion;
    public TextureRegion thrusterOutlineRegion;
    public TextureRegion mainThrusterRegion;
    public TextureRegion mainThrusterHeatRegion;
    public TextureRegion mainThrusterOutlineRegion;
    
    //huge nerf lol
    public boolean drawHarmlessThrusters = false;
    public float engineRadius = 5.6f;
    
    private Color trailColor = Blocks.water.mapColor.cpy().mul(1.5f);
    
    private int thrusterPosID = 0;

    public GroundThrustAbility() {
    	
    }
    
    public GroundThrustAbility(boolean harmless) {
    	drawHarmlessThrusters = harmless;
    }
    
    public String getBundle(){
        var type = getClass();
        return "ability." + (type.isAnonymousClass() ? type.getSuperclass() : type).getSimpleName().replace("Ability", "").toLowerCase();
    }

    @Override
    public void addStats(Table t) {
        //super.addStats(t);
		float descriptionWidth = 350f;
        t.add(Core.bundle.get(getBundle() + ".description")).wrap().width(descriptionWidth);
        t.row();
        t.add(abilityStat("speedmultiplierground", Strings.autoFixed((int)(groundSpeedMultiplier * 100f), 3)));
        t.row();
        t.add(abilityStat("healthmultiplierground", Strings.autoFixed((int)(groundHealthMultiplier * 100f), 3)));
        t.row();
        t.add(abilityStat("damagemultiplierground", Strings.autoFixed((int)(groundDamageMultiplier * 100f), 3)));
        t.row();
        t.add(abilityStat("reloadmultiplierground", Strings.autoFixed((int)(groundReloadMultiplier * 100f), 3)));
        t.row();
    }

	@Override
	public void init(UnitType type){
		firstUpdate = true;
	}
	
	@Override
    public void update(Unit unit) {
        super.update(unit);
        
        Floor floor = unit.floorOn();
        var type = unit.type;
		thrusterPosID = unit.id;
		
		if (!unit.isImmune(floor.status))
			unit.apply(floor.status);
        
        if (TektonVars.getMisc(unit.id, "leftTrail") == null)
        	TektonVars.addMisc(new IDObj(unit.id, "leftTrail", new Trail(1)));
		Trail lTrail = (Trail)TektonVars.getMisc(unit.id, "leftTrail").value;
        
        if (TektonVars.getMisc(unit.id, "rightTrail") == null)
        	TektonVars.addMisc(new IDObj(unit.id, "rightTrail", new Trail(1)));
		Trail rTrail = (Trail)TektonVars.getMisc(unit.id, "rightTrail").value;
		
        if (firstUpdate) {
        	warmup = isOnLiquid(unit) ? 0f : 1f;
        	firstUpdate = false;

        	lTrail.clear();
        	rTrail.clear();
        }

        if (TektonVars.getVec2F(thrusterPosID) == null)
        	TektonVars.addVec2F(new RSeq<Vec2F>(thrusterPosID));
        Seq<Vec2F> thrusterPositions = TektonVars.getVec2F(thrusterPosID).seq;
    	thrusterPositions.clear();
        
        boolean flying = unit.isFlying();
        for(int i = 0; i < 2; i++){
            Trail t = i == 0 ? lTrail : rTrail;
            t.length = type.trailLength;

            int sign = i == 0 ? -1 : 1;
            float cx = Angles.trnsx(unit.rotation - 90, type.waveTrailX * sign, type.waveTrailY) + unit.x, cy = Angles.trnsy(unit.rotation - 90, type.waveTrailX * sign, type.waveTrailY) + unit.y;
            t.update(cx, cy, world.floorWorld(cx, cy).isLiquid && !flying ? 1 : 0);
        }
        
        for (int i = 0; i < 4; i++) {
        	var angleOffset = (90f * i);
        	var rotation = unit.rotation - 90f - angleOffset;
        	
        	float 
	        	posX = unit.x + Angles.trnsx(rotation, thrusterDistance * warmup, thrusterDistance * warmup) + Angles.trnsx(unit.rotation - 90f, 0, thrusterYOffset), 
	        	posY = unit.y + Angles.trnsy(rotation, thrusterDistance * warmup, thrusterDistance * warmup) + Angles.trnsy(unit.rotation - 90f, 0, thrusterYOffset);
        	
        	thrusterPositions.add(new Vec2F(posX, posY, rotation));
        }
        
        if (warmup >= 0.85f)
        	reloadProgress += Time.delta * warmup;
        
    	warmup = Mathf.lerpDelta(warmup, isOnLiquid(unit) ? 0f : 1f, warmupSpeed);
    	thrusterWarmup = Mathf.lerpDelta(thrusterWarmup, isOnLiquid(unit) ? 0f : 1f, thrustWarmupSpeed);
    	mainThrusterWarmup = Mathf.lerpDelta(mainThrusterWarmup, unit.moving() && unit.speed() > type.speed * mainThrusterMinSpeed ? 1f : 0f, mainThrustWarmupSpeed);

        //act as a naval unit
        Floor on = unit.isFlying() ? Blocks.air.asFloor() : floor;
        unit.speedMultiplier *= (on.shallow ? 1f : 1.3f);
        
        //nerf it so it's not too strong
        if (warmup >= 0.9f) {
            unit.speedMultiplier *= groundSpeedMultiplier;
            unit.damageMultiplier *= groundDamageMultiplier;
            unit.reloadMultiplier *= groundReloadMultiplier;
        }
        
        if (reloadProgress >= thrustReload) {
        	if (thrustBulletType != null) {
        		for (Vec2F pos : thrusterPositions) {
	            	float 
	        		bulletX = pos.x + Angles.trnsx(pos.rotation + 45f, thrusterBulletOffset), 
	    			bulletY = pos.y + Angles.trnsy(pos.rotation + 45f, thrusterBulletOffset);
	            	
	            	if (!drawHarmlessThrusters) {
		            	thrustBulletType.create(unit, unit.team, bulletX, bulletY, pos.rotation + 45f);
		            	thrustBulletType.shootEffect.create(bulletX, bulletY, pos.rotation + 45f, thrustBulletType.hitColor, this);
		            	thrustBulletType.smokeEffect.create(bulletX, bulletY, pos.rotation + 45f, thrustBulletType.hitColor, this);
	            	}
	            	
	            	if (smokeEffect != null && smokeEffect != Fx.none)smokeEffect.at(bulletX, bulletY, pos.rotation + 45f, world.floor((int)(bulletX / 8), (int)(bulletY / 8)).mapColor, unit);
	        	}
	        	if (unit.moving() && unit.speed() > type.speed * mainThrusterMinSpeed) {
	                float 
	                mainX = unit.x + Angles.trnsx(unit.rotation - 180f, (mainThrusterDistance * warmup) + mainThrusterBulletOffset), 
	                mainY = unit.y + Angles.trnsy(unit.rotation - 180f, (mainThrusterDistance * warmup) + mainThrusterBulletOffset);
	                
	                if (!drawHarmlessThrusters) {
		            	thrustBulletType.create(unit, unit.team, mainX, mainY, unit.rotation - 180f);
		            	thrustBulletType.shootEffect.create(mainX, mainY, unit.rotation - 180f, thrustBulletType.hitColor, this);
		            	thrustBulletType.smokeEffect.create(mainX, mainY, unit.rotation - 180f, thrustBulletType.hitColor, this);
	                }
	            	
	            	if (smokeEffect != null && smokeEffect != Fx.none)smokeEffect.at(mainX, mainY, unit.rotation - 180f, world.floor((int)(mainX / 8), (int)(mainY / 8)).mapColor, unit);
	        	}
        	}
        	reloadProgress = 0f;
        }
        
    	if(!headless && thrustSound != Sounds.none){
            control.sound.loop(thrustSound, unit, thrustSoundVolume * warmup);
        }

    	if (smokeEffect != null && smokeEffect != Fx.none) smokeEffect.at(unit);
    	
        if (unit.moving()) {
        	if (sparkEffect != null && sparkEffect != Fx.none) sparkEffect.at(unit);
        }
        
        if((splashTimer += Mathf.dst(unit.deltaX(), unit.deltaY())) >= (7f + unit.hitSize()/8f)){
            floor.walkEffect.at(unit.x, unit.y, unit.hitSize() / 8f, floor.mapColor);
            splashTimer = 0f;
        }
        
        Mathf.clamp(warmup);
    }
	
    public boolean isOnLiquid(Unit unit) {
        Tile tile = unit.tileOn();
        return tile != null && unit.floorOn().isLiquid /*&& unit.tileOn().block().isAir()*/;
    }
    
    protected void makeOutline(MultiPacker packer, TextureRegion region, Color outlineColor, int outlineRadius){
    	if(region instanceof AtlasRegion atlas && !Core.atlas.has(atlas.name + "-outline")){
            String regionName = atlas.name;
            Pixmap outlined = Pixmaps.outline(Core.atlas.getPixmap(region), outlineColor, outlineRadius);

            Drawf.checkBleed(outlined);

            packer.add(PageType.main, regionName + "-outline", outlined);
            outlined.dispose();
        }
    }
    
    @Override
    public void draw(Unit unit) {
        if(unit.inFogTo(Vars.player.team())) return;
        var type = unit.type;
        if (TektonVars.getMisc(unit.id, "leftTrail") == null || TektonVars.getMisc(unit.id, "rightTrail") == null || TektonVars.getVec2F(thrusterPosID) == null) {
        	return;
        }

        final Seq<Vec2F> thrusterPositions = TektonVars.getVec2F(thrusterPosID).seq;
        final Trail lTrail = (Trail)TektonVars.getMisc(unit.id, "leftTrail").value;
        final Trail rTrail = (Trail)TektonVars.getMisc(unit.id, "rightTrail").value;
        
        if(thrusterRegion == null || thrusterHeatRegion == null || mainThrusterRegion == null || mainThrusterHeatRegion == null){
        	thrusterRegion = Core.atlas.find(unit.type.name + "-thruster", unit.type.region);
        	thrusterHeatRegion = Core.atlas.find(unit.type.name + "-thruster-heat", thrusterRegion);
        	
        	mainThrusterRegion = Core.atlas.find(unit.type.name + "-main-thruster", thrusterRegion);
        	mainThrusterHeatRegion = Core.atlas.find(unit.type.name + "-main-thruster-heat", thrusterHeatRegion);
        }
        
    	thrusterOutlineRegion = Core.atlas.find(unit.type.name + "-thruster-outline");
    	mainThrusterOutlineRegion = Core.atlas.find(unit.type.name + "-main-thruster-outline");
    	
        if (!thrusterOutlineRegion.found()){
        	thrusterOutlineRegion = Core.atlas.find(unit.type.name + "-thruster", thrusterRegion);
    		makeOutline(Tekton.packer, thrusterOutlineRegion, type.outlineColor, type.outlineRadius);
    		thrusterOutlineRegion = Core.atlas.find(unit.type.name + "-thruster-outline", thrusterRegion);
        }
    	
    	
    	if (!mainThrusterOutlineRegion.found()){
    		mainThrusterOutlineRegion = Core.atlas.find(unit.type.name + "-main-thruster", mainThrusterRegion);
    		makeOutline(Tekton.packer, mainThrusterOutlineRegion, type.outlineColor, type.outlineRadius);
    		mainThrusterOutlineRegion = Core.atlas.find(unit.type.name + "-thruster-outline", mainThrusterRegion);
        }
        
	    if(type.trailLength > 0 && !type.naval && (unit.isFlying() || !type.useEngineElevation)){
	    	type.drawTrail(unit);
	    }

    	var z = Draw.z();

        for (Vec2F pos : thrusterPositions) {
        	float 
        	engineX = pos.x + Angles.trnsx(pos.rotation + 45f, thrusterBulletOffset + ((engineRadius / 2f) * mainThrusterWarmup)), 
			engineY = pos.y + Angles.trnsy(pos.rotation + 45f, thrusterBulletOffset + ((engineRadius / 2f) * mainThrusterWarmup));
        	
        	Draw.z(type.groundLayer - 0.01f);
        	//Draw.alpha(1f);
        	Draw.color(isOnLiquid(unit) ? unit.floorOn().mapColor.cpy().lerp(Color.white, warmup) : Color.white);
        	Draw.alpha(warmup);
            Draw.rect(thrusterRegion, pos.x, pos.y, pos.rotation - 45f);
            
            if (!drawHarmlessThrusters)
            	Drawf.additive(thrusterHeatRegion, heatColor, thrusterWarmup * warmup, pos.x, pos.y, pos.rotation - 45f, type.groundLayer - 0.009f);
            else
            	drawEngine(unit, engineX, engineY, engineRadius * warmup, pos.rotation + 45f);
            
        	Draw.z(type.groundLayer - 0.011f);
        	Draw.alpha(warmup);
            Draw.rect(thrusterOutlineRegion, pos.x, pos.y, pos.rotation - 45f);
        }
    	
        float 
        mainX = unit.x + Angles.trnsx(unit.rotation - 180f, mainThrusterDistance * warmup), 
        mainY = unit.y + Angles.trnsy(unit.rotation - 180f, mainThrusterDistance * warmup);
        
        float 
        engineX = unit.x + Angles.trnsx(unit.rotation - 180f, (mainThrusterDistance * warmup) + mainThrusterBulletOffset + ((engineRadius / 2f) * mainThrusterWarmup)), 
		engineY = unit.y + Angles.trnsy(unit.rotation - 180f, (mainThrusterDistance * warmup) + mainThrusterBulletOffset + ((engineRadius / 2f) * mainThrusterWarmup));

    	Draw.z(type.groundLayer - 0.01f);
    	//Draw.alpha(1f);
    	Draw.color(isOnLiquid(unit) ? unit.floorOn().mapColor.cpy().lerp(Color.white, warmup) : Color.white);
    	Draw.alpha(warmup);
        Draw.rect(mainThrusterRegion, mainX, mainY, unit.rotation + 90f);
        
        if (!drawHarmlessThrusters)
        	Drawf.additive(mainThrusterHeatRegion, heatColor, mainThrusterWarmup * warmup, mainX, mainY, unit.rotation + 90f, type.groundLayer - 0.009f);
        else
        	drawEngine(unit, engineX, engineY, engineRadius * mainThrusterWarmup * warmup, unit.rotation - 180f);
        
    	Draw.z(type.groundLayer - 0.011f);
    	Draw.alpha(warmup);
        Draw.rect(mainThrusterOutlineRegion, mainX, mainY, unit.rotation + 90f);
        
        Draw.color();
    	Draw.alpha(1f);
        
        if (warmup >= 0.85f && !Vars.state.isPaused() && unit.moving() && unit.speed() > type.speed * mainThrusterMinSpeed)
        	Effect.shake(thrusterShake * warmup, 1f, unit);
        
        Draw.z(Layer.debris);

        Floor floor = unit.tileOn() == null ? Blocks.air.asFloor() : unit.tileOn().floor();
        Color color = Tmp.c1.set(floor.mapColor.equals(Color.black) ? Blocks.water.mapColor : floor.mapColor).mul(1.5f);
        trailColor.lerp(color, Mathf.clamp(Time.delta * 0.04f));

        if (lTrail != null)
        	lTrail.draw(trailColor, type.trailScl);
        if (rTrail != null)
        	rTrail.draw(trailColor, type.trailScl);
        
        Draw.z(z);
    }
    
    public String abilityStat(String stat, Object... values) {
        return Core.bundle.format("ability.stat." + stat, values);
    }
    
    public void drawEngine(Unit unit, float x, float y, float radius, float rotation) {
        var z = Draw.z();
        
    	UnitType type = unit.type;
        Color color = type.engineColor == null ? unit.team.color : type.engineColor;
        
        float rad = (radius + Mathf.absin(Time.time, 2f, radius / 4f));

        Draw.color(color);
    	Draw.alpha(warmup);
        Draw.z(type.groundLayer - 0.012f);
        Fill.circle(
            x,
            y,
            rad
        );
        Draw.color(type.engineColorInner);
    	Draw.alpha(warmup);
        Fill.circle(
            x - Angles.trnsx(rotation, rad / 4f),
            y - Angles.trnsy(rotation, rad / 4f),
            rad / 2f
        );
        
        Draw.z(z);
    }
}
