package tekton.type.weathers;

import static mindustry.Vars.*;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.struct.Seq;
import arc.util.*;
import arc.util.io.Reads;
import arc.util.io.Writes;
import ent.anno.Annotations.*;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.weather.*;
import tekton.content.*;
import tekton.type.bullets.*;

public class ObstaclesWeather extends ParticleWeather {
	/** big numbers causes amargeddon */
	public float obstacleChance = 0.01f;
	public BulletType obstacle = new EmptyBulletType();
	public Effect obstacleFallEffect = Fx.none;
	public Team obstacleTeam = Team.derelict;
	public Seq<ObstacleFallTimer> timerList = new Seq<ObstacleFallTimer>();
	
	private float trand = 0f;

	public ObstaclesWeather(String name) {
		super(name);
		
	}
	
	@Override
    public void update(WeatherState state) {
		super.update(state);
		if (Groups.unit.isEmpty() || Vars.state.isPaused())
			return;
		
		trand = Mathf.random(0f, 100f) / 100f;
		if (trand <= obstacleChance) {
			var randX = Mathf.random(0, Vars.world.width()) * Vars.tilesize;
			var randY = Mathf.random(0, Vars.world.height()) * Vars.tilesize;
			timerList.add(new ObstacleFallTimer(randX, randY));
			obstacleFallEffect.at(randX, randY);
		}
		
		int i = 0;
		for (var timer : timerList) {
			timer.id = i;
			i++;
		}
		
		i = 0;
		for (var timer : timerList) {
			timer.add(Time.delta);
			if (timer.timer >= obstacleFallEffect.lifetime) {
				int id = i;
				if (world.tile((int)timer.x / Vars.tilesize, (int)timer.y / Vars.tilesize) != null)
					if (!world.tile((int)timer.x / Vars.tilesize, (int)timer.y / Vars.tilesize).legSolid())
						obstacle.create(Groups.unit.first(), obstacleTeam, timer.x, timer.y, 90f);
				timerList.remove((f) -> { return timer.id == id; });
			}
			i++;
		}
    }
	
	public class ObstacleFallTimer {
		public int id = 0;
		public float timer = 0f, x, y;
		
		public ObstacleFallTimer(float x, float y) {
			this.x = x;
			this.y = y;
		}
		
		public void add(float add) {
			timer += add;
		}
	}
}
