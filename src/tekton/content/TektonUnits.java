package tekton.content;

import arc.graphics.*;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.ai.types.*;
import mindustry.entities.Effect;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.ammo.*;
import mindustry.content.*;
import mindustry.type.unit.*;
import mindustry.world.meta.*;
import tekton.*;
import tekton.type.biological.TektonBioUnit;
import tekton.type.bullets.*;
import tekton.type.distanceMissile.*;
//import tekton.content.gen.*;
import tekton.type.minion.*;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import static mindustry.Vars.*;

public class TektonUnits {
	
	public static UnitType 
	piezo, electret, discharge, hysteresis, supernova, //tank
	martyris, bellator, eques, phalanx, imperatoris, //air
	caravela, sagres, argos, ariete, //castelo, //naval
	nail, strike, hammer, impact, //earthquake, //mech
	//physalis, nutricula, chronex, cyanea, gigantea, //ship
	delta, kappa, //sigma, //core
	formica, gracilipes, colobopsis, carabidae, isoptera, araneae, latrodectus, danaus, antheraea, //longicornis, //ground biological
	diptera, polyphaga, groundPolyphaga, lepidoptera, //air biological
	assemblyDrone, //payload
	none, impactMissile, electron //others
	;

    public static int mapMissileDist = 0;

	public static void missileDistance(String id) {
		mapMissileDist = EntityMapping.register(id, DistanceMissileUnitEntity::create);
    }
	public static void missileDistance(String... ids) {
        for (String id : ids) missileDistance(id);
    }
	
	public static void load(){
		float coreFleeRange = 500f;
		//EntityRegister.setupID();
		
		missileDistance("impact-missile");
		
		//others
		
		none = new UnitType("none") {{
            this.constructor = UnitEntity::create;
			speed = 0f;
			mineTier = 0;
			mineSpeed = accel = drag = buildSpeed = 0f;
            isEnemy = false;
            flying = true;
            health = 120;
            armor = 0;
            engineOffset = 0f;
            engineSize = 0;
            hitSize = 0f;
            itemCapacity = 0;
            crashDamageMultiplier = 0;
            strafePenalty = 0.9f;
            engineOffset = 6f;
            alwaysUnlocked = mineFloor = mineWalls = targetable = physics = hittable = false;
            targetPriority = -2;
            fogRadius = 0;
            hidden = true;
		}};
		
        impactMissile = new DistanceMissileUnitType("impact-missile") {{
        	aiController = DistanceMissileAI::new;
            speed = 3.7f;
            range = maxRange = 1f;
            immunities.addAll(TektonMissileUnitType.defaultImmunities);
            maxDistance = 43f * Vars.tilesize;
            lifetime = 60f * 3f;
            outlineColor = TektonColor.tektonOutlineColor.cpy();
            engineColor = trailColor = Pal.techBlue;
            engineLayer = Layer.effect;
            engineOffset = 7;
            health = 60;
            loopSoundVolume = 0.1f;
            rotateSpeed = 4.5f;
            deathExplosionEffect = Fx.none;
            trailLength = 14;
            lowAltitude = true;
            hitSize = 7f;
            targetAir = true;
            
            parts.add(new FlarePart() {{
                progress = PartProgress.life.slope().curve(Interp.pow2In);
                radius = 0f;
                radiusTo = 45f;
                stroke = 3f;
                rotation = 0f;
                y = -8f;
                followRotation = false;
                layer = Layer.flyingUnit + 1f;
            }});
            
            weapons.add(new Weapon() {{
                shootCone = 360f;
                mirror = false;
                reload = 1f;
                shootOnDeath = true;
            	shootY = 0f;
                
                bullet = new ExplosionBulletType(70f * 2f, 35f) {{
                    collidesAir = true;
                	range = 1f;
                	hitShake = 3f;
                	shootEffect = new MultiEffect(Fx.massiveExplosion, new WrapEffect(Fx.dynamicSpikes, Pal.techBlue, 24f), new WaveEffect() {{
                        colorFrom = colorTo = Pal.techBlue;
                        sizeTo = 40f;
                        lifetime = 12f;
                        strokeFrom = 4f;
                    }});
                }};
            }});
        }};

		electron = new MinionUnitType("electron") {{
			this.constructor = TimedKillUnit::create;
            aiController = MinionAI::new;
			flying = true;
			hoverable = true;
			hidden = true;
			alwaysUnlocked = true;
			playerControllable = false;
			logicControllable = false;
			useUnitCap = false;
			createScorch = createWreck = false;
			drawShields = false;
			float rang = 100f, weapY = -0.1f;
            hitSize = 9f;
			speed = 1.7f;
            accel = 0.06f;
            drag = 0.05f;
            range = maxRange = rang;
            lifetime = 60f * 3f;
            outlineColor = TektonColor.tektonOutlineColor.cpy();
            engineOffset = 7;
            health = 30f;
            loopSoundVolume = 0f;
            rotateSpeed = 10f;
            baseRotateSpeed = 10f;
            trailLength = 0;
            lowAltitude = true;
            omniMovement = true;
            alwaysShootWhenMoving = true;
            rotateMoveFirst = false;
            circleTarget = true;
            autoFindTarget = true;
            faceTarget = true;
            physics = true;
            bounded = true;
            homingDelay = 0f;
            missileAccelTime = 0f;
            
            abilities.add(new ForceFieldAbility(30f, 0.1f, 80f, 60f * 100, 4, 0f));
            parts.add(new HoverPart() {{
    			x = 0f;
    			y = weapY;
            	mirror = false;
            	radius = 10;
            	stroke = 1.5f;
            	minStroke = 0;
            	circles = 4;
            	sides = 10;
            	phase = 80;
            	layerOffset = -0.01f;
            	color = Pal.lancerLaser;
    		}});
            
            weapons.add(new Weapon(name + "-weapon") {{
                shootCone = 360f;
                mirror = false;
                rotate = true;
                rotateSpeed = 100f;
                shootCone = 1f;
                reload = 30f;
            	shootY = 0f;
            	x = 0;
            	y = weapY;
    			shootSound = Sounds.none;
                autoTarget = true;
                predictTarget = false;
                controllable = false;
                shoot.firstShotDelay = 30f;
                
                bullet = new TeslaBulletType(){{
                	chains = 1;
    				maxRange = rang;
    				hitSize = 1f;
    				statusDuration = 5f;
    				status = StatusEffects.shocked;
    				hitColor = lightningColor = Pal.lancerLaser;
    				damage = (60f / 60f) * reload;
    				lightning = 0;
    				hitEffect = applyEffect = TektonFx.electricPulseBig;
    				hitSound = Sounds.shockBlast;
    				hitSoundVolume = 0.2f;
    				chainEffect = Fx.chainEmp.wrap(Pal.lancerLaser);
    			}};
            }});
		}};
        
		//core
		
		delta = new TektonUnitType("delta") {{
            this.constructor = UnitEntity::create;
            controller = u -> new BuilderAI(true, coreFleeRange);
            coreUnitDock = true;
            mineHardnessScaling = false;
			speed = 4f;
			mineTier = 1;
			mineSpeed = 3.5f;
            accel = 0.06f;
            drag = 0.05f;
            isEnemy = false;
            buildSpeed = 0.7f;
            flying = true;
            health = 120;
            armor = 0;
            engineOffset = 6f;
            hitSize = 7f;
            itemCapacity = 30;
            faceTarget = true;
            crashDamageMultiplier = 0;
            strafePenalty = 0.9f;
            engineOffset = 6f;
            lowAltitude = false;
            alwaysUnlocked = true;
            mineFloor = false;
            mineWalls = true;
            targetable = false;
            physics = false;
            hittable = false;

            fogRadius = 0;
            
            targetPriority = -2;
            
            immunities.addAll(TektonStatusEffects.wetInAcid, TektonStatusEffects.shortCircuit);
		}};
		
		kappa = new TektonUnitType("kappa") {{
            this.constructor = PayloadUnit::create;
            controller = u -> new BuilderAI(true, coreFleeRange);
            coreUnitDock = true;
            mineHardnessScaling = false;
			speed = 5f;
			mineTier = 1;
			mineSpeed = 4f;
            accel = 0.08f;
            drag = 0.05f;
            isEnemy = false;
            buildSpeed = 0.85f;
            flying = true;
            health = 200;
            armor = 1;
            engineOffset = 6f;
            engineSize = 4f;
            hitSize = 13f;
            itemCapacity = 60;
            faceTarget = true;
            crashDamageMultiplier = 0;
            strafePenalty = 0.9f;
            lowAltitude = false;
            alwaysUnlocked = true;
            mineFloor = false;
            mineWalls = true;

            fogRadius = 0;
            targetable = false;
            hittable = false;
            
            targetPriority = -2;
            
            payloadCapacity = 2f * 2f * tilesize * tilesize;
            pickupUnits = false;
            vulnerableWithPayloads = true;
            
            immunities.addAll(TektonStatusEffects.wetInAcid, TektonStatusEffects.shortCircuit);
		}};
		
		//payload
		
		assemblyDrone = new TektonUnitType("assembly-drone") {{
			this.constructor = BuildingTetherPayloadUnit::create;
            controller = u -> new AssemblerAI();

            flying = true;
            drag = 0.06f;
            accel = 0.11f;
            speed = 1.3f;
            health = 90;
            engineSize = 2f;
            engineOffset = 6.5f;
            payloadCapacity = 0f;
            targetable = false;
            bounded = false;

            outlineColor = TektonColor.tektonOutlineColor;
            isEnemy = false;
            hidden = true;
            useUnitCap = false;
            logicControllable = false;
            playerControllable = false;
            allowedInPayloads = false;
            createWreck = false;
            envEnabled = Env.any;
            envDisabled = Env.none;
        }};
		
		//tanks
		
		piezo = new TektonTankUnitType("piezo") {{
			this.constructor = TankUnit::create;
			fogRadiusMultipliyer = 0.7f;
			hitSize = 12f;
            treadPullOffset = 3;
            speed = 0.85f;
            rotateSpeed = 4f;
            health = 500;
            armor = 3f;
            itemCapacity = 0;
            treadRects = new Rect[]{new Rect(-24, -26, 15, 54)};
            researchCostMultiplier = 3f;
            
            weapons.add(new Weapon(name + "-weapon") {{
                //layerOffset = 0.0001f;
                reload = 90f;
                shootY = 4f;
                recoil = 1f;
                rotate = true;
                rotateSpeed = 2.4f;
                mirror = false;
                x = 0f;
                y = 0f;
                parentizeEffects = true;
                ejectEffect = Fx.none;
                shootSound = Sounds.lasershoot;
                inaccuracy = 2;
                //cooldownTime = 30f;
                
                shoot = new ShootPattern() {{
                	shots = 3;
                	shotDelay = 7f;
                }};

                bullet = new BasicBulletType(6f, 15) {{
                    sprite = "tekton-retangular-bullet";
                    smokeEffect = Fx.shootBigSmoke;
                    shootEffect = TektonFx.electricPulse;
                    width = 7f;
                    height = 14f;
                    lifetime = 17f;
                    hitSize = 3f;
                    hitColor = backColor = trailColor = Color.valueOf("4a9eff");
                    frontColor = Color.white.cpy();
                    lightRadius = 15f;
                    lightOpacity = 0.5f;
                    trailChance = -1;
                    trailWidth = 0.8f;
                    trailLength = 4;
                    despawnHit = true;
                    hitEffect = despawnEffect = TektonFx.electricPulse;
                    reflectable = false;
                }};
            }});
		}};
		
		electret = new TektonTankUnitType("electret") {{
			this.constructor = TankUnit::create;
			hitSize = 18f;
            treadPullOffset = 3;
            speed = 0.8f;
            rotateSpeed = 2.8f;
            health = 1200;
            armor = 7f;
            itemCapacity = 0;
            treadRects = new Rect[]{new Rect(-31, -38, 20, 76)};
            
            weapons.add(new Weapon(name + "-weapon") {{
                //layerOffset = 0.0001f;
                x = 0f;
                y = 0f;
                reload = 40f;
                shootY = 5f;
                recoil = 1.3f;
                rotate = true;
                rotateSpeed = 1.8f;
                mirror = false;
                parentizeEffects = true;
                ejectEffect = Fx.none;
                shootSound = TektonSounds.tchau;
                inaccuracy = 2;
                //cooldownTime = 30f;
                
                shoot = new ShootAlternate() {{
                	spread = 7;
                }};

                bullet = new BasicBulletType(4f, 10) {{
                    sprite = "large-orb";
                    smokeEffect = Fx.shootBigSmoke;
                    shootEffect = TektonFx.electricPulse;
                    width = 8.5f;
                    height = 8.5f;
                    lifetime = 24f;
                    hitSize = 4f;
                    hitColor = backColor = trailColor = Color.valueOf("4a9eff");
                    frontColor = Color.white.cpy();
                    lightRadius = 15f;
                    lightOpacity = 0.5f;
                    trailChance = -1;
                    trailWidth = 1.7f;
                    trailLength = 6;
                    despawnHit = true;
                    despawnEffect = hitEffect = TektonFx.electricPulseBig;
                    despawnSound = hitSound = Sounds.spark;
                    hitSoundPitch = 1.4f;
                    hitSoundVolume = 1.2f;
                    reflectable = false;
                    shrinkX = 0;
                    shrinkY = 0;
                    lightning = 6;
                    lightningLength = 6;
                    lightningLengthRand = 8;
                    lightningDamage = 10;
                    lightningColor = Color.valueOf("4a9eff");
                }};
            }});
            researchCostMultiplier = 0f;
		}};
		
		discharge = new TektonTankUnitType("discharge") {{
			this.constructor = TankUnit::create;
			fogRadiusMultipliyer = 0.76f;
            drawShields = false;
			hitSize = 26f;
            speed = 0.7f;
            rotateSpeed = 2.1f;
            health = 4000;
            armor = 10f;
            itemCapacity = 0;
            treadPullOffset = 0;
            abilities.add(new ForceFieldAbility(30f, 0.15f, 800f, 60f * 20, 4, 45f));
            
            treadRects = new Rect[]{
            		//i am starting to understand
            		new Rect(13f, -53f, 31, 44), // upper
            		new Rect(-13f, 12f, 29, 44) // down
            		};
            
            weapons.add(new Weapon(name + "-weapon") {{
                //layerOffset = 0.0001f;
                x = 0f;
                y = 0f;
                reload = 90f;
                shootY = 13f;
                recoil = 1.3f;
                rotate = true;
                rotateSpeed = 1.4f;
                mirror = false;
                parentizeEffects = true;
                ejectEffect = Fx.none;
                shootSound = Sounds.laser;
                inaccuracy = 0;
                heatColor = Color.valueOf("4a9eff");
                shake = 5f;
                //cooldownTime = 30f;
                shoot.firstShotDelay = 0f;
                bullet = new LaserBulletType(230) {{
                    lightColor = lightningColor = Pal.lancerLaser;
                    colors = new Color[]{Pal.lancerLaser.cpy().a(0.4f), Pal.lancerLaser, Color.white};
                    //chargeEffect = new MultiEffect(TektonFx.lancerLaserCharge, TektonFx.lancerLaserChargeBegin);
                    shootEffect = Fx.lancerLaserShoot;
                    chargeEffect = smokeEffect = Fx.none;
                    hitEffect = Fx.hitLancer;
                    hitSize = 4;
                    lifetime = 16f;
                    shake = 0.6f;
                    //drawSize = 400f;
                    collidesAir = true;
                    length = 173f;
                    pierceCap = 4;
                    status = StatusEffects.shocked;
                    statusDuration = 5f;
                    
                    sideAngle = 45f;
                    sideWidth = 2f;
                    sideLength = 80f;
                }};
            }});
            researchCostMultiplier = 0f;
		}};
		
		hysteresis = new TektonTankUnitType("hysteresis") {{
			this.constructor = TankUnit::create;
			hitSize = 28f;
            speed = 0.65f;
            rotateSpeed = 1.5f;
            health = 10000;
            armor = 18f;
            itemCapacity = 0;
            abilities.add(new ShieldArcAbility() {{
                region = "tekton-hysteresis-shield";
                radius = 36f;
                angle = 82f;
                regen = 0.6f;
                cooldown = 60f * 8f;
                max = 2000f;
                y = -18f;
                width = 5f;
                whenShooting = false;
            }});
            
            treadPullOffset = 0;
            treadRects = new Rect[]{new Rect(-57, -68, 31, 140)};
            crushDamage = 13f / 5f;
            
            weapons.add(new Weapon(name + "-weapon") {{
            	var offsetY = 14f;
            	parts.add(new RegionPart("-breech") {{
	            		mirror = true;
	            		under = false;
	                    x = y = 0f;
	                    var a = 1.5f;
	            		moveX = a * 1.5f;
	            		moveY = -a;
	            		heatProgress = PartProgress.warmup;
	                    heatColor = Color.valueOf("4a9eff");
                    }}, new HaloPart() {{
                    	color = Pal.lancerLaser;
                    	radius = 0f;
                    	radiusTo = 2f;
                    	mirror = false;
                    	x = 0f;
                    	y = offsetY;
                    	haloRotateSpeed = 2f;
                    	hollow = false;
                    	stroke = 0;
                    	strokeTo = 2f;
                    	sides = 3;
                    	layer = Layer.effect;
                    	progress = PartProgress.warmup;
                    }}, new ShapePart() {{
                    	color = Pal.lancerLaser;
                    	radius = 0f;
                    	radiusTo = 2.5f;
                    	mirror = false;
                    	x = 0f;
                    	y = offsetY;
                    	rotateSpeed = -2f;
                    	hollow = false;
                    	stroke = 0;
                    	strokeTo = 1.25f;
                    	sides = 16;
                    	layer = Layer.effect;
                    	progress = PartProgress.warmup;
                    }});
                top = true;
                
                shoot = new ShootHelix() {{
	                mag = 1f;
	                scl = 2.7f;
	                shots = 1;
            	}};
                
            	layerOffset = 0.01f;
                x = 0f;
                y = 0f;
                reload = 200f;
                shootY = offsetY;
                recoil = 1.3f;
                rotate = true;
                rotateSpeed = 1.4f;
                minWarmup = 0.9f;
                mirror = false;
                parentizeEffects = false;
                ejectEffect = Fx.none;
                shootSound = Sounds.malignShoot;
                soundPitchMin = 0.6f;
                soundPitchMax = 0.8f;
                inaccuracy = 0;
                heatColor = Color.valueOf("4a9eff");
                //cooldownTime = 30f;
                shake = 4f;
                shoot.firstShotDelay = 0f;
                bullet = new BasicBulletType(7f, 200f) {{
                	width = 17f;
                	height = 17f;
                    shrinkY = 0;
                	sprite = "large-orb";
                    chargeEffect = smokeEffect = Fx.none;
                    hitEffect = despawnEffect = Fx.hitLancer;
                    hitSize = 5;
                    lifetime = 30f;
                    hitShake = 5f;
                    collidesAir = true;
                    pierce = false;
                    pierceBuilding = false;
                    status = StatusEffects.shocked;
                    statusDuration = 5f;
                    despawnSound = hitSound = Sounds.plasmaboom;
                    hitColor = backColor = trailColor = Pal.lancerLaser;
                    frontColor = Color.white;
                    /*trailEffect = new Effect(50, e -> {
                        color(Pal.lancerLaser);
                        Fill.circle(e.x, e.y, e.rotation * e.fout());
                    }).layer(Layer.bullet - 0.001f);*/
                    trailChance = -1;
                    trailLength = 40;
                    trailWidth = 4f;
                    shootEffect = new ExplosionEffect() {{
                        lifetime = 40f;
                        waveStroke = 4f;
                        waveColor = sparkColor = smokeColor = Pal.lancerLaser;
                        waveRad = 15f;
                        smokeSize = 5f;
                        smokes = 8;
                        smokeSizeBase = 0f;
                        sparks = 8;
                        sparkRad = 40f;
                        sparkLen = 4f;
                        sparkStroke = 3f;
                    }};
                    fragBullet = new TektonEmpBulletType() {{
                        float rad = 80f;
                        radius = rad;
                        scaleLife = false;
                        lightOpacity = 0.7f;
                        unitDamageScl = 0.8f;
                        timeIncrease = 3f;
                        timeDuration = 60f * 20f;
                        powerDamageScl = 3f;
                        damage = 60;
                        hitColor = lightColor = Pal.lancerLaser;
                        lightRadius = 70f;
                        clipSize = 250f;
                        shootEffect = hitPowerEffect;
                        hitPowerEffect.wrap(Pal.lancerLaser.cpy());
                        applyEffect.wrap(Pal.lancerLaser.cpy());
                        smokeEffect = new Effect(40, e -> {
                            color(Pal.lancerLaser);
                            stroke(e.fout() * 1.6f);

                            randLenVectors(e.id, 18, e.finpow() * 27f, e.rotation, 360f, (x, y) -> {
                                float ang = Mathf.angle(x, y);
                                lineAngle(e.x + x, e.y + y, ang, e.fout() * 6 + 1f);
                            });
                        });
                        lifetime = 1f;
                        sprite = "circle-bullet";
                        backColor = Pal.lancerLaser;
                        frontColor = Color.white;
                        width = height = 12f;
                        shrinkY = 0f;
                        speed = 0f;
                        trailColor = Pal.lancerLaser;
                        trailInterval = 3f;
                        splashDamage = 70f;
                        splashDamageRadius = rad;
                        hitShake = 4f;
                        trailRotation = true;
                        status = StatusEffects.shocked;
                        statusDuration = 5f;
                        hitSound = Sounds.plasmaboom;

                        trailEffect = new Effect(16f, e -> {
                            color(Pal.lancerLaser);
                            for(int s : Mathf.signs){
                                Drawf.tri(e.x, e.y, 4f, 30f * e.fslope(), e.rotation + 90f*s);
                            }
                        });

                        hitEffect = new Effect(50f, 100f, e -> {
                            e.scaled(7f, b -> {
                                color(Pal.lancerLaser, b.fout());
                                Fill.circle(e.x, e.y, rad);
                            });

                            color(Pal.lancerLaser);
                            stroke(e.fout() * 3f);
                            Lines.circle(e.x, e.y, rad);

                            int points = 8;
                            float offset = Mathf.randomSeed(e.id, 360f);
                            for(int i = 0; i < points; i++){
                                float angle = i* 360f / points + offset;
                                //for(int s : Mathf.zeroOne){
                                    Drawf.tri(e.x + Angles.trnsx(angle, rad), e.y + Angles.trnsy(angle, rad), 6f, 50f * e.fout(), angle/* + s*180f*/);
                                //}
                            }

                            Fill.circle(e.x, e.y, 12f * e.fout());
                            color();
                            Fill.circle(e.x, e.y, 6f * e.fout());
                            Drawf.light(e.x, e.y, rad * 1.6f, Pal.lancerLaser, e.fout());
                        });
                    }};
                    fragBullets = 1;
                    fragVelocityMin = 0f;
                    fragVelocityMax = 0f;
                    fragRandomSpread = 0f;
                }};
            }});
            researchCostMultiplier = 0f;
		}};

		supernova = new TektonTankUnitType("supernova") {{
			this.constructor = TankUnit::create;
			fogRadiusMultipliyer = 0.7f;
			hitSize = 44f;
            speed = 0.54f;
            rotateSpeed = 1.1f;
            health = 21000;
            armor = 24f;
            itemCapacity = 0;
            treadPullOffset = 0;
            float xo = 214f/2f, yo = 214f/2f;
            treadRects = new Rect[]{new Rect(18 - xo, 8 - yo, 56, 198)};
            crushDamage = 20f / 5f;
            rotateToBuilding = true;
            faceTarget = true;
            aimDst = 1f;
            
            var weapPos = -9f;
            
            weapons.add(new Weapon(name + "-weapon") {{
            	shootSound = Sounds.laserblast;
                chargeSound = Sounds.lasercharge;
                soundPitchMin = 1f;
                top = false;
                mirror = false;
                shake = 14f;
                shootY = weapPos;
                x = y = 0;
                reload = 350f;
                recoil = 0f;
                rotateSpeed = 0;
                inaccuracy = 0f;
                rotate = false;
                heatColor = Color.valueOf("4a9eff");
                shootCone = 7f;
                autoTarget = true;
                
                cooldownTime = 350f;

                shootStatusDuration = 60f * 2f;
                shootStatus = StatusEffects.unmoving;
                shoot.firstShotDelay = TektonFx.electricLaserCharge.lifetime;
                parentizeEffects = true;

                bullet = new LaserBulletType(){{
                    length = 330f;
                    damage = 1400f;
                    width = 75f;

                    lifetime = 65f;

                    lightningSpacing = 35f;
                    lightningLength = 5;
                    lightningDelay = 1.1f;
                    lightningLengthRand = 15;
                    lightningDamage = 50;
                    lightningAngleRand = 40f;
                    largeHit = true;
                    lightColor = lightningColor = Pal.lancerLaser;

                    chargeEffect = TektonFx.electricLaserCharge;

                    sideAngle = 15f;
                    sideWidth = 0f;
                    sideLength = 0f;
                    colors = new Color[]{Pal.lancerLaser.cpy().a(0.4f), Pal.lancerLaser, Color.white};
                }};
            }});
            
            weapons.add(new MinionWeapon(name + "-spawner") {{
            	parts.add(new RegionPart("-breech") {{
            		mirror = true;
            		under = false;
                    x = y = 0f;
                    var a = 1.5f;
            		moveX = a;
            		moveY = -a;
            		heatProgress = PartProgress.warmup;
                    heatColor = Color.valueOf("4a9eff");
                    }});
            	
            	layerOffset = 0.01f;
            	
            	x = 16f;
            	y = -1f;
            	minWarmup = 0.95f;
                reload = 20f;
                cooldownTime = 42f;
                heatColor = Pal.turretHeat;
                ejectEffect = Fx.none;
                recoil = 1f;
                shootCone = 90f;
                
                shootSound = Sounds.missile;
                mirror = true;
                top = true;
                rotate = true;
                rotateSpeed = 3.5f;
                
                autoTarget = true;
                predictTarget = false;
                controllable = false;
    			//range = 40f * Vars.tilesize;;
                
            	bullet = new MinionBullet() {{
            		range = 40f * Vars.tilesize;;
                	shootEffect = new MultiEffect(Fx.shootBigColor, new Effect(9, e -> {
                        color(Color.white, e.color, e.fin());
                        stroke(0.7f + e.fout());
                        Lines.square(e.x, e.y, e.fin() * 5f, e.rotation + 45f);

                        Drawf.light(e.x, e.y, 23f, e.color, e.fout() * 0.7f);
                    }), new WaveEffect() {{
                        colorFrom = colorTo = Pal.lancerLaser;
                        sizeTo = 15f;
                        lifetime = 12f;
                        strokeFrom = 3f;
                    }});
                    smokeEffect = Fx.none;
                    shake = 1f;
                    speed = 0f;
                    keepVelocity = false;
                    collidesGround = collidesAir = true;
                    spawnUnit = electron;
                }};
            }});
            
            parts.add(new HaloPart() {{
            	color = Pal.lancerLaser;
            	radius = 3.5f;
            	mirror = false;
            	x = 0f;
            	y = weapPos;
            	haloRotateSpeed = 2f;
            	hollow = false;
            	stroke = 1.5f;
            	sides = 3;
            	layer = Layer.effect;
            }}, new ShapePart() {{
            	color = Pal.lancerLaser;
            	radius = 3f;
            	mirror = false;
            	x = 0f;
            	y = weapPos;
            	rotateSpeed = -2f;
            	hollow = false;
            	stroke = 1.5f;
            	sides = 4;
            	layer = Layer.effect;
            }}, new ShapePart() {{
            	color = Pal.lancerLaser;
            	radius = 6f;
            	mirror = false;
            	x = 0f;
            	y = weapPos;
            	rotateSpeed = 2f;
            	hollow = true;
            	stroke = 1.5f;
            	sides = 12;
            	layer = Layer.effect;
            }});
            
            researchCostMultiplier = 0f;
		}};
		
		//air
		
		martyris = new TektonUnitType("martyris") {{
            this.constructor = UnitEntity::create;
            fogRadiusMultipliyer = 0.6f;
			createWreck = false;
			speed = 3.5f;
            accel = 0.06f;
            drag = 0.05f;
            flying = true;
            health = 20;
            engineOffset = 5.75f;
            targetFlags = new BlockFlag[]{BlockFlag.generator, null};
            hitSize = 9;
            itemCapacity = 0;
            //armor = 0;
            faceTarget = true;
            targetPriority = 0;
            crashDamageMultiplier = 0;
            strafePenalty = 0.9f;
            engineOffset = 6f;
            lowAltitude = false;
            parts.add(defaultHoverPart(0, -0.1f, false, 10, 1.5f, 80f));
            abilities.add(wavePart(0, -0.1f, false, 10, 1.5f, 80f / 3f, 4f));
            weapons.add(new Weapon() {{
            	shootCone = 180;
            	shootOnDeath = true;
            	x = 0;
            	shootY = 0;
            	layerOffset = -1;
            	reload = 24;
            	rotate = false;
            	inaccuracy = 0;
            	mirror = false;
            	shootSound = Sounds.explosion;
            	shoot.firstShotDelay = 10f;
                ejectEffect = new MultiEffect(new ParticleEffect() {{
                	particles = 10;
                	line = true;
                	lifetime = 7;
                	length = 25;
                	lenFrom = 5;
                	lenTo = 0;
                	strokeFrom = 3;
                	strokeTo = 0;
                	colorFrom = colorTo = Pal.suppress;
                }}, new WaveEffect() {{
                	sizeFrom = 4;
                	sizeTo = 45;
                	lifetime = 16;
                	strokeFrom = 5;
                	strokeTo = 1;
                	colorFrom = Color.white.cpy();
                	colorTo = Pal.suppress;
                }}, new WaveEffect() {{
                	sizeFrom = 8;
                	sizeTo = 60;
                	lifetime = 20;
                	strokeFrom = 5;
                	strokeTo = 1;
                	colorFrom = Color.white.cpy();
                	colorTo = Pal.suppress;
                }});
                bullet = new BasicBulletType(0, 0) {{
                	width = 2;
                	height = 7;
                	collidesTiles = false;
                	collides = false;
                	hitSound = Sounds.explosion;
                	pierceBuilding = false;
                	rangeOverride = 30;
                	trailChance = -1;
                	despawnHit = true;
                	despawnEffect = Fx.none;
                	hitColor = Pal.suppress;
                	hitEffect = Fx.pulverize;
                	splashDamageRadius = 65;
                	instantDisappear = true;
                	splashDamage = 50;
                	killShooter = true;
                	hittable = false;
                	collidesAir = true;
                	scaledSplashDamage = true;
                }};
            }});
		}};
		
		bellator = new TektonUnitType("bellator") {{
            this.constructor = UnitEntity::create;
            fogRadiusMultipliyer = 0.7f;
            aiController = FlyingAI::new;
			lowAltitude = false;
			hitSize = 10;
			speed = 2.2f;
			accel = 0.06f;
			drag = 0.05f;
			flying = true;
			health = 600;
			rotateSpeed = 4.5f;
			targetAir = true;
			armor = 1;
			itemCapacity = 0;
			engineOffset = 8.5f;
			faceTarget = true;
			targetPriority = 0;
			crashDamageMultiplier = 0.7f;
			strafePenalty = 0.9f;
            parts.add(defaultHoverPart(6.65f, -0.7f, true, 10, 1.5f, 120f));
            abilities.add(wavePart(6.65f, -0.7f, true, 10, 1.5f, 120f / 3f, 4f));
            weapons.add(new Weapon(name + "-weapon") {{
            	x = 0;
            	y = 0;
            	shootY = 4;
            	inaccuracy = 2;
            	reload = 22;
            	recoil = 1;
            	mirror = false;
            	rotate = true;
            	parentizeEffects = true;
            	shootSound = Sounds.lasershoot;
            	rotationLimit = 25;
            	rotateSpeed = 10;
            	layerOffset = -0.001f;
            	top = false;
            	bullet = new LiquidBulletType(TektonLiquids.acid) {{
                    damage = 13;
                    speed = 2.5f;
                    drag = 0.009f;
                    shootEffect = Fx.shootSmall;
                    lifetime = 57f;
                    collidesAir = false;
                }};
            	bullet = new LaserBulletType(20) {{
            		colors = new Color[] {Pal.suppress};
            		pierce= true;
            		pierceBuilding = false;
            		pierceCap = 2;
            		splashDamagePierce = false;
            		reflectable = false;
            		width = 5.7f;
            		length = 160;
            		lightColor = hitColor = Pal.suppress;
            		hitEffect = Fx.hitBulletColor;
            		smokeEffect = Fx.colorSpark;
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
                    	hitColor = colorTo = Pal.suppress;
            		}}, new WaveEffect() {{
                    	sizeFrom = 0;
                    	sizeTo = 7;
                    	lifetime = 10;
                    	strokeFrom = 1.5f;
                    	strokeTo = 0;
                    	colorFrom = Color.white;
                    	hitColor = colorTo = Pal.suppress;
            		}});
            		despawnEffect = Fx.none;
            	}};
            }});
		}};
		
		eques = new TektonUnitType("eques") {{
            this.constructor = UnitEntity::create;
            fogRadiusMultipliyer = 0.65f;
            aiController = FlyingFollowAI::new;
            lowAltitude = false;
			hitSize = 19;
			speed = 1.6f;
			accel = 0.04f;
			drag = 0.05f;
			flying = true;
			health = 1500;
			rotateSpeed = 2.5f;
			targetAir = false;
			armor = 3;
			itemCapacity = 0;
			engineOffset = 14f;
			faceTarget = true;
			targetPriority = 0;
			fogRadius = 22;
			crashDamageMultiplier = 1f;
			strafePenalty = 0.8f;
            parts.add(defaultHoverPart(6.6f, 5.25f, true, 10, 1.5f, 150f));
            abilities.add(wavePart(6.6f, 5.25f, true, 10, 1.5f, 150f / 3f, 6f));
            parts.add(defaultHoverPart(6.65f, -5.65f, true, 12, 1.5f, 150f));
            abilities.add(wavePart(6.6f, -5.65f, true, 12, 1.5f, 150f / 3f, 6f));
            weapons.add(new Weapon(name + "-weapon") {{
            	heatColor = Pal.turretHeat;
            	x = 0;
            	y = 8.5f;
            	shootY = 4;
            	inaccuracy = 0;
            	reload = 100;
            	recoil = 3;
            	mirror = false;
            	rotate = false;
            	rotationLimit = 0;
            	rotateSpeed = 0;
            	parentizeEffects = true;
            	shootSound = Sounds.missileSmall;
                heatColor = Pal.turretHeat;
            	layerOffset = -0.001f;
            	top = false;
            	bullet = new BulletType() {{
                    shootEffect = Fx.shootBig;
                    smokeEffect = Fx.shootBigSmoke2;
                    shake = 1f;
                    speed = 0f;
                    keepVelocity = false;
                    collidesAir = false;
                    spawnUnit = new TektonMissileUnitType("eques-missile") {{
                        targetAir = false;
                        speed = 3.7f;
                        maxRange = 5f;
                        lifetime = 60f * 1f;
                        outlineColor = TektonColor.tektonOutlineColor.cpy();
                        deathExplosionEffect = Fx.none;
                        engineColor = trailColor = Pal.sapBulletBack;
                        engineLayer = Layer.effect;
                        health = 55;
                        loopSoundVolume = 0.1f;
                        weapons.add(new Weapon() {{
                            shootCone = 360f;
                            mirror = false;
                            x = y = shootY = 0;
                            reload = 1f;
                            shootOnDeath = true;
                            bullet = new ExplosionBulletType(90f, 20f) {{
                            	hitShake = 3f;
                                shootEffect = new MultiEffect(new ParticleEffect() {{
                                	particles = 10;
                                	line = true;
                                	lifetime = 7;
                                	length = 25;
                                	lenFrom = 5;
                                	lenTo = 0;
                                	strokeFrom = 3;
                                	strokeTo = 0;
                                	colorFrom = Pal.suppress;
                                	colorTo = Pal.suppress;
                            		layer = Layer.flyingUnit + 1f;
                                }}, new WaveEffect() {{
                                	sizeFrom = 4;
                                	sizeTo = 14;
                                	lifetime = 16;
                                	strokeFrom = 5;
                                	strokeTo = 1;
                                	colorFrom = Color.white;
                                	colorTo = Pal.suppress;
                            		layer = Layer.flyingUnit + 1f;
                                }}, new WaveEffect() {{
                                	sizeFrom = 8;
                                	sizeTo = 36;
                                	lifetime = 20;
                                	strokeFrom = 5;
                                	strokeTo = 1;
                                	colorFrom = Color.white;
                                	colorTo = Pal.suppress;
                                }});
                                collidesAir = false;
                            }};
                        }});
                    }};
                }};
            }});
            weapons.add(new Weapon("tekton-dagger-generic") {{
            	x = 6.15f;
            	y = 0;
            	shootY = 2;
            	inaccuracy = 0;
            	reload = 14;
            	recoil = 1.2f;
            	mirror = true;
            	rotate = true;
            	rotationLimit = 270;
            	rotateSpeed = 7f;
            	parentizeEffects = true;
            	shootSound = Sounds.lasershoot;
            	layerOffset = 0.001f;
            	top = true;
                controllable = false;
                autoTarget = true;
            	bullet = new LaserBulletType(16) {{
            		colors = new Color[] {Pal.suppress};
            		pierce = true;
            		pierceBuilding = false;
            		pierceCap = 2;
            		splashDamagePierce = false;
            		reflectable = false;
            		width = 5.7f;
            		length = 160;
            		
            		lightColor = hitColor = Pal.suppress;
            		hitEffect = Fx.hitBulletColor;
            		smokeEffect = Fx.colorSpark;
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
                    	colorTo = Pal.suppress;
                		layer = Layer.flyingUnit + 1f;
            		}}, new WaveEffect() {{
                    	sizeFrom = 0;
                    	sizeTo = 7;
                    	lifetime = 10;
                    	strokeFrom = 1.5f;
                    	strokeTo = 0;
                    	colorFrom = Color.white;
                    	colorTo = Pal.suppress;
                		layer = Layer.flyingUnit + 1f;
            		}});
            		despawnEffect = Fx.none;
            	}};
            }});
            researchCostMultiplier = 0f;
		}};
		
		phalanx = new TektonUnitType("phalanx") {{
			var rang = 120f;

	        ammoType = new PowerAmmoType();
	        
            immunities.addAll(StatusEffects.burning, StatusEffects.melting);
			
            this.constructor = UnitEntity::create;
            targetAir = false;
            targetFlags = new BlockFlag[]{BlockFlag.battery, BlockFlag.factory, null};
            circleTarget = true;
            rotateSpeed = 2.5f;
			targetPriority = 1;
			strafePenalty = 0.5f;
			speed = 1.2f;
            lowAltitude = false;
			hitSize = 45;
			accel = 0.035f;
			drag = 0.06f;
			flying = true;
			health = 8000;
			rotateSpeed = 2.5f;
			armor = 7;
			itemCapacity = 0;
			faceTarget = false;
			customFogRadius = true;
			fogRadius = rang / 8f;
			crashDamageMultiplier = 1.6f;
            engineSize = 4.8f;
            engineOffset = 72 / 3f;
			abilities.add(new SuppressionFieldAbility() {{
                orbRadius = 9.5f;
                y = -3.4f;
            }});
            abilities.add(wavePart(0, -3.4f, false, 9.5f * 2.2f, 3f, 120f / 2f, 10f, 9.5f));
            weapons.add(new Weapon(name + "-weapon") {{
            	showStatSprite = false;
            	display = true;
                mirror = false;
                top = true;
                shake = 4f;
                shootY = -3.4f;
                x = y = 0f;
                rotate = false;
                ignoreRotation = true;
                shootCone = 180f;
                maxRange = 60f;

                shoot.firstShotDelay = Fx.greenLaserChargeSmall.lifetime - 1f;
                parentizeEffects = true;

                reload = 155f;
                recoil = 0f;
                chargeSound = Sounds.lasercharge2;
                shootSound = Sounds.beam;
                continuous = true;
                cooldownTime = 200f;

                bullet = new ContinuousFlameBulletType() {{
                    collides = false;
                    collidesAir = false;
                    damage = 0f;
                    length = 0f;
                    hitEffect = new Effect(12, e -> {
                        color(Pal.sapBullet);
                        stroke(e.fout() * 2f);

                        randLenVectors(e.id, 6, e.finpow() * 18f, (x, y) -> {
                            float ang = Mathf.angle(x, y);
                            lineAngle(e.x + x, e.y + y, ang, e.fout() * 4 + 1f);
                        });
                    });
                    drawSize = 0f;
                    lifetime = 160f;
                    shake = 1f;
                    shootEffect = TektonFx.phalanxSmoke;
                    smokeEffect = Fx.none;
                    despawnEffect = Fx.titanExplosion.wrap(Pal.sapBullet);
                    chargeEffect = new Effect(40f, 100f, e -> {
                        color(Pal.sapBullet);
                        stroke(e.fin() * 2f);
                        Lines.circle(e.x, e.y, e.fout() * 50f);
                    }).followParent(true).rotWithParent(true);
                    
                    flareLayer = Layer.flyingUnit + 0.001f;
                    colors = new Color[]{Pal.sapBullet.cpy().a(.2f), Pal.sapBullet.cpy().a(.5f), Pal.sapBullet.cpy().mul(1.2f), Color.white};
                    flareWidth = 5f;
                    flareLength = 70f;
                    flareColor = Pal.sapBullet.cpy().mul(1.15f);

                    pierce = false;
                    pierceBuilding = false;
                    pierceArmor = false;
                    pierceCap = -1;
                    
                    status = StatusEffects.melting;
                    statusDuration = 200f;

                    heatColor = lightColor = hitColor = flareColor;
                    
                    var div = 2.5f;
                    intervalBullet = new BasicBulletType(0, 1f) {{
                    	sprite = "large-bomb";
                        collides = false;
                        collidesAir = false;
                    	lifetime = 1f;
                    	instantDisappear = true;
                    	reflectable = false;
                    	hittable = false;
                    	width = height = 50f;
                        shrinkX = shrinkY = 0.1f;
                    	frontColor = backColor = Pal.sapBullet;
                        splashDamage = 200f / div;
                        buildingDamageMultiplier = 0.5f;
                        splashDamageRadius = rang / 2f;
                        splashDamagePierce = true;
                        scaledSplashDamage = true;
                        pierce = true;
                        pierceBuilding = true;
                        pierceArmor = true;
                        pierceCap = -1;
                        despawnEffect = hitEffect = new MultiEffect(new Effect(30f, 160f, e -> {
                            color(Pal.sapBullet);
                            Fx.rand.setSeed(e.id);
                            for(int i = 0; i < 16; i++){
                                float angle = Fx.rand.random(360f);
                                float lenRand = Fx.rand.random(0.5f, 1f);
                                Lines.lineAngle(e.x, e.y, angle, e.foutpow() * 50f * Fx.rand.random(1f, 0.6f) + 2f, e.finpow() * 70f * lenRand + 6f);
                            }
                        }), new Effect(400f, e -> {
                            float margin = 1f - Mathf.curve(e.fout(), 0.9f);
                            float fin = Math.min(margin, e.fout());

                            color(Pal.sapBullet);
                            Fill.circle(e.x, e.y, fin * 16f);
                        }));
                        shootEffect = smokeEffect = chargeEffect = Fx.none;
                        drawSize = 0f;
                        lifetime = 160f;

                        makeFire = true;
                        incendChance = 0.1f;
                        incendSpread = 5f;
                        incendAmount = 1;
                        status = StatusEffects.melting;
                        statusDuration = 200f;
                    }};
                    bulletInterval = 60f / div;
                    intervalBullets = 1;
                }};

                shootStatus = TektonStatusEffects.incineration;
                shootStatusDuration = bullet.lifetime + shoot.firstShotDelay;
            }});
            researchCostMultiplier = 0f;
		}};
		
		imperatoris = new TektonUnitType("imperatoris") {{
			outlineColor = TektonColor.tektonOutlineColor;
	        envDisabled = Env.none;
	        ammoType = new PowerAmmoType();
	        fogRadiusMultipliyer = 0.55f;
	        
            immunities.add(StatusEffects.burning);
            immunities.add(StatusEffects.melting);
			
            this.constructor = UnitEntity::create;
            targetAir = true;
            targetFlags = new BlockFlag[]{BlockFlag.reactor, BlockFlag.battery, BlockFlag.core, null};
            rotateSpeed = 2.5f;
			strafePenalty = 0.5f;
			speed = 1.2f;
            lowAltitude = false;
			hitSize = 50;
			accel = 0.026f;
			drag = 0.06f;
			flying = true;
			health = 16000;
			rotateSpeed = 1.8f;
			armor = 14;
			itemCapacity = 0;
			faceTarget = true;
			crashDamageMultiplier = 2f;
            engineSize = 5.6f;
            engineOffset = 62 / 3f;
            var xPos = 19f;
            var yPos = 12.5f;
            aimDst = xPos + 20f;
            range = xPos + 100f;
            
            for (float i : Mathf.signs) {
            	abilities.add(new SuppressionFieldAbility() {{
                    orbRadius = 9.5f;
                    range = 320f;
                    x = xPos * i;
                    y = yPos;
                }});
                abilities.add(wavePart(xPos * i, yPos, false, 9.5f * 2.2f, 3f, 120f / 2f, 10f, 9.5f));
            }
            ((SuppressionFieldAbility)abilities.get(0)).active = false;
            abilities.get(0).display = false;
            
            var bulletImperatoris = new ContinuousFlameBulletType() {{
            	//layer = Layer.flyingUnit + 0.1f;
                damage = 400f / 12f;
                length = 300f;
                drawSize = 0f;
                lifetime = 200f;
                continuous = true;
                width = 10f;
                shake = 1f;
                shootEffect = smokeEffect = hitEffect = despawnEffect = Fx.none;
                chargeEffect = new Effect(40f, 100f, e -> {
                    color(Pal.sapBullet);
                    stroke(e.fin() * 2f);
                    Lines.circle(e.x, e.y, e.fout() * 50f);
                }).followParent(true).rotWithParent(true);
                chargeEffect.layer = Layer.flyingUnit;
                
                flareLayer = Layer.flyingUnit + 1f;
                colors = new Color[]{Pal.sapBullet.cpy().a(.2f), Pal.sapBullet.cpy().a(.5f), Pal.sapBullet.cpy().mul(1.2f), Color.white};
                flareWidth = 5f;
                flareLength = 70f;
                hitColor = flareColor = Pal.sapBullet.cpy().mul(1.15f);

                pierce = true;
                pierceBuilding = true;
                pierceArmor = true;
                pierceCap = 2;
                //pierceDamageFactor = 0.15f;
                incendChance = 0.05f;
                incendAmount = 2;
                incendSpread = 3f;
                
                status = StatusEffects.melting;
                statusDuration = 130f;
            }};
            
            var imperatorisWeapon = new Weapon(name + "-weapon") {{
            	x = xPos - 6;
            	y = yPos + 19f;
                mirror = false;
                top = false;
                layerOffset = -0.0001f;
                shake = 4f;
                shootY = 1f;
                rotate = true;
                shootCone = 15f;
                rotationLimit = 60f;
                alternate = false;
                rotateSpeed = 3f;

                shoot.firstShotDelay = Fx.greenLaserChargeSmall.lifetime - 1f;
                parentizeEffects = true;

                reload = 40f;
                recoil = 2f;
                chargeSound = Sounds.lasercharge2;
                shootSound = Sounds.beam;
                continuous = true;
                cooldownTime = 200f;
                
                parts.add(new RegionPart("-piece") {{
            		mirror = true;
            		under = false;
                    x = y = 0f;
                    var a = 2f;
            		moveX = a;
            		moveY = -a;
            		progress = PartProgress.warmup;
                    heatColor = Pal.sapBullet;
                    outlineLayerOffset = -0.0001f;
                    }});

                bullet = bulletImperatoris;

                heatColor = lightColor = Pal.sapBullet.cpy().mul(1.15f);
            }};
            
            weapons.add(imperatorisWeapon.copy(), new Weapon(name + "-weapon") {{
            	x = 0f;
            	y = yPos + 14f;
                mirror = false;
                top = true;
                shake = imperatorisWeapon.shake;
                shootY = imperatorisWeapon.shootY;
                rotate = imperatorisWeapon.rotate;
                shootCone = imperatorisWeapon.shootCone;
                rotationLimit = imperatorisWeapon.rotationLimit;
                alternate = false;
                rotateSpeed = imperatorisWeapon.rotateSpeed;

                shoot.firstShotDelay = Fx.greenLaserChargeSmall.lifetime - 1f;
                parentizeEffects = imperatorisWeapon.parentizeEffects;

                reload = imperatorisWeapon.reload;
                recoil = imperatorisWeapon.recoil;
                chargeSound = imperatorisWeapon.chargeSound;
                shootSound = imperatorisWeapon.shootSound;
                continuous = imperatorisWeapon.continuous;
                cooldownTime = imperatorisWeapon.cooldownTime;
                layerOffset = 0.01f;
                
                parts.add(imperatorisWeapon.parts);

                bullet = bulletImperatoris;

                heatColor = lightColor = imperatorisWeapon.heatColor;
            }}, imperatorisWeapon);
            weapons.get(0).x = -imperatorisWeapon.x;
            
            researchCostMultiplier = 0f;
		}};
		
		//naval
		
		caravela = new TektonUnitType("caravela") {{
            this.constructor = UnitWaterMove::create;
            fogRadiusMultipliyer = 0.8f;
            speed = 1.2f;
            drag = 0.13f;
            hitSize = 10f;
            health = 550;
            accel = 0.4f;
            rotateSpeed = 3.3f;
            faceTarget = false;

            armor = 2f;
            immunities.add(TektonStatusEffects.tarredInMethane);

            weapons.add(new Weapon("tekton-dagger-generic") {{
                reload = 35f;
                x = 3.95f;
                shootY = 3f;
                y = -2.05f;
                rotate = true;
                ejectEffect = Fx.casing1;
            	inaccuracy = 3;
                shoot = new ShootPattern() {{
                	shots = 3;
                	shotDelay = 3;
                }};
                bullet = new BasicBulletType(2.5f, 9) {{
                    width = 7f;
                    height = 9f;
                    lifetime = 45f;
                    ammoMultiplier = 2;
                }};
            }});
        }};
        
        sagres = new TektonUnitType("sagres") {{
            this.constructor = UnitWaterMove::create;
            fogRadiusMultipliyer = 0.8f;
            speed = 1.05f;
            drag = 0.13f;
            hitSize = 13f;
            health = 1300;
            accel = 0.37f;
            rotateSpeed = 2.5f;
            faceTarget = false;

            armor = 4f;
            immunities.add(TektonStatusEffects.tarredInMethane);

            weapons.add(new Weapon("tekton-dagger-generic") {{
                reload = 15f;
                x = 4.5f;
                shootY = 3f;
                y = -3f;
                rotate = true;
                ejectEffect = Fx.casing1;
                controllable = false;
                autoTarget = true;
            	inaccuracy = 3;
                bullet = new BasicBulletType(3.5f, 3) {{
                    lifetime = 40f;
                    ammoMultiplier = 4f;
                    width = 5.25f;
                    height = 7f;
                }};
            }});
            
            weapons.add(new Weapon("tekton-mount-weapon") {{
                reload = 65f;
                x = 4.85f;
                shootY = 3.2f;
                y = 5.95f;
                rotate = true;
                ejectEffect = Fx.casing2;
                shootSound = Sounds.shoot;
            	rotateSpeed = 6f;
            	inaccuracy = 6;
                shoot = new ShootPattern() {{
                	shots = 3;
                	shotDelay = 6;
                }};
                bullet = new BasicBulletType(4f, 3) {{
                    lifetime = 33f;
                    ammoMultiplier = 2f;
                    shootEffect = Fx.shootSmall;
                    width = 6.25f;
                    height = 8.5f;
                    hitEffect = Fx.flakExplosion;
                    splashDamage = 25;
                    splashDamageRadius = 25f;
                }};
            }});
        }};
        
        argos = new TektonUnitType("argos") {{
            this.constructor = UnitWaterMove::create;
            speed = 0.94f;
            drag = 0.13f;
            hitSize = 20f;
            health = 5000;
            accel = 0.33f;
            rotateSpeed = 2f;
            faceTarget = false;
            fogRadiusMultipliyer = 0.65f;

            armor = 7f;
            immunities.add(TektonStatusEffects.tarredInMethane);

            weapons.add(new Weapon("tekton-mount-weapon") {{
                reload = 40f;
                x = -7f;
                shootY = 3.2f;
                y = -9f;
                rotate = false;
                ignoreRotation = true;
                shootCone = 360f;
                baseRotation = 180;
                ejectEffect = Fx.casing2;
                shootSound = Sounds.missile;
            	inaccuracy = 4;
                controllable = false;
                autoTarget = true;
                
                shoot = new ShootPattern() {{
                	shots = 4;
                	shotDelay = 8;
                }};
                
                bullet = new MissileBulletType(4f, 1) {{
                    lifetime = 60f;
                    homingPower = 0.2f;
                    ammoMultiplier = 2f;
                    shootEffect = Fx.shootSmall;
                    width = 6.25f;
                    height = 8.5f;
                    hitEffect = Fx.flakExplosion;
                    splashDamage = 20;
                    splashDamageRadius = 24f;
                    range = 40f;
                }};
            }});
            
            weapons.add(new Weapon(name + "-weapon") {{
            	parts.addAll(
	    			new RegionPart() {{
	            		suffix = "-cannons-out";
	            		under = true;
	            		//layerOffset = -0.00001f;
	            	}},
	    			new FramePart() {{
	            		suffix = "-cannons";
	            		frames = 12;
	            		interval = 0.57f;
	            		under = true;
	            		//outlineRegionSuffix = "-out";
	            		//layerOffset = -0.00001f;
	            	}});
            	mirror = false;
                reload = 4f;
                x = y = 0f;
                shootY = 8.5f;
                rotate = true;
                ejectEffect = Fx.casing1;
                shootSound = Sounds.shoot;
            	soundPitchMin = 1.3f;
            	soundPitchMax = 1.5f;
                ignoreRotation = true;
                shootCone = 360f;
                shootWarmupSpeed = 0.025f;
            	rotateSpeed = 6f;
            	inaccuracy = 6;
            	recoil = 0;
                shoot = new ShootPattern() {{
                	//shots = 25;
                	firstShotDelay = 50f;
                	shotDelay = 0f;
                	//name = "tekton-argos-weapon-cannons";
                }};
                
                bullet = new BasicBulletType(8f, 1) {{
                    lifetime = 28f;
                    ammoMultiplier = 10f;
                    shootEffect = Fx.shootSmall;
                    width = 6f;
                    height = 8f;
                    //hitEffect = Fx.flakExplosion;
                    splashDamage = 12;
                    splashDamageRadius = 9f;
                }};
            }});
            researchCostMultiplier = 0f;
        }};
        
        ariete = new TektonUnitType("ariete") {{
            this.constructor = UnitWaterMove::create;
            fogRadiusMultipliyer = 0.8f;
            speed = 0.75f;
            drag = 0.15f;
            hitSize = 36f;
            health = 12000;
            accel = 0.25f;
            rotateSpeed = 1.6f;
            faceTarget = false;

            armor = 10f;
            immunities.add(TektonStatusEffects.tarredInMethane);
            
            var missile = new MissileBulletType(4f, 1) {{
                lifetime = 60f;
                homingPower = 0.2f;
                ammoMultiplier = 2f;
                shootEffect = Fx.shootSmall;
                width = 6.25f;
                height = 8.5f;
                hitEffect = Fx.flakExplosion;
                splashDamage = 15;
                splashDamageRadius = 22f;
                range = 40f;
            }};
            
            weapons.add(new Weapon("tekton-mount-weapon") {{
                reload = 40f;
                x = 13f;
                shootY = 3.2f;
                y = 1.7f;
                rotate = true;
                shootCone = 360f;
                ejectEffect = Fx.casing2;
                shootSound = Sounds.missile;
            	inaccuracy = 4;
                controllable = false;
                autoTarget = true;
                
                shoot = new ShootPattern() {{
                	shots = 2;
                	shotDelay = 8;
                }};
                
                bullet = missile;
            }});
            
            weapons.add(new Weapon("tekton-mount-weapon") {{
                reload = 60f;
                x = 18.5f;
                shootY = 3.2f;
                y = -1.8f;
                rotate = true;
                shootCone = 360f;
                ejectEffect = Fx.casing2;
                shootSound = Sounds.missile;
            	inaccuracy = 4;
                controllable = false;
                autoTarget = true;
                
                shoot = new ShootPattern() {{
                	shots = 2;
                	shotDelay = 8;
                	firstShotDelay = 20f;
                }};
                
                bullet = missile;
            }});
            
            weapons.add(new Weapon(name + "-weapon") {{
            	mirror = false;
                reload = 12f;
                
                x = 0f;
                y = 0f;
                shoot = new ShootAlternate() {{
                	spread = 10f;
                	//shots = 2;
                }};
            	shootY = 15f;
                reload = 12;
                shake = 2f;
                rotateSpeed = 2f;
                ejectEffect = Fx.casing2;
                shootSound = Sounds.shootBig;
                rotate = true;
                shadow = 8f;
            	inaccuracy = 3;
            	rotateSpeed = 2f;
                
                bullet = new BasicBulletType(7f, 40f) {{
                    width = 12f;
                    height = 18f;
                    lifetime = 30f;
                    shootEffect = Fx.shootBig;
                    /*pierce = true;
                    pierceBuilding = true;
                    pierceCap = 2;
                    splashDamage = 10f;
                    splashDamageRadius = 35f;*/
                    pierceArmor = true;
                    hitEffect = Fx.flakExplosion;
                    fragBullets = 4;
                    fragBullet = new BasicBulletType(4f, 60f / fragBullets) {{
                        lifetime = 8f;
                        pierce = true;
                        pierceBuilding = true;
                        pierceCap = 2;
                        splashDamage = 20f;
                        splashDamageRadius = 30f;
                    }};
                }};
            }});
            researchCostMultiplier = 0f;
        }};
		
		//mech
		
        nail = new TektonUnitType("nail") {{
            this.constructor = MechUnit::create;
            speed = 0.7f;
            hitSize = 9f;
            rotateSpeed = 3f;
            health = 360;
            armor = 2f;
            fogRadiusMultipliyer = 0.6f;

            weapons.add(new Weapon(name + "-weapon") {{
            	recoil = 0f;
                shootSound = Sounds.missile;
                mirror = false;
                showStatSprite = false;
                x = 0f;
                y = 0f;
                shootY = 3.5f;
                reload = 80f;
                cooldownTime = 42f;
                heatColor = Pal.turretHeat;

                bullet = new ArtilleryBulletType(3f, 10) {{
                    shootEffect = new MultiEffect(Fx.shootSmallColor, new Effect(9, e -> {
                        color(Color.white, e.color, e.fin());
                        stroke(0.7f + e.fout());
                        Lines.square(e.x, e.y, e.fin() * 5f, e.rotation + 45f);

                        Drawf.light(e.x, e.y, 23f, e.color, e.fout() * 0.7f);
                    }));

                    collidesTiles = true;
                    backColor = hitColor = Pal.techBlue;
                    frontColor = Color.white;

                    knockback = 0.8f;
                    lifetime = 50f;
                    width = height = 9f;
                    splashDamageRadius = 19f;
                    splashDamage = 20f;

                    trailLength = 27;
                    trailWidth = 2.5f;
                    trailEffect = Fx.none;
                    trailColor = backColor;
                    hitShake = 1f;

                    trailInterp = Interp.slope;

                    shrinkX = 0.6f;
                    shrinkY = 0.2f;

                    hitEffect = despawnEffect = new MultiEffect(Fx.hitSquaresColor, new WaveEffect() {{
                        colorFrom = colorTo = Pal.techBlue;
                        sizeTo = splashDamageRadius + 2f;
                        lifetime = 9f;
                        strokeFrom = 2f;
                    }});
                }};
            }});
        }};
        
        strike = new TektonUnitType("strike") {{
            this.constructor = MechUnit::create;
            fogRadiusMultipliyer = 0.56f;
            speed = 0.66f;
            hitSize = 12f;
            rotateSpeed = 3f;
            health = 780;
            armor = 5f;
            float bx = 1f;
            float treload = 30f;
            for (float ex : new float[]{-bx, 0, bx}) {
            	weapons.add(new Weapon(name + "-weapon") {{
                	recoil = 0f;
                	top = false;
                	showStatSprite = false;
                    y = 0f;
                    x = 4f * ex;
                    shootY = 5f;
                    reload = treload * 3f;
                    ejectEffect = Fx.none;
                    recoil = 0f;
                    shootSound = Sounds.missile;
                    heatColor = Pal.turretHeat;
                    //velocityRnd = 0.5f;
                    inaccuracy = 15f;
                    alternate = false;
                    mirror = false;
                    shoot.firstShotDelay = (treload / 3f) * (bx + ex);
                    bullet = new MissileBulletType(4f, 10) {{
                        homingPower = 0.09f;
                        weaveMag = 4;
                        weaveScale = 4;
                        lifetime = 50f;

                        trailWidth = 2f;
                        trailLength = 4;

                    	splashDamage = 15f;
                    	splashDamageRadius = 25f;
                        hitShake = 1.5f;
                        
                        keepVelocity = false;
                        shootEffect = new MultiEffect(Fx.shootSmallColor, new Effect(9, e -> {
                            color(Color.white, e.color, e.fin());
                            stroke(0.7f + e.fout());
                            Lines.square(e.x, e.y, e.fin() * 5f, e.rotation + 45f);

                            Drawf.light(e.x, e.y, 23f, e.color, e.fout() * 0.7f);
                        }));
                        smokeEffect = Fx.shootBigSmoke;
                        hitEffect = despawnEffect = new MultiEffect(Fx.hitBulletColor, new WaveEffect() {{
                            colorFrom = colorTo = Pal.techBlue;
                            sizeTo = splashDamageRadius + 3f;
                            lifetime = 9f;
                            strokeFrom = 3f;
                        }});
                        frontColor = Color.white;
                        hitSound = Sounds.artillery;
                        backColor = trailColor = hitColor = Pal.techBlue;
                    }};
                }});
            }
        }};
        
        hammer = new TektonUnitType("hammer") {{
            this.constructor = MechUnit::create;
            fogRadiusMultipliyer = 0.6f;
            speed = 0.6f;
            hitSize = 18f;
            rotateSpeed = 2f;
            health = 2300;
            armor = 7f;
        	weapons.add(new Weapon(name + "-weapon") {{
        		layerOffset = -0.000002f;
        		top = false;
                y = 0f;
                x = 12.1f;
                reload = 60f;
                recoil = 1.5f;
                shake = 2f;
                ejectEffect = Fx.casing2;
                shootSound = Sounds.dullExplosion;
                bullet = new BasicBulletType(7f, 40f) {{
                    sprite = "missile-large";
                    width = 7.5f;
                    height = 13f;
                    lifetime = 40f;
                    hitSize = 6f;
                    hitColor = backColor = trailColor = Pal.techBlue;
                    frontColor = Color.white;
                    trailWidth = 2.8f;
                    trailLength = 8;
                    splashDamageRadius = 25f;
                    splashDamage = 50f;

                    trailEffect = Fx.hitSquaresColor;
                    trailRotation = true;
                    trailInterval = 3f;

                    fragBullets = 5;

                    fragBullet = new BasicBulletType(5f, 30f) {{
                        sprite = "missile-large";
                        width = 5f;
                        height = 7f;
                        lifetime = 15f;
                        hitSize = 4f;
                        hitColor = backColor = trailColor = Pal.techBlue;
                        frontColor = Color.white;
                        trailWidth = 1.7f;
                        trailLength = 3;
                        drag = 0.01f;
                        despawnEffect = hitEffect = new MultiEffect(Fx.hitSquaresColor, new WaveEffect() {{
                            colorFrom = colorTo = Pal.techBlue;
                            sizeTo = splashDamageRadius + 2f;
                            lifetime = 9f;
                            strokeFrom = 2f;
                        }});
                    }};
                    
                    shootEffect = new MultiEffect(Fx.shootSmallColor, new Effect(9, e -> {
                        color(Color.white, e.color, e.fin());
                        stroke(0.7f + e.fout());
                        Lines.square(e.x, e.y, e.fin() * 5f, e.rotation + 45f);

                        Drawf.light(e.x, e.y, 23f, e.color, e.fout() * 0.7f);
                    }));
                    smokeEffect = Fx.shootBigSmoke;
                    
                    hitEffect = despawnEffect = new MultiEffect(Fx.hitBulletColor, new WaveEffect() {{
                        colorFrom = colorTo = Pal.techBlue;
                        sizeTo = splashDamageRadius + 3f;
                        lifetime = 9f;
                        strokeFrom = 3f;
                    }});
                }};
            }});
        }};
        
        impact = new TektonUnitType("impact") {{
			this.constructor = LegsUnit::create;
			fogRadiusMultipliyer = 0.75f;
            speed = 0.53f;
            drag = 0.11f;
            hitSize = 25f;
            rotateSpeed = 2.5f;
            health = 8400;
            armor = 14f;
            faceTarget = true;
            singleTarget = true;

            lockLegBase = false;
            legContinuousMove = true;
            legGroupSize = 3;
            legStraightness = 0.4f;
            baseLegStraightness = 0.5f;
            legMaxLength = 1.3f;
            legMinLength = 0.2f;
            researchCostMultiplier = 0f;

            legCount = 4;
            legLength = 18f;
            legForwardScl = 0.5f;
            legMoveSpace = 1.2f;
            rippleScale = 2f;
            stepShake = 0.5f;
            legExtension = -3f;
            legLengthScl = 0.95f;
            legBaseOffset = 7f;

            legSplashDamage = 32;
            legSplashRange = 30;
            drownTimeMultiplier = 1.5f;

            legMoveSpace = 1f;
            allowLegStep = true;
            hovering = true;

            shadowElevation = 0.4f;
            groundLayer = Layer.legUnit + 1;
            researchCostMultiplier = 0f;

            range = maxRange = ((DistanceMissileUnitType)impactMissile).maxDistance;
            var tRange = ((DistanceMissileUnitType)impactMissile).maxDistance;
            
            for (float i : new float[] {0f, 1f, 2f}) {
            	weapons.add(new DependentMissileWeapon(name + "-weapon") {{
            		var deg = -45 * i;
                    shootSound = Sounds.missileLarge;
                    mirror = true;
                    alternate = false;
                    rotate = false;
                    top = false;
                    shootCone = 45;
                    x = 15f;
                    y = -1.2f;
                    baseRotation = 0;
                    //alwaysShootWhenMoving = true;
                    weaponRange = tRange + 10f;
                    //predictTarget = false;
                    
                    shoot = new ShootBarrel() {{
                    	barrels = new float[] {
                    			10f * Mathf.cosDeg(deg), 10f * Mathf.sinDeg(deg), 145f - (20f * i)
                    	};
                    	firstShotDelay = 10f * i;
                    }};
                    
                    minWarmup = 0.95f;
                    reload = 110f;
                    cooldownTime = 42f;
                    heatColor = Pal.turretHeat;
                    ejectEffect = Fx.none;
                    recoil = 1f;
                    shake = 1.5f;

                    shootStatus = StatusEffects.slow;
                    shootStatusDuration = reload + 140f;
                    
                    parts.add(new RegionPart("-move") {{
                		mirror = false;
                        under = true;
                        top = false;
                        x = 0f;
                        y = 0f;
                		moveX = 4f * Mathf.cosDeg(deg);
                		moveY = 4f * Mathf.sinDeg(deg);
                		rotation = 90f;
                		moveRot = -45f + (-45f * (i + 1f));
                		heatProgress = PartProgress.heat;
                		heatColor = Pal.turretHeat;
                        layer = Layer.legUnit + 1 - 0.001f;
                        heatLayerOffset = 0.0001f;
                        outlineLayerOffset = 0f;
                        }});
                    
                    bullet = new BulletType() {{
                    	range = tRange;
                    	shootEffect = new MultiEffect(Fx.shootBigColor, new Effect(9, e -> {
                            color(Color.white, e.color, e.fin());
                            stroke(0.7f + e.fout());
                            Lines.square(e.x, e.y, e.fin() * 5f, e.rotation + 45f);

                            Drawf.light(e.x, e.y, 23f, e.color, e.fout() * 0.7f);
                        }), new WaveEffect() {{
                            colorFrom = colorTo = Pal.techBlue;
                            sizeTo = 15f;
                            lifetime = 12f;
                            strokeFrom = 3f;
                        }});
                        smokeEffect = Fx.none;
                        shake = 1f;
                        speed = 0f;
                        keepVelocity = false;
                        collidesGround = collidesAir = true;
                        spawnUnit = impactMissile;
                    }};
                }});
            }
        }};
		
		//ground biological
        
        var bioRegenAmount = 3f;
		
		formica = new TektonBioUnit("formica") {{
			this.constructor = LegsUnit::create;
			customFogRadius = true;
			fogRadius = 6f;
            speed = 0.9f;
            drag = 0.11f;
            hitSize = 9f;
            rotateSpeed = 3f;
            health = 260;
            armor = 4f;
            legStraightness = 0.3f;
            stepShake = 0f;

            legCount = 6;
            legLength = 8f;
            lockLegBase = true;
            legContinuousMove = true;
            legExtension = -2f;
            legBaseOffset = 3f;
            legMaxLength = 1.1f;
            legMinLength = 0.2f;
            legLengthScl = 0.96f;
            legForwardScl = 1.1f;
            legGroupSize = 3;
            rippleScale = 0.2f;

            legMoveSpace = 1f;
            allowLegStep = true;
            hovering = false;
            legPhysicsLayer = false;

            shadowElevation = 0.1f;
            groundLayer = Layer.legUnit - 1f;
            targetAir = false;
            researchCostMultiplier = 0f;

            weapons.add(new Weapon(name + "-weapon") {{
                shootSound = Sounds.missile;
                mirror = false;
                rotate = false;
                shootCone = 90f;
                x = 0f;
                y = 4f;
                shootY = 3.5f;
                reload = 60f;
                cooldownTime = 42f;
                heatColor = TektonLiquids.acid.color;
                ejectEffect = Fx.none;
                
                parts.add(new RegionPart("-mouth") {{ layer = Layer.legUnit - 1f; under = true; top = false; mirror = true; x = 2f; y = 2.5f; moveX = moveY = 0f; /*growY = 1.05f;*/ moveRot = 35f; progress = PartProgress.recoil; /*rotation = 25f;*/ }});

                bullet = new BasicBulletType(0, 0) {{
                	width = 2;
                	height = 7;
                	collidesTiles = false;
                	collides = false;
                	hitSound = Sounds.none;
                	pierceBuilding = false;
                	rangeOverride = 10;
                	trailChance = -1;
                	despawnHit = true;
                	hitEffect = despawnEffect = TektonFx.biologicalPulse;
                	hitColor = TektonLiquids.acid.color;
                	splashDamageRadius = 10;
                	splashDamage = 25;
                	instantDisappear = true;
                	hittable = false;
                	collidesAir = false;
                	scaledSplashDamage = true;
                	shootEffect = smokeEffect = Fx.none;
                }};
            }});
        }};
        
        gracilipes = new TektonBioUnit("gracilipes") {{
			this.constructor = LegsUnit::create;
            speed = 1f;
            drag = 0.1f;
            hitSize = 13f;
            rotateSpeed = 3f;
            health = 800;
            armor = 8f;
            legStraightness = 0.3f;
            stepShake = 0f;

            legCount = 6;
            legLength = 14f;
            lockLegBase = true;
            legContinuousMove = true;
            legExtension = -3f;
            legBaseOffset = 4.5f;
            legMaxLength = 1.1f;
            legMinLength = 0.2f;
            legLengthScl = 0.95f;
            legForwardScl = 0.7f;
            legGroupSize = 3;
            rippleScale = 0.2f;

            legMoveSpace = 1f;
            //allowLegStep = true;
            hovering = true;
            //legPhysicsLayer = false;

            shadowElevation = 0.2f;
            groundLayer = Layer.legUnit - 1f;
            
            targetAir = false;
            researchCostMultiplier = 0f;

            weapons.add(new Weapon(name + "-head") {{
                layerOffset = -0.0001f;
                shootSound = Sounds.missile;
                mirror = false;
                rotate = true;
                rotationLimit = 40f;
                rotateSpeed = 4f;
                top = false;
                shootCone = 90f;
                x = 0f;
                y = 7f;
                shootY = 8f;
                reload = 60f;
                recoil = 0;
                cooldownTime = 42f;
                heatColor = TektonLiquids.acid.color;
                ejectEffect = Fx.none;
                
                parts.add(new RegionPart("-mouth") {{ layerOffset = -0.0001f; under = true; top = false; mirror = true; x = 2.2f; y = 5.2f; moveX = moveY = 0f; /*growY = 1.05f;*/ moveRot = 30f; progress = PartProgress.recoil; /*rotation = 25f;*/ }});

                bullet = new BasicBulletType(0, 0) {{
                	width = 2;
                	height = 7;
                	collidesTiles = false;
                	collides = false;
                	hitSound = Sounds.none;
                	pierceBuilding = false;
                	rangeOverride = 10;
                	trailChance = -1;
                	despawnHit = true;
                	hitEffect = despawnEffect = TektonFx.biologicalPulse;
                	hitColor = TektonLiquids.acid.color;
                	splashDamageRadius = 17;
                	instantDisappear = true;
                	splashDamage = 40;
                	hittable = false;
                	collidesAir = false;
                	scaledSplashDamage = true;
                	shootEffect = smokeEffect = Fx.none;
                }};
            }});
            
            weapons.add(new Weapon(name + "-weapon") {{
                reload = 80f;
            	shootStatus = StatusEffects.slow;
            	shootStatusDuration = reload + 10f;
                //loopSound = Sounds.spray;
                shootSound = Sounds.artillery;
                mirror = false;
                baseRotation = 180f;
                x = 0f;
                y = -6f;
                shootY = 3.5f;
                rotate = true;
                rotateSpeed = 3.5f;
                
                cooldownTime = 42f;
                heatColor = TektonLiquids.acid.color;

                bullet = new ArtilleryBulletType(3f, 40) {{
                	status = TektonStatusEffects.acidified;
                	statusDuration = 60f * 4f;
                    shootEffect = TektonFx.biologicalPulse;

                    collidesTiles = true;
                    backColor = hitColor = TektonLiquids.acid.color.cpy();
                    frontColor = TektonLiquids.acid.color.cpy();

                    knockback = 1f;
                    lifetime = 50f;
                    width = height = 9f;
                    splashDamageRadius = 19f;
                    splashDamage = 30f;

                    trailLength = 27;
                    trailWidth = 2.5f;
                    trailEffect = Fx.none;
                    trailColor = backColor;

                    trailInterp = Interp.slope;

                    shrinkX = 0.6f;
                    shrinkY = 0.2f;

                    hitEffect = despawnEffect = new MultiEffect(Fx.hitSquaresColor, new WaveEffect() {{
                        colorFrom = colorTo = TektonLiquids.acid.color.cpy();
                        sizeTo = splashDamageRadius + 2f;
                        lifetime = 9f;
                        strokeFrom = 2f;
                    }});
                }};
            }});
        }};
        
        colobopsis = new TektonBioUnit("colobopsis") {{
        	this.constructor = CrawlUnit::create;
			customFogRadius = true;
			fogRadius = 6f;
            speed = 0.8f;
            hitSize = 11f;
            omniMovement = false;
            rotateSpeed = 2.2f;
            health = 1400;
            drownTimeMultiplier = 2f;
            armor = 8f;
            segments = 4;
            drawBody = false;
            crushDamage = 0.5f;
            aiController = SuicideAI::new;
            targetAir = false;

            segmentScl = 3.4f;
            segmentPhase = 5f;
            segmentMag = 0.8f;
            
            range = 40f;
            
            weapons.add(new Weapon(){{
                shootOnDeath = true;
                //targetUnderBlocks = false;
                reload = 24f;
                shootCone = 180f;
                ejectEffect = Fx.none;
                shootSound = Sounds.explosion;
                x = shootY = 0f;
                mirror = false;
                bullet = new BulletType(){{
                    collidesTiles = false;
                    collides = false;
                    hitSound = Sounds.explosion;

                    rangeOverride = 25f;
                    hitEffect = TektonFx.biologicalPulseBig;
                    speed = 0f;
                    splashDamageRadius = 44f;
                    instantDisappear = true;
                    splashDamage = 80f;
                    killShooter = true;
                    hittable = false;
                    collidesAir = true;

                	status = TektonStatusEffects.wetInAcid;
                	statusDuration = 60f * 8f;
                	
                	fragBullets = 17;
                	fragVelocityMin = 0.15f;
                	fragVelocityMax = 0.52f;
                	fragBullet = new LiquidBulletType(TektonLiquids.acid) {{
                        damage = 7;
                        speed = 2f;
                        drag = 0.009f;
                        shootEffect = TektonFx.biologicalPulse;
                        lifetime = 30f;
                        puddleAmount = 100f;
                        collidesAir = true;
                    }};
                }};
            }});
        }};
        
        carabidae = new TektonBioUnit("carabidae") {{
			this.constructor = LegsUnit::create;
            speed = 0.83f;
            drag = 0.1f;
            hitSize = 21f;
            rotateSpeed = 2.8f;
            health = 2400;
            armor = 10f;
            legStraightness = 0.3f;
            stepShake = 0.3f;

            legCount = 6;
            legLength = 16f;
            legGroupSize = 3;
            lockLegBase = true;
            legContinuousMove = true;
            legExtension = -3f;
            legBaseOffset = 5f;
            legMaxLength = 1.1f;
            legMinLength = 0.2f;
            legLengthScl = 0.95f;
            legForwardScl = 0.8f;
            legGroupSize = 3;
            //rippleScale = 0.2f;
            
            legSplashDamage = 10;
            legSplashRange = 10;

            legMoveSpace = 1f;
            //allowLegStep = true;
            hovering = true;
            //legPhysicsLayer = false;

            shadowElevation = 0.2f;
            groundLayer = Layer.legUnit - 1f;
            
            targetAir = false;
            researchCostMultiplier = 0f;

            weapons.add(new Weapon(name) {{
                reload = 1f;
                shootSound = Sounds.none;
                mirror = false;
                rotate = false;
                continuous = true;
                alwaysContinuous = true;
                top = false;
                shootCone = 20f;
                x = 0f;
                y = 0f;
                shootY = 15f;
                recoil = 0f;
                cooldownTime = 42f;
                heatColor = TektonLiquids.acid.color;
                ejectEffect = Fx.none;
                
                bullet = new ContinuousFlameBulletType(20) {{
                	width = 4f;
                	length = 120f;
                	damageInterval = 10f;
                    recoil = 1f / 60f;
                	drawFlare = false;
                	hitColor = Color.acid;
                    colors = new Color[]{Color.valueOf("46b870").a(0.55f), Color.acid.a(0.7f), Color.valueOf("cafcbe").a(0.8f), Color.valueOf("ffffff")};
                	pierce = true;
                	pierceBuilding = true;
                	pierceCap = 3;
                	status = StatusEffects.burning;
                	statusDuration = 60f * 4f;

                    makeFire = true;
                    incendChance = 0.075f;
                    incendSpread = 2f;
                    incendAmount = 1;
                }};
                
            	shootStatus = StatusEffects.slow;
            	shootStatusDuration = reload + 10f;
            }});
        }};
        
        isoptera = new TektonBioUnit("isoptera") {{
			this.constructor = LegsUnit::create;
			customFogRadius = true;
			fogRadius = 8f;
            speed = 0.65f;
            drag = 0.1f;
            hitSize = 22f;
            rotateSpeed = 2.5f;
            health = 2900;
            armor = 12f;
            legStraightness = 0.3f;
            stepShake = 0.1f;

            legCount = 6;
            legLength = 19f;
            legGroupSize = 3;
            lockLegBase = true;
            legContinuousMove = true;
            legExtension = -3f;
            legBaseOffset = 6.25f;
            legMaxLength = 1.1f;
            legMinLength = 0.2f;
            legLengthScl = 0.95f;
            legForwardScl = 0.8f;
            legGroupSize = 3;
            //rippleScale = 0.2f;
            
            legSplashDamage = 20;
            legSplashRange = 18;

            legMoveSpace = 1f;
            //allowLegStep = true;
            hovering = true;
            //legPhysicsLayer = false;

            shadowElevation = 0.2f;
            groundLayer = Layer.legUnit - 1f;
            
            targetAir = false;
            researchCostMultiplier = 0f;

            weapons.add(new Weapon(name + "-head") {{
            	range = 31f;
                shootSound = Sounds.dullExplosion;
                mirror = false;
                rotate = false;
                /*rotationLimit = 40f;
                rotateSpeed = 4f;*/
                top = false;
                shootCone = 20f;
                x = 0f;
                y = 0f;
                shootY = 24f;
                reload = 90f;
                recoil = -2.5f;
                cooldownTime = 42f;
                heatColor = TektonLiquids.acid.color;
                ejectEffect = Fx.none;
                
                bullet = new BasicBulletType(2, 0) {{
                	rangeOverride = 24f;
                	width = 7;
                	height = 7;
                	collidesTiles = false;
                	collides = false;
                	hitSound = Sounds.none;
                	pierceBuilding = false;
                	trailChance = -1;
                	despawnHit = true;
                	hitEffect = despawnEffect = TektonFx.biologicalPulseBig;
                	hitColor = TektonLiquids.acid.color;
                	splashDamageRadius = 25f;
                	splashDamage = 200f;
                	knockback = 7f;
                	scaledSplashDamage = true;
                	instantDisappear = true;
                	hittable = false;
                	collidesAir = false;
                	impact = true;
                	pierceArmor = true;
                	shootEffect = smokeEffect = Fx.none;
                }};
            }});
        }};
        
        araneae = new TektonBioUnit("araneae") {{
        	this.constructor = LegsUnit::create;
            speed = 0.84f;
            hitSize = 12f;
            health = 400;
            armor = 5f;
            
            abilities.remove((ability) -> { return ability instanceof LiquidExplodeAbility; });
            abilities.addAll(new LiquidExplodeAbility() {{ liquid = TektonLiquids.cobweb; }});
            immunities.add(TektonStatusEffects.cobwebbed);
            
            rotateSpeed = 2.4f;
            
            legCount = 8;
            legLength = 12f;
            lockLegBase = true;
            legContinuousMove = true;
            legExtension = -2f;
            legBaseOffset = 3f;
            legMaxLength = 1.1f;
            legMinLength = 0.2f;
            legLengthScl = 0.96f;
            legForwardScl = 1.1f;
            legGroupSize = 4;
            rippleScale = 0.2f;

            hovering = true;
            legPhysicsLayer = false;
            
            shadowElevation = 0.1f;
            groundLayer = Layer.legUnit - 1f;

            alwaysShootWhenMoving = true;
            targetAir = true;
            researchCostMultiplier = 0f;
            
            weapons.add(new Weapon(name + "-head") {{
                layerOffset = -0.1f;
                shootSound = Sounds.missile;
                mirror = false;
                rotate = true;
                top = false;
                minWarmup = 0.95f;
                rotationLimit = 10f;
                rotateSpeed = 4f;
                layerOffset = -0.0001f;
                shootCone = 8f;
                x = y = 0f;
                shootY = 14f;
                reload = 60f;
                recoil = 0.75f;
                heatColor = TektonLiquids.acid.color;
                ejectEffect = Fx.none;
                
                inaccuracy = 4f;
                
                bullet = new ArtilleryBulletType(4.5f, 1) {{
                	lifetime = 25f;
                	shrinkX = shrinkY = 0f;
                	width = 7f;
                	height = 8f;
                	
                	//despawnSound = hitSound = Sounds.artillery;
                	
                	trailRotation = true;
                	trailChance = -1;
                    trailWidth = 2f;
                    trailLength = 2;
	                trailSinScl = 2.5f;
	                trailSinMag = 0.5f;
	                trailEffect = Fx.disperseTrail;
	                trailInterval = 2f;
	                trailInterp = v -> Math.max(Mathf.slope(v), 0.8f);
	                
	                collidesAir = true;
	                
	                hitShake = despawnShake = 0f;
	                
                	despawnHit = true;
                	hitEffect = despawnEffect = new ExplosionEffect() {{
                        lifetime = 30f;
                        waveStroke = 3f;
                        waveColor = sparkColor = smokeColor = TektonLiquids.cobweb.color;
                        waveRad = 8f;
                        smokeSize = 2f;
                        smokes = 6;
                        smokeSizeBase = 0f;
                        sparks = 6;
                        sparkRad = 17f;
                        sparkLen = 4f;
                        sparkStroke = 3f;
                    }};
                    trailColor = backColor = hitColor = TektonLiquids.cobweb.color;
                	status = TektonStatusEffects.cobwebbed;
                	statusDuration = 60f;
                	splashDamageRadius = 8f;
                	splashDamage = 20f;
                	puddleAmount = 10f;
                	puddleRange = 2f;
                	puddles = 1;
                	puddleLiquid = TektonLiquids.cobweb;
                	shootEffect = smokeEffect = Fx.none;
                }};
            }});
        }};
        
        latrodectus = new TektonBioUnit("latrodectus") {{
        	this.constructor = LegsUnit::create;
            speed = 0.6f;
            hitSize = 28f;
            health = 8000;
            armor = 20f;

            abilities.remove((ability) -> { return ability instanceof LiquidExplodeAbility; });
            abilities.addAll(new LiquidExplodeAbility() {{ liquid = TektonLiquids.cobweb; }});
            immunities.add(TektonStatusEffects.cobwebbed);
            
            weapons.add(new Weapon("") {{
            	layerOffset = -1f;
            	top = false;
            	rotate = false;
            	baseRotation = 180f;
            	alwaysShooting = true;
            	reload = 600f;
            	shoot.firstShotDelay = reload;
            	shootY = 8f;
            	mirror = false;
            	shootSound = Sounds.mud;
            	
            	bullet = new BasicBulletType(0, 0) {{
                	width = 7;
                	height = 7;
                	collidesTiles = false;
                	collides = false;
                	hitSound = despawnSound = Sounds.none;
                	pierceBuilding = false;
                	trailChance = -1;
                	despawnHit = true;
                	hitEffect = despawnEffect = Fx.none;
                	hitColor = TektonLiquids.acid.color;
                	instantDisappear = true;
                	hittable = false;
                	shootEffect = smokeEffect = Fx.none;
                	spawnUnit = araneae;
                }};
            }});
            
            rotateSpeed = 1.8f;
            
            lockLegBase = true;
            legContinuousMove = true;
            legStraightness = 0.3f;
            baseLegStraightness = 0.25f;

            legCount = 8;
            legLength = 45f;
            rippleScale = 1.2f;
            stepShake = 0.8f;
            legGroupSize = 2;
            legExtension = -3f;

            legBaseOffset = 8f;
            legStraightLength = 0.9f;
            
            legLengthScl = 0.93f;
            legForwardScl = 1.7f;
            legMoveSpace = 1.05f;
            legMaxLength = 1.2f;
            legMinLength = 0.2f;
            legSpeed = 0.25f;
            //legPairOffset = 1f;
            
            legSplashDamage = 60;
            legSplashRange = 14;

            hovering = true;

            shadowElevation = 0.7f;
            groundLayer = Layer.legUnit;
            
            drownTimeMultiplier = 1.75f;

            alwaysShootWhenMoving = true;
            targetAir = true;
            researchCostMultiplier = 0f;

            weapons.add(new Weapon(name + "-head") {{
            	parts.add(
        			new RegionPart("-mouth") 
            		{{ 
	            		under = true;
	            		mirror = true;
	            		x = 5f;
	            		y = 6f;
	            		moveX = 0f;
        				moveY = 0f;
	            		moveRot = -40f;
	            		progress = PartProgress.warmup;
            		}},
            		new RegionPart("-submouth") 
            		{{ 
	            		under = true;
	            		mirror = true;
	            		x = 9.5f;
	            		y = 4f;
	            		moveX = 1f;
        				moveY = 0f;
	            		moveRot = -45f;
	            		progress = PartProgress.warmup;
            		}}
            	);
                shootSound = Sounds.shootBig;
                layerOffset = -0.1f;
                top = false;
                mirror = false;
                rotate = true;
                minWarmup = 0.95f;
                rotationLimit = 30f;
                rotateSpeed = 2f;
                layerOffset = -0.0001f;
                shootCone = 10f;
                x = 0f;
                y = 13f;
                shootY = 8f;
                reload = 90f;
                recoil = 1.25f;
                cooldownTime = 42f;
                heatColor = TektonLiquids.acid.color;
                ejectEffect = Fx.none;
                
                inaccuracy = 5f;
                shoot.shots = 3;
                shoot.shotDelay = 10f;
                
                bullet = new ArtilleryBulletType(5.5f, 1) {{
                	lifetime = 45f;
                	shrinkX = shrinkY = 0f;
                	width = 14f;
                	height = 17f;
                	collidesTiles = true;
                	collidesGround = collidesAir = true;
                	hittable = true;
                	collides = true;
                	reflectable = false;
                	
                	despawnSound = hitSound = Sounds.wave;
                	
                	trailRotation = true;
                	trailChance = -1;
                    trailWidth = 4f;
                    trailLength = 4;
	                trailSinScl = 2.5f;
	                trailSinMag = 0.5f;
	                trailEffect = Fx.disperseTrail;
	                trailInterval = 2f;
	                trailInterp = v -> Math.max(Mathf.slope(v), 0.8f);
	                
	                hitShake = despawnShake = 3f;
	                
                	despawnHit = true;
                	hitEffect = despawnEffect = new ExplosionEffect() {{
                        lifetime = 40f;
                        waveStroke = 4f;
                        waveColor = sparkColor = smokeColor = TektonLiquids.cobweb.color;
                        waveRad = 15f;
                        smokeSize = 5f;
                        smokes = 8;
                        smokeSizeBase = 0f;
                        sparks = 8;
                        sparkRad = 40f;
                        sparkLen = 4f;
                        sparkStroke = 3f;
                    }};
                    trailColor = backColor = hitColor = TektonLiquids.cobweb.color;
                	status = TektonStatusEffects.cobwebbed;
                	statusDuration = 60f * 3.5f;
                	splashDamageRadius = 30f;
                	splashDamage = 80f;
                	knockback = 7f;
                	puddleAmount = 100f;
                	puddleRange = 8f;
                	puddles = 1;
                	puddleLiquid = TektonLiquids.cobweb;
                	shootEffect = smokeEffect = Fx.none;
                }};
            }});
		}}; 
        
		danaus = new TektonBioUnit("danaus"){{
			this.constructor = CrawlUnit::create;
			customFogRadius = true;
			fogRadius = 3f;
			health = 500;
            armor = 6f;
            hitSize = 9f;
            omniMovement = false;
            rotateSpeed = 2.5f;
            drownTimeMultiplier = 2f;
            segments = 3;
            drawBody = false;
            crushDamage = 0.5f;
            aiController = HugAI::new;
            targetAir = false;

            segmentScl = 3f;
            segmentPhase = 5f;
            segmentMag = 0.5f;
            speed = 1.2f;
        }};
		
		antheraea = new TektonBioUnit("antheraea"){{
			this.constructor = CrawlUnit::create;
			abilities.addAll(new SpawnDeathAbility(danaus, 5, 8f));
			customFogRadius = true;
			fogRadius = 9f;
			health = 10000;
            armor = 24f;
            hitSize = 38f;
            omniMovement = false;
            rotateSpeed = 1.7f;
            drownTimeMultiplier = 4f;
            segments = 4;
            drawBody = false;
            crushDamage = 2f;
            aiController = HugAI::new;
            targetAir = false;

            segmentScl = 4f;
            segmentPhase = 5f;
            speed = 1f;
        }};
		
		//air biological
        
        diptera = new TektonBioUnit("diptera") {{
			this.constructor = ElevationMoveUnit::create;
            hoverable = true;
            hovering = true;
            
            shadowElevation = 0.1f;
            
            //layer 91?
            abilities.addAll(generateDefaultWing(3.25f, -0.5f, 180f, 200f, 15f, name + "-wing", Layer.groundUnit + 1f));

            drag = 0.14f;
            accel = 0.4f;
			hitSize = 10f;
            speed = 2f;
            rotateSpeed = 3f;
            health = 150;
            armor = 2f;
            omniMovement = false;
            rotateMoveFirst = true;
            faceTarget = true;
            targetAir = false;
            
            weapons.add(new Weapon(name + "-weapon") {{
            	mirror = false;
            	top = false;
                shootSound = Sounds.flame;
                x = shootY = 0f;
                y = 8f;
                rotate = true;
                rotateSpeed = 3f;
                rotationLimit = 90f;
                
                shootCone = 40f;
                reload = 11f;
                recoil = 1f;
                ejectEffect = Fx.none;
                parentizeEffects = false;
                bullet = new BulletType(3.4f, 7f) {{
                    ammoMultiplier = 3f;
                    hitSize = 7f;
                    lifetime = 9f;
                    pierce = false;
                    collidesAir = false;
                    statusDuration = 60f * 2;
                    shootEffect = new Effect(32f, 80f, e -> {
                        color(Color.green, Pal.heal, Color.gray, e.fin());

                        randLenVectors(e.id, 8, e.finpow() * 60f, e.rotation, 10f, (x, y) -> {
                            Fill.circle(e.x + x, e.y + y, 0.65f + e.fout() * 1.5f);
                            Drawf.light(e.x + x, e.y + y, 16f * e.fout(), Pal.heal, 0.6f);
                        });
                    });
                    hitEffect = Fx.hitFlamePlasma;
                    despawnEffect = Fx.none;
                    status = TektonStatusEffects.acidified;
                    keepVelocity = false;
                    hittable = false;
                }};
            }});
		}};
		
		groundPolyphaga = new TektonBioUnit("polyphaga-ground") {{
			this.constructor = CrawlUnit::create;
            flying = false;
            hidden = true;
            segments = 1;
            
            shadowElevation = 0.1f;

            drag = 0.2f;
            accel = 0.4f;
			hitSize = 13f;
            speed = 0.85f;
            rotateSpeed = 3f;
            health = 300;
            armor = 3f;
            omniMovement = false;
            rotateMoveFirst = true;
            faceTarget = true;
            targetAir = false;
            
            weapons.add(new Weapon("tekton-polyphaga-head") {{
            	parts.add(new RegionPart("-mouth") {{ under = true; /*top = false;*/ mirror = true; x = 2.25f; y = 4f; moveX = moveY = 0f; /*growY = 1.05f;*/ moveRot = -45f; progress = PartProgress.warmup; /*rotation = 25f;*/ }});
            	//showStatSprite = false;
            	mirror = false;
            	top = false;
                shootSound = Sounds.flame;
                x = 0f;
        		shootY = 5f;
        		minWarmup = 0.9f;
                y = 6.4f;
                rotate = true;
                rotateSpeed = 2f;
                rotationLimit = 40f;
                
                shootCone = 180f;
                reload = 50f;
                
                shoot.shotDelay = 3f;
                shoot.shots = 5;
            	inaccuracy = 5;
                
                recoil = 0.5f;
                ejectEffect = Fx.none;
                parentizeEffects = false;
                bullet = new LiquidBulletType(TektonLiquids.acid) {{
                    damage = 7;
                    speed = 2f;
                    drag = 0.009f;
                    statusDuration = 60f * 4;
                    shootEffect = TektonFx.biologicalPulse;
                    lifetime = 30f;
                    collidesAir = false;
                }};
            }});
		}};
		
		polyphaga = new TektonBioUnit("polyphaga") {{
			this.constructor = UnitEntity::create;
            flying = true;
            
            //shadowElevation = 0.1f;

            parts.add(new RegionPart(name) {{ suffix = "-plates"; /*outlineLayerOffset = -2.0001f;*/ layer = 92f; mirror = false; x = y = 0; }});
            
            abilities.addAll(generateDefaultWing(3f, -0.1f, 240f, 240f, 15f, name + "-wing", 91f));
            abilities.addAll(generateDefaultWing(3f, -0.25f, 260f, 260f, -15f, name + "-wing-bottom", 91f - 0.001f));
            abilities.add(new SpawnDeathAbility() {{ unit = groundPolyphaga; }});

            drag = 0.2f;
            accel = 0.3f;
			hitSize = groundPolyphaga.hitSize;
            speed = 1.6f;
            rotateSpeed = 2.8f;
            health = 250;
            armor = 4f;
            omniMovement = false;
            rotateMoveFirst = true;
            faceTarget = true;
            lowAltitude = true;
            targetAir = true;
            
            weapons.add(new Weapon(name + "-head") {{
            	parts.add(new RegionPart("-mouth") {{ under = true; /*top = false;*/ mirror = true; x = 2.25f; y = 4f; moveX = moveY = 0f; /*growY = 1.05f;*/ moveRot = -45f; progress = PartProgress.warmup; /*rotation = 25f;*/ }});
            	//showStatSprite = false;
            	mirror = false;
            	top = false;
                shootSound = Sounds.flame;
                x = 0f;
        		shootY = 5f;
        		minWarmup = 0.9f;
                y = 6.4f;
                rotate = true;
                rotateSpeed = 2f;
                rotationLimit = 40f;
                
                shootCone = 40f;
                reload = 40f;
                
                shoot.shotDelay = 3f;
                shoot.shots = 5;
            	inaccuracy = 5;
                
                recoil = 0.5f;
                ejectEffect = Fx.none;
                parentizeEffects = false;
                bullet = new LiquidBulletType(TektonLiquids.acid) {{
                    damage = 7;
                    speed = 2f;
                    drag = 0.009f;
                    shootEffect = TektonFx.biologicalPulse;
                    lifetime = 30f;
                    collidesAir = true;
                }};
            }});
		}};
		
		lepidoptera = new TektonBioUnit("lepidoptera") {{
			this.constructor = UnitEntity::create;
            flying = true;
            
            //shadowElevation = 0.1f;
            abilities.addAll(generateDefaultWing(5.5f, 4.3f, 180f, 180f, 15f, name + "-wing", 91f));
            //abilities.addAll(generateDefaultWing(3f, -0.25f, 260f, 260f, -15f, name + "-wing-bottom", 91f - 0.001f));

            drag = 0.2f;
            accel = 0.3f;
			hitSize = groundPolyphaga.hitSize;
            speed = 1.6f;
            rotateSpeed = 2.8f;
            health = 1400;
            armor = 6f;
            omniMovement = false;
            rotateMoveFirst = true;
            lowAltitude = false;
            targetAir = false;
            circleTarget = true;
            rotateSpeed = 2.5f;
			targetPriority = 1;
			strafePenalty = 0.5f;
            faceTarget = false;
            range = 60f;
            
            weapons.add(new Weapon() {{
            	x = y = 0f;
            	shootY = -5f;
                mirror = false;
                reload = 55f;
                minShootVelocity = 0.01f;

                soundPitchMin = 1f;
                shootSound = Sounds.mud;
                ejectEffect = Fx.none;

                bullet = new BombBulletType(120f, 45f) {{
                    sprite = "large-bomb";
                    width = height = 120/4f;

                    maxRange = 30f;
                    ignoreRotation = true;

                    backColor = TektonLiquids.acid.color.cpy();
                    frontColor = Color.white;
                    mixColorTo = TektonLiquids.acid.color.cpy();

                    //ridiculous
                    //hitSound = Sounds.mud;

                    shootCone = 180f;
                    hitShake = 4f;

                    collidesAir = false;

                    lifetime = 70f;

                    despawnEffect = hitEffect = TektonFx.biologicalPulseBig;
                    keepVelocity = false;
                    spin = 2f;

                    shrinkX = shrinkY = 0.7f;

                    speed = 0f;
                    collides = false;

                    splashDamage = 120f;
                    splashDamageRadius = 45f;
                    damage = splashDamage * 0.7f;
                    
                    status = TektonStatusEffects.acidified;
                    statusDuration = 60f * 2f;
                }};
            }});
		}};
		
		
	}
	
	private static HoverPart defaultHoverPart(float xPos, float yPos, boolean mirr, float rad, float strokeSize, float phases) {
		var a = new HoverPart() {{
			x = xPos;
			y = yPos;
        	mirror = mirr;
        	radius = rad;
        	stroke = strokeSize;
        	minStroke = 0;
        	circles = 4;
        	sides = 10;
        	phase = phases;
        	layerOffset = -0.01f;
        	color = Color.valueOf("bf92f9");
		}};
		return a;
	};
	
	private static Seq<MoveEffectAbility> wavePart(float xPos, float yPos, boolean mirr, float rad, float strokeSize, float life, float interv) {
		var a = new Seq<MoveEffectAbility>();
		var eff = new WaveEffect() {{
	    	sizeFrom = 0;
	    	sizeTo = rad;
	    	lifetime = life;
	    	strokeFrom = strokeSize;
	    	strokeTo = 0;
	    	colorFrom = Color.valueOf("bf92f9").add(Color.white.cpy().mul(0.1f));
			colorTo = Color.valueOf("bf92f9");
	    	followParent = false;
    		rotWithParent = true;
	    }};
	    
	    var min = 0.3f;

		a.add(new MoveEffectAbility() {{
			x = xPos;
			y = yPos;
			interval = interv;
			effect = eff;
        	rotateEffect = true;
        	parentizeEffects = false;
        	minVelocity = min;
		}});
		
		if (mirr)
			a.add(new MoveEffectAbility() {{
				x = -xPos;
				y = yPos;
				interval = interv;
				effect = eff;
	        	rotateEffect = true;
	        	parentizeEffects = false;
	        	minVelocity = min;
			}});
		
		return a;
	}
	
	private static Seq<MoveEffectAbility> wavePart(float xPos, float yPos, boolean mirr, float rad, float strokeSize, float life, float interv, float minSize) {
		var a = new Seq<MoveEffectAbility>();
		var eff = new WaveEffect() {{
	    	sizeFrom = minSize;
	    	sizeTo = rad;
	    	lifetime = life;
	    	strokeFrom = strokeSize;
	    	strokeTo = 0;
	    	colorFrom = Color.valueOf("bf92f9").add(Color.white.cpy().mul(0.1f));
			colorTo = Color.valueOf("bf92f9");
	    	followParent = false;
    		rotWithParent = true;
	    }};
	    
	    var min = 0.3f;

		a.add(new MoveEffectAbility() {{
			x = xPos;
			y = yPos;
			interval = interv;
			effect = eff;
        	rotateEffect = true;
        	parentizeEffects = false;
        	minVelocity = min;
		}});
		
		if (mirr)
			a.add(new MoveEffectAbility() {{
				x = -xPos;
				y = yPos;
				interval = interv;
				effect = eff;
	        	rotateEffect = true;
	        	parentizeEffects = false;
	        	minVelocity = min;
			}});
		
		return a;
	}
	
	private static MoveEffectAbility[] generateDefaultWing(float xPos, float yPos, float tbaseRotation, float baseRotationOffset, float tspin, String regionName, float layer) {
		return new MoveEffectAbility[] {
			new MoveEffectAbility() {{
	        	minVelocity = 0f;
	        	interval = 2f;
	        	x = -xPos;
	        	y = yPos;
	        	rotation = 0f;
	        	rotateEffect = true;
	        	parentizeEffects = true;
	        	effect = new MultiEffect(
	    				effecter(tbaseRotation, tspin, regionName + "-left", 0f, layer),
	    				effecter(baseRotationOffset, -tspin, regionName + "-left", 1f, layer)
	    				);
			}},
			new MoveEffectAbility() {{
	        	minVelocity = 0f;
	        	interval = 2f;
	        	x = xPos;
	        	y = yPos;
	        	rotation = 0f;
	        	rotateEffect = true;
	        	parentizeEffects = true;
	        	effect = new MultiEffect(
	    				effecter(180f - tbaseRotation, -tspin, regionName + "-right", 0f, layer),
	    				effecter(180f - baseRotationOffset, tspin, regionName + "-right", 1f, layer)
	    				);
	        }}
		};
	}
	
	private static ParticleEffect effecter(float tbaseRotation, float tspin, String regionName, float delay, float tlayer) {
		return new ParticleEffect() {{
    		layer = tlayer;
    		followParent = true;
    		rotWithParent = true;
    		region = regionName;
    		baseRotation = tbaseRotation;
    		spin = tspin;
    		lifetime = 2f;
    		lightOpacity = 0f;
    		length = 0f;
    		particles = 1;
    		startDelay = delay;
            sizeFrom = 20f;
            sizeTo = 20f;
		}};
	}
}
