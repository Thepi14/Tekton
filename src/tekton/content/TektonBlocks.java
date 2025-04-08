package tekton.content;

import arc.Core;
import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import arc.graphics.Color;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.StatusEffects;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.entities.part.*;
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
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import tekton.*;
import tekton.type.gravity.*;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;
import static tekton.content.TektonColor.*;
import static tekton.content.TektonItems.*;
import static mindustry.type.ItemStack.*;

public class TektonBlocks {
	public static Block 
	
	//Environment
	methane, deepMethane, methaneDarkSilicaSand, silicaSand, darkSilicaSand, 
	brownSand, brownStone, brownIce, brownStoneWall, brownStoneVent, 
	diatomite, diatomiteWall, diatomiteVent,
	
	//ores
	oreIron, oreZirconium, oreTantalum, oreUranium,
	
	//wall ores
	zirconWall, ferricIronWall, wallOreIron, wallOreUranium,
	
	//boulders
	diatomiteBoulder, brownStoneBoulder,
	
	//crafting
	siliconFilter, siliconSmelter, graphiteConcentrator, coldElectrolyzer, atmosphericMethaneConcentrator, 
	polycarbonateSynthesizer, sandFilter, ammoniaCatalyst, cryogenicMixer, polytalumFuser, hydrogenIncinerator,
	
	//gravity
	gravityConductor, gravityRouter, electricalCoil, thermalCoil, phaseNanoCoil,
	
	//walls
	ironWall, ironWallLarge, polycarbonateWall, polycarbonateWallLarge, tantalumWall, tantalumWallLarge, gate, 
	polytalumWall, polytalumWallLarge, uraniumWall, uraniumWallLarge, nanoAlloyWall, nanoAlloyWallLarge,
	
	//transport
	ironDuct, tantalumDuct, ironRouter, ironBridge, ironDistributor, ironOverflow, ironUnderflow, ironSorter, ironInvertedSorter, ironUnloader,
	
	//liquid
	bridgePipe, polycarbonateBridgePipe, pipeJunction, pipeRouter, polycarbonatePipe, pipe, methanePump, polycarbonateLiquidContainer, polycarbonateLiquidReserve,
	
	//power
	lineNode, lineTower, lineLink, powerCapacitor, powerBank, reinforcedDiode, lightningRod, methaneBurner, 
	geothermalGenerator, methaneCombustionChamber, thermalDifferenceGenerator, uraniumReactor, fusionReactor,
	
	//production
	wallDrill, silicaAspirator, silicaTurbine, geothermalCondenser, undergroundWaterExtractor, reactionDrill, carbonicLaserDrill,
	
	//storage
	corePrimal, coreDeveloped, capsule, vault,
	
	//defense
	regenerator, regenerationDome,
	
	//others
	researchRadar, sensor,
	
	//turrets
	one, duel, compass, skyscraper, spear, sword, azure, interfusion, freezer, havoc, tesla, prostrate, concentration, repulsion,
	
	//units
	primordialUnitFactory, unitDeveloper, tankRefabricator, airRefabricator, navalRefabricator, mechRefabricator, 
	multiAssembler, tankAssemblerModule, airAssemblerModule, navalAssemblerModule, mechAssemblerModule,
	
	//payload
	ironPayloadConveyor, ironPayloadRouter, deconstructor, constructor, payloadLoader, payloadUnloader, payloadLauncher,
	
	//debug / sandbox
	gravitySource, nullBlock
	;
	
	public static void load(){
		nullBlock = new Block("null"){{
			requirements(Category.defense, BuildVisibility.debugOnly, with(iron, 1));
			health = 1;
		}};
		
		//Environment
		methane = new Floor("shallow-methane") {{
			speedMultiplier = 0.65f;
			variants = 4;
			liquidDrop = TektonLiquids.liquidMethane;
			attributes.set(TektonAttributes.methane, 0.5f);
			attributes.set(TektonAttributes.liquidMethane, 1f);
			isLiquid = true;
			cacheLayer = CacheLayer.water;
			liquidMultiplier = 1f;
			albedo = 0.2f;
			supportsOverlay = true;
			status = TektonStatusEffects.tarredInMethane;
			statusDuration = 250f;
			attributes.set(Attribute.water, -1f);
		}};
		
		deepMethane = new Floor("deep-methane") {{
			speedMultiplier = 0.15f;
			variants = 4;
			liquidDrop = TektonLiquids.liquidMethane;
			attributes.set(TektonAttributes.methane, 0.25f);
			attributes.set(TektonAttributes.liquidMethane, 2f);
			isLiquid = true;
			cacheLayer = CacheLayer.water;
			liquidMultiplier = 1.5f;
			albedo = 0.2f;
			supportsOverlay = true;
			drownTime = 300f;
			status = TektonStatusEffects.tarredInMethane;
			statusDuration = 700f;
			attributes.set(Attribute.water, -2f);
		}};
		
		silicaSand = new Floor("silica-sand-floor") {{
			itemDrop = Items.sand;
			variants = 2;
			attributes.set(TektonAttributes.silica, 2f);
			playerUnmineable = true;
			attributes.set(Attribute.water, 0.3f);
		}};
		
		darkSilicaSand = new Floor("dark-silica-sand-floor") {{
			itemDrop = Items.sand;
			variants = 2;
			attributes.set(TektonAttributes.silica, 1f);
			playerUnmineable = true;
			attributes.set(Attribute.water, 0.3f);
		}};
		
		//brown
		
		brownSand = new Floor("brown-sand") {{
			itemDrop = Items.sand;
			variants = 3;
			attributes.set(TektonAttributes.silica, 0.7f);
			playerUnmineable = true;
			attributes.set(Attribute.water, 0.7f);
		}};
		
		brownStone = new Floor("brown-stone") {{
			variants = 3;
			//attributes.set(Attribute.water, 0f);
		}};
		
		brownIce = new Floor("brown-ice") {{
			variants = 3;
			attributes.set(Attribute.water, 2f);
		}};
		
		brownStoneBoulder = new Prop("brown-stone-boulder"){{
			variants = 2;
			brownStone.asFloor().decoration = this;
		}};
		
		brownStoneVent = new SteamVent("brown-stone-vent"){{
            parent = blendGroup = brownStone;
            attributes.set(Attribute.steam, 1f);
        }};
		
		brownStoneWall = new StaticWall("brown-stone-wall") {{
			variants = 2;
			brownStone.asFloor().wall = this;
			brownSand.asFloor().wall = this;
			brownIce.asFloor().wall = this;
		}};
        
        //diatomite
		
		diatomite = new Floor("diatomite-floor") {{
			variants = 2;
			attributes.set(Attribute.water, -0.2f);
		}};
		
		methaneDarkSilicaSand = new ShallowLiquid("dark-silica-sand-methane"){{
			speedMultiplier = 0.75f;
			albedo = 0.2f;
			attributes.set(TektonAttributes.methane, 1f);
			attributes.set(TektonAttributes.liquidMethane, 0.5f);
			supportsOverlay = true;
			status = TektonStatusEffects.tarredInMethane;
			statusDuration = 100f;
			attributes.set(Attribute.water, -0.5f);
			mapColor = Color.valueOf("514c3c");
		}};
		
		((ShallowLiquid)methaneDarkSilicaSand).set(methane, darkSilicaSand);
		
		diatomiteWall = new StaticWall("diatomite-wall") {{
			variants = 4;
			diatomite.asFloor().wall = this;
		}};
		
		diatomiteVent = new SteamVent("diatomite-vent"){{
            parent = blendGroup = diatomite;
            attributes.set(Attribute.steam, 1f);
        }};
		
		diatomiteBoulder = new Prop("diatomite-boulder"){{
			variants = 2;
			diatomite.asFloor().decoration = this;
		}};
		
		//1px outline + 4.50 gaussian shadow in gimp <- yes
		
		//ores
		oreIron = new OreBlock("ore-iron", iron){{
			oreDefault = true;
			oreThreshold = 0.864f;
			oreScale = 24.904762f;
			variants = 3;
		}};
		
		oreZirconium = new OreBlock("ore-zirconium", zirconium){{
			oreDefault = true;
			oreThreshold = 0.864f;
			oreScale = 24.904762f;
			variants = 3;
		}};
		
		oreTantalum = new OreBlock("ore-tantalum", tantalum){{
			oreDefault = true;
			oreThreshold = 0.864f;
			oreScale = 24.904762f;
			variants = 3;
		}};
		
		oreTantalum = new OreBlock("ore-uranium", uranium){{
			oreDefault = true;
			oreThreshold = 0.864f;
			oreScale = 24.904762f;
			variants = 3;
		}};
		
		zirconWall = new StaticWall("zircon-wall"){{
			itemDrop = zirconium;
			variants = 3;
		}};
		
		ferricIronWall = new StaticWall("ferric-iron-wall"){{
			itemDrop = iron;
			variants = 3;
		}};
		
		wallOreIron = new OreBlock("ore-wall-iron", iron){{
			wallOre = true;
			variants = 3;
		}};
		
		wallOreUranium = new OreBlock("ore-wall-uranium", uranium){{
			wallOre = true;
			variants = 3;
		}};
		
		//crafting
		siliconFilter = new GenericCrafter("silicon-filter"){{
			requirements(Category.crafting, with(iron, 45, zirconium, 30));
			
			outputItem = new ItemStack(Items.silicon, 1);
			consumeItem(silica, 2);
			craftTime = 140f;
			size = 2;
			health = 360;
			hasItems = true;
			squareSprite = false;

			craftEffect = Fx.drillSteam;
			ambientSound = Sounds.grinding;
			ambientSoundVolume = 0.08f;
			researchCostMultiplier = 0.1f;
		}};
		
		siliconSmelter = new GenericCrafter("silicon-smelter"){{
			requirements(Category.crafting, with(tantalum, 65, zirconium, 80, Items.silicon, 50, Items.graphite, 40));
            craftEffect = Fx.smeltsmoke;
			
			outputItem = new ItemStack(Items.silicon, 7);
			consumePower(150f / 60f);
			consumeItem(silica, 5);
			consumeItem(Items.graphite, 2);
			craftTime = 185f;
			size = 3;
			health = 650;
			itemCapacity = 20;
			hasItems = true;
			squareSprite = false;

            drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffc9de")));
            ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.12f;
			researchCostMultiplier = 0.5f;
		}};
		
		graphiteConcentrator = new GenericCrafter("graphite-concentrator"){{
			requirements(Category.crafting, with(iron, 90, zirconium, 70, Items.silicon, 25));
			
			outputItem = new ItemStack(Items.graphite, 3);
			outputLiquid = new LiquidStack(Liquids.water, 10f / 60f);
			craftTime = 180f;
			size = 3;
			health = 600;
			hasPower = true;
			hasLiquids = true;
			squareSprite = false;
			ignoreLiquidFullness = true;
			liquidCapacity = 40;
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.methane, 0.96f), new DrawDefault());
			ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.07f;

			consumePower(120f / 60f);
			consumeItem(silica, 1);
			consumeLiquid(TektonLiquids.methane, 20f / 60f);
			
			craftEffect = Fx.smeltsmoke;
			ambientSound = Sounds.grinding;
			ambientSoundVolume = 0.05f;
			researchCostMultiplier = 0.1f;
		}};
		
		coldElectrolyzer = new GenericCrafter("cold-electrolyzer") {{
			requirements(Category.crafting, with(zirconium, 100, Items.silicon, 60, Items.graphite, 80));
			consumePower(60f / 60f);
			consumeLiquid(Liquids.water, 8f / 60f);
			
			craftTime = 10f;
			size = 3;
			health = 600;
			squareSprite = false;
			hasPower = true;
			hasLiquids = true;
			rotate = true;
			invertFlip = true;
			group = BlockGroup.liquids;
			itemCapacity = 0;
			liquidCapacity = 50;
			
			regionRotated1 = 3;
			liquidOutputDirections = new int[]{1, 3};
			outputLiquids = LiquidStack.with(TektonLiquids.oxygen, 3f / 60f, Liquids.hydrogen, 6f / 60f);
			
			ambientSound = Sounds.electricHum;
			ambientSoundVolume = 0.08f;
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.water, 5.6f), new DrawBubbles(Color.valueOf("7693e3")){{
				sides = 10;
				recurrence = 3f;
				spread = 6;
				radius = 1.5f;
				amount = 20;
			}}, new DrawRegion("", 0, false), new DrawLiquidOutputs());
			
			researchCostMultiplier = 0.2f;
		}};
		
		atmosphericMethaneConcentrator = new AttributeCrafter("atmospheric-methane-concentrator") {{
			requirements(Category.crafting, with(zirconium, 80, Items.silicon, 60));
			consumePower(70f / 60f);
			squareSprite = false;
			size = 3;
			craftTime = 60f;
			health = 700;
			liquidCapacity = 30;
			
			attribute = TektonAttributes.methane;
			baseEfficiency = 1f;
			boostScale = 0f;
			
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
			requirements(Category.crafting, with(iron, 80, Items.graphite, 60, zirconium, 80, Items.silicon, 50));
			consumePower(200f / 60f);
			squareSprite = true;
			size = 3;
			craftTime = 60f;
			health = 700;
			hasPower = true;
			hasLiquids = true;
			
			consumeLiquid(Liquids.water, 10f / 60f);
			consumeLiquid(TektonLiquids.methane, 6f / 60f);
			outputItem = new ItemStack(polycarbonate, 2);
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.methane, 0.5f), new DrawRegion("-sub"), new DrawLiquidTile(Liquids.water, 10.05f), new DrawDefault(), new DrawFade());

			craftEffect = new Effect(40, e -> {
				randLenVectors(e.id, 6, 5f + e.fin() * 8f, (x, y) -> {
					color(polycarbonate.color, Color.lightGray, e.fin());
					Fill.square(e.x + x, e.y + y, 0.2f + e.fout() * 2f, 45);
				});
			});
			updateEffect = new Effect(40, e -> {
				randLenVectors(e.id, 5, 3f + e.fin() * 5f, (x, y) -> {
					color(polycarbonate.color, Color.gray, e.fin());
					Fill.circle(e.x + x, e.y + y, e.fout());
				});});
			
			ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.9f;
			
			researchCostMultiplier = 0.4f;
		}};
		
		cryogenicMixer = new GenericCrafter("cryogenic-mixer") {{
			requirements(Category.crafting, with(zirconium, 65, tantalum, 70, Items.silicon, 60));
			consumePower(150f / 60f);
			squareSprite = true;
			size = 3;
			craftTime = 60f;
			health = 800;
			hasPower = true;
			hasLiquids = true;
			
			consumeLiquid(Liquids.water, 8f / 60f);
			consumeItem(tantalum);
			outputItem = new ItemStack(cryogenicCompound, 1);
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.water){{drawLiquidLight = true;}}, new DrawDefault(), new DrawGlowRegion(){{
				alpha = 1f;
				glowScale = 5f;
				color = Color.valueOf("87ceeb99");
			}});
			lightLiquid = Liquids.cryofluid;
			
			/*ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.9f;*/
			
			researchCostMultiplier = 0.5f;
		}};
		
		sandFilter = new GenericCrafter("sand-filter") {{
			requirements(Category.crafting, with(iron, 60, zirconium, 80, Items.graphite, 60, polycarbonate, 30));
			consumePower(60f / 60f);
			squareSprite = false;
			size = 3;
			craftTime = 60f * 2f;
			itemCapacity = 10;
			health = 600;
			hasPower = true;
			hasLiquids = true;
			
			consumeLiquid(Liquids.water, 4f / 60f);
			consumeItem(Items.sand, 3);
			outputItem = new ItemStack(silica, 2);

			ambientSound = Sounds.bioLoop;
			ambientSoundVolume = 0.08f;
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawItemFulness(Items.sand) {{ alpha = 0.5f; }}, 
					new DrawLiquidTile(Liquids.water), new DrawItemFulness(silica) {{ alpha = 0.5f; }}, 
					new DrawRegion("-rotator", 0.8f, true), new DrawDefault());
			
			researchCostMultiplier = 0.3f;
		}};
		
		ammoniaCatalyst = new GenericCrafter("ammonia-catalyst") {{
			requirements(Category.crafting, with(iron, 60, zirconium, 80, Items.graphite, 40, tantalum, 40));
			consumePower(60f / 60f);
			squareSprite = false;
			size = 3;
			craftTime = 60f;
			itemCapacity = 0;
			health = 600;
			hasPower = true;
			hasLiquids = true;
			liquidCapacity = 30;

			consumeLiquid(TektonLiquids.ammonia, 3f / 60f);
			outputLiquid = new LiquidStack(Liquids.hydrogen, 9f / 60f);

			ambientSound = Sounds.electricHum;
			ambientSoundVolume = 0.08f;
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidRegion(TektonLiquids.ammonia) {{ suffix = "-liquid1"; }}, new DrawLiquidRegion(Liquids.hydrogen) {{ suffix = "-liquid2"; }}, new DrawDefault());
			
			researchCostMultiplier = 0.3f;
		}};
		
		polytalumFuser = new GenericCrafter("polytalum-fuser") {{
			requirements(Category.crafting, with(Items.graphite, 70, zirconium, 120, Items.silicon, 90, tantalum, 80, polycarbonate, 80));
			consumePower(350f / 60f);
			squareSprite = true;
			size = 3;
			craftTime = 80f;
			health = 600;
			hasPower = true;
			hasLiquids = true;

			consumeItem(polycarbonate, 2);
			consumeItem(tantalum, 1);
			consumeLiquid(Liquids.hydrogen, 3f / 60f);
			consumeLiquid(TektonLiquids.ammonia, 3f / 60f);
			
			outputItem = new ItemStack(polytalum, 1);
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidRegion(Liquids.hydrogen), 
					new DrawLiquidRegion(TektonLiquids.ammonia) {{ suffix = "-liquid2"; }}, new DrawDefault(), new DrawGlowRegion(), new DrawGlowRegion("-glow2") {{ glowScale = -glowScale; }}, new DrawGlowRegion("-glow3") {{ glowScale = 0; color = Liquids.hydrogen.color; }});

			craftEffect = new Effect(40, e -> {
				randLenVectors(e.id, 6, 5f + e.fin() * 8f, (x, y) -> {
					color(polycarbonate.color, Color.lightGray, e.fin());
					Fill.square(e.x + x, e.y + y, 0.2f + e.fout() * 2f, 45);
				});
			});
			updateEffect = new Effect(40, e -> {
				randLenVectors(e.id, 5, 3f + e.fin() * 5f, (x, y) -> {
					color(polycarbonate.color, Color.gray, e.fin());
					Fill.circle(e.x + x, e.y + y, e.fout());
				});});
			
			ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.9f;
			
			researchCostMultiplier = 0.6f;
		}};
		
		hydrogenIncinerator = new ItemIncinerator("hydrogen-incinerator"){{
            requirements(Category.crafting, with(Items.graphite, 5, zirconium, 15));
            effect = TektonFx.incinerateHydrogen;
            size = 1;
            health = 90;
            envEnabled |= Env.space;
            consumePower(0.50f);
            consumeLiquid(Liquids.hydrogen, 0.1f / 60f);
        }};
        
        //gravity stuff
		
		gravityConductor = new GravityConductor("gravity-conductor") {{
			requirements(Category.crafting, with(iron, 20, Items.silicon, 10, polycarbonate, 15));
			health = 280;
			size = 2;
			
            researchCostMultiplier = 5f;
			
            group = BlockGroup.heat;
            //drawer = new DrawMulti(new DrawDefault(), new DrawGravityOutput(), new DrawGravityInput() {{ drawSides = false; }});

			//ambientSound = Sounds.electricHum;
			//ambientSoundVolume = 0.1f;
            
			researchCostMultiplier = 0.2f;
		}};
		
		gravityRouter = new GravityConductor("gravity-router"){{
            requirements(Category.crafting, with(iron, 25, Items.silicon, 15, polycarbonate, 15));
			health = 320;
			
            researchCostMultiplier = 10f;

            group = BlockGroup.heat;
            size = 2;
            drawer = new DrawMulti(new DrawDefault(), new DrawGravityOutput(-1, false), new DrawGravityOutput(), new DrawGravityOutput(1, false), new DrawGravityInput());
            regionRotated1 = 1;
            splitGravity = true;

			//ambientSound = Sounds.electricHum;
			//ambientSoundVolume = 0.1f;
            
			researchCostMultiplier = 0.2f;
        }};
        
        electricalCoil = new GravityProducer("electrical-coil") {{
        	requirements(Category.crafting, with(iron, 75, Items.silicon, 40, Items.graphite, 25));
			health = 320;
			
            researchCostMultiplier = 3f;
            
            drawer = new DrawMulti(new DrawDefault(), new DrawGravityOutput());
            rotateDraw = false;
            size = 2;
            gravityOutput = 1;
            regionRotated1 = 1;
            itemCapacity = 0;
            consumePower(100f / 60f);

            ambientSound = TektonSounds.gravity;
            ambientSoundVolume = 0.03f;

			researchCostMultiplier = 0.2f;
        }};
        
        thermalCoil = new GravityProducer("thermal-coil") {{
        	requirements(Category.crafting, with(iron, 110, Items.silicon, 70, Items.graphite, 40, tantalum, 45));
			health = 400;
			
            researchCostMultiplier = 2f;
            
            drawer = new DrawMulti(new DrawDefault(), new DrawGravityOutput());
            rotateDraw = false;
            size = 2;
            gravityOutput = 4;
            regionRotated1 = 1;
            consumeItem(cryogenicCompound, 1);
            craftTime = 60f * 4f;
            consumePower(150f / 60f);

            ambientSound = TektonSounds.gravity;
            ambientSoundVolume = 0.04f;
            
			researchCostMultiplier = 0.4f;
        }};
        
        phaseNanoCoil = new GravityProducer("phase-nano-coil") {{
        	requirements(Category.crafting, with(iron, 180, Items.silicon, 120, nanoAlloy, 50, polytalum, 80));
			health = 820;
			
            researchCostMultiplier = 2f;
            
            drawer = new DrawMulti(new DrawDefault(), new DrawGravityOutput());
            rotateDraw = false;
            size = 3;
            gravityOutput = 12;
            regionRotated1 = 1;
            consumeItem(Items.phaseFabric, 1);
            craftTime = 60f * 8f;
            consumePower(300f / 60f);

            ambientSound = TektonSounds.gravity;
            ambientSoundVolume = 0.05f;
            
			researchCostMultiplier = 0.55f;
        }};
        
        gravitySource = new GravityProducer("gravity-source"){{
            requirements(Category.crafting, BuildVisibility.sandboxOnly, with());
            drawer = new DrawMulti(new DrawDefault(), new DrawGravityOutput());
            rotateDraw = false;
            size = 1;
            gravityOutput = 1000f;
            warmupRate = 1000f;
            regionRotated1 = 1;
            itemCapacity = 0;
            alwaysUnlocked = true;
            ambientSound = Sounds.none;
            //allDatabaseTabs = true;
        }};
		
		//defense
		var ironLife = 400;
		var polycarbonateLife = 300;
		var tantalumLife = 700;
		var polytalumLife = 600;
		var uraniumLife = 1100;
		var nanoAlloyLife = 950;
		
		ironWall = new Wall("iron-wall"){{
			requirements(Category.defense, with(iron, 6));
			health = ironLife;
			armor = 1f;
			alwaysUnlocked = true;
			researchCostMultiplier = 0f;
		}};
		
		ironWallLarge = new Wall("iron-wall-large"){{
			requirements(Category.defense, with(iron, 24));
			health = ironLife * 4;
			armor = 1f;
			researchCostMultiplier = 0.05f;
			size = 2;
		}};
		
		polycarbonateWall = new Wall("polycarbonate-wall"){{
			requirements(Category.defense, with(polycarbonate, 6, zirconium, 1));
			health = polycarbonateLife;
			insulated = true;
			absorbLasers = true;
			buildCostMultiplier = 4;
			researchCostMultiplier = 0.2f;
		}};
		
		polycarbonateWallLarge = new Wall("polycarbonate-wall-large"){{
			requirements(Category.defense, with(polycarbonate, 24, zirconium, 4));
			health = polycarbonateLife * 4;
			size = 2;
			insulated = true;
			absorbLasers = true;
			buildCostMultiplier = 4;
			researchCostMultiplier = 0.2f;
		}};
		
		tantalumWall = new Wall("tantalum-wall"){{
			requirements(Category.defense, with(tantalum, 6));
			health = tantalumLife;
			armor = 12f;
			buildCostMultiplier = 2;
			researchCostMultiplier = 0.5f;
		}};
		
		tantalumWallLarge = new Wall("tantalum-wall-large"){{
			requirements(Category.defense, with(tantalum, 24));
			health = tantalumLife * 4;
			armor = 12f;
			size = 2;
			buildCostMultiplier = 2;
			researchCostMultiplier = 0.5f;
		}};

		gate = new AutoDoor("gate"){{
            requirements(Category.defense, with(tantalum, 24, Items.silicon, 24));
            health = (tantalumLife * 4) - 20;
            armor = 12f;
            size = 2;
        }};
		
		polytalumWall = new Wall("polytalum-wall"){{
			requirements(Category.defense, with(polytalum, 6, zirconium, 1, polycarbonate, 2));
			health = polytalumLife;
			armor = 7;
			insulated = true;
			absorbLasers = true;
			buildCostMultiplier = 4;
			researchCostMultiplier = 0.5f;
		}};
		
		polytalumWallLarge = new Wall("polytalum-wall-large"){{
			requirements(Category.defense, with(polytalum, 24, zirconium, 4, polycarbonate, 8));
			health = polytalumLife * 4;
			armor = 7;
			size = 2;
			insulated = true;
			absorbLasers = true;
			buildCostMultiplier = 4;
			researchCostMultiplier = 0.5f;
		}};
		
		uraniumWall = new Wall("uranium-wall"){{
			requirements(Category.defense, with(uranium, 6, zirconium, 3));
			health = uraniumLife;
			armor = 14;
			buildCostMultiplier = 2f;
			researchCostMultiplier = 0.5f;
		}};
		
		uraniumWallLarge = new Wall("uranium-wall-large"){{
			requirements(Category.defense, with(uranium, 24, zirconium, 12));
			health = uraniumLife * 4;
			armor = 14;
			size = 2;
			buildCostMultiplier = 2f;
			researchCostMultiplier = 0.5f;
		}};
		
		nanoAlloyWall = new AdvancedWall("nano-alloy-wall"){{
			requirements(Category.defense, with(nanoAlloy, 6, Items.phaseFabric, 1));
			health = nanoAlloyLife;
			armor = 12;
			buildCostMultiplier = 3f;
			lightningChance = 0.05f;
			chanceDeflect = 2f;
			flashHit = true;
			flashColor = Color.red;
			researchCostMultiplier = 0.5f;
			suppressable = true;
		}};
		
		nanoAlloyWallLarge = new AdvancedWall("nano-alloy-wall-large"){{
			requirements(Category.defense, with(nanoAlloy, 24, Items.phaseFabric, 4));
			health = nanoAlloyLife * 4;
			armor = 12;
			size = 2;
			buildCostMultiplier = 3f;
			lightningChance = 0.05f;
			chanceDeflect = 2f;
			flashHit = true;
			flashColor = Color.red;
			lightRadius = 5f;
			researchCostMultiplier = 0.5f;
			suppressable = true;
		}};
		
		//defense
		researchRadar = new TeamRadar("research-radar") {{
			requirements(Category.effect, BuildVisibility.fogOnly, with(Items.silicon, 60, Items.graphite, 50, iron, 100));
			health = 100;
			outlineColor = tektonOutlineColor;
			fogRadius = 50;
			researchCost = with(Items.silicon, 80, Items.graphite, 70, iron, 120);
			size = 2;
			
			consumePower(2f);
		}};
		
		sensor = new CoreRadar("sensor") {{
			requirements(Category.effect, BuildVisibility.fogOnly, with(Items.silicon, 200, Items.graphite, 100, polycarbonate, 100, tantalum, 100));
			size = 3;
			outlineColor = tektonOutlineColor;
			health = 400;
			fogRadius = 4;

			consumePower(500f / 60f);
		}};
		
		var regenFactor = 0.5f;
		
		regenerator = new RegenProjector("regenerator"){{
			requirements(Category.effect, with(Items.silicon, 50, zirconium, 100, Items.graphite, 25, iron, 60));
			size = 2;
			range = 16;
			baseColor = Pal.regen;
			health = 140;
			
			consumePower(1f);
			consumeLiquid(Liquids.hydrogen, 0.5f / 60f);
			consumeItem(Items.phaseFabric).boost();
			
			healPercent = regenFactor / 60f;
			
			Color col = Color.valueOf("8ca9e8");
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.hydrogen, 9f / 4f), new DrawDefault(), new DrawGlowRegion(){{
				color = Color.sky;
			}}, new DrawPulseShape(false){{
				layer = Layer.effect;
				color = col;
			}}, new DrawShape(){{
				layer = Layer.effect;
				radius = 3.5f;
				useWarmupRadius = true;
				timeScl = 2f;
				color = col;
			}}, new DrawWarmupRegion() {{ color = Color.sky; }});
		}};
		
		regenerationDome = new RegenProjector("regeneration-dome"){{
			requirements(Category.effect, with(Items.silicon, 110, tantalum, 100, Items.graphite, 70, polycarbonate, 60));
			size = 3;
			range = 32;
			baseColor = Pal.regen;
			health = 360;
			
			itemCapacity = 20;
			liquidCapacity = 20f;
			
			consumePower(240f / 60f);
			consumeLiquid(Liquids.hydrogen, 2f / 60f);
			consumeItem(Items.phaseFabric, 3).boost();
			
			healPercent = (regenFactor / 60f) * 4f;
			
			Color col = Color.valueOf("8ca9e8");
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.hydrogen, 9f / 4f), new DrawDefault(), new DrawGlowRegion(){{
				color = Color.sky;
			}}, new DrawPulseShape(false){{
				layer = Layer.effect;
				color = col;
			}}, new DrawShape(){{
				layer = Layer.effect;
				radius = 3.5f;
				useWarmupRadius = true;
				timeScl = 2f;
				color = col;
			}}, new DrawWarmupRegion() {{ color = Color.sky; }});
		}};
		
		//transport blocks
		
		ironDuct = new Duct("iron-duct"){{
			requirements(Category.distribution, with(iron, 1));
			health = 80;
			speed = 17f;
			alwaysUnlocked = true;
		}};
		
		tantalumDuct = new Duct("tantalum-duct"){{
			requirements(Category.distribution, with(iron, 1, tantalum, 1));
			health = 140;
			speed = 17f;
			armored = true;
		}};
		
		ironRouter = new Router("iron-duct-router") {{
			requirements(Category.distribution, with(iron, 4));
			health = 100;
			itemCapacity = 2;
			speed = 17f;
			researchCostMultiplier = 0.1f;
		}};
		
		ironDistributor = new Router("iron-duct-distributor") {{
			requirements(Category.distribution, with(iron, 20));
			size = 2;
			health = 400;
			itemCapacity = 8;
			speed = 17f;
			researchCostMultiplier = 0.2f;
		}};
		
		ironOverflow = new OverflowGate("iron-duct-overflow-controller") {{
			requirements(Category.distribution, with(iron, 4, zirconium, 2));
			health = 100;
			speed = 17f;
			researchCostMultiplier = 0.1f;
		}};
		
		ironUnderflow = new OverflowGate("iron-duct-underflow-controller") {{
			requirements(Category.distribution, with(iron, 4, zirconium, 2));
			health = 100;
			speed = 17f;
			researchCostMultiplier = 0.1f;
			invert = true;
		}};
		
		ironSorter = new Sorter("iron-duct-sorter") {{
			requirements(Category.distribution, with(iron, 4, zirconium, 2));
			health = 100;
			researchCostMultiplier = 0.1f;
		}};
		
		ironInvertedSorter = new Sorter("iron-duct-sorter-inverted") {{
			requirements(Category.distribution, with(iron, 4, zirconium, 2));
			health = 100;
			researchCostMultiplier = 0.1f;
			invert = true;
		}};
		
		ironBridge = new DuctBridge("iron-duct-bridge") {{
			requirements(Category.distribution, with(iron, 20, zirconium, 15));
			health = 300;
			speed = 20f;
			range = 4;
			researchCostMultiplier = 0.1f;
			squareSprite = false;

			((Duct)ironDuct).bridgeReplacement = this;
			((Duct)tantalumDuct).bridgeReplacement = this;
		}};
		
		ironUnloader = new DirectionalUnloader("iron-unloader") {{
			requirements(Category.distribution, with(Items.graphite, 20, Items.silicon, 20, iron, 40));
			health = 120;
			speed = 4f;
			solid = false;
			underBullets = true;
			regionRotated1 = 1;
		}};
		
		//liquid
		
		methanePump = new AttributeCrafter("pneumatic-pump") {{
			requirements(Category.liquid, with(iron, 25, zirconium, 15));
			health = 300;
			size = 2;
			squareSprite = false;
			hasLiquids = true;
			liquidCapacity = 30;
			attribute = TektonAttributes.methane;
			baseEfficiency = 0;
			displayEfficiencyScale = 1;
			minEfficiency = 1;
			boostScale = 0.25f;
			floating = true;
			//placeableLiquid = true;
			outputLiquid = new LiquidStack(TektonLiquids.methane, 10f / 60f);
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawCultivator() {{
				plantColor = Color.valueOf("57592b");
				plantColorLight = Color.valueOf("7a7d3c");
				bottomColor = Color.valueOf("7a7d3c");
				recurrence = 6.0f;
			}}, new DrawRegion("-middle"), new DrawRegion("-rotator", 0.8f, true), new DrawRegion("-top"));
			researchCostMultiplier = 0.2f;
		}};
		
		pipe = new Conduit("zirconium-pipe"){{
			requirements(Category.liquid, with(zirconium, 2));
			health = 100;
			leaks = true;
			junctionReplacement = pipeJunction;
			rotBridgeReplacement = bridgePipe;
			liquidCapacity = 10;
			underBullets = true;
			researchCostMultiplier = 0.5f;
		}};
		
		polycarbonatePipe = new Conduit("polycarbonate-pipe"){{
			requirements(Category.liquid, with(zirconium, 2, polycarbonate, 1));
			health = 120;
			leaks = true;
			liquidCapacity = 20;
			underBullets = true;
			liquidPressure = 1.025f;
			researchCostMultiplier = 0.8f;
		}};
		
		pipeJunction = new LiquidJunction("pipe-junction"){{
			requirements(Category.liquid, with(zirconium, 4, Items.graphite, 2));
			health = 100;
			solid = false;
			underBullets = true;
			liquidCapacity = 7;
			squareSprite = false;
			((Conduit)pipe).junctionReplacement = this;
			((Conduit)polycarbonatePipe).junctionReplacement = this;
			researchCostMultiplier = 0.5f;
		}};

		bridgePipe = new DirectionLiquidBridge("bridge-pipe"){{
			requirements(Category.liquid, with(iron, 4, zirconium, 4, Items.graphite, 2));
			health = 170;
			range = 4;
			((Conduit)pipe).rotBridgeReplacement = this;
			researchCostMultiplier = 0.3f;
		}};
		
		polycarbonateBridgePipe = new DirectionLiquidBridge("polycarbonate-bridge-pipe"){{
			requirements(Category.liquid, with(polycarbonate, 4, zirconium, 6, Items.graphite, 4));
			health = 250;
			range = 6;
			((Conduit)polycarbonatePipe).rotBridgeReplacement = this;
			researchCostMultiplier = 0.4f;
		}};
		
		pipeRouter = new LiquidRouter("pipe-router"){{
			requirements(Category.liquid, with(zirconium, 4, Items.graphite, 2));
			health = 100;
			solid = false;
			underBullets = true;
			liquidCapacity = 7;
			squareSprite = false;
			researchCostMultiplier = 0.5f;
		}};
		
		polycarbonateLiquidContainer = new LiquidRouter("polycarbonate-liquid-container") {{
			requirements(Category.liquid, with(zirconium, 14, polycarbonate, 12));
			size = 2;
			solid = true;
			liquidCapacity = 1000f;
			liquidPadding = 6f/4f;
			squareSprite = false;
			researchCostMultiplier = 0.6f;
		}};
		
		polycarbonateLiquidReserve = new LiquidRouter("polycarbonate-liquid-reserve"){{
			requirements(Category.liquid, with(tantalum, 40, polycarbonate, 50));
			size = 3;
			solid = true;
			liquidCapacity = 3000f;
			liquidPadding = 2f;
			squareSprite = false;
			researchCostMultiplier = 0.7f;
		}};
		
		//power
		
		lineNode = new BeamNode("line-node") {{
			requirements(Category.power, with(iron, 3, Items.silicon, 3));
			laserColor1 = Color.valueOf("fffffff0");
			laserColor2 = Color.valueOf("ffd8b880");
			health = 140;
			range = 7;
			consumesPower = outputsPower = true;
			fogRadius = 1;
			researchCostMultiplier = 0.5f;
			
			consumePowerBuffered(100f);
		}};
		
		lineTower = new BeamNode("line-tower") {{
			requirements(Category.power, with(iron, 30, tantalum, 10, Items.silicon, 15));
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
		
		lineLink = new LongPowerNodeLink("line-link"){{
			requirements(Category.power, with(zirconium, 300, Items.silicon, 250, tantalum, 150, polytalum, 150, uranium, 80));
			health = 2000;
			size = 4;
			laserRange = 120f;
			autolink = false;
			laserColor1 = Color.valueOf("ffffff").a(0.45f);
			laserColor2 = Color.valueOf("ffd9c2").a(0.45f);
			glowColor = Color.valueOf("ff8330").a(0.45f);
			laserScale = 1f;
			//scaledHealth = 130;
			crushDamageMultiplier = 0.7f;
			ambientSound = Sounds.pulse;
			ambientSoundVolume = 0.07f;
		}};
		
		powerCapacitor = new Battery("power-capacitor") {{
			health = 140 / 2;
			requirements(Category.power, with(iron, 5, zirconium, 20));
			size = 2;
			emptyLightColor = Color.valueOf("fa6666");
			fullLightColor = Color.valueOf("f8c266");
			consumePowerBuffered(10000f);
			baseExplosiveness = 2f;
		}};
		
		powerBank = new Battery("power-bank") {{
			health = 1040 / 2;
			requirements(Category.power, with(tantalum, 20, zirconium, 50, Items.silicon, 30));
			size = 3;
			emptyLightColor = Color.valueOf("fa6666");
			fullLightColor = Color.valueOf("f8c266");
			consumePowerBuffered(30000f);
			baseExplosiveness = 5f;
		}};
		
		reinforcedDiode = new PowerDiode("reinforced-diode") {{
			health = 350;
			requirements(Category.power, with(Items.silicon, 15, polycarbonate, 15, iron, 10));
		}};
		
		lightningRod = new LightningRod("lightning-rod") {{
			health = 880;
			size = 3;
			cummulative = false;
			outlineColor = tektonOutlineColor;
			lightColor = Color.orange;
			requirements(Category.power, with(Items.silicon, 100, polytalum, 80, iron, 180, nanoAlloy, 20));
		}};
		
		methaneBurner = new ConsumeGenerator("methane-burner") {{
			requirements(Category.power, with(iron, 45, zirconium, 15));
			health = 200;
			size = 2;
			consumeLiquid(TektonLiquids.methane, 6.5f / 60f);
			powerProduction = 1;
			//itemDuration = 80;
			liquidCapacity = 35f;
			generateEffect = Fx.generatespark;
			ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.03f;
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.methane), new DrawCrucibleFlame() {{
				flameColor = Color.valueOf("caf549ff");
				midColor = Color.valueOf("f2d585ff");
				particles = 10;
			}}, new DrawDefault(), new DrawWarmupRegion());
			researchCostMultiplier = 0.4f;
		}};
		
		geothermalGenerator = new ThermalGenerator("geothermal-generator"){{
            requirements(Category.power, with(iron, 60, zirconium, 25, Items.silicon, 30));
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

            drawer = new DrawMulti(new DrawDefault(), new DrawRegion("-rotator-sub", -3f, true) {{ buildingRotate = true; }}, new DrawRegion("-rotator", 3f, true) {{ buildingRotate = true; }});

            hasLiquids = false;
            liquidCapacity = 0f;
            fogRadius = 3;
            researchCostMultiplier = 0.4f;
        }};
		
		methaneCombustionChamber = new ConsumeGenerator("methane-combustion-chamber") {{
			requirements(Category.power, with(iron, 60, zirconium, 80, Items.silicon, 60, tantalum, 50));
			health = 650;
			size = 3;
			squareSprite = false;
			consumeLiquids(LiquidStack.with(TektonLiquids.oxygen, 1f / 60f, TektonLiquids.methane, 40f / 60f));
			powerProduction = 520f / 60f;
			liquidCapacity = 35f;
			generateEffect = Fx.generatespark;
			ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.06f;
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.oxygen, 0.96f), new DrawPistons(){{
				sinMag = 3f;
				sinScl = 5f;
				//sideOffset = Mathf.PI / 2f;
				angleOffset = 45f;
				sinOffset = 60f;
			}}, new DrawRegion("-mid"), new DrawLiquidTile(TektonLiquids.methane, 37f / 4f), new DrawDefault(), new DrawWarmupRegion(), new DrawGlowRegion(){{
				alpha = 1f;
				glowScale = 5f;
				color = Color.valueOf("8045ff99");
			}});
			researchCostMultiplier = 0.6f;
		}};
		
		thermalDifferenceGenerator = new ConsumeGenerator("thermal-difference-generator"){{
			requirements(Category.power, with(iron, 70, tantalum, 50, zirconium, 100, Items.silicon, 65, polycarbonate, 50));
			health = 800;
			powerProduction = 1100f / 60f;
			itemDuration = 2.1f * 60f;
			squareSprite = false;
			hasLiquids = true;
			hasItems = true;
			size = 3;
			ambientSound = Sounds.steam;
			ambientSoundVolume = 0.03f;
			generateEffect = Fx.generatespark;

			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawDefault(), new DrawWarmupRegion(), new DrawParticles() {{
				color = Liquids.cryofluid.color;
				alpha = 0.55f;
				particleSize = 4f;
				particles = 10;
				particleRad = 12f;
				particleLife = 90f;
				blending = Blending.additive;
				reverse = true;
			}}, 
			new DrawRegion("-turbine"){{
				rotateSpeed = 3f;
			}},
			new DrawRegion("-turbine"){{
				rotateSpeed = -3f;
				rotation = 45f;
			}}, new DrawGlowRegion(){{
				alpha = 1f;
				glowScale = 5f;
				color = Color.valueOf("87ceeb99");
			}}, new DrawRegion("-mid-bottom"), new DrawLiquidRegion(), new DrawRegion("-mid"));
			
			lightLiquid = Liquids.cryofluid;

			consumeItem(cryogenicCompound);
			consumeLiquids(LiquidStack.with(Liquids.hydrogen, 2.9f / 60f));
		}};

		uraniumReactor = new TektonNuclearReactor("uranium-reactor"){{
			requirements(Category.power, with(zirconium, 500, Items.silicon, 350, Items.graphite, 200, uranium, 150, polycarbonate, 100));
			ambientSound = Sounds.hum;
			ambientSoundVolume = 0.24f;
			size = 4;
			health = 1400;
			itemDuration = 140f;
			powerProduction = 4000 / 60;
			heating = 0.005f;
			explosionRadius = 25;
			explosionDamage = 4000;
			squareSprite = false;
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidRegion(), new DrawRegion("-sub"), new DrawRegion("-turbine") {{ rotateSpeed = 1.5f; }}, new DrawDefault(), new DrawParticles() {{
				color = Color.lightGray;
				alpha = 0.45f;
				particleSize = 6f;
				particles = 12;
				particleRad = 18;
				particleLife = 180;
				//blending = Blending.additive;
				reverse = true;
			}}, new DrawRegion("-top"), new DrawGlowRegion(){{
				alpha = 1f;
				glowScale = 5f;
				color = Color.valueOf("bbd658ff").a(0.7f);
			}});

			consumeItem(uranium);
			consumeLiquid(Liquids.water, 50f / 60f);
		}};
		
		fusionReactor = new FusionReactor("fusion-reactor") {{
			requirements(Category.power, with(tantalum, 750, Items.silicon, 600, Items.graphite, 300, uranium, 250, polytalum, 450, nanoAlloy, 200));
			health = 3050;
			size = 5;
			squareSprite = false;
			itemDuration = 140f;
			
			itemCapacity = 20;
			liquidCapacity = 100f;

            ambientSound = Sounds.pulse;
            ambientSoundVolume = 0.085f;
            
            //regionRotated1 = 1;
            
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawPlasma() {{ plasma1 = Color.valueOf("ff6ea3"); plasma2 = Color.valueOf("b300ff"); }}, 
            		new DrawRegion("-middle"), new DrawItemLiquidTile(Liquids.cryofluid, cryogenicCompound, 4f * 3f), new DrawDefault(), 
            		new DrawGravityInput(), new DrawGravityRegion() {{ color.a(0.5f); }}, new DrawGlowRegion() {{ color = Color.valueOf("ff6ea3").a(0.6f); }});
            
            lightColor = Color.valueOf("ff6ea3");
			
			explosionRadius = 60;
			explosionDamage = 14000;
			
			powerProduction = 15000 / 60;
			
			consumePower(3000 / 60);
			consumeItem(cryogenicCompound, 1).update(false);
			consumeLiquid(Liquids.hydrogen, 6f / 60f).update(false);
		}};
		
		//production
		
		wallDrill = new BeamDrill("wall-drill") {{
			requirements(Category.production, with(iron, 25));
			health = 350;
			size = 2;
			range = 3;
			drillTime = 400;
			tier = 2;
			ambientSound = Sounds.drill;
			ambientSoundVolume = 0.1f;
			squareSprite = false;
			alwaysUnlocked = true;
			
			consumeLiquid(TektonLiquids.ammonia, 0.25f / 60f).boost();
			optionalBoostIntensity = 1.6f;
			
			sparkColor = Color.valueOf("8fff87");
			heatColor = new Color(0.5f, 1f, 0.42f, 0.9f);
			boostHeatColor = Color.yellow.cpy().mul(0.87f);
		}};
		
		silicaAspirator = new AttributeCrafter("silica-aspirator") {{
			requirements(Category.production, with(iron, 15, zirconium, 10));
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
			requirements(Category.production, with(iron, 80, Items.silicon, 50, Items.graphite, 40, tantalum, 30));
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
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawRegion("-rotator-sub", -10, true), new DrawRegion("-rotator", 10, true) {{ rotation = 45f / 2f; }}, new DrawDefault(), new DrawParticles() {{
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
		
		geothermalCondenser = new AttributeCrafter("geothermal-condenser"){{
            requirements(Category.production, with(Items.graphite, 20, Items.silicon, 40, iron, 60));
            attribute = Attribute.steam;
            group = BlockGroup.liquids;
            minEfficiency = 9f - 0.0001f;
            baseEfficiency = 0f;
            displayEfficiency = false;
			rotate = true;
			invertFlip = true;
            craftEffect = Fx.turbinegenerate;
            craftTime = 120f;
            size = 3;
            ambientSound = Sounds.hum;
            ambientSoundVolume = 0.06f;
            hasLiquids = true;
            boostScale = 1f / 9f;
			ignoreLiquidFullness = false;
			squareSprite = false;
            
            itemCapacity = 0;
            
			regionRotated1 = 3;
			liquidOutputDirections = new int[]{1, 3};
			outputLiquids = LiquidStack.with(TektonLiquids.ammonia, 6f / 60f, Liquids.water, 20f / 60f);

            drawer = new DrawMulti(new DrawRegion("-bottom", 0, false), new DrawLiquidOutputs(), new DrawBlurSpin("-rotator", 6f), new DrawRegion("-mid"), new DrawLiquidTile(TektonLiquids.ammonia, 38f / 4f), new DrawLiquidTile(Liquids.water, 38f / 4f), new DrawRegion("", 0, false));
			
            consumePower(70f / 60f);
            liquidCapacity = 60f;
        }};
        
        var undergroundWaterExtractorDebuff = 2f;
		
		undergroundWaterExtractor = new AttributeCrafter("underground-water-extractor") {{
			requirements(Category.production, with(tantalum, 60, Items.silicon, 60, zirconium, 80, Items.graphite, 60));
			consumePower(100f / 60f);
			craftTime = 1f;
			size = 3;
			health = 750;
			hasPower = true;
			hasLiquids = true;
			rotate = true;
			invertFlip = true;
			group = BlockGroup.liquids;
			itemCapacity = 0;
			liquidCapacity = 15;
			maxBoost = 1f;
			minEfficiency = 0.01f;
			baseEfficiency = 0f;
			boostScale = 1f / 9f;
			ignoreLiquidFullness = false;
			attribute = Attribute.water;
			craftEffect = Fx.none;

			regionRotated1 = 3;
			liquidOutputDirections = new int[]{1, 3};
			outputLiquids = LiquidStack.with(TektonLiquids.ammonia, (6f / 60f) / undergroundWaterExtractorDebuff, Liquids.water, (20f / 60f) / undergroundWaterExtractorDebuff);
			
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(TektonLiquids.ammonia, 4f), new DrawLiquidTile(Liquids.water, 4f), new DrawRegion("", 0, false), new DrawLiquidOutputs(), new DrawRegion("-rotator", 1.5f, true) {{ buildingRotate = true; }}, new DrawRegion("-top", 0, false));

			ambientSound = Sounds.hum;
			ambientSoundVolume = 0.1f;
			squareSprite = false;
			researchCostMultiplier = 0.4f;
		}};
		
		reactionDrill = new BurstDrill("reaction-drill") {{
			requirements(Category.production, with(Items.silicon, 70, TektonItems.zirconium, 120, iron, 120));
			drillTime = 60f * 11f;
			size = 4;
			squareSprite = false;
			hasPower = true;
			tier = 6;
			drillEffect = new MultiEffect(Fx.mineImpact.wrap(zirconiumSpark), Fx.drillSteam.wrap(zirconiumSpark), Fx.mineImpactWave.wrap(zirconiumSpark, 0f));
			baseArrowColor = Color.valueOf("69625c");
			arrowColor = glowColor = Color.valueOf("fdff80");
			arrows = 1;
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
			consumeLiquid(TektonLiquids.ammonia, 2f / 60f).boost();
		}};
		
		carbonicLaserDrill = new Drill("carbonic-laser-drill") {{
			requirements(Category.production, with(iron, 140, Items.silicon, 80, tantalum, 80, Items.graphite, 120, polytalum, 70));
			drillTime = 60f * 5;
			size = 5;
			drawRim = true;
			hasPower = true;
			tier = 5;
			updateEffect = Fx.pulverizeRed;
			updateEffectChance = 0.03f;
			drillEffect = Fx.mineHuge;
			rotateSpeed = 10f;
			warmupSpeed = 0.01f;
			itemCapacity = 50;

	        ambientSoundVolume = 0.044f;
			
			drillMultipliers.put(TektonItems.zirconium, multiCarbonicDrill(0.2f));
			drillMultipliers.put(TektonItems.iron, multiCarbonicDrill(0.4f));
			drillMultipliers.put(TektonItems.tantalum, multiCarbonicDrill(0.1f));
			drillMultipliers.put(TektonItems.uranium, 1f);
			blockedItem = Items.sand;
			//drillMultipliers.put(Items.sand, multiCarbonicDrill(0.5f));
			
			liquidBoostIntensity = 1.6f;
			
			consumePower(480f / 60f);
			consumeLiquid(Liquids.hydrogen, 5f / 60f);
			consumeLiquid(TektonLiquids.ammonia, 3f / 60f).boost();
		}};
		
		//storage
		
		corePrimal = new CoreBlock("core-primal"){{
			requirements(Category.effect, BuildVisibility.shown, with(iron, 1700, zirconium, 1000));
			alwaysUnlocked = true;
			isFirstTier = true;
			unitType = TektonUnits.delta;
			health = 3000;
			armor = 4;
			size = 3;
			itemCapacity = 3000;
			buildCostMultiplier = 1f;
			incinerateNonBuildable = true;
			requiresCoreZone = true;
			squareSprite = false;
			unitCapModifier = 7;
			researchCostMultiplier = 0.07f;
		}};
		
		coreDeveloped = new CoreBlock("core-developed"){{
			requirements(Category.effect, BuildVisibility.shown, with(zirconium, 3000, Items.silicon, 3000, tantalum, 2000));
			alwaysUnlocked = false;
			unitType = TektonUnits.kappa;
			health = 10000;
			armor = 10;
			size = 4;
			itemCapacity = 5000;
			buildCostMultiplier = 1f;
			thrusterLength = 34/4f;
			incinerateNonBuildable = true;
			requiresCoreZone = true;
			squareSprite = false;
			unitCapModifier = 10;
			researchCostMultipliers.put(Items.silicon, 0.5f);
			researchCostMultiplier = 0.17f;
		}};
		
		/*
	 	coreNucleus = new CoreBlock("core-nucleus"){{
			requirements(Category.effect, with(Items.copper, 8000, Items.lead, 8000, Items.silicon, 5000, Items.thorium, 4000));

			unitType = UnitTypes.gamma;
			health = 6000;
			itemCapacity = 13000;
			size = 5;
			thrusterLength = 40/4f;

			unitCapModifier = 24;
			researchCostMultiplier = 0.11f;
		}};
		 */
		
		capsule = new StorageBlock("capsule"){{
			requirements(Category.effect, with(iron, 170, zirconium, 100));
			size = 2;
			itemCapacity = 170;
			health = 300;
			coreMerge = false;
			researchCostMultiplier = 0.1f;
			squareSprite = false;
		}};
		
		vault = new StorageBlock("vault"){{
			requirements(Category.effect, with(iron, 400, zirconium, 300, tantalum, 200));
			size = 3;
			itemCapacity = 750;
			health = 1000;
			coreMerge = false;
			researchCostMultiplier = 0.3f;
			squareSprite = false;
		}};
		
		//turrets
        
        var defCoolantMultiplier = 20f;
		
		one = new ItemTurret("one") {{
            coolantMultiplier = defCoolantMultiplier;
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			drawer = new DrawTurret("quad-");
			requirements(Category.turret, with(iron, 45, zirconium, 35));
			health = 250;
			reload = 60;
			range = 150;
			shootSound = Sounds.pew;
			predictTarget = false;
			maxAmmo = 10;
			inaccuracy = 5;
			ammo(
					iron, new BasicBulletType(6f, 25){{
						width = 7f;
						height = 9f;
						lifetime = 15f;
						shootEffect = Fx.shootSmall;
						shootEffect = Fx.shootSmallSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.valueOf("fff6d4");
						backColor = Color.valueOf("fff6d4");
						trailColor = Color.valueOf("fff6d4");
						buildingDamageMultiplier = 0.25f;
						ammoMultiplier = 2;
					}},
					Items.silicon, new BasicBulletType(6.2f, 35){{
						width = 7f;
						height = 9f;
						lifetime = 15f;
						shootEffect = Fx.shootSmall;
						shootEffect = Fx.shootSmallSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.valueOf("fff6d4");
						backColor = Color.valueOf("fff6d4");
						trailColor = Color.valueOf("fff6d4");
						knockback = 1f;
						homingPower = 0.1f;
						reloadMultiplier = 1.2f;
						buildingDamageMultiplier = 0.25f;
						ammoMultiplier = 2;
					}}
				);
			coolant = consumeCoolant(0.5f / 60f, true, true);
			buildCostMultiplier = 1.4f;
			alwaysUnlocked = true;
		}};
		
		duel = new ItemTurret("duel") {{
            coolantMultiplier = defCoolantMultiplier;
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			size = 2;
			drawer = new DrawTurret("quad-"){{
				parts = new Seq<DrawPart>(new DrawPart[]{
					new RegionPart("-cannon-left"){{
					progress = PartProgress.recoil;
					moveY = -1.5f;
					recoilIndex = 0;
					mirror = false;
					under = false;
					//moves.add(new PartMove(PartProgress.recoil, 0.5f, -0.5f, -8f));
				}}, new RegionPart("-cannon-right"){{
					progress = PartProgress.recoil;
					moveY = -1.5f;
					recoilIndex = 3;
					mirror = false;
					under = false;
				}},
				new RegionPart("-top"){{
					mirror = false;
					under = false;
				}}});
			}};
			requirements(Category.turret, with(iron, 100, zirconium, 120, Items.graphite, 60));
			health = 800;
			reload = 40;
			range = 160;
			//shootSound = Sounds.pew;
			maxAmmo = 20;
			inaccuracy = 3;
			ammoPerShot = 1;
			recoils = 6;

			soundPitchMin = 1.3f;
			soundPitchMax = 1.5f;
			ammo(
					iron, new BasicBulletType(5f, 20){{
						width = 7f;
						height = 9f;
						lifetime = 30f;
						shootEffect = Fx.shootSmall;
						shootEffect = Fx.shootSmallSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.valueOf("fff6d4");
						backColor = Color.valueOf("fff6d4");
						trailColor = Color.valueOf("fff6d4");
						lightRadius = 15;
						lightOpacity = 0.5f;
						lightColor = Color.valueOf("fff6d4");
						trailChance = -1;
						trailWidth = 2;
						trailLength = 7;
						buildingDamageMultiplier = 0.25f;
						ammoMultiplier = 1;
					}},
					tantalum, new BasicBulletType(7f, 35){{
						width = 7f;
						height = 9f;
						lifetime = 22f;
						shootEffect = Fx.shootSmall;
						shootEffect = Fx.shootSmallSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = Color.valueOf("d4d4ff");
						backColor = Color.valueOf("d4d4ff");
						trailColor = Color.valueOf("d4d4ff");
						lightRadius = 15;
						lightOpacity = 0.5f;
						lightColor = Color.valueOf("d4d4ff");
						trailChance = -1;
						trailWidth = 2;
						trailLength = 7;
						pierce = true;
						pierceCap = 2;
						knockback = 0.25f;
						reloadMultiplier = 1.1f;
						buildingDamageMultiplier = 0.25f;
						ammoMultiplier = 2;
					}}
				);
			shoot = new ShootBarrel() {{
				shots = 3;
				shotDelay = 6;
				barrels = new float[] {
					-3, 0, 0,
					-3, 0, 0,
					-3, 0, 0,
					3, 0, 0,
					3, 0, 0,
					3, 0, 0
				};
			}};
			coolant = consumeCoolant(1f / 60f, true, true);
			researchCostMultiplier = 0.4f;
			buildCostMultiplier = 1.5f;
		}};
		
		compass = new PowerTurret("compass"){{
			var theCol = Color.valueOf("ff4733");
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			drawer = new DrawTurret("quad-");
			requirements(Category.turret, with(Items.silicon, 150, iron, 80, zirconium, 60));
			range = 176f;
			size = 2;
			health = 900;
			shootCone = 4;
			recoil = 0.6f;
			reload = 55f;
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
				colorFrom = Color.valueOf("ffffff");
				colorTo = theCol;
			}});

			consumePower(160f / 60f);
			//consumeLiquid(Liquids.water, 0.5f);

			shootType = new BasicBulletType(5.5f, 0){{
				sprite = "large-orb";
				smokeEffect = Fx.shootBigSmoke;
				width = 9f;
				height = 9f;
				lifetime = 30f;
				hitSize = 4f;
				hitColor = backColor = trailColor = theCol;
				frontColor = Color.white;
				lightRadius = 15f;
				lightOpacity = 0.5f;
				trailChance = -1;
				trailWidth = 1.7f;
				trailLength = 6;
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
								colorTo = theCol;
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
								colorTo = theCol;
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
				lightningDamage = 8;
				lightningColor = theCol;
				ammoMultiplier = 1;
				buildingDamageMultiplier = 0.25f;
			}};
			
			buildCostMultiplier = 1.4f;
			researchCostMultiplier = 0.8f;
		}};
		
		skyscraper = new ItemTurret("skyscraper") {{
            coolantMultiplier = defCoolantMultiplier;
			unitSort = UnitSorts.weakest;
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			size = 2;
			drawer = new DrawTurret("quad-"){{
				parts = new Seq<DrawPart>(new DrawPart[]{
					new RegionPart("-barrel"){{
					progress = PartProgress.recoil;
					moveY = -1f;
					//recoilIndex = 3;
					mirror = true;
					under = false;
				}},
				new RegionPart(""){{
					mirror = false;
					under = true;
					progress = PartProgress.constant(0);
				}}});
			}};
			requirements(Category.turret, with(iron, 100, zirconium, 100, Items.graphite, 40));
			health = 600;
			reload = 25f;
			range = 180f;
			//shootSound = Sounds.pew;
			maxAmmo = 10;
			inaccuracy = 14;
			ammoPerShot = 1;
			recoils = 2;
			soundPitchMin = 0.9f;
			soundPitchMax = 1f;
			targetGround = false;
			ammo(
					iron, new FlakBulletType(7f, 3){{
						width = 7f;
						height = 9f;
						lifetime = 30f;
						shootEffect = Fx.shootSmall;
						shootEffect = Fx.shootSmallSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = backColor = trailColor = Color.valueOf("fff6d4");
						lightRadius = 15;
						lightOpacity = 0.5f;
						lightColor = Color.valueOf("fff6d4");
						trailChance = -1;
						trailWidth = 2;
						trailLength = 7;
						buildingDamageMultiplier = 0.25f;
						hitEffect = Fx.flakExplosion;
						splashDamage = 20;
						splashDamageRadius = 18f;
						explodeRange = 12f;
						ammoMultiplier = 2;
					}},
					polycarbonate, new FlakBulletType(7f, 3){{
						width = 7f;
						height = 9f;
						lifetime = 30f;
						shootEffect = Fx.shootSmall;
						shootEffect = Fx.shootSmallSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = backColor = trailColor = polycarbonate.color;
						lightRadius = 15;
						lightOpacity = 0.5f;
						lightColor = polycarbonate.color;
						trailChance = -1;
						trailWidth = 2;
						trailLength = 7;
						reloadMultiplier = 0.8f;
						buildingDamageMultiplier = 0.25f;
						hitEffect = Fx.flakExplosion;
						splashDamage = 25;
						splashDamageRadius = 15f;
						explodeRange = 9f;

						fragBullets = 6;
						fragBullet = new BasicBulletType(3f, 5){{
							width = 5f;
							height = 12f;
							shrinkY = 1f;
							lifetime = 20f;
							backColor = frontColor = polycarbonate.color;
							despawnEffect = Fx.none;
							collidesGround = false;
						}};
						ammoMultiplier = 1;
					}}
				);
			shoot = new ShootAlternate() {{
				spread = 7;
				shotDelay = 0;
				shots = 2;
			}};
			coolant = consumeCoolant(1f / 60f, true, true);
			researchCostMultiplier = 0.4f;
			buildCostMultiplier = 1.5f;
		}};
		
		spear = new ItemTurret("spear") {{
            coolantMultiplier = defCoolantMultiplier;
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			size = 3;
			drawer = new DrawTurret("quad-"){{
				parts = new Seq<DrawPart>(new DrawPart[]{
					new RegionPart("-barrel"){{
					progress = PartProgress.recoil;
					moveY = -4f;
					//recoilIndex = 3;
					mirror = false;
					under = true;
				}},	new RegionPart("-ear"){{
					progress = PartProgress.warmup;
					heatProgress = PartProgress.warmup;
					var a = 1.8f;
					moveX = a;
					moveY = a;
					//recoilIndex = 3;
					mirror = true;
					under = false;
				}},
				new RegionPart(""){{
					mirror = false;
					under = false;
					progress = PartProgress.constant(0);
				}}});
			}};
			unitSort = UnitSorts.strongest;
			shoot.firstShotDelay = 20f;
			requirements(Category.turret, with(Items.silicon, 150, tantalum, 200, Items.graphite, 150));
			health = 1900;
			reload = 100f;
			range = 260f;
			shootSound = Sounds.mediumCannon;
			maxAmmo = 10;
			inaccuracy = 1;
			accurateDelay = true;
			ammoPerShot = 2;
			soundPitchMin = 0.9f;
			soundPitchMax = 1f;
			Effect sfe = new MultiEffect(Fx.shootBigColor, Fx.colorSparkBig);
			Color col = Color.valueOf("ea8878").lerp(Pal.redLight, 0.5f);
			ammo(
					tantalum, new BasicBulletType(10f, 250){{
						hitSize = 7f;
						width = 9f;
						height = 12f;
						lifetime = 20f;
						shootEffect = sfe;
						shootEffect = Fx.shootBig;
						shootEffect = Fx.shootBigSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = backColor = trailColor = col;
						lightRadius = 20;
						lightOpacity = 0.5f;
						lightColor = uranium.color;
						trailChance = -1;
						trailWidth = 2.2f;
						trailLength = 10;
						buildingDamageMultiplier = 0.25f;
						hitEffect = despawnEffect = Fx.blastExplosion;
						pierce = false;
						//pierceBuilding = true;
						pierceArmor = true;
						//pierceCap = 2;
						knockback = 8f;
						
						splashDamageRadius = 30f;
						splashDamage = 50f;
						
						fragOnHit = false;
						fragRandomSpread = 0f;
						fragSpread = 10f;
						fragBullets = 5;
						fragVelocityMin = 1f;
						despawnSound = Sounds.dullExplosion;

						fragBullet = new BasicBulletType(8f, 40){{
							sprite = "missile-large";
							width = 8f;
							height = 12f;
							lifetime = 15f;
							hitSize = 4f;
							hitColor = backColor = trailColor = col;
							frontColor = Color.white;
							trailWidth = 2.8f;
							trailLength = 6;
							hitEffect = despawnEffect = Fx.hitBulletColor;
							splashDamageRadius = 10f;
							splashDamage = 25f;
							pierce = false;
							pierceBuilding = true;
							pierceCap = 3;
							ammoMultiplier = 1;
						}};
					}});
			coolant = consumeCoolant(2f / 60f, true, true);
			researchCostMultiplier = 0.8f;
			buildCostMultiplier = 1.8f;
		}};
		
		sword = new PowerTurret("sword"){{
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			heatColor = Color.valueOf("ff3333df");
			drawer = new DrawTurret("quad-") {{
				heatColor = Color.valueOf("ff3333df");
				parts.addAll(
						new RegionPart("-blade"){{
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
						new RegionPart("-gun"){{
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
						new RegionPart("-top"){{
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
			requirements(Category.turret, with(Items.silicon, 200, iron, 300, tantalum, 120, polycarbonate, 100));
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
			reload = 350f;
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
			cooldownTime = 100;
			warmupMaintainTime = 120;

			consumePower(700f / 60f);
			consumeLiquid(Liquids.water, 3f / 60f);

			shootType = new LaserBulletType(500f / 3f){{
				colors = new Color[]{Color.valueOf("ff4545"), Color.valueOf("ff5959"), Color.valueOf("ff6e6e"), Color.valueOf("ff9191")};
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
				buildingDamageMultiplier = 0.25f;
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
				lightningColor = Color.valueOf("ff5959");
			}};
			
			buildCostMultiplier = 2.3f;
			researchCostMultiplier = 1f;
		}};
		
		azure = new ItemTurret("azure") {{
            coolantMultiplier = defCoolantMultiplier;
			unitSort = UnitSorts.closest;
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			size = 3;
			drawer = new DrawTurret("quad-"){{
				parts.addAll(
				new RegionPart("-barrel-c"){{
					progress = PartProgress.recoil;
					moveY = -1.5f;
					recoilIndex = 0;
					mirror = false;
					under = false;
				}},
				new RegionPart("-barrel-r"){{
					progress = PartProgress.recoil;
					moveY = -1.5f;
					recoilIndex = 1;
					mirror = false;
					under = true;
				}},
				new RegionPart("-barrel-l"){{
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
			requirements(Category.turret, with(iron, 100, zirconium, 100, Items.graphite, 40));
			health = 1000;
			reload = 5f;
			var brange = range = 220f;
			//shootSound = Sounds.pew;
			maxAmmo = 10;
			inaccuracy = 14;
			ammoPerShot = 1;
			soundPitchMin = 0.9f;
			soundPitchMax = 1f;
			targetGround = false;
			ammo(
					polycarbonate, new FlakBulletType(7f, 3){{
						width = 7f;
						height = 9f;
						lifetime = 30f;
						shootEffect = Fx.shootSmall;
						shootEffect = Fx.shootSmallSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = backColor = trailColor = polycarbonate.color;
						lightRadius = 15;
						lightOpacity = 0.5f;
						lightColor = polycarbonate.color;
						trailChance = -1;
						trailWidth = 2;
						trailLength = 7;
						reloadMultiplier = 0.8f;
						buildingDamageMultiplier = 0.25f;
						hitEffect = Fx.flakExplosion;
						splashDamage = 25;
						splashDamageRadius = 15f;
						explodeRange = 9f;

						fragBullets = 6;
						fragBullet = new BasicBulletType(3f, 5){{
							width = 5f;
							height = 12f;
							shrinkY = 1f;
							lifetime = 20f;
							backColor = frontColor = polycarbonate.color;
							despawnEffect = Fx.none;
							collidesGround = false;
						}};
						ammoMultiplier = 1;
					}},
					
					polytalum, new FlakBulletType(9f, 6){{
						rangeChange = 80f;
						width = 7f;
						height = 9f;
						lifetime = 30f;
						shootEffect = Fx.shootSmall;
						shootEffect = Fx.shootSmallSmoke;
						sprite = "tekton-basic-bullet";
						backSprite = "tekton-basic-bullet-back";
						frontColor = backColor = trailColor = polytalum.color;
						lightRadius = 15;
						lightOpacity = 0.5f;
						lightColor = polytalum.color;
						trailChance = -1;
						trailWidth = 2;
						trailLength = 7;
						reloadMultiplier = 1f;
						buildingDamageMultiplier = 0.25f;
						hitEffect = Fx.flakExplosion;
						splashDamage = 40;
						splashDamageRadius = 22f;
						explodeRange = 11f;

						fragBullets = 7;
						fragBullet = new BasicBulletType(3f, 7){{
							width = 5f;
							height = 12f;
							shrinkY = 1f;
							lifetime = 27f;
							backColor = frontColor = polytalum.color;
							despawnEffect = Fx.none;
							collidesGround = false;
						}};
						ammoMultiplier = 1;
					}}
				);
			coolant = consumeCoolant(1f / 60f, true, true);
			researchCostMultiplier = 0.4f;
			buildCostMultiplier = 1.5f;
		}};
		
		interfusion = new ItemTurret("interfusion") {{
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			size = 3;
			unitSort = UnitSorts.farthest;
			heatColor = TektonColor.zirconiumSpark;
			shootY = 2f;
			drawer = new DrawTurret("quad-"){{
				parts.addAll(
				new RegionPart("-barrel"){{
					progress = PartProgress.warmup;
					heatProgress = PartProgress.warmup;
					heatColor = TektonColor.zirconiumSpark;
					moveX = 3f;
					mirror = true;
					under = false;
				}},
				new RegionPart("-inner"){{
					progress = PartProgress.warmup;
					heatProgress = PartProgress.warmup;
					heatColor = TektonColor.zirconiumSpark;
					moveX = 3.4f;
					moveY = -1.2f;
					moveRot = -7f;
					mirror = true;
					under = false;
				}},
				new RegionPart("-outer"){{
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
			requirements(Category.turret, with(Items.silicon, 90, tantalum, 200, Items.graphite, 100, zirconium, 150));
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
					zirconium, new ShrapnelBulletType(){{
						length = tRange * 0.95f;
						damage = 200;
						width = 25f;
						serrationLenScl = 7f;
						serrationSpaceOffset = 60f;
						serrationFadeOffset = 0f;
						serrations = 10;
						serrationWidth = 6f;
						hitLarge = true;
						fromColor = TektonColor.zirconiumSpark;
						toColor = Color.white;
						shootEffect = smokeEffect = Fx.sparkShoot;
						pierceBuilding = true;
						pierceCap = 10;
						knockback = 10;
						ammoMultiplier = 1;
						buildingDamageMultiplier = 0.25f;
					}});
			consumeLiquid(TektonLiquids.oxygen, 1.5f / 60f);
			//coolant = consumeCoolant(0.3f, true, true);
			coolantMultiplier = 0.5f;
			researchCostMultiplier = 0.7f;
			buildCostMultiplier = 2f;
		}};
		
		freezer = new ItemLiquidTurret("freezer") {{
			requirements(Category.turret, with(Items.silicon, 90, tantalum, 100, Items.graphite, 170, polycarbonate, 120));
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
						buildingDamageMultiplier = 0.25f;
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
						buildingDamageMultiplier = 0.25f;
						
			        	hitColor =Color.valueOf("d0ff63ff");
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
						buildingDamageMultiplier = 0.25f;
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
						buildingDamageMultiplier = 0.25f;
					}},
					
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
						buildingDamageMultiplier = 0.25f;
					}}
				);
			
			itemAmmo = cryogenicCompound;
			
			coolantMultiplier = 0f;
			researchCostMultiplier = 0.8f;
			buildCostMultiplier = 1.8f;
		}};
		
		havoc = new ItemTurret("havoc"){{
			requirements(Category.turret, with(Items.silicon, 220, tantalum, 200, zirconium, 160, uranium, 180));
            coolantMultiplier = defCoolantMultiplier;
			squareSprite = false;
			outlineColor = tektonOutlineColor;
            coolantMultiplier = 2f;
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
			size = 4;
			health = 3700;
			shootCone = 1;
			recoil = 1f;
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
			inaccuracy = 0;
			cooldownTime = 100;
			warmupMaintainTime = 120;
            rotateSpeed = 1.5f;

			consumeLiquid(Liquids.hydrogen, 3f / 60f);
            coolant = consume(new ConsumeLiquid(Liquids.water, 4f / 60f));
			
			ammo(
					uranium, new ArtilleryBulletType(3f, 250, "shell"){{
		                hitEffect = new MultiEffect(Fx.titanExplosion, Fx.titanSmoke);
		                despawnEffect = Fx.none;
		                knockback = 3f;
		                lifetime = 140f;
		                height = 19f;
		                width = 17f;
		                splashDamageRadius = 55f;
		                splashDamage = 180f;
		                
		                scaledSplashDamage = true;
		                backColor = hitColor = trailColor = Color.valueOf("ea8878").lerp(Pal.redLight, 0.5f);
		                frontColor = Color.white;
		                ammoMultiplier = 1f;
		                hitSound = Sounds.titanExplosion;

		                status = StatusEffects.blasted;
		                
		                fragLifeMax = 65f;
		                fragLifeMin = 20f;
		                fragVelocityMin = 1f;
		                fragVelocityMax = 3.5f;
		                
		                fragBullets = 16;
		                
		                fragBullet = new ArtilleryBulletType(0.45f, 30f) {{
                            despawnShake = 1f;
                            lightRadius = 30f;
                            lightColor = Pal.redLight;
                            lightOpacity = 0.5f;
                            knockback = 0.8f;
                            width = height = 18f;
		                	lifetime = 1f;
		                	collidesGround = true;
		                	collidesAir = false;
                            collidesTiles = false;
		                	splashDamage = 50f;
		                	splashDamageRadius = 15f;
			                backColor = hitColor = trailColor = Color.valueOf("ea8878").lerp(Pal.redLight, 0.5f);
			                drag = 0.02f;
                            hitEffect = Fx.massiveExplosion;
                            despawnEffect = Fx.scatheSlash;
                            smokeEffect = Fx.shootBigSmoke2;
                            buildingDamageMultiplier = 0.3f;

                            trailLength = 20;
                            trailWidth = 3.5f;
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
		                buildingDamageMultiplier = 0.2f;
		            }});
			
            //unitSort = UnitSorts.weakest;
			
			buildCostMultiplier = 2f;
			researchCostMultiplier = 1f;
		}};
		
		tesla = new PowerTurret("tesla"){{
			requirements(Category.turret, with(Items.silicon, 250, tantalum, 200, polytalum, 160, Items.graphite, 140));
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
			range = 255f;
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

			consumePower(1400f / 60f);
			consumeLiquid(Liquids.water, 5f / 60f);
			
            unitSort = UnitSorts.strongest;

			shootType = new TeslaBulletType(){{
				maxRange = 240f;
				ammoMultiplier = 1;
				buildingDamageMultiplier = 0.25f;
				hitSize = 4;
				statusDuration = 10f;
				status = StatusEffects.shocked;
				hitColor = Color.valueOf("ff5959");
				damage = 1300f;
				lightning = 7;
				lightningDamage = 15f;
                lightningLength = 8;
                lightningLengthRand = 10;
				lightningColor = Color.valueOf("ff5959");
			}};
			
			buildCostMultiplier = 2f;
			researchCostMultiplier = 1f;
		}};
		
		prostrate = new PowerTurret("prostrate"){{
			requirements(Category.turret, with(Items.silicon, 300, tantalum, 160, polytalum, 140, zirconium, 200));
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			heatColor = Color.valueOf("ff3333df");
			var mov = 3.5f;
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
							heatColor = Color.valueOf("ff3333df");
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
							heatColor = Color.valueOf("ff3333df");
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
							heatColor = Color.valueOf("ff3333df");
							heatProgress = PartProgress.warmup;
						}},
						new ShapePart(){{
		                    progress = PartProgress.warmup;
		                    color = Color.valueOf("ff5959");
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
		                new HaloPart(){{
		                    progress = PartProgress.warmup.delay(0.5f);
		                    color = Color.valueOf("ff5959");
		                    layer = Layer.effect;
		                    x = y = 0;

		                    haloRotation = 00f;
		                    shapes = 4;
		                    triLength = 0f;
		                    triLengthTo = 20f;
		                    haloRadius = 4f;
		                    tri = true;
		                    radius = 4f;
		                }}
					);
			}};
			shootY = 0f;
			size = 4;
			health = 2900;
			recoil = 0f;
			reload = 20f;
			shake = 2f;
			smokeEffect = Fx.none;
			heatColor = Color.red;
			shootSound = Sounds.none;
            loopSound = Sounds.glow;
            loopSoundVolume = 0.8f;
			soundPitchMin = 0.6f;
			soundPitchMax = 0.8f;
			shootWarmupSpeed = 0.08f;
			minWarmup = 0.8f;
			cooldownTime = 100;
			warmupMaintainTime = 120;
			predictTarget = false;
			targetGround = false;
			inaccuracy = 0;
			rotateSpeed = 0f;
			shootCone = 361f;
			
            float rad = 200f;
			range = rad;

			consumePower(900f / 60f);
			consumeLiquid(Liquids.water, 5f / 60f);
			
            //unitSort = UnitSorts.closest;

			shootType = new TektonEmpBulletType(){{
				speed = 0f;
				lifetime = 1f;
				ammoMultiplier = 1;
				buildingDamageMultiplier = 0.25f;
				hitSize = 4;
				collidesAir = true;
				collidesGround = false;
				collidesTiles = false;
				absorbable = false;
				despawnHit = true;
				
				statusDuration = 10f;
				status = StatusEffects.shocked;
				hitColor = Color.valueOf("ff5959");
				damage = 100f;
				radius = rad + 10f;
				splashDamageRadius = rad + 10f;
				splashDamage = 1f;
				unitDamageScl = 1f;
				chainEffect = Fx.chainEmp.wrap(hitColor);
				hitPowerEffect.wrap(hitColor);
                applyEffect.wrap(hitColor);
				hitEffect = new Effect(50f, 100f, e -> {
                    color(e.color);
                    stroke(e.fout() * 3f);
                    Lines.circle(e.x, e.y, rad);

                    int points = 16;
                    float offset = 0f;
                    for(int i = 0; i < points; i++){
                        float angle = i* 360f / points + offset;
                        for(int s : Mathf.zeroOne){
                            Drawf.tri(e.x + Angles.trnsx(angle, rad), e.y + Angles.trnsy(angle, rad), 6f, 50f * e.fout(), angle);
                        }
                    }

                    Fill.circle(e.x, e.y, 12f * e.fout());
                    color();
                    Fill.circle(e.x, e.y, 6f * e.fout());
                    Drawf.light(e.x, e.y, rad * 1.6f, e.color, e.fout());
                });
			}
			
			@Override
		    public float continuousDamage(){
		        return damage * 100f / reload * 3f;
		    }
			
			@Override
		    public float estimateDPS(){
		        return damage * 100f / reload * 3f;
		    }
			
			};
			
			buildCostMultiplier = 2.3f;
			researchCostMultiplier = 1f;
		}};
		
		concentration = new ItemTurret("concentration"){{
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			var div = 1.4f;

            requirements(Category.turret, with(iron, 400, polycarbonate, 300, uranium, 300, polytalum, 200, Items.silicon, 400));
            ammo(
            		Items.silicon, new BasicBulletType(14f, 550f){{
        			lifetime /= div;
        			sprite = "tekton-big-circle-bullet";
        			height = width = 14f;
        			shrinkX = shrinkY = 0f;
                    shootEffect = TektonFx.instShoot;
                    hitEffect = TektonFx.instHit;
                    smokeEffect = Fx.smokeCloud;
                    despawnEffect = TektonFx.instBomb;

                    hitSound = Sounds.none;
                    despawnSound = Sounds.shotgun;
                    
                    trailEffect = TektonFx.sparks;
                    trailChance = 10f;
                    frontColor = Color.white;
                    backColor = trailColor = lightColor = lightningColor = hitColor = Pal.techBlue;
                    
                    buildingDamageMultiplier = 0.2f;
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
	                    	length = 50f;
	                    	keepVelocity = false;
	                    	pierce = true;
	                    	pierceArmor = false;
	                    	lightColor = lightningColor = hitColor = fromColor = Pal.techBlue;
	                    	toColor = Color.white;
	                    	despawnSound = Sounds.none;
                			hitSound = Sounds.shotgun;
	                    	hitSoundPitch = 1f;
	                    	hitSoundVolume = 0.3f;
	                    }};
            		}},
            		
            		nanoAlloy, new BasicBulletType(14f, 1000f){{
            			lifetime /= div;
            			sprite = "tekton-big-circle-bullet";
            			height = width = 14f;
            			shrinkX = shrinkY = 0f;
                        shootEffect = TektonFx.instShoot;
                        hitEffect = TektonFx.instHit;
                        smokeEffect = Fx.smokeCloud;
                        despawnEffect = TektonFx.instBomb;
                        
                        hitSound = Sounds.none;
                        despawnSound = Sounds.shotgun;
                        
                        trailEffect = TektonFx.sparks;
                        trailChance = 10f;
                        frontColor = Color.white;
                        backColor = trailColor = lightColor = lightningColor = hitColor = Pal.lightOrange;
                        
                        buildingDamageMultiplier = 0.2f;
                        pierce = true;
                        pierceArmor = true;
                        pierceDamageFactor = 1f / 28f;
                        pierceCap = 27;
                        hitShake = 7f;
                        ammoMultiplier = 1f;
                        fragBullets = 1;
                        fragAngle = 0f;
                        fragRandomSpread = 0f;
                        fragBullet = 
                    		new ShrapnelBulletType() {{
    	                    	damage = 140f;
    	                    	length = 50f;
    	                    	keepVelocity = false;
    	                    	pierce = true;
    	                    	pierceArmor = true;
    	                        buildingDamageMultiplier = 0.5f;
    	                    	lightColor = lightningColor = hitColor = fromColor = Pal.lightOrange;
    	                    	toColor = Color.white;
    	                    	despawnSound = Sounds.none;
                    			hitSound = Sounds.shotgun;
    	                    	hitSoundPitch = 1f;
    	                    	hitSoundVolume = 0.3f;
    	                    }};
                		}}
            );
            
            var col = Pal.techBlue;
            var mov = 9f;
            
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
						}}
				);
			}};
			
			range = 580f / div;
			minWarmup = 0.9f;
            maxAmmo = 40;
            ammoPerShot = 4;
            rotateSpeed = 2f;
            reload = 200f;
            ammoUseEffect = Fx.casing3Double;
            recoil = 3f;
            cooldownTime = reload;
            shake = 4f;
            size = 4;
            shootCone = 2f;
            shootSound = Sounds.railgun;
            unitSort = UnitSorts.strongest;
            envEnabled |= Env.space;

            scaledHealth = 150;

			consumeLiquid(Liquids.hydrogen, 3.5f / 60f);
            consumePower(10f);
        }};
		
		repulsion = new GravitationalTurret("repulsion"){{
			requirements(Category.turret, with(Items.silicon, 450, tantalum, 200, iron, 500, polytalum, 200));
			health = 2700;
			squareSprite = false;
			outlineColor = tektonOutlineColor;
			size = 4;
            heatColor = Color.clear;
            recoil = 0;
            reload = 12f;
            shootSound = Sounds.none;
            loopSound = TektonSounds.gravityemission;
            loopSoundVolume = 4.5f;
            shootY = 0f;
            predictTarget = false;
            minWarmup = 0.8f;
            shootCone = 90f;
            
            drawer = new DrawTurret("quad-") {{ 
            	parts.clear();
            	parts.add(
					new RegionPart(""){{
						progress = PartProgress.warmup;
						heatProgress = PartProgress.warmup;
						mirror = false;
						x = y = 0;
						under = outline = false;
						heatColor = TektonColor.gravityColor;
					}});
            	}};
            consumePower(820f / 60f);
			
			shootType = new WaveBulletType(4f, 0.1f) {{
				lifetime = 60f;
				interval = reload;
				damageInterval = 1f;
		        drawSize = ((lifetime * waveSpeed) + minRadius) * 2f;
		        knockback = 3.5f;
			}};
			
			range = (shootType.lifetime * ((WaveBulletType)shootType).waveSpeed) + ((WaveBulletType)shootType).minRadius;
		}};
		
		//units
		
		primordialUnitFactory = new TektonUnitFactory("unit-factory") {{
			requirements(Category.units, with(iron, 150, zirconium, 90, Items.silicon, 200));
			size = 3;
			health = 550;
			floating = true;
			liquidCapacity = 25f;
			fogRadius = 3;
			consumePower(2f);

			regionSuffix = "-iron";
			setPayloadRegions(this, regionSuffix);
			
			plans.addAll(
					new UnitPlan(TektonUnits.piezo, 60f * 25f, with(iron, 45, Items.silicon, 25)), 
					new UnitPlan(TektonUnits.martyris, 60f * 15f, with(iron, 35, Items.silicon, 15, Items.graphite, 10)),
					new UnitPlan(TektonUnits.caravela, 60f * 30f, with(iron, 35, Items.silicon, 25, polycarbonate, 15)), 
					new UnitPlan(TektonUnits.nail, 60f * 35f, with(iron, 35, Items.silicon, 25, tantalum, 15)));

			researchCostMultiplier = 0.1f;
		}};
		
		unitDeveloper = new TektonReconstructor("unit-developer"){{
			requirements(Category.units, with(zirconium, 220, tantalum, 150, Items.silicon, 200));

			size = 3;
			health = 1100;
			floating = true;
			fogRadius = 3;
			liquidCapacity = 10f;
			consumePower(3f);
			consumeLiquid(Liquids.hydrogen, 5f / 60f);
			consumeItems(with(Items.silicon, 50, tantalum, 50));
			regionSuffix = "-iron";

			constructTime = 60f * 40f;

			upgrades.addAll(
				new UnitType[]{TektonUnits.piezo, TektonUnits.electret},
				new UnitType[]{TektonUnits.martyris, TektonUnits.bellator},
				new UnitType[]{TektonUnits.caravela, TektonUnits.sagres},
				new UnitType[]{TektonUnits.nail, TektonUnits.strike}
			);
			researchCostMultiplier = 0.25f;
		}};
		
		tankRefabricator = new TektonReconstructor("tank-refabricator"){{
			requirements(Category.units, with(uranium, 200, Items.silicon, 400, tantalum, 250));

			size = 5;
			health = 2400;
			fogRadius = 3;
			liquidCapacity = 20f;
			consumePower(5f);
			consumeLiquid(TektonLiquids.ammonia, 8f / 60f);
			consumeItems(with(uranium, 80, Items.silicon, 180, tantalum, 120));
			regionSuffix = "-iron";

			constructTime = 60f * 60f;

			upgrades.addAll(
				new UnitType[]{TektonUnits.electret, TektonUnits.discharge}
			);
            researchCostMultiplier = 0.5f;
		}};
		
		airRefabricator = new TektonReconstructor("air-refabricator"){{
			requirements(Category.units, with(uranium, 200, Items.silicon, 450, zirconium, 300, tantalum, 150));

			size = 5;
			health = 2400;
			fogRadius = 3;
			liquidCapacity = 20f;
			consumePower(5f);
			consumeLiquid(TektonLiquids.ammonia, 8f / 60f);
			consumeItems(with(uranium, 80, Items.silicon, 150, zirconium, 200));
			regionSuffix = "-iron";

			constructTime = 60f * 80f;

			upgrades.addAll(
				new UnitType[]{TektonUnits.bellator, TektonUnits.eques}
			);
            researchCostMultiplier = 0.5f;
		}};
		
		navalRefabricator = new TektonReconstructor("naval-refabricator"){{
			requirements(Category.units, with(uranium, 200, Items.silicon, 400, polycarbonate, 250, tantalum, 180));

			size = 5;
			health = 2400;
			floating = true;
			fogRadius = 3;
			liquidCapacity = 20f;
			consumePower(5f);
			consumeLiquid(TektonLiquids.ammonia, 8f / 60f);
			consumeItems(with(uranium, 80, Items.silicon, 120, polycarbonate, 150));
			regionSuffix = "-iron";

			constructTime = 60f * 70f;

			upgrades.addAll(
				new UnitType[]{TektonUnits.sagres, TektonUnits.argos}
			);
            researchCostMultiplier = 0.5f;
		}};
		
		mechRefabricator = new TektonReconstructor("mech-refabricator"){{
			requirements(Category.units, with(uranium, 200, Items.silicon, 450, Items.graphite, 200, tantalum, 180));

			size = 5;
			health = 2400;
			fogRadius = 3;
			liquidCapacity = 20f;
			consumePower(5f);
			consumeLiquid(TektonLiquids.ammonia, 8f / 60f);
			consumeItems(with(uranium, 100, Items.silicon, 150, Items.graphite, 100));
			regionSuffix = "-iron";

			constructTime = 60f * 80f;

			upgrades.addAll(
				new UnitType[]{TektonUnits.strike, TektonUnits.hammer}
			);
            researchCostMultiplier = 0.5f;
		}};
		
		multiAssembler = new TektonUnitAssembler("multi-assembler"){{
            requirements(Category.units, with(uranium, 300, polytalum, 150, Items.silicon, 300, tantalum, 300, Items.phaseFabric, 60));
            regionSuffix = "-iron";
            size = 5;
            plans.addAll(
            new AssemblerUnitPlan(TektonUnits.none, 0f, PayloadStack.list(nullBlock, 1)),
            new AssemblerUnitPlan(TektonUnits.hysteresis, 60f * 50f, PayloadStack.list(TektonUnits.piezo, 4, TektonBlocks.tantalumWallLarge, 12)),
            new AssemblerUnitPlan(TektonUnits.phalanx, 60f * 70f, PayloadStack.list(TektonUnits.martyris, 4, TektonBlocks.ironWallLarge, 12)),
            new AssemblerUnitPlan(TektonUnits.ariete, 60f * 60f, PayloadStack.list(TektonUnits.caravela, 5, TektonBlocks.polycarbonateWallLarge, 10)),
            new AssemblerUnitPlan(TektonUnits.impact, 60f * 70f, PayloadStack.list(TektonUnits.nail, 5, TektonBlocks.tantalumWallLarge, 12))
            );
            areaSize = 13;
            
            droneType = TektonUnits.assemblyDrone;

            consumePower(210f / 60f);
            consumeLiquid(Liquids.hydrogen, 11f / 60f);
            consumeItem(cryogenicCompound, 1);
            itemDuration = 90f;
            itemCapacity = 40;
            
            researchCostMultiplier = 0.3f;
        }};
        
        tankAssemblerModule = new TektonUnitAssemblerModule("tank-assembler-module"){{
            requirements(Category.units, with(tantalum, 300, uranium, 250, iron, 500, Items.silicon, 200));
            consumePower(4f);
            regionSuffix = "-iron";
            
            tier = 1;
            
            size = 5;
            researchCostMultiplier = 0.6f;
        }};
        
        airAssemblerModule = new TektonUnitAssemblerModule("air-assembler-module"){{
            requirements(Category.units, with(tantalum, 250, uranium, 200, zirconium, 400, Items.silicon, 300));
            consumePower(4f);
            regionSuffix = "-iron";
            
            tier = 2;
            
            size = 5;
            researchCostMultiplier = 0.6f;
        }};
        
        navalAssemblerModule = new TektonUnitAssemblerModule("naval-assembler-module"){{
            requirements(Category.units, with(tantalum, 250, uranium, 200, polycarbonate, 350, Items.silicon, 250));
            consumePower(4f);
            regionSuffix = "-iron";
            
            tier = 3;
            
            size = 5;
            researchCostMultiplier = 0.6f;
        }};
        
        mechAssemblerModule = new TektonUnitAssemblerModule("mech-assembler-module"){{
            requirements(Category.units, with(tantalum, 250, uranium, 200, Items.graphite, 250, Items.silicon, 250));
            consumePower(4f);
            regionSuffix = "-iron";
            
            tier = 4;
            
            size = 5;
            researchCostMultiplier = 0.6f;
        }};
        
		//payload
		
		ironPayloadConveyor = new PayloadConveyor("iron-payload-conveyor"){{
			requirements(Category.units, with(tantalum, 10));
			moveTime = 35f;
			canOverdrive = false;
			health = 800;
			researchCostMultiplier = 4f;
			underBullets = true;
		}};
		
		ironPayloadRouter = new PayloadRouter("iron-payload-router"){{
			requirements(Category.units, with(tantalum, 15));
			moveTime = 35f;
			health = 800;
			canOverdrive = false;
			researchCostMultiplier = 4f;
			underBullets = true;
		}};
		
		deconstructor = new TektonPayloadDeconstructor("iron-deconstructor"){{
			requirements(Category.units, with(iron, 120, Items.silicon, 100, zirconium, 100, Items.graphite, 80));
			regionSuffix = "-iron";
			itemCapacity = 250;
			consumePower(3f);
			size = 3;
			deconstructSpeed = 1f;
            researchCostMultiplier = 0.7f;
		}};

		constructor = new TektonConstructor("iron-constructor"){{
			requirements(Category.units, with(Items.silicon, 100, iron, 150, tantalum, 80));
			regionSuffix = "-iron";
			hasPower = true;
			buildSpeed = 0.6f;
			consumePower(2f);
			size = 3;
			filter = Seq.with();
            researchCostMultiplier = 0.7f;
		}};
		
		payloadLoader = new TektonPayloadLoader("iron-payload-loader"){{
			requirements(Category.units, with(Items.graphite, 50, Items.silicon, 50, tantalum, 80));
			regionSuffix = "-iron";
			hasPower = true;
			consumePower(2f);
			size = 3;
			fogRadius = 5;
            researchCostMultiplier = 0.7f;
		}};

		payloadUnloader = new TektonPayloadUnloader("iron-payload-unloader"){{
			requirements(Category.units, with(Items.graphite, 50, Items.silicon, 50, tantalum, 30));
			regionSuffix = "-iron";
			hasPower = true;
			consumePower(2f);
			size = 3;
			fogRadius = 5;
            researchCostMultiplier = 0.7f;
		}};
		
		payloadLauncher = new TektonPayloadMassDriver("payload-launcher"){{
			requirements(Category.units, with(tantalum, 100, Items.silicon, 120, Items.graphite, 50));
			regionSuffix = "-iron";
			size = 3;
			reload = 130f;
			chargeTime = 90f;
			range = 700f;
			maxPayloadSize = 2.5f;
			fogRadius = 5;
			consumePower(0.6f);
            researchCostMultiplier = 0.7f;
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
}
