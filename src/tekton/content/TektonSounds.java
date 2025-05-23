package tekton.content;

import arc.Core;
import arc.assets.AssetDescriptor;
import arc.assets.loaders.SoundLoader;
import arc.audio.Sound;
import arc.struct.Seq;
import mindustry.Vars;

import java.lang.reflect.Field;

public class TektonSounds {
	
	public static Sound plasmared, plasmablue, firered, electricorbshoot, machinegunsound, redlasercharge, laserred, laserredsmall, pyon, tchau, shotheavy, shoothuge, sonarloop, sonarping, freezer, lightningstrike, tesla, gravity, gravityemission;
	
	public static void load(){
		Class<?> c = TektonSounds.class;
		Seq<Field> fields = new Seq<>(c.getFields());
		fields.retainAll(f -> Sound.class.equals(f.getType()));
		try{
			for(Field f : fields)f.set(null, loadSound(f.getName()));
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}
	}
	
	private static Sound loadSound(String soundName){
		if (!Vars.headless) {
			String name = "sounds/" + soundName.toLowerCase();
			String path = Vars.tree.get(name + ".ogg").exists() ? name + ".ogg" : name + ".mp3";
			
			Sound sound = new Sound();
			
			AssetDescriptor<?> desc = Core.assets.load(path, Sound.class, new SoundLoader.SoundParameter(sound));
			desc.errored = Throwable::printStackTrace;
			return sound;
		}
		else return new Sound();
	}
}
