package tekton.type.weathers;

import static mindustry.Vars.world;

import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.WeatherState;
import mindustry.type.weather.ParticleWeather;
import tekton.type.bullets.EmptyBulletType;

public class ObstaclesWeather extends ParticleWeather {
	/** big numbers causes amargeddon */
	public float obstacleChance = 0.01f;
	public BulletType obstacle = new EmptyBulletType();
	public Effect obstacleFallEffect = Fx.none;
	public Team obstacleTeam = Team.derelict;
	public Seq<ObstacleFallTimer> timerList = new Seq<>();

	private float trand = 0f;

	public ObstaclesWeather(String name) {
		super(name);
	}

	@Override
    public void update(WeatherState state) {
		super.update(state);
		if (Groups.unit.isEmpty() || Vars.state.isPaused()) {
			return;
		}

		trand = Mathf.random(0f, 100f) / 100f;
		if (trand <= obstacleChance) {
			var randX = Mathf.random(0, Vars.world.width() - 1) * Vars.tilesize;
			var randY = Mathf.random(0, Vars.world.height() - 1) * Vars.tilesize;
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
				if (world.tile((int)timer.x / Vars.tilesize, (int)timer.y / Vars.tilesize) != null) {
					if (!world.tile((int)timer.x / Vars.tilesize, (int)timer.y / Vars.tilesize).legSolid()) {
						obstacle.create(Groups.unit.first(), obstacleTeam, timer.x, timer.y, 90f);
					}
				}
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
