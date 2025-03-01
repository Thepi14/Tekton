package tekton.content;

import mindustry.game.Schematic;
import mindustry.game.Schematics;

public class TektonLoadouts {
	public static Schematic corePrimal;
	
	public static void load() {
		corePrimal = Schematics.readBase64("bXNjaAF4nGNgZmBmZmDJS8xNZWALKMrMTcxh4E5JLU4uyiwoyczPY2BgYMtJTErNKWZgio5lZBAqSc0uyc/TTc4vStUtgKhnYGAEISABAA6DEy8=");
    }
}
