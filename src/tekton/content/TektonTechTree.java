package tekton.content;

import arc.struct.*;
import arc.util.*;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.entities.bullet.*;
import mindustry.game.Objectives.*;
import mindustry.type.*;
import mindustry.type.unit.*;
import mindustry.world.blocks.defense.turrets.*;

import static mindustry.Vars.*;
import static tekton.content.TektonBlocks.*;
import static tekton.content.TektonUnits.*;
import static tekton.content.TektonSectors.*;
import static mindustry.content.TechTree.*;

public class TektonTechTree {
	public static void load(){
		
		Seq<Objective> tektonSector = Seq.with(new OnPlanet(TektonPlanets.tekton));
		
		var costMultipliers = new ObjectFloatMap<Item>();
        for(var item : content.items()) costMultipliers.put(item, 0.8f);

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
        	node(coreDeveloped, () -> {
        		node(corePerfected, () -> {
            		
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
                				
            				});
            				node(polycarbonatePipe, () -> {
            					node(polycarbonateBridgePipe, () -> {
                					
                				});
            					node(polytalumPipe, () -> {
            						
                            	});
                        	});
                    	});
                	});
            	});
    			node(reactionDrill, Seq.with(new OnSector(scintilla), new Research(coldElectrolyzer)), () -> {
    				node(gravitationalDrill, () -> {
                		
                	});
    				node(plasmaWallDrill, () -> {
        				
                	});
            	});
            	node(silicaAspirator, () -> {
            		
                	//crafting
                	node(siliconFilter, () -> {
        				node(graphiteConcentrator, Seq.with(new SectorComplete(satus)), () -> {
            				node(siliconCompressor, () -> {
                        		
                        	});
        					node(coldElectrolyzer, Seq.with(new OnSector(scintilla)), () -> {
            					node(atmosphericMethaneConcentrator, () -> {
            						node(polycarbonateSynthesizer, Seq.with(new SectorComplete(river)), () -> {
                						node(cryogenicMixer, () -> {
                                    		
                                    	});
                                	});
                            	});
            					node(magnetizer, Seq.with(new OnSector(scintilla)), () -> {
        							node(gravityConductor, () -> {
                						node(electricalCoil, () -> {
                							node(thermalCoil, Seq.with(new Research(cryogenicMixer)), () -> {
                								node(phaseNanoCoil, Seq.with(new Research(nanoAlloyCrucible), new Research(phasePrinter)), () -> {
                        							
                        						});
                    						});
                							node(polytalumFuser, Seq.with(new Research(polycarbonateSynthesizer)), () -> {
                    							node(phasePrinter, Seq.with(new SectorComplete(river)), () -> { //change
                    								
                			    				});
                                        	});
                							node(nanoAlloyCrucible, Seq.with(new SectorComplete(river)), () -> { //change
            									
            			    				});
            								node(gravityRouter, () -> {
            									node(nanoGravityConductor, Seq.with(new Research(nanoAlloyCrucible)), () -> {
                        							
                        						});
                    						});
                						});
            						});
            					});
        						node(hydrogenIncinerator, () -> {
        							node(ammoniaCatalyst, () -> {
                                		
                                	});
                            	});
                        	});
            			});
        				node(silicaTurbine, () -> {
            				node(sandFilter, () -> {
                        		
                        	});
                    	});
        			});
            	});
            	
            	//energy
        		node(methaneBurner, Seq.with(new Research(pneumaticPump), new OnSector(middle)), () -> {
        			node(lineNode, () -> {
                		node(geothermalGenerator, Seq.with(new SectorComplete(middle)), () -> {
                    		node(geothermalCondenser, () -> {
                    			node(undergroundWaterExtractor, () -> {
                    				
                            	});
                        	});
                    		node(methaneCombustionChamber, Seq.with(new SectorComplete(proelium)), () -> {
                    			node(thermalDifferenceGenerator, Seq.with(new SectorComplete(proelium)), () -> {
                    				node(uraniumReactor, Seq.with(new SectorComplete(proelium)), () -> {
                    					node(fusionReactor, Seq.with(new SectorComplete(proelium), new Research(electricalCoil)), () -> {
                                    		
                                    	});
                                	});
                            	});
                        	});
            			});
                		node(powerCapacitor, Seq.with(new SectorComplete(middle)), () -> {
                			node(powerBank, Seq.with(new SectorComplete(proelium)), () -> {
                        		
                        	});
                			node(reinforcedDiode, Seq.with(), () -> {
                        		
                        	});
                    	});
                		node(lineTower, () -> {
                			node(lineLink, Seq.with(new SectorComplete(proelium)), () -> {
                        		
                        	});
                    	});
                		node(regenerator, Seq.with(new Research(coldElectrolyzer), new SectorComplete(scintilla)), () -> {
                			node(regenerationDome, () -> {
                				node(builderDroneCenter, () -> {
                        			
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
                    node(ironWallLarge, () -> {
                    	
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
    					node(azure, Seq.with(new OnSector(lake)),  () -> {
            				node(prostrate, Seq.with(new SectorComplete(lake), new Research(magnetizer)),  () -> {
        						
                        	});
                    	});
                	});
        			node(spear, Seq.with(new SectorComplete(pit)),  () -> {
        				node(interfusion, Seq.with(new OnSector(scintilla)),  () -> {
            				node(havoc, Seq.with(new SectorComplete(lake), new Research(coldElectrolyzer)),  () -> {
                				node(concentration, Seq.with(new SectorComplete(lake), new Research(magnetizer)),  () -> {
                					node(tempest, Seq.with(new SectorComplete(lake)),  () -> {
                                		
                                	});
                            	});
                        	});
                    	});
                	});
            	});
        		node(compass, Seq.with(new SectorComplete(middle)),  () -> {
        			node(sword, Seq.with(new SectorComplete(lake)),  () -> {
        				node(tesla, Seq.with(new SectorComplete(lake)),  () -> {
            				node(radiance, Seq.with(new SectorComplete(lake)),  () -> {
                        		
                        	});
                    	});
                	});
        			node(freezer, Seq.with(new SectorComplete(lake), new Research(cryogenicMixer)),  () -> {
            			node(repulsion, Seq.with(new SectorComplete(lake), new Research(magnetizer)), () -> {
    						
    					});
                	});
            	});
        		
        		//radar
        		node(researchRadar, Seq.with(new OnSector(scintilla)), () -> {
        			node(sensor, Seq.with(new SectorComplete(proelium)), () -> {
        				
        			});
    			});
        	});
        	
        	//units tier 1
        	node(primordialUnitFactory, Seq.with(new SectorComplete(satus)), () -> {
        		node(TektonUnits.piezo, () -> {
        			
        		});
    			node(TektonUnits.martyris, Seq.with(new SectorComplete(scintilla)), () -> {
        			
        		});
				node(TektonUnits.caravela, Seq.with(new SectorComplete(proelium)), () -> {
        			
        		});
				node(TektonUnits.nail, Seq.with(new SectorComplete(proelium)), () -> {
        			
        		});
				node(unitRepairTurret, () -> {
        			
        		});
				//tier 2 & 3
				node(tankDeveloper, Seq.with(new SectorComplete(proelium)), () -> {
					node(TektonUnits.electret, () -> {
        				node(tankRefabricator, Seq.with(new SectorComplete(proelium)), () -> {
        					node(TektonUnits.discharge, Seq.with(new Research(tankRefabricator)), () -> {
        	        			
        	        		});
        				});
            		});
					node(airDeveloper, Seq.with(new SectorComplete(proelium)), () -> {
	        			node(TektonUnits.bellator, () -> {
	        				node(airRefabricator, Seq.with(new SectorComplete(proelium)), () -> {
	    						node(TektonUnits.eques, Seq.with(new Research(airRefabricator)), () -> {
	        	        			
	        	        		});
	        				});
	            		});
						node(navalDeveloper, Seq.with(new SectorComplete(proelium)), () -> {
							node(TektonUnits.sagres, Seq.with(new SectorComplete(river)), () -> {
		    					node(navalRefabricator, Seq.with(new SectorComplete(proelium)), () -> {
		    						node(TektonUnits.argos, Seq.with(new Research(navalRefabricator)), () -> {
		        	        			
		        	        		});
		        				});
		            		});
							node(mechDeveloper, Seq.with(new SectorComplete(proelium)), () -> {
								node(TektonUnits.strike, Seq.with(new SectorComplete(river)), () -> {
			    					node(mechRefabricator, Seq.with(new SectorComplete(proelium)), () -> {
			    						node(TektonUnits.hammer, Seq.with(new Research(mechRefabricator)), () -> {
			        	        			
			        	        		});
			        				});
			            		});
								//tier 4
								node(multiAssembler, Seq.with(new SectorComplete(proelium)), () -> {
									node(tankAssemblerModule, Seq.with(new SectorComplete(proelium)), () -> {
										node(TektonUnits.hysteresis, Seq.with(new Research(tankAssemblerModule)), () -> {
			        	        			
			        	        		});
			        				});
									node(airAssemblerModule, Seq.with(new SectorComplete(proelium)), () -> {
										node(TektonUnits.phalanx, Seq.with(new Research(airAssemblerModule)), () -> {
			        	        			
			        	        		});
			        				});
									node(navalAssemblerModule, Seq.with(new SectorComplete(proelium)), () -> {
										node(TektonUnits.ariete, Seq.with(new Research(navalAssemblerModule)), () -> {
			        	        			
			        	        		});
			        				});
									node(mechAssemblerModule, Seq.with(new SectorComplete(proelium)), () -> {
										node(TektonUnits.impact, Seq.with(new Research(mechAssemblerModule)), () -> {
			        	        			
			        	        		});
			        				});
									//tier 5
									node(ultimateAssembler, Seq.with(new Research(TektonBlocks.nanoAlloyWallLarge)), () -> {
										node(TektonUnits.supernova, Seq.with(new Research(tankAssemblerModule)), () -> {
			        	        			
			        	        		});
										node(TektonUnits.imperatoris, Seq.with(new Research(airAssemblerModule)), () -> {
			        	        			
			        	        		});
										node(TektonUnits.castelo, Seq.with(new Research(navalAssemblerModule)), () -> {
			        	        			
			        	        		});
										node(TektonUnits.earthquake, Seq.with(new Research(mechAssemblerModule)), () -> {
			        	        			
			        	        		});
				    				});
			    				});
							});
						});
					});
				});
        	});
        	
        	/*
        	 * Satus - start, iron & zirconium, final: silicon, duel, skycraper
        	 * middle - preparation, unit Factory, piezo, final: graphite, compass, piezo
        	 * scintilla - attack, final: martyris
        	 * proelium - survival, tantalum, final: electret, nail
        	 * pit - survival, final: spear
        	 * river - 
        	 * lake - 
        	 * */
        	
        	//sectors
        	node(satus, () -> {
        		node(middle, Seq.with(new SectorComplete(satus), new Research(siliconFilter)), () -> {
        			node(scintilla, Seq.with(new SectorComplete(middle), new Research(primordialUnitFactory)), () -> {
            			node(proelium, Seq.with(new SectorComplete(scintilla), new Research(duel)), () -> {
            				node(pit, Seq.with(new SectorComplete(proelium), new Research(reactionDrill)), () -> {
            					node(lake, Seq.with(new SectorComplete(river), new Research(reactionDrill)), () -> {
            						/*node(aequor, Seq.with(new SectorComplete(lake), new Research(reactionDrill)), () -> {
                                		
                                	});*/
                            	});
                        	});
            				node(river, Seq.with(new SectorComplete(proelium)), () -> {
            					
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
    					nodeProduce(TektonItems.magnet, () -> {
        	        		
        	        	});
    	        	});
            	});
    			nodeProduce(TektonItems.zirconium, () -> {
    				nodeProduce(Items.graphite, () -> {
    					nodeProduce(TektonItems.tantalum, () -> {
        					nodeProduce(TektonItems.uranium, () -> {
        						nodeProduce(Items.phaseFabric, () -> {
        							nodeProduce(TektonItems.nanoAlloy, () -> {
        	        	        		
        	        	        	});
                	        	});
            	        	});
        	    			nodeProduce(TektonItems.cryogenicCompound, () -> {
        		        		
        		        	});
        	        	});
    	        	});
	        	});
    			
    			//liquids
    			nodeProduce(TektonLiquids.methane, () -> {
    				nodeProduce(TektonLiquids.liquidMethane, () -> {
                		
                	});
            		nodeProduce(Liquids.water, Seq.with(new Research(graphiteConcentrator)), () -> {
            			nodeProduce(Liquids.hydrogen, () -> {
                			nodeProduce(TektonLiquids.ammonia, () -> {
                				
                        	});
                			
                			//biological session
            				nodeProduce(TektonLiquids.acid, () -> {
            					node(TektonUnits.formica, () -> {
            						node(TektonUnits.gracilipes, () -> {
            							node(TektonUnits.carabidae, () -> {
            								
                    					});
                					});
    								node(TektonUnits.danaus, () -> {
            							node(TektonUnits.antheraea, () -> {
                    						
                    					});
                					});
            						node(TektonUnits.colobopsis, () -> {
                						node(TektonUnits.isoptera, () -> {
                    						
                    					});
                					});
            						node(TektonUnits.diptera, () -> {
            							node(TektonUnits.polyphaga, () -> {
            								node(TektonUnits.lepidoptera, () -> {
                        						
                        					});
                    					});
                					});
    								node(TektonUnits.latrodectus, () -> {
    									node(TektonLiquids.cobweb, Seq.with(new Research(latrodectus)), () -> {
                    						
                    					});
                					});
            					});
            					
                    			//alien session
                    			/*nodeProduce(TektonLiquids.metazotoplasm, () -> {
                            		
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
 