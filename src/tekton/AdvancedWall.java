package tekton;

import arc.graphics.Color;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.meta.Env;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

import static arc.graphics.g2d.Draw.color;
import static mindustry.Vars.*;

public class AdvancedWall extends Wall {
    public float effectChance = 0.003f;
	public float healPercent = 1f;
	public Color baseColor = Pal.lightOrange;
	public float reload = 100f;
	
	public Effect regenEffect = new Effect(100f, e -> {
        color(baseColor);
        Fill.square(e.x, e.y, e.fslope() * 1.5f + 0.14f, 45f);
    });
	
	public AdvancedWall(String name) {
		super(name);
		fogRadius = 0;
		emitLight = true;
        envEnabled = Env.any;
        lightRadius = 3f;
        update = true;
	}
	
	public class RegenWallBuild extends WallBuild {
		public boolean needRegen = false;
		public float charge = Mathf.random(reload);
	 
		@Override
		public void updateTile(){
			needRegen = damaged();
			if (isHealSuppressed())
				charge = 0f;
			else if (needRegen)
				charge += Time.delta;
				
			if (charge >= ((AdvancedWall)block).reload && needRegen) {
				charge = 0f;
				heal(maxHealth() * (healPercent) / 100f);
				recentlyHealed();
				regenEffect.at(x + Mathf.range(block.size * tilesize/2f - 1f), y + Mathf.range(block.size * tilesize/2f - 1f));
				Fx.healBlockFull.at(x, y, block.size, baseColor, block);
			}
		}
		
		@Override
        public void drawLight(){
			if (!inFogTo(player.team())) {
	            Drawf.light(x, y, lightRadius, baseColor, 0.7f);
			}
        }
		
        @Override
        public void write(Writes write){
            super.write(write);
            write.f(charge);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            charge = read.f();
        }
	}
}
