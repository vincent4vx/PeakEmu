//package org.peakemu.objects;
//
//import org.peakemu.objects.player.Player;
//import org.peakemu.world.GameMap;
//
//import org.peakemu.objects.Metier.StatsMetier;
//import org.peakemu.objects.Monstre.MobGroup;
//import org.peakemu.objects.NPC_tmpl.NPC_question;
//import org.peakemu.objects.player.Player.traque;
//
//import org.peakemu.Ancestra;
//import org.peakemu.common.ConditionParser;
//import org.peakemu.common.Constants;
//import org.peakemu.common.Formulas;
//import org.peakemu.database.Database;
//import org.peakemu.common.SocketManager;
//import org.peakemu.world.World;
//
//
//import org.peakemu.game.GameServer;
//import org.peakemu.game.GameClient;
//import org.peakemu.game.out.ExchangeList;
//import org.peakemu.world.Area;
//import org.peakemu.world.MapCell;
//import org.peakemu.world.ItemTemplate;
//import org.peakemu.world.SubArea;
//import org.peakemu.world.enums.Effect;
//import org.peakemu.world.enums.PlayerRace;
//
//public class Action {
//
//	private int ID;
//	private String args;
//	private String cond;
//	
//	/*public Action(int id, String args, String cond)
//	{
//		this.ID = id;
//		this.args = args;
//		this.cond = cond;
//	}*/
//
//
//	public void apply(Player perso, Player target, int itemID, int cellid)
//	{
//		if(perso == null)return;
//		if((perso.get_curCarte().getId() == 1003 || perso.get_curCarte().getId() == 6164 || perso.get_curCarte().getId() == 6171) && perso.getAccount().get_gmLvl() < 1 && ID != 202) 
//			return;
//		if(!cond.equalsIgnoreCase("") && !cond.equalsIgnoreCase("-1")&& !ConditionParser.validConditions(perso,cond))
//		{
//			SocketManager.GAME_SEND_Im_PACKET(perso, "119");
//			return;
//		}
//		if(perso.getAccount().getGameThread() == null) return;
//		GameClient out = perso.getAccount().getGameThread();	
//		switch(ID)
//		{
//		case -22: // Remettre prisonnier
//			if(perso.get_followers() != "" && !perso.get_followers().isEmpty())
//			{
//				int skinFollower = Integer.parseInt(perso.get_followers());
//				int questId = Constants.getQuestByMobSkin(skinFollower);
//				GameServer.addToLog("associe a quete "  + questId);
//				if(questId != -1)
//				{
//					perso.upgradeQuest(questId);
//					perso.remove_follow();
//					int itemFollow = Constants.getItemByMobSkin(skinFollower);
//					perso.removeByTemplateID(itemFollow, 1);
//				}
//				//else
//					//SocketManager.GAME_SEND_MESSAGE(perso, "Vous n'avez aucun prisonnier a remettre !", "FF0000");
//			}
//			//else
//				//SocketManager.GAME_SEND_MESSAGE(perso, "Vous n'avez aucun prisonnier a remettre !", "FF0000");	
//		break;
//		case -21: // Canon de moon
//			// Casque en bois ou ailes en bois manquant
//			if(!perso.hasEquiped(1019) || !perso.hasEquiped(1021))
//			{
//				return;
//			}
//			SocketManager.GAME_SEND_GA_PACKET(perso.getAccount().getGameThread(), "", "2", perso.get_GUID()+"", "1");
//			perso.teleport((short)437, 411);
//		break;
//		case -20: // Ouverture / fermeture porte
//            //TODO
//			// Args = PorteCellid;cellMarchable,cellMarchable2...
////			try
////			{
////				Integer frameCellid = Integer.parseInt(args.split(";")[0]);
////				String cellsWalkable = args.split(";")[1];
////					
////				GameMap mapTarget = World.getCarte(CELLTARGET);
////				mapTarget.getCell(frameCellid).switchOn(cellsWalkable);
////			} catch(Exception e) { }
//			break;
//		case -10://Bonbon(30 combats)
//			int objBoost = Integer.parseInt(args);
//			int availablePos = Constants.getAvailableCandyPos(perso);
//			if (perso.hasEquiped(objBoost))
//				return;
//			else if(availablePos == -1)
//			{
//				return;
//			}
//			else 
//			{
//				//Item nuevo = World.getObjTemplate(objBoost).createNewItemByPos(1, availablePos, false);
//				//perso.addObjet(nuevo, false);
//                perso.getItems().moveItem(perso.getItems().get(itemID), availablePos, 1);
//				SocketManager.GAME_SEND_Ow_PACKET(perso);
//				SocketManager.GAME_SEND_STATS_PACKET(perso);
//				//perso.removeItem(itemID, 1, true, true);
//			}
////			Database.SAVE_PERSONNAGE(perso,true);
//		break;
//		case -2://cr�er guilde
//			if(perso.is_away())return;
//			if(perso.get_guild() != null || perso.getGuildMember() != null)
//			{
//				SocketManager.GAME_SEND_gC_PACKET(perso, "Ea");
//				return;
//			}
//			if(perso.hasItemTemplate(1575, 1)) {
//			SocketManager.GAME_SEND_gn_PACKET(perso);
//			perso.removeByTemplateID(1575,-1);
//			SocketManager.GAME_SEND_Im_PACKET(perso, "022;"+-1+"~"+1575);
//			} else {
//				SocketManager.GAME_SEND_Im_PACKET(perso, "14");
//				return;
//			}
//		break;
//		case -1://Ouvrir banque
//            //Sauvagarde du perso et des item avant.               
////            Database.SAVE_PERSONNAGE(perso,true);
//            if(perso.getDeshonor() >= 1)
//            {
//                    SocketManager.GAME_SEND_Im_PACKET(perso, "183");
//                    return;
//            }
//            Bank bank = perso.getAccount().getBank();
//            final int cost = bank.getCost();
//            if(cost > 0)
//            {
//
//                    final long playerKamas = perso.getKamas();
//                    final long kamasRemaining = playerKamas - cost;
//                    final long bankKamas = bank.getKamas();
//                    final long totalKamas = bankKamas+playerKamas;
//
//                    if (kamasRemaining < 0)//Si le joueur n'a pas assez de kamas SUR LUI pour ouvrir la banque
//                    {
//                            if(bankKamas >= cost)
//                            {
//                                    bank.setKamas(bankKamas-cost); //On modifie les kamas de la banque
//                            }
//                            else if(totalKamas >= cost)
//                            {
//                            		perso.setKamas(playerKamas - (cost - bankKamas));
//                            		bank.setKamas(bankKamas);
//                                    SocketManager.GAME_SEND_STATS_PACKET(perso);
//                                    SocketManager.GAME_SEND_Im_PACKET(perso, "020;"+playerKamas);
//                            }else
//                            {
//                                    SocketManager.GAME_SEND_MESSAGE_SERVER(perso, "10|"+cost);
//                                    return;
//                            }
//                    } else //Si le joueur a les kamas sur lui on lui retire directement
//                    {
//                            perso.setKamas(kamasRemaining);
//                            SocketManager.GAME_SEND_STATS_PACKET(perso);
//                            SocketManager.GAME_SEND_Im_PACKET(perso, "020;"+cost);
//                    }
//            }
//            SocketManager.GAME_SEND_ECK_PACKET(perso.getAccount().getGameThread(), 5, "");
//            perso.getAccount().getGameThread().send(ExchangeList.fromBank(bank));
//            perso.set_away(true);
//            perso.setCurExchange(bank.createExchange(perso));
//            break;
//			case 0://T�l�portation
//				try
//				{
//					short newMapID = Short.parseShort(args.split(",",2)[0]);
//					int newCellID = Integer.parseInt(args.split(",",2)[1]);
//					
//					perso.teleport(newMapID,newCellID);	
//				}catch(Exception e ){return;};
//			break;
//			
//			case 1://Discours NPC
//				out = perso.getAccount().getGameThread();
//				if(args.equalsIgnoreCase("DV"))
//				{
//					SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
//					perso.set_isTalkingWith(0);
//				}else
//				{
//					int qID = -1;
//					try
//					{
//						qID = Integer.parseInt(args);
//					}catch(NumberFormatException e){};
//					
//					NPC_question  quest = World.getNPCQuestion(qID, perso);
//					if(quest == null)
//					{
//						SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
//						perso.set_isTalkingWith(0);
//						return;
//					}
//					SocketManager.GAME_SEND_QUESTION_PACKET(out, quest.parseToDQPacket(perso));
//				}
//			break;
//			case 4://Kamas
//				try
//				{
//					int count = Integer.parseInt(args);
//					long curKamas = perso.getKamas();
//					long newKamas = curKamas + count;
//					if(newKamas <0) newKamas = 0;
//					perso.setKamas(newKamas);
//					
//					//Si en ligne (normalement oui)
//					if(perso.isOnline())
//						SocketManager.GAME_SEND_STATS_PACKET(perso);
//				}catch(Exception e){GameServer.addToLog(e.getMessage());};
//			break;
//			case 5://objet
//				try
//				{
//					int tID = Integer.parseInt(args.split(",")[0]);
//					int count = Integer.parseInt(args.split(",")[1]);
//					boolean send = true;
//					if(args.split(",").length >2)send = args.split(",")[2].equals("1");
//					
//					//Si on ajoute
//					if(count > 0)
//					{
//						ItemTemplate T = World.getObjTemplate(tID);
//						if(T == null)return;
//						Item O = T.createNewItem(count, false);
//						//Si retourne true, on l'ajoute au monde
//						perso.addObjet(O, true);
//					}else
//					{
//						perso.removeByTemplateID(tID,-count);
//					}
//					//Si en ligne (normalement oui)
//					if(perso.isOnline())//on envoie le packet qui indique l'ajout//retrait d'un item
//					{
//						SocketManager.GAME_SEND_Ow_PACKET(perso);
//						if(send)
//						{
//							if(count >= 0){
//								SocketManager.GAME_SEND_Im_PACKET(perso, "021;"+count+"~"+tID);
//							}
//							else if(count < 0){
//								SocketManager.GAME_SEND_Im_PACKET(perso, "022;"+-count+"~"+tID);
//							}
//						}
//					}
//				}catch(Exception e){GameServer.addToLog(e.getMessage());}
//			break;
//			
//			case 6://Apprendre un m�tier
//				try
//				{
//					int mID = Integer.parseInt(args);
//					if(World.getMetier(mID) == null)return;
//					// Si c'est un m�tier 'basic' :
//					if(mID == 	2 || mID == 11 ||
//					   mID == 13 || mID == 14 ||
//					   mID == 15 || mID == 16 ||
//					   mID == 17 || mID == 18 ||
//					   mID == 19 || mID == 20 ||
//					   mID == 24 || mID == 25 ||
//					   mID == 26 || mID == 27 ||
//					   mID == 28 || mID == 31 ||
//					   mID == 36 || mID == 41 ||
//					   mID == 56 || mID == 58 ||
//					   mID == 60 || mID == 65)
//					{
//						if(perso.getMetierByID(mID) != null) //M�tier d�j� appris
//						{
//							SocketManager.GAME_SEND_Im_PACKET(perso, "111");
//						}
//						
//						/**
//						 * if(perso.totalJobBasic() > 2)//On compte les m�tiers d�ja acquis si c'est sup�rieur a 2 on ignore
//						{
//							SocketManager.GAME_SEND_Im_PACKET(perso, "19");
//						}else//Si c'est < ou = � 2 on apprend
//						{
//							if(perso.hasAlljobUp()) 
//							{ //Si tout les autres m�tiers sont lvl 30 mini
//								perso.learnJob(World.getMetier(mID));
//							}else 
//							{
//								SocketManager.GAME_SEND_Im_PACKET(perso, "18;30");
//							}
//						}
//						 */
//						if(perso.totalJobBasic() == 0)
//						{
//							perso.learnJob(World.getMetier(mID));
//						}else
//						if(perso.totalJobBasic() > 2)//On compte les m�tiers d�ja acquis si c'est sup�rieur a 2 on ignore
//						{
//							SocketManager.GAME_SEND_Im_PACKET(perso, "19");
//						}
//						else
//							if(perso.totalJobBasic() <= 2 )//Si c'est < ou = � 2 on apprend
//						{
//							if(perso.hasAlljobUp()) 
//							{ //Si tout les autres m�tiers sont lvl 30 mini
//								perso.learnJob(World.getMetier(mID));
//							}else 
//							{
//								SocketManager.GAME_SEND_Im_PACKET(perso, "18;30");
//							}
//						}
//					}
//					// Si c'est une specialisations 'FM' :
//					if(mID == 	43 || mID == 44 ||
//					   mID == 45 || mID == 46 ||
//					   mID == 47 || mID == 48 ||
//					   mID == 49 || mID == 50 ||
//					   mID == 62 || mID == 63 ||
//					   mID == 64)
//					{
//						//M�tier simple level 65 n�cessaire
//						if(perso.getMetierByID(17) != null && perso.getMetierByID(17).get_lvl() >= 65 && mID == 43
//						|| perso.getMetierByID(11) != null && perso.getMetierByID(11).get_lvl() >= 65 && mID == 44
//						|| perso.getMetierByID(14) != null && perso.getMetierByID(14).get_lvl() >= 65 && mID == 45
//						|| perso.getMetierByID(20) != null && perso.getMetierByID(20).get_lvl() >= 65 && mID == 46
//						|| perso.getMetierByID(31) != null && perso.getMetierByID(31).get_lvl() >= 65 && mID == 47
//						|| perso.getMetierByID(13) != null && perso.getMetierByID(13).get_lvl() >= 65 && mID == 48
//						|| perso.getMetierByID(19) != null && perso.getMetierByID(19).get_lvl() >= 65 && mID == 49
//						|| perso.getMetierByID(18) != null && perso.getMetierByID(18).get_lvl() >= 65 && mID == 50
//						|| perso.getMetierByID(15) != null && perso.getMetierByID(15).get_lvl() >= 65 && mID == 62
//						|| perso.getMetierByID(16) != null && perso.getMetierByID(16).get_lvl() >= 65 && mID == 63
//						|| perso.getMetierByID(27) != null && perso.getMetierByID(27).get_lvl() >= 65 && mID == 64)
//						{
//							//On compte les specialisations d�ja acquis si c'est sup�rieur a 2 on ignore
//							if(perso.getMetierByID(mID) != null)//M�tier d�j� appris
//							{
//								SocketManager.GAME_SEND_Im_PACKET(perso, "111");
//							}
//							
//							if(perso.totalJobFM() > 2)//On compte les m�tiers d�ja acquis si c'est sup�rieur a 2 on ignore
//							{
//								SocketManager.GAME_SEND_Im_PACKET(perso, "19");
//							}
//							else//Si c'est < ou = � 2 on apprend
//							{
//								perso.learnJob(World.getMetier(mID));
//								perso.getMetierByID(mID).addXp(perso, 582000);//Level 100 direct
//							}	
//						}else
//						{
//							SocketManager.GAME_SEND_Im_PACKET(perso, "12");
//						}
//					}
//				}catch(Exception e){GameServer.addToLog(e.getMessage());};
//			break;
//			
//			case 7://retour au point de sauvegarde
//				perso.warpToSavePos();
//			break;
//			case 8://Ajouter une Stat
//				try
//				{
//					int statID = Integer.parseInt(args.split(",",2)[0]);
//					int number = Integer.parseInt(args.split(",",2)[1]);
//					perso.getBaseStats().addOneStat(Effect.valueOf(statID), number);
//					SocketManager.GAME_SEND_STATS_PACKET(perso);
//					if(statID == 123)//chance
//						SocketManager.GAME_SEND_Im_PACKET(perso, "011;"+number);
//					if(statID == 124)//sagesse
//						SocketManager.GAME_SEND_Im_PACKET(perso, "09;"+number);
//					if(statID == 125)//Vita
//						SocketManager.GAME_SEND_Im_PACKET(perso, "013;"+number);
//					if(statID == 126)//Intelligence
//						SocketManager.GAME_SEND_Im_PACKET(perso, "014;"+number);
//					if(statID == 118)//Force
//						SocketManager.GAME_SEND_Im_PACKET(perso, "010;"+number);
//					if(statID == 119)//Agilit�
//						SocketManager.GAME_SEND_Im_PACKET(perso, "012;"+number);
//				}catch(Exception e ){return;};
//			break;
//			case 9://Apprendre un sort
//				try
//				{
//					int sID = Integer.parseInt(args);
//					if(World.getSort(sID) == null)return;
//					perso.learnSpell(sID,1, true,true);
//				}catch(Exception e){GameServer.addToLog(e.getMessage());};
//			break;
//			case 10://Pain/potion/viande/poisson
//				try
//				{
//					int min = Integer.parseInt(args.split(",",2)[0]);
//					int max = Integer.parseInt(args.split(",",2)[1]);
//					if(max == 0) max = min;
//					int val = Formulas.getRandomValue(min, max);
//					if(target != null)
//					{
//						if(target.get_PDV() + val > target.get_PDVMAX())val = target.get_PDVMAX()-target.get_PDV();
//						target.set_PDV(target.get_PDV()+val);
//						SocketManager.GAME_SEND_STATS_PACKET(target);
//						SocketManager.GAME_SEND_Im_PACKET(target, "01;"+val);
//
//					}
//					else
//					{
//						if(perso.get_PDV() + val > perso.get_PDVMAX())val = perso.get_PDVMAX()-perso.get_PDV();
//						perso.set_PDV(perso.get_PDV()+val);
//						SocketManager.GAME_SEND_STATS_PACKET(perso);
//						SocketManager.GAME_SEND_Im_PACKET(perso, "01;"+val);
//					}
//				}catch(Exception e){GameServer.addToLog(e.getMessage());};
//			break;
//			case 11://Definir l'alignement
//				try
//				{
//					byte newAlign = Byte.parseByte(args.split(",",2)[0]);
//					boolean replace = Integer.parseInt(args.split(",",2)[1]) == 1;
//					//Si le perso n'est pas neutre, et qu'on doit pas remplacer, on passe
//					if(perso.get_align() != Constants.ALIGNEMENT_NEUTRE && !replace)return;
//					perso.modifAlignement(newAlign);
//				}catch(Exception e){GameServer.addToLog(e.getMessage());};
//			break;
//			case 12://Spawn d'un groupe de monstre
//				try
//				{
//					boolean delObj = args.split(",")[0].equals("true");
//					boolean inArena = args.split(",")[1].equals("true");
//
//					if(inArena && !World.isArenaMap(perso.get_curCarte().getId()))return;	//Si la map du personnage n'est pas class� comme �tant dans l'ar�ne
//
//					PierreAme pierrePleine = (PierreAme)perso.getItems().get(itemID);
//
//					String groupData = pierrePleine.parseGroupData();
//					String condition = "MiS = "+perso.get_GUID();	//Condition pour que le groupe ne soit lan�able que par le personnage qui � utiliser l'objet
//					perso.get_curCarte().spawnNewGroup(true, perso.get_curCell().getID(), groupData,condition);
//
//					if(delObj)
//						perso.removeItem(itemID, 1, true, true);
//				}catch(Exception e){GameServer.addToLog(e.getMessage());};
//			break;
//		    case 13: //Reset Carac
//		        try
//		        {
////		          perso.getBaseStats().addOneStat(125, -perso.getBaseStats().getEffect(125));
////		          perso.getBaseStats().addOneStat(124, -perso.getBaseStats().getEffect(124));
////		          perso.getBaseStats().addOneStat(118, -perso.getBaseStats().getEffect(118));
////		          perso.getBaseStats().addOneStat(123, -perso.getBaseStats().getEffect(123));
////		          perso.getBaseStats().addOneStat(119, -perso.getBaseStats().getEffect(119));
////		          perso.getBaseStats().addOneStat(126, -perso.getBaseStats().getEffect(126));
//		          perso.addCapital((perso.get_lvl() - 1) * 5 - perso.get_capital());
//
//		          SocketManager.GAME_SEND_STATS_PACKET(perso);
//		        }catch(Exception e){GameServer.addToLog(e.getMessage());};
//		    break;
//		    case 14://Ouvrir l'interface d'oublie de sort
//		    	perso.setisForgetingSpell(true);
//				SocketManager.GAME_SEND_FORGETSPELL_INTERFACE('+', perso);
//			break;
//			case 15://T�l�portation donjon
//				try
//				{
//					short newMapID = Short.parseShort(args.split(",")[0]);
//					int newCellID = Integer.parseInt(args.split(",")[1]);
//					int ObjetNeed = Integer.parseInt(args.split(",")[2]);
//					int MapNeed = Integer.parseInt(args.split(",")[3]);
//					if(ObjetNeed == 0)
//					{
//						//T�l�portation sans objets
//						perso.teleport(newMapID,newCellID);
//					}else if(ObjetNeed > 0)
//					{
//						if(MapNeed == 0)
//						{
//							//T�l�portation sans map
//							perso.teleport(newMapID,newCellID);
//						}else if(MapNeed > 0)
//						{
//							if (perso.hasItemTemplate(ObjetNeed, 1) && perso.get_curCarte().getId() == MapNeed)
//							{
//								//Le perso a l'item
//								//Le perso est sur la bonne map
//								//On t�l�porte, on supprime apr�s
//								perso.teleport(newMapID,newCellID);
//								perso.removeByTemplateID(ObjetNeed, 1);
//								SocketManager.GAME_SEND_Ow_PACKET(perso);
//								SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~"+ObjetNeed);
//							}
//							else if(perso.get_curCarte().getId() != MapNeed)
//							{
//								//Le perso n'est pas sur la bonne map
//								SocketManager.GAME_SEND_MESSAGE(perso, "Vous n'etes pas sur la bonne map du donjon pour etre teleporter.", "009900");
//							}
//							else
//							{
//								//Le perso ne poss�de pas l'item
//								SocketManager.GAME_SEND_MESSAGE(perso, "Vous ne possedez pas la clef necessaire.", "009900");
//							}
//						}
//					}
//				}catch(Exception e){GameServer.addToLog(e.getMessage());};
//			break;
//			case 16://Ajout d'honneur HonorValue
//				try
//				{
//					if(perso.get_align() != 0)
//					{
//						int AddHonor = Integer.parseInt(args);
//						int ActualHonor = perso.get_honor();
//						perso.set_honor(ActualHonor+AddHonor);
//					}
//				}catch(Exception e){GameServer.addToLog(e.getMessage());};
//			break;
//			
//			case 17://Xp m�tier JobID,XpValue
//				try
//				{
//					int JobID = Integer.parseInt(args.split(",")[0]);
//					int XpValue = Integer.parseInt(args.split(",")[1]);
//					if(perso.getMetierByID(JobID) != null)
//					{
//						perso.getMetierByID(JobID).addXp(perso, XpValue);
//					}
//				}catch(Exception e){GameServer.addToLog(e.getMessage());};
//			break;
//			
//			case 18://T�l�portation chez sois
//				if(House.AlreadyHaveHouse(perso))//Si il a une maison
//				{
//					Item obj = perso.getItems().get(itemID);
//					if (perso.hasItemTemplate(obj.getTemplate().getID(), 1))
//					{
//						perso.removeByTemplateID(obj.getTemplate().getID(),1);
//						House h = House.get_HouseByPerso(perso);
//						if(h == null) return;
//						perso.teleport((short)h.get_mapid(), h.get_caseid());
//					}if(perso.isOnMount())
//					{
//						//Si sur dinde, on envoie le packet et on le fait descendre
//						SocketManager.GAME_SEND_Im_PACKET(perso, "1118");
//						return;
//					}
//				}
//			break;
//			case 19://T�l�portation maison de guilde (ouverture du panneau de guilde)
//				SocketManager.GAME_SEND_GUILDHOUSE_PACKET(perso);
//			break;
//			case 20://+Points de sorts
//				try
//				{
//					int pts = Integer.parseInt(args);
//					if(pts < 1) return;
//					perso.addSpellPoint(pts);
//					SocketManager.GAME_SEND_STATS_PACKET(perso);
//					SocketManager.GAME_SEND_Im_PACKET(perso, "016;"+pts);
//				}catch(Exception e){GameServer.addToLog(e.getMessage());};
//			break;
//			case 21://+Energie
//				try
//				{
//					int Energy = Integer.parseInt(args);
//					if(Energy < 1) return;
//					
//					int EnergyTotal = perso.get_energy()+Energy;
//					if(EnergyTotal > 10000) EnergyTotal = 10000;
//					
//					
//					perso.set_energy(EnergyTotal);
//					SocketManager.GAME_SEND_STATS_PACKET(perso);
//					SocketManager.GAME_SEND_Im_PACKET(perso, "07;"+EnergyTotal);
//				}catch(Exception e){GameServer.addToLog(e.getMessage());};
//			break;
//			case 22://+Xp
//				try
//				{
//					long XpAdd = Integer.parseInt(args);
//					if(XpAdd < 1) return;
//					
//					long TotalXp = perso.get_curExp()+XpAdd;
//					perso.set_curExp(TotalXp);
//					SocketManager.GAME_SEND_STATS_PACKET(perso);
//				}catch(Exception e){GameServer.addToLog(e.getMessage());};
//			break;
//			//metier
//			
//			case 23://UnlearnJob
//				try
//				{
//					int Job = Integer.parseInt(args);
//					if(Job < 1) return;
//					StatsMetier m = perso.getMetierByID(Job);
//					if(m == null) return;
//					perso.unlearnJob(m.getID());
//					SocketManager.GAME_SEND_STATS_PACKET(perso);
////					Database.SAVE_PERSONNAGE(perso, false);
//				}catch(Exception e){GameServer.addToLog(e.getMessage());}
//			break;
//			
//			case 24://SimpleMorph
//				try
//				{
//					int morphID = Integer.parseInt(args);
//					if(morphID < 0)return;
//					perso.set_gfxID(morphID);
//					SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.get_curCarte(), perso.get_GUID());
//					SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(perso.get_curCarte(), perso);
//				}catch(Exception e){GameServer.addToLog(e.getMessage());};
//			break;
//			case 25://SimpleUnMorph
//				int UnMorphID = perso.getRace().ordinal()*10 + perso.get_sexe();
//				perso.set_gfxID(UnMorphID);
//				SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.get_curCarte(), perso.get_GUID());
//				SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(perso.get_curCarte(), perso);
//			break;
//			case 26://T�l�portation enclo de guilde (ouverture du panneau de guilde)
//				SocketManager.GAME_SEND_GUILDENCLO_PACKET(perso);
//			break;
//			case 27://startFigthVersusMonstres args : monsterID,monsterLevel| ...
//				String ValidMobGroup = "";
//				try
//		        {
//					for(String MobAndLevel : args.split("\\|"))
//					{
//						int monsterID = -1;
//						int monsterLevel = -1;
//						String[] MobOrLevel = MobAndLevel.split(",");
//						monsterID = Integer.parseInt(MobOrLevel[0]);
//						monsterLevel = Integer.parseInt(MobOrLevel[1]);
//						
//						if(World.getMonstre(monsterID) == null || World.getMonstre(monsterID).getGradeByLevel(monsterLevel) == null)
//						{
//							if(Ancestra.CONFIG_DEBUG) GameServer.addToLog("Monstre invalide : monsterID:"+monsterID+" monsterLevel:"+monsterLevel);
//							continue;
//						}
//						ValidMobGroup += monsterID+","+monsterLevel+","+monsterLevel+";";
//					}
//					if(ValidMobGroup.isEmpty()) return;
//					MobGroup group  = new MobGroup(perso.get_curCarte()._nextObjectID,perso.get_curCell().getID(),ValidMobGroup);
//					perso.get_curCarte().startFigthVersusMonstres(perso, group);
//		        }catch(Exception e){GameServer.addToLog(e.getMessage());}
//			break;
//			case 50://Traque
//				if(perso.get_traque() == null)
//				{
//					traque traq = new traque(0, null);
//					perso.set_traque(traq);
//				}
//				if(perso.get_lvl() < 50)
//				{
//					SocketManager.GAME_SEND_Im_PACKET(perso,"13");
//					return;
//				}
//				if(perso.get_traque().get_time() < System.currentTimeMillis() - 600000 || perso.get_traque().get_time() == 0)
//				{
//					Player tempP = null;
//					int tmp = 15;
//					int diff = 0;
//					for(GameClient GT : World.getInstance().getSessionHandler().getGameClients())
//					{
//					Player P = GT.getPlayer();
//					if(P == null || P == perso)continue;
//					if(P.getAccount().get_curIP().compareTo(perso.getAccount().get_curIP()) == 0)continue;
//					//SI pas s�riane ni neutre et si alignement oppos�
//					if(P.get_align() == perso.get_align() || P.get_align() == 0 || P.get_align() == 3)continue;
//					
//					if(P.get_lvl()>perso.get_lvl())diff = P.get_lvl() - perso.get_lvl();
//					if(perso.get_lvl()>P.get_lvl())diff = perso.get_lvl() - P.get_lvl();
//					if(diff<tmp)tempP = P; tmp = diff;
//					}
//					if(tempP == null)
//					{
//						SocketManager.GAME_SEND_MESSAGE(perso, "Nous n'avons pas trouve de cible a ta hauteur. Reviens plus tard." , "000000");
//						break;
//					}
//					
//					
//					SocketManager.GAME_SEND_MESSAGE(perso, "Vous etes desormais en chasse de "+tempP.get_name()+"." , "000000");
//					
//					perso.get_traque().set_traqued(tempP);
//					perso.get_traque().set_time(System.currentTimeMillis());
//					
//					
//					ItemTemplate T = World.getObjTemplate(10085);
//					if(T == null)return;
//					perso.removeByTemplateID(T.getID(),100);
//					
//					Item newObj = T.createNewItem(20, false);
//					//On ajoute le nom du type � recherch�
//					
//					int alignid = tempP.get_align();
//					String align = "";
//					switch(alignid)
//					{
//					case 0:
//					align = "Neutre";
//					case 1:
//					align = "Bontarien";
//					break;
//					case 2:
//					align = "Brakmarien";
//					break;
//					case 3:
//					align = "Seriane";
//					break;
//					}
//					// parcho de type:
//					//nom
//					//alignement
//					//grade
//					//Niveau
//					newObj.addTxtStat(Effect.NAME, tempP.get_name());
//					newObj.addTxtStat(Effect.ALIGN, align);
//					newObj.addTxtStat(Effect.GRADE, Integer.toString(tempP.getGrade()));
//					newObj.addTxtStat(Effect.LEVEL, Integer.toString(tempP.get_lvl()));
//					
//					//Si retourne true, on l'ajoute au monde
//					if(perso.addObjet(newObj, true)){
//			}else
//				perso.removeByTemplateID(T.getID(),20);
//
//			}
//			else{
//			SocketManager.GAME_SEND_MESSAGE(perso, "Thomas Sacre : Vous venez juste de signer un contrat, vous devez vous reposer." , "000000");
//				}
//
//			break;
//			case 100://Donner x abileter
//                Dragodinde mount = perso.getMount();
//                World.addDragodinde(
//                  new Dragodinde(
//                 mount.get_id(),
//                 mount.get_color(),
//                 mount.get_sexe(),
//                 mount.get_amour(),
//                 mount.get_endurance(),
//                 mount.get_level(),
//                 mount.get_exp(),
//                 mount.get_nom(),
//                 mount.get_fatigue(),
//                 mount.get_energie(),
//                 mount.get_reprod(),
//                 mount.get_maturite(),
//                 mount.get_serenite(),
//                 mount.parseDindeObjetsToDB(),
//                 mount.get_ancetres(),
//                 args));
//                 perso.setMount(World.getDragoByID(mount.get_id()));
//                 SocketManager.GAME_SEND_Re_PACKET(perso, "+", World.getDragoByID(mount.get_id()));
//                 Database.UPDATE_MOUNT_INFOS(mount);
//                 break;
//			case 51://Cible sur la g�oposition
//				String perr = "";
//				
//				perr = perso.getItems().get(itemID).getTraquedName();
//				if(perr == null)
//				{
//					break;	
//				}
//				Player cible = World.getPersoByName(perr);
//				if(cible==null)break;
//				if(!cible.isOnline())
//				{
//					SocketManager.GAME_SEND_MESSAGE(perso, "Ce joueur n'est pas connecte." , "000000");
//					break;
//				}
//				SocketManager.GAME_SEND_FLAG_PACKET(perso, cible);
//			break;
//			case 52://recompenser pour traque
//				if(perso.get_traque() != null && perso.get_traque().get_time() == -2)
//				{
//					int xp = Formulas.getTraqueXP(perso.get_lvl());
//					perso.addXp(xp);
//					perso.set_traque(null);//On supprime la traque
//					SocketManager.GAME_SEND_MESSAGE(perso, "Vous venez de recevoir "+xp+" points d'experiences." , "000000");
//				}
//				else
//				{
//					SocketManager.GAME_SEND_MESSAGE(perso, "Thomas Sacre : Reviens me voir quand tu aura abatu un ennemi." , "000000");
//				}
//
//			break;
//			case 101://Arriver sur case de mariage
//				if((perso.get_sexe() == 0 && perso.get_curCell().getID() == 282 && perso.hasEquiped(6660) && perso.hasEquiped(1501)) || (perso.get_sexe() == 1 && perso.get_curCell().getID() == 297 && perso.hasEquiped(6662) && perso.hasEquiped(1501)))
//				{
//					World.AddMarried(perso.get_sexe(), perso);
//				}else 
//				{
//					SocketManager.GAME_SEND_Im_PACKET(perso, "1102");
//					//SocketManager.GAME_SEND_MESSAGE(perso, "Erreur: vous devez avoir un chapeau de la mari�e et une alliance pour la mari�e. Un chapeau du mari� et une alliance sont requis pour le mari" , "000000");
//				}
//			break;
//			case 102://Marier des personnages
//				World.PriestRequest(perso, perso.get_curCarte(), perso.get_isTalkingWith());
//			break;
//			case 103://Divorce
//				if(perso.getKamas() < 50000)
//				{
//					return;
//				}else
//				{
//					perso.setKamas(perso.getKamas()-50000);
//					Player wife = World.getPersonnage(perso.getWife());
//					wife.Divorce();
//					perso.Divorce();
//				}
//			break;
//			case 34: // Set fullmorph
//				if (perso.get_fight() != null) return;
//				int morphId = Integer.parseInt(args);
//				if(morphId < 1) return;
//				
//				perso.setFullMorph(morphId);
//			break;	
//			case 35: // D�morph
//				if(perso.get_fight() != null) return;
//	   		 	   perso.unsetFullMorph();
//			break;
//			case 40: // Give quest
//				int questId = Integer.parseInt(args);
//				perso.addNewQuest(questId);
//			break;
//			case 41: // Confirm objective
//				String[] splitArgs = args.split("\\|");
//				perso.confirmObjective(Integer.parseInt(splitArgs[0]), splitArgs[1],"");
//			break;
//			case 42: // Monte prochaine �tape quete ou termine
//				int quest = Integer.parseInt(args);
//				perso.upgradeQuest(quest);
//			break;
//			case 228://F�es d'artifice(Fireworks)
//				try
//				{
//					int AnimationId = Integer.parseInt(args);
//					Fireworks animation = World.getFireworks(AnimationId);
//					if(perso.get_fight() != null) return;
//					perso.changeOrientation(1);
//					SocketManager.GAME_SEND_GA_PACKET_TO_MAP(perso.get_curCarte(), "0", 228, perso.get_GUID()+";"+cellid+","+Fireworks.PrepareToGA(animation), "");
//				}catch(Exception e){GameServer.addToLog(e.getMessage());};
//			break;
//			case 69: // tp incarnam => astrub 
//				SocketManager.GAME_SEND_GA_PACKET(perso.getAccount().getGameThread(), "", "2", perso.get_GUID()+"", "7");
//				if(perso.getRace() == PlayerRace.FECA)
//				{
//					perso.teleport((short)7398,(int)284);
//					perso.set_savePos("7398,284");
//				}
//				if(perso.getRace() == PlayerRace.OSAMODAS)
//				{
//					perso.teleport((short)7545,(int)297);
//					perso.set_savePos("7545,297");
//				}
//				if(perso.getRace() == PlayerRace.ENUTROF)
//				{
//					perso.teleport((short)7442,(int)255);
//					perso.set_savePos("7442,255");
//				}
//				if(perso.getRace() == PlayerRace.SRAM)
//				{
//					perso.teleport((short)7392,(int)282);
//					perso.set_savePos("7392,282");
//				}
//				if(perso.getRace() == PlayerRace.XELOR)
//				{
//					perso.teleport((short)7332,(int)312);
//					perso.set_savePos("7332,312");
//				}
//				if(perso.getRace() == PlayerRace.ECAFLIP)
//				{
//					perso.teleport((short)7446,(int)284);
//					perso.set_savePos("7446,284");
//				}
//				if(perso.getRace() == PlayerRace.ENIRIPSA)
//				{
//					perso.teleport((short)7361,(int)192);
//					perso.set_savePos("7361,192");
//				}
//				if(perso.getRace() == PlayerRace.IOP)
//				{
//					perso.teleport((short)7427,(int)267);
//					perso.set_savePos("7427,267");
//				}
//				if(perso.getRace() == PlayerRace.CRA)
//				{
//					perso.teleport((short)7378,(int)324);
//					perso.set_savePos("7378,324");
//				}
//				if(perso.getRace() == PlayerRace.SADIDA)
//				{
//					perso.teleport((short)7395,(int)357);
//					perso.set_savePos("7395,357");
//				}
//				if(perso.getRace() == PlayerRace.SACRIEUR)
//				{
//					perso.teleport((short)7336,(int)198);
//					perso.set_savePos("7336,198");
//				}
//				if(perso.getRace() == PlayerRace.PANDAWA)
//				{
//					perso.teleport((short)8035,(int)340);
//					perso.set_savePos("8035,340");
//				}
//				//perso.set_savePos();
//				//SQLManager.SAVE_PERSONNAGE(perso,true);
//				//SocketManager.GAME_SEND_GA_PACKET(perso.getAccount().getGameThread().get_out(), "", "2", perso.get_GUID()+"", "7");
//				SocketManager.GAME_SEND_Im_PACKET(perso, "06");
//				
//			break;
//			case 201:// Poser un Prisme
//				try {
//					int cellperso = perso.get_curCell().getID();
//					GameMap tCarte = perso.get_curCarte();
//					SubArea subarea = tCarte.getSubArea();
//					Area area = subarea.getArea();
//					int alignement = perso.get_align();
//					if (cellperso <= 0) {
//						break;
//					}
//					if(Ancestra.getWhenHasPosePrism().containsKey(1)){
//						{
//					
//							if(System.currentTimeMillis() - (Ancestra.getWhenHasPosePrism().get(1) + (Ancestra.PRISMES_DELAIS_NEW_POSE*1000*60)) < 0)
//						    {
//					        
//					            break;
//					        }else
//							{
//					        	Ancestra.getWhenHasPosePrism().put(1, (long)-1);//on reinitialise
//					        	Ancestra.setPosePrism(0);
//							}
//						}
//					}
//					if (alignement == 0 || alignement == 3) {
//						
//						break;
//					}
//					if (!perso.is_showWings()) {
//						
//						break;
//					}
//					if (Ancestra.CartesWithoutPrismes != null && Ancestra.CartesWithoutPrismes.contains(Integer.parseInt(String.valueOf(tCarte.getId())))) {
//						
//						break;
//					}
//					if ((subarea.getAlignement() != 0 && subarea.getAlignement() != -1) || !subarea.getConquistable()) {
//						
//						break;
//					}
//						
//					Prism Prisme = new Prism(World.getNextIDPrisme(), alignement, 1, tCarte.getId(), cellperso, 0, -1);
//					subarea.setAlignement(alignement);
//					subarea.setPrismeID(Prisme.getID());
//					for (Player z : World.getOnlinePersos()) {
//						if (z == null)
//							continue;
//						if (z.get_align() == 0) {
//							SocketManager.GAME_SEND_am_ALIGN_PACKET_TO_SUBAREA(z, subarea.getId() + "|" + alignement + "|1");
//							if (area.getalignement() == 0)
//								SocketManager.GAME_SEND_aM_ALIGN_PACKET_TO_AREA(z, area.getId() + "|" + alignement);
//							continue;
//						}
//						SocketManager.GAME_SEND_am_ALIGN_PACKET_TO_SUBAREA(z, subarea.getId() + "|" + alignement + "|0");
//						if (area.getalignement() == 0)
//							SocketManager.GAME_SEND_aM_ALIGN_PACKET_TO_AREA(z, area.getId() + "|" + alignement);
//					}
//					if (area.getalignement() == 0) {
//						area.setPrismeID(Prisme.getID());
//						area.setalignement(alignement);
//						Prisme.setAreaConquest(area.getId());
//					}
//					World.addPrisme(Prisme);
//					Database.ADD_PRISME(Prisme);
//					perso.removeItem(8990);
//					
//					Ancestra.setPosePrism(Ancestra.getPosePrism()+1);
//					if (Ancestra.getPosePrism() >= 4)
//						Ancestra.getWhenHasPosePrism().put(1, System.currentTimeMillis());
//					SocketManager.GAME_SEND_PRISME_TO_MAP(tCarte, Prisme);
//				} catch (Exception e) {}
//				break;
//			case 202: //quitter la prison en payant 10'000k
//				long KamasInitial = perso.getKamas();
//				long taxe = KamasInitial-10000;
//				if(perso.getKamas() < 10000)
//					return;
//				if(perso.get_align() == 1)
//				{
//					perso.setKamas(taxe);
//					perso.teleport((short)6158,282);
//				}
//				if(perso.get_align() == 2)
//				{
//					perso.setKamas(taxe);
//					perso.teleport((short)6167,240);
//				}
//				SocketManager.GAME_SEND_STATS_PACKET(perso);
//			break;
//			/*case 99: //Reset Carac
//		        try
//		        {
//		          perso.getBaseStats().addOneStat(125, -perso.baseStats.getEffect(125));
//		          perso.getBaseStats().addOneStat(124, -perso.baseStats.getEffect(124));
//		          perso.getBaseStats().addOneStat(118, -perso.baseStats.getEffect(118));
//		          perso.getBaseStats().addOneStat(123, -perso.baseStats.getEffect(123));
//		          perso.getBaseStats().addOneStat(119, -perso.baseStats.getEffect(119));
//		          perso.getBaseStats().addOneStat(126, -perso.baseStats.getEffect(126));
//		          perso.addCapital((perso.get_lvl() - 1) * 5 - perso.get_capital());
//
//		          SocketManager.GAME_SEND_STATS_PACKET(perso);
//		        }catch(Exception e){GameServer.addToLog(e.getMessage());};
//		    break;*/
//			case 99: //Reset Carac
//		          try
//		          {
////		            perso.getBaseStats().addOneStat(125, -perso.getBaseStats().getEffect(125));
////		            perso.getBaseStats().addOneStat(124, -perso.getBaseStats().getEffect(124));
////		            perso.getBaseStats().addOneStat(118, -perso.getBaseStats().getEffect(118));
////		            perso.getBaseStats().addOneStat(123, -perso.getBaseStats().getEffect(123));
////		            perso.getBaseStats().addOneStat(119, -perso.getBaseStats().getEffect(119));
////		            perso.getBaseStats().addOneStat(126, -perso.getBaseStats().getEffect(126));
//		            perso.addCapital((perso.get_lvl() - 1) * 5 - perso.get_capital());
//
//		            SocketManager.GAME_SEND_STATS_PACKET(perso);
//		          }catch(Exception e){GameServer.addToLog(e.getMessage());};
//		          perso.setisForgetingSpell(true);
//		    SocketManager.GAME_SEND_FORGETSPELL_INTERFACE('+', perso);
//		      break;
//			default:
//				GameServer.addToLog("Action ID="+ID+" non implantee");
//			break;
//		
//		}
//	}
//
//
//	public int getID()
//	{
//		return ID;
//	}
//}
