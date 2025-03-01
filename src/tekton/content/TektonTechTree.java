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

        costMultipliers.put(TektonItems.polycarbonate, 0.8f);
        costMultipliers.put(TektonItems.polytalum, 0.7f);
        costMultipliers.put(TektonItems.uranium, 0.6f);
        costMultipliers.put(Items.phaseFabric, 0.3f);
        costMultipliers.put(TektonItems.nanoAlloy, 0.5f);
        
        TektonPlanets.tekton.techTree = nodeRoot("tekton", corePrimal, true, () -> {
        	context().researchCostMultipliers = costMultipliers;
        	
        	//distribution
        	node(ironDuct, tektonSector, () -> {
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
            	});
        		node(ironBridge, () -> {
					
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
        	
        	//production
        	node(wallDrill, () -> {
        		//liquid
            	node(methanePump, Seq.with(new SectorComplete(satus)), () -> {
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
                        	});
                    	});
                	});
            	});
        		node(geothermalCondenser, () -> {
        			node(undergroundWaterExtractor, () -> {
        				
                	});
            	});
    			node(reactionDrill, Seq.with(new OnSector(scintilla), new Research(coldElectrolyzer)), () -> {
    				node(carbonicLaserDrill, () -> {
                		
                	});
            	});
            	node(silicaAspirator, () -> {
                	//crafting
                	node(siliconFilter, Seq.with(new Research(silicaAspirator)), () -> {
        				node(siliconSmelter, () -> {
                    		
                    	});
        				node(graphiteConcentrator, Seq.with(new SectorComplete(satus)), () -> {
        					node(coldElectrolyzer, Seq.with(new OnSector(scintilla)), () -> {
        						node(polycarbonateSynthesizer, () -> {
        							node(polytalumFuser, () -> {
                                		
                                	});
                            	});
        						node(cryogenicMixer, () -> {
                            		
                            	});
        						node(hydrogenIncinerator, () -> {
        							node(ammoniaCatalyst, () -> {
                                		
                                	});
                            	});
                        	});
        					node(atmosphericMethaneConcentrator, () -> {
                        		
                        	});
            			});
        				node(sandFilter, () -> {
                    		
                    	});
        			});
    				node(silicaTurbine, () -> {
                		
                	});
            	});
        	});
        	
        	//energy
    		node(methaneBurner, Seq.with(new Research(methanePump)), () -> {
    			node(lineNode, () -> {
            		node(geothermalGenerator, Seq.with(new OnSector(proelium)), () -> {
                		node(methaneCombustionChamber, Seq.with(new SectorComplete(proelium)), () -> {
                			node(thermalDifferenceGenerator, Seq.with(new SectorComplete(proelium)), () -> {
                				node(uraniumReactor, Seq.with(new SectorComplete(proelium)), () -> {
                            		
                            	});
                        	});
                    	});
        			});
            		node(powerCapacitor, Seq.with(new OnSector(scintilla)), () -> {
            			node(powerBank, Seq.with(new SectorComplete(proelium)), () -> {
                    		
                    	});
            			node(reinforcedDiode, Seq.with(new SectorComplete(scintilla)), () -> {
                    		
                    	});
                	});
            		node(lineTower, () -> {
            			node(lineLink, Seq.with(new SectorComplete(proelium)), () -> {
                    		
                    	});
                	});
            		node(researchRadar, Seq.with(new OnSector(scintilla)), () -> {
            			node(sensor, Seq.with(new SectorComplete(proelium)), () -> {
            				
            			});
        			});
            		node(regenerator, Seq.with(new Research(coldElectrolyzer), new SectorComplete(scintilla)), () -> {
            			node(regenerationDome, () -> {
                			
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
            	
        		node(duel, Seq.with(new OnSector(scintilla)),  () -> {
    				node(skyscraper, Seq.with(new SectorComplete(scintilla)),  () -> {
                		
                	});
    				node(interfusion, Seq.with(new OnSector(scintilla)),  () -> {
                		
                	});
        			node(spear, Seq.with(new OnSector(scintilla)),  () -> {
        				node(havoc, Seq.with(new OnSector(scintilla)),  () -> {
                    		
                    	});
                	});
            	});
        		node(compass, Seq.with(new OnSector(proelium)),  () -> {
        			node(sword, Seq.with(new SectorComplete(aequor)),  () -> {
        				node(tesla, Seq.with(new SectorComplete(aequor)),  () -> {
                    		
                    	});
        				node(prostrate, Seq.with(new SectorComplete(aequor)),  () -> {
                    		
                    	});
                	});
            	});
        		node(freezer, Seq.with(new SectorComplete(aequor), new Research(cryogenicMixer)),  () -> {
            		
            	});
        	});
        	
        	//cores
        	node(coreDeveloped, () -> {
        		
        	});
        	
        	//units tier 1
        	node(primordialUnitFactory, Seq.with(new SectorComplete(satus)), () -> {
        		node(TektonUnits.piezo, () -> {
        			
        		});
    			node(TektonUnits.martyris, Seq.with(new SectorComplete(scintilla)), () -> {
        			
        		});
				node(TektonUnits.caravela, Seq.with(new OnSector(river)), () -> {
        			
        		});
				node(TektonUnits.nail, Seq.with(new SectorComplete(river)), () -> {
        			
        		});
				//tier 2 & 3
        		node(unitDeveloper, Seq.with(new SectorComplete(proelium)), () -> {
        			node(TektonUnits.electret, () -> {
        				node(tankRefabricator, Seq.with(new SectorComplete(proelium)), () -> {
        					node(TektonUnits.discharge, Seq.with(new Research(tankRefabricator)), () -> {
        	        			
        	        		});
        				});
            		});
    				node(TektonUnits.bellator, () -> {
        				node(airRefabricator, Seq.with(new SectorComplete(proelium)), () -> {
    						node(TektonUnits.eques, Seq.with(new Research(airRefabricator)), () -> {
        	        			
        	        		});
        				});
            		});
    				node(TektonUnits.sagres, Seq.with(new SectorComplete(river)), () -> {
    					node(navalRefabricator, Seq.with(new SectorComplete(proelium)), () -> {
    						node(TektonUnits.argos, Seq.with(new Research(navalRefabricator)), () -> {
        	        			
        	        		});
        				});
            		});
    				node(TektonUnits.sagres, Seq.with(new SectorComplete(river)), () -> {
    					node(navalRefabricator, Seq.with(new SectorComplete(proelium)), () -> {
    						node(TektonUnits.argos, Seq.with(new Research(navalRefabricator)), () -> {
        	        			
        	        		});
        				});
            		});
    				node(TektonUnits.striker, Seq.with(new SectorComplete(river)), () -> {
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
							/*node(TektonUnits.impact, Seq.with(new Research(mechAssemblerModule)), () -> {
        	        			
        	        		});*/
        				});
    				});
            	});
        	});
        	
        	//sectors
        	node(satus, () -> {
        		node(scintilla, Seq.with(new SectorComplete(satus), new Research(primordialUnitFactory)), () -> {
        			node(proelium, Seq.with(new SectorComplete(scintilla), new Research(duel)), () -> {
        				node(river, Seq.with(new SectorComplete(proelium)), () -> {
        					node(lake, Seq.with(new SectorComplete(river), new Research(reactionDrill)), () -> {
        						node(aequor, Seq.with(new SectorComplete(lake), new Research(reactionDrill)), () -> {
                            		
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
    	        		
    	        	});
    				nodeProduce(Items.graphite, () -> {
    	        		
    	        	});
            	});
    			nodeProduce(TektonItems.zirconium, () -> {
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
            						node(TektonUnits.isoptera, () -> {
                						
                					});
            						node(TektonUnits.diptera, () -> {
            							node(TektonUnits.polyphaga, () -> {
            								node(TektonUnits.lepidoptera, () -> {
                        						
                        					});
                    					});
                					});
            					});
                    			nodeProduce(TektonLiquids.metazotoplasm, () -> {
                            		
                            	});
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
 