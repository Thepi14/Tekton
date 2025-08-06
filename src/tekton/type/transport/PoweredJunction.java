package tekton.type.transport;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.gen.BufferItem;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.world.blocks.distribution.Junction;
import mindustry.world.blocks.distribution.Router;

import static mindustry.Vars.*;

public class PoweredJunction extends Junction {
	
	public float enhancedSpeed = 29f;

	public PoweredJunction(String name) {
		super(name);
		
	}
	
	public class PoweredJunctionBuild extends JunctionBuild {
		private float currentSpeed;
		
		@Override
        public void updateTile(){
			currentSpeed = Mathf.lerp(speed, enhancedSpeed, power().status);

            for(int i = 0; i < 4; i++){
                if(buffer.indexes[i] > 0){
                    if(buffer.indexes[i] > capacity) buffer.indexes[i] = capacity;
                    long l = buffer.buffers[i][0];
                    float time = BufferItem.time(l);

                    if(Time.time >= time + currentSpeed / timeScale || Time.time < time){

                        Item item = content.item(BufferItem.item(l));
                        Building dest = nearby(i);

                        //skip blocks that don't want the item, keep waiting until they do
                        if(item == null || dest == null || !dest.acceptItem(this, item) || dest.team != team){
                            continue;
                        }

                        dest.handleItem(this, item);
                        System.arraycopy(buffer.buffers[i], 1, buffer.buffers[i], 0, buffer.indexes[i] - 1);
                        buffer.indexes[i] --;
                    }
                }
            }
        }

        @Override
        public void handleItem(Building source, Item item){
            int relative = source.relativeTo(tile);
            buffer.accept(relative, item);
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            int relative = source.relativeTo(tile);

            if(relative == -1 || !buffer.accepts(relative) || (!(source.block instanceof PoweredConveyor) && !(source.block instanceof PoweredJunction) && !(source.block instanceof Router))) return false;
            Building to = nearby(relative);
            return to != null && to.team == team;
        }
	}
}
