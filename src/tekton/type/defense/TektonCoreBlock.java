package tekton.type.defense;

import mindustry.world.blocks.storage.CoreBlock;
import tekton.Drawt;
import mindustry.entities.Effect;
import mindustry.entities.effect.*;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;

import static mindustry.Vars.*;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;

public class TektonCoreBlock extends CoreBlock {
	public float lightningProtectionRadius = 20f * tilesize;
	public Effect lightningAbsorptionEffect = new WaveEffect() {{
		sides = 4;
		rotation = 45f;
		lifetime = 60f;
		sizeFrom = 2f;
		sizeTo = 20f;
		lightOpacity = 0.5f;
		colorFrom = Color.white.cpy();
		colorTo = lightColor = Color.valueOf("ffe047");
		strokeFrom = 2f;
		strokeTo = 0f;
	}};
	
	public TektonCoreBlock(String name) {
		super(name);
		
	}
	
	@Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);
		Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, lightningProtectionRadius, Pal.accent);
    }
	
	public class TektonCoreBlockBuild extends CoreBuild implements LightningAbsorber {
		
		@Override
        public void drawSelect(){
            super.drawSelect();
        	Drawf.dashCircle(x, y, lightningProtectionRadius, Pal.accent);
		}
		
		@Override
		public void absorbLightning() {
			lightningAbsorptionEffect.at(this);
		}
		
		@Override
		public float lightningProtectionRadius() {
			return lightningProtectionRadius;
		}
	}
}
