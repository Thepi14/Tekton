package tekton;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;
import static mindustry.Vars.net;
import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.struct.EnumSet;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.core.Renderer;
import mindustry.entities.Effect;
import mindustry.entities.TargetPriority;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.blocks.power.LongPowerNode;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.Env;
import tekton.content.TektonFx;

public class LongPowerNodeLink extends LongPowerNode {

	public LongPowerNodeLink(String name) {
		super(name);
		
        solid = true;
        update = true;
        consumesPower = true;
        outputsPower = false;
        connectedPower = true;
        conductivePower = true;
        hasPower = true;
    	consumePower(500f / 60f);
    	//consumePowerBuffered(100f);
        priority = TargetPriority.base;
        drawDisabled = false;
        underBullets = false;
        schematicPriority = 0;
        maxNodes = 2;
        envEnabled |= Env.space;
        flags = EnumSet.of(BlockFlag.battery);
        destroySound = Sounds.explosionbig;
        drawRange = true;
        destroyEffect = new MultiEffect(Fx.titanExplosion.wrap(Pal.lighterOrange), new Effect(30f, 160f, e -> {
            color(Pal.lighterOrange);
            stroke(e.fout() * 3f);
            float circleRad = 6f + e.finpow() * 60f;
            Lines.circle(e.x, e.y, circleRad);

            TektonFx.rand.setSeed(e.id);
            for(int i = 0; i < 16; i++){
                float angle = TektonFx.rand.random(360f);
                float lenRand = TektonFx.rand.random(0.5f, 1f);
                Lines.lineAngle(e.x, e.y, angle, e.foutpow() * 50f * TektonFx.rand.random(1f, 0.6f) + 2f, e.finpow() * 70f * lenRand + 6f);
            }
        }),
    	new Effect(300f, 300f, b -> {
            float intensity = 3f;

            color(Pal.lighterOrange, 0.7f);
            for(int i = 0; i < 4; i++){
            	TektonFx.rand.setSeed(b.id*2 + i);
                float lenScl = TektonFx.rand.random(0.5f, 1f);
                int fi = i;
                b.scaled(b.lifetime * lenScl, e -> {
                    randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int)(2.9f * intensity), 22f * intensity, (x, y, in, out) -> {
                        float fout = e.fout(Interp.pow5Out) * TektonFx.rand.random(0.5f, 1f);
                        float rad = fout * ((2f + intensity) * 2.35f);

                        Fill.circle(e.x + x, e.y + y, rad);
                        Drawf.light(e.x + x, e.y + y, rad * 2.5f, b.color, 0.5f);
                    });
                });
            }
        })
    	);
        
        baseExplosiveness = 7f;
        destroyBullet = new BulletType(0f, 1000f) {{
        	hitEffect = despawnEffect = Fx.none;
        	hitSize = 100f;
        	/*instantDisappear = true;
        	despawnHit = true;*/
        	lifetime = 0.2f * 60f;
        	hittable = false;
        	reflectable = false;
        	splashDamageRadius = 100;
        	splashDamage = 100;
        	scaledSplashDamage = true;
        	bulletInterval = 1f;
        	intervalBullet = new BulletType(0f, 0f) {{
            	hittable = false;
            	reflectable = false;
                lightning = 20;
                lightningLength = 10;
                lightningLengthRand = 10;
                lightningDamage = 100;
                lightningColor = Color.valueOf("ff7030");
                hitSound = Sounds.shockBlast;
            	despawnShake = 4f;
            }};
            status = StatusEffects.blasted;
            statusDuration = 5f;
            lightning = 20;
            lightningLength = 10;
            lightningLengthRand = 10;
            lightningDamage = 100;
            lightningColor = Color.valueOf("ff7030");
            hitSound = Sounds.shockBlast;
        	despawnShake = 4f;
        }};
        emitLight = true;
        lightRadius = 70f;
        lightColor = Color.valueOf("ff7030");
        fogRadius = 3;
	}
	
	@Override
    public void load(){
        super.load();
        
        //glow = Core.atlas.find(name + "-glow", Core.atlas.find("tekton-line-node-beam-end"));
        laser = Core.atlas.find(name + "-beam", Core.atlas.find("tekton-line-node-beam"));
        laserEnd = Core.atlas.find(name + "-beam-end", Core.atlas.find("tekton-line-node-beam-end"));
    }
	
	public void drawLaser(float x1, float y1, float x2, float y2, int size1, int size2){
        Drawf.laser(laser, laserEnd, x1, y1, x2, y2, laserScale * (1f - glowMag + Mathf.absin(glowScl, glowMag)));
    }
	
	@Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);

        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, (laserRange * tilesize), Pal.accent);
    }
	
	public class LongPowerNodeLinkBuild extends LongPowerNodeBuild{
		
		public LongPowerNodeLinkBuild lastOther;
		
        public boolean linkValid(Building tile, Building link){
            return linkValid(tile, link, true);
        }

        public boolean linkValid(Building tile, Building link, boolean checkMaxNodes){
            if(tile == link || link == null || !link.block.hasPower || !link.block.connectedPower || tile.team != link.team) return false;
            if(checkMaxNodes && tile.block instanceof LongPowerNodeLink && link.block instanceof LongPowerNodeLink && overlaps(link, tile, (laserRange * tilesize) + (size / 2f))){
                return true;
            }
            return false;
        }
        
        @Override
        public boolean onConfigureBuildTapped(Building other){
            if(linkValid(this, other)){
                configure(other.pos());
                return false;
            }

            if(this == other){ //double tapped
                if(other.power.links.size == 0){ //find links
                    Seq<Point2> points = new Seq<>();
                    getPotentialLinks(tile, team, link -> {
                        if(!insulated(this, link) && points.size < maxNodes){
                            points.add(new Point2(link.tileX() - tile.x, link.tileY() - tile.y));
                        }
                    });
                    configure(points.toArray(Point2.class));
                }else{ //clear links
                    configure(new Point2[0]);
                }
                deselect();
                return false;
            }

            return true;
        }@Override
        public void created(){ // Called when one is placed/loaded in the world
            if(autolink && laserRange > maxRange) maxRange = laserRange;

            super.created();
        }


        @Override
        public void placed(){
            if(net.client() || power.links.size > 0) return;

            getPotentialLinks(tile, team, other -> {
                if(!power.links.contains(other.pos())){
                    configureAny(other.pos());
                }
            });

            super.placed();
        }

        @Override
        public void dropped(){
            power.links.clear();
            updatePowerGraph();
        }

        @Override
        public void drawSelect(){
            super.drawSelect();

            if(!drawRange) return;

            Lines.stroke(1f);

            Draw.color(Pal.accent);
            //Drawf.circles(x, y, laserRange * tilesize);
            Drawf.dashCircle(x, y, laserRange * tilesize, Pal.accent);
            Draw.reset();
        }

        @Override
        public void drawConfigure(){
            Drawf.circles(x, y, tile.block().size * tilesize / 2f + 1f + Mathf.absin(Time.time, 4f, 1f));
            Drawf.dashCircle(x, y, laserRange * tilesize, Pal.accent);

            if(drawRange){
                Drawf.circles(x, y, laserRange * tilesize);

                for(int x = (int)(tile.x - laserRange - 2); x <= tile.x + laserRange + 2; x++){
                    for(int y = (int)(tile.y - laserRange - 2); y <= tile.y + laserRange + 2; y++){
                        Building link = world.build(x, y);

                        if(link != this && linkValid(this, link, false)){
                            boolean linked = linked(link);

                            if(linked){
                                Drawf.square(link.x, link.y, link.block.size * tilesize / 2f + 1f, Pal.place);
                            }
                        }
                    }
                }

                Draw.reset();
            }else{
                power.links.each(i -> {
                    var link = world.build(i);
                    if(link != null && linkValid(this, link, false)){
                        Drawf.square(link.x, link.y, link.block.size * tilesize / 2f + 1f, Pal.place);
                    }
                });
            }
        }

        @Override
        public void draw(){
            super.draw();

            if(Mathf.zero(Renderer.laserOpacity) || isPayload()) return;

            Draw.z(Layer.power);
            setupColor(power.graph.getSatisfaction());

            for(int i = 0; i < power.links.size; i++){
                Building link = world.build(power.links.get(i));

                if(!linkValid(this, link)) continue;

                if(link.block instanceof LongPowerNodeLink && link.id >= id) continue;

                drawLaser(x, y, link.x, link.y, size, link.block.size);
            }

            Draw.reset();
        }

        protected boolean linked(Building other){
            return power.links.contains(other.pos());
        }

        @Override
        public Point2[] config(){
            Point2[] out = new Point2[power.links.size];
            for(int i = 0; i < out.length; i++){
                out[i] = Point2.unpack(power.links.get(i)).sub(tile.x, tile.y);
            }
            return out;
        }
    }
}
