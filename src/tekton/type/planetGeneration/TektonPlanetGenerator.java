package tekton.type.planetGeneration;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec3;
import arc.util.Log;
import arc.util.noise.Noise;
import arc.util.noise.Ridged;
import arc.util.noise.Simplex;
import mindustry.content.Blocks;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.type.Sector;
import mindustry.world.Block;
import mindustry.world.TileGen;
import tekton.content.*;

public class TektonPlanetGenerator extends PlanetGenerator {
	float sizeMultipliyer = 1.6f,
			oceanLevelChangerGeneral = -0.1f,
			beachLevel = 0.46f + oceanLevelChangerGeneral,
			subBeachLevel = 0.45f + oceanLevelChangerGeneral,
			oceanLevel = 0.44f + oceanLevelChangerGeneral,
			deepOceanLevel = 0.43f + oceanLevelChangerGeneral;

    float iceCoverage = 0.35f, 
    		iceHeight = 0.25f, 
    		completeIceHeight = 0.9f,
    		iceLevel = 0.42f;
	
	int noiseSeedAdd = -7000;
	int puddleSeedAdd = 7000;
	int waveSeedAdd = 14000;
	float maxSize = 0.65f, noiseMultipliyer = 1.2f;
	
	public static final Rand rand = new Rand();

	public TektonPlanetGenerator() {
		//super();
		baseSeed = 14;
		seed = 71471;
		defaultLoadout = TektonLoadouts.corePrimal;
		block = TektonBlocks.methane;
		rand.setSeed((seed + baseSeed) * 17);

        Noise.setSeed(seed + puddleSeedAdd);
	}
    
    /*@Override
    public void generateSector(Sector sector) {
    	
    }*/
	
	/*@Override
    public boolean isEmissive(){
        return true;
    }*/

    @Override
    public float getHeight(Vec3 position) {
    	float pers = 1/4f;
    	
    	float deepOceanHeight = 0.8f;
    	float oceanHeight = 0.71f;
    	float beachMethaneHeight = 0.68f;
    	float beachHeight = 0.64f;
    	
        float noise = Simplex.noise3d(seed + noiseSeedAdd , 4, 0.9f, 1f, (position.z / 10f) * sizeMultipliyer, (position.y) * sizeMultipliyer, (position.x / 2f) * sizeMultipliyer);
        float puddleNoise = Simplex.noise3d(seed + puddleSeedAdd, 2, 0.9f, 1f / 12f, (position.x) * sizeMultipliyer, (position.y) * sizeMultipliyer, (position.z) * sizeMultipliyer);
        //float puddleNoise = Noise.snoise3((position.x) * sizeMultipliyer, (position.y) * sizeMultipliyer, (position.z) * sizeMultipliyer, 1f, 1f);
        float waveNoise = Ridged.noise3d(seed + waveSeedAdd, (position.y / 2f + noise * 4f) * sizeMultipliyer, 0, 0, pers);
        //float waveNoise = Simplex.noise3d(seed + waveSeedAdd, 2, 0.9f, 2.5f, (position.x / 12f) * sizeMultipliyer, (position.y / 2f + noise * 4f) * sizeMultipliyer, (position.z / 12f) * sizeMultipliyer);
        
        float actualNoise = noise * noiseMultipliyer;

        if (Math.abs(position.y) > completeIceHeight && (waveNoise >= beachMethaneHeight || puddleNoise >= beachMethaneHeight)) {
            return iceLevel;
        }
        
        if(waveNoise >= deepOceanHeight || puddleNoise >= deepOceanHeight) return deepOceanLevel;
        if(waveNoise >= oceanHeight || puddleNoise >= oceanHeight) return oceanLevel;
        if(waveNoise >= beachMethaneHeight || puddleNoise >= beachMethaneHeight) return subBeachLevel;
        if(waveNoise >= beachHeight || puddleNoise >= beachHeight) return beachLevel;
        
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
    public Color getColor(Vec3 position) {
        float biomeMask = Simplex.noise3d(seed, 3, 0.4, 1f, position.z, position.y, position.x);
        float patternMask = Simplex.noise3d(seed, 1, 0.6, 2f, position.z, position.y, position.x);
        
        var methaneAlbedo = 0.1f;
        
        if (
        		(rawHeight(position) > iceCoverage && rawHeight(position) < 1f - iceCoverage && 
        		(!(position.y > iceHeight - 1f) || !(position.y < 1f - iceHeight)) || 
        		Math.abs(position.y) > completeIceHeight)) 
        	return biomeMask > 0.8f || Math.abs(position.y) > completeIceHeight ? Color.valueOf("d0e36f") : Color.valueOf("b6c953").a(methaneAlbedo);
        
    	if (getHeight(position) <= deepOceanLevel) {
        	return lerpColor(TektonColor.liquidMethane.cpy().a(methaneAlbedo), TektonColor.methane.cpy().a(methaneAlbedo), 0.65f);
        }
    	else if (getHeight(position) <= oceanLevel) {
        	return TektonColor.methane.cpy().a(methaneAlbedo);
        }
        else if (getHeight(position) <= subBeachLevel) {
        	return lerpColor(Color.valueOf("4f4646").a(methaneAlbedo), TektonColor.methane.cpy().a(methaneAlbedo), 0.35f);
        }
        else if (getHeight(position) <= beachLevel) {
        	return Color.valueOf("4f4646");
        }
        
        var vegetationCoverage = 0.39f;
        var vegetationHeight = 0.66f;
        
        if (
        		rawHeight(position) > vegetationCoverage && rawHeight(position) < 1f - vegetationCoverage && 
        		(!(position.y > vegetationHeight - 1f) || !(position.y < 1f - vegetationHeight))) 
        	return rand.random(1f) > 0.07f ? lerpColor(Color.valueOf("2c3c4d"), Color.valueOf("506873"), Mathf.round(rawHeight(position) - 0.02f)) : Color.valueOf("84ff00").a(methaneAlbedo);
        
        if(biomeMask > 0.6f && patternMask < 0.6f) return Color.valueOf("948881");
        if(biomeMask > 0.5f) return Color.valueOf("5c483e");
        if(biomeMask > 0.4f && patternMask < 0.3f) return Color.valueOf("515151");
        if(biomeMask > 0.2f) return Color.valueOf("6e6761");
        
        return TektonColor.methane.cpy().a(methaneAlbedo);
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
