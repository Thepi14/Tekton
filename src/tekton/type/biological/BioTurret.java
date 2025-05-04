package tekton.type.biological;

import static mindustry.Vars.*;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.entities.Effect;
import mindustry.entities.Puddles;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.meta.BuildVisibility;
import tekton.Drawt;
import tekton.content.TektonColor;
import tekton.content.TektonFx;
import tekton.content.TektonLiquids;

public class BioTurret extends PowerTurret implements BiologicalBlock {

	public float regenReload = 100f;
	public float healPercent = 1f;
	public Color regenColor = Pal.heal;
	public Effect regenEffect = TektonFx.buildingBiologicalRegeneration.wrap(regenColor);
	
	public BioTurret(String name) {
		super(name);
		createRubble = drawCracks = false;
		buildVisibility = BuildVisibility.sandboxOnly;
		lightColor = TektonColor.acid.cpy();
		lightRadius = 4f;
		emitLight = true;
		drawTeamOverlay = false;
		createRubble = drawCracks = false;
        outlineColor = TektonColor.tektonOutlineColor;
        baseExplosiveness = 5f;
		hideDetails = true;

		destroyEffect = TektonFx.biologicalDynamicExplosion;
	}

	public class BioTurretBuild extends PowerTurretBuild {
		public boolean needRegen = false;
		public float regenCharge = Mathf.random(regenReload);
        
        @Override
        public void updateTile() {
        	super.updateTile();
        	
			needRegen = damaged();
			
			if (needRegen)
				regenCharge += Time.delta * timeScale * efficiency;
			
			if (regenCharge >= regenReload && needRegen) {
				regenCharge = 0f;
				heal(maxHealth() * (healPercent) / 100f);
				recentlyHealed();
				regenEffect.at(x + Mathf.range(block.size * tilesize/2f - 1f), y + Mathf.range(block.size * tilesize/2f - 1f));
			}
        }
        
		@Override
        public void drawLight() {
			if (!emitLight)
				return;
            Drawf.light(x, y, ((90f + Mathf.absin(5, 5f)) * lightRadius * warmup()) / tilesize, Tmp.c1.set(lightColor), 0.4f * lightRadius * efficiency);
        }
        
        @Override
        public void onDestroyed() {
            super.onDestroyed();
			Tile tile = world.tileWorld(x + Tmp.v1.x, y + Tmp.v1.y);
            Puddles.deposit(tile, TektonLiquids.acid, 140f * efficiency);
			Drawt.DrawAcidDebris(x, y, Mathf.random(4) * 90, size);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(regenCharge);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            regenCharge = read.f();
        }
        
        @Override
        public boolean canPickup() {
            return false;
        }
	}
}
