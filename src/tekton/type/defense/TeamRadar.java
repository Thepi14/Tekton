package tekton.type.defense;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.game.Team;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.world.blocks.defense.Radar;

public class TeamRadar extends Radar {

    public TextureRegion baseTeamRegion;
    public TextureRegion teamGlowRegion;

	public TeamRadar(String name) {
		super(name);
	}
	
	@Override
    public void load() {
        super.load();
        
    	baseTeamRegion = Core.atlas.find(name + "-base-team");
		teamGlowRegion = Core.atlas.find(name + "-team-glow");
    }
	
	public class TeamRadarBuild extends RadarBuild{
		@Override
        public void draw(){
            Draw.rect(baseRegion, x, y);
            Draw.color(this.team == Team.sharded ? Color.clear : this.team.color);
            Draw.rect(baseTeamRegion, x, y);
            Draw.color();
            Draw.rect(region, x, y, rotateSpeed * totalProgress);
            Draw.color(this.team == Team.sharded ? Color.clear : this.team.color);
            Draw.rect(teamGlowRegion, x, y, rotateSpeed * totalProgress);
            Draw.color();

            Drawf.additive(glowRegion, team.color, efficiency * glowColor.a * (1f - glowMag + Mathf.absin(glowScl, glowMag)), x, y, rotateSpeed * totalProgress, Layer.blockAdditive);
        }
	}
}
