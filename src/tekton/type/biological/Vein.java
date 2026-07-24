package tekton.type.biological;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;
import static mindustry.Vars.*;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.IntSet;
import arc.util.Eachable;
import arc.util.Nullable;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.entities.Puddles;
import mindustry.entities.TargetPriority;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.Liquid;
import mindustry.ui.Bar;
import mindustry.world.Tile;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;
import tekton.Drawt;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonLiquids;
import tekton.content.TektonVars;

public class Vein extends Conduit implements BiologicalBlock {
	
    static final float rotatePad = 6, hpad = rotatePad / 2f / 4f;
    static final float[][] rotateOffsets = {{hpad, hpad}, {-hpad, hpad}, {-hpad, -hpad}, {hpad, -hpad}};
    
	public float visualMaxHeat = 15f;
    public boolean splitBiopower = false;

    public @Nullable Liquid explosionPuddleLiquid = TektonLiquids.ammonia;
    public int explosionPuddles = 1;
    public float explosionPuddleRange = tilesize * 3f;
    public float explosionPuddleAmount = 140f;

    public Color glowColor = TektonColor.ammonia.cpy();
    public float alpha = 0.9f, glowScale = 15f, glowIntensity = 1f;

    public float deathTimer = 10f;
    private TextureRegion glowRegion;

	public Vein(String name) {
		super(name);

		botColor = Color.clear;
        envEnabled |= Env.space;
		buildVisibility = BuildVisibility.sandboxOnly;

		botColor = Color.clear;
		createRubble = drawCracks = false;
		update = true;
        sync = true;
        
        targetable = false;
		drawTeamOverlay = false;
		hideDetails = true;
		emitLight = true;
        lightColor = TektonColor.ammonia.cpy();
        lightRadius = 1f;
		priority = TargetPriority.wall;

		destroyEffect = TektonFx.biologicalAmmoniaDynamicExplosion;
	}

	@Override
	public void load() {
		super.load();

		glowRegion = Core.atlas.find(name + "-glow");
	}

    @Override
    public void setBars() {
        super.setBars();

        //TODO show number
        addBar("biopower", (VeinBuild entity) -> new Bar(
        		() -> Core.bundle.format("bar.gravity", (int)(Math.abs(entity.biopower) + 0.01f)),
        		() -> TektonColor.ammonia,
        		() -> entity.biopower / TektonVars.visualMaxGravity));
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{topRegions[0]};
    }

	public class VeinBuild extends ConduitBuild implements BiopowerBlock, BiopowerConsumer {
		public float biopower = 0f;
        public float[] sideBiopower = new float[4];
        public IntSet cameFrom = new IntSet();
        public long lastBiopowerUpdate = -1;
		public float totalProgress = Mathf.random(1000f);
	    public float currentDeathTimer = 0f;

        @Override
        public void drawLight() {
            super.drawLight();
			if (!emitLight) {
				return;
			}
            Drawf.light(x, y, (90f + Mathf.absin(5, 5f)) * currentGlow(), Tmp.c1.set(lightColor), 0.4f * currentGlow() * efficiency);
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
        public void updateTile() {
        	//super.updateTile();
            smoothLiquid = Mathf.lerpDelta(smoothLiquid, liquids.currentAmount() / liquidCapacity, 0.05f);
            health = block.health;

        	updateBiopower();
        	totalProgress += Time.delta * timeScale * efficiency;
        	if (((back() == null && left() == null && right() == null && front() == null) || (back() == null && left() == null && right() == null && front() != null)) && biopower == 0) {
				currentDeathTimer += Time.delta * timeScale * efficiency;
			} else {
				currentDeathTimer = 0f;
			}

        	if (currentDeathTimer > deathTimer) {
				kill();
			}

            if(liquids.currentAmount() > 0.0001f && timer(timerFlow, 1)){
                moveLiquidForward(true, liquids.current());
            }
        }

        @Override
        public void onDestroyed() {
            super.onDestroyed();
            createExplosion();
			Drawt.DrawAmmoniaDebris(x, y, size);
        }

        public void createExplosion() {
        	if(explosionPuddleLiquid != null){
                for(int i = 0; i < explosionPuddles; i++){
                    Tmp.v1.trns(Mathf.random(360f), Mathf.random(explosionPuddleRange));
                    Tile tile = world.tileWorld(x + Tmp.v1.x, y + Tmp.v1.y);
                    Puddles.deposit(tile, explosionPuddleLiquid, explosionPuddleAmount);
                }
                Puddles.deposit(tile, explosionPuddleLiquid, explosionPuddleAmount);
            }
        }

		@Override
        public void onProximityUpdate(){
            super.onProximityUpdate();

            int[] bits = buildBlending(tile, rotation, null, true);
            blendbits = bits[0];
            xscl = bits[1];
            yscl = bits[2];
            blending = bits[4];

            Building next = front(), prev = back();
            capped = next == null || next.team != team || !next.block.hasLiquids;
            backCapped = blendbits == 0 && (prev == null || prev.team != team || !prev.block.hasLiquids);
        }

        public void updateBiopower() {
            if(lastBiopowerUpdate == Vars.state.updateId) {
				return;
			}

            lastBiopowerUpdate = Vars.state.updateId;
            biopower = calculateBiopower(sideBiopower, cameFrom);
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
        public float biopower() {
            return biopower;
        }

        @Override
        public float biopowerFrac() {
            return (biopower / TektonVars.visualMaxGravity) / (splitBiopower ? 3f : 1);
        }

		@Override
		public float calculateBiopower(Building building, float[] sideBiopower, IntSet cameFrom) {
			return BiopowerBlock.super.calculateBiopower(building, sideBiopower, cameFrom);
		}

		public float currentGlow() {
			return Math.max(0.1f, Mathf.absin(totalProgress, glowScale, alpha) * glowIntensity);
		}

		@Override
		public void draw() {
			super.draw();

            Draw.blend(Blending.additive);
            Draw.color(glowColor);
            Draw.alpha(currentGlow() * efficiency * alpha);

            Draw.rect(glowRegion, x, y);

            Draw.blend();
            Draw.color();
            Draw.reset();
		}
		
		@Override
		protected void drawAt(float x, float y, int bits, int rotation, SliceMode slice, boolean under){
            float angle = rotation * 90f;
            if(under){
                Draw.rect(sliced(botRegions[bits], slice), x, y, angle);
            }else{
                int offset = yscl == -1 ? 3 : 0;

                int frame = liquids.current().getAnimationFrame();
                int gas = liquids.current().gas ? 1 : 0;
                float ox = 0f, oy = 0f;
                int wrapRot = (rotation + offset) % 4;
                TextureRegion liquidr = bits == 1 && padCorners ? rotateRegions[wrapRot][gas][frame] : renderer.fluidFrames[gas][frame];

                if(bits == 1 && padCorners){
                    ox = rotateOffsets[wrapRot][0];
                    oy = rotateOffsets[wrapRot][1];
                }

                //the drawing state machine sure was a great design choice with no downsides or hidden behavior!!! //son :wilted_flower:
                float xscl = Draw.xscl, yscl = Draw.yscl;
                Draw.scl(1f, 1f);
                //Drawf.liquid(sliced(liquidr, slice), x + ox, y + oy, smoothLiquid, liquids.current().color.write(Tmp.c1).a(1f));
                Draw.scl(xscl, yscl);

                Draw.rect(sliced(topRegions[bits], slice), x, y, angle);
            }
        }

        @Override
        public boolean canPickup(){
            return false;
        }
	}
}
