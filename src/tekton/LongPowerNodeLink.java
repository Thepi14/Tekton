package tekton;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;
import static mindustry.Vars.*;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.struct.EnumSet;
import arc.struct.IntSeq;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Fx;
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
import mindustry.ui.Bar;
import mindustry.world.blocks.power.LongPowerNode;
import mindustry.world.blocks.power.PowerGraph;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.Env;
import mindustry.world.modules.PowerModule;
import arc.func.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.world.*;
import tekton.content.TektonFx;
import tekton.content.TektonItems;
import tekton.content.TektonStatusEffects;

public class LongPowerNodeLink extends LongPowerNode {

    public float alpha = 0.9f, glowScale = 10f, glowIntensity = 0.5f;
    public Color color = TektonItems.nanoAlloy.color.cpy();
	
	public int returnLinkSize(Building entity) {
    	PowerModule power = entity.power;
    	int num = 0;
    	for (var node : power.links.toArray()) {
    		if (Vars.world.build(node).block() instanceof LongPowerNodeLink)
    			num++;
    	}
    	return num;
    }

	@SuppressWarnings("unchecked")
	public LongPowerNodeLink(String name) {
		super(name);
		
		configurable = true;
        swapDiagonalPlacement = true;
        destructible = true;
		
		config(Integer.class, (entity, value) -> {
            PowerModule power = entity.power;
            Building other = world.build(value);
            boolean contains = power.links.contains(value), valid = other != null && other.power != null;

            if(contains)
            {
                //unlink
                power.links.removeValue(value);
                if(valid) other.power.links.removeValue(entity.pos());

                PowerGraph newgraph = new PowerGraph();

                //reflow from this point, covering all tiles on this side
                newgraph.reflow(entity);

                if(valid && other.power.graph != newgraph){
                    //create new graph for other end
                    PowerGraph og = new PowerGraph();
                    //reflow from other end
                    og.reflow(other);
                }
            }
            else if(linkValid(entity, other) && valid && returnLinkSize(entity) < maxNodes)
            {
                power.links.addUnique(other.pos());

                if(other.team == entity.team){
                    other.power.links.addUnique(entity.pos());
                }

                power.graph.addGraph(other.power.graph);
            }
        });

        config(Point2[].class, (tile, value) -> {
            IntSeq old = new IntSeq(tile.power.links);
            
            //clear old
            for(int i = 0; i < old.size; i++){
                configurations.get(Integer.class).get(tile, old.get(i));
            }
            
            //set new
            for(Point2 p : value){
                configurations.get(Integer.class).get(tile, Point2.pack(p.x + tile.tileX(), p.y + tile.tileY()));
            }
        });
		
        autolink = false;
		solid = true;
        update = true;
        outputsPower = connectedPower = conductivePower = hasPower = consumesPower = true;
        sync = true;
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
            status = TektonStatusEffects.shortCircuit;
            statusDuration = 10f * 60f;
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
        
        glow = Core.atlas.find(name + "-glow", Core.atlas.find("tekton-line-node-beam-end"));
        laser = Core.atlas.find(name + "-beam", Core.atlas.find("tekton-line-node-beam"));
        laserEnd = Core.atlas.find(name + "-beam-end", Core.atlas.find("tekton-line-node-beam-end"));
    }
	
    @Override
    public void init(){
        super.init();

        clipSize = Math.max(clipSize, laserRange * tilesize);
    }
	
	public void drawLaser(float x1, float y1, float x2, float y2, float mult){
        Drawf.laser(laser, laserEnd, x1, y1, x2, y2, laserScale * (1f - glowMag + Mathf.absin(glowScl, glowMag) * mult));
    }
	
	@Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);

        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, (laserRange * tilesize), Pal.accent);
    }
	
	@Override
    public void setBars(){
        super.setBars();
        addBar("power", makePowerBalance());
        addBar("batteries", makeBatteryBalance());

        addBar("connections", entity -> new Bar(() ->
        Core.bundle.format("bar.powerlines", returnLinkSize(entity), maxNodes),
            () -> Pal.items,
            () -> (float)returnLinkSize(entity) / (float)maxNodes
        ));
    }
	
	@Override
	protected void getPotentialLinks(Tile tile, Team team, Cons<Building> others){
        if(!autolink) return;

        Boolf<Building> valid = other -> other != null && other.tile != tile && other.block.connectedPower && other.power != null &&
            //(other.block.outputsPower || other.block.consumesPower || other.block instanceof PowerNode) &&
            overlaps(tile.x * tilesize + offset, tile.y * tilesize + offset, other.tile, laserRange * tilesize) && other.team == team &&
            !graphs.contains(other.power.graph) &&
            !PowerNode.insulated(tile, other.tile) &&
            !(other instanceof LongPowerNodeLinkBuild obuild && (((LongPowerNodeLink)obuild.block).returnLinkSize(obuild)) >= ((LongPowerNodeLink)obuild.block).maxNodes) &&
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

    @Override
    public boolean linkValid(Building tile, Building link){
        return linkValid(tile, link, true);
    }
    
    @Override
    public boolean linkValid(Building tile, Building link, boolean checkMaxNodes){
        if(tile == link || link == null || tile.team != link.team)
        	return false;
        if(link.block instanceof LongPowerNodeLink node && overlaps(link, tile, (laserRange * tilesize) + (size / 2f))){
            return checkMaxNodes ? (returnLinkSize(link) < node.maxNodes) || link.power.links.contains(tile.pos()) : false;
        }
        return false;
    }
	
	public class LongPowerNodeLinkBuild extends LongPowerNodeBuild{
		
		public LongPowerNodeLinkBuild lastOther;
        
        @Override
        public boolean onConfigureBuildTapped(Building other){
            if(linkValid(this, other)){
                configure(other.pos());
                return false;
            }

            if(this == other){
                deselect();
                return false;
            }

            return true;
        }

        @Override
        public void created(){ // Called when one is placed/loaded in the world
            if(autolink && laserRange > maxRange) maxRange = laserRange;

            super.created();
        }
        
        @Override
        public void updateTile(){
            super.updateTile();
            
            warmup = Mathf.lerpDelta(warmup, returnLinkSize(this) > 0 ? 1f : 0f, 0.05f) * efficiencyScale();
        }
        
        @Override
        public float efficiencyScale() {
        	return power.status;
        }

        @Override
        public void placed(){
            if(net.client() || power.links.size > 0) return;

            /*getPotentialLinks(tile, team, other -> {
                if(!power.links.contains(other.pos())){
                    configureAny(other.pos());
                }
            });*/

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
        	var check = true;
            Drawf.circles(x, y, tile.block().size * tilesize / 2f + 1f + Mathf.absin(Time.time, 4f, 1f));
            Drawf.dashCircle(x, y, laserRange * tilesize, Pal.accent);

            if(drawRange){
                Drawf.circles(x, y, laserRange * tilesize);

                for(int x = (int)(tile.x - laserRange - 2); x <= tile.x + laserRange + 2; x++){
                    for(int y = (int)(tile.y - laserRange - 2); y <= tile.y + laserRange + 2; y++){
                        Building link = world.build(x, y);

                        if(link != this && linkValid(this, link, check)){
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
                    if(link != null && linkValid(this, link, check)){
                        Drawf.square(link.x, link.y, link.block.size * tilesize / 2f + 1f, Pal.place);
                    }
                });
            }
        }

        @Override
        public void draw(){
            //super.draw();
            Draw.rect(region, x, y);
            var z = Draw.z();
            
            Drawf.additive(glow, glowColor, (1f - glowMag + Mathf.absin(glowScl, glowMag)) * efficiency, x, y, 0f, Layer.blockAdditive);
            
            if(Mathf.zero(Renderer.laserOpacity) || isPayload()) return;
            
            Draw.z(Layer.power + 0.1f);
            setupColor(power.graph.getSatisfaction());
            
            for(int i = 0; i < power.links.size; i++){
                Building link = world.build(power.links.get(i));

                if(!linkValid(this, link)) continue;
                if(link.block instanceof LongPowerNodeLink && link.id >= id) continue;
                
                drawLaser(x, y, link.x, link.y, efficiencyScale());
            }
            
            Draw.z(z);
            Draw.reset();
        }
		
		@Override
        public void drawLight(){
            Drawf.light(x, y, lightRadius, glowColor, 0.7f * efficiency);
        }

        protected boolean linked(Building other){
            return power.links.contains(other.pos()) && other.block instanceof LongPowerNodeLink;
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