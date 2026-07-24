package tekton.type.weathers;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.WeatherState;
import mindustry.type.weather.RainWeather;
import mindustry.world.blocks.storage.CoreBlock;
import tekton.math.TekMath;
import tekton.type.biological.BiologicalBlock;

public class DamagingRainWeather extends RainWeather {
	public float minDamage = 0f, maxDamage = 4f;
	public float interval = 300f; //big interval to not lag the game
	
	private float currentInterval = 0f;
	
    public DamagingRainWeather(String name){
        super(name);
    }
    
	@Override
    public void update(WeatherState state) {
		super.update(state);
		
		if (Vars.state.isPaused()) {
			return;
		}
		
		currentInterval += Time.delta;
		if (currentInterval > interval) {
			currentInterval %= interval;
			
			for (int x = 0; x < Vars.world.width(); x++) {
				for (int y = 0; y < Vars.world.height(); y++) {
					var tile = Vars.world.tileBuilding(x, y);
					
					if (tile.build == null)
						continue;
					else if (tile.build.team == Team.derelict || tile.build.dead || tile.build.block instanceof BiologicalBlock || tile.build.block instanceof CoreBlock)
						continue;
					
					float damageScale = TekMath.pow2(tile.build.block.size);
					tile.build.damage(Mathf.random(minDamage, maxDamage) / damageScale);
				}
			}
		}
	}
}
