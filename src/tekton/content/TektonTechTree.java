package tekton.content;

import static mindustry.Vars.content;
import static mindustry.content.TechTree.*;
import static tekton.content.TektonBlocks.*;
import static tekton.content.TektonUnits.*;
import static tekton.content.TektonSectors.*;

import arc.struct.ObjectFloatMap;
import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.game.Objectives.*;
import mindustry.type.Item;

public class TektonTechTree {
	public static void load(){

		Seq<Objective> tektonSector = Seq.with(new OnPlanet(TektonPlanets.tekton));
		
		var currentMaxSector = radiation;

		var costMultipliers = new ObjectFloatMap<Item>();
        for(var item : content.items()) {
			costMultipliers.put(item, 0.8f);
		}

        costMultipliers.put(TektonItems.magnet, 0.5f);
        costMultipliers.put(TektonItems.polytalum, 0.6f);
        costMultipliers.put(TektonItems.uranium, 0.8f);
        costMultipliers.put(Items.phaseFabric, 0.3f);
        costMultipliers.put(TektonItems.nanoAlloy, 0.6f);

        TektonPlanets.tekton.techTree = nodeRoot("tekton", corePrimal, true, () -> {
        	context().researchCostMultipliers = costMultipliers;

        	//distribution
        	node(ironDuct, tektonSector, () -> {
        		node(ironBridge, () -> {
        			node(tantalumDuct, () -> {
        				node(nanoConveyor, () -> {
        					node(nanoJunction, () -> {

            				});
        					if (!nanoRouter.isHidden()) {
								node(nanoRouter, () -> {

	            				});
							}
        				});
    				});
				});
        		node(ironRouter, () -> {
        			node(ironSorter, () -> {
        				node(ironInvertedSorter, () -> {

                    	});
                	});
        			node(ironOverflow, () -> {
        				node(ironUnderflow, () -> {

                    	});
        				node(capsule, () -> {
                			node(ironUnloader, () -> {

                        	});
                			node(vault, () -> {

            				});
        				});
                	});
        			node(ironMessage, () -> {
        				node(ironCanvas, () -> {

                    	});
                	});
            	});

        		//payloads
				node(ironPayloadConveyor, () -> {
					node(payloadLauncher, () -> {
						node(payloadLoader, () -> {
							node(payloadUnloader, () -> {

							});
						});
						node(constructor, () -> {
							node(deconstructor, () -> {

							});
						});
					});
					node(ironPayloadRouter, () -> {

					});
				});
        	});

        	//cores
        	node(coreDeveloped, Seq.with(new SectorComplete(lake)), () -> {
        		node(corePerfected, Seq.with(new SectorComplete(radiation)), () -> {

            	});
        	});

        	//production
        	node(wallDrill, () -> {

        		//liquid
            	node(pneumaticPump, Seq.with(new SectorComplete(satus)), () -> {
            		node(pressurePump, Seq.with(new SectorComplete(proelium)), () -> {

    				});
            		node(pipe, () -> {
            			node(pipeJunction, () -> {
            				node(pipeRouter, () -> {
            					node(polycarbonateLiquidContainer, () -> {
            						node(polycarbonateLiquidReserve, () -> {

            	    				});
                				});
            				});
            				node(bridgePipe, () -> {
            					node(polycarbonateBridgePipe, () -> {

                				});
            				});
            				node(polycarbonatePipe, () -> {
            					node(polytalumPipe, () -> {

                            	});
                        	});
                    	});
                	});
            	});
    			node(reactionDrill, Seq.with(new SectorComplete(scintilla), new Research(coldElectrolyzer)), () -> {
    				node(gravitationalDrill, () -> {

                	});
    				node(plasmaWallDrill, () -> {

                	});
            	});
            	node(silicaAspirator, Seq.with(new SectorComplete(satus)), () -> {

                	//crafting
                	node(siliconFilter, () -> {
        				node(siliconCompressor, () -> {

                    	});
        				node(graphiteConcentrator, Seq.with(new SectorComplete(satus)), () -> {
        					node(coldElectrolyzer, Seq.with(new OnSector(scintilla)), () -> {
        						node(polycarbonateSynthesizer, Seq.with(new SectorComplete(pit)), () -> {
            						node(cryogenicMixer, Seq.with(new SectorComplete(pit)), () -> {

                                	});
                            	});
            					node(magnetizer, Seq.with(new SectorComplete(river)), () -> {
                					node(atmosphericMethaneConcentrator, () -> {
            							node(ammoniaCatalyst, () -> {

                                    	});
                                	});
        							node(gravityConductor, () -> {
                						node(electricalCoil, () -> {
                							node(thermalCoil, Seq.with(new Research(cryogenicMixer)), () -> {
            									node(reinforcedCoil, () -> {
                									node(expansionCoil, () -> {
                        								node(phaseNanoCoil, Seq.with(new Research(nanoAlloyCrucible), new Research(phasePrinter)), () -> {

                                						});
                            						});
                        						});
                    						});
                							node(polytalumFuser, Seq.with(new Research(polycarbonateSynthesizer)), () -> {
                    							node(phasePrinter, Seq.with(new SectorComplete(currentMaxSector)), () -> { //change

                			    				});
                                        	});
                							node(nanoAlloyCrucible, Seq.with(new SectorComplete(currentMaxSector)), () -> { //change

            			    				});
            								node(gravityRouter, () -> {
            									node(nanoGravityConductor, Seq.with(new Research(nanoAlloyCrucible)), () -> {

                        						});
                    						});
                						});
            						});
            					});
        						node(hydrogenIncinerator, Seq.with(new Research(coldElectrolyzer)), () -> {

                            	});
                        	});
            			});
        			});
    				node(silicaTurbine, Seq.with(new SectorComplete(proelium)), () -> {
        				node(sandFilter, Seq.with(new SectorComplete(scintilla), new Research(reactionDrill)), () -> {

                    	});
                	});
            	});

            	//power
        		node(methaneBurner, Seq.with(new SectorComplete(satus)), () -> {
        			node(lineNode, () -> {
                		node(geothermalGenerator, Seq.with(new OnSector(middle)), () -> {
                    		node(geothermalCondenser, () -> {
                    			node(undergroundWaterExtractor, Seq.with(new SectorComplete(lake)), () -> {

                            	});
                        	});
                    		node(methaneCombustionChamber, Seq.with(new SectorComplete(pit)), () -> {
                    			node(thermalDifferenceGenerator, Seq.with(new SectorComplete(cave)), () -> {
                    				node(acidCore, Seq.with(new SectorComplete(currentMaxSector)), () -> { //change?

                                	});
                    				node(uraniumReactor, Seq.with(new SectorComplete(radiation)), () -> { //change
                    					node(fusionReactor, Seq.with(new SectorComplete(currentMaxSector), new Research(electricalCoil)), () -> { //change

                                    	});
                                	});
                            	});
                        	});
            			});
                		node(powerCapacitor, Seq.with(new SectorComplete(middle)), () -> {
                			node(powerBank, Seq.with(new SectorComplete(proelium)), () -> {
                				node(lightningRod, Seq.with(new SectorComplete(currentMaxSector)), () -> { //change

                            	});
                        	});
                			node(reinforcedDiode, Seq.with(), () -> {

                        	});
                    	});
                		node(lineTower, () -> {
                			node(lineLink, Seq.with(new SectorComplete(aequor)), () -> { //change

                        	});
                    	});
                		node(regenerator, Seq.with(new Research(coldElectrolyzer), new SectorComplete(scintilla)), () -> {
                			node(regenerationDome, Seq.with(new SectorComplete(cave)), () -> {
                				node(builderDroneCenter, Seq.with(new SectorComplete(currentMaxSector)), () -> { //change

                            	});
            					node(latencyTower, Seq.with(new SectorComplete(currentMaxSector)), () -> { //change

                            	});
                        	});
                    	});
                	});
            	});
        	});

        	//turrets
        	node(one, () -> {

            	//wall
            	node(ironWall, () -> {
                    node(ironWallLarge, Seq.with(new SectorComplete(satus)), () -> {

                    });
                    //resistance
                    node(tantalumWall, () -> {
                        node(tantalumWallLarge, () -> {
                        	node(gate, () -> {

                            });
                        });
                        node(uraniumWall, () -> {
                            node(uraniumWallLarge, () -> {

                            });
                            node(nanoAlloyWall, Seq.with(new Research(polytalumWall)), () -> {
                                node(nanoAlloyWallLarge, Seq.with(new Research(polytalumWallLarge)), () -> {

                                });
                            });
                        });
                    });
                    //plastic
                    node(polycarbonateWall, () -> {
                        node(polycarbonateWallLarge, () -> {

                        });
                        node(polytalumWall, () -> {
                            node(polytalumWallLarge, () -> {

                            });
                        });
                    });
                });

        		node(duel, Seq.with(new SectorComplete(satus)),  () -> {
    				node(skyscraper, Seq.with(new SectorComplete(satus)),  () -> {
    					node(azure, Seq.with(new SectorComplete(pit)),  () -> {
            				node(prostrate, Seq.with(new SectorComplete(radiation), new Research(magnetizer)),  () -> {

                        	});
                    	});
                	});
        			node(spear, Seq.with(new SectorComplete(pit)),  () -> {
        				node(interfusion, Seq.with(new SectorComplete(river)),  () -> {
            				
                    	});
        				node(havoc, Seq.with(new SectorComplete(aequor), new Research(coldElectrolyzer)),  () -> { //change
            				node(concentration, Seq.with(new SectorComplete(aequor), new Research(magnetizer)),  () -> { //change
            					node(tempest, Seq.with(new SectorComplete(aequor)),  () -> { //change

                            	});
                        	});
                    	});
                	});
            	});
        		node(compass, Seq.with(new SectorComplete(middle)),  () -> {
        			node(sword, Seq.with(new SectorComplete(river)),  () -> {
        				node(tesla, Seq.with(new SectorComplete(currentMaxSector)),  () -> { //change
            				node(radiance, Seq.with(new SectorComplete(currentMaxSector)),  () -> { //change

                        	});
                    	});
                	});
        			node(freezer, Seq.with(new Research(cryogenicMixer)),  () -> {
            			node(repulsion, Seq.with(new SectorComplete(currentMaxSector), new Research(magnetizer)), () -> { //change

    					});
                	});
            	});

        		//radar
        		node(researchRadar, Seq.with(new OnSector(scintilla)), () -> {
        			node(sensor, Seq.with(new SectorComplete(lake)), () -> {

        			});
    			});
        	});

        	//units tier 1
        	node(primordialUnitFactory, Seq.with(new SectorComplete(satus)), () -> {
        		node(TektonUnits.piezo, () -> {

        		});
    			node(TektonUnits.martyris, Seq.with(new SectorComplete(scintilla)), () -> {

        		});
				node(TektonUnits.nail, Seq.with(new SectorComplete(proelium)), () -> {

        		});
				node(TektonUnits.caravela, Seq.with(new SectorComplete(lake)), () -> {

        		});
				node(unitRepairTurret, () -> {

        		});
				//tier 2 & 3
				node(tankDeveloper, Seq.with(new SectorComplete(proelium)), () -> {
					node(TektonUnits.electret, () -> {
        				node(tankRefabricator, Seq.with(new SectorComplete(currentMaxSector)), () -> {
        					node(TektonUnits.discharge, Seq.with(new Research(tankRefabricator)), () -> {

        	        		});
        				});
            		});
					node(airDeveloper, Seq.with(new SectorComplete(pit)), () -> {
	        			node(TektonUnits.bellator, () -> {
	        				node(airRefabricator, Seq.with(new OnSector(currentMaxSector)), () -> {
	    						node(TektonUnits.eques, Seq.with(new Research(airRefabricator)), () -> {

	        	        		});
	        				});
	            		});
						node(mechDeveloper, Seq.with(new SectorComplete(lake)), () -> {
	    					node(TektonUnits.strike, () -> {
		    					node(mechRefabricator, Seq.with(new OnSector(currentMaxSector)), () -> {
		    						node(TektonUnits.hammer, Seq.with(new Research(mechRefabricator)), () -> {

		        	        		});
		        				});
		            		});
	    					node(navalDeveloper, Seq.with(new SectorComplete(river)), () -> {
								node(TektonUnits.sagres, () -> {
			    					node(navalRefabricator, Seq.with(new SectorComplete(currentMaxSector)), () -> {
			    						node(TektonUnits.argos, Seq.with(new Research(navalRefabricator)), () -> {

			        	        		});
			        				});
									//tier 4
									node(multiAssembler, Seq.with(new SectorComplete(currentMaxSector)), () -> {
										node(tankAssemblerModule, Seq.with(new Research(tankRefabricator), new SectorComplete(currentMaxSector)), () -> {
											node(TektonUnits.hysteresis, Seq.with(new Research(tankAssemblerModule)), () -> {

				        	        		});
				        				});
										node(airAssemblerModule, Seq.with(new Research(airRefabricator), new SectorComplete(currentMaxSector)), () -> {
											node(TektonUnits.phalanx, Seq.with(new Research(airAssemblerModule)), () -> {

				        	        		});
				        				});
										node(navalAssemblerModule, Seq.with(new Research(navalDeveloper), new SectorComplete(currentMaxSector)), () -> {
											node(TektonUnits.ariete, Seq.with(new Research(navalAssemblerModule)), () -> {

				        	        		});
				        				});
										node(mechAssemblerModule, Seq.with(new Research(mechDeveloper), new SectorComplete(currentMaxSector)), () -> {
											node(TektonUnits.impact, Seq.with(new Research(mechAssemblerModule)), () -> {

				        	        		});
				        				});
										//tier 5
										node(ultimateAssembler, Seq.with(), () -> {
											node(TektonUnits.supernova, Seq.with(new Research(ultimateAssembler), new Research(tankAssemblerModule), new Research(TektonBlocks.uraniumWallLarge)), () -> {

				        	        		});
											node(TektonUnits.imperatoris, Seq.with(new Research(ultimateAssembler), new Research(airAssemblerModule), new Research(TektonBlocks.uraniumWallLarge)), () -> {

				        	        		});
											node(TektonUnits.castelo, Seq.with(new Research(ultimateAssembler), new Research(navalAssemblerModule), new Research(TektonBlocks.polytalumWallLarge)), () -> {

				        	        		});
											node(TektonUnits.earthquake, Seq.with(new Research(ultimateAssembler), new Research(mechAssemblerModule), new Research(TektonBlocks.polytalumWallLarge)), () -> {

				        	        		});
					    				});
				    				});
			            		});
							});
						});
					});
				});
        	});

        	/*
        	 * introduction
        	 *
        	 * Satus - start, iron & zirconium, final: silicon, duel, skycraper
        	 * middle - preparation, graphite, final: unit Factory, piezo, compass
        	 *
        	 * start of the campaign, t2 attacks
        	 *
        	 * scintilla - attack, final: martyris
        	 * proelium - survival, final: electret, nail, tantalum
        	 * pit - survival, final: spear, azure, bellator
        	 * 
        	 * t2 possession, naval introduction, t3 attacks, last pre uranium resources
        	 *
        	 * lake - T3 bosses, final: sword, caravela, strike, interfusion, polycarbonate
        	 * river - attack, final: sagres, sword, freezer, payload Launchers, magnet
        	 * infestation - T3 biological bosses, final: acid
        	 * 
        	 * real challenges
        	 * 
        	 * rainforest - survival T4 boss, biological creatures
        	 * beach - survival T4 boss (optional)
        	 * colony - (optional)
        	 * transit - (optional)
        	 * cave - attack, final: .
        	 * aequor - attack, final sector before uranium, final: .
        	 * 
        	 * post uranium
        	 * 
        	 * radiation - final: uranium.
        	 *
        	 * */

        	//sectors
        	node(satus, () -> {
        		node(middle, Seq.with(new SectorComplete(satus), new Research(silicaAspirator), new Research(siliconFilter)), () -> {
        			node(scintilla, Seq.with(new SectorComplete(middle), new Research(Items.graphite), new Research(primordialUnitFactory)), () -> {
            			node(proelium, Seq.with(new SectorComplete(scintilla), new Research(duel), new Research(reactionDrill)), () -> {
            				node(pit, Seq.with(new SectorComplete(proelium), new Research(TektonItems.tantalum)), () -> {
            					node(lake, Seq.with(new SectorComplete(pit)), () -> {
                    				node(river, Seq.with(new SectorComplete(lake), new Research(TektonItems.polycarbonate), new Research(caravela)), () -> {
                    					node(infestation, Seq.with(new SectorComplete(river)), () -> {
                    						node(cave, Seq.with(new SectorComplete(infestation), new Research(magnetizer)), () -> {
                        						node(aequor, Seq.with(new SectorComplete(cave), new Research(sagres)), () -> {
                        							node(radiation, Seq.with(new SectorComplete(aequor), new Research(phaseNanoCoil)), () -> {
                            							
                                                	});
                                            	});
                                        	});
                        					
                            				node(rainforest, Seq.with(new SectorComplete(infestation), new Research(magnetizer)), () -> {
                            					node(transit, Seq.with(new SectorComplete(rainforest)), () -> {
                                					node(colony, Seq.with(new SectorComplete(transit)), () -> {
                            							
                            						});
                                            	});
                        						
                        						node(beach, Seq.with(new SectorComplete(rainforest)), () -> {
                        							
                        						});
                                        	});
                                    	});
                                	});
                            	});
                        	});
                    	});
                	});
        		});
        	});

        	//items
        	nodeProduce(TektonItems.iron, () -> {
    			nodeProduce(TektonItems.silica, () -> {
    				nodeProduce(Items.sand, Seq.with(new Research(sandFilter)), () -> {

    	        	});
    				nodeProduce(Items.silicon, () -> {
        				nodeProduce(Items.graphite, () -> {
        					nodeProduce(TektonItems.magnet, () -> {

            	        	});
        	        	});
    	        	});
            	});
    			nodeProduce(TektonItems.zirconium, () -> {
    				nodeProduce(TektonItems.polycarbonate, () -> {
    					nodeProduce(TektonItems.polytalum, () -> {

        	        	});
    	        	});
    				nodeProduce(TektonItems.tantalum, () -> {
    	    			nodeProduce(TektonItems.cryogenicCompound, () -> {

    		        	});
    					nodeProduce(TektonItems.uranium, () -> {
							nodeProduce(TektonItems.nanoAlloy, () -> {

	        	        	});
    						nodeProduce(Items.phaseFabric, () -> {
            	        	});
        	        	});
    	        	});
	        	});

    			//liquids
    			nodeProduce(TektonLiquids.methane, () -> {
            		nodeProduce(Liquids.water, () -> {
            			nodeProduce(TektonLiquids.ammonia, () -> {
            				nodeProduce(TektonLiquids.dicyanogen, () -> {

                        	});
                    	});
            			nodeProduce(Liquids.hydrogen, () -> {

                			//biological session
            				nodeProduce(TektonLiquids.acid, () -> {
            					/*node(TektonUnits.formica, Seq.with(new SectorComplete(rainforest)), () -> {
            						node(TektonUnits.gracilipes, Seq.with(new SectorComplete(rainforest)), () -> {
            							node(TektonUnits.carabidae, Seq.with(new SectorComplete(rainforest)), () -> {

                    					});
                					});
    								node(TektonUnits.danaus, Seq.with(new SectorComplete(rainforest)), () -> {
            							node(TektonUnits.antheraea, Seq.with(new SectorComplete(transit)), () -> {

                    					});
                					});
            						node(TektonUnits.colobopsis, Seq.with(new SectorComplete(rainforest)), () -> {
                						node(TektonUnits.isoptera, Seq.with(new SectorComplete(rainforest)), () -> {

                    					});
                					});
            						node(TektonUnits.diptera, Seq.with(new SectorComplete(rainforest)), () -> {
            							node(TektonUnits.polyphaga, Seq.with(new SectorComplete(rainforest)), () -> {
            								node(TektonUnits.lepidoptera, Seq.with(new SectorComplete(rainforest)), () -> {

                        					});
                    					});
                					});
    								node(TektonUnits.latrodectus, Seq.with(new SectorComplete(transit)), () -> {
    									node(TektonLiquids.cobweb, Seq.with(new Research(latrodectus)), () -> {

                    					});
                					});
            					});*/
                        	});
                    	});
            			nodeProduce(TektonLiquids.oxygen, () -> {

                    	});
                	});
            	});
        	});
        });
	}
}
