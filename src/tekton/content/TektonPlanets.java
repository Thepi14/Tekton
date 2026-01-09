package tekton.content;

import arc.graphics.*;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.graphics.g3d.PlanetGrid.*;
import mindustry.maps.planet.*;
import mindustry.type.*;
import mindustry.type.Weather.WeatherEntry;
import mindustry.world.*;
import mindustry.world.blocks.Attributes;
import mindustry.world.meta.*;
import tekton.Tekton;
import tekton.type.planetGeneration.*;
import tekton.type.world.TektonEnv;
import mindustry.content.*;

import arc.func.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.maps.planet.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class TektonPlanets {
	public static TektonPlanet tekton, mirera;
	
	public static void load(){
		tekton = new TektonPlanet("tekton", Planets.sun, 1.1f, 2) {{
			generator = new TektonPlanetGenerator();
			description = "A extremely cold planet with unknown threats and possibilities.";
			alwaysUnlocked = true;
			allowLaunchSchematics = false;
			allowLaunchToNumbered = false;
			allowLaunchLoadout = false;
			allowSectorInvasion = false;
			allowWaveSimulation = false;
			clearSectorOnLose = true;
            prebuildBase = true;
			startSector = 0;
			orbitSpacing = 1;
			orbitRadius = 140;
			//minZoom = 0.9f;
			totalRadius += 2f;
			atmosphereRadIn = 0.04f;
			atmosphereRadOut = 0.4f;
			updateLighting = false;
			//sectorSeed = 77147;
			bloom = true;
			landCloudColor = Color.valueOf("c0ed42");
			atmosphereColor = Color.valueOf("343b11");
			iconColor = Color.valueOf("6e941e");
			hasAtmosphere = true;
			solarSystem = Planets.sun;
			enemyBuildSpeedMultiplier = 0.4f;
			//methaned now
			defaultEnv = TektonEnv.methane | Env.terrestrial;
			
			ruleSetter = r -> {
                //r.waveTeam = Team.blue;
                /*r.staticColor = new Color(0f, 0f, 0f, 1f);
                r.dynamicColor = new Color(0f, 0f, 0f, 0.7f);*/
                r.cloudColor = Color.valueOf("3e401f");
                r.placeRangeCheck = false;
                r.showSpawns = false;
                r.fog = true;
                r.staticFog = true;
                r.lighting = true;
                r.fire = false;
                r.ambientLight = Color.valueOf("101805a4");
            	if (r.weather.size == 0) {
	                r.weather = new Seq<WeatherEntry>().addAll(new WeatherEntry(TektonWeathers.methaneRain) {{  }}, new WeatherEntry(TektonWeathers.darkSandstorm) {{ minFrequency = 20f * Time.toMinutes; maxFrequency = 60f * Time.toMinutes; }});
            	}
            	//foolish
            	if (r.weather.contains(t -> (t.weather == TektonWeathers.tektonFog))) {
            		r.weather.remove(t -> (t.weather == TektonWeathers.tektonFog));
            	}
                r.coreDestroyClear = true;
                r.onlyDepositCore = true;
                //r.teams.get(Team.blue).rtsAi = true;
                //r.teams.get(Team.green).rtsAi = true;
                r.coreIncinerates = true;
                r.solarMultiplier = 0.1f;
                r.bannedBlocks.addAll(Blocks.phaseWall, Blocks.phaseWallLarge);
                r.hideBannedBlocks = true;
                r.loadout = new Seq<ItemStack>().add(new ItemStack(TektonItems.iron, 200));
            };
            
            /*campaignRuleDefaults.fog = true;
            campaignRuleDefaults.showSpawns = false;
            campaignRuleDefaults.rtsAI = true;*/
            
            accessible = true;
            
            //atmosphere is full of methane
            defaultAttributes.set(TektonAttributes.methane, 1f);
            
			defaultCore = TektonBlocks.corePrimal;
            unlockedOnLand.add(TektonBlocks.corePrimal);
            
            meshLoader = () -> new HexMesh(this, 5);
            
            var increaseCloudRad = 0.040f;
            cloudMeshLoader = () -> new MultiMesh(
            		//inner
            		new HexSkyMesh(this, 3377, 1.8f, 0.12f + increaseCloudRad, 5, Color.valueOf("57592b5e"), 3, 0.3f, 1f, 0.6f),
            		new HexSkyMesh(this, 714, -1.7f, 0.125f + increaseCloudRad, 5, Color.valueOf("57592b5e"), 3, 0.3f, 1f, 0.6f),
            		//outer
            		new HexSkyMesh(this, 1477, 0.14f, 0.145f + increaseCloudRad, 5, Color.valueOf("74800e").a(0.75f), 4, 0.42f, 1f, 0.43f),
            		new HexSkyMesh(this, 7714, 0.7f, 0.16f + increaseCloudRad, 5, Color.valueOf("c2d175").a(0.75f), 4, 0.42f, 1.2f, 0.45f));
            
            hiddenItems.addAll(Items.copper, Items.lead, Items.titanium, Items.plastanium, Items.thorium, Items.surgeAlloy, Items.metaglass, Items.carbide, Items.beryllium, Items.oxide, Items.tungsten, Items.sporePod, Items.pyratite, Items.blastCompound, Items.coal, Items.scrap);
            
            for (var sector : sectors) {
            	if (!TektonSectors.all.contains(sector.preset)) {
            		sector.preset = TektonSectors.satus;
            	}
            }
		}};
		
		mirera = new TektonPlanet("mirera", tekton, 0.25f, 1) {{
			generator = new TektonMoonGenerator();
			description = "Tekton's moon, has no atmosphere.";
			alwaysUnlocked = false;
			orbitRadius = 7;
			allowLaunchSchematics = false;
			allowLaunchToNumbered = false;
			allowLaunchLoadout = false;
			allowSectorInvasion = false;
			startSector = 0;
			minZoom = 1;
			drawOrbit = true;
			atmosphereRadIn = 0;
			atmosphereRadOut = 0.3f;
			accessible = false;
			updateLighting = true;
			sectorSeed = 14513;
			bloom = false;
			visible = true;
			landCloudColor = Color.valueOf("666359");
			atmosphereColor = Color.valueOf("666359");
			iconColor = Color.valueOf("666359");
			hasAtmosphere = false;
			allowWaveSimulation = true;
			clearSectorOnLose = true;
			allowWaves = true;
			prebuildBase = false;
			defaultCore = TektonBlocks.corePrimal;
            unlockedOnLand.add(TektonBlocks.corePrimal);
			solarSystem = Planets.sun;
			
			meshLoader = () -> new HexMesh(this, 3);
			cloudMeshLoader = () -> new MultiMesh();
			
			defaultEnv = Env.terrestrial;
			
			ruleSetter = r -> {
                r.waveTeam = Team.blue;
                r.staticColor = new Color(0f, 0f, 0f, 1f);
                r.dynamicColor = new Color(0f, 0f, 0f, 0.7f);
                r.cloudColor = Color.valueOf("3e401f");
                r.placeRangeCheck = false;
                r.showSpawns = false;
                r.fog = true;
                r.staticFog = true;
                r.lighting = true;
                r.fire = false;
                r.ambientLight = Color.valueOf("101805a4");
                r.weather.clear();
                /*r.weather = new Seq<WeatherEntry>().addAll(
                		//new WeatherEntry(TektonWeathers.tektonFog) {{ always = true; }},
                		new WeatherEntry(TektonWeathers.methaneRain),
                		new WeatherEntry(TektonWeathers.darkSandstorm));*/
                r.coreDestroyClear = true;
                r.onlyDepositCore = true;
                //r.teams.get(Team.blue).rtsAi = true;
                r.teams.get(Team.blue).buildAi = false;
                r.coreIncinerates = true;
                r.solarMultiplier = 0.1f;
                r.bannedBlocks.addAll(Blocks.phaseWall, Blocks.phaseWallLarge);
                r.hideBannedBlocks = true;
                r.loadout = new Seq<ItemStack>().add(new ItemStack(TektonItems.iron, 200));
            };
		}};
		
        Planets.serpulo.hiddenItems.addAll(TektonItems.tektonOnlyItems);
        Planets.erekir.hiddenItems.addAll(TektonItems.tektonOnlyItems);
	}
	
	public static class TektonPlanet extends Planet{
		public TektonPlanet(String name, Planet parent, float radius, int sectorSize){
			super(name, parent, radius, sectorSize);
		}
		
		public TektonPlanet(String name, Planet parent, float radius){
			super(name, parent, radius);
		}
	}
}
