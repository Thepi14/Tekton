package tekton.type.planetGeneration;

import arc.func.Intf;
import arc.graphics.Color;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.struct.IntMap;
import arc.struct.ObjectIntMap;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.struct.ObjectIntMap.Entry;
import arc.util.Log;
import arc.util.Structs;
import arc.util.Tmp;
import arc.util.noise.Noise;
import arc.util.noise.Ridged;
import arc.util.noise.Simplex;
import mindustry.Vars;
import mindustry.ai.Astar;
import mindustry.content.Blocks;
import mindustry.content.Liquids;
import mindustry.content.Weathers;
import mindustry.ctype.UnlockableContent;
import mindustry.game.Rules;
import mindustry.game.Schematics;
import mindustry.game.Team;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.type.Liquid;
import mindustry.type.Sector;
import mindustry.type.Weather.WeatherEntry;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.TileGen;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.SteamVent;
import mindustry.world.blocks.environment.TallBlock;
import mindustry.world.blocks.environment.TreeBlock;
import mindustry.world.meta.Attribute;
import mindustry.world.meta.Env;
import tekton.content.TektonBlocks;
import tekton.content.TektonColor;
import tekton.content.TektonLiquids;
import tekton.content.TektonLoadouts;
import tekton.content.TektonPlanets;
import tekton.content.TektonWeathers;
import tekton.math.IntRef;
import tekton.type.world.TektonEnv;

import static mindustry.Vars.world;
import static tekton.content.TektonBlocks.*;

public class TektonPlanetGenerator extends PlanetGenerator {
	float sizeMultipliyer = 1.6f,
			oceanLevelChangerGeneral = -0.1f,
			beachLevel = 0.5f + oceanLevelChangerGeneral,
			subBeachLevel = 0.49f + oceanLevelChangerGeneral,
			oceanLevel = 0.48f + oceanLevelChangerGeneral,
			deepOceanLevel = 0.47f + oceanLevelChangerGeneral;

    float iceCoverage = 0.35f,
    		iceHeight = 0.25f,
    		completeIceHeight = 0.9f,
    		iceLevel = 0.42f;
    
    float vegetationCoverage = 0.39f,
    		vegetationHeight = 0.66f;

	int noiseSeedAdd = -21593;
	int puddleSeedAdd = 12298;
	int waveSeedAdd = 27449;
	float maxSize = 0.62f, noiseMultipliyer = 1.1f;
	
	boolean generatedAcid = false;
	int currentAcids = 0;
	int maxAcids = 30;
	
	int[] oceanicSectorsMesh = {21, 73, 72, 8, 60, 55, 4, 38, 74, 90, 31, 47, 84, 85, 10, 70, 7, 71, 51, 9, 77, 79, 28, 41, 64, 65, 29};
	Seq<IntRef> oceanicSectors = new Seq<IntRef>();

	public TektonPlanetGenerator() {
		//super();
		baseSeed = 52487;
		seed = 42254;
		defaultLoadout = TektonLoadouts.corePrimal;
		rand.setSeed((seed + baseSeed) * 143);

        Noise.setSeed(seed + puddleSeedAdd);
        
        for (int value : oceanicSectorsMesh) {
        	oceanicSectors.add(new IntRef(value));
        }
        
        oceanicSectors.addAll();
	}
	
	//sector generation
	
    public static float airThresh = 0.13f, airScl = 14;
	public static float liqThresh = 0.64f, liqScl = 87f;

	@Override
    public float getSizeScl(){
        return 2000 * 1.07f * 6f / 5f; //same as Erekir
    }
	
	@Override
    public void genTile(Vec3 position, TileGen tile){
        tile.floor = Blocks.ferricStone;

        tile.block = tile.floor.asFloor().wall; //inverted because for some reason the ferricCraters do not have a wall
        boolean oceanic = false;
        
        for (IntRef i : oceanicSectors) {
        	var sector = TektonPlanets.tekton.sectors.get(i.value);
        	if (sector.tile.v.x == position.x && sector.tile.v.y == position.y && sector.tile.v.z == position.z) {
        		oceanic = true;
        		break;
        	}
        }

        if(Ridged.noise3d(seed + 1, position.x, position.y, position.z, 2, airScl) > (oceanic ? airThresh * 0.2f : airThresh)){
            tile.block = Blocks.air;
        }
    }

    @Override
    protected void generate() {
    	Vec3 position = sector.tile.v;
    	boolean oceanic = false;
    	
    	for (IntRef id : oceanicSectors) {
    		if (sector.id == id.value) {
    			oceanic = true;
    			break;
    		}
    	}
    	
    	final boolean oc = oceanic;
    	
    	boolean ice = getIce(position), completeIce = getCompleteIce(position);
    	boolean neurospora = completeIce ? false : (position.y < vegetationHeight - 1f) || (position.y > 1f - vegetationHeight) ? true : false;

        cells(4);
        
        //ferric stone walls for more dense terrain
        pass((x, y) -> {
            if(floor == Blocks.ferricStone && noise(x + 50, y, 3, 0.4f, 13f, 1f) > (oc ? 0.59f * 2f : 0.59f)) {
                block = Blocks.ferricStoneWall;
            }
        });
        
        //brown, most abundant biome sometimes
        pass((x, y) -> {
            if (Math.abs(noise(x, y - x*1.2f, 7, 0.5f, 400f, 1f) - 0.5f) > 0.078f) {
                floor = brownStone;
                
                if (tiles.get(x, y).block() != Blocks.air)
            		block = brownStoneWall;
                
                if (noiseSeed(30186002, x, y - x*1.1f, 8f, 0.7f, 70f, 1f) > 0.52f)
                	floor = brownSand;
                else if (noiseSeed(260468082, x, y - x*1.05f, 6f, 0.74f, 70f, 1f) > 0.64f)
                	floor = brownIce;
            }
        });
    	
        //ice
    	if (completeIce || ice) {
        	if (completeIce) {
                pass((x, y) -> {
                	float noise = noise(x + 782, y + x*1.6f, 7, 0.8f, 280f, 1f);
                	if((noise > 0.37f && tiles.get(x, y).floor().wall != brownStoneWall) || noise > 0.2f) {
                    	floor = methaneSnow;
                        ore = Blocks.air;
                    }
                });
        	}
        	else if(ice) {
                pass((x, y) -> {
                	float noise = noise(x + 782, y + x*1.6f, 7, 0.8f, 280f, 1f);
                    if((noise > 0.8f && tiles.get(x, y).floor().wall != brownStoneWall) || noise > 0.58f) {
                    	floor = methaneSnow;
                        ore = Blocks.air;
                    }
                });
            }
        	
    		pass((x, y) -> {
                if (tiles.get(x, y).block() != Blocks.air && floor == methaneSnow) {
                	block = methaneIceWall;
                }
                
                if(noiseSeed(260468082, x + 550, y - x*1.05f, 6f, 0.74f, 70f, 1f) > 0.58f && floor == methaneSnow) {
                	floor = methaneIce;
                    ore = Blocks.air;
                }
            });
    	}
        
    	//emptyness
    	
    	float length = width/2.6f;
        Vec2 trns = Tmp.v1.trns(rand.random(360f), length);
        int
        spawnX = (int)(trns.x + width/2f), spawnY = (int)(trns.y + height/2f),
        endX = (int)(-trns.x + width/2f), endY = (int)(-trns.y + height/2f);
        float maxd = Mathf.dst(width/2f, height/2f);

        erase(spawnX, spawnY, 15);
        brush(pathfind(spawnX, spawnY, endX, endY, tile -> (tile.solid() ? 300f : 0f) + maxd - tile.dst(width/2f, height/2f)/10f, Astar.manhattan), 9);
        erase(endX, endY, 15);

        distort(10f, 12f);
        distort(5f, 7f);
    	
    	pass((x, y) -> {
            float max = 0;
            for(Point2 p : Geometry.d8){
                //TODO I think this is the cause of lag
                max = Math.max(max, Vars.world.getDarkness(x + p.x, y + p.y));
            }
            if(max > 0){
                block = floor.asFloor().wall;
                if(block == Blocks.air) block = Blocks.ferricStoneWall;
            }
        });
    	
    	//something

        inverseFloodFill(tiles.getn(spawnX, spawnY));

        //make sure enemies have room
        erase(endX, endY, 6);

        //TODO enemies get stuck on 1x1 passages.

        tiles.getn(endX, endY).setOverlay(Blocks.spawn);
        
        //uraninite
        pass((x, y) -> {
            if (Math.abs(noise(x + 999, y + 300f, 7, 0.65f, 360f, 1f) - 0.5f) > 0.12f && (floor == Blocks.ferricStone || floor == Blocks.ferricCraters)) {
                floor = uraniniteFloor;
                if(tiles.get(x, y).block() == Blocks.ferricStoneWall) {
                    block = uraniniteWall;
                }
                
                if(tiles.get(x, y).block() == Blocks.air && (noise(x + 999, y + 20, 2.38, 0.33f, 25f, 1f) > 0.8f || noise(x, y + 20, 2.38, 0.33f, 20f, 1f) > 0.86f)) {
                    floor = trinitite;
                }
            }
        });
        
        //methane puddles
        pass((x, y) -> {
        	float methaneNoise1 = noise(x + 100, y + 200, 2.38, 0.33f, 25f, 1f),
        		  methaneNoise2 = noise(x + 200, y + 500, 2.38, 0.33f, 20f, 1f);
        	
        	if (nearFloor(x, y, 5, uraniniteFloor.asFloor()))
        		return;
        	
            if(block == Blocks.air && (floor == Blocks.ferricStone || floor == Blocks.ferricCraters || floor == methaneSnow || floor == methaneIce) && (methaneNoise1 > 0.75f || methaneNoise2 > 0.8f)) {
                floor = deepMethane;
            }
            else if (block == Blocks.air && (floor == brownStone || floor == brownSand) && methaneNoise1 > 0.8f) {
                floor = deepMethane;
            }
        });

        //smooth methane
        median(3, 0.6, deepMethane);
        
    	//neurospora
    	if (neurospora) {
            pass((x, y) -> {
                if (Math.abs(noise(x, y + 300f, 7, 0.65f, 280f, 1f) - 0.5f) > 0.09f) {
                    floor = neurosporaFloor;
                    if(tiles.get(x, y).block() == Blocks.air && (noise(x + 999, y + 20, 2.38, 0.33f, 25f, 1f) > 0.8f || noise(x, y + 20, 2.38, 0.33f, 20f, 1f) > 0.86f)) {
                        floor = acidFloor;
                    }
                    
                    if (nearWall(x, y) && rand.chance(0.1f) && block == Blocks.air) {
                    	block = neurosporaWall;
                    }
                }
            });
        	
            median(2, 0.6, neurosporaFloor);
            
            //smooth acid
            median(3, 0.6, acidFloor);

            pass((x, y) -> {
                if(noise(x + 200, y + 600, 9.04f, 0.76f, 40f, 1f) > 0.66f && floor == neurosporaFloor) {
                    floor = neurosporaEvolvedFloor;
                }

            	if (block != Blocks.air && tiles.get(x, y).floor() == acidFloor)
            		block = Blocks.air;
            	else if (block != Blocks.air && tiles.get(x, y).floor().wall == neurosporaWall)
            		block = neurosporaWall;
            	
                if((floor == neurosporaFloor || floor == neurosporaEvolvedFloor) && block.isStatic()){
                    block = neurosporaWall;
                }
            });
    	}
    	
    	float diatomiteSpawnMaxDist = 50f;
    	
    	//diatomite, on top of every other biome because its too rare and too necessary
    	pass((x, y) -> {
    		if(noise(x + 777, y + 333, 5.76f, 0.575f, 140f, 1f) > 0.7f
    				- Math.min(Mathf.maxZero((diatomiteSpawnMaxDist - new Vec2(x, y).dst(new Vec2(spawnX, spawnY))) * 0.005f), 0.125f)) { //generate diatomite biome close to core
                floor = diatomite;
                if (tiles.get(x, y).block() != Blocks.air)
                	block = diatomiteWall;
                if (noise(x + 333, y + 777, 7f, 0.7f, 23f, 1f) < 0.3f || noise(x, y + 20, 2.38, 0.33f, 20f, 1f) < 0.3f)
                    floor = silicaSand;
            }
    	});
    	
        pass((x, y) -> {
        	blendSelective(x, y, silicaSand, 3.37f, darkSilicaSand, diatomite);
        });
    	
        //ocean
    	if (oceanic) {
    		pass((x, y) -> {
    			var noise = noise(x + 444, y + 444, 7f, 0.62f, 384f, 1f);
                if (noise > 0.425f) {
                	if (noise > 0.45f) {
                		floor = deepMethane;
                    	block = Blocks.air;
                    	ore = Blocks.air;
                	}
                }
            });
    	}
		
    	//do not let the methane touch the brown ice
        pass((x, y) -> {
        	if (tiles.get(x, y).floor() == brownIce && (near(x, y, 3, methaneIce) || near(x, y, 3, methaneSnow) || near(x, y, 3, methane) || near(x, y, 3, deepMethane)))
        		floor = brownStone;
        });
    	
        //turn into dark silica sand on contact with methane
        pass((x, y) -> {
        	if ((floor == diatomite || floor == silicaSand) && block != Blocks.air)
	        	for(Point2 p : Geometry.d4) {
	                Tile other = tiles.get(x + p.x, y + p.y);
	                if(other != null && (other.floor() == methane || other.floor() == deepMethane)){
	            		floor = darkSilicaSand;
	                    break;
	                }
	            }
        });
        
        //TODO: bad
        pass((x, y) -> {
            if(block != Blocks.air && floor.asFloor().wall == neurosporaWall){
                block = neurosporaWall;
            }
        });
    	
    	//ores
		pass((x, y) -> {
            if(block != Blocks.air){
                if(nearAir(x, y)){
                    if(block == diatomiteWall){
                    	float diatomiteNoise = noise(x + 78, y, 3, 0.7f, 20f, 1f);
                    	if (diatomiteNoise < 0.34f || diatomiteNoise > 0.76f) {
                    		block = zirconCrystal;
                    		ore = Blocks.air;
                    	}
                    	else if (diatomiteNoise < 0.4f || diatomiteNoise > 0.7f)
                    		block = zirconWall;
                    }
                    else if (block == brownStoneWall && noise(x + 78, y, 3, 1f, 37f, 1f) < 0.28f) { //most common ore in the brown biome
                        ore = wallOreTantalum;
                    }
                    else if (block == uraniniteWall && noise(x + 78, y, 4, 0.7f, 33f, 1f) > 0.75f) {
                        ore = wallOreUranium;
                    }
                    else if (block == neurosporaWall && noise(x + 78, y, 3, 0.7f, 20f, 1f) < 0.4f) {
                		block = neurosporaCluster;
                    }
                    else {
                    	float ironNoise = noise(x + 782, y, 4, 0.8f, 38f, 1f);
                    	if (block == Blocks.ferricStoneWall && ironNoise > 0.60f) { //most common ore in the ferric biome
                    		block = ferricIronWall;
                    	}
                    	else if((block == neurosporaWall || block == brownStoneWall) && ironNoise > 0.665f){
                            ore = wallOreIron;
                        }
                    }
                }
            }else if (!nearWall(x, y)) {
            	float tantalumNoise = noise(x + 150, y + x*2 + 100, 4, 0.86f, 55f, 1f);
            	if (tantalumNoise > 0.76f && (floor == brownStone || floor == brownSand || floor == Blocks.ferricStone || floor == neurosporaFloor)) {
            		if ((floor == brownStone || floor == brownSand) && tantalumNoise > 0.72f)
            			ore = oreTantalum;
            		else if (floor == Blocks.ferricStone && tantalumNoise > 0.76f)
            			ore = oreTantalum;
            		else if (floor == neurosporaFloor && tantalumNoise > 0.78f)
            			ore = oreTantalum;
                }
            	else if (noise(x + 999, y + 600 - x, 4, 0.63f, 45f, 1f) < 0.25f && floor == uraniniteFloor){
                    ore = oreUranium;
                }
            }
            else {
                if (noise(x + 222, y + x + 400 - x, 4, 0.63f, 45f, 1f) < 0.27f && floor == diatomite){
                    ore = oreZirconium;
                }
                else if ((noise(x + 150, y + x*1.4f + 200, 4, 0.7f, 22f, 1f) > 0.78f || noise(x + 150, y + x*1.4f + 200, 4, 0.7f, 22f, 1f) < 0.24f) && (floor == brownStone || floor == brownSand || floor == Blocks.ferricStone || floor == neurosporaFloor)) {
                    ore = oreIron;
                }
            }
            
            if (block == uraniniteWall && rand.chance(0.13) && nearAir(x, y) && !near(x, y, 3, uraniniteCrystal)){
                block = uraniniteCrystal;
                ore = Blocks.air;
            }
            
            if (block == diatomiteWall && rand.chance(0.1) && nearAir(x, y) && !near(x, y, 3, zirconCrystal)){
                block = zirconCrystal;
                ore = Blocks.air;
            }
            
            if (block == neurosporaWall && rand.chance(0.2) && nearAir(x, y) && !near(x, y, 3, neurosporaCluster)){
                block = neurosporaCluster;
                ore = Blocks.air;
            }
            else 
    		//add random clusters in neurospora biome
            if(floor == neurosporaFloor && rand.chance(0.005) && block == Blocks.air && !nearWall(x, y)){
                block = neurosporaCluster;
            }
            
            //remove acid from core
            if(floor == acidFloor && Mathf.within(x, y, spawnX, spawnY, 30f + noise(x, y, 2, 0.8f, 9f, 15f))){
                floor = neurosporaFloor;
            }
        });
		
		int minVents = rand.random(6, 9);
        int ventCount = 0;
        
        float[] ventsChances = new float[] {
        		0.0012f,
        		0.0024f,
        		0.0022f,
        		0.0017f,
        };
        
        SteamVent[] vents = new SteamVent[] {
    		(SteamVent)ferricStoneVent, 
    		(SteamVent)diatomiteVent, 
    		(SteamVent)brownStoneVent, 
    		(SteamVent)neurosporaVent
        };
        
        Block[][] ventsFilters = new Block[][] {
        	{
        		Blocks.ferricStone
        	},
        	{
        		diatomite
        	},
    		{
    			brownStone
    		},
    		{
    			neurosporaFloor, neurosporaEvolvedFloor
    		}
    	};
    	
        //vents
    	for (int type = 0; type < vents.length; type++) {
            outer:
                for(Tile tile : tiles) {
                    //do not generate on the core please
                    if(Mathf.within(tile.x, tile.y, spawnX, spawnY, 16f)){
                    	continue outer;
                    }
                	
                    var floor = tile.floor();
                    var vent = vents[type];
                    if(floor == vent.parent.asFloor() && rand.chance(ventsChances[type])) {
                        int radius = 2;
                        for(int x = -radius; x <= radius; x++) {
                            for(int y = -radius; y <= radius; y++) {
                                Tile other = tiles.get(x + tile.x, y + tile.y);
                                boolean hasFloor = false;
                                for (int i = 0; i < ventsFilters[type].length; i++) {
                                	if (other != null && other.floor() == ventsFilters[type][i]) {
                                		hasFloor = true;
                                		break;
                                	}
                                }
                                if(other == null || !hasFloor || other.block().solid) {
                                    continue outer;
                                }
                            }
                        }

                        ventCount++;
                        for(var pos : SteamVent.offsets) {
                            Tile other = tiles.get(pos.x + tile.x + 1, pos.y + tile.y + 1);
                            other.setFloor(vent);
                        
                        }
                    }
                }
    	}
        
    	//semi final
    	
		//remove ore facing liquids
        pass((x, y) -> {
            if (!nearFreeSurface(x, y, 2))
	            if (block != zirconCrystal && block.itemDrop != null) {
	            	if (block == ferricIronWall)
	            		block = Blocks.ferricStoneWall;
	            	else if (block == zirconWall)
	            		block = diatomiteWall;
	            }
	            else if (ore.asFloor().wallOre) {
	            	ore = Blocks.air;
	            }
        });

        //remove props near ores, they're too annoying
        pass((x, y) -> {
            if(ore.asFloor().wallOre || block.itemDrop != null || (block == Blocks.air && ore != Blocks.air)){
                removeWall(x, y, 3, b -> b instanceof TallBlock || b instanceof TreeBlock);
            }
        });
        
        //prevent acid from touching other biomes
        blend(acidFloor, neurosporaFloor, 4);

        //blend methane
		pass((x, y) -> {
			if (block == Blocks.air && floor == deepMethane && nearFreeSurface(x, y, 2)){
				floor = methane;
            	ore = Blocks.air;
        	}
		});
        
        trimDark();
    	
    	//final

        for(Tile tile : tiles){
            if(tile.overlay().needsSurface && !tile.floor().hasSurface()){
                tile.setOverlay(Blocks.air);
            }
        }
        
        
        scatter(Blocks.ferricStone, Blocks.ferricCraters, 0.01f);
        scatter(neurosporaFloor, neurosporaEvolvedFloor, 0.006f);

        decoration(0.017f);

        Vars.state.rules.env = sector.planet.defaultEnv;
        
        //TODO remove acid and methane around core.
        Schematics.placeLaunchLoadout(spawnX, spawnY);
        
        Vars.state.rules.waves = false;
        Vars.state.rules.hideSpawns = false;
    }
    
    @Override
    public void addWeather(Sector sector, Rules rules){
        rules.weather.clear();

        //apply weather based on terrain
        ObjectIntMap<Block> floorc = new ObjectIntMap<>();
        ObjectSet<UnlockableContent> content = new ObjectSet<>();

        for(Tile tile : world.tiles){
            if(world.getDarkness(tile.x, tile.y) >= 3){
                continue;
            }

            Liquid liquid = tile.floor().liquidDrop;
            if(tile.floor().itemDrop != null) content.add(tile.floor().itemDrop);
            if(tile.overlay().itemDrop != null) content.add(tile.overlay().itemDrop);
            if(liquid != null) content.add(liquid);

            if(!tile.block().isStatic()){
                floorc.increment(tile.floor());
                if(tile.overlay() != Blocks.air){
                    floorc.increment(tile.overlay());
                }
            }
        }

        //sort counts in descending order
        Seq<Entry<Block>> entries = floorc.entries().toArray();
        entries.sort(e -> -e.value);
        //remove all blocks occurring < 30 times - unimportant
        entries.removeAll(e -> e.value < 30);

        Block[] floors = new Block[entries.size];
        for(int i = 0; i < entries.size; i++){
            floors[i] = entries.get(i).key;
        }

        //bad contains() code, but will likely never be fixed
        boolean hasSpores = floors.length > 0 && content.contains(TektonLiquids.acid);
        boolean hasSnow = floors.length > 0 && (floors[0].name.contains("ice") || floors[0].name.contains("snow"));
        boolean hasRain = floors.length > 0 && !hasSpores  && !hasSnow && content.contains(TektonLiquids.methane) && !floors[0].name.contains("sand");
        boolean hasDesert = floors.length > 0 && !hasSnow && floors[0] == Blocks.sand;

        if(hasSnow){
            rules.weather.add(new WeatherEntry(TektonWeathers.methaneSnow));
        }

        if(hasRain){
            rules.weather.add(new WeatherEntry(TektonWeathers.methaneRain));
        }

        if(hasDesert){
            rules.weather.add(new WeatherEntry(TektonWeathers.darkSandstorm));
        }

        if(hasSpores){
            rules.weather.add(new WeatherEntry(TektonWeathers.neurosporaStorm));
            rules.weather.add(new WeatherEntry(TektonWeathers.acidRain));
        }
        
        //other more hazardous weathers must be added manually
    }

    public boolean nearFloor(int cx, int cy, int rad, Floor floor) {
        for(int x = -rad; x <= rad; x++) {
            for(int y = -rad; y <= rad; y++) {
                int wx = cx + x, wy = cy + y;
                if(Structs.inBounds(wx, wy, width, height) && Mathf.within(x, y, rad)) {
                    Tile other = tiles.getn(wx, wy);
                    if(other.floor() == floor) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean nearFreeSurface(int x, int y, float radius) {
    	int rad = (int)radius;
        boolean found = false;
        
        outer:
            for(int ex = -rad; ex <= rad; ex++){
                for(int ey = -rad; ey <= rad; ey++){
                    if(ex*ex + ey*ey > rad*rad) continue;
                    Tile tile = tiles.get(x + ex, y + ey);
                    if (tile == null)
                    	continue;

                    if(tile.floor().hasSurface() && tile.block() == Blocks.air) {
                        found = true;
                        break outer;
                    }
                }
            }

       return found;
    }
    
    public void blendSelective(int x, int y, Block block, float radius, Block floor, Block ignore) {
    	var in = tiles.get(x, y);
    	if(in.floor() == block || block == Blocks.air || in.floor() == ignore || (!floor.isFloor() && (in.block() == block || in.block() == ignore))) return;
    	
    	int rad = (int)radius;
        boolean found = false;

        outer:
        for(int ex = -rad; ex <= rad; ex++){
            for(int ey = -rad; ey <= rad; ey++){
                if(ex*ex + ey*ey > rad*rad) continue;
                Tile tile = tiles.get(x + ex, y + ey);
                if (tile == null)
                	continue;

                if(tile.floor() == block || tile.block() == block || tile.overlay() == block){
                    found = true;
                    break outer;
                }
            }
        }

        if(found){
            if(!floor.isFloor()){
                this.block = floor;
            }else{
                this.floor = floor;
            }
        }
    }

    protected float noiseSeed(int seed, float x, float y, double octaves, double falloff, double scl, double mag){
        Vec3 v = sector.rect.project(x, y);
        return Simplex.noise3d(seed, octaves, falloff, 1f / scl, v.x, v.y, v.z) * (float)mag;
    }
	
    @Override
    public void generateSector(Sector sector) {
    	
    }

    @Override
    public float getHeight(Vec3 position) {
    	for (int sector : oceanicSectorsMesh) {
    		if (position.dst(TektonPlanets.tekton.sectors.get(sector).tile.v) < 0.235f)
    			return oceanLevel;
    	}
    	
    	float pers = 1/4f;
    	var mul = 1.5f;

    	float oceanHeight = 0.65f;
    	float beachMethaneHeight = 0.62f;

        float noise = Simplex.noise3d(seed + noiseSeedAdd , 4, 0.9f, 1f, (position.z / 10f) * sizeMultipliyer, (position.y) * sizeMultipliyer, (position.x / 2f) * sizeMultipliyer);
        float puddleNoise = Simplex.noise3d(seed + puddleSeedAdd, 2, 0.8f, 1f / 4.5f, position.x * sizeMultipliyer * mul, position.y * sizeMultipliyer * mul, position.z * sizeMultipliyer * mul);
        float waveNoise = Ridged.noise3d(seed + waveSeedAdd, (position.y / 2f + noise * 4f) * sizeMultipliyer, 0, 0, pers);
        
        float secondaryPuddleNoise = Simplex.noise3d(seed + 60950, 3, 0.8f, 1f / 5f, -position.x * sizeMultipliyer - 5f, -position.y * sizeMultipliyer + 5f, position.z * sizeMultipliyer - 10f) - 0.037f;

        float actualNoise = noise * noiseMultipliyer;

        if (Math.abs(position.y) > completeIceHeight && (waveNoise >= beachMethaneHeight || puddleNoise >= beachMethaneHeight)) {
            return iceLevel;
        }

        if(waveNoise >= oceanHeight || puddleNoise >= oceanHeight || secondaryPuddleNoise >= oceanHeight) return oceanLevel;
        if(waveNoise >= beachMethaneHeight || puddleNoise >= beachMethaneHeight) return subBeachLevel;

        return Math.min(Math.max(actualNoise, oceanLevel), maxSize);
    }

    public float rawHeight(Vec3 position){
    	float noise = Simplex.noise3d(seed + noiseSeedAdd , 4, 0.9f, 1f, (position.z / 10f) * sizeMultipliyer, (position.y) * sizeMultipliyer, (position.x / 2f) * sizeMultipliyer);
        float actualNoise = noise * noiseMultipliyer;

        return Math.min(Math.max(actualNoise, oceanLevel), maxSize);
    }

    public float oceanDepth(Vec3 position){
        float noise = Simplex.noise3d(seed + noiseSeedAdd , 4, 0.9f, 1f, (position.z / 10f) * sizeMultipliyer, (position.y) * sizeMultipliyer, (position.x / 2f) * sizeMultipliyer);
        float actualNoise = noise * noiseMultipliyer;

        return Math.min(actualNoise, maxSize);
    }

    public Color lerpColor(Color colFrom, Color colTo, float progress) {
    	return new Color(
    			Mathf.lerp(colFrom.r, colTo.r, progress),
    			Mathf.lerp(colFrom.g, colTo.g, progress),
    			Mathf.lerp(colFrom.b, colTo.b, progress),
    			Mathf.lerp(colFrom.a, colTo.a, progress));
    }
    
    public boolean getIce(Vec3 position) {
    	return (rawHeight(position) > iceCoverage && rawHeight(position) < 1f - iceCoverage &&
        		(!(position.y > iceHeight - 1f) || !(position.y < 1f - iceHeight)) ||
        		Math.abs(position.y) > completeIceHeight);
    }
    
    public boolean getCompleteIce(Vec3 position) {
    	return Math.abs(position.y) > completeIceHeight;
    }

    @Override
    public void getColor(Vec3 position, Color out) {
        float biomeMask = Simplex.noise3d(seed, 3, 0.4, 1f, position.x, position.y, position.z);
        float patternMask = Simplex.noise3d(seed, 1, 0.6, 2f, position.x, position.y, position.z);

        if (getIce(position)) {
        	out.set(biomeMask > 0.85f || getCompleteIce(position) ? methaneSnowColor : methaneIceColor);
    		return;
		}

    	/*if (getHeight(position) <= deepOceanLevel) {
    		out.set(TektonBlocks.deepMethane.mapColor.cpy().a(methaneAlbedo));
    		return;
        }
    	else */if (getHeight(position) <= oceanLevel) {
    		out.set(methaneOceanColor);
    		return;
        }
        /*else if (getHeight(position) <= subBeachLevel) {
        	out.set(lerpColor(Color.valueOf("4f4646").a(methaneAlbedo), TektonColor.methane.cpy().a(methaneAlbedo), 0.35f));
    		return;
        }*/
        /*else if (getHeight(position) <= beachLevel) {
        	out.set(Color.valueOf("4f4646"));
    		return;
        }*/

        if (
        		rawHeight(position) > vegetationCoverage && rawHeight(position) < 1f - vegetationCoverage &&
        		(!(position.y > vegetationHeight - 1f) || !(position.y < 1f - vegetationHeight))) {
        	
        	if (!generatedAcid) {
            	boolean acid = !(rand.random(1f) > 0.07f); //yes i know, bad programming habits but whatever it works
            	if (acid) {
            		currentAcids++;
            		acidPositions.add(position);
            	}
            	out.set(!acid ? lerpColor(darkNeurosporaColor, neurosporaColor, Mathf.round(rawHeight(position) - 0.02f)) : acidColor);
            	
            	if (currentAcids >= maxAcids) {
            		generatedAcid = true;
            	}
        	}
        	else {
        		var pos = acidPositions.find(v -> { return v.x == position.x && v.y == position.y && v.z == position.z; });
        		out.set(pos == null ? lerpColor(darkNeurosporaColor, neurosporaColor, Mathf.round(rawHeight(position) - 0.02f)) : acidColor);
        	}
        	
    		return;
		}

        /*if(biomeMask > 0.8f && patternMask < 0.4f) {
        	out.set(lerpColor(darkNeurosporaColor, neurosporaColor, Mathf.round(rawHeight(position) - 0.02f))); //silica
    		return;
		}*/
        if(biomeMask > 0.6f && patternMask < 0.6f) {
        	out.set(lerpColor(silicaColor, diatomiteColor, Mathf.round(rawHeight(position) - 0.02f))); //diatomite
    		return;
		}
        if(biomeMask > 0.5f) {
        	out.set(lerpColor(brownColor, brownSandColor, Mathf.round(rawHeight(position) - 0.02f))); //brown
    		return;
		}
        if(biomeMask > 0.4f && patternMask < 0.45f && position.dst(TektonPlanets.tekton.sectors.get(39).tile.v) > 0.2f) { //remove uranium from River sector
        	out.set(lerpColor(darkUraniumColor, uraniumColor, Mathf.round(rawHeight(position) - 0.02f))); //uranium
    		return;
		}
        if(biomeMask > 0.1f) {
        	out.set(lerpColor(darkFerricColor, ferricColor, Mathf.round(rawHeight(position) - 0.02f))); //ferric
    		return;
		}

        out.set(TektonColor.liquidMethane.cpy().a(0.1f));
    }
    
    private final Color 
    silicaColor = Color.valueOf("b09e9d"), diatomiteColor = Color.valueOf("948881"), 
    brownSandColor = Color.valueOf("704f3e"), brownColor = Color.valueOf("5c483e"), 
    ferricColor = Color.valueOf("6e6761"), darkFerricColor = Color.valueOf("5e5853"), 
    uraniumColor = Color.valueOf("425442"), darkUraniumColor = Color.valueOf("304030"), 
    neurosporaColor = Color.valueOf("506873"), darkNeurosporaColor = Color.valueOf("2c3c4d"),
    acidColor = Color.valueOf("84ff00").a(0.1f), 
    methaneOceanColor = Color.valueOf("3e401f").a(0.1f), 
    methaneIceColor = Color.valueOf("b6c953"), methaneSnowColor = Color.valueOf("d0e36f")
    ;
    
    public ObjectSet<Vec3> acidPositions = new ObjectSet<Vec3>();
    
    //TODO: placeholder?
    static double metalDstScl = 0.25;
    Vec3 basePos = new Vec3(1, 0.0, 0.0);
    
    @Override
    public void getEmissiveColor(Vec3 position, Color out){
		var pos = acidPositions.find(v -> { return v.x == position.x && v.y == position.y && v.z == position.z; });
		if (pos != null) {
			out.set(acidColor).a(1f).mul(0.8f).toFloatBits();
			return;
		}
    	
        float dst = 999f, captureDst = 999f, lightScl = 0f;

        Object[] sectors = TektonPlanets.tekton.sectors.items;
        int size = TektonPlanets.tekton.sectors.size;

        for(int i = 0; i < size; i ++){
            var sector = (Sector)sectors[i];

            if(sector.hasEnemyBase() && !sector.isCaptured()){
                dst = Math.min(dst, position.dst(sector.tile.v) - (sector.preset != null ? sector.preset.difficulty/10f * 0.03f - 0.03f : 0f));
            }else if(sector.hasBase()){
                float cdst = position.dst(sector.tile.v);
                if(cdst < captureDst){
                    captureDst = cdst;
                    lightScl = sector.info.lightCoverage;
                }
            }
        }

        lightScl = Math.min(lightScl / 50000f, 1.3f);
        if(lightScl < 1f) lightScl = Interp.pow5Out.apply(lightScl);

        float freq = 0.05f;
        //TODO: once the old megabase returns, change it to 0.55f
        if(position.dst(basePos) < 0.3f ?

            dst*metalDstScl + Simplex.noise3d(seed + 1, 3, 0.4, 5.5f, position.x, position.y + 200f, position.z)*0.08f + ((basePos.dst(position) + 0.00f) % freq < freq/2f ? 1f : 0f) * 0.07f < 0.08f/* || dst <= 0.0001f*/ :
            dst*metalDstScl + Simplex.noise3d(seed, 3, 0.4, 9f, position.x, position.y + 370f, position.z)*0.06f < 0.045){

            out.set(Team.blue.color)
                .mul(0.8f + Simplex.noise3d(seed, 1, 1, 9f, position.x, position.y + 99f, position.z) * 0.4f)
                .lerp(Team.sharded.color, 0.2f*Simplex.noise3d(seed, 1, 1, 9f, position.x, position.y + 999f, position.z)).toFloatBits();
        }else if(captureDst*metalDstScl + Simplex.noise3d(seed, 3, 0.4, 9f, position.x, position.y + 600f, position.z)*0.07f < 0.05 * lightScl){
            out.set(Team.sharded.color).mul(0.7f + Simplex.noise3d(seed, 1, 1, 9f, position.x, position.y + 99f, position.z) * 0.4f)
                .lerp(Team.blue.color, 0.3f*Simplex.noise3d(seed, 1, 1, 9f, position.x, position.y + 999f, position.z)).toFloatBits();

        }
    }
    
    @Override
    public void onSectorCaptured(Sector sector){
        sector.planet.reloadMeshAsync();
    }

    @Override
    public void onSectorLost(Sector sector){
        sector.planet.reloadMeshAsync();
    }

    @Override
    public void beforeSaveWrite(Sector sector){
        sector.planet.reloadMeshAsync();
    }

	@Override
    public boolean isEmissive(){
        return true;
    }
}

/*public class TektonPlanetGenerator extends PlanetGenerator {
float sizeMultipliyer = 1.6f;

float
		oceanCoverage = 0.42f,
		beachCoverage = 0.51f,
		diatomiteCoverage = 0.4f,
		brownCoverage = 0.48f,
		uraniniteCoverage = 0.4f,
		neurosporaHeight = 0.66f,
		neurosporaCoverage = 0.53f;

int noiseSeedAdd = -7000,
		puddleSeedAdd = 7000,
		waveSeedAdd = 14000;

float maxSize = 0.65f;

public static final Rand rand = new Rand();

Block[] terrain = {Blocks.ferricStone, TektonBlocks.diatomite, TektonBlocks.brownStone, TektonBlocks.neurosporaFloor, TektonBlocks.uraniniteFloor, TektonBlocks.methaneIce, TektonBlocks.methane, TektonBlocks.deepMethane, TektonBlocks.darkSilicaSand};

float oceanHeight = 0.45f;

public float baseNoise(Vec3 position) { return Simplex.noise3d(seed + noiseSeedAdd , 4, 0.8f, 1f, (position.z / 10f) * sizeMultipliyer, (position.y) * sizeMultipliyer, (position.x / 2f) * sizeMultipliyer); }

																//seed, octaves, falloff, scl, mag
public float diatomiteNoise(Vec3 position) { return Simplex.noise3d(seed, 4, 0.6f, 1f, (position.z / 2f) * sizeMultipliyer, (position.y) * sizeMultipliyer, (position.x / 2f) * sizeMultipliyer); }
public float brownNoise(Vec3 position) { return Simplex.noise3d(seed - noiseSeedAdd, 4, 0.5f, 1.2f, (position.z / 8f) * sizeMultipliyer, (position.y) * sizeMultipliyer, (position.x / 2f) * sizeMultipliyer); }
public float uraniniteNoise(Vec3 position) { return Simplex.noise3d(seed + 4, 4, 0.6f, 1f, (position.z / 2f) * sizeMultipliyer, (position.y) * sizeMultipliyer, (position.x / 2f) * sizeMultipliyer); }
public float neurosporaNoise(Vec3 position) { return Simplex.noise3d(seed + 7, 4, 0.7f, 1f, (position.z / 4f) * sizeMultipliyer, (position.y) * sizeMultipliyer, (position.x / 4f) * sizeMultipliyer); }
public float waveNoise(Vec3 position) { return Simplex.noise3d(seed - waveSeedAdd, 4, 0.25f, 1.4f, (position.z / 3f) * sizeMultipliyer, (position.y / 3f) * sizeMultipliyer, (position.x / 3f) * sizeMultipliyer); }

public TektonPlanetGenerator() {
	//super();
	baseSeed = 14;
	seed = 71471;
	defaultLoadout = TektonLoadouts.corePrimal;
	block = TektonBlocks.methane;
	rand.setSeed((seed + baseSeed) * 17);

    Noise.setSeed(seed + puddleSeedAdd);
}

@Override
public float getHeight(Vec3 position) {
	float height = baseNoise(position);
	if (getBiome(position) == Biome.ocean || getBiome(position) == Biome.deepOcean)
		return oceanHeight;

    return height;
}

public enum Biome {
	ferric,
	diatomite,
	brown,
	uraninite,
	neurospora,
	ice,
	beach,
	ocean,
	deepOcean
}

  public Biome getBiome(Vec3 position) {
    	float diatomiteNoise = diatomiteNoise(position),
    			brownNoise = brownNoise(position),
    			uraniniteNoise = uraniniteNoise(position),
    			neurosporaNoise = neurosporaNoise(position),
    			waveNoise = waveNoise(position);

    	if (waveNoise > 1f - oceanCoverage)
    		return Biome.ocean;
    	if (waveNoise > 1f - beachCoverage)
    		return Biome.beach;

    	if ((!(position.y > neurosporaHeight - 1f) || !(position.y < 1f - neurosporaHeight)) && neurosporaNoise > 1f - neurosporaCoverage)
    		return Biome.neurospora;

    	if (diatomiteNoise > 1f - diatomiteCoverage)
    		return Biome.diatomite;
    	else if (uraniniteNoise > 1f - uraniniteCoverage)
    		return Biome.uraninite;
    	else if (brownNoise > 1f - brownCoverage)
    		return Biome.brown;

        return Biome.ferric;
  }

@Override
public Color getColor(Vec3 position) {
    var methaneAlbedo = 0.1f;
	float height = baseNoise(position);

	switch(getBiome(position)) {
    	case ferric:
    		return terrain[0].mapColor;
    	case diatomite:
    		return terrain[1].mapColor;
    	case brown:
    		return terrain[2].mapColor;
    	case uraninite:
    		return terrain[4].mapColor;
    	case neurospora:
    		return terrain[3].mapColor;
    	case ice:
    		return terrain[5].mapColor;
    	case beach:
    		return terrain[8].mapColor;
    	case ocean:
    		return terrain[6].mapColor;
    	case deepOcean:
    		return terrain[7].mapColor;
		default:
			return Color.red;
	}
}

public Color lerpColor(Color colFrom, Color colTo, float progress) {
	return new Color(
			Mathf.lerp(colFrom.r, colTo.r, progress),
			Mathf.lerp(colFrom.g, colTo.g, progress),
			Mathf.lerp(colFrom.b, colTo.b, progress),
			Mathf.lerp(colFrom.a, colTo.a, progress));
}
}*/
