package tekton.type.power;

import static mindustry.Vars.*;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.io.*;
import mindustry.Vars;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.blocks.power.*;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tekton.Drawt;
import tekton.type.defense.LightningAbsorber;

public class LightningRod extends PowerGenerator {
	public float protectionRadius = 40f * Vars.tilesize;
	public float powerOutputDuration = 600f;
	public Color glowColor = Color.orange.cpy().a(1f);
	public float glowOpacity = 0.25f;
	public boolean circleArea = false;
	public boolean squareArea = false;
	public boolean diamondArea = true;
	public boolean cummulative = true;
	public float maxPowerEfficiency = 4f;
	
    public TextureRegion glowRegion;
    
	public LightningRod(String name) {
		super(name);
		powerProduction = 10f;
		//outlineIcon = true;
	}
	
	@Override
    public void load() {
        super.load();
        
        glowRegion = Core.atlas.find(name + "-glow");
    }
	
	@Override
    public void setBars() {
        super.setBars();
    }
	
	@Override
    public void setStats() {
        super.setStats();
        
        stats.add(Stat.productionTime, powerOutputDuration / 60f, StatUnit.seconds);
    }
	
	@Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        if (circleArea)
            Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, protectionRadius, Pal.accent);
        else if (squareArea)
        	Drawf.dashSquare(Pal.accent, x * tilesize + offset, y * tilesize + offset, protectionRadius * 2f);
        else if (diamondArea)
        	Drawt.dashDiamond(x * tilesize + offset, y * tilesize + offset, protectionRadius);
        
        super.drawPlace(x, y, rotation, valid);
    }
	
	@Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{region};
    }
	
	public class LightningRodBuild extends GeneratorBuild implements LightningAbsorber {
		
		public boolean hit = false;
		public float powerOutput = 0f;
		
		@Override
		public void updateTile() {
			powerOutput -= 1f / powerOutputDuration;
			
			if (cummulative)
				powerOutput = Mathf.clamp(powerOutput, 0f, maxPowerEfficiency);
			else
				powerOutput = Mathf.clamp(powerOutput);
			
			productionEfficiency = powerOutput;
			
			if (hit) {
				if (cummulative)
					powerOutput += 1f;
				else
					powerOutput = 1f;
				hit = false;
			}
		}
		
		@Override
		public float efficiency() {
			return productionEfficiency;
		}
		
		@Override
        public float totalProgress() {
            return powerOutput;
        }
        
        @Override
        public float getPowerProduction() {
            return productionEfficiency * powerProduction;
        }
		
		@Override
        public void drawSelect() {
			if (circleArea)
	            Drawf.dashCircle(x + offset, y + offset, protectionRadius, Pal.accent);
			else if (squareArea)
	        	Drawf.dashSquare(Pal.accent, x + offset, y + offset, protectionRadius * 2f);
			else if (diamondArea)
				Drawt.dashDiamond(x + offset * tilesize, y + offset * tilesize, protectionRadius);
        }
		
		@Override
		public void draw() {
            var z = Draw.z();
            Draw.rect(region, x, y);
            Draw.z(Layer.turret);
            Drawf.additive(glowRegion, glowColor.a(Mathf.clamp(powerOutput, 0f, 1f / glowOpacity) * glowOpacity), x, y);
            Draw.z(z);
            Draw.reset();
		}
		
		@Override
        public void drawLight() {
            Drawf.light(x, y, (powerOutput + 0.5f) * (protectionRadius / 2f), lightColor, Mathf.clamp(powerOutput, 0f, 1f / glowOpacity) * glowOpacity);
        }
		
		@Override
		public void write(Writes write) {
			super.write(write);
			
			write.bool(hit);
			write.f(powerOutput);
		}
		
		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			
			hit = read.bool();
			powerOutput = read.f();
		}
		
		@Override
        public boolean canPickup() {
            return false;
        }

		@Override
		public void absorbLightning() {
			hit = true;
		}
	}
}
