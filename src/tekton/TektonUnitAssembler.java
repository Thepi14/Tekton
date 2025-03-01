package tekton;

import static tekton.content.TektonBlocks.setPayloadRegions;


import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.logic.*;
import mindustry.net.Net;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.ConstructBlock.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.units.UnitAssemblerModule.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.world.blocks.units.UnitAssembler;
import mindustry.world.meta.Env;

import static mindustry.Vars.*;

public class TektonUnitAssembler extends UnitAssembler {
	
	public float itemDuration = 60f;

	public TektonUnitAssembler(String name) {
		super(name);
		envEnabled = Env.any;
	}

    @Override
    public void load(){
        super.load();
        setPayloadRegions(this, regionSuffix);
    }
    
    public class TektonUnitAssemblerBuild extends UnitAssemblerBuild {
    	private float itemTimer = 0f;
    	
    	@Override
        public void updateTile(){
    		super.updateTile();
    		
            Item item = items.first();
            
            if (item == null) {
            	efficiency = 0f;
            	return;
            }
            
            efficiency *= items.get(item) > 0 ? 1f : 0f;
            
            itemTimer += edelta() * efficiency;
            
            if (itemTimer >= itemDuration) {
                items.remove(item, 1);
                itemTimer = 0f;
            }
        }

    	@Override
    	public void checkTier(){
            modules.sort(b -> b.tier());
            int max = 0;
            for(int i = 0; i < modules.size; i++){
                var mod = modules.get(i);
                if(mod.tier() >= max || mod.tier() == max + 1){
                    max = mod.tier();
                }
            }
            currentTier = max;
        }
    	
    	@Override
        public void write(Writes write){
            super.write(write);

            write.f(itemTimer);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            
            itemTimer = read.f();
        }
    }
}
