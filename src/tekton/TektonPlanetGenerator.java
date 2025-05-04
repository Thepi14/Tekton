package tekton;

import arc.graphics.Color;
import arc.math.geom.Vec3;
import arc.util.noise.Ridged;
import arc.util.noise.Simplex;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.type.Sector;
import tekton.content.*;

public class TektonPlanetGenerator extends PlanetGenerator {
	float sizeMultipliyer = 1.6f;
	float oceanLevel = 0.45f;
	int noiseSeedAdd = -7000;
	int puddleSeedAdd = 7000;
	int waveSeedAdd = 14000;
	float maxSize = 0.77f;

	public TektonPlanetGenerator() {
		//super();
		baseSeed = 14;
		seed = 71471;
		defaultLoadout = TektonLoadouts.corePrimal;
	}
    
    /*@Override
    public void generateSector(Sector sector) {
    	
    }*/

    @Override
    public float getHeight(Vec3 position) {
        float noise = Simplex.noise3d(seed + noiseSeedAdd , 4, 0.9f, 1f, (position.z / 10f) * sizeMultipliyer, (position.y) * sizeMultipliyer, (position.x / 2f) * sizeMultipliyer);
        float puddleNoise = Simplex.noise3d(seed + puddleSeedAdd, 2, 0.9f, 1f, (position.x / 12f) * sizeMultipliyer, (position.y / 12f) * sizeMultipliyer, (position.z / 12f) * sizeMultipliyer);
        float waveNoise = Ridged.noise3d(seed + waveSeedAdd, (position.y / 2f + noise * 4f) * sizeMultipliyer, 0, 0, 1/4f);
        float actualNoise = noise * 1.2f;
        
        if(waveNoise > 0.7f || puddleNoise > 0.7f) return oceanLevel;
        return Math.min(Math.max(actualNoise, oceanLevel), maxSize);
    }
    
    @Override
    public Color getColor(Vec3 position) {
        float biomeMask = Simplex.noise3d(seed, 3, 0.4, 1f, position.z, position.y, position.x);
        float patternMask = Simplex.noise3d(seed, 1, 0.6, 2f, position.z, position.y, position.x);

        if(getHeight(position) <= oceanLevel) return TektonColor.liquidMethane;
        if(biomeMask > 0.6f && patternMask < 0.6f) return Color.valueOf("948881");
        if(biomeMask > 0.5f) return Color.valueOf("5c483e");
        if(biomeMask > 0.4f && patternMask < 0.3f) return Color.valueOf("515151");
        if(biomeMask > 0.2f) return Color.valueOf("6e6761");
        return TektonColor.methane;
    }
}
