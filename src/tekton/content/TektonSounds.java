package tekton.content;

import java.lang.reflect.Field;

import arc.Core;
import arc.assets.AssetDescriptor;
import arc.assets.loaders.SoundLoader;
import arc.audio.Sound;
import arc.struct.Seq;
import mindustry.Vars;

public class TektonSounds {

	public static Sound
	
	//shoot turrets
	shootOne,
	shootDuel,
	shootCompass,
	shootSkyscraper,
	shootSpear,
	shootAzure,
	shootInterfusion,
	shootHavoc,
	shootTesla,
	shootRepulsion,
	shootConcentration,
	shootRadiance,
	
	//shoot units
	shootPiezo,
	shootElectret,
	
	shootStrike,
	shootHammer,
	shootImpact,
	shootEarthquake,
	
	shootSagres,
	shootArgos,
	shootAriete,
	shootCastelo,
	
	shootEques,
	
	//shoot units (biological)
	shootCyaneaLightning,
	
	//beams
	radianceBeam,

	//charges
	chargeSword,
	chargeConcentration,
	
	chargeEarthquake,
	
	//explosions
	explosionFreeze,
	explosionMagnetic,
	explosionFreezeBig,
	explosionMagneticBig,
	
	//loops
	loopElectron,
	
	loopFreezer,
	
	loopGravity,
	loopGravitationalDrill,
	loopLatency,
	loopSonar,
	
	loopBioHover,
	
	//environment
	lightningStrike,
	
	//blocks
	pingSonar,
	
	firered,
	plasmared,
	laserredsmall,
	pyon
	;

	final static String none = "NONE";

	public static void load(){
		Class<?> c = TektonSounds.class;
		Seq<Field> fields = new Seq<>(c.getFields());
		fields.retainAll(f -> Sound.class.equals(f.getType()));
		try{
			for(Field f : fields) {
				f.set(null, loadSound(f.getName()));
			}
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}
	}

	//ive gone so far now
	private static Sound loadSound(String soundName){
		if (!Vars.headless) {
			final String[] namedDirectories = new String[] {"shoot", "charge", "beams", "loops", "explosions", "block"}; //named directories are folders that contain sounds with names equal or closely equal to the folders name (like shoot = shoot or beams = beam)
			final String[] otherDirectories = new String[] {"block", "environment", "movement", /*"ui"*/}; //other directories are folders that contain sounds with names that are not equal to the folders name
			final String mainSounds = "sounds/";

			//find it on the main sounds folder
			String path = mainSounds + soundName;
			String result = getSoundPath(path);
			
			if (result == none) {
				//find it on the default named directories
				for (String folder : namedDirectories) {
					if (soundName.toLowerCase().contains(
							folder.toCharArray()[folder.length() - 1] == 's' ? //verify if the folders name ends with a 's', like beams, loops etc, to turn it into beam and loop respectively
									folder.substring(0, folder.length() - 1) : 
										folder)
							) {
						
						path = mainSounds + folder + "/" + soundName;
						result = getSoundPath(path);
						
						if (result != none)
							break;
					}
				}
				
				//find it on other non named directories
				if (result == none) {
					for (String folder : otherDirectories) {
						path = mainSounds + folder + "/" + soundName;
						result = getSoundPath(path);
						
						if (result != none)
							break;
					}
				}
			}

			Sound sound = new Sound();

			AssetDescriptor<?> desc = Core.assets.load(result, Sound.class, new SoundLoader.SoundParameter(sound));
			desc.errored = Throwable::printStackTrace;
			return sound;
		} else {
			return new Sound();
		}
	}
	
	public static String getSoundPath(String path) {
		return Vars.tree.get(path + ".ogg").exists() ? path + ".ogg" : Vars.tree.get(path + ".mp3").exists() ? path + ".mp3" : Vars.tree.get(path + ".wav").exists() ? path + ".wav" : //find case sensitive
			Vars.tree.get(path.toLowerCase() + ".ogg").exists() ? path.toLowerCase() + ".ogg" : Vars.tree.get(path.toLowerCase() + ".mp3").exists() ? path.toLowerCase() + ".mp3" : Vars.tree.get(path.toLowerCase() + ".wav").exists() ? path.toLowerCase() + ".wav" : //find only diminutive
			none;
	}
}
