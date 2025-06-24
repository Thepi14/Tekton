package tekton.content;

import arc.Events;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Schematic;
import mindustry.game.Schematics;
import mindustry.world.blocks.storage.CoreBlock;

public class TektonLoadouts {
	public static Schematic corePrimal;
	
	public static void load() {
		corePrimal = Schematics.readBase64("bXNjaAF4nGNgZmBmZmDJS8xNZWALKMrMTcxh4E5JLU4uyiwoyczPY2BgYMtJTErNKWZgio5lZBAqSc0uyc/TTc4vStUtgKhnYGAEISABAA6DEy8=");
		
		Events.run(EventType.Trigger.update, () -> {
            if (!Vars.schematics.getLoadouts((CoreBlock) TektonBlocks.corePrimal).contains(corePrimal)) {
                Vars.schematics.getLoadouts((CoreBlock) TektonBlocks.corePrimal).clear();
                Vars.schematics.getLoadouts((CoreBlock) TektonBlocks.corePrimal).add(corePrimal);
            }
            TektonPlanets.tekton.defaultCore = TektonBlocks.corePrimal;
            TektonPlanets.tekton.generator.defaultLoadout = corePrimal;
        });
    }
}
