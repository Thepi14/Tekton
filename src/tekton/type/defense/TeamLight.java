package tekton.type.defense;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.struct.EnumSet;
import arc.util.Tmp;
import mindustry.game.Team;
import mindustry.world.blocks.power.LightBlock;
import mindustry.world.meta.BlockFlag;

public class TeamLight extends LightBlock {
    public TextureRegion teamRegion;

	public TeamLight(String name) {
		super(name);
        flags = EnumSet.of(BlockFlag.hasFogRadius);
	}

	@Override
    public void load() {
        super.load();

        teamRegion = Core.atlas.find(name + "-team");
    }

    public class TeamLightBuild extends LightBuild {
    	
        @Override
        public void draw(){
            super.draw();
            Draw.color(this.team == Team.sharded ? Color.clear : this.team.color);
            Draw.rect(teamRegion, x, y);
            Draw.color();
        }
    	
    	@Override
        public float fogRadius(){
            return fogRadius * efficiency;
        }
    }
}
