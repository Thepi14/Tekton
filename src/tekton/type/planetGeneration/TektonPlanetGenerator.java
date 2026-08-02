package tekton.type.planetGeneration;

import arc.graphics.Color;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec3;
import arc.struct.ObjectSet;
import arc.util.Log;
import arc.util.noise.Noise;
import arc.util.noise.Ridged;
import arc.util.noise.Simplex;
import mindustry.game.Team;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.type.Sector;
import tekton.content.TektonBlocks;
import tekton.content.TektonColor;
import tekton.content.TektonLoadouts;
import tekton.content.TektonPlanets;

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

	int noiseSeedAdd = -21593;
	int puddleSeedAdd = 12298;
	int waveSeedAdd = 27449;
	float maxSize = 0.62f, noiseMultipliyer = 1.1f;
	
	boolean generatedAcid = false;
	int currentAcids = 0;
	int maxAcids = 30;

	public TektonPlanetGenerator() {
		//super();
		baseSeed = 52487;
		seed = 42254;
		defaultLoadout = TektonLoadouts.corePrimal;
		block = TektonBlocks.methane;
		rand.setSeed((seed + baseSeed) * 143);

        Noise.setSeed(seed + puddleSeedAdd);
	}

    /*@Override
    public void generateSector(Sector sector) {

    }*/

    @Override
    public float getHeight(Vec3 position) {
    	int[] oceanicSectors = {21, 73, 72, 8, 60, 55, 4, 38, 74, 90, 31, 47, 84, 85, 10, 70, 7, 71, 51, 9, 77, 79, 28, 41, 64, 65};
    	
    	for (int sector : oceanicSectors) {
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

    @Override
    public void getColor(Vec3 position, Color out) {
        float biomeMask = Simplex.noise3d(seed, 3, 0.4, 1f, position.x, position.y, position.z);
        float patternMask = Simplex.noise3d(seed, 1, 0.6, 2f, position.x, position.y, position.z);

        var methaneAlbedo = 0.1f;

        if (
        		(rawHeight(position) > iceCoverage && rawHeight(position) < 1f - iceCoverage &&
        		(!(position.y > iceHeight - 1f) || !(position.y < 1f - iceHeight)) ||
        		Math.abs(position.y) > completeIceHeight)) {
        	out.set(biomeMask > 0.85f || Math.abs(position.y) > completeIceHeight ? Color.valueOf("d0e36f") : Color.valueOf("b6c953"));
    		return;
		}

    	/*if (getHeight(position) <= deepOceanLevel) {
    		out.set(TektonBlocks.deepMethane.mapColor.cpy().a(methaneAlbedo));
    		return;
        }
    	else */if (getHeight(position) <= oceanLevel) {
    		out.set(TektonBlocks.deepMethane.mapColor.cpy().a(methaneAlbedo));
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

        var vegetationCoverage = 0.39f;
        var vegetationHeight = 0.66f;

        if (
        		rawHeight(position) > vegetationCoverage && rawHeight(position) < 1f - vegetationCoverage &&
        		(!(position.y > vegetationHeight - 1f) || !(position.y < 1f - vegetationHeight))) {
        	
        	if (!generatedAcid) {
            	boolean acid = !(rand.random(1f) > 0.07f); //yes i know, bad programming habits but whatever it works
            	if (acid) {
            		currentAcids++;
            		acidPositions.add(position);
            	}
            	out.set(!acid ? lerpColor(Color.valueOf("2c3c4d"), Color.valueOf("506873"), Mathf.round(rawHeight(position) - 0.02f)) : acidColor);
            	
            	if (currentAcids >= maxAcids) {
            		generatedAcid = true;
            	}
        	}
        	else {
        		var pos = acidPositions.find(v -> { return v.x == position.x && v.y == position.y && v.z == position.z; });
        		out.set(pos == null ? lerpColor(Color.valueOf("2c3c4d"), Color.valueOf("506873"), Mathf.round(rawHeight(position) - 0.02f)) : acidColor);
        	}
        	
    		return;
		}

        if(biomeMask > 0.8f && patternMask < 0.4f) {
        	out.set(Color.valueOf("f0cdc9")); //silica
    		return;
		}
        if(biomeMask > 0.6f && patternMask < 0.6f) {
        	out.set(Color.valueOf("948881")); //diatomite
    		return;
		}
        if(biomeMask > 0.5f) {
        	out.set(Color.valueOf("5c483e")); //brown
    		return;
		}
        if(biomeMask > 0.4f && patternMask < 0.45f && position.dst(TektonPlanets.tekton.sectors.get(39).tile.v) > 0.2f) { //remove uranium from River sector
        	out.set(Color.valueOf("425442")); //uranium
    		return;
		}
        if(biomeMask > 0.1f) {
        	out.set(Color.valueOf("6e6761")); //ferric
    		return;
		}

        out.set(TektonColor.liquidMethane.cpy().a(methaneAlbedo));
    }
    
    private final Color acidColor = Color.valueOf("84ff00").a(0.1f);
    
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
