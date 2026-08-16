package tekton.content;

import java.lang.reflect.Field;

import arc.Events;
import arc.audio.Music;
import arc.audio.Sound;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType.MusicRegisterEvent;
import mindustry.game.EventType.Trigger;

public class TektonMusic {
	public static Seq<String> tektonAmbientMusicNames, tektonDarkMusicNames, tektonBossMusicNames;
	public static Seq<Music> tektonAmbientMusic, tektonDarkMusic, tektonBossMusic;
	
	public static void load() {
		tektonAmbientMusicNames = new Seq<String>().addAll("biological", "game14");
		tektonDarkMusicNames = new Seq<String>().addAll();
		tektonBossMusicNames = new Seq<String>().addAll("boss3");
		
		setupMusicLists();
		
		Events.on(MusicRegisterEvent.class, e -> {
			reload();
		});
		//Vars.control.sound.bossMusic.addAll(tektonBossMusic);
	}
	
	public static void setupMusicLists() {
		tektonAmbientMusic = new Seq<Music>();
		tektonDarkMusic = new Seq<Music>();
		tektonBossMusic = new Seq<Music>();
		
		for (String name : tektonAmbientMusicNames)
			tektonAmbientMusic.add(loadMusic(name));
		for (String name : tektonDarkMusicNames)
			tektonDarkMusic.add(loadMusic(name));
		for (String name : tektonBossMusicNames)
			tektonBossMusic.add(loadMusic(name));
	}
	
	public static Music loadMusic(String name) {
		return Vars.tree.loadMusic(name);
	}
	
	public static void reload() {
		//Vars.control.sound.ambientMusic.addAll(loadMusic("game14"));
		//Vars.control.sound.darkMusic.addAll(loadMusic("game14"));
		Vars.control.sound.ambientMusic.addAll(tektonAmbientMusic);
		Vars.control.sound.darkMusic.addAll(tektonDarkMusic);
		Vars.control.sound.bossMusic.addAll(tektonBossMusic);
	}
}
