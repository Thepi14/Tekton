package tekton.content;

import arc.Core;
import arc.graphics.*;
import arc.math.*;
import arc.math.geom.Vec2;
import arc.struct.*;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.Planets;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.entities.part.*;
import mindustry.entities.part.DrawPart.PartProgress;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.units.*;
import mindustry.world.blocks.units.DroneCenter;
import mindustry.world.blocks.units.DroneCenter.EffectDroneAI;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.logic.CanvasBlock;
import mindustry.world.blocks.logic.MessageBlock;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import tekton.*;
import tekton.environment.*;
import tekton.type.biological.*;
import tekton.type.bullets.*;
import tekton.type.defense.*;
import tekton.type.draw.*;
import tekton.type.gravity.*;
import tekton.type.part.FramePart;
import tekton.type.payloads.*;
import tekton.type.power.*;
import tekton.type.production.*;
import tekton.type.transport.*;
import tekton.type.world.TektonEnv;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.lineAngle;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;
import static tekton.content.TektonColor.*;
import static tekton.content.TektonItems.*;

import static mindustry.Vars.tilesize;
import static tekton.content.TektonVars.*;
import static mindustry.type.ItemStack.*;

public class TektonBlocks {
	public static Block 
	
	//Environment
	methane, deepMethane, methaneDarkSilicaSand, silicaSand, darkSilicaSand, acidFloor, //6
	
	brownSand, brownStone, brownIce, brownStoneWall, brownStoneVent, //5
	
	diatomite, diatomiteWall, zirconCrystal, diatomiteVent, //3
	
	neurosporaFloor, neurosporaEvolvedFloor, neurosporaFlower, neurosporaWall, neurosporaCluster, neurosporaVent, //6
	
	uraniniteFloor, uraniniteWall, uraniniteCrystal, trinitite, //3
	
	metalIronFloor, metalIronFloor2, metalIronFloor3, metalIronFloor4, metalIronFloor5, metalIronVent, metalIronExhaust, //7
	metalIronDarkFloor, metalIronDarkFloor2, metalIronDarkFloor3, metalIronDarkFloor4, metalIronDarkFloor5, metalIronWall, //6
	
	methaneSnow, methaneIce, methaneIceWall, 
	
	//ores
	oreIron, oreZirconium, oreTantalum, oreUranium, //4
	
	//wall ores
	zirconWall, ferricIronWall, wallOreIron, wallOreTantalum, wallOreUranium, //4
	
	//boulders
	diatomiteBoulder, brownStoneBoulder, uraniniteBoulder, methaneSnowBoulder, //4
	
	//crafting
	siliconFilter, siliconCompressor, graphiteConcentrator, coldElectrolyzer, polycarbonateSynthesizer, 
	magnetizer, atmosphericMethaneConcentrator, sandFilter, ammoniaCatalyst, cryogenicMixer, 
	dicyonogenEmitter, polytalumFuser, phasePrinter, nanoAlloyCrucible, hydrogenIncinerator, 
	
	//gravity
	gravityConductor, nanoGravityConductor, gravityRouter, electricalCoil, thermalCoil, phaseNanoCoil, 
	
	//walls
	ironWall, ironWallLarge, tantalumWall, tantalumWallLarge, gate, polycarbonateWall, polycarbonateWallLarge, 
	polytalumWall, polytalumWallLarge, uraniumWall, uraniumWallLarge, nanoAlloyWall, nanoAlloyWallLarge, 
	
	//transport
	ironDuct, tantalumDuct, ironRouter, ironBridge, ironDistributor, ironOverflow, ironUnderflow, ironSorter, ironInvertedSorter, ironUnloader, nanoConveyor, nanoJunction, nanoRouter, 
	
	//liquid
	pneumaticPump, pressurePump, pipe, pipeJunction, bridgePipe, polycarbonateBridgePipe, pipeRouter, polycarbonatePipe, polytalumPipe, 
	polycarbonateLiquidContainer, polycarbonateLiquidReserve, 
	
	//power
	lineNode, lineTower, lineLink, powerCapacitor, powerBank, reinforcedDiode, lightningRod, methaneBurner, 
	geothermalGenerator, methaneCombustionChamber, thermalDifferenceGenerator, uraniumReactor, fusionReactor, 
	
	//production
	wallDrill, plasmaWallDrill, silicaAspirator, silicaTurbine, geothermalCondenser, undergroundWaterExtractor, reactionDrill, gravitationalDrill, 
	
	//storage
	corePrimal, coreDeveloped, corePerfected, capsule, vault, 
	
	//defense
	lamp, researchRadar, sensor, builderDroneCenter, regenerator, regenerationDome, 
	
	//turrets
	one, duel, compass, skyscraper, spear, sword, azure, interfusion, freezer, havoc, tesla, prostrate, concentration, repulsion, radiance, tempest,
	
	//units
	primordialUnitFactory, unitDeveloper, tankDeveloper, airDeveloper, navalDeveloper, mechDeveloper, tankRefabricator, airRefabricator, navalRefabricator, mechRefabricator, 
	multiAssembler, ultimateAssembler, tankAssemblerModule, airAssemblerModule, navalAssemblerModule, mechAssemblerModule, unitRepairTurret, 
	
	//payload
	ironPayloadConveyor, ironPayloadRouter, deconstructor, constructor, payloadLoader, payloadUnloader, payloadLauncher, 
	
	//biological
	glowPod, smallNest, mediumNest, largeNest, artery, cerebellum, cobwebWall, cobwebWallLarge, 
	
	//logic
	ironCanvas, ironMessage,
	
	//debug / sandbox / config
	gravitySource, nullBlock
	;
	
	public static void load() {
		
		nullBlock = new Block("null") {{
			requirements(Category.effect, BuildVisibility.hidden, with(iron, 1));
			health = 1;
		}};
		
		//Environment
		methane = new Floor("shallow-methane", 4) {{
			speedMultiplier = 0.7f;
			liquidDrop = TektonLiquids.methane;
			isLiquid = true;
			cacheLayer = CacheLayer.water;
			liquidMultiplier = 1f;
			albedo = 0.2f;
			supportsOverlay = true;
			status = TektonStatusEffects.tarredInMethane;
			statusDuration = 240f;
			attributes.set(Attribute.water, -1f);
		}};
		
		deepMethane = new Floor("deep-methane", 4) {{
			speedMultiplier = 0.3f;
			liquidDrop = TektonLiquids.methane;
			isLiquid = true;
			cacheLayer = CacheLayer.water;
			//balancing
			liquidMultiplier = 0.5f;
			albedo = 0.2f;
			supportsOverlay = true;
			drownTime = 240f;
			status = TektonStatusEffects.tarredInMethane;
			statusDuration = 660f;
			attributes.set(Attribute.water, -2f);
		}};
		
		acidFloor = new Floor("acid-floor", 0) {{
			speedMultiplier = 0.4f;
			liquidDrop = TektonLiquids.acid;
			isLiquid = true;
			cacheLayer = CacheLayer.water;
			drownTime = 200f;
			status = TektonStatusEffects.acidified;
			statusDuration = 200f;
			attributes.set(Attribute.water, 0.2f);
            albedo = 0.9f;
            
            emitLight = true;
            lightRadius = 16f;
            lightColor = Color.acid.cpy().a(0.3f);
		}};
        
        //diatomite
		
		diatomite = new Floor("diatomite-floor", 2) {{
			attributes.set(Attribute.water, -0.2f);
		}};
		
		diatomiteWall = new StaticWall("diatomite-wall") {{
			variants = 4;
			diatomite.asFloor().wall = this;
			attributes.set(Attribute.sand, 1.5f);
		}};
		
		zirconCrystal = new TallBlock("zircon-crystal"){{
            variants = 3;
            clipSize = 128f;
			itemDrop = zirconium;
			
            emitLight = true;
            lightRadius = 40f;
            lightColor = TektonItems.zirconium.color.cpy().a(0.19f);
        }};
		
		diatomiteVent = new SteamVent("diatomite-vent") {{
            parent = blendGroup = diatomite;
            attributes.set(Attribute.steam, 1f);
			variants = 2;
        }};
		
		diatomiteBoulder = new Prop("diatomite-boulder") {{
			variants = 2;
			diatomite.asFloor().decoration = this;
		}};
		
		silicaSand = new Floor("silica-sand-floor", 2) {{
			itemDrop = Items.sand;
			attributes.set(TektonAttributes.silica, 1.2f);
			playerUnmineable = true;
			attributes.set(Attribute.water, 0.3f);
		}};
		
		darkSilicaSand = new Floor("dark-silica-sand-floor", 2) {{
			itemDrop = Items.sand;
			attributes.set(TektonAttributes.silica, 0.8f);
			playerUnmineable = true;
			attributes.set(Attribute.water, 0.3f);
		}};
		
		silicaSand.asFloor().wall = diatomiteWall;
		darkSilicaSand.asFloor().wall = diatomiteWall;
		
		//deprecated
		methaneDarkSilicaSand = new ShallowLiquid("dark-silica-sand-methane") {{
			//liquidDrop = TektonLiquids.methane;
			speedMultiplier = 0.75f;
			albedo = 0.2f;
			liquidMultiplier = 1.5f;
			supportsOverlay = true;
			status = TektonStatusEffects.tarredInMethane;
			statusDuration = 100f;
			attributes.set(Attribute.water, -0.5f);
			mapColor = Color.valueOf("514c3c");
		}};
		
		((ShallowLiquid)methaneDarkSilicaSand).set(methane, darkSilicaSand);
		
		//brown
		
		brownSand = new Floor("brown-sand", 3) {{
			itemDrop = Items.sand;
			attributes.set(TektonAttributes.silica, 0.4f);
			attributes.set(Attribute.water, 0.5f);
			
			playerUnmineable = true;
		}};
		
		brownStone = new Floor("brown-stone", 3) {{
			//attributes.set(Attribute.water, 0f);
		}};
		
		brownIce = new Floor("brown-ice", 3) {{
			attributes.set(Attribute.water, 2f);
		}};
		
		brownStoneBoulder = new Prop("brown-stone-boulder") {{
			variants = 2;
			brownStone.asFloor().decoration = this;
		}};
		
		brownStoneVent = new SteamVent("brown-stone-vent") {{
            parent = blendGroup = brownStone;
            attributes.set(Attribute.steam, 1f);
        }};
		
		brownStoneWall = new StaticWall("brown-stone-wall") {{
			variants = 2;
			
			brownStone.asFloor().wall = this;
			brownSand.asFloor().wall = this;
			brownIce.asFloor().wall = this;
			
			attributes.set(Attribute.sand, 1f);
		}};
		
		//neurospora
		
		neurosporaFloor = new Floor("neurospora-floor", 3) {{
			attributes.set(Attribute.water, 0.35f);
			status = TektonStatusEffects.neurosporaSlowed;
			statusDuration = 100f;
		}};
		
		neurosporaEvolvedFloor = new Floor("neurospora-evolved-floor", 3) {{
			attributes.set(Attribute.water, 0.7f);
			status = TektonStatusEffects.neurosporaSlowed;
			statusDuration = 100f;
		}};
		
		neurosporaFlower = new Prop("neurospora-flower") {{
            variants = 2;
            breakSound = Sounds.plantBreak;
        }};
        
        neurosporaWall = new StaticWall("neurospora-wall") {{
			variants = 3;
			neurosporaFloor.asFloor().wall = this;
			
			attributes.set(Attribute.sand, 0.25f);
		}};
		
		neurosporaCluster = new NotRotatedTreeBlock("neurospora-cluster") {{
            variants = 3;
            clipSize = 128f;
            rotate = false;
            rotateDraw = false;
            lockRotation = true;
        }};
		
		neurosporaVent = new SteamVent("neurospora-vent") {{
            parent = blendGroup = neurosporaFloor;
            attributes.set(Attribute.steam, 1f);
			variants = 2;
			status = TektonStatusEffects.neurosporaSlowed;
			statusDuration = 100f;
		}};
		
		//uraninite
		
		uraniniteFloor = new Floor("uraninite-floor", 4) {{
			attributes.set(Attribute.water, -0.5f);
			status = TektonStatusEffects.radioactiveContamination;
			statusDuration = 60f;
		}};
		
		uraniniteWall = new StaticWall("uraninite-wall") {{
			variants = 3;
			uraniniteFloor.asFloor().wall = this;
		}};
		
		uraniniteCrystal = new TallBlock("uraninite-crystal"){{
            variants = 3;
            clipSize = 128f;
        }};
		
		trinitite = new Floor("trinitite", 3) {{
			itemDrop = Items.sand;
			attributes.set(TektonAttributes.silica, 0.2f);
			playerUnmineable = true;
			//attributes.set(Attribute.water, 0.7f);
		}};
		
		uraniniteBoulder = new Prop("uraninite-boulder") {{
			variants = 2;
			uraniniteFloor.asFloor().decoration = this;
		}};
		
		//metal iron
		
		metalIronFloor = new Floor("metal-iron-floor", 0) {{
			attributes.set(Attribute.water, -1f);
		}};
		
		metalIronFloor2 = new Floor("metal-iron-floor-2", 0) {{
			attributes.set(Attribute.water, -1f);
		}};
		
		metalIronFloor3 = new Floor("metal-iron-floor-3", 0) {{
			attributes.set(Attribute.water, -1f);
		}};
		
		metalIronFloor4 = new Floor("metal-iron-floor-4", 0) {{
			attributes.set(Attribute.water, -1f);
		}};
		
		metalIronFloor5 = new Floor("metal-iron-floor-5", 0) {{
			attributes.set(Attribute.water, -1f);
		}};
		
		metalIronVent = new SteamVent("metal-iron-vent") {{
            parent = blendGroup = metalIronFloor2;
            attributes.set(Attribute.steam, 1f);
			variants = 2;
        }};
        
        metalIronExhaust = new SteamVent("metal-iron-exhaust") {{
            parent = blendGroup = metalIronFloor2;
            attributes.set(Attribute.steam, 1f);
			variants = 2;
        }};
        
        metalIronDarkFloor = new Floor("metal-iron-dark-floor", 0) {{
			attributes.set(Attribute.water, -1f);
		}};
		
		metalIronDarkFloor2 = new Floor("metal-iron-dark-floor-2", 0) {{
			attributes.set(Attribute.water, -1f);
		}};
		
		metalIronDarkFloor3 = new Floor("metal-iron-dark-floor-3", 0) {{
			attributes.set(Attribute.water, -1f);
		}};
		
		metalIronDarkFloor4 = new Floor("metal-iron-dark-floor-4", 0) {{
			attributes.set(Attribute.water, -1f);
		}};
		
		metalIronDarkFloor5 = new Floor("metal-iron-dark-floor-5", 0) {{
			attributes.set(Attribute.water, -1f);
		}};
        
        metalIronWall = new StaticWall("metal-iron-wall") {{
			variants = 4;
			metalIronFloor.asFloor().wall = this;
        }};
        
        Seq.with(metalIronFloor, metalIronFloor2, metalIronFloor3, metalIronFloor4, metalIronFloor5, 
        		metalIronDarkFloor, metalIronDarkFloor2, metalIronDarkFloor3, metalIronDarkFloor4, metalIronDarkFloor5).each(b -> b.asFloor().wall = metalIronWall);
        
        //methane ice
        
        methaneSnow = new Floor("methane-snow") {{
            attributes.set(Attribute.water, -0.2f);
            albedo = 0.7f;
			attributes.set(TektonAttributes.methane, 0.2f);
        }};
        
        methaneIce = new Floor("methane-ice") {{
            dragMultiplier = 0.35f;
            speedMultiplier = 0.9f;
            attributes.set(Attribute.water, -0.4f);
            albedo = 0.65f;
			attributes.set(TektonAttributes.methane, 0.4f);
        }};
        
        methaneIceWall = new StaticWall("methane-ice-wall") {{
        	methaneIce.asFloor().wall = this;
            albedo = 0.6f;
        }};
        
        methaneSnowBoulder = new Prop("methane-snow-boulder") {{
			variants = 2;
			methaneSnow.asFloor().decoration = this;
            albedo = 0.65f;
		}};
		
		//ores
        
		oreIron = new OreBlock("ore-iron", iron) {{
			oreDefault = true;
			oreThreshold = 0.864f;
			oreScale = 24.904762f;
			variants = 3;
		}};
		
		oreZirconium = new OreBlock("ore-zirconium", zirconium) {{
			oreDefault = true;
			oreThreshold = 0.864f;
			oreScale = 24.904762f;
			variants = 3;
		}};
		
		oreTantalum = new OreBlock("ore-tantalum", tantalum) {{
			oreDefault = true;
			oreThreshold = 0.864f;
			oreScale = 24.904762f;
			variants = 3;
		}};
		
		oreUranium = new OreBlock("ore-uranium", uranium) {{
			oreDefault = true;
			oreThreshold = 0.864f;
			oreScale = 24.904762f;
			variants = 3;
		}};
		
		ferricIronWall = new StaticWall("ferric-iron-wall") {{
			itemDrop = iron;
			variants = 3;
		}};
		
		zirconWall = new StaticWall("zircon-wall") {{
			itemDrop = zirconium;
			variants = 3;
		}};
		
		wallOreIron = new OreBlock("ore-wall-iron", iron) {{
			wallOre = true;
			variants = 3;
		}};
		
		wallOreTantalum = new OreBlock("ore-wall-tantalum", tantalum) {{
			wallOre = true;
			variants = 3;
		}};
		
		wallOreUranium = new OreBlock("ore-wall-uranium", uranium) {{
			wallOre = true;
			variants = 3;
		}};
		
		//crafting
		siliconFilter = new GenericCrafter("silicon-filter") {{
			requirements(Category.crafting, tek(), with(iron, 45, zirconium, 30));
			
			outputItem = new ItemStack(Items.silicon, 1);
			consumeItem(silica, 2);
			craftTime = 140f;
			size = 2;
			health = 360;
			hasItems = true;
			squareSprite = false;
            fogRadius = 2;

			craftEffect = new Effect(140f, e -> {
		        float length = 2f + e.finpow() * 12f;
		        TektonFx.rand.setSeed(e.id);
		        for(int i = 0; i < 11; i++){
		        	TektonFx.v.trns(TektonFx.rand.random(360f), TektonFx.rand.random(length));
		            float sizer = TektonFx.rand.random(1.0f, 2.8f);

		            e.scaled(e.lifetime * TektonFx.rand.random(0.5f, 1f), b -> {
		                color(Color.valueOf("ffc9de"), b.fslope() * 0.93f);

		                Fill.circle(e.x + TektonFx.v.x, e.y + TektonFx.v.y, sizer + b.fslope() * 1.2f);
		            });
		        }
		    }).startDelay(10f);
			ambientSound = Sounds.grinding;
			ambientSoundVolume = 0.08f;
			researchCostMultiplier = 0.1f;
		}};
		
		siliconCompressor = new GenericCrafter("silicon-compressor") {{
			requirements(Category.crafting, tek(), with(tantalum, 65, zirconium, 80, Items.silicon, 50, Items.graphite, 40));
            craftEffect = Fx.none;
			
			outputItem = new ItemStack(Items.silicon, 7);
			consumePower(160f / 60f);
			consumeItem(silica, 5);
			consumeItem(Items.graphite, 2);
			craftTime = 185f;
			size = 3;
			health = 670;
			itemCapacity = 20;
			hasItems = true;
			squareSprite = false;
            fogRadius = 3;

            drawer = new DrawMulti(
	        		new DrawRegion("-bottom"), 
	        		new DrawArcSmelt() {{ 
		            	flameColor = Color.valueOf("ffc9de");
		            	midColor = Color.white.cpy();
		            	circleStroke /= 1.5f;
		            	particleStroke /= 1.5f;
		            	flameRad /= 1.2f;
		            	particleRad /= 1.2f;
		            	circleSpace /= 1.2f;
	            	}}, 
	        		new DrawDefault(), 
	        		new DrawFlame(Color.valueOf("ffc9de")) {{ lightRadius = 20f; flameRadius = 9f / tilesize; flameRadiusIn = 4f / tilesize; }}
            	);
            ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.5f;
			researchCostMultiplier = 0.5f;
		}};
		
		graphiteConcentrator = new GenericCrafter("graphite-concentrator") {{
			requirements(Category.crafting, tek(), with(iron, 90, zirconium, 70, Items.silicon, 25));
			
			outputItem = new ItemStack(Items.graphite, 3);
			outputLiquid = new LiquidStack(Liquids.water, 10f / 60f);
			craftTime = 180f;
			size = 3;
			health = 620;
			hasPower = true;
			hasLiquids = true;
			squareSprite = false;
			ignoreLiquidFullness = true;
			liquidCapacity = 40;
            fogRadius = 3;
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.methane, 1f), new DrawDefault());

			consumePower(80f / 60f);
			consumeItem(silica, 1);
			consumeLiquid(TektonLiquids.methane, 20f / 60f);
			
			craftEffect = Fx.smeltsmoke;
			ambientSound = Sounds.grinding;
			ambientSoundVolume = 0.05f;
			researchCostMultiplier = 0.1f;
		}};
		
		coldElectrolyzer = new GenericCrafter("cold-electrolyzer") {{
			requirements(Category.crafting, tek(), with(zirconium, 100, Items.silicon, 60, Items.graphite, 80));
			consumePower(60f / 60f);
			consumeLiquid(Liquids.water, 10f / 60f);
			
			craftTime = 10f;
			size = 3;
			health = 540;
			squareSprite = false;
			hasPower = true;
			hasLiquids = true;
			rotate = true;
			invertFlip = true;
			group = BlockGroup.liquids;
			itemCapacity = 0;
			liquidCapacity = 50;
            fogRadius = 3;
			
			regionRotated1 = 3;
			liquidOutputDirections = new int[]{1, 3};
			outputLiquids = LiquidStack.with(TektonLiquids.oxygen, 3f / 60f, Liquids.hydrogen, 6f / 60f);
			
			ambientSound = Sounds.electricHum;
			ambientSoundVolume = 0.08f;
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.water, 5.6f), new DrawBubbles(Color.valueOf("7693e3")) {{
				sides = 10;
				recurrence = 3f;
				spread = 6;
				radius = 1.5f;
				amount = 20;
			}}, new DrawRegion("", 0, false), new DrawLiquidOutputs());
			
			researchCostMultiplier = 0.2f;
		}};
		
		atmosphericMethaneConcentrator = new AttributeCrafter("atmospheric-methane-concentrator") {{
			requirements(Category.crafting, tek(), with(zirconium, 80, Items.graphite, 40, Items.silicon, 60, magnet, 40));
			consumePower(40f / 60f);
			squareSprite = false;
			size = 3;
			craftTime = 60f;
			health = 540;
			liquidCapacity = 30f;
			
			attribute = TektonAttributes.methane;
			baseEfficiency = 1f;
			boostScale = 1f / 9f;
            fogRadius = 3;
			
			outputLiquid = new LiquidStack(TektonLiquids.methane, 10f / 60f);
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.methane, 0.96f), new DrawDefault(), new DrawParticles() {{
				color = TektonColor.methane;
				alpha = 0.6f;
				particleSize = 4;
				particles = 10;
				particleRad = 12;
				particleLife = 140;
				reverse = false;
			}});
			
			ambientSoundVolume = 0.06f;
			ambientSound = Sounds.extractLoop;
			craftEffect = Fx.none;
			
			researchCostMultiplier = 0.2f;
		}};
		
		polycarbonateSynthesizer = new GenericCrafter("polycarbonate-synthesizer") {{
			requirements(Category.crafting, tek(), with(iron, 80, Items.graphite, 60, zirconium, 80, Items.silicon, 50));
			consumePower(100f / 60f);
			squareSprite = true;
			size = 3;
			craftTime = 60f;
			health = 700;
			hasPower = true;
			hasLiquids = true;
            liquidCapacity = 30f;
            fogRadius = 3;
			
			consumeLiquid(Liquids.water, 10f / 60f);
			consumeLiquid(TektonLiquids.methane, 20f / 60f);
			outputItem = new ItemStack(polycarbonate, 2);
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.methane, 0.5f), new DrawRegion("-sub"), 
					new DrawLiquidTile(Liquids.water, 10.05f), new DrawDefault(), new DrawFade());

			craftEffect = new Effect(40, e -> {
				randLenVectors(e.id, 6, 5f + e.fin() * 8f, (x, y) -> {
					color(TektonColor.polycarbonateShootColor, Color.lightGray, e.fin());
					Fill.square(e.x + x, e.y + y, 0.2f + e.fout() * 2f, 45);
				});
			});
			updateEffect = new Effect(40, e -> {
				randLenVectors(e.id, 5, 3f + e.fin() * 5f, (x, y) -> {
					color(TektonColor.polycarbonateShootColor, Color.gray, e.fin());
					Fill.circle(e.x + x, e.y + y, e.fout());
				});});
			
			ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.5f;
			
			researchCostMultiplier = 0.4f;
		}};
		
		magnetizer = new GravityProducer("magnetizer") {{
			requirements(Category.crafting, tek(), with(iron, 120, tantalum, 80, Items.graphite, 60, Items.silicon, 100));
			consumePower(120f / 60f);
			squareSprite = true;
			size = 3;
			craftTime = 60f * 2f;
			health = 840;
			hasPower = true;
			hasLiquids = true;
            liquidCapacity = 20f;
            fogRadius = 3;
            
            gravityOutput = 4 * gravityMul;
			
			consumeItem(iron, 1);
			consumeLiquid(TektonLiquids.oxygen, 1f / 60f);
			outputItem = new ItemStack(magnet, 1);
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.oxygen), 
					new DrawPlasma() {{ plasmas = 2; plasma1 = TektonColor.gravityColor; plasma2 = polycarbonate.color; }}, 
					new DrawDefault(), new DrawGlowRegion() {{ color = TektonColor.gravityColor; }}, new DrawGravityOutput());

			craftEffect = new WaveEffect() {{
				sizeFrom = 2;
				sizeTo = 14;
				lifetime = 20f;
				strokeFrom = 2;
				strokeTo = 0;
				colorFrom = Color.valueOf("ffffff");
				colorTo = magnet.color.cpy();
			}};
			updateEffect = Fx.none;
			
			ambientSound = Sounds.extractLoop;
            ambientSoundVolume = 0.08f;
			
			researchCostMultiplier = 1f;
		}};
		
		cryogenicMixer = new GenericCrafter("cryogenic-mixer") {{
			requirements(Category.crafting, tek(), with(zirconium, 65, tantalum, 70, Items.silicon, 60));
			consumePower(80f / 60f);
			squareSprite = true;
			size = 3;
			craftTime = 60f;
			health = 800;
			hasPower = true;
			hasLiquids = true;
            fogRadius = 3;
			
			consumeLiquid(Liquids.water, 4f / 60f);
			consumeItem(tantalum);
			outputItem = new ItemStack(cryogenicCompound, 1);

            drawLiquidLight = true;
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.water), new DrawDefault(), new DrawGlowRegion() {{
				alpha = 1f;
				glowScale = 5f;
				color = Color.valueOf("87ceeb99");
			}}, new DrawLight(Liquids.cryofluid.color));
			
			/*ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.9f;*/
			
			researchCostMultiplier = 0.5f;
		}};
		
		sandFilter = new GenericCrafter("sand-filter") {{
			requirements(Category.crafting, tek(), with(iron, 60, zirconium, 80, Items.graphite, 60, polycarbonate, 30));
			consumePower(30f / 60f);
			squareSprite = false;
			size = 3;
			craftTime = 60f * 2f;
			itemCapacity = 10;
			health = 600;
			hasPower = true;
			hasLiquids = true;
            fogRadius = 3;
			
			consumeLiquid(Liquids.water, 2f / 60f);
			consumeItem(Items.sand, 3);
			outputItem = new ItemStack(silica, 2);

			ambientSound = Sounds.bioLoop;
			ambientSoundVolume = 0.08f;
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawItemFulness(Items.sand) {{ alpha = 0.65f; }}, 
					new DrawLiquidTile(Liquids.water) {{ alpha = 0.85f; }}, new DrawItemFulness(silica) {{ alpha = 0.75f; }}, 
					new DrawRegion("-rotator", 0.8f, true), new DrawDefault());
			
			researchCostMultiplier = 0.3f;
		}};
		
		ammoniaCatalyst = new LiquidConverter("ammonia-catalyst") {{
			requirements(Category.crafting, tek(), with(polycarbonate, 100, magnet, 40, Items.graphite, 60, tantalum, 80));
			consumePower(60f / 60f);
			squareSprite = false;
			size = 3;
			craftTime = 60f;
			itemCapacity = 0;
			health = 600;
			hasPower = true;
			hasLiquids = true;
			liquidCapacity = 30;
            fogRadius = 3;
            
            liquidConsumption = 10f / 60f;

			consumeLiquid(TektonLiquids.ammonia, liquidConsumption).update(false).boost();
			consumeLiquid(TektonLiquids.methane, liquidConsumption).update(false).boost();
			consumeLiquid(TektonLiquids.acid, liquidConsumption).update(false).boost();
			
			convertableLiquids = new LiquidStack[] {
					new LiquidStack(TektonLiquids.ammonia, 10f / 60f),
					new LiquidStack(TektonLiquids.methane, 15f / 60f),
					new LiquidStack(TektonLiquids.acid, 20f / 60f)
			};
			
			outputLiquid = new LiquidStack(Liquids.hydrogen, 3f / 60f);

			ambientSound = Sounds.electricHum;
			ambientSoundVolume = 0.08f;
			
			var pad = 8f - (10f / 32f);
			var alp = 0.7f;
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.hydrogen, 0.96f), new DrawRegion("-middle"), 
					new DrawLiquidTile(TektonLiquids.acid, pad) {{ alpha = alp; }}, 
					new DrawLiquidTile(TektonLiquids.methane ,pad) {{ alpha = alp; }}, 
					new DrawLiquidTile(TektonLiquids.ammonia, pad) {{ alpha = alp; }}, 
					new DrawCircles(){{
		                color = Liquids.hydrogen.color.cpy().mul(1.3f).a(1f);
		                strokeMax = 1f;
		                strokeMin = 0f;
		                radius = 6f;
		                amount = 2;
		                sides = 4;
		            }},
					new DrawDefault(), new DrawGlowRegion() {{ color = Liquids.hydrogen.color.cpy(); }}, new DrawLight(Liquids.hydrogen.color));
			
			researchCostMultiplier = 0.3f;
		}};
		
		dicyonogenEmitter = new GravityCrafter("dicyanogen-emitter") {{
			requirements(Category.crafting, tek(), with(uranium, 80, Items.graphite, 80, polycarbonate, 100, Items.silicon, 80));
			consumePower(120f / 60f);
			squareSprite = false;
			size = 3;
			craftTime = 120f;
			health = 820;
			hasPower = true;
			hasLiquids = true;
            liquidCapacity = 40f;
            fogRadius = 3;

        	requiredGravity = 4 * gravityMul;
        	maxGravity = requiredGravity * 4;
			
			consumeLiquid(TektonLiquids.ammonia, 10f / 60f);
			consumeItem(Items.graphite, 2);
			
			outputLiquid = new LiquidStack(TektonLiquids.dicyanogen, 3f / 60f);
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.ammonia), new DrawRegion("-middle"), 
					new DrawLiquidTile(TektonLiquids.dicyanogen, 9f), new DrawDefault(), 
					new DrawGravityRegion("-gravity-glow"), new DrawGravityInput(), 
					new DrawGlowRegionOffset("-glow1") {{ offset = 0.666f * craftTime; glowScale = 7f; color = TektonColor.gravityColor; }}, 
					new DrawGlowRegionOffset("-glow2") {{ offset = 0.333f * craftTime; glowScale = 7f; color = TektonColor.gravityColor; }}, 
					new DrawGlowRegionOffset("-glow3") {{ glowScale = 7f; color = TektonColor.gravityColor; }});

			craftEffect = updateEffect = Fx.none;
			
			ambientSound = Sounds.extractLoop;
            ambientSoundVolume = 0.08f;
			
			researchCostMultiplier = 0.4f;
		}};
		
		polytalumFuser = new GravityCrafter("polytalum-fuser") {{
			requirements(Category.crafting, tek(), with(magnet, 80, Items.silicon, 120, tantalum, 140, polycarbonate, 140));
			consumePower(160f / 60f);
			size = 3;
			craftTime = 80f;
			health = 600;
			hasPower = true;
			hasLiquids = true;
            fogRadius = 3;

        	requiredGravity = 8 * gravityMul;
        	maxGravity = requiredGravity * 2;

			consumeItems(with(polycarbonate, 2, tantalum, 2));
			consumeLiquids(LiquidStack.with(TektonLiquids.acid, 20f / 60f, TektonLiquids.methane, 40f / 60f));
			
			outputItem = new ItemStack(polytalum, 1);
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.methane), new DrawRegion("-middle"), 
					new DrawLiquidTile(TektonLiquids.acid, 9f), new DrawDefault(), new DrawGlowRegion(), new DrawGlowRegion("-glow2") {{ glowScale = -glowScale; }}, 
					new DrawGlowRegion("-glow3") {{ glowScale = 0; color = TektonLiquids.acid.color; }}, new DrawLight(TektonLiquids.acid.color), 
					new DrawGravityRegion("-gravity-glow"), new DrawGravityInput());

			craftEffect = new Effect(40, e -> {
				randLenVectors(e.id, 6, 5f + e.fin() * 8f, (x, y) -> {
					color(TektonColor.polytalumShootColor, TektonColor.polycarbonateShootColor, e.fin());
					Fill.square(e.x + x, e.y + y, 0.2f + e.fout() * 2f, 45);
				});
			});
			updateEffect = new Effect(40, e -> {
				randLenVectors(e.id, 5, 3f + e.fin() * 5f, (x, y) -> {
					color(TektonColor.polytalumShootColor, TektonColor.polycarbonateShootColor, e.fin());
					Fill.circle(e.x + x, e.y + y, e.fout());
				});});
			
			ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.4f;
			
			researchCostMultiplier = 0.6f;
		}};
        
        nanoAlloyCrucible = new GravityCrafter("nano-alloy-crucible") {{
            requirements(Category.crafting, tek(), with(magnet, 80, tantalum, 200, Items.silicon, 180, uranium, 160, Items.graphite, 120));
            health = 1080;
            size = 4;
        	
        	requiredGravity = 12 * gravityMul;
        	maxGravity = requiredGravity * 4;
			craftTime = 180f;
			hasPower = true;

            envEnabled |= Env.space;

            craftEffect = Fx.smeltsmoke;
            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.7f;

            fogRadius = 4;
            itemCapacity = 25;
            
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawArcSmelt() {{ flameColor = midColor = Color.valueOf("f57627"); }}, 
            		new DrawLinesToCenter() {{ lineColor = particleColor = Color.valueOf("f57627"); distanceCenter = 8f * 2.5f; lineRadiusMag = 0.6f; }}, 
            		new DrawGlowRegion() {{ color = Color.valueOf("f57627"); layer = Layer.block; }}, 
            		new DrawDefault(), new DrawGravityRegion("-gravity-glow"), new DrawGlowRegion("-glow2"), new DrawGravityInput(), 
            		new DrawLight(Color.valueOf("f57627"), 45f) {{ lightSinMag = 4f; }});

			outputItem = new ItemStack(nanoAlloy, 1);
        	
			consumeItem(iron, 4);
			consumeItem(zirconium, 2);
			consumeItem(Items.silicon, 3);
			consumeItem(tantalum, 2);

            consumePower(200f / 60f);
        }};
		
		phasePrinter = new GravityCrafter("phase-printer") {{
            requirements(Category.crafting, tek(), with(polytalum, 100, tantalum, 140, Items.silicon, 240, Items.graphite, 200, uranium, 140));
            health = 820;
            size = 4;
			
        	requiredGravity = 8 * gravityMul;
        	maxGravity = requiredGravity * 4;
        	
        	
			craftTime = 240f;
			hasPower = true;
			hasLiquids = true;
            envEnabled |= Env.space;

            craftEffect = Fx.smeltsmoke;
            ambientSound = Sounds.techloop;
            ambientSoundVolume = 0.02f;
            
            fogRadius = 4;
            itemCapacity = 40;
            liquidCapacity = 40;
        	
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(), new DrawRegion("-middle"), new DrawSpikes() {{
                color = Color.valueOf("ffd59e");
                stroke = 1.5f;
                layers = 2;
                amount = 12;
                rotateSpeed = 0.5f;
                layerSpeed = -0.9f;
                radius = 7f;
                length = 5.5f;
            }}, new DrawMultiWeave() {{ glowColor = new Color(1f, 0.4f, 0.4f, 0.8f); }}, new DrawGravityRegion("-gravity-glow"), new DrawGravityInput(), 
            		new DrawGlowRegion() {{ color = new Color(1f, 0.4f, 0.3f, 1f); }}, new DrawDefault(), new DrawLight(Items.phaseFabric.color, 40f) {{ lightSinMag = 4f; }});
            
            consumePower(320f / 60f);
            consumeItems(with(uranium, 2, TektonItems.silica, 8));
			consumeLiquid(TektonLiquids.oxygen, 1f / 60f);
			
			outputItem = new ItemStack(Items.phaseFabric, 1);
			
			researchCostMultiplier = 0.9f;
		}};
		
		hydrogenIncinerator = new ItemIncinerator("hydrogen-incinerator") {{
            requirements(Category.crafting, tek(), with(Items.graphite, 5, zirconium, 15));
            effect = TektonFx.incinerateHydrogen;
            size = 1;
            health = 90;
            envEnabled |= Env.space;
            fogRadius = 1;
            
            consumePower(0.50f / 3f);
            consumeLiquid(Liquids.hydrogen, 0.1f / 60f);
        }};
        
        //gravity stuff
        
        electricalCoil = new GravityProducer("electrical-coil") {{
        	requirements(Category.crafting, tek(), with(iron, 75, Items.silicon, 40, magnet, 10));
			health = 320;
            size = 2;
            fogRadius = 2;
			
            researchCostMultiplier = 3f;
            
            drawer = new DrawMulti(new DrawDefault(), new DrawGravityOutput());
            rotateDraw = false;
            gravityOutput = 1 * gravityMul;
            regionRotated1 = 1;
            itemCapacity = 0;
            consumePower(40f / 60f);

            ambientSound = TektonSounds.gravity;
            ambientSoundVolume = 0.03f;

			researchCostMultiplier = 0.2f;
        }};
        
        thermalCoil = new GravityProducer("thermal-coil") {{
        	requirements(Category.crafting, tek(), with(iron, 110, Items.silicon, 70, magnet, 30, tantalum, 45));
			health = 400;
            size = 2;
            fogRadius = 2;
			
            researchCostMultiplier = 2f;
            
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawItemLiquidTile(Liquids.cryofluid, cryogenicCompound), new DrawDefault(), new DrawGravityOutput());
            rotateDraw = false;
            gravityOutput = 4 * gravityMul;
            regionRotated1 = 1;
            consumeItem(cryogenicCompound, 1);
            craftTime = 60f * 4f;
            consumePower(80f / 60f);

            ambientSound = TektonSounds.gravity;
            ambientSoundVolume = 0.04f;
            
			researchCostMultiplier = 0.4f;
        }};
        
        phaseNanoCoil = new GravityProducer("phase-nano-coil") {{
        	requirements(Category.crafting, tek(), with(iron, 180, Items.silicon, 120, nanoAlloy, 40, magnet, 50));
			health = 680;
            size = 3;
            fogRadius = 3;
			
            researchCostMultiplier = 2f;
            
            drawer = new DrawMulti(new DrawDefault(), new DrawGravityOutput());
            rotateDraw = false;
            gravityOutput = 12 * gravityMul;
            regionRotated1 = 1;
            consumeItem(Items.phaseFabric, 1);
            craftTime = 60f * 8f;
            consumePower(80f / 60f);

            ambientSound = TektonSounds.gravity;
            ambientSoundVolume = 0.05f;
            
			researchCostMultiplier = 0.55f;
        }};
		
		gravityConductor = new GravityConductor("gravity-conductor") {{
			requirements(Category.crafting, tek(), with(iron, 20, Items.silicon, 10));
			health = 280;
			size = 2;
            fogRadius = 2;
			
            researchCostMultiplier = 5f;
			
            group = BlockGroup.heat;
            //drawer = new DrawMulti(new DrawDefault(), new DrawGravityOutput(), new DrawGravityInput() {{ drawSides = false; }});

			//ambientSound = Sounds.electricHum;
			//ambientSoundVolume = 0.1f;
		}};
		
		nanoGravityConductor = new GravityConductor("nano-gravity-conductor") {{
			requirements(Category.crafting, tek(), with(Items.silicon, 8, nanoAlloy, 4));
			health = 140;
			size = 1;
            fogRadius = 1;
			
            researchCostMultiplier = 5f;
			
            group = BlockGroup.heat;
            //drawer = new DrawMulti(new DrawDefault(), new DrawGravityOutput(), new DrawGravityInput() {{ drawSides = false; }});

			//ambientSound = Sounds.electricHum;
			//ambientSoundVolume = 0.1f;
		}};
		
		gravityRouter = new GravityConductor("gravity-router") {{
            requirements(Category.crafting, tek(), with(iron, 20, Items.silicon, 15, tantalum, 10));
			health = 320;
            size = 2;
            fogRadius = 2;
			
            researchCostMultiplier = 10f;

            group = BlockGroup.heat;
            drawer = new DrawMulti(new DrawDefault(), new DrawGravityOutput(-1, false), new DrawGravityOutput(), new DrawGravityOutput(1, false), new DrawGravityInput());
            regionRotated1 = 1;
            splitGravity = true;

			//ambientSound = Sounds.electricHum;
			//ambientSoundVolume = 0.1f;
        }};
        
        gravitySource = new GravityProducer("gravity-source") {{
            requirements(Category.crafting, BuildVisibility.sandboxOnly, with());
            drawer = new DrawMulti(new DrawDefault(), new DrawGravityOutput());
            rotateDraw = false;
            size = 1;
            gravityOutput = 1000 * gravityMul;
            warmupRate = 1000f;
            regionRotated1 = 1;
            itemCapacity = 0;
            alwaysUnlocked = true;
            ambientSound = Sounds.none;
            //allDatabaseTabs = true;
        }};
		
		//defense
		var ironLife = 480;
		var polycarbonateLife = 360;
		var tantalumLife = 720;
		var uraniumLife = 1020;
		var polytalumLife = 640;
		var nanoAlloyLife = 920;
		
		ironWall = new Wall("iron-wall") {{
			requirements(Category.defense, tek(), with(iron, 6));
			health = ironLife;
			armor = 1f;
			alwaysUnlocked = true;
			researchCostMultiplier = 0f;
		}};
		
		ironWallLarge = new Wall("iron-wall-large") {{
			requirements(Category.defense, tek(), with(iron, 24));
			health = ironLife * 4;
			armor = 1f;
			researchCostMultiplier = 0.05f;
			size = 2;
		}};
		
		tantalumWall = new Wall("tantalum-wall") {{
			requirements(Category.defense, tek(), with(tantalum, 6));
			health = tantalumLife;
			armor = 8f;
			buildCostMultiplier = 2f;
			researchCostMultiplier = 0.5f;
		}};
		
		tantalumWallLarge = new Wall("tantalum-wall-large") {{
			requirements(Category.defense, tek(), with(tantalum, 24));
			health = tantalumLife * 4;
			armor = 8f;
			size = 2;
			buildCostMultiplier = 2f;
			researchCostMultiplier = 0.5f;
		}};

		gate = new AutoDoor("gate") {{
            requirements(Category.defense, tek(), with(tantalum, 24, Items.silicon, 24));
            health = (tantalumLife * 4) - 80;
            armor = 14f;
            size = 2;
        }};
		
		polycarbonateWall = new Wall("polycarbonate-wall") {{
			requirements(Category.defense, tek(), with(polycarbonate, 6));
			health = polycarbonateLife;
			insulated = true;
			absorbLasers = true;
			buildCostMultiplier = 3.5f;
			researchCostMultiplier = 0.2f;
		}};
		
		polycarbonateWallLarge = new Wall("polycarbonate-wall-large") {{
			requirements(Category.defense, tek(), with(polycarbonate, 24));
			health = polycarbonateLife * 4;
			size = 2;
			insulated = true;
			absorbLasers = true;
			buildCostMultiplier = 3.5f;
			researchCostMultiplier = 0.2f;
		}};
		
		polytalumWall = new Wall("polytalum-wall") {{
			requirements(Category.defense, tek(), with(polytalum, 6));
			health = polytalumLife;
			armor = 14f;
			insulated = true;
			absorbLasers = true;
			buildCostMultiplier = 3f;
			researchCostMultiplier = 0.5f;
		}};
		
		polytalumWallLarge = new Wall("polytalum-wall-large") {{
			requirements(Category.defense, tek(), with(polytalum, 24));
			health = polytalumLife * 4;
			armor = 14f;
			size = 2;
			insulated = true;
			absorbLasers = true;
			buildCostMultiplier = 3f;
			researchCostMultiplier = 0.5f;
		}};
		
		uraniumWall = new Wall("uranium-wall") {{
			requirements(Category.defense, tek(), with(uranium, 6, zirconium, 3));
			health = uraniumLife;
			armor = 17f;
			buildCostMultiplier = 2.5f;
			researchCostMultiplier = 0.5f;
		}};
		
		uraniumWallLarge = new Wall("uranium-wall-large") {{
			requirements(Category.defense, tek(), with(uranium, 24, zirconium, 12));
			health = uraniumLife * 4;
			armor = 17f;
			size = 2;
			buildCostMultiplier = 2.5f;
			researchCostMultiplier = 0.5f;
		}};
		
		float nanoPower = 4f;
		
		nanoAlloyWall = new AdvancedWall("nano-alloy-wall") {{
			requirements(Category.defense, tek(), with(nanoAlloy, 6));
			health = nanoAlloyLife;
			armor = 20f;
			buildCostMultiplier = 3f;
			lightningChance = 0.05f;
			chanceDeflect = 2f;
			flashHit = true;
			flashColor = Color.red;
			researchCostMultiplier = 0.5f;
			suppressable = true;
            fogRadius = 1;

			consumePower((nanoPower / 60f) / 4f);
		}};
		
		nanoAlloyWallLarge = new AdvancedWall("nano-alloy-wall-large") {{
			requirements(Category.defense, tek(), with(nanoAlloy, 24));
			health = nanoAlloyLife * 4;
			armor = 20f;
			size = 2;
			buildCostMultiplier = 3f;
			lightningChance = 0.05f;
			chanceDeflect = 2f;
			flashHit = true;
			flashColor = Color.red;
			lightRadius = 5f;
			researchCostMultiplier = 0.5f;
			suppressable = true;
            fogRadius = 2;
	        
	        outputsPower = false;
	        hasPower = true;
	        consumesPower = true;
	        conductivePower = true;
			
			consumePower(nanoPower / 60f);
		}};
		
		//defense
		
		lamp = new TeamLight("lamp") {{
            requirements(Category.effect, tek(), with(Items.silicon, 4, zirconium, 4));
            brightness = 0.75f;
            radius = 90f;
    		fogRadius = (int)(radius * 0.75f) / Vars.tilesize;
            consumePower(0.05f);
        }};
		
		researchRadar = new TeamRadar("research-radar") {{
			requirements(Category.effect, tek(), with(Items.silicon, 40, Items.graphite, 25, iron, 80));
			health = 100;
			outlineColor = tektonOutlineColor;
			fogRadius = 50;
			researchCost = with(Items.silicon, 80, Items.graphite, 70, iron, 120);
			size = 2;
			
			consumePower(1f);
		}};
		
		sensor = new CoreRadar("sensor") {{
			requirements(Category.effect, tek(), with(Items.silicon, 160, Items.graphite, 80, polycarbonate, 80, tantalum, 100));
			size = 3;
			outlineColor = tektonOutlineColor;
			health = 400;
			fogRadius = 4;
			lightRadius = 100f;
			squareSprite = false;

			consumeLiquid(Liquids.hydrogen, 3f / 60f);
			consumePower(180f / 60f);
		}};
		
		builderDroneCenter = new BuilderUnitFactory("builder-drone-center") {{
			requirements(Category.effect, tek(), with(Items.silicon, 200, uranium, 120, polytalum, 80, magnet, 40));
			size = 4;
			fogRadius = 4;
			health = 780;
			squareSprite = false;

			consumePower(240f / 60f);
			unitBuildTime = 60f * 16f;
			consumeLiquid(TektonLiquids.dicyanogen, 3f / 60f);
			unitType = TektonUnits.builderDrone;
		}};
		
		var regenFactor = 0.5f;
		
		regenerator = new Regenerator("regenerator") {{
			requirements(Category.effect, tek(), with(Items.silicon, 60, Items.graphite, 40, iron, 80));
			size = 2;
			range = 24;
			health = 140;
			squareSprite = false;
			
			itemCapacity = 10;
			liquidCapacity = 15f;

			effect = TektonFx.regenParticleHydrogen;
			
			consumePower(30f / 60f);
			consumeLiquid(Liquids.hydrogen, 1f / 60f);
			consumeItem(Items.phaseFabric).boost();
			
			baseColor = Pal.heal;
			
			healPercent = regenFactor / 60f;

            Color col = Pal.heal;
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.hydrogen, 9f / 4f), new DrawDefault(), new DrawGlowRegion() {{
				color = Color.sky;
			}}, new DrawPulseShape(false) {{
				layer = Layer.effect;
				color = col;
			}}, new DrawShape() {{
				layer = Layer.effect;
				radius = 3.5f;
				useWarmupRadius = true;
				timeScl = 2f;
				color = col;
			}}, new DrawWarmupRegion() {{ color = Color.sky; }});
		}};
		
		regenerationDome = new Regenerator("regeneration-dome") {{
			requirements(Category.effect, tek(), with(Items.silicon, 120, magnet, 20, iron, 140, polycarbonate, 60));
			size = 3;
			range = 48;
			health = 360;
			squareSprite = false;
			
			itemCapacity = 20;
			liquidCapacity = 40f;
			
			effect = TektonFx.regenParticleOxygen;
			
			consumePower(100f / 60f);
			consumeLiquid(TektonLiquids.oxygen, 1f / 60f);
			consumeItem(Items.phaseFabric, 2).boost();
			
			baseColor = Pal.heal;
			
			healPercent = (regenFactor / 60f) * 4f;
			
			Color col = Pal.heal;
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.oxygen, 9f / 4f), new DrawDefault(), new DrawGlowRegion() {{
				color = TektonColor.oxygen;
			}}, new DrawPulseShape(false) {{
				layer = Layer.effect;
				color = col;
			}}, new DrawShape() {{
				layer = Layer.effect;
				radius = 3.5f;
				useWarmupRadius = true;
				timeScl = 2f;
				color = col;
			}}, new DrawWarmupRegion() {{ color = TektonColor.oxygen; }});
		}};
		
		//transport blocks
		
		var ductSpeed = 11.1f;
		
		ironDuct = new Duct("iron-duct") {{
			requirements(Category.distribution, tek(), with(iron, 1));
			health = 80;
			speed = ductSpeed;
			alwaysUnlocked = true;
		}};
		
		tantalumDuct = new Duct("tantalum-duct") {{
			requirements(Category.distribution, tek(), with(iron, 1, tantalum, 1));
			health = 140;
			speed = ductSpeed;
			armored = true;
		}};
		
		ironRouter = new Router("iron-duct-router") {{
			requirements(Category.distribution, tek(), with(iron, 4));
			health = 100;
			itemCapacity = 2;
			speed = ductSpeed;
			researchCostMultiplier = 0.1f;
		}};
		
		ironDistributor = new Router("iron-duct-distributor") {{
			requirements(Category.distribution, tek(), with(iron, 20));
			size = 2;
			health = 400;
			itemCapacity = 8;
			speed = ductSpeed;
			researchCostMultiplier = 0.2f;
		}};
		
		ironOverflow = new OverflowGate("iron-duct-overflow-controller") {{
			requirements(Category.distribution, tek(), with(iron, 4, zirconium, 2));
			health = 100;
			speed = ductSpeed;
			researchCostMultiplier = 0.1f;
		}};
		
		ironUnderflow = new OverflowGate("iron-duct-underflow-controller") {{
			requirements(Category.distribution, tek(), with(iron, 4, zirconium, 2));
			health = 100;
			speed = ductSpeed;
			researchCostMultiplier = 0.1f;
			invert = true;
		}};
		
		ironSorter = new Sorter("iron-duct-sorter") {{
			requirements(Category.distribution, tek(), with(iron, 4, zirconium, 2));
			health = 100;
			researchCostMultiplier = 0.1f;
		}};
		
		ironInvertedSorter = new Sorter("iron-duct-sorter-inverted") {{
			requirements(Category.distribution, tek(), with(iron, 4, zirconium, 2));
			health = 100;
			researchCostMultiplier = 0.1f;
			invert = true;
		}};
		
		ironBridge = new DuctBridge("iron-duct-bridge") {{
			requirements(Category.distribution, tek(), with(iron, 20, zirconium, 15));
			health = 200;
			speed = ductSpeed;
			range = 4;
			researchCostMultiplier = 0.1f;
			squareSprite = false;

			((Duct)ironDuct).bridgeReplacement = this;
			((Duct)tantalumDuct).bridgeReplacement = this;
		}};
		
		ironUnloader = new DirectionalUnloader("iron-unloader") {{
			requirements(Category.distribution, tek(), with(Items.graphite, 20, Items.silicon, 20, iron, 40));
			health = 120;
			speed = 4f;
			solid = false;
			underBullets = true;
			regionRotated1 = 1;
		}};
		
		nanoConveyor = new PoweredConveyor("nano-conveyor") {{
			requirements(Category.distribution, tek(), with(tantalum, 1, nanoAlloy, 1));
            health = 200;
            speed = 0.09f;
            enhancedSpeed = 0.22f;
            displayedSpeed = 28f;
            
            bridgeReplacement = ironBridge;
            
			consumePower(1f / 60f);
            researchCost = with(nanoAlloy, 30, tantalum, 80);
		}};
		
		nanoJunction = new PoweredJunction("nano-junction") {{
			requirements(Category.distribution, tek(), with(tantalum, 2, nanoAlloy, 2));
			health = 240;
			itemCapacity = 2;
			var sp = 4f;
			speed = sp * 3.14f;
			enhancedSpeed = sp;

            underBullets = true;
            solid = false;
            
            hasPower = consumesPower = conductivePower = true;

			consumePower(2f / 60f);
            researchCost = with(nanoAlloy, 30, tantalum, 80);
            
    		((PoweredConveyor)nanoConveyor).junctionReplacement = this;
		}};
		
		nanoRouter = new Router("nano-router") {{
			requirements(Category.distribution, tek(), with(tantalum, 1, nanoAlloy, 5));
			health = 240;
			itemCapacity = 2;
			speed = 0.22f;

            underBullets = true;
            solid = false;
            
            hasPower = conductivePower = true;
            consumesPower = false;

			//consumePower(1f / 60f);
            researchCost = with(nanoAlloy, 50, tantalum, 80);
            
            buildVisibility = BuildVisibility.hidden;
		}};
		
		//liquid
		
		pneumaticPump = new Pump("pneumatic-pump") {{
			requirements(Category.liquid, tek(), with(iron, 25, zirconium, 15));
			health = 300;
			size = 2;
			squareSprite = false;
			//hasLiquids = true;
			liquidCapacity = 30f;
			/*floating = true;
			placeableLiquid = true;*/
			pumpAmount = 10f / 60f / 4f;
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawPumpLiquidDynamic() {{
				padding = 2f;
			}}, new DrawRegion("-sub"), new DrawRegion("-rotator", 0.8f, true), new DrawDefault());
			
			researchCostMultiplier = 0.2f;
		}};
		
		pressurePump = new Pump("pressure-pump") {{
			requirements(Category.liquid, tek(), with(Items.graphite, 40, zirconium, 80, tantalum, 50));
			health = 550;
			size = 3;
			squareSprite = false;
			//hasLiquids = true;
			liquidCapacity = 100f;
			/*floating = true;
			placeableLiquid = true;*/
			pumpAmount = 40f / 60f / 9f;
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawPumpLiquidDynamic() {{
				padding = 4f;
			}}, new DrawRegion("-sub"), new DrawPistons() {{ angleOffset = 45f; }}, new DrawRegion("-rotator", 4f, true), new DrawDefault());
			consumePower(1f);
			
			researchCostMultiplier = 0.5f;
		}};
		
		pipe = new Conduit("zirconium-pipe") {{
			requirements(Category.liquid, tek(), with(zirconium, 1));
			health = 90;
			leaks = true;
			liquidCapacity = 20f;
			underBullets = true;
            researchCostMultiplier = 3;
			botColor = Color.valueOf("3d352f");
            //explosivenessScale = flammabilityScale = 20f/50f;
		}};
		
		polycarbonatePipe = new Conduit("polycarbonate-pipe") {{
			requirements(Category.liquid, tek(), with(zirconium, 1, polycarbonate, 1));
			health = 160;
			leaks = true;
			liquidCapacity = 30f;
			underBullets = true;
			liquidPressure = 1.025f;
            researchCostMultiplier = 3;
			botColor = Color.valueOf("3d352f");
            //explosivenessScale = flammabilityScale = 20f/50f;
		}};
		
		polytalumPipe = new ArmoredConduit("polytalum-pipe") {{
			requirements(Category.liquid, tek(), with(polycarbonate, 1, polytalum, 1));
			health = 250;
			leaks = false;
			liquidCapacity = 30f;
			underBullets = true;
			liquidPressure = 1.025f;
            researchCostMultiplier = 3;
			botColor = Color.valueOf("3d352f");
            //explosivenessScale = flammabilityScale = 20f/50f;
		}};
		
		pipeJunction = new LiquidJunction("pipe-junction") {{
			requirements(Category.liquid, tek(), with(zirconium, 4, Items.graphite, 2));
			health = 100;
			solid = false;
			underBullets = true;
			liquidCapacity = 7;
			squareSprite = false;
			((Conduit)pipe).junctionReplacement = this;
			((Conduit)polycarbonatePipe).junctionReplacement = this;
			researchCostMultiplier = 0.5f;
		}};

		bridgePipe = new DirectionLiquidBridge("bridge-pipe") {{
			requirements(Category.liquid, tek(), with(iron, 4, zirconium, 4, Items.graphite, 2));
			health = 170;
			range = 4;
			((Conduit)pipe).rotBridgeReplacement = this;
			researchCostMultiplier = 0.3f;
		}};
		
		polycarbonateBridgePipe = new DirectionLiquidBridge("polycarbonate-bridge-pipe") {{
			requirements(Category.liquid, tek(), with(polycarbonate, 4, zirconium, 6, Items.graphite, 4));
			health = 250;
			range = 6;
			((Conduit)polycarbonatePipe).rotBridgeReplacement = this;
			researchCostMultiplier = 0.4f;
		}};
		
		pipeRouter = new LiquidRouter("pipe-router") {{
			requirements(Category.liquid, tek(), with(zirconium, 4, Items.graphite, 2));
			health = 100;
			solid = false;
			underBullets = true;
			liquidCapacity = 7;
			squareSprite = false;
			researchCostMultiplier = 0.5f;
		}};
		
		polycarbonateLiquidContainer = new LiquidRouter("polycarbonate-liquid-container") {{
			requirements(Category.liquid, tek(), with(zirconium, 14, polycarbonate, 12));
			size = 2;
			solid = true;
			liquidCapacity = 1000f;
			liquidPadding = 6f/4f;
			squareSprite = false;
			researchCostMultiplier = 0.6f;
		}};
		
		polycarbonateLiquidReserve = new LiquidRouter("polycarbonate-liquid-reserve") {{
			requirements(Category.liquid, tek(), with(tantalum, 40, polycarbonate, 50));
			size = 3;
			solid = true;
			liquidCapacity = 3000f;
			liquidPadding = 2f;
			squareSprite = false;
			researchCostMultiplier = 0.7f;
		}};
		
		//power
		
		lineNode = new LineNode("line-node") {{
			requirements(Category.power, tek(), with(iron, 5, Items.silicon, 1));
			laserColor1 = Color.valueOf("fffffff0");
			laserColor2 = Color.valueOf("ffd8b880");
			health = 140;
			range = 7;
			consumesPower = outputsPower = true;
			fogRadius = 1;
			researchCostMultiplier = 0.5f;
			
			consumePowerBuffered(100f);
		}};
		
		lineTower = new LineNode("line-tower") {{
			requirements(Category.power, tek(), with(iron, 30, tantalum, 10, Items.silicon, 15));
			laserColor1 = Color.valueOf("fffffff0");
			laserColor2 = Color.valueOf("ffd8b880");
			health = 1040;
			size = 3;
			range = 22;
			consumesPower = outputsPower = true;
			fogRadius = 2;
			researchCostMultiplier = 0.5f;
			
			consumePowerBuffered(1000f);
		}};
		
		lineLink = new LongPowerNodeLink("line-link") {{
			requirements(Category.power, tek(), with(nanoAlloy, 40, Items.silicon, 120, tantalum, 180, magnet, 60));
			health = 2000;
			size = 4;
			laserRange = 170f;
			autolink = false;
			laserColor1 = Color.valueOf("ffffff").a(0.45f);
			laserColor2 = Color.valueOf("ffd9c2").a(0.45f);
			glowColor = Color.valueOf("ff8330").a(0.45f);
			laserScale = 1f;
			//scaledHealth = 130;
			crushDamageMultiplier = 0.7f;
			ambientSound = Sounds.pulse;
			ambientSoundVolume = 0.07f;
			
	    	consumePower(160f / 60f);
		}};
		
		powerCapacitor = new Battery("power-capacitor") {{
			health = 140 / 2;
			requirements(Category.power, tek(), with(iron, 40, Items.graphite, 20));
			size = 2;
			emptyLightColor = Color.valueOf("fa6666");
			fullLightColor = Color.valueOf("f8c266");
			consumePowerBuffered(10000f);
			baseExplosiveness = 2f;
		}};
		
		powerBank = new Battery("power-bank") {{
			requirements(Category.power, tek(), with(tantalum, 30, Items.graphite, 50, Items.silicon, 30));
			health = 1040 / 2;
			size = 3;
			emptyLightColor = Color.valueOf("fa6666");
			fullLightColor = Color.valueOf("f8c266");
			consumePowerBuffered(30000f);
			baseExplosiveness = 5f;
		}};
		
		reinforcedDiode = new PowerDiode("reinforced-diode") {{
			requirements(Category.power, tek(), with(Items.silicon, 15, polycarbonate, 15, iron, 10));
			health = 350;
		}};
		
		lightningRod = new LightningRod("lightning-rod") {{
			requirements(Category.power, tek(), with(Items.silicon, 80, iron, 100, polycarbonate, 60, nanoAlloy, 30));
			health = 880;
			size = 3;
			cummulative = false;
			outlineColor = tektonOutlineColor;
			lightColor = Color.orange;
		}};
		
		methaneBurner = new ConsumeGenerator("methane-burner") {{
			requirements(Category.power, tek(), with(iron, 45, zirconium, 15, Items.silicon, 20));
			health = 200;
			size = 2;
			consumeLiquid(TektonLiquids.methane, 10f / 60f);
			powerProduction = 1;
			//itemDuration = 80;
			liquidCapacity = 35f;
			generateEffect = TektonFx.methanespark;
			ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.03f;
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.methane), new DrawCrucibleFlame() {{
				flameColor = Color.valueOf("caf549ff");
				midColor = Color.valueOf("f2d585ff");
				particles = 10;
			}}, new DrawDefault(), new DrawWarmupRegion());
			researchCostMultiplier = 0.4f;
		}};
		
		geothermalGenerator = new ThermalGenerator("geothermal-generator") {{
            requirements(Category.power, tek(), with(Items.graphite, 20, zirconium, 50, Items.silicon, 40));
            attribute = Attribute.steam;
            group = BlockGroup.power;
            displayEfficiencyScale = 1f / 9f;
            minEfficiency = 9f - 0.0001f;
            powerProduction = 140f / (60f * 9f);
            displayEfficiency = false;
            generateEffect = Fx.turbinegenerate;
            effectChance = 0.04f;
            size = 3;
            ambientSound = Sounds.hum;
            ambientSoundVolume = 0.06f;

            drawer = new DrawMulti(new DrawDefault(), new DrawRegion("-rotator-sub", -3f, true) {{ buildingRotate = true; }}, 
            		new DrawRegion("-rotator", 3f, true) {{ buildingRotate = true; }});

            hasLiquids = false;
            liquidCapacity = 0f;
            fogRadius = 3;
            researchCostMultiplier = 0.4f;
        }};
		
		methaneCombustionChamber = new ConsumeGenerator("methane-combustion-chamber") {{
			requirements(Category.power, tek(), with(iron, 60, zirconium, 80, Items.silicon, 60, tantalum, 80));
			health = 680;
			size = 3;
			squareSprite = false;
			consumeLiquids(LiquidStack.with(TektonLiquids.oxygen, 1f / 60f, TektonLiquids.methane, 40f / 60f));
			powerProduction = 520f / 60f;
			liquidCapacity = 35f;
			ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.06f;
			
			generateEffect = new RadialEffect(TektonFx.oxygenCombustionSmoke, 4, 90f, 8f) {{ rotationOffset = 45f; }};
			generateEffectRange = 0f;
			//effectChance = 1f;
            //generateEffect = TektonFx.methanespark;
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.oxygen, 2f), new DrawPistons() {{
				sinMag = 3f;
				sinScl = 5f;
				//sideOffset = Mathf.PI / 2f;
				angleOffset = 45f;
				sinOffset = 60f;
			}}, new DrawRegion("-mid"), new DrawLiquidTile(TektonLiquids.methane, 37f / 4f), new DrawDefault(), new DrawWarmupRegion(), new DrawGlowRegion() {{
				alpha = 1f;
				glowScale = 5f;
				color = Color.valueOf("8045ff99");
			}});
			researchCostMultiplier = 0.6f;
		}};
		
		thermalDifferenceGenerator = new ConsumeGenerator("thermal-difference-generator") {{
			requirements(Category.power, tek(), with(magnet, 60, polycarbonate, 80, Items.graphite, 100, Items.silicon, 120));
			health = 820;
			powerProduction = 1400f / 60f;
			itemDuration = 2.1f * 60f;
			squareSprite = false;
			hasLiquids = true;
			hasItems = true;
			size = 3;
			ambientSound = Sounds.steam;
			ambientSoundVolume = 0.03f;
			generateEffect = Fx.generatespark;

			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawItemLiquidTile(Liquids.cryofluid, cryogenicCompound, 9f / 4f) {{ alpha = 0.8f; }}, new DrawRegion("-mid-bottom"),
				new DrawRegion("-turbine") {{
					rotateSpeed = 5f;
			}}, new DrawDefault(), new DrawWarmupRegion(), new DrawGlowRegion() {{
				alpha = 0.7f;
				glowScale = 5f;
				color = Liquids.cryofluid.color.cpy().mul(1.2f);
			}}, new DrawParticles() {{
				color = Liquids.cryofluid.color;
				alpha = 0.6f;
				particleSize = 4f;
				particles = 10;
				particleRad = 12f;
				particleLife = 90f;
				blending = Blending.additive;
				particleSizeInterp = Interp.pow2Out;
				reverse = true;
				rotateScl = 1.4f;
			}});
			
			lightLiquid = Liquids.cryofluid;

			consumeItem(cryogenicCompound);
			consumeLiquids(LiquidStack.with(TektonLiquids.oxygen, 1f / 60f));
		}};

		uraniumReactor = new TektonNuclearReactor("uranium-reactor") {{
			requirements(Category.power, tek(), with(zirconium, 500, Items.silicon, 320, Items.graphite, 200, uranium, 140, polycarbonate, 120));
			ambientSound = Sounds.hum;
			ambientSoundVolume = 0.24f;
			size = 4;
			health = 1470;
			itemDuration = 140f;
			powerProduction = 4000 / 60;
			heating = 0.005f;
			squareSprite = false;
			
			generateEffect = new RadialEffect(TektonFx.nuclearSmoke, 4, 90f, 13.25f) {{ rotationOffset = 45f; }};
			
			var col = TektonColor.uraniumShootColor.cpy();
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.water, 2f), new DrawBubbles(Color.valueOf("7693e3")) {{
				sides = 10;
				recurrence = 3f;
				spread = 8;
				radius = 1.5f;
				amount = 20;
			}}, new DrawCircles(){{
                color = col.cpy().a(0.24f);
                strokeMax = 2.5f;
                radius = 10f;
                amount = 3;
            }}, new DrawFlame(col),
			new DrawDefault(), new DrawGlowRegion() {{
				alpha = 1f;
				glowScale = -5f;
				color = col.cpy().a(0.7f);
				glowIntensity = 0.7f;
			}});
			
			explosionRadius = 25;
			explosionDamage = 5000;

			consumeItem(uranium);
			consumeLiquid(Liquids.water, 50f / 60f);
		}};
		
		fusionReactor = new FusionReactor("fusion-reactor") {{
			requirements(Category.power, tek(), with(tantalum, 750, Items.silicon, 600, uranium, 250, polytalum, 450, nanoAlloy, 200, magnet, 200));
			health = 3050;
			size = 5;
			squareSprite = false;
			itemDuration = 45f;
			
			itemCapacity = 20;
			liquidCapacity = 100f;

            ambientSound = Sounds.pulse;
            ambientSoundVolume = 0.085f;
            
            //regionRotated1 = 1;
            
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.hydrogen, 2f) {{ alpha = 0.7f; }}, 
            		new DrawPlasma() {{ plasma1 = Color.valueOf("ffb957"); plasma2 = Color.valueOf("ff6ea3"); }}, 
            		new DrawRegion("-middle"), new DrawItemLiquidTile(Liquids.cryofluid, cryogenicCompound, 4f * 3f), new DrawDefault(), 
            		new DrawGravityInput(), new DrawGravityRegion() {{ color.a(0.3f); }}, new DrawGlowRegion() {{ color = Color.valueOf("ffb957").a(0.4f); }},
            		new DrawGlowRegion("-heat") {{ color = Color.red.cpy().a(0.25f); }});
            
            lightColor = Color.valueOf("ff6ea3");
            heatingPerTick /= 4f;
			
			explosionRadius = 60;
			explosionDamage = 14000;
			
			powerProduction = 14000 / 60;
			
			consumePower(3000 / 60);
			consumeItem(cryogenicCompound, 1).update(false);
			consumeLiquid(Liquids.hydrogen, 6f / 60f).update(false);
		}};
		
		//production
		
		wallDrill = new BeamDrill("wall-drill") {{
			requirements(Category.production, tek(), with(iron, 25));
			health = 210;
			size = 2;
			range = 3;
			drillTime = 350f;
			tier = 3;
			ambientSound = Sounds.drill;
			ambientSoundVolume = 0.1f;
			squareSprite = false;
			alwaysUnlocked = true;
			
            fogRadius = 3;
			
			consumeLiquid(TektonLiquids.ammonia, 0.25f / 60f).boost();
			optionalBoostIntensity = 2.5f;
			
			sparkColor = Color.valueOf("8fff87");
			heatColor = new Color(0.5f, 1f, 0.42f, 0.9f);
			boostHeatColor = Pal.missileYellow.cpy().mul(0.8f);
		}};
		
		plasmaWallDrill = new BeamDrill("plasma-wall-drill") {{
			requirements(Category.production, tek(), with(iron, 100, Items.silicon, 100, tantalum, 70, polycarbonate, 60));
			health = 920;
			size = 3;
			range = 6;
			drillTime = 100f;
			tier = 5;
			ambientSound = Sounds.drill;
			ambientSoundVolume = 0.1f;
			squareSprite = false;
			
            fogRadius = 4;
            laserWidth = 0.7f;
            itemCapacity = 20;

            consumePower(0.5f);
            consumeLiquid(Liquids.hydrogen, 0.5f / 60f);
			consumeLiquid(TektonLiquids.ammonia, 1f / 60f).boost();
			optionalBoostIntensity = 2.5f;
			
			sparkColor = Color.valueOf("8fff87");
			heatColor = new Color(0.5f, 1f, 0.42f, 0.9f);
			boostHeatColor = Pal.missileYellow.cpy().mul(0.8f);
		}};
		
		silicaAspirator = new AttributeCrafter("silica-aspirator") {{
			requirements(Category.production, tek(), with(iron, 15, zirconium, 10));
			health = 250;
			size = 2;
			craftTime = 300;
			hasLiquids = false;
			baseEfficiency = 0;
			displayEfficiencyScale = 1;
			minEfficiency = 1;
			boostScale = 0.25f;
			maxBoost = 2;
			attribute = TektonAttributes.silica;
			outputItem = new ItemStack(silica, 1);
			squareSprite = false;
			researchCostMultiplier = 0.05f;
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawRegion("-rotator", 10, true), new DrawRegion("-top"), new DrawParticles() {{
				color = Color.valueOf("e3beba");
				alpha = 0.45f;
				particleSize = 3;
				particles = 9;
				particleRad = 12;
				particleLife = 130;
				reverse = true;
			}});
			ambientSound = Sounds.hum;
			ambientSoundVolume = 0.1f;
		}};
		
		silicaTurbine = new AttributeCrafter("silica-turbine") {{
			requirements(Category.production, tek(), with(iron, 80, Items.silicon, 50, Items.graphite, 40, tantalum, 30));
			health = 600;
			size = 3;
			craftTime = 50;
			hasLiquids = false;
			baseEfficiency = 0;
			displayEfficiencyScale = 1;
			minEfficiency = 1;
			boostScale = 1f / 9f;
			maxBoost = 2;
			attribute = TektonAttributes.silica;
			outputItem = new ItemStack(silica, 1);
			squareSprite = false;
			researchCostMultiplier = 0.2f;
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawRegion("-rotator-sub", -10, true), new DrawRegion("-rotator", 10, true) {{ rotation = 45f / 2f; }}, 
					new DrawDefault(), new DrawParticles() {{
				color = Color.valueOf("e3beba");
				alpha = 0.45f;
				particleSize = 4.5f;
				particles = 12;
				particleRad = 18;
				particleLife = 160;
				reverse = true;
			}}, new DrawRegion("-top"));
			ambientSound = Sounds.hum;
			ambientSoundVolume = 0.1f;
			consumePower(40f / 60f);
		}};
		
		geothermalCondenser = new AttributeCrafterBoosted("geothermal-condenser") {{
            requirements(Category.production, tek(), with(Items.graphite, 20, Items.silicon, 40, iron, 60));
            attribute = Attribute.steam;
            group = BlockGroup.liquids;
			maxBoost = 2f;
            minEfficiency = 9f - 0.0001f;
            baseEfficiency = 0f;
            displayEfficiency = false;
			rotate = true;
			invertFlip = true;
            craftTime = 120f;
            size = 3;
            hasLiquids = true;
            boostScale = 1f / 9f;
            liquidBoostIntensity = 2f;
			ignoreLiquidFullness = false;
			squareSprite = false;
            craftEffect = Fx.turbinegenerate;
            
            itemCapacity = 0;
            
            liquidCapacity = 60f;
			regionRotated1 = 3;
			liquidOutputDirections = new int[]{1, 3};
			outputLiquids = LiquidStack.with(TektonLiquids.ammonia, 6f / 60f, Liquids.water, 20f / 60f);

            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.ammonia, 1.4f), new DrawLiquidTile(Liquids.water, 1.4f), new DrawRegion() {{ buildingRotate = false; }}, new DrawLiquidOutputs(), new DrawBlurSpin("-rotator", 6f), new DrawRegion("-mid"), 
            		new DrawLiquidTile(Liquids.hydrogen, 38f / 4f), new DrawRegion("-top") {{ buildingRotate = false; }});
			
            consumeLiquid(Liquids.hydrogen, 3f / 60f).boost();
            consumePower(80f / 60f);
            
            ambientSound = Sounds.hum;
            ambientSoundVolume = 0.06f;
			researchCostMultiplier = 0.4f;
        }};
        
        var undergroundWaterExtractorDebuff = 2f;
		
		undergroundWaterExtractor = new AttributeCrafterBoosted("underground-water-extractor") {{
			requirements(Category.production, tek(), with(tantalum, 60, Items.silicon, 60, zirconium, 80, Items.graphite, 60));
			craftTime = 1f;
			size = 3;
			health = 750;
			hasPower = true;
			hasLiquids = true;
			rotate = true;
			invertFlip = true;
			group = BlockGroup.liquids;
			itemCapacity = 0;
			maxBoost = 2f;
			minEfficiency = 5f;
			baseEfficiency = 0f;
			boostScale = (1f / 9f) * 0.5f;
            liquidBoostIntensity = 2f;
			ignoreLiquidFullness = false;
			attribute = Attribute.water;
			craftEffect = Fx.none;
			
			liquidCapacity = 60f;
			regionRotated1 = 3;
			liquidOutputDirections = new int[]{1, 3};
			outputLiquids = LiquidStack.with(TektonLiquids.ammonia, (6f / 60f) / undergroundWaterExtractorDebuff, Liquids.water, (20f / 60f) / undergroundWaterExtractorDebuff);
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.ammonia, 4f), new DrawLiquidTile(Liquids.water, 4f), new DrawRegion() {{ buildingRotate = false; }}, 
					new DrawLiquidOutputs(), new DrawRegion("-rotator", 1.5f, true) {{ buildingRotate = true; }}, new DrawRegion("-middle"), new DrawLiquidTile(Liquids.hydrogen, 38f / 4f), new DrawRegion("-top") {{ buildingRotate = false; }});
			
            consumeLiquid(Liquids.hydrogen, 3f / 60f).boost();
			consumePower(80f / 60f);
			
			ambientSound = Sounds.hum;
			ambientSoundVolume = 0.1f;
			squareSprite = false;
			researchCostMultiplier = 0.4f;
		}};
		
		reactionDrill = new BurstDrillBoosted("reaction-drill") {{
			requirements(Category.production, tek(), with(Items.silicon, 70, TektonItems.zirconium, 120, iron, 120, Items.graphite, 40));
			health = 980;
			drillTime = 60f * 11f;
			size = 4;
			squareSprite = false;
			hasPower = true;
			tier = 6;
			drillEffect = new MultiEffect(Fx.mineImpact.wrap(zirconiumSpark), Fx.drillSteam.wrap(zirconiumSpark), Fx.mineImpactWave.wrap(zirconiumSpark, 0f));
			baseArrowColor = Color.valueOf("69625c");
			arrowColor = glowColor = TektonColor.oxygen.cpy();
			arrows = 3;
			arrowOffset = 0.35f;
			arrowSpacing = 2.2f;
			shake = 3.5f;
			itemCapacity = 30;
			researchCostMultiplier = 0.4f;

			blockedItem = TektonItems.uranium;
			drillMultipliers.put(TektonItems.zirconium, multiReactionDrill(0.2f));
			drillMultipliers.put(TektonItems.iron, multiReactionDrill(0.4f));
			drillMultipliers.put(TektonItems.tantalum, 1f);
			drillMultipliers.put(TektonItems.silica, 0f);
			drillMultipliers.put(Items.sand, multiReactionDrill(0.6f));

			fogRadius = 3;

			consumePower(160f / 60f);
			consumeLiquid(TektonLiquids.oxygen, 1f / 60f);
			heatColor = zirconiumSpark;
			alpha = 0.55f;
			liquidBoostIntensity = 2f;
			boostLiquid = TektonLiquids.ammonia;
		}};
		
		gravitationalDrill = new GravitationalDrill("gravitational-drill") {{
			requirements(Category.production, tek(), with(iron, 140, Items.silicon, 200, uranium, 120, polytalum, 120, magnet, 60));
			health = 2360;
			drillTime = 60f * 5;
			size = 5;
			drawRim = true;
			hasPower = true;
			tier = 5;
			updateEffect = new Effect(40, e -> {
		        randLenVectors(e.id, 5, 3f + e.fin() * 8f, (x, y) -> {
		            color(TektonColor.gravityColor, Pal.stoneGray, e.fin());
		            Fill.square(e.x + x, e.y + y, e.fout() * 2f + 0.5f, 45);
		        });
		    });
			updateEffectChance = 0.03f;
			drillEffect = Fx.mineHuge;
			rotateSpeed = 10f;
			warmupSpeed = 0.01f;
			itemCapacity = 50;
			heatColor = TektonColor.gravityColor;
			
			requiredGravity = 6 * gravityMul;
			maxGravity = 12 * gravityMul;

	        ambientSoundVolume = 0.044f;
			
			drillMultipliers.put(TektonItems.zirconium, multiCarbonicDrill(0.2f));
			drillMultipliers.put(TektonItems.iron, multiCarbonicDrill(0.4f));
			drillMultipliers.put(TektonItems.tantalum, multiCarbonicDrill(0.1f));
			drillMultipliers.put(TektonItems.uranium, 1f);
			blockedItem = Items.sand;
			//drillMultipliers.put(Items.sand, multiCarbonicDrill(0.5f));
			
			liquidBoostIntensity = 1f;
			
			consumePower(480f / 60f);
			consumeLiquid(Liquids.hydrogen, 6f / 60f);
			//consumeLiquid(TektonLiquids.ammonia, 3f / 60f).boost();
		}};
		
		//storage
		
		corePrimal = new CoreBlock("core-primal") {{
			requirements(Category.effect, with(iron, 1000, zirconium, 600, Items.silicon, 600), true);
			alwaysUnlocked = true;
			isFirstTier = true;
			unitType = TektonUnits.delta;
			health = 3000;
			armor = 4f;
			size = 3;
			itemCapacity = 2000;
			incinerateNonBuildable = true;
			requiresCoreZone = true;
			squareSprite = false;
			unitCapModifier = 7;
            buildCostMultiplier = 0.7f;
			researchCostMultiplier = 0.07f;
			
			buildVisibility = tek();
		}};
		
		coreDeveloped = new TektonCoreBlock("core-developed") {{
			requirements(Category.effect, tek(), with(iron, 3000, magnet, 1000, Items.silicon, 2000, tantalum, 2000));
			alwaysUnlocked = false;
			unitType = TektonUnits.kappa;
			health = 8000;
			armor = 7f;
			size = 4;
			itemCapacity = 4000;
			thrusterLength = 34/4f;
			incinerateNonBuildable = true;
			requiresCoreZone = true;
			squareSprite = false;
			unitCapModifier = 10;
            buildCostMultiplier = 0.7f;
			researchCostMultiplier = 0.17f;
			researchCostMultipliers.put(Items.silicon, 0.5f);
		}};

	 	corePerfected = new TektonCoreBlock("core-perfected") {{
			requirements(Category.effect, tek(), with(iron, 6000, magnet, 3000, tantalum, 4000, Items.silicon, 5000, uranium, 3000));
			unitType = TektonUnits.sigma;
			health = 16000;
			armor = 14f;
			size = 5;
			itemCapacity = 7000;
            thrusterLength = 40/4f;
			incinerateNonBuildable = true;
			requiresCoreZone = true;
			squareSprite = false;
			unitCapModifier = 17;
            buildCostMultiplier = 0.7f;
			researchCostMultiplier = 0.11f;
			researchCostMultipliers.put(Items.silicon, 0.5f);
			lightningProtectionRadius *= 2f;
		}};
		
		capsule = new StorageBlock("capsule") {{
			requirements(Category.effect, tek(), with(iron, 170, zirconium, 100));
			size = 2;
			itemCapacity = 170;
			health = 300;
			coreMerge = false;
			researchCostMultiplier = 0.1f;
			squareSprite = false;
		}};
		
		vault = new StorageBlock("vault") {{
			requirements(Category.effect, tek(), with(iron, 400, zirconium, 300, tantalum, 200));
			size = 3;
			itemCapacity = 750;
			health = 1000;
			coreMerge = false;
			researchCostMultiplier = 0.3f;
			squareSprite = false;
		}};
		
		//turrets
        
        var defCoolantMultiplier = 0.5f + (0.5f / 3f);
        var turretBuildingDamageMultipliyer = 0.2f;
        
		one = new ItemTurret("one") {{
			requirements(Category.turret, tek(), with(iron, 40, zirconium, 20));
			size = 1;
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			drawer = new DrawTurret("quad-");
			health = 250;
			reload = 60;
			shootSound = Sounds.pew;
			predictTarget = true;
			maxAmmo = 10;
			inaccuracy = 5;
			//range = 12 * Vars.tilesize;
			ammo(
					iron, new BasicBulletType(6f, 25) {{
						width = 7f;
						height = 9f;
						lifetime = 16f;
						shootEffect = Fx.shootSmall;
						smokeEffect = Fx.shootSmallSmoke;
						hitEffect = despawnEffect = Fx.hitBulletColor;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						hitColor = backColor = trailColor = lightColor = commonShootColor;
						frontColor = Color.white;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						ammoMultiplier = 2;
					}},
					Items.silicon, new BasicBulletType(6.2f, 30) {{
						width = 7f;
						height = 9f;
						lifetime = 14f;
						shootEffect = Fx.shootSmall;
						smokeEffect = Fx.shootSmallSmoke;
						hitEffect = despawnEffect = Fx.hitBulletColor;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						hitColor = backColor = trailColor = lightColor = siliconShootColor;
						frontColor = Color.white;
						knockback = 0.2f;
						homingPower = 0.1f;
						reloadMultiplier = 1.2f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						ammoMultiplier = 4;
					}},
					Items.graphite, new BasicBulletType(6.2f, 40) {{
						width = 7f;
						height = 9f;
						lifetime = 14f;
						shootEffect = Fx.shootSmall;
						smokeEffect = Fx.shootSmallSmoke;
						//hitEffect = despawnEffect = Fx.hitBulletColor;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						hitColor = backColor = trailColor = lightColor = commonShootColor;
						frontColor = Color.white;
						knockback = 0.2f;
						reloadMultiplier = 0.7f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						ammoMultiplier = 4;
					}}
				);
			
			var coolantConsumption = 0.5f / 60f;
            coolant = consume(new ConsumeLiquid(TektonLiquids.ammonia, coolantConsumption));
            coolantMultiplier = defCoolantMultiplier / coolantConsumption;
			buildCostMultiplier = 4f;
			alwaysUnlocked = true;
		}};
		
		duel = new ItemTurret("duel") {{
			requirements(Category.turret, tek(), with(iron, 100, Items.silicon, 60));
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			size = 2;
			drawer = new DrawTurret("quad-") {{
				parts = new Seq<DrawPart>(new DrawPart[]{
					new RegionPart("-cannon-left") {{
					progress = PartProgress.recoil;
					moveY = -1.5f;
					recoilIndex = weaponIndex = 0;
					mirror = false;
					under = false;
					heatColor = Color.clear;
				}}, new RegionPart("-cannon-right") {{
					progress = PartProgress.recoil;
					moveY = -1.5f;
					recoilIndex = weaponIndex = 3;
					mirror = false;
					under = false;
					heatColor = Color.clear;
				}}, new RegionPart("-cannon-left-heat") {{
            		progress = PartProgress.recoil;
					moveY = -1.5f;
					recoilIndex = weaponIndex = 0;
					mirror = false;
					under = false;
					color = heatColor = Color.clear;
					colorTo = Pal.turretHeat;
					blending = Blending.additive;
					outline = false;
            	}},	new RegionPart("-cannon-right-heat") {{
            		progress = PartProgress.recoil;
					moveY = -1.5f;
					recoilIndex = weaponIndex = 3;
					mirror = false;
					under = false;
					color = heatColor = Color.clear;
					colorTo = Pal.turretHeat;
					blending = Blending.additive;
					outline = false;
            	}},
				new RegionPart("-top") {{
					mirror = false;
					under = false;
				}}});
			}};
			health = 800;
			reload = 40f;
			range = 160f;
			//shootSound = Sounds.pew;
			maxAmmo = 20;
			inaccuracy = 3;
			ammoPerShot = 1;
			recoils = 6;

			soundPitchMin = 1.3f;
			soundPitchMax = 1.5f;
			ammo(
					iron, new BasicBulletType(5.15f, 20) {{
						width = 7f;
						height = 9f;
						lifetime = 30f;
						shootEffect = Fx.shootSmall;
						smokeEffect = Fx.shootSmallSmoke;
						hitEffect = despawnEffect = Fx.hitBulletColor;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = hitColor = lightColor = commonShootColor;
						lightRadius = 15;
						lightOpacity = 0.5f;
						trailChance = -1;
						trailWidth = 1.7f;
						trailLength = 7;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						ammoMultiplier = 1;
					}},
					Items.silicon, new BasicBulletType(5.15f, 25) {{
						width = 7f;
						height = 9f;
						lifetime = 30f;
						shootEffect = Fx.shootSmall;
						smokeEffect = Fx.shootSmallSmoke;
						hitEffect = despawnEffect = Fx.hitBulletColor;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = hitColor = lightColor = siliconShootColor;
						lightRadius = 15;
						lightOpacity = 0.5f;
						trailChance = -1;
						trailWidth = 1.7f;
						trailLength = 7;
						knockback = 0.1f;
						homingPower = 0.1f;
						reloadMultiplier = 1.2f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						ammoMultiplier = 2;
					}},
					Items.graphite, new BasicBulletType(5.15f, 40) {{
						width = 7f;
						height = 9f;
						lifetime = 30f;
						shootEffect = Fx.shootSmall;
						smokeEffect = Fx.shootSmallSmoke;
						//hitEffect = despawnEffect = Fx.hitBulletColor;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = hitColor = lightColor = commonShootColor;
						lightRadius = 15;
						lightOpacity = 0.5f;
						trailChance = -1;
						trailWidth = 1.7f;
						trailLength = 7;
						knockback = 0.2f;
						reloadMultiplier = 0.7f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						ammoMultiplier = 2;
					}},
					tantalum, new BasicBulletType(7.1f, 35) {{
						width = 7f;
						height = 9f;
						lifetime = 22f;
						shootEffect = Fx.shootSmallColor;
						smokeEffect = Fx.shootSmallSmoke;
						hitEffect = despawnEffect = Fx.hitBulletColor;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = hitColor = lightColor = tantalumShootColor;
						lightRadius = 15;
						lightOpacity = 0.5f;
						trailChance = -1;
						trailWidth = 1.7f;
						trailLength = 7;
						pierce = true;
						pierceCap = 2;
						knockback = 0.3f;
						reloadMultiplier = 0.9f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						ammoMultiplier = 2;
					}},
					magnet, new BasicBulletType(7.1f, 10) {{
						width = 7f;
						height = 9f;
						lifetime = 22f;
						shootEffect = Fx.shootSmallColor;
						smokeEffect = Fx.shootSmallSmoke;
						hitEffect = despawnEffect = new MultiEffect(Fx.hitBulletColor, new WaveEffect() {{ 
							sizeFrom = 2;
							sizeTo = 16;
							lifetime = 14;
							strokeFrom = 2;
							strokeTo = 0;
							colorFrom = Color.white;
							colorTo = magnetShootColor;
						}});
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = hitColor = lightColor = magnetShootColor;
						lightRadius = 15;
						lightOpacity = 0.5f;
						
						trailChance = -1;
						trailInterval = 2f;
						trailEffect = new WaveEffect() {{ 
							sizeFrom = 0;
							sizeTo = 6;
							lifetime = 8;
							strokeFrom = 1.5f;
							strokeTo = 0;
							colorFrom = Color.white;
							colorTo = magnetShootColor;
						}};
						
						trailWidth = 1.7f;
						trailLength = 7;
						pierce = true;
						pierceCap = 2;
						knockback = 4f;
						splashDamage = 20f;
						splashDamageRadius = 16f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						ammoMultiplier = 2;
					}}
				);
			shoot = new ShootBarrel() {{
				shots = 3;
				shotDelay = 6;
				var y = -0.6f;
				barrels = new float[] {
					-3, y, 0,
					-3, y, 0,
					-3, y, 0,
					3, y, 0,
					3, y, 0,
					3, y, 0
				};
			}};
			var coolantConsumption = 1f / 60f;
            coolant = consume(new ConsumeLiquid(TektonLiquids.ammonia, coolantConsumption));
            coolantMultiplier = defCoolantMultiplier / coolantConsumption;
			researchCostMultiplier = 0.2f;
			buildCostMultiplier = 2.1f;
		}};
		
		compass = new PowerTurret("compass") {{
			requirements(Category.turret, tek(), with(Items.silicon, 80, Items.graphite, 40));
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			drawer = new DrawTurret("quad-");
			range = 176f;
			size = 2;
			health = 900;
			shootCone = 4;
			recoil = 0.6f;
			reload = 80f;
			shootEffect = Fx.none;
			heatColor = Color.red;
			shootSound = TektonSounds.tchau;
			soundPitchMin = 1.0f;
			soundPitchMax = 1.1f;
			shootWarmupSpeed = 0.08f;
			minWarmup = 0.9f;
			//heatColor = Color.valueOf("ff3333df");
			targetAir = false;
			moveWhileCharging = false;
			accurateDelay = false;
			inaccuracy = 4;
			cooldownTime = 40f;
			//warmupMaintainTime = 100f;
			shootEffect = new MultiEffect(new ParticleEffect() {{
				particles = 6;
				line = true;
				lifetime = 15;
				length = 15;
				lenFrom = 3;
				lenTo = 0;
				strokeFrom = 1;
				strokeTo = 0;
				colorFrom = Color.white;
				colorTo = redShootColor;
			}});

			consumePower(120f / 60f);
			//consumeLiquid(Liquids.water, 0.5f);

			shootType = new BasicBulletType(5.5f, 0) {{
				hittable = false;
				sprite = "large-orb";
				smokeEffect = Fx.shootBigSmoke;
				width = 9f;
				height = 9f;
				lifetime = 30f;
				hitSize = 4f;
				hitColor = backColor = trailColor = redShootColor;
				frontColor = Color.white;
				lightRadius = 15f;
				lightOpacity = 0.5f;
				trailChance = -1;
				trailWidth = 1.7f;
				trailLength = 6;
				collidesAir = false;
				despawnHit = true;
				despawnEffect = hitEffect = new MultiEffect() {{
					effects = new Effect[]{
							new WaveEffect() {{
								sizeFrom = 2;
								sizeTo = 14;
								lifetime = 10;
								strokeFrom = 2;
								strokeTo = 0;
								colorFrom = Color.valueOf("ffffff");
								colorTo = redShootColor;
							}},
							new ParticleEffect() {{
								particles = 6;
								line = true;
								lifetime = 15;
								length = 15;
								lenFrom = 3;
								lenTo = 0;
								strokeFrom = 1;
								strokeTo = 0;
								colorFrom = Color.valueOf("ffffff");
								colorTo = redShootColor;
							}}
					};
				}};
				despawnSound = hitSound = Sounds.spark;
				hitSoundPitch = 1.4f;
				hitSoundVolume = 1.2f;
				reflectable = true;
				shrinkX = 0;
				shrinkY = 0;
				lightning = 7;
				lightningLength = 6;
				lightningLengthRand = 8;
				lightningDamage = 7;
				lightningColor = redShootColorLightning;
				//lightningType.collidesAir = false;
				ammoMultiplier = 1;
				buildingDamageMultiplier = turretBuildingDamageMultipliyer;
			}};
			
			buildCostMultiplier = 2.5f;
			researchCostMultiplier = 0.4f;
		}};
		
		skyscraper = new ItemTurret("skyscraper") {{
			requirements(Category.turret, tek(), with(zirconium, 80, Items.silicon, 50));
			unitSort = UnitSorts.weakest;
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			size = 2;
			drawer = new DrawTurret("quad-") {{
				parts = new Seq<DrawPart>(new DrawPart[]{
					new RegionPart("-barrel") {{
					progress = PartProgress.recoil;
					moveY = -1f;
					//recoilIndex = 3;
					mirror = true;
					under = false;
				}},
				new RegionPart("") {{
					mirror = false;
					under = true;
					progress = PartProgress.constant(0);
				}}});
			}};
			health = 600;
			reload = 25f;
			range = 204f;
			//shootSound = Sounds.pew;
			maxAmmo = 10;
			inaccuracy = 7f;
			ammoPerShot = 1;
			recoils = 2;
			soundPitchMin = 0.9f;
			soundPitchMax = 1f;
			targetGround = false;
			ammo(
					iron, new FlakBulletType(7f, 6) {{
						width = 7f;
						height = 9f;
						lifetime = 30f;
						shootEffect = Fx.shootSmall;
						smokeEffect = Fx.shootSmallSmoke;
						hitEffect = despawnEffect = Fx.hitBulletColor;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = lightColor = hitColor = lightColor = commonShootColor;
						lightRadius = 15;
						lightOpacity = 0.5f;
						trailChance = -1;
						trailWidth = 1.8f;
						trailLength = 7;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						splashDamage = 30;
						splashDamageRadius = 18f;
						explodeRange = 12f;
						ammoMultiplier = 2;
					}},
					Items.graphite, new FlakBulletType(7f, 12) {{
						width = 7f;
						height = 9f;
						lifetime = 30f;
						shootEffect = Fx.shootSmall;
						smokeEffect = Fx.shootSmallSmoke;
						//hitEffect = despawnEffect = Fx.hitBulletColor;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = lightColor = hitColor = lightColor = commonShootColor;
						lightRadius = 15;
						lightOpacity = 0.5f;
						trailChance = -1;
						trailWidth = 1.8f;
						trailLength = 7;
						reloadMultiplier = 0.7f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						splashDamage = 60;
						splashDamageRadius = 26f;
						explodeRange = 18f;
						ammoMultiplier = 2;
					}},
					polycarbonate, new FlakBulletType(7f, 6) {{
						width = 7f;
						height = 9f;
						lifetime = 30f;
						shootEffect = Fx.shootSmall;
						smokeEffect = Fx.shootSmallSmoke;
						hitEffect = despawnEffect = Fx.hitBulletColor;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = lightColor = hitColor = polycarbonateShootColor;
						lightRadius = 15;
						lightOpacity = 0.5f;
						trailChance = -1;
						trailWidth = 1.8f;
						trailLength = 7;
						reloadMultiplier = 0.8f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						splashDamage = 40f;
						splashDamageRadius = 18f;
						explodeRange = 12f;

						fragBullets = 6;
						fragBullet = new BasicBulletType(3f, 8f) {{
							width = 5f;
							height = 12f;
							shrinkY = 1f;
							lifetime = 20f;
							backColor = frontColor = polycarbonateShootColor;
							despawnEffect = Fx.none;
							collidesGround = false;
						}};
						ammoMultiplier = 4;
					}},
					polytalum, new FlakBulletType(7.5f, 9) {{
						width = 7f;
						height = 9f;
						lifetime = 30f;
						shootEffect = Fx.shootSmall;
						smokeEffect = Fx.shootSmallSmoke;
						hitEffect = despawnEffect = Fx.hitBulletColor;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = lightColor = hitColor = polytalumShootColor;
						lightRadius = 15;
						lightOpacity = 0.5f;
						trailChance = -1;
						trailWidth = 2;
						trailLength = 7;
						reloadMultiplier = 1.2f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						splashDamage = 50f;
						splashDamageRadius = 24f;
						explodeRange = 18f;
						rangeChange = (7.5f * 8f) - 24f;

						fragBullets = 7;
						fragBullet = new BasicBulletType(3.5f, 14f) {{
							width = 5f;
							height = 12f;
							shrinkY = 1f;
							lifetime = 20f;
							backColor = frontColor = polytalumShootColor;
							despawnEffect = Fx.none;
							collidesGround = false;
						}};
						ammoMultiplier = 4;
					}}
				);
			shoot = new ShootAlternate() {{
				spread = 7;
				shotDelay = 0;
				shots = 2;
			}};
			var coolantConsumption = 1f / 60f;
            coolant = consume(new ConsumeLiquid(TektonLiquids.ammonia, coolantConsumption));
            coolantMultiplier = defCoolantMultiplier / coolantConsumption;
            
			researchCostMultiplier = 0.2f;
			buildCostMultiplier = 1.8f;
		}};
		
		spear = new ItemTurret("spear") {{
			requirements(Category.turret, tek(), with(Items.silicon, 100, tantalum, 180, iron, 240));
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			size = 3;
			drawer = new DrawTurret("quad-") {{
				parts = new Seq<DrawPart>(new DrawPart[]{
					new RegionPart("-barrel") {{
					progress = PartProgress.recoil;
					moveY = -4f;
					//recoilIndex = 3;
					mirror = false;
					under = true;
				}},	new RegionPart("-ear") {{
					progress = PartProgress.warmup;
					heatProgress = PartProgress.warmup;
					var a = 1.8f;
					moveX = a;
					moveY = a;
					//recoilIndex = 3;
					mirror = true;
					under = false;
				}},
				new RegionPart("") {{
					mirror = false;
					under = false;
					progress = PartProgress.constant(0);
				}}});
			}};
			shootY = 6.4f;
			unitSort = UnitSorts.strongest;
			shoot.firstShotDelay = 20f;
			health = 1900;
			reload = 100f;
			range = 260f;
			shootSound = Sounds.mediumCannon;
			soundPitchMin = 0.7f;
			soundPitchMax = 0.85f;
			maxAmmo = 10;
			inaccuracy = 2;
			accurateDelay = true;
			ammoPerShot = 2;
			ammo(
					tantalum, new BasicBulletType(10f, 80) {{
						hitSize = 7f;
						width = 9f;
						height = 12f;
						lifetime = 20f;
						shootEffect = TektonFx.shootBig;
						smokeEffect = TektonFx.shootBigSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = hitColor = tantalumShootColor;
						lightRadius = 20;
						lightOpacity = 0.5f;
						lightColor = tantalum.color;
						trailChance = -1;
						trailWidth = 2.2f;
						trailLength = 10;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						hitEffect = despawnEffect = TektonFx.blastExplosionColor;
						pierce = false;
						pierceArmor = true;
						knockback = 8f;
						
						splashDamageRadius = 24f;
						splashDamage = 50f;
						
						fragOnHit = true;
						fragRandomSpread = 0f;
						fragSpread = 10f;
						fragBullets = 5;
						fragVelocityMin = 1f;
						despawnSound = Sounds.dullExplosion;

						fragBullet = new BasicBulletType(8f, 25) {{
							sprite = "missile-large";
							width = 8f;
							height = 12f;
							lifetime = 15f;
							hitSize = 4f;
							hitColor = backColor = trailColor = tantalumShootColor;
							frontColor = Color.white;
							trailWidth = 2.8f;
							trailLength = 6;
							hitEffect = despawnEffect = Fx.hitBulletColor;
							splashDamageRadius = 24f;
							splashDamage = 20f;
							pierce = false;
							pierceBuilding = false;
							pierceCap = 3;
							buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						}};
						
						ammoMultiplier = 1f;
					}},
					
					magnet, new BasicBulletType(10f, 40) {{
						hitSize = 7f;
						width = 9f;
						height = 12f;
						lifetime = 20f;
						shootEffect = TektonFx.shootBig;
						smokeEffect = TektonFx.shootBigSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = hitColor = lightColor = magnetShootColor;
						lightRadius = 20;
						lightOpacity = 0.5f;
						lightColor = magnet.color;
						trailChance = -1;
						trailWidth = 2.2f;
						trailLength = 10;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						hitEffect = despawnEffect = TektonFx.blastExplosionColor;
						pierce = false;
						pierceArmor = true;
						knockback = 26f;
						
						splashDamageRadius = 40f;
						splashDamage = 70f;
						
						fragOnHit = true;
						fragRandomSpread = 0f;
						fragSpread = 10f;
						fragBullets = 1;
						fragVelocityMin = 1f;
						despawnSound = TektonSounds.gravityemission;
						
						trailChance = -1;
						trailInterval = 2f;
						trailEffect = new WaveEffect() {{ 
							sizeFrom = 1;
							sizeTo = 10;
							lifetime = 8;
							strokeFrom = 1.5f;
							strokeTo = 0;
							colorFrom = Color.white;
							colorTo = magnetShootColor;
						}};

						fragBullet = new WaveBulletType(7f / 2.5f, 8f) {{
							circleDeegres = 50f;
							minRadius = 8f;
							lifetime = 60f;
							knockback = 40f;
							hitColor = backColor = trailColor = lightColor = magnetShootColor;
							frontColor = Color.white;
							buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						}};

						ammoMultiplier = 2f;
					}},
					
					cryogenicCompound, new BasicBulletType(10f, 20) {{
						hitSize = 7f;
						width = 9f;
						height = 12f;
						lifetime = 20f;
						shootEffect = TektonFx.shootBig;
						smokeEffect = TektonFx.shootBigSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = hitColor = cryogenicCompoundShootColor;
						lightRadius = 20;
						lightOpacity = 0.5f;
						lightColor = cryogenicCompound.color;
						//trailChance = -1;
						trailWidth = 2.2f;
						trailLength = 10;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						hitEffect = despawnEffect = TektonFx.blastExplosionColorSlow;
						pierce = false;
						pierceArmor = false;
						knockback = 4f;
						
						rangeChange = -52f;
						splashDamageRadius = 32f;
						splashDamage = 40f;
						status = StatusEffects.freezing;
						statusDuration = 60f * 5f;
						
						trailEffect = StatusEffects.freezing.effect;
						trailInterval = 20f;
						trailChance = 1f;
						
						fragOnHit = true;
						fragBullets = 1;
						fragVelocityMin = 1f;
						hitSound = despawnSound = TektonSounds.freezeexplosion;

						fragBullet = new StatusEffectAreaBulletType(90f, 32f) {{
							status = StatusEffects.freezing;
							areaEffect = StatusEffects.freezing.effect;
							buildingDamageMultiplier = 0f;
						}};

						ammoMultiplier = 2f;
					}},
					
					uranium, new BasicBulletType(12f, 160) {{
						hitSize = 7f;
						width = 9f;
						height = 12f;
						lifetime = 16f;
						shootEffect = TektonFx.shootBig;
						smokeEffect = TektonFx.shootBigSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = hitColor = uraniumShootColor;
						lightRadius = 20;
						lightOpacity = 0.5f;
						lightColor = uranium.color;
						trailChance = -1;
						trailWidth = 2.2f;
						trailLength = 10;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						hitEffect = despawnEffect = TektonFx.blastExplosionColor;
						pierce = true;
						//pierceBuilding = true;
						pierceArmor = true;
						pierceCap = 3;
						knockback = 12f;
						
						splashDamageRadius = 24f;
						splashDamage = 70f;
						
						fragOnHit = true;
						fragRandomSpread = 0f;
						fragSpread = 10f;
						fragBullets = 4;
						fragVelocityMin = 0.4f;
						fragVelocityMax = 1f;
						despawnSound = hitSound = Sounds.dullExplosion;
						reloadMultiplier = 0.8f;

						fragBullet = new BasicBulletType(8f, 35) {{
							sprite = "missile-large";
							width = 8f;
							height = 12f;
							lifetime = 15f;
							hitSize = 4f;
							hitColor = backColor = trailColor = uraniumShootColor;
							frontColor = Color.white;
							trailWidth = 2.8f;
							trailLength = 6;
							hitEffect = despawnEffect = Fx.hitBulletColor;
							splashDamageRadius = 24f;
							splashDamage = 30f;
							pierce = true;
							pierceBuilding = false;
							pierceCap = 2;
							buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						}};
						
						ammoMultiplier = 2f;
					}},
					
					nanoAlloy, new BasicBulletType(14f, 240) {{
						hitSize = 7f;
						width = 9f;
						height = 12f;
						lifetime = 18f;
						shootEffect = TektonFx.shootBig;
						smokeEffect = TektonFx.shootBigSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = hitColor = nanoAlloyShootColor;
						lightRadius = 20;
						lightOpacity = 0.5f;
						lightColor = uranium.color;
						trailChance = -1;
						trailWidth = 2.2f;
						trailLength = 10;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						hitEffect = despawnEffect = TektonFx.blastExplosionColor;
						pierce = true;
						//pierceBuilding = true;
						pierceArmor = true;
						pierceCap = 7;
						knockback = 14f;
						
						splashDamageRadius = 24f;
						splashDamage = 100f;
						
						fragOnHit = true;
						fragSpread = 50f / 7f;
						fragBullets = 7;
						fragVelocityMin = 0f;
						fragVelocityMax = 0f;
						despawnSound = hitSound = Sounds.spark;
						reloadMultiplier = 0.8f;

	                    bulletInterval = 2f;
						intervalBullet = new LightningBulletType(){{
	                        damage = 16;
	                        collidesAir = false;
	                        lightningColor = nanoAlloyShootColor;
	                        lightningLength = 3;
	                        lightningLengthRand = 6;

	                        //for visual stats only.
	                        buildingDamageMultiplier = 0.8f;

	                        lightningType = new BulletType(0.0001f, 0f){{
	                            lifetime = Fx.lightning.lifetime;
	                            hitEffect = Fx.hitLancer;
	                            despawnEffect = Fx.none;
	                            status = StatusEffects.shocked;
	                            statusDuration = 10f;
	                            hittable = false;
	                            lightColor = nanoAlloyShootColor;
	                            buildingDamageMultiplier = 0.25f;
	                        }};
	                    }};
	                    
						fragBullet = new LightningBulletType(){{
	                        damage = 24f;
	                        collidesAir = false;
	                        lightningColor = nanoAlloyShootColor;
	                        lightningLength = 5;
	                        lightningLengthRand = 8;

	                        //for visual stats only.
	                        buildingDamageMultiplier = 0.8f;

	                        lightningType = new BulletType(0.0001f, 0f){{
	                            lifetime = Fx.lightning.lifetime;
	                            hitEffect = Fx.hitLancer;
	                            despawnEffect = Fx.none;
	                            status = StatusEffects.shocked;
	                            statusDuration = 10f;
	                            hittable = false;
	                            lightColor = nanoAlloyShootColor;
	                            buildingDamageMultiplier = 0.25f;
	                        }};
	                    }};
						
						ammoMultiplier = 4f;
					}});
			var coolantConsumption = 2f / 60f;
            coolant = consume(new ConsumeLiquid(TektonLiquids.ammonia, coolantConsumption));
            coolantMultiplier = defCoolantMultiplier / coolantConsumption;
            
			researchCostMultiplier = 0.6f;
			buildCostMultiplier = 0.9f;
		}};
		
		sword = new PowerTurret("sword") {{
			requirements(Category.turret, tek(), with(Items.silicon, 180, magnet, 60, tantalum, 120));
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			heatColor = Color.valueOf("ff3333df");
			drawer = new DrawTurret("quad-") {{
				heatColor = Color.valueOf("ff3333df");
				parts.addAll(
						new RegionPart("-blade") {{
							progress = PartProgress.warmup;
							heatProgress = PartProgress.warmup;
							mirror = true;
							moveRot = -30f;
							x = 5.8f;
							y = 0;
							moveX = 4f;
							moveY = -4f;
							under = true;
							heatColor = Color.valueOf("ff3333ff");
						}},
						new RegionPart("-gun") {{
							progress = PartProgress.recoil;
							mirror = false;
							moveRot = 0f;
							x = 0;
							y = 0;
							moveX = 0;
							moveY = -2f;
							under = true;
							heatColor = Pal.turretHeat;
						}},
						new RegionPart("-top") {{
							progress = PartProgress.warmup;
							mirror = false;
							x = 0;
							y = 0;
							moveX = 0;
							moveY = 0;
							under = false;
							heatColor = Color.valueOf("ff3333ff");
							heatProgress = PartProgress.warmup;
						}});
			}};
			range = 255f;

			shoot = new ShootPattern() {{
				firstShotDelay = 80f;
				shotDelay = 10f;
				shots = 3;
			}};

			size = 3;
			health = 1700;
			shootCone = 1;
			recoil = 1f;
			reload = 360f;
			shake = 2f;
			//shootEffect = Fx.lancerLaserShoot.wrap(Color.valueOf("ff4545"));
			smokeEffect = Fx.none;
			heatColor = Color.red;
			shootSound = Sounds.laser;
			soundPitchMin = 1.3f;
			soundPitchMax = 1.45f;
			shootWarmupSpeed = 0.08f;
			minWarmup = 0.9f;
			targetAir = false;
			moveWhileCharging = false;
			accurateDelay = false;
			inaccuracy = 3;
			cooldownTime = 100f;
			warmupMaintainTime = 30f;

			consumePower(4f);
			consumeLiquid(Liquids.water, 3f / 60f);

			shootType = new LaserBulletType(120f) {{
				colors = new Color[]{redShootColor, Color.valueOf("ff5959"), Color.valueOf("ff6e6e"), Color.valueOf("ff9191")};
				chargeEffect = new MultiEffect(
					new WaveEffect() {{
					sizeFrom = 35;
					sizeTo = 0.1f;
					strokeFrom = 2;
					strokeTo = 0.9f;
					interp = Interp.pow2In;
					lifetime = 90f;
					colorFrom = Color.valueOf("ff454500");
					colorTo = Color.valueOf("ff9191ff");
					lightColor = Color.valueOf("ff4545");
					followParent = true;
				}}, new WaveEffect() {{
					sizeFrom = 26;
					sizeTo = 0.1f;
					strokeFrom = 1.7f;
					strokeTo = 0.9f;
					interp = Interp.pow2In;
					lifetime = 70f;
					colorFrom = Color.valueOf("ff454500");
					colorTo = Color.valueOf("ff9191ff");
					lightColor = Color.valueOf("ff4545");
					followParent = true;
				}}, new WaveEffect() {{
					sizeFrom = 17;
					sizeTo = 0.1f;
					strokeFrom = 1.4f;
					strokeTo = 0.9f;
					interp = Interp.pow2In;
					lifetime = 50f;
					colorFrom = Color.valueOf("ff454500");
					colorTo = Color.valueOf("ff9191ff");
					lightColor = Color.valueOf("ff4545");
					followParent = true;
				}}, new ParticleEffect() {{
					particles = 9;
					length = 20;
					baseLength = 5;
					sizeFrom = 0.1f;
					sizeTo = 3;
					interp = Interp.pow2In;
					sizeInterp = Interp.pow2;
					lifetime = 100;
					colorFrom = Color.valueOf("ff454500");
					colorTo = Color.valueOf("ff9191ff");
					lightColor = Color.valueOf("ff4545");
					line = true;
					strokeFrom = 2;
					strokeTo = 0;
					followParent = true;
				}}, //point
				new ParticleEffect() {{
					particles = 1;
					length = 0;
					baseLength = 0;
					sizeFrom = 0.1f;
					sizeTo = 5;
					interp = Interp.pow2In;
					sizeInterp = Interp.pow2;
					lifetime = 100;
					colorFrom = Color.valueOf("ff4545");
					colorTo = Color.valueOf("ff4545");
					lightColor = Color.valueOf("ff4545");
					followParent = true;
				}});
				
				ammoMultiplier = 1;
				buildingDamageMultiplier = turretBuildingDamageMultipliyer;
				hitSize = 4;
				width = 12;
				lifetime = 16f;
				drawSize = 400f;
				collidesAir = false;
				length = 250f;
				pierceCap = 4;
				statusDuration = 500;
				status = StatusEffects.melting;
				hitEffect = Fx.hitBulletColor;
				splashDamagePierce = true;
				//lightningType.collidesAir = false;
				lightningColor = redShootColorLightning;
			}};
			
			buildCostMultiplier = 1.15f;
			researchCostMultiplier = 0.7f;
		}};
		
		azure = new ItemTurret("azure") {{
			requirements(Category.turret, tek(), with(zirconium, 140, Items.silicon, 120, tantalum, 80));
			unitSort = UnitSorts.closest;
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			size = 3;
			drawer = new DrawTurret("quad-") {{
				parts.addAll(
				new RegionPart("-barrel-c") {{
					progress = PartProgress.recoil;
					moveY = -1.5f;
					recoilIndex = 0;
					mirror = false;
					under = false;
				}},
				new RegionPart("-barrel-r") {{
					progress = PartProgress.recoil;
					moveY = -1.5f;
					recoilIndex = 1;
					mirror = false;
					under = true;
				}},
				new RegionPart("-barrel-l") {{
					progress = PartProgress.recoil;
					moveY = -1.5f;
					recoilIndex = 2;
					mirror = false;
					under = true;
				}});
			}};
			
			shoot = new ShootBarrel() {{
				barrels = new float[] {
						0f, 5f, 0f,
						4.25f, 2f, 0f,
						-4.25f, 2f, 0f
				};
				shots = 1;
			}};
			shootY = 5f;
			recoils = 3;
			recoil = 0.5f;
			health = 1000;
			reload = 6f;
			var brange = range = 220f;
			//shootSound = Sounds.pew;
			maxAmmo = 10;
			inaccuracy = 7f;
			ammoPerShot = 1;
			soundPitchMin = 0.9f;
			soundPitchMax = 1f;
			targetGround = false;
			ammo(
					tantalum, new BasicBulletType(7f, 40f) {{
				        collidesGround = false;
						width = 7f;
						height = 9f;
						lifetime = 30f;
						shootEffect = Fx.shootSmall;
						shootEffect = Fx.shootSmallSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = hitColor = tantalumShootColor;
						lightRadius = 15;
						lightOpacity = 0.5f;
						lightColor = tantalumShootColor;
						trailChance = -1;
						trailWidth = 1.8f;
						trailLength = 7;
						//reloadMultiplier = 0.8f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						hitEffect = despawnEffect = Fx.hitBulletColor;
						
						pierce = true;
						pierceCap = 3;
						
						ammoMultiplier = 6f;
					}},
					
					polycarbonate, new FlakBulletType(7f, 6f) {{
						width = 7f;
						height = 9f;
						lifetime = 30f;
						shootEffect = Fx.shootSmall;
						shootEffect = Fx.shootSmallSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = hitColor = polycarbonateShootColor;
						lightRadius = 15;
						lightOpacity = 0.5f;
						lightColor = polycarbonateShootColor;
						trailChance = -1;
						trailWidth = 1.8f;
						trailLength = 7;
						reloadMultiplier = 0.8f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						hitEffect = despawnEffect = Fx.hitBulletColor;
						splashDamage = 40;
						splashDamageRadius = 40f;
						explodeRange = 9f;

						fragBullets = 6;
						fragBullet = new BasicBulletType(3f, 8f) {{
							width = 5f;
							height = 12f;
							shrinkY = 1f;
							lifetime = 20f;
							backColor = frontColor = polycarbonateShootColor;
							despawnEffect = Fx.none;
							collidesGround = false;
						}};
						ammoMultiplier = 6f;
					}},
					
					polytalum, new FlakBulletType(9f, 6f) {{
						rangeChange = 80f;
						width = 7f;
						height = 9f;
						lifetime = 30f;
						shootEffect = Fx.shootSmall;
						shootEffect = Fx.shootSmallSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.white;
						backColor = trailColor = hitColor = polytalumShootColor;
						lightRadius = 15;
						lightOpacity = 0.5f;
						lightColor = polytalumShootColor;
						trailChance = -1;
						trailWidth = 1.8f;
						trailLength = 7;
						reloadMultiplier = 1.2f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						hitEffect = despawnEffect = Fx.hitBulletColor;
						splashDamage = 50;
						splashDamageRadius = 40f;
						explodeRange = 11f;

						fragBullets = 7;
						fragBullet = new BasicBulletType(3.5f, 14f) {{
							width = 5f;
							height = 12f;
							shrinkY = 1f;
							lifetime = 27f;
							backColor = frontColor = polytalumShootColor;
							despawnEffect = Fx.none;
							collidesGround = false;
						}};
						ammoMultiplier = 8f;
					}}
				);
			
			var coolantConsumption = 2f / 60f;
            coolant = consume(new ConsumeLiquid(TektonLiquids.ammonia, coolantConsumption));
            coolantMultiplier = defCoolantMultiplier / coolantConsumption;
			researchCostMultiplier = 0.4f;
			buildCostMultiplier = 1.5f;
		}};
		
		interfusion = new ItemTurret("interfusion") {{
			requirements(Category.turret, tek(), with(Items.silicon, 140, polycarbonate, 100, tantalum, 120));
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			size = 3;
			unitSort = UnitSorts.farthest;
			heatColor = TektonColor.zirconiumSpark;
			shootY = 2f;
			drawer = new DrawTurret("quad-") {{
				parts.addAll(
				new RegionPart("-barrel") {{
					progress = PartProgress.warmup;
					heatProgress = PartProgress.warmup;
					heatColor = TektonColor.zirconiumSpark;
					moveX = 3f;
					mirror = true;
					under = false;
				}},
				new RegionPart("-inner") {{
					progress = PartProgress.warmup;
					heatProgress = PartProgress.warmup;
					heatColor = TektonColor.zirconiumSpark;
					moveX = 3.4f;
					moveY = -1.2f;
					moveRot = -7f;
					mirror = true;
					under = false;
				}},
				new RegionPart("-outer") {{
					progress = PartProgress.warmup;
					heatProgress = PartProgress.warmup;
					heatColor = TektonColor.zirconiumSpark;
					moveX = 4.4f;
					moveY = -1.6f;
					moveRot = -15f;
					mirror = true;
					under = false;
				}});
			}};
			health = 2100;
			reload = 80f;
			range = 120f;
			shootWarmupSpeed = 0.15f;
			minWarmup = 0.95f;
			var tRange = range;
			shootSound = TektonSounds.shotheavy;
			maxAmmo = 50;
			inaccuracy = 0;
			ammoPerShot = 2;
			soundPitchMin = 1f;
			soundPitchMax = 1.1f;
			shoot = new ShootSpread(3, 20f);
			ammo(
					zirconium, new ShrapnelBulletType() {{
						length = tRange * 0.95f;
						damage = 400;
						width = 25f;
						serrationLenScl = 7f;
						serrationSpaceOffset = 60f;
						serrationFadeOffset = 0f;
						serrations = 10;
						serrationWidth = 6f;
						hitLarge = true;
						fromColor = zirconiumShootColor;
						toColor = Color.white;
						shootEffect = smokeEffect = Fx.sparkShoot;
						pierce = true;
						pierceBuilding = true;
						pierceCap = 7;
						knockback = 10;
						ammoMultiplier = 1f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
					}},
					Items.graphite, new ShrapnelBulletType() {{
						length = tRange * 0.95f;
						damage = 200;
						width = 25f;
						serrationLenScl = 7f;
						serrationSpaceOffset = 60f;
						serrationFadeOffset = 0f;
						serrations = 10;
						serrationWidth = 6f;
						hitLarge = true;
						fromColor = commonShootColor;
						toColor = Color.white;
						shootEffect = smokeEffect = Fx.sparkShoot;
						pierce = true;
						pierceBuilding = true;
						pierceCap = 5;
						knockback = 4;
						ammoMultiplier = 1f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						
						bulletInterval = 60f;
						intervalBullets = 10;
						intervalRandomSpread = 25f;
						intervalSpread = 0f;
						intervalAngle = 0f;
						
						intervalBullet = new BasicBulletType(0f, 0f) {{ // 1.5
							instantDisappear = true;
							fragRandomSpread = 0f;
							fragSpread = 0f;
							fragBullets = 1;
							
							fragLifeMax = 1f;
							fragLifeMin = 0.7f;
							
							fragVelocityMax = 1f;
							fragVelocityMin = 0.7f;
							
							fragBullet = new BasicBulletType(10f, 20f) {{ // 1.5
								lifetime = 18f;
								width = height *= 2f;
								
								trailLength = 14;
				                trailWidth = 1.4f;
				                trailSinScl = 2.5f;
				                trailSinMag = 0.5f;
				                
				                pierce = true;
				                pierceCap = 4;
				                
				                backColor = hitColor = lightColor = trailColor = commonShootColor;
				                frontColor = Color.white;
							}};
						}};
					}},
					Items.phaseFabric, new ShrapnelBulletType() {{// 1
						length = tRange * 1.05f;
						damage = 700;
						width = 25f;
						serrationLenScl = 7f;
						serrationSpaceOffset = 60f;
						serrationFadeOffset = 0f;
						serrations = 10;
						serrationWidth = 6f;
						hitLarge = true;
						fromColor = phaseFabricShootColor;
						toColor = Color.white;
						shootEffect = smokeEffect = Fx.sparkShoot;
						pierce = true;
						pierceBuilding = true;
						pierceArmor = true;
						knockback = 30;
						ammoMultiplier = 4f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						
						fragBullets = 1;
						fragRandomSpread = 20f;
						fragBullet = new BasicBulletType(2f, 0f) {{ // 1.5
							hittable = false;
							absorbable = false;
							collides = false;
							
							lifetime = 24f;
							width = height *= 2f;
							
							trailLength = 14;
			                trailWidth = 1.4f;
			                trailSinScl = 2.5f;
			                trailSinMag = 0.5f;
			                
			                backColor = hitColor = lightColor = trailColor = phaseFabricShootColor;
			                frontColor = Color.white;
			                
			                hitSound = despawnSound = TektonSounds.shotheavy;
							
			                fragBullets = 1;
							fragRandomSpread = 0f;
							fragBullet = new ShrapnelBulletType() {{ // 2
								var mul = 0.667f;
								length = tRange * 1.05f * mul;
								damage = 700 * mul;
								width = 25f * mul;
								serrationLenScl = 7f * mul;
								serrationSpaceOffset = 60f * mul;
								serrationFadeOffset = 0f * mul;
								serrations = 10;
								serrationWidth = 6f * mul;
								hitLarge = true;
								fromColor = phaseFabricShootColor;
								toColor = Color.white;
								shootEffect = smokeEffect = Fx.sparkShoot;
								pierce = true;
								pierceBuilding = true;
								pierceArmor = true;
								knockback = 30 * mul;
								buildingDamageMultiplier = turretBuildingDamageMultipliyer;
								
								fragBullets = 1;
								fragRandomSpread = 20f;
								fragBullet = new BasicBulletType(2f, 0f) {{ // 2.5
									hittable = false;
									absorbable = false;
									collides = false;
									
									lifetime = 24f;
									width = height *= 2f;
									
									trailLength = 14;
					                trailWidth = 1.4f;
					                trailSinScl = 2.5f;
					                trailSinMag = 0.5f;
					                
					                backColor = hitColor = lightColor = trailColor = phaseFabricShootColor;
					                frontColor = Color.white;
					                
					                hitSound = despawnSound = TektonSounds.shotheavy;
									
					                fragBullets = 1;
									fragRandomSpread = 0f;
									fragBullet = new ShrapnelBulletType() {{ // 3
										var mul = 0.334f;
										length = tRange * 1.05f * mul;
										damage = 700 * mul;
										width = 25f * mul;
										serrationLenScl = 7f * mul;
										serrationSpaceOffset = 60f * mul;
										serrationFadeOffset = 0f * mul;
										serrations = 10;
										serrationWidth = 6f * mul;
										hitLarge = true;
										fromColor = phaseFabricShootColor;
										toColor = Color.white;
										shootEffect = smokeEffect = Fx.sparkShoot;
										pierce = true;
										pierceBuilding = true;
										pierceArmor = true;
										knockback = 30 * mul;
										buildingDamageMultiplier = turretBuildingDamageMultipliyer;
									}};
								}};
							}};
						}};
					}});
			consumeLiquid(TektonLiquids.oxygen, 1f / 60f);
			//coolant = consumeCoolant(0.3f, true, true);
			coolantMultiplier = 0.5f;
			researchCostMultiplier = 0.5f;
			buildCostMultiplier = 1.4f;
		}};
		
		freezer = new ItemLiquidTurret("freezer") {{
			requirements(Category.turret, tek(), with(Items.silicon, 120, Items.graphite, 80, polycarbonate, 80));
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			size = 3;
			drawer = new DrawTurret("quad-");
			unitSort = UnitSorts.strongest;
			health = 1900;
			targetUnderBlocks = false;
			range = 160f;
			shootSound = Sounds.none;
			loopSound = TektonSounds.freezer;
			ambientSoundVolume = 1.5f;
			loopSoundVolume = 2f;
			soundPitchMin = 1f;
			soundPitchMax = 1f;
			maxAmmo = 10;
			itemCapacity = 10;
			liquidCapacity = 20;
			ammoPerShot = 1;
			inaccuracy = 6;
			accurateDelay = true;
			heatColor = cryogenicCompound.color;
			recoil = 0f;
			minWarmup = 0f;
			reload = 1f;
			
			ammo(
					Liquids.water, new LiquidBulletType(Liquids.cryofluid) {{
						lifetime = 40f;
						speed = 4f;
						knockback = 0.6f;
						puddleSize = 8f;
						orbSize = 4f;
						drag = 0.001f;
						statusDuration = 60f * 3f;
						damage = 2f;
						ammoMultiplier = 3f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
					}},
					
					TektonLiquids.methane, new DoubleLiquidBulletType(TektonLiquids.liquidMethane, Liquids.cryofluid) {{
						lifetime = 40f;
						speed = 4f;
						knockback = 1f;
						puddleSize = 10f;
						orbSize = 4.5f;
						drag = 0.001f;
						statusDuration = 60f * 4f;
						damage = 4f;
						ammoMultiplier = 3.5f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
						
			        	hitColor = Color.valueOf("d0ff63ff");
					}},
					
					TektonLiquids.ammonia, new DoubleLiquidBulletType(TektonLiquids.ammonia, Liquids.cryofluid) {{
						lifetime = 40f;
						speed = 4f;
						knockback = 1.4f;
						puddleSize = 12f;
						orbSize = 3.5f;
						drag = 0.001f;
						statusDuration = 60f * 3f;
						damage = 6f;
						ammoMultiplier = 4f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
					}},
					
					TektonLiquids.acid, new DoubleLiquidBulletType(TektonLiquids.acid, Liquids.cryofluid) {{
						lifetime = 40f;
						speed = 4f;
						knockback = 0.5f;
						puddleSize = 8f;
						orbSize = 4f;
						drag = 0.001f;
						ammoMultiplier = 30f;
						statusDuration = 60f * 4f;
						damage = 10f;
						ammoMultiplier = 4f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
					}}/*,
					
					TektonLiquids.metazotoplasm, new DoubleLiquidBulletType(TektonLiquids.metazotoplasm, Liquids.cryofluid) {{
						lifetime = 40f;
						speed = 4f;
						knockback = 0.5f;
						puddleSize = 10f;
						orbSize = 4f;
						drag = 0.001f;
						ammoMultiplier = 30f;
						statusDuration = 60f * 3f;
						damage = 10f;
						ammoMultiplier = 4f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
					}}*/
				);
			
			itemAmmo = cryogenicCompound;
			
			coolantMultiplier = 0f;
			researchCostMultiplier = 0.4f;
			buildCostMultiplier = 2f;
		}};
		
		havoc = new ItemTurret("havoc") {{
			requirements(Category.turret, tek(), with(Items.silicon, 220, tantalum, 320, uranium, 250));
			size = 4;
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			heatColor = Color.valueOf("ff3333df");
			//heat = PartProgress.warmup;
			drawer = new DrawTurret("quad-") {{
				parts.addAll(
						new RegionPart("-plate2") {{
							progress = PartProgress.warmup;
							mirror = true;
							x = y = 0;
							moveX = -1f;
							moveY = 0f;
							moveRot = 10f;
							under = true;
							heatColor = Color.valueOf("ff3333df");
							heatProgress = PartProgress.warmup;
						}},
						new RegionPart("-barrel") {{
							progress = PartProgress.recoil;
							mirror = false;
							x = y = 0;
							under = false;
							moveY = -6f;
							heatColor = Color.valueOf("ff3333df");
							heatProgress = PartProgress.recoil;
						}},
						new RegionPart("-barrel-outer") {{
							progress = PartProgress.recoil.delay(0.2f);
							mirror = false;
							x = y = 0;
							moveY = -3f;
							under = false;
							heatColor = Color.valueOf("ff3333df");
							heatProgress = PartProgress.recoil;
						}},
						new RegionPart("-structure") {{
							progress = PartProgress.recoil;
							mirror = false;
							x = y = 0;
							under = false;
							heatColor = Color.valueOf("ff3333df");
							heatProgress = PartProgress.warmup;
						}},
						new RegionPart("-plate") {{
							progress = PartProgress.warmup;
							mirror = true;
							x = y = 0;
							moveX = -1f;
							moveY = -1f;
							moveRot = -7f;
							under = false;
							heatColor = Color.valueOf("ff3333df");
							heatProgress = PartProgress.warmup;
						}}
					);
			}};
            targetAir = false;
            range = 390f;
			shootY = 14f;
			health = 3200;
			shootCone = 1;
			recoil = 3f;
			reload = 150f;
			shake = 2f;
			
            ammoPerShot = 4;
            maxAmmo = ammoPerShot * 3;
            
			smokeEffect = Fx.none;
			heatColor = Color.red;
			shootSound = Sounds.explosionbig;
			soundPitchMin = 0.7f;
			soundPitchMax = 0.9f;
			shootWarmupSpeed = 0.08f;
			minWarmup = 0.9f;
			inaccuracy = 2;
			cooldownTime = 100;
			warmupMaintainTime = 120;
            rotateSpeed = 1.5f;

			consumeLiquid(Liquids.hydrogen, 3f / 60f);

			var coolantConsumption = 4f / 60f;
            coolant = consume(new ConsumeLiquid(TektonLiquids.ammonia, coolantConsumption));
            coolantMultiplier = defCoolantMultiplier / coolantConsumption;
			
			ammo(
					uranium, new ArtilleryBulletType(3f, 250, "shell") {{
		                hitEffect = new MultiEffect(Fx.titanExplosion);
		                despawnEffect = Fx.none;
		                knockback = 3f;
		                lifetime = 140f;
		                height = 19f;
		                width = 17f;
		                splashDamageRadius = 55f;
		                splashDamage = 180f;
		                
		                scaledSplashDamage = true;
		                backColor = hitColor = trailColor = lightColor = uraniumShootColor;
		                frontColor = Color.white;
		                ammoMultiplier = 1f;
		                hitSound = Sounds.titanExplosion;

		                status = StatusEffects.blasted;
		                
		                fragLifeMax = 120f;
		                fragLifeMin = 30f;
		                fragVelocityMin = 0.5f;
		                fragVelocityMax = 2f;
		                
		                fragBullets = 16;
		                
		                fragBullet = new ArtilleryBulletType(0.8f, 30f) {{
                            despawnShake = 1f;
                            lightRadius = 30f;
                            lightColor = Pal.redLight;
                            lightOpacity = 0.5f;
                            knockback = 0.8f;
                            width = height = 16f;
		                	lifetime = 1f;
		                	collidesGround = true;
		                	collidesAir = false;
                            collidesTiles = false;
		                	splashDamage = 50f;
		                	splashDamageRadius = 28f;
		                	backColor = hitColor = trailColor = lightColor = uraniumShootColor;
			                drag = 0.02f;
                            despawnEffect = hitEffect = Fx.massiveExplosion;
                            smokeEffect = Fx.none;
    						buildingDamageMultiplier = turretBuildingDamageMultipliyer;

                            trailLength = 24;
                            trailWidth = 2.8f;
                            trailEffect = Fx.none;
		                }};

		                trailLength = 32;
		                trailWidth = 3.35f;
		                trailSinScl = 2.5f;
		                trailSinMag = 0.5f;
		                trailEffect = Fx.disperseTrail;
		                trailInterval = 2f;
		                despawnShake = 7f;

		                shootEffect = Fx.shootTitan;
		                smokeEffect = Fx.shootSmokeTitan;
		                trailRotation = true;

		                trailInterp = v -> Math.max(Mathf.slope(v), 0.8f);
		                shrinkX = 0.2f;
		                shrinkY = 0.1f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
		            }},
					
					magnet, new ArtilleryBulletType(3f, 120, "shell") {{
		                hitEffect = new MultiEffect(Fx.titanExplosion);
		                despawnEffect = Fx.none;
						knockback = 30f;
		                lifetime = 140f;
		                height = 19f;
		                width = 17f;
		                splashDamageRadius = 55f;
		                splashDamage = 180f;
		                
		                scaledSplashDamage = true;
		                backColor = hitColor = trailColor = lightColor = magnetShootColor;
		                frontColor = Color.white;
		                ammoMultiplier = 2f;
		                hitSound = Sounds.titanExplosion;

						fragBullets = 1;
						fragVelocityMin = 1f;

						fragBullet = new BulletSpawnerBullet(60f * 1.2f, 60f) {{
							bulletInterval = 5f;
							spawnSound = TektonSounds.gravityemission;
							intervalBullet = new WaveBulletType(7f / 2.5f, 8f) {{
								minRadius = 1f;
								knockback = 30f;
								backColor = hitColor = trailColor = lightColor = magnetShootColor;
								lifetime = 9f;
								circleDeegres = 360f;
							}};
							buildingDamageMultiplier = 0f;
						}};

		                trailLength = 32;
		                trailWidth = 3.35f;
		                trailSinScl = 2.5f;
		                trailSinMag = 0.5f;
		                trailEffect = new MultiEffect(Fx.disperseTrail, new WaveEffect() {{ 
							sizeFrom = 4;
							sizeTo = 20;
							lifetime = 20;
							strokeFrom = 2.5f;
							strokeTo = 0;
							colorFrom = Color.white;
							colorTo = magnetShootColor;
						}});
		                trailInterval = 4f;
		                despawnShake = 7f;

		                shootEffect = Fx.shootTitan;
		                smokeEffect = Fx.shootSmokeTitan;
		                trailRotation = true;

		                trailInterp = v -> Math.max(Mathf.slope(v), 0.8f);
		                shrinkX = 0.2f;
		                shrinkY = 0.1f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
		            }},
					
					cryogenicCompound, new ArtilleryBulletType(3f, 120, "shell") {{
		                hitEffect = new MultiEffect(Fx.titanExplosion, Fx.titanSmoke);
		                despawnEffect = Fx.none;
		                knockback = 3f;
		                lifetime = 140f;
		                height = 19f;
		                width = 17f;
		                splashDamageRadius = 55f;
		                splashDamage = 180f;
		                
		                scaledSplashDamage = true;
		                backColor = hitColor = trailColor = lightColor = cryogenicCompoundShootColor;
		                frontColor = Color.white;
		                ammoMultiplier = 2f;
						hitSound = despawnSound = TektonSounds.freezeexplosionbig;

		                status = StatusEffects.freezing;
		                statusDuration = 60f * 10f;

						fragBullets = 1;
						fragVelocityMin = 1f;

						fragBullet = new StatusEffectAreaBulletType(60f * 7f, 60f) {{
							status = StatusEffects.freezing;
							areaEffect = StatusEffects.freezing.effect;
							buildingDamageMultiplier = 0f;
						}};

		                trailLength = 32;
		                trailWidth = 3.35f;
		                trailSinScl = 2.5f;
		                trailSinMag = 0.5f;
		                trailEffect = Fx.disperseTrail;
		                trailInterval = 2f;
		                despawnShake = 7f;

		                shootEffect = Fx.shootTitan;
		                smokeEffect = Fx.shootSmokeTitan;
		                trailRotation = true;

		                trailInterp = v -> Math.max(Mathf.slope(v), 0.8f);
		                shrinkX = 0.2f;
		                shrinkY = 0.1f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
		            }},
					
					nanoAlloy, new ArtilleryBulletType(3f, 240, "shell") {{
		                hitEffect = new MultiEffect(Fx.titanExplosion);
		                despawnEffect = Fx.none;
		                knockback = 3f;
		                lifetime = 140f;
		                height = 19f;
		                width = 17f;
		                splashDamageRadius = 55f;
		                splashDamage = 200f;
		                
		                scaledSplashDamage = true;
		                backColor = hitColor = trailColor = lightColor = nanoAlloyShootColor;
		                frontColor = Color.white;
		                ammoMultiplier = 4f;
		                hitSound = Sounds.plasmaboom;
		                
	                    bulletInterval = 5f;
						intervalBullet = new LightningBulletType(){{
	                        damage = 16;
	                        collidesAir = false;
	                        lightningColor = nanoAlloyShootColor;
	                        lightningLength = 3;
	                        lightningLengthRand = 6;

	                        //for visual stats only.
	                        buildingDamageMultiplier = 0.8f;

	                        lightningType = new BulletType(0.0001f, 0f){{
	                            lifetime = Fx.lightning.lifetime;
	                            hitEffect = Fx.hitLancer;
	                            despawnEffect = Fx.none;
	                            status = StatusEffects.shocked;
	                            statusDuration = 10f;
	                            hittable = false;
	                            lightColor = nanoAlloyShootColor;
	                            buildingDamageMultiplier = 0.25f;
	                        }};
	                    }};
	                    
        				lightning = 7;
        				lightningDamage = 30f;
                        lightningLength = 8;
                        lightningLengthRand = 10;

		                fragBullets = 1;
            			fragBullet = new TektonEmpBulletType() {{
                            collidesAir = true;
                            instantDisappear = true;
                            despawnHit = true;
            				damage = 70f;
            				radius = 55f;
                            clipSize = 250f;
            				powerDamageScl = 2f;
            				unitDamageScl = 1f;
            				buildingDamageMultiplier = turretBuildingDamageMultipliyer;
            				lightColor = lightningColor = hitColor = Pal.lightOrange;
            				hitPowerEffect = new Effect(40, e -> {
            			        color(e.color);
            			        stroke(e.fout() * 1.6f);

            			        randLenVectors(e.id, 18, e.finpow() * 27f, e.rotation, 360f, (x, y) -> {
            			            float ang = Mathf.angle(x, y);
            			            lineAngle(e.x + x, e.y + y, ang, e.fout() * 6 + 1f);
            			        });
            			    });
            				hitEffect = new Effect(50f, 100f, e -> {
                                e.scaled(7f, b -> {
                                    color(Pal.lightOrange, b.fout());
                                    Fill.circle(e.x, e.y, radius);
                                });

                                color(Pal.lightOrange);
                                stroke(e.fout() * 3f);
                                Lines.circle(e.x, e.y, radius);

                                int points = 7;
                                float offset = Mathf.randomSeed(e.id, 360f);
                                for(int i = 0; i < points; i++){
                                    float angle = i* 360f / points + offset;
                                    //for(int s : Mathf.zeroOne){
                                        Drawf.tri(e.x + Angles.trnsx(angle, radius), e.y + Angles.trnsy(angle, radius), 6f, 50f * e.fout(), angle/* + s*180f*/);
                                    //}
                                }

                                Fill.circle(e.x, e.y, 12f * e.fout());
                                color();
                                Fill.circle(e.x, e.y, 6f * e.fout());
                                Drawf.light(e.x, e.y, radius * 1.6f, Pal.lightOrange, e.fout());
                            });
            				chainEffect = Fx.chainEmp.wrap(Pal.lightOrange); 
            				applyEffect = Fx.hitLancer.wrap(Pal.lightOrange);
            			}};

		                trailLength = 32;
		                trailWidth = 3.35f;
		                trailSinScl = 2.5f;
		                trailSinMag = 0.5f;
		                trailEffect = Fx.disperseTrail;
		                trailInterval = 2f;
		                despawnShake = 7f;

		                shootEffect = Fx.shootTitan;
		                smokeEffect = Fx.shootSmokeTitan;
		                trailRotation = true;

		                trailInterp = v -> Math.max(Mathf.slope(v), 0.8f);
		                shrinkX = 0.2f;
		                shrinkY = 0.1f;
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
		            }});
			
            //unitSort = UnitSorts.weakest;
			
			buildCostMultiplier = 0.6f;
			researchCostMultiplier = 1f;
		}};
		
		tesla = new PowerTurret("tesla") {{
			requirements(Category.turret, tek(), with(Items.silicon, 300, tantalum, 200, polytalum, 160, magnet, 100));
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			heatColor = Color.valueOf("ff3333df");
			//heat = PartProgress.warmup;
			drawer = new DrawTurret("quad-") {{
				parts.addAll(
						new RegionPart("-spike") {{
							progress = PartProgress.warmup;
							mirror = true;
							x = 0;
							y = 0;
							moveX = moveY = 18f / 4f;
							under = true;
							heatColor = Color.valueOf("ff3333df");
							heatProgress = PartProgress.warmup;
						}},
						new FramePart("-rotator") {{
							progress = PartProgress.warmup;
							x = 0;
							y = 0;
							under = false;
							heatColor = Color.valueOf("ff3333df");
							heatProgress = PartProgress.warmup;
							frames = 17;
							interval = 1f;
							loop = false;
						}},
						new RegionPart("-piece") {{
							progress = PartProgress.warmup;
							mirror = true;
							x = 0;
							y = 0;
							moveX = 8f / 4f;
							moveY = -8f / 4f;
							under = false;
							heatColor = Color.valueOf("ff3333df");
							heatProgress = PartProgress.warmup;
						}},
						new RegionPart("-piece2") {{
							progress = PartProgress.warmup;
							mirror = true;
							x = 0;
							y = 0;
							moveX = 8f / 4f;
							moveY = -8f / 4f;
							under = false;
							heatColor = Color.valueOf("ff3333df");
							heatProgress = PartProgress.warmup;
						}}
					);
			}};
			range = 250f;
			shootY = 14f;
			size = 4;
			health = 3400;
			shootCone = 1;
			recoil = 1f;
			reload = 180f;
			shake = 2f;
			smokeEffect = Fx.none;
			heatColor = Color.red;
			shootSound = TektonSounds.tesla;
			soundPitchMin = 0.9f;
			soundPitchMax = 1.0f;
			shootWarmupSpeed = 0.08f;
			minWarmup = 0.5f;
			inaccuracy = 0;
			cooldownTime = 100;
			warmupMaintainTime = 120;
			predictTarget = false;
			targetAir = false;

			consumePower(800f / 60f);
			consumeLiquid(Liquids.water, 5f / 60f);
			
            unitSort = UnitSorts.strongest;

			shootType = new TeslaBulletType() {{
				maxRange = 240f;
				ammoMultiplier = 1;
				buildingDamageMultiplier = turretBuildingDamageMultipliyer;
				hitSize = 4;
				statusDuration = 10f;
				status = StatusEffects.shocked;
				hitColor = redShootColor;
				damage = 1800f;
				lightning = 8;
				lightningDamage = 50f;
                lightningLength = 8;
                lightningLengthRand = 10;
				lightningColor = redShootColorLightning;
				//lightningType.collidesAir = false;
				collidesAir = false;
				
				applyEffect = new MultiEffect(
						Fx.titanExplosion.wrap(redShootColorLightning),
	            		new Effect(120f, 20f, e -> {
	                        TektonFx.rand.setSeed(e.id + 1);
	                		color(e.color);
	                        stroke(e.fout() * 2f);
	                        float circleRad = 0.3f + e.finpow() * 180f;
	                        float range = 2f;
	                        float ex = TektonFx.rand.range(range);
	                        float ey = TektonFx.rand.range(range);

	                        for(int i = 0; i < 4; i++){
	                            Drawf.tri(e.x + ex, e.y + ey, 5f, 20f * e.fout(), i*90);
	                        }

	                        color();
	                        for(int i = 0; i < 4; i++){
	                            Drawf.tri(e.x + ex, e.y + ey, 2.5f, 20f / 3f * e.fout(), i*90);
	                        }

	                        Drawf.light(e.x + ex, e.y + ey, circleRad * 1.6f, redShootColorLightning, e.fout());
	                    }));
				
				hitEffect = new MultiEffect(
	        			new WaveEffect() {{
	            	    	sizeFrom = 2;
	            	    	sizeTo = 14;
	            	    	lifetime = 10;
	            	    	strokeFrom = 2;
	            	    	strokeTo = 0;
	            	    	colorFrom = Color.white;
	            	    	colorTo = redShootColor;
	            	    }},
	            		new ParticleEffect() {{
	            			particles = 6;
	            			line = true;
	            			lifetime = 15;
	            			length = 15;
	            			lenFrom = 3;
	            			lenTo = 0;
	            			strokeFrom = 1;
	            			strokeTo = 0;
	            			colorFrom = Color.white;
	            			colorTo = redShootColor;
	            		}}
            	    );
			}};
			
			buildCostMultiplier = 0.8f;
			researchCostMultiplier = 1f;
		}};
		
		prostrate = new PowerTurret("prostrate") {{
			requirements(Category.turret, tek(), with(Items.silicon, 250, magnet, 100, zirconium, 200, uranium, 100));
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			heatColor = Color.valueOf("ff3333df");
			var mov = 3.5f;
			var prosCol = Color.valueOf("d1dcff");
			drawer = new DrawTurret("quad-") {{
				parts.addAll(
						new RegionPart("-spine") {{
							progress = PartProgress.warmup;
							mirror = false;
							x = 0;
							y = 0;
							under = true;
							layerOffset = -0.3f;
							layer = Layer.turret - 1f;
	                        turretHeatLayer = Layer.turret - 0.2f;
							heatColor = prosCol;
							heatProgress = PartProgress.warmup;
						}},
						new RegionPart("-plate-down") {{
							progress = PartProgress.warmup;
							mirror = true;
							x = 0;
							y = 0;
							moveX = mov;
							moveY = -mov;
							under = false;
							heatColor = prosCol;
							heatProgress = PartProgress.warmup;
						}},
						new RegionPart("-plate-upper") {{
							progress = PartProgress.warmup;
							mirror = true;
							x = 0;
							y = 0;
							moveX = mov;
							moveY = mov;
							under = false;
							heatColor = prosCol;
							heatProgress = PartProgress.warmup;
						}},
						new ShapePart() {{
		                    progress = PartProgress.warmup;
		                    color = prosCol;
		                    circle = true;
		                    hollow = false;
		                    stroke = 0f;
		                    strokeTo = 2f;
		                    radius = 0f;
		                    radiusTo = 2.5f;
		                    layer = Layer.effect;
		                    x = y = 0;
		                    mirror = false;
		                }},
		                new HaloPart() {{
		                    progress = PartProgress.warmup.delay(0.5f);
		                    color = prosCol;
		                    layer = Layer.effect;
		                    x = y = 0;

		                    haloRotation = 00f;
		                    shapes = 4;
		                    triLength = 0f;
		                    triLengthTo = 20f;
		                    tri = true;
		                    haloRadius = 4f;
		                    radius = 4f;
		                }}
					);
			}};
			shootY = 0f;
			size = 4;
			health = 2900;
			recoil = 0f;
			reload = 30f;
			shake = 2f;
			smokeEffect = Fx.none;
			heatColor = prosCol;
			shootSound = Sounds.bolt;
            loopSound = Sounds.none;
            loopSoundVolume = 0.8f;
			soundPitchMin = 0.6f;
			soundPitchMax = 0.8f;
			shootWarmupSpeed = 0.08f;
			minWarmup = 0.8f;
			cooldownTime = 100;
			warmupMaintainTime = 120;
			predictTarget = false;
			targetGround = false;
			targetUnderBlocks = false;
			inaccuracy = 0;
			rotateSpeed = 0f;
			shootCone = 361f;
			
            float rad = 8f * 30f;
			range = rad;

			consumePower(300f / 60f);
			consumeLiquid(Liquids.water, 5f / 60f);
			
            //unitSort = UnitSorts.closest;

			shootType = new TektonEmpBulletType() {{
				hitSize = speed = 0f;
				lifetime = 1f;
				ammoMultiplier = 1;
				buildingDamageMultiplier = turretBuildingDamageMultipliyer;
				collidesAir = true;
				collidesGround = collidesTiles = absorbable = false;
				despawnHit = true;
				
				statusDuration = 10f;
				status = StatusEffects.shocked;
				hitColor = Color.valueOf("d1dcff");
				damage = 0f;
				radius = splashDamageRadius = rad;
				splashDamage = 80f;
				unitDamageScl = 1f;
				chainEffect = Fx.chainEmp.wrap(hitColor);
				hitPowerEffect.wrap(hitColor);
                applyEffect.wrap(hitColor);
				hitEffect = despawnEffect = new Effect(50f, 100f, e -> {
                    color(e.color);
                    stroke(e.fout() * 3f);
                    Lines.circle(e.x, e.y, rad);

                    /*int points = 16;
                    float offset = 0f;
                    for(int i = 0; i < points; i++){
                        float angle = i* 360f / points + offset;
                        for(int s : Mathf.zeroOne){
                            Drawf.tri(e.x + Angles.trnsx(angle, rad), e.y + Angles.trnsy(angle, rad), 6f, 50f * e.fout(), angle);
                        }
                    }*/

                    Fill.circle(e.x, e.y, 12f * e.fout());
                    color();
                    Fill.circle(e.x, e.y, 6f * e.fout());
                    Drawf.light(e.x, e.y, rad * 1.6f, e.color, e.fout());
                });
			}};
			
			buildCostMultiplier = 1.1f;
			researchCostMultiplier = 1f;
		}};
		
		repulsion = new GravitationalTurret("repulsion") {{
			requirements(Category.turret, tek(), with(Items.silicon, 300, Items.graphite, 200, magnet, 120, polytalum, 120));
			health = 2700;
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			size = 4;
            heatColor = Color.clear;
            recoil = 0;
            reload = 8f;
            shootSound = TektonSounds.gravityemission;
            loopSound = Sounds.none; 
            soundPitchMin = soundPitchMax = 0.45f;
            //loopSoundVolume = 2f;
            shootY = 0f;
            predictTarget = false;
            minWarmup = 0.8f;
            shootCone = 90f;
            minGravity = 12 * gravityMul;
            maxGravity = minGravity + (minGravity / 2);
            unitSort = UnitSorts.closest;
            
            drawer = new DrawTurret("quad-") {{ 
            	//parts.clear();
            	parts.add(
					new RegionPart("") {{
						progress = PartProgress.warmup;
						heatProgress = PartProgress.warmup;
						mirror = false;
						x = y = 0;
						under = outline = false;
						heatColor = TektonColor.gravityColor;
					}});
            	}};
            consumePower(8f);
			
			shootType = new WaveBulletType(4f, 1f) {{
				lifetime = 60f;
				interval = reload;
				damageInterval = 1f;
		        drawSize = ((lifetime * waveSpeed) + minRadius) * 2f;
		        knockback = 12f;
				buildingDamageMultiplier = 0f;
			}};
			
			range = (shootType.lifetime * ((WaveBulletType)shootType).waveSpeed) + ((WaveBulletType)shootType).minRadius;
		}};
		
		concentration = new GravitationalItemTurret("concentration") {{
            requirements(Category.turret, tek(), with(iron, 300, nanoAlloy, 100, uranium, 180, magnet, 120, Items.silicon, 250));
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			var div = 1.4f;
            ammo(
            		Items.silicon, new BasicBulletType(14f, 550f) {{
	        			lifetime /= div;
	        			sprite = "tekton-big-circle-bullet";
	        			height = width = 14f;
	        			shrinkX = shrinkY = 0f;
	                    shootEffect = TektonFx.instShoot;
	                    chargeEffect = TektonFx.concentrationChargeEffect.wrap(siliconShootColor);
	                    hitEffect = new MultiEffect(TektonFx.instHit, new Effect(120f, 20f, e -> {
	                        TektonFx.rand.setSeed(e.id + 1);
	                		color(e.color);
	                        stroke(e.fout() * 2f);
	                        float circleRad = 0.3f + e.finpow() * 180f;
	                        float range = 2f;
	                        float ex = TektonFx.rand.range(range);
	                        float ey = TektonFx.rand.range(range);
	
	                        for(int i = 0; i < 4; i++){
	                            Drawf.tri(e.x + ex, e.y + ey, 5f, 20f * e.fout(), i*90);
	                        }
	
	                        color();
	                        for(int i = 0; i < 4; i++){
	                            Drawf.tri(e.x + ex, e.y + ey, 2.5f, 20f / 3f * e.fout(), i*90);
	                        }
	
	                        Drawf.light(e.x + ex, e.y + ey, circleRad * 1.6f, e.color, e.fout());
	                    }));
	                    smokeEffect = Fx.smokeCloud;
	                    despawnEffect = TektonFx.instBomb;
	
	                    hitSound = Sounds.none;
	                    despawnSound = Sounds.shotgun;
	                    
	                    trailEffect = TektonFx.sparks;
	                    trailChance = 10f;
	                    frontColor = Color.white;
	                    backColor = trailColor = lightColor = lightningColor = hitColor = siliconShootColor;
	
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
	                    pierce = true;
	                    pierceArmor = true;
	                    pierceDamageFactor = 1f / 15f;
	                    pierceCap = 14;
	                    hitShake = 5f;
	                    ammoMultiplier = 1f;
	                    fragBullets = 1;
	                    fragAngle = 0f;
	                    fragRandomSpread = 0f;
	                    fragBullet = 
	                		new ShrapnelBulletType() {{
		                    	damage = 70f;
		                    	length = 70f;
		                    	width = 70f;
		                    	keepVelocity = false;
		                    	pierce = true;
		                    	pierceArmor = false;
								buildingDamageMultiplier = turretBuildingDamageMultipliyer;
		                    	lightColor = lightningColor = hitColor = fromColor = siliconShootColor;
		                    	toColor = Color.white;
		                    	despawnSound = Sounds.none;
	                			hitSound = Sounds.shotgun;
		                    	hitSoundPitch = 1f;
		                    	hitSoundVolume = 0.3f;
		                    }};
            		}},
            		
            		magnet, new BasicBulletType(14f, 400f) {{
	        			lifetime /= div;
	        			sprite = "tekton-big-circle-bullet";
	        			height = width = 14f;
	        			shrinkX = shrinkY = 0f;
	                    shootEffect = TektonFx.instShoot;
	                    chargeEffect = TektonFx.concentrationChargeEffect.wrap(magnetShootColor);
	                    hitEffect = new MultiEffect(TektonFx.instHit, new Effect(120f, 20f, e -> {
	                        TektonFx.rand.setSeed(e.id + 1);
	                		color(e.color);
	                        stroke(e.fout() * 2f);
	                        float circleRad = 0.3f + e.finpow() * 180f;
	                        float range = 2f;
	                        float ex = TektonFx.rand.range(range);
	                        float ey = TektonFx.rand.range(range);
	
	                        for(int i = 0; i < 4; i++){
	                            Drawf.tri(e.x + ex, e.y + ey, 5f, 20f * e.fout(), i*90);
	                        }
	
	                        color();
	                        for(int i = 0; i < 4; i++){
	                            Drawf.tri(e.x + ex, e.y + ey, 2.5f, 20f / 3f * e.fout(), i*90);
	                        }
	
	                        Drawf.light(e.x + ex, e.y + ey, circleRad * 1.6f, e.color, e.fout());
	                    }));
	                    smokeEffect = Fx.smokeCloud;
	                    despawnEffect = TektonFx.instBomb;
	
	                    hitSound = Sounds.none;
	                    despawnSound = Sounds.shotgun;
	                    
	                    trailEffect = TektonFx.sparks;
	                    trailChance = 10f;
	                    frontColor = Color.white;
	                    backColor = trailColor = lightColor = lightningColor = hitColor = magnetShootColor;
	
						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
	                    pierce = true;
	                    pierceArmor = true;
	                    pierceDamageFactor = 1f / 15f;
	                    pierceCap = 14;
	                    hitShake = 5f;
	                    ammoMultiplier = 1f;
	                    fragBullets = 1;
	                    fragAngle = 0f;
	                    fragRandomSpread = 0f;
	                    fragBullet = 
	                		new ShrapnelBulletType() {{
		                    	damage = 140f;
		                    	length = 90f;
		                    	width = 90f;
		                    	keepVelocity = false;
		                    	pierce = true;
		                    	pierceArmor = false;
								buildingDamageMultiplier = turretBuildingDamageMultipliyer;
		                    	lightColor = lightningColor = hitColor = fromColor = magnetShootColor;
		                    	toColor = Color.white;
		                    	despawnSound = Sounds.none;
	                			hitSound = Sounds.shotgun;
		                    	hitSoundPitch = 1f;
		                    	hitSoundVolume = 0.3f;
		                    	
		                    	intervalBullets = 10;
		                    	bulletInterval = 60f;
		                    	intervalBullet = new WaveBulletType() {{
		                    		damage = 0;
		                    		circleDeegres = 360f / 11f;
									minRadius = 8f;
									lifetime = 30f;
									knockback = 30f;
									hitColor = backColor = trailColor = lightColor = magnetShootColor;
									frontColor = Color.white;
									buildingDamageMultiplier = turretBuildingDamageMultipliyer;
		                    	}};
		                    }};
            		}},
            		
            		nanoAlloy, new BasicBulletType(14f, 1100f) {{
            			lifetime /= div;
            			sprite = "tekton-big-circle-bullet";
            			height = width = 14f;
            			shrinkX = shrinkY = 0f;
                        shootEffect = TektonFx.instShoot;
                        chargeEffect = TektonFx.concentrationChargeEffect.wrap(nanoAlloyShootColor);
                        hitEffect = new MultiEffect(TektonFx.instHit, new Effect(120f, 20f, e -> {
                            TektonFx.rand.setSeed(e.id + 1);
                    		color(e.color);
                            stroke(e.fout() * 2f);
                            float circleRad = 0.3f + e.finpow() * 180f;
                            float range = 1f;
                            float ex = TektonFx.rand.range(range);
                            float ey = TektonFx.rand.range(range);

                            for(int i = 0; i < 4; i++){
                                Drawf.tri(e.x + ex, e.y + ey, 5f, 20f * e.fout(), i*90);
                            }

                            color();
                            for(int i = 0; i < 4; i++){
                                Drawf.tri(e.x + ex, e.y + ey, 2.5f, 20f / 3f * e.fout(), i*90);
                            }

                            Drawf.light(e.x + ex, e.y + ey, circleRad * 1.6f, e.color, e.fout());
                        }));
                        smokeEffect = Fx.smokeCloud;
                        despawnEffect = TektonFx.instBomb;
                        
                        hitSound = Sounds.none;
                        despawnSound = Sounds.shotgun;
                        
                        trailEffect = TektonFx.sparks;
                        trailChance = 10f;
                        frontColor = Color.white;
                        backColor = trailColor = lightColor = lightningColor = hitColor = nanoAlloyShootColor;

						buildingDamageMultiplier = turretBuildingDamageMultipliyer;
                        pierce = true;
                        pierceArmor = true;
                        pierceDamageFactor = 1f / 28f;
                        pierceCap = 27;
                        hitShake = 7f;
                        ammoMultiplier = 2f;
                        fragBullets = 1;
                        fragAngle = 0f;
                        fragRandomSpread = 0f;
                        fragBullet = 
                    		new ShrapnelBulletType() {{
    	                    	damage = 140f;
    	                    	length = 70f;
    	                    	width = 70f;
    	                    	keepVelocity = false;
    	                    	pierce = true;
    	                    	pierceArmor = true;
    							buildingDamageMultiplier = turretBuildingDamageMultipliyer;
    	                    	lightColor = lightningColor = hitColor = fromColor = nanoAlloyShootColor;
    	                    	toColor = Color.white;
    	                    	despawnSound = Sounds.none;
                    			hitSound = Sounds.shotgun;
    	                    	hitSoundPitch = 1.1f;
    	                    	hitSoundVolume = 0.3f;

    	                        lightning = 7;
    	                        lightningLength = 14;
    	                        lightningLengthRand = 7;
    	                        lightningDamage = 30f;
    	                    }};
                		}}
            );
            
            var col = Pal.techBlue;
            var mov = 9f;
            
            moveWhileCharging = false;
            shoot.firstShotDelay = 60f;
            
            heatColor = Pal.turretHeat;
            drawer = new DrawTurret("quad-") {{
            	//((RegionPart)parts.get(0)).outlineLayerOffset = -0.01f;
				parts.addAll(
						new RegionPart("-blade") {{
							progress = PartProgress.warmup;
							mirror = true;
							x = 0;
							y = 0;
							moveX = mov / 2f;
							moveY = 0;
							under = true;
							layerOffset = -0.00001f;
							outlineLayerOffset = -0.01f;
							
							moveRot = -30f;
							heatColor = col;
							heatProgress = PartProgress.warmup;
						}},
						new RegionPart("-blade2") {{
							progress = PartProgress.warmup;
							mirror = true;
							x = 0;
							y = 0;
							moveX = mov;
							moveY = 0;
							under = true;
							layerOffset = -0.00002f;
							outlineLayerOffset = -0.01f;
							
							moveRot = -60f;
							heatColor = col;
							heatProgress = PartProgress.warmup;
						}},
						new RegionPart("-glow") {{
							progress = PartProgress.warmup;
							mirror = false;
							x = 0;
							y = 0;
							heatColor = col;
							heatProgress = PartProgress.warmup;
						}}/*,
						new FlarePart() {{
	        				progress = PartProgress.charge;
	        				color1 = col;
	        				color2 = Color.white.cpy();
	        				spinSpeed = 0f;
	        				radius = 0f;
	        				radiusTo = 27f;
	        				layer = Layer.effect;
	        				x = 0f;
	        				y = 14f;
	        			}}*/
				);
			}};
			
			range = 580f / div;
			minWarmup = 0.98f;
            maxAmmo = 40;
            ammoPerShot = 4;
            rotateSpeed = 2f;
            reload = 120f;
            ammoUseEffect = Fx.casing3Double;
            recoil = 3f;
            cooldownTime = reload;
            warmupMaintainTime = 90f;
            shootWarmupSpeed = 0.055f;
            shake = 4f;
            size = 4;
            shootCone = 2f;
            shootSound = Sounds.railgun;
            chargeSound = TektonSounds.greencharge;
            unitSort = UnitSorts.strongest;
            envEnabled |= Env.space;
            minGravity = 8 * gravityMul;
            maxGravity = 16 * gravityMul;

			health = 3100;

			//consumeLiquid(Liquids.hydrogen, 3.5f / 60f);
            //coolant = consume(new ConsumeLiquid(TektonLiquids.ammonia, 4f / 60f));
            consumePower(6f);
        }};
		
        radiance = new GravitationalContinuousTurret("radiance") {{
			requirements(Category.turret, tek(), with(Items.silicon, 800, magnet, 300, zirconium, 1000, polytalum, 500, Items.phaseFabric, 300));
			health = 7440;
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			size = 5;
            reload = 1f;
            shootCone = (360f / 7f) * 2f;
            rotateSpeed = 1.7f;
            shootSound = Sounds.none;
            loopSound = TektonSounds.exterminationbeam;
            loopSoundVolume = 3.5f * 1.8f;
            predictTarget = false;
            scaleDamageEfficiency = true;
            minGravity = 24 * 6;
            maxGravity = minGravity * 1;
            heatColor = Color.clear.cpy();
            shootY = 0f;
            recoil = 1.1f;
            recoilPow = 2f;
            minWarmup = 0.99f;
            cooldownTime = 170f;
            warmupMaintainTime = 130f;
            shootWarmupSpeed = 0.035f;
            //minRange = shootY;
            targetUnderBlocks = false;
            unitSort = UnitSorts.closest;
            aimChangeSpeed = 3f;
            
            var col = Color.valueOf("81fce6");
            
            var bullet = new ContinuousLaserBulletType() {{
            	damage = 70f;
            	length = 300f;
            	width = 1.4f;
				buildingDamageMultiplier = turretBuildingDamageMultipliyer;
                pierceArmor = true;
                
                status = StatusEffects.melting;
                
                colors = new Color[] {Color.valueOf("58edbb55"), Color.valueOf("58edbbaa"), col, Color.white};
                hitColor = colors[3];
            }};
            
           	range = bullet.length;
            
            Seq<Vec2> spawnPositions = new Seq<Vec2>();
            int points = 7;
            float radius = 20f;
            for (int i = 0; i < points; i++) {
            	Vec2 newPos = new Vec2(
            			Mathf.cosDeg(((180f / points) * (i + 0.5f)) - 90f) * -radius, 
            			Mathf.sinDeg(((180f / points) * (i + 0.5f)) - 90f) * -radius
            			).add(new Vec2(25f, 0f));
            	spawnPositions.add(newPos);
            }
            
            var hc = Color.white.cpy();
            var rec = 12f;
            var haloProgress = PartProgress.warmup.delay(0.5f);
            float haloY = -17f, haloRotSpeed = 1f;
            
            drawer = new DrawTurret("quad-") {{
            	//parts.clear();
            	parts.addAll(
        			new RegionPart("-needle3") {{
						progress = PartProgress.warmup.delay(0.6f);
						heatProgress = PartProgress.warmup;
						mirror = true;
						x = y = 0;
						moveX = 5f;
						moveY = 22f - rec;
						moveRot = -9f;
						under = true;
						heatColor = hc;
					}}, 
					new RegionPart("-needle2") {{
						progress = PartProgress.warmup.delay(0.4f);
						heatProgress = PartProgress.warmup;
						mirror = true;
						x = y = 0;
						moveX = 4f;
						moveY = 13f - rec;
						moveRot = -16f;
						under = true;
						heatColor = hc;
					}}, 
					new RegionPart("-needle1") {{
						progress = PartProgress.warmup.delay(0.2f);
						heatProgress = PartProgress.warmup;
						mirror = true;
						x = y = 0;
						moveX = 2f;
						moveY = 4f - rec;
						moveRot = -10f;
						under = true;
						heatColor = hc;
					}}, 
					new RegionPart("-main") {{
						progress = PartProgress.warmup;
						heatProgress = PartProgress.warmup;
						mirror = false;
						moveY -= rec;
						x = y = 0;
						heatColor = hc;
					}},
	                new ShapePart(){{
	                    progress = PartProgress.warmup.delay(0.2f);
	                    color = col;
	                    circle = true;
	                    hollow = true;
	                    stroke = 0f;
	                    strokeTo = 2f;
	                    radius = 10f;
	                    layer = Layer.effect;
	                    y = haloY;
	                    rotateSpeed = haloRotSpeed;
	                }},
	                new ShapePart(){{
	                    progress = PartProgress.warmup.delay(0.2f);
	                    color = col;
	                    circle = false;
	                    hollow = true;
	                    stroke = 0f;
	                    strokeTo = 1.6f;
	                    sides = 4;
	                    radius = 4f;
	                    layer = Layer.effect;
	                    y = haloY;
	                    rotateSpeed = -haloRotSpeed;
	                }},
	                new HaloPart(){{
	                    progress = haloProgress;
	                    color = col;
	                    layer = Layer.effect;
	                    y = haloY;

	                    haloRotation = 90f;
	                    shapes = 2;
	                    triLength = 0f;
	                    triLengthTo = 20f;
	                    haloRadius = 16f;
	                    tri = true;
	                    radius = 4f;
	                }},
	                new HaloPart(){{
	                    progress = haloProgress;
	                    color = col;
	                    layer = Layer.effect;
	                    y = haloY;

	                    haloRotation = 90f;
	                    shapes = 2;
	                    triLength = 0f;
	                    triLengthTo = 5f;
	                    haloRadius = 16f;
	                    tri = true;
	                    radius = 4f;
	                    shapeRotation = 180f;
	                }},
	                /*new HaloPart(){{
	                    progress = haloProgress;
	                    color = col;
	                    layer = Layer.effect;
	                    y = haloY;
	                    haloRotateSpeed = -haloRotSpeed;

	                    shapes = 7;
	                    triLength = 0f;
	                    triLengthTo = 5f;
	                    haloRotation = 45f;
	                    haloRadius = 16f;
	                    tri = true;
	                    radius = 8f;
	                }},
	                new HaloPart(){{
	                    progress = haloProgress;
	                    color = col;
	                    layer = Layer.effect;
	                    y = haloY;
	                    haloRotateSpeed = -haloRotSpeed;

	                    shapes = 7;
	                    shapeRotation = 180f;
	                    triLength = 0f;
	                    triLengthTo = 2f;
	                    haloRotation = 45f;
	                    haloRadius = 16f;
	                    tri = true;
	                    radius = 8f;
	                }},*/
	                new HaloPart(){{
	                    progress = haloProgress;
	                    color = col;
	                    layer = Layer.effect;
	                    y = haloY;
	                    haloRotateSpeed = haloRotSpeed;

	                    shapes = 7;
	                    triLength = 0f;
	                    triLengthTo = 3f;
	                    haloRotation = 45f;
	                    haloRadius = 10f;
	                    tri = true;
	                    radius = 6f;
	                }});
	        		
	        		for (int i : Mathf.signs) {
	        			var offset = 20f * i;
        				parts.add(new HaloPart(){{
		                    progress = haloProgress;
		                    color = col;
		                    layer = Layer.effect;
		                    y = haloY;

		                    haloRotation = 90f + offset;
		                    shapes = 2;
		                    triLength = 0f;
		                    triLengthTo = 20f;
		                    haloRadius = 16f;
		                    tri = true;
		                    radius = 4f;
		                }},
		                new HaloPart(){{
		                    progress = haloProgress;
		                    color = col;
		                    layer = Layer.effect;
		                    y = haloY;

		                    haloRotation = 90f + offset;
		                    shapes = 2;
		                    triLength = 0f;
		                    triLengthTo = 5f;
		                    haloRadius = 16f;
		                    tri = true;
		                    radius = 4f;
		                    shapeRotation = 180f;
		                }});
	        		}
        		
	        		for (var pos : spawnPositions) {
	        			parts.add(new FlarePart() {{
	        				progress = PartProgress.warmup.delay(0.75f);
	        				color1 = col;
	        				color2 = Color.white.cpy();
	        				spinSpeed = 1f;
	        				radius = 0f;
	        				radiusTo = 7f;
	        				layer = Layer.effect;
	        				x = Angles.trnsx(90f, pos.x, pos.y);
	        				y = Angles.trnsy(90f, pos.x, pos.y) + shootY;
	        			}});
	        		}
            	}};
            	
            Effect effect = new MultiEffect(Fx.colorTrail, new ParticleEffect() {{
    			particles = 1;
    			line = true;
    			lifetime = 30;
    			length = 30;
    			lenFrom = 3;
    			lenTo = 0;
    			strokeFrom = 2;
    			strokeTo = 0;
    			colorFrom = Color.white;
    			colorTo = col;
    		}});
            
            shootType = new MultiTargetedBulletType(bullet, spawnPositions, effect) {{ 
                pierceArmor = true;
            }};
            
            consumePower(8f);
			consumeLiquid(TektonLiquids.dicyanogen, 6f / 60f);
            buildCostMultiplier = 0.5f;
		}};
		
		tempest = new ItemTurret("tempest") {{
			requirements(Category.turret, tek(), with(Items.silicon, 800, tantalum, 800, nanoAlloy, 500, uranium, 600, Items.phaseFabric, 300));
			health = 8240;
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			size = 5;
            reload = 12f;
            rotateSpeed = 1.7f;
            shootSound = Sounds.shootSmite;
            var pitchChange = 0.3f;
            soundPitchMin += pitchChange;
    		soundPitchMax += pitchChange;
            loopSound = Sounds.glow;
            loopSoundVolume = 0.8f;
            predictTarget = false;
            heatColor = Color.clear.cpy();
            shootY = 17f;
            recoil = 1.1f;
            recoilPow = 2f;
            recoilTime = 60f;
            minWarmup = 0.99f;
            cooldownTime = 170f;
            warmupMaintainTime = 130f;
            shootWarmupSpeed = 0.035f;
            //minRange = shootY;
            targetUnderBlocks = false;
            //unitSort = UnitSorts.farthest;
    		shootCone = 90f;
            inaccuracy = 45f / 2f;
            shake = 3f;
            
            var circleRad = 7f;
            shoot = new ShootSummon(0f, 0f, circleRad, 20f);

            var col = Pal.lightOrange;
            var rec = 12f;
            var haloProgress = PartProgress.warmup.delay(0.5f);
            float haloY = -17f, haloRotSpeed = 1f;
            
            float armMovX = 4f, armMovY = 4f;
            
            drawer = new DrawTurret("quad-") {{
            	//parts.clear();
            	parts.addAll(
        			new RegionPart("-arm-glow") {{
						progress = PartProgress.warmup;
						heatProgress = PartProgress.warmup.delay(0.6f);
						color = Pal.lighterOrange;
						heatColor = Pal.lightOrange;
						mirror = true;
						x = y = 0;
						moveX = armMovX;
						moveY = armMovY;
						moveRot = -20f;
						under = true;
						
                        outline = false;
                        layerOffset = -0.3f;
                        turretHeatLayer = Layer.turret - 0.2f;
					}},
        			new RegionPart("-arm") {{
						progress = PartProgress.warmup;
						heatProgress = PartProgress.warmup.delay(0.6f);
						mirror = true;
						x = y = 0;
						moveX = armMovX;
						moveY = armMovY;
						moveRot = -20f;
						under = true;
					}},
        			new RegionPart("") {{
						progress = PartProgress.warmup;
						heatProgress = PartProgress.warmup.delay(0.6f);
						mirror = false;
						x = y = 0;
						under = true;
					}},
        			//halo
	                new ShapePart(){{
	                    progress = PartProgress.warmup.delay(0.2f);
	                    color = col;
	                    circle = true;
	                    hollow = true;
	                    stroke = 0f;
	                    strokeTo = 2f;
	                    radius = 10f;
	                    layer = Layer.effect;
	                    y = haloY;
	                    rotateSpeed = haloRotSpeed;
	                }},
	                new ShapePart(){{
	                    progress = PartProgress.warmup.delay(0.2f);
	                    color = col;
	                    circle = false;
	                    hollow = true;
	                    stroke = 0f;
	                    strokeTo = 1.6f;
	                    sides = 4;
	                    radius = 4f;
	                    layer = Layer.effect;
	                    y = haloY;
	                    rotateSpeed = haloRotSpeed;
	                }},
	                new HaloPart(){{
	                    progress = haloProgress;
	                    color = col;
	                    layer = Layer.effect;
	                    y = haloY;
	                    haloRotateSpeed = -haloRotSpeed;

	                    shapes = 7;
	                    triLength = 0f;
	                    triLengthTo = 3f;
	                    haloRotation = 45f;
	                    haloRadius = 10f;
	                    tri = true;
	                    radius = 6f;
	                }},
	                //front halo
	                new ShapePart(){{
	                    progress = PartProgress.warmup.delay(0.2f);
	                    color = col;
	                    circle = true;
	                    hollow = true;
	                    stroke = 0f;
	                    strokeTo = 1.7f;
	                    radius = circleRad;
	                    layer = Layer.effect;
	                    y = shootY;
	                    rotateSpeed = haloRotSpeed;
	                }},
	                new ShapePart(){{
	                    progress = PartProgress.warmup.delay(0.2f);
	                    color = col;
	                    circle = false;
	                    hollow = true;
	                    stroke = 0f;
	                    strokeTo = 1.4f;
	                    sides = 4;
	                    radius = circleRad - 0.5f;
	                    layer = Layer.effect;
	                    y = shootY;
	                    rotateSpeed = -haloRotSpeed;
	                }},
	                new HaloPart(){{
	                    progress = haloProgress;
	                    color = col;
	                    layer = Layer.effect;
	                    y = shootY;
	                    haloRotateSpeed = haloRotSpeed;

	                    shapes = 4;
	                    triLength = 0f;
	                    triLengthTo = 3f;
	                    shapeRotation = 180f;
	                    haloRotation = 45f;
	                    haloRadius = 10f;
	                    tri = true;
	                    radius = circleRad - 1f;
	                }}
    			);
            	for (int i = -3; i <= 3; i++) {
            		final var r = i;
            		float ang = 180f / 7f;
                	parts.addAll(
            			new HaloPart(){{
		                    progress = haloProgress;
		                    color = col;
		                    layer = Layer.effect;
		                    y = haloY;
	
		                    haloRotation = (ang * r) + 180f;
		                    shapes = 1;
		                    triLength = 0f;
		                    triLengthTo = 20f;
		                    haloRadius = 16f;
		                    tri = true;
		                    radius = 4f;
		                }},
		                new HaloPart(){{
		                    progress = haloProgress;
		                    color = col;
		                    layer = Layer.effect;
		                    y = haloY;
	
		                    haloRotation = (ang * r) + 180f;
		                    shapes = 1;
		                    triLength = 0f;
		                    triLengthTo = 5f;
		                    haloRadius = 16f;
		                    tri = true;
		                    radius = 4f;
		                    shapeRotation = 180f;
		                }}
        			);
                }
            }};
            
            var r = 200f;
            range = r * 2f;
            ammo(
            		nanoAlloy, new HomingTeslaBulletType() {{
            			maxRange = r;
            			lengthRand = r * 0.75f;
            			homingRadius = 10f * tilesize;
        				ammoMultiplier = 2;
        				buildingDamageMultiplier = turretBuildingDamageMultipliyer;
        				hitSize = 4;
        				statusDuration = 10f;
        				status = StatusEffects.shocked;
        				lightColor = lightningColor = hitColor = Pal.lightOrange;
        				damage = 140f;
        				lightning = 7;
        				lightningDamage = 30f;
                        lightningLength = 8;
                        lightningLengthRand = 10;

                        chainEffect = TektonFx.tempestChain;
            			applyEffect = Fx.titanExplosion.wrap(Pal.lightOrange);
            			
            			hitSound = TektonSounds.tesla;
    					despawnSound = Sounds.none;
            			hitSoundPitch = 1.4f;
            			hitSoundVolume = 0.3f;
            			
                        despawnShake = 3f;
            			
            			fragBullets = 1;
            			fragBullet = new TektonEmpBulletType() {{
                            collidesAir = true;
                            instantDisappear = true;
                            despawnHit = true;
            				damage = 70f;
            				radius = 70f;
                            clipSize = 250f;
            				powerDamageScl = 2f;
            				unitDamageScl = 1f;
            				buildingDamageMultiplier = turretBuildingDamageMultipliyer;
            				lightColor = lightningColor = hitColor = Pal.lightOrange;
            				hitPowerEffect = new Effect(40, e -> {
            			        color(e.color);
            			        stroke(e.fout() * 1.6f);

            			        randLenVectors(e.id, 18, e.finpow() * 27f, e.rotation, 360f, (x, y) -> {
            			            float ang = Mathf.angle(x, y);
            			            lineAngle(e.x + x, e.y + y, ang, e.fout() * 6 + 1f);
            			        });
            			    });
            				hitEffect = new Effect(50f, 100f, e -> {
                                e.scaled(7f, b -> {
                                    color(Pal.lightOrange, b.fout());
                                    Fill.circle(e.x, e.y, radius);
                                });

                                color(Pal.lightOrange);
                                stroke(e.fout() * 3f);
                                Lines.circle(e.x, e.y, radius);

                                int points = 7;
                                float offset = Mathf.randomSeed(e.id, 360f);
                                for(int i = 0; i < points; i++){
                                    float angle = i* 360f / points + offset;
                                    //for(int s : Mathf.zeroOne){
                                        Drawf.tri(e.x + Angles.trnsx(angle, radius), e.y + Angles.trnsy(angle, radius), 6f, 50f * e.fout(), angle/* + s*180f*/);
                                    //}
                                }

                                Fill.circle(e.x, e.y, 12f * e.fout());
                                color();
                                Fill.circle(e.x, e.y, 6f * e.fout());
                                Drawf.light(e.x, e.y, radius * 1.6f, Pal.lightOrange, e.fout());
                            });
            				chainEffect = Fx.chainEmp.wrap(Pal.lightOrange); 
            				applyEffect = Fx.hitLancer.wrap(Pal.lightOrange);
            			}};
        				
        				hitEffect = new MultiEffect(
        	        			new WaveEffect() {{
        	            	    	sizeFrom = 2;
        	            	    	sizeTo = 14;
        	            	    	lifetime = 10;
        	            	    	strokeFrom = 2;
        	            	    	strokeTo = 0;
        	            	    	colorFrom = Color.valueOf("ffffff");
        	            	    	colorTo = Pal.lightOrange;
        	            	    }},
        	            		new ParticleEffect() {{
        	            			particles = 6;
        	            			line = true;
        	            			lifetime = 15;
        	            			length = 15;
        	            			lenFrom = 3;
        	            			lenTo = 0;
        	            			strokeFrom = 1;
        	            			strokeTo = 0;
        	            			colorFrom = Color.valueOf("ffffff");
        	            			colorTo = Pal.lightOrange;
        	            		}}
                    	    );;
            		}}
            );
            
			
            var coolantConsumption = 6f / 60f;
            coolant = consume(new ConsumeLiquid(TektonLiquids.ammonia, coolantConsumption));
            coolantMultiplier = defCoolantMultiplier / coolantConsumption;
            buildCostMultiplier = 0.4f;
		}};
		
		//units
		
		primordialUnitFactory = new TektonUnitFactory("unit-factory") {{
			requirements(Category.units, tek(), with(iron, 150, zirconium, 90, Items.silicon, 200));
			size = 3;
			health = 550;
			floating = true;
			liquidCapacity = 25f;
			fogRadius = 3;
			consumePower(2f);

			regionSuffix = "-iron";
			
			plans.addAll(
					new UnitPlan(TektonUnits.piezo, 60f * 25f, with(Items.silicon, 25, iron, 45)), 
					new UnitPlan(TektonUnits.martyris, 60f * 15f, with(Items.silicon, 15, zirconium, 25)), 
					new UnitPlan(TektonUnits.caravela, 60f * 30f, with(Items.silicon, 20, polycarbonate, 15)), 
					new UnitPlan(TektonUnits.nail, 60f * 35f, with(Items.silicon, 25, iron, 35, tantalum, 15)));

			researchCostMultiplier = 0.1f;
		}};
		
		//ignored but not replaced
		unitDeveloper = new TektonReconstructor("unit-developer") {{
			requirements(Category.units, with(zirconium, 220, tantalum, 150, Items.silicon, 200), false);

			size = 3;
			health = 1100;
			floating = true;
			fogRadius = 3;
			liquidCapacity = 10f;
			consumePower(3f);
			consumeLiquid(Liquids.hydrogen, 3f / 60f);
			consumeItems(with(Items.silicon, 50, tantalum, 50));
			regionSuffix = "-iron";

			constructTime = 60f * 40f;

			/*upgrades.addAll(
				new UnitType[]{TektonUnits.piezo, TektonUnits.electret},
				new UnitType[]{TektonUnits.martyris, TektonUnits.bellator},
				new UnitType[]{TektonUnits.caravela, TektonUnits.sagres},
				new UnitType[]{TektonUnits.nail, TektonUnits.strike}
			);*/
			
			buildVisibility = BuildVisibility.hidden;
			researchCostMultiplier = 0.25f;
		}};
		
		tankDeveloper = new TektonReconstructor("tank-developer") {{
			requirements(Category.units, tek(), with(iron, 220, tantalum, 150, Items.silicon, 140));

			size = 3;
			health = 1100;
			floating = false;
			fogRadius = 3;
			liquidCapacity = 10f;
			consumePower(3f);
			consumeLiquid(Liquids.hydrogen, 3f / 60f);
			consumeItems(with(Items.silicon, 50, tantalum, 40, iron, 60));
			regionSuffix = "-iron";

			constructTime = 60f * 40f;

			upgrades.add(
				new UnitType[]{TektonUnits.piezo, TektonUnits.electret}
			);
			researchCostMultiplier = 0.2f;
		}};
		
		airDeveloper = new TektonReconstructor("air-developer") {{
			requirements(Category.units, tek(), with(zirconium, 180, tantalum, 120, Items.silicon, 160));

			size = 3;
			health = 1100;
			floating = false;
			fogRadius = 3;
			liquidCapacity = 10f;
			consumePower(3f);
			consumeLiquid(Liquids.hydrogen, 3f / 60f);
			consumeItems(with(Items.silicon, 40, tantalum, 30, zirconium, 50));
			regionSuffix = "-iron";

			constructTime = 60f * 40f;

			upgrades.add(
				new UnitType[]{TektonUnits.martyris, TektonUnits.bellator}
			);
			researchCostMultiplier = 0.25f;
		}};
		
		navalDeveloper = new TektonReconstructor("naval-developer") {{
			requirements(Category.units, tek(), with(polycarbonate, 100, tantalum, 150, Items.silicon, 160));

			size = 3;
			health = 1100;
			floating = true;
			fogRadius = 3;
			liquidCapacity = 10f;
			consumePower(3f);
			consumeLiquid(Liquids.hydrogen, 3f / 60f);
			consumeItems(with(Items.silicon, 50, tantalum, 40, polycarbonate, 30));
			regionSuffix = "-iron";

			constructTime = 60f * 40f;

			upgrades.add(
				new UnitType[]{TektonUnits.caravela, TektonUnits.sagres}
			);
			researchCostMultiplier = 0.25f;
		}};
		
		mechDeveloper = new TektonReconstructor("mech-developer") {{
			requirements(Category.units, tek(), with(Items.graphite, 100, tantalum, 150, Items.silicon, 200));

			size = 3;
			health = 1100;
			floating = false;
			fogRadius = 3;
			liquidCapacity = 10f;
			consumePower(3.5f);
			consumeLiquid(Liquids.hydrogen, 3f / 60f);
			consumeItems(with(Items.silicon, 50, tantalum, 50, Items.graphite, 40));
			regionSuffix = "-iron";

			constructTime = 60f * 40f;

			upgrades.add(
				new UnitType[]{TektonUnits.nail, TektonUnits.strike}
			);
			researchCostMultiplier = 0.25f;
		}};
		
		tankRefabricator = new TektonReconstructor("tank-refabricator") {{
			requirements(Category.units, tek(), with(uranium, 200, Items.silicon, 400, tantalum, 250));

			size = 5;
			health = 2400;
			fogRadius = 3;
			liquidCapacity = 20f;
			consumePower(5f);
			consumeLiquid(Liquids.hydrogen, 6f / 60f);
			consumeItems(with(uranium, 80, Items.silicon, 180, tantalum, 120));
			regionSuffix = "-iron";

			constructTime = 60f * 60f;

			upgrades.addAll(
				new UnitType[]{TektonUnits.electret, TektonUnits.discharge}
			);
            researchCostMultiplier = 0.5f;
		}};
		
		airRefabricator = new TektonReconstructor("air-refabricator") {{
			requirements(Category.units, tek(), with(uranium, 200, Items.silicon, 450, zirconium, 300, tantalum, 150));

			size = 5;
			health = 2400;
			fogRadius = 3;
			liquidCapacity = 20f;
			consumePower(5f);
			consumeLiquid(Liquids.hydrogen, 6f / 60f);
			consumeItems(with(uranium, 80, Items.silicon, 150, zirconium, 200));
			regionSuffix = "-iron";

			constructTime = 60f * 80f;

			upgrades.addAll(
				new UnitType[]{TektonUnits.bellator, TektonUnits.eques}
			);
            researchCostMultiplier = 0.5f;
		}};
		
		navalRefabricator = new TektonReconstructor("naval-refabricator") {{
			requirements(Category.units, tek(), with(uranium, 200, Items.silicon, 400, polycarbonate, 200, tantalum, 180));

			size = 5;
			health = 2400;
			floating = true;
			fogRadius = 3;
			liquidCapacity = 20f;
			consumePower(5f);
			consumeLiquid(Liquids.hydrogen, 6f / 60f);
			consumeItems(with(uranium, 80, Items.silicon, 120, polycarbonate, 150));
			regionSuffix = "-iron";

			constructTime = 60f * 70f;

			upgrades.addAll(
				new UnitType[]{TektonUnits.sagres, TektonUnits.argos}
			);
            researchCostMultiplier = 0.5f;
		}};
		
		mechRefabricator = new TektonReconstructor("mech-refabricator") {{
			requirements(Category.units, tek(), with(uranium, 200, Items.silicon, 450, magnet, 120, tantalum, 200));

			size = 5;
			health = 2400;
			fogRadius = 3;
			liquidCapacity = 20f;
			consumePower(5f);
			consumeLiquid(Liquids.hydrogen, 6f / 60f);
			consumeItems(with(uranium, 100, Items.silicon, 150, Items.graphite, 100));
			regionSuffix = "-iron";

			constructTime = 60f * 80f;

			upgrades.addAll(
				new UnitType[]{TektonUnits.strike, TektonUnits.hammer}
			);
            researchCostMultiplier = 0.5f;
		}};
		
		multiAssembler = new TektonUnitAssembler("multi-assembler") {{
            requirements(Category.units, tek(), with(uranium, 500, polytalum, 400, Items.silicon, 800, tantalum, 500));
            regionSuffix = "-iron";
            size = 5;
            health = 3000;
            plans.addAll(
            		//a null one is needed because modules must make sense i think
            new AssemblerUnitPlan(TektonUnits.none, 0f, PayloadStack.list(nullBlock, 1)),
            new AssemblerUnitPlan(TektonUnits.hysteresis, 60f * 50f, PayloadStack.list(TektonUnits.piezo, 4, TektonBlocks.tantalumWallLarge, 12)),
            new AssemblerUnitPlan(TektonUnits.phalanx, 60f * 70f, PayloadStack.list(TektonUnits.martyris, 4, TektonBlocks.ironWallLarge, 12)),
            new AssemblerUnitPlan(TektonUnits.ariete, 60f * 60f, PayloadStack.list(TektonUnits.caravela, 4, TektonBlocks.polycarbonateWallLarge, 10)),
            new AssemblerUnitPlan(TektonUnits.impact, 60f * 70f, PayloadStack.list(TektonUnits.nail, 5, TektonBlocks.tantalumWallLarge, 12))
            );
            areaSize = 13;
            
            droneType = TektonUnits.assemblyDrone;
            
            consumePower(210f / 60f);
            consumeLiquid(TektonLiquids.dicyanogen, 6f / 60f);
            consumeItem(cryogenicCompound, 1);
            itemDuration = 60f;
            itemCapacity = 40;
            
            researchCostMultiplier = 0.3f;
        }};
        
        ultimateAssembler = new GravitationalUnitAssembler("ultimate-assembler") {{
            requirements(Category.units, tek(), with(uranium, 700, polytalum, 600, Items.silicon, 1000, magnet, 300, Items.phaseFabric, 200));
            regionSuffix = "-iron";
            health = 5200;
            size = 7;
            maxGravity = 12 * gravityMul;
            plans.addAll(
            		//a null one is needed because modules must make sense i think 2
            new AssemblerUnitPlan(TektonUnits.none, 0f, PayloadStack.list(nullBlock, 1)),
            new AssemblerUnitPlan(TektonUnits.supernova, 180f * 60f, PayloadStack.list(TektonUnits.electret, 6, TektonBlocks.uraniumWallLarge, 20)),
            new AssemblerUnitPlan(TektonUnits.imperatoris, 180f * 60f, PayloadStack.list(TektonUnits.bellator, 6, TektonBlocks.uraniumWallLarge, 20)),
            new AssemblerUnitPlan(TektonUnits.castelo, 180f * 60f, PayloadStack.list(TektonUnits.sagres, 6, TektonBlocks.polytalumWallLarge, 20)),
            new AssemblerUnitPlan(TektonUnits.earthquake, 180f * 60f, PayloadStack.list(TektonUnits.strike, 6, TektonBlocks.polytalumWallLarge, 20))
            );
            areaSize = 15;
            
            droneType = TektonUnits.ultimateAssemblyDrone;
            
            consumePower(360f / 60f);
            consumeLiquid(TektonLiquids.dicyanogen, 12f / 60f);
            consumeItem(cryogenicCompound, 2);
            itemConsumption = 2;
            itemDuration = 60f;
            itemCapacity = 80;
            liquidCapacity = 20f;
            
            researchCostMultiplier = 0.5f;
        }};
        
        tankAssemblerModule = new TektonUnitAssemblerModule("tank-assembler-module") {{
            requirements(Category.units, tek(), with(tantalum, 300, uranium, 250, iron, 500, Items.silicon, 200));
            consumePower(2f);
            regionSuffix = "-iron";
            
            tier = 1;
            
            size = 5;
            researchCostMultiplier = 0.6f;
        }};
        
        airAssemblerModule = new TektonUnitAssemblerModule("air-assembler-module") {{
            requirements(Category.units, tek(), with(tantalum, 250, uranium, 200, zirconium, 400, Items.silicon, 300));
            consumePower(2f);
            regionSuffix = "-iron";
            
            tier = 2;
            
            size = 5;
            researchCostMultiplier = 0.6f;
        }};
        
        navalAssemblerModule = new TektonUnitAssemblerModule("naval-assembler-module") {{
            requirements(Category.units, tek(), with(tantalum, 250, uranium, 200, polycarbonate, 350, Items.silicon, 250));
            consumePower(2f);
            regionSuffix = "-iron";
            
            tier = 3;
            
            size = 5;
            researchCostMultiplier = 0.6f;
        }};
        
        mechAssemblerModule = new TektonUnitAssemblerModule("mech-assembler-module") {{
            requirements(Category.units, tek(), with(tantalum, 250, uranium, 200, Items.graphite, 250, Items.silicon, 250));
            consumePower(2f);
            regionSuffix = "-iron";
            
            tier = 4;
            
            size = 5;
            researchCostMultiplier = 0.6f;
        }};
		
        unitRepairTurret = new RepairWaveTurret("unit-repair-turret") {{
            requirements(Category.units, tek(), with(Items.graphite, 100, Items.silicon, 100, tantalum, 80, magnet, 20));
            outlineColor = TektonColor.tektonOutlineColor;

            size = 2;
            repairRadius = 100f;
            repairAmount = 10f;
            
            bullet = new WaveBulletType() {{
            	//range = repairRadius;
            	circleDeegres = repairAngle;
            	minRadius = 5f;
            	linePoints = 24;
                lightColor = hitColor = Pal.heal;
                collidesAir = collidesGround = collidesTeam = true;
                healAmount = (repairAmount / damageInterval) / 5f;
            }};
            
            powerUse = 1f;

            consumePower(1f);
            consumeLiquid(TektonLiquids.oxygen, 2f / 60f);
        }};
        
		//payload
		
		ironPayloadConveyor = new PayloadConveyor("iron-payload-conveyor") {{
			requirements(Category.units, tek(), with(tantalum, 10));
			moveTime = 35f;
			canOverdrive = false;
			health = 800;
			researchCostMultiplier = 4f;
			underBullets = true;
		}};
		
		ironPayloadRouter = new PayloadRouter("iron-payload-router") {{
			requirements(Category.units, tek(), with(tantalum, 15));
			moveTime = 35f;
			health = 800;
			canOverdrive = false;
			researchCostMultiplier = 4f;
			underBullets = true;
		}};
		
		deconstructor = new TektonPayloadDeconstructor("iron-deconstructor") {{
			requirements(Category.units, tek(), with(iron, 120, Items.silicon, 100, zirconium, 100, Items.graphite, 80));
			regionSuffix = "-iron";
			itemCapacity = 250;
			consumePower(40f / 60f);
			size = 3;
			deconstructSpeed = 1f;
            researchCostMultiplier = 0.7f;
		}};

		constructor = new TektonConstructor("iron-constructor") {{
			requirements(Category.units, tek(), with(Items.silicon, 100, iron, 150, tantalum, 80));
			regionSuffix = "-iron";
			hasPower = true;
			buildSpeed = 0.6f;
			consumePower(1f);
			size = 3;
			filter = Seq.with();
            researchCostMultiplier = 0.7f;
		}};
		
		payloadLoader = new TektonPayloadLoader("iron-payload-loader") {{
			requirements(Category.units, tek(), with(Items.graphite, 50, Items.silicon, 50, tantalum, 80));
			regionSuffix = "-iron";
			hasPower = true;
			consumePower(0.2f);
			size = 3;
			fogRadius = 5;
            researchCostMultiplier = 0.7f;
		}};

		payloadUnloader = new TektonPayloadUnloader("iron-payload-unloader") {{
			requirements(Category.units, tek(), with(Items.graphite, 50, Items.silicon, 50, tantalum, 30));
			regionSuffix = "-iron";
			hasPower = true;
			consumePower(0.2f);
			size = 3;
			fogRadius = 5;
            researchCostMultiplier = 0.7f;
		}};
		
		payloadLauncher = new TektonPayloadMassDriver("payload-launcher") {{
			requirements(Category.units, tek(), with(tantalum, 100, Items.silicon, 120, Items.graphite, 50));
			regionSuffix = "-iron";
			size = 3;
			reload = 130f;
			chargeTime = 90f;
			range = 700f;
			maxPayloadSize = 2.5f;
			fogRadius = 5;
			consumePower(0.2f);
			maxGravity = 1 * gravityMul;
            researchCostMultiplier = 0.7f;
		}};
		
		//logic
		
		ironCanvas = new CanvasBlock("iron-canvas"){{
            requirements(Category.logic, tek(), with(Items.silicon, 10, iron, 10, Items.graphite, 5));
            
            canvasSize = 12;
            padding = 7f / 4f * 2f;
            
            size = 2;
        }};
		
        ironMessage = new MessageBlock("iron-message"){{
            requirements(Category.logic, tek(), with(Items.graphite, 5, iron, 5));
            health = 80;
        }};
        
		var bioVisibility = BuildVisibility.sandboxOnly;
		
		//biological
		
		glowPod = new GlowPod("glow-pod") {{
			requirements(Category.logic, tek(bioVisibility), with());
			health = 100;
            brightness = 0.75f;
            radius = 90f;
    		fogRadius = (int)(radius * 0.75f) / Vars.tilesize;
            consumePower(0.05f);
			hideDetails = true;
		}};
		
		smallNest = new Nest("small-nest") {{
			requirements(Category.logic, tek(bioVisibility), with());
			creatureTypes.add(TektonUnits.formica, TektonUnits.diptera);
			size = 2;
			health = 300;
			armor = 0;
			ticksToSpawn = 60f * 30f;
			ticksRandom = 60f * 10f;
			fogRadius = 5;
			lightRadius = 2f;
			powerProduction = 0.4f;
			explosionShake = 1f;
			explosionPuddleRange = tilesize * 2f;
			explosionPuddles = 10;
			explosionRadius = 6;
		    explosionDamage = 100;
		    spawnOnDestroy = true;
		    growScale = 15f;
		}};
		
		mediumNest = new Nest("medium-nest") {{
			requirements(Category.logic, tek(bioVisibility), with());
			creatureTypes.add(TektonUnits.gracilipes, TektonUnits.polyphaga, TektonUnits.colobopsis);
			health = 700;
			fogRadius = 8;
			explosionShake = 2f;
			explosionRadius = 10;
		    explosionDamage = 250;
		}};
		
		largeNest = new Nest("large-nest") {{
			requirements(Category.logic, tek(bioVisibility), with());
			creatureTypes.add(TektonUnits.carabidae, TektonUnits.lepidoptera, TektonUnits.isoptera);
			size = 4;
			health = 1200;
			armor = 3;
			ticksToSpawn = 60f * 70f;
			ticksRandom = 60f * 30f;
			fogRadius = 14;
			lightRadius = 7f;
			powerProduction = 2f;
			explosionShake = 4f;
			explosionShakeDuration = 10f;
			explosionPuddleRange = tilesize * 4f;
			explosionPuddles = 20;
			explosionRadius = 18;
		    explosionDamage = 600;
		    growScale = 30f;
		}};
		
		artery = new BioNode("artery") {{
			requirements(Category.logic, tek(bioVisibility), with());
			health = 200;
			fogRadius = 6;
			maxNodes = 7;
			laserRange = 7;
			hideDetails = true;
		}};
		
		cerebellum = new BioTurret("cerebellum") {{
			requirements(Category.logic, tek(bioVisibility), with());
			squareSprite = false;
			heatColor = TektonColor.acid.cpy();
			drawer = new DrawTurret("nest-") {{
				heatColor = TektonColor.acid.cpy();
				var rad = 20f;
				parts.addAll(
					new HaloPart() {{
						sides = 3;
						haloRotateSpeed = 2f;
						radius = 0f;
						radiusTo = 4f;
						haloRadius = rad;
						color = TektonColor.acid.cpy();
						colorTo = TektonColor.acid.cpy().mul(1.3f);
						layer = Layer.effect;
	                    triLength = 0f;
	                    triLengthTo = 8f;
	                    tri = true;
					}},
					new HaloPart() {{
						sides = 3;
						haloRotateSpeed = -2f;
						radius = 0f;
						radiusTo = 4f;
						haloRadius = rad;
						color = TektonColor.acid.cpy();
						colorTo = TektonColor.acid.cpy().mul(1.3f);
						layer = Layer.effect;
	                    triLength = 0f;
	                    triLengthTo = 8f;
	                    tri = true;
	                    shapeRotation = 180f;
					}},
					new ShapePart () {{
						radius = rad;
						radiusTo = rad;
						hollow = true;
						circle = true;
						stroke = 0f;
						strokeTo = 2f;
						color = TektonColor.acid.cpy();
						colorTo = TektonColor.acid.cpy().mul(1.3f);
						layer = Layer.effect;
					}}
				);
			}};
			float rang = 140f;
			range = rang;
			shootY = 0f;
			size = 2;
			health = 450;
			armor = 2f;
			shootCone = 360f;
			rotateSpeed = 4f;
			recoil = 1f;
			reload = 60f;
			shake = 1f;
			smokeEffect = Fx.none;
			shootSound = Sounds.shockBlast;
			soundPitchMin = 0.9f;
			soundPitchMax = 1.0f;
			shootWarmupSpeed = 0.08f;
			minWarmup = 0.9f;
			inaccuracy = 0;
			cooldownTime = 100;
			warmupMaintainTime = 120;
			predictTarget = false;

			consumePower(20f / 60f);
			
            //unitSort = UnitSorts.strongest;

			shootType = new TeslaBulletType() {{
				maxRange = rang;
				ammoMultiplier = 1;
				buildingDamageMultiplier = 0.25f;
				hitSize = 4;
				statusDuration = 10f;
				status = StatusEffects.shocked;
				hitColor = TektonColor.acid.cpy();
				damage = 200f;
				lightning = 5;
				lightningDamage = 14f;
                lightningLength = 5;
                lightningLengthRand = 8;
				lightningColor = TektonColor.acid.cpy();
				chainEffect = Fx.chainEmp.wrap(TektonColor.acid.cpy());
				applyEffect = TektonFx.biologicalPulseBig;
				hitEffect = new MultiEffect() {{
		         	effects = new Effect[]{
		        			new WaveEffect() {{
		            	    	sizeFrom = 2;
		            	    	sizeTo = 14;
		            	    	lifetime = 10;
		            	    	strokeFrom = 2;
		            	    	strokeTo = 0;
		            	    	colorFrom = Color.valueOf("ffffff");
		            	    	colorTo = TektonColor.acid.cpy();
		            	    }},
		            		new ParticleEffect() {{
		            			particles = 6;
		            			line = true;
		            			lifetime = 15;
		            			length = 15;
		            			lenFrom = 3;
		            			lenTo = 0;
		            			strokeFrom = 1;
		            			strokeTo = 0;
		            			colorFrom = Color.valueOf("ffffff");
		            			colorTo = TektonColor.acid.cpy();
		            		}}
		        	};
		        }};
			}};
		}};
		
		var cobwebLife = 300;
		
		cobwebWall = new StatusEffectWall("cobweb-wall") {{
			requirements(Category.logic, tek(bioVisibility), with());
			health = cobwebLife;
			armor = 4;
			status = TektonStatusEffects.cobwebbed;
			statusDuration = 60f * 3f;
			alwaysUnlocked = false;
		}};
		
		cobwebWallLarge = new StatusEffectWall("cobweb-wall-large") {{
			requirements(Category.logic, tek(bioVisibility), with());
			health = cobwebLife * 4;
			armor = 4;
			size = 2;
			status = TektonStatusEffects.cobwebbed;
			statusDuration = 60f * 3f;
			alwaysUnlocked = false;
		}};
	}

	private static float multiReactionDrill(float value) {
		return 1f + (value / (4^2));
	};
	
	private static float multiCarbonicDrill(float value) {
		return 1f + (value / (5^2));
	};
	
	public static void setPayloadRegions(PayloadBlock block, String regionSuffix) {
		block.topRegion = Core.atlas.find(block.name + "-top", "tekton-factory-top-" + block.size + regionSuffix);
		block.outRegion = Core.atlas.find(block.name + "-out", "tekton-factory-out-" + block.size + regionSuffix);
		block.inRegion = Core.atlas.find(block.name + "-in", "tekton-factory-in-" + block.size + regionSuffix);
	}
	
	public static BuildVisibility tek(BuildVisibility v){
        return new BuildVisibility(() -> Vars.state == null || Vars.state.isMenu() || (v.visible() && Vars.state.rules.planet == TektonPlanets.tekton || Vars.state.rules.env == TektonEnv.any || Vars.state.rules.planet == Planets.sun));
    }

    public static BuildVisibility tek(){
        return tek(BuildVisibility.shown);
    }
}
