package org.peakemu.common;

import org.peakemu.Ancestra;
import org.peakemu.game.GameServer;
import org.peakemu.objects.player.Player;

import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;
import org.peakemu.world.enums.Effect;

public class ConditionParser {
	public static boolean validConditions(Player perso, String req) {
		if (req == null || req.isEmpty() || req.equals("-1"))
			return true;
        
		if (req.contains("BI"))
			return false;

		Jep jep = new Jep();

		req = req.replace("&", "&&").replace("=", "==").replace("|", "||")
				.replace("!", "!=").replace("~", "==");

		if (req.contains("PO"))
			req = havePO(req, perso);
		if (req.contains("PN"))
			req = canPN(req, perso);
//		if (req.contains("QE"))
//			req = haveQE(req, perso);
		// TODO : G�rer PJ Pj
		try {
			// Stats stuff compris
			jep.addVariable("CI",
					perso.getTotalStats().getEffect(Effect.ADD_INTE));
			jep.addVariable("CV",
					perso.getTotalStats().getEffect(Effect.ADD_VITA));
			jep.addVariable("CA",
					perso.getTotalStats().getEffect(Effect.ADD_AGIL));
			jep.addVariable("CW",
					perso.getTotalStats().getEffect(Effect.ADD_SAGE));
			jep.addVariable("CC",
					perso.getTotalStats().getEffect(Effect.ADD_CHAN));
			jep.addVariable("CS",
					perso.getTotalStats().getEffect(Effect.ADD_FORC));
			// Stats de bases
			jep.addVariable("Ci",
					perso.getBaseStats().getEffect(Effect.ADD_INTE));
			jep.addVariable("Cs",
					perso.getBaseStats().getEffect(Effect.ADD_FORC));
			jep.addVariable("Cv",
					perso.getBaseStats().getEffect(Effect.ADD_VITA));
			jep.addVariable("Ca",
					perso.getBaseStats().getEffect(Effect.ADD_AGIL));
			jep.addVariable("Cw",
					perso.getBaseStats().getEffect(Effect.ADD_SAGE));
			jep.addVariable("Cc",
					perso.getBaseStats().getEffect(Effect.ADD_CHAN));
			// Autre
			jep.addVariable("Ps", perso.getAlignement());
			jep.addVariable("Pa", perso.getAlignLevel());
			jep.addVariable("PP", perso.getGrade());
			jep.addVariable("PL", perso.getLevel());
			jep.addVariable("PK", perso.getKamas());
			jep.addVariable("PG", perso.getRace());
			jep.addVariable("PS", perso.getGender());
			jep.addVariable("PZ", 1);// Abonnement
			jep.addVariable("PX", perso.getAccount().get_gmLvl());
			jep.addVariable("PW", perso.getMaxPod());
			jep.addVariable("PB", perso.getMap().getSubArea().getId());
//			jep.addVariable("PR", (perso.getWife() > 0 ? 1 : 0));
			jep.addVariable("SI", perso.getMap().getId());
			// Les pierres d'ames sont lancables uniquement par le lanceur.
			jep.addVariable("MiS", perso.getSpriteId());
			//jep.addVariable("Bs", World.getSubArea(76).getAlignement()); // Alignement village brigandin
			jep.parse(req);
			Object result = jep.evaluate();
			boolean ok = false;
			if (result != null)
				ok = Boolean.valueOf(result.toString());
			
			return ok;
		} catch (JepException e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
		return true;
	}

	public static String havePO(String cond, Player perso)// On remplace les
																// PO par leurs
																// valeurs si
																// possession de
																// l'item
	{
		boolean Jump = false;
		boolean ContainsPO = false;
		boolean CutFinalLenght = true;
		String copyCond = "";
		int finalLength = 0;

		if (Ancestra.CONFIG_DEBUG)
			GameServer.addToLog("Entered Cond : " + cond);

		if (cond.contains("&&")) {
			for (String cur : cond.split("&&")) {
				if (cond.contains("==")) {
					for (String cur2 : cur.split("==")) {
						if (cur2.contains("PO")) {
							ContainsPO = true;
							continue;
						}
						
						int q = 1;
						if(cur2.contains(";")) // Quantité spécifiée
						{
							q = Integer.parseInt(cur2.split(";")[1]);
							cur2 = cur2.split(";")[0];
						}
						
						if (Jump) {
							copyCond += cur2;
							Jump = false;
							continue;
						}
						if (!cur2.contains("PO") && !ContainsPO) {
							copyCond += cur2 + "==";
							Jump = true;
							continue;
						}
						if (cur2.contains("!="))
							continue;
						ContainsPO = false;
						if (perso.hasItemTemplate(Integer.parseInt(cur2), q)) {
							copyCond += Integer.parseInt(cur2) + "=="
									+ Integer.parseInt(cur2);
						} else {
							copyCond += Integer.parseInt(cur2) + "==" + 0;
						}
					}
				}
				if (cond.contains("!=")) {
					for (String cur2 : cur.split("!=")) {
						if (cur2.contains("PO")) {
							ContainsPO = true;
							continue;
						}
						int q = 1;
						if(cur2.contains(";")) // Quantité spécifiée
						{
							q = Integer.parseInt(cur2.split(";")[1]);
							cur2 = cur2.split(";")[0];
						}
						
						if (Jump) {
							copyCond += cur2;
							Jump = false;
							continue;
						}
						if (!cur2.contains("PO") && !ContainsPO) {
							copyCond += cur2 + "!=";
							Jump = true;
							continue;
						}
						if (cur2.contains("=="))
							continue;
						ContainsPO = false;
						if (perso.hasItemTemplate(Integer.parseInt(cur2), q)) {
							copyCond += Integer.parseInt(cur2) + "!="
									+ Integer.parseInt(cur2);
						} else {
							copyCond += Integer.parseInt(cur2) + "!=" + 0;
						}
					}
				}
				copyCond += "&&";
			}
		} else if (cond.contains("||")) {
			for (String cur : cond.split("\\|\\|")) {
				if (cond.contains("==")) {
					for (String cur2 : cur.split("==")) {
						if (cur2.contains("PO")) {
							ContainsPO = true;
							continue;
						}
						int q = 1;
						if(cur2.contains(";")) // Quantité spécifiée
						{
							q = Integer.parseInt(cur2.split(";")[1]);
							cur2 = cur2.split(";")[0];
						}
						
						if (Jump) {
							copyCond += cur2;
							Jump = false;
							continue;
						}
						if (!cur2.contains("PO") && !ContainsPO) {
							copyCond += cur2 + "==";
							Jump = true;
							continue;
						}
						if (cur2.contains("!="))
							continue;
						ContainsPO = false;
						if (perso.hasItemTemplate(Integer.parseInt(cur2), q)) {
							copyCond += Integer.parseInt(cur2) + "=="
									+ Integer.parseInt(cur2);
						} else {
							copyCond += Integer.parseInt(cur2) + "==" + 0;
						}
					}
				}
				if (cond.contains("!=")) {
					for (String cur2 : cur.split("!=")) {
						if (cur2.contains("PO")) {
							ContainsPO = true;
							continue;
						}
						int q = 1;
						if(cur2.contains(";")) // Quantité spécifiée
						{
							q = Integer.parseInt(cur2.split(";")[1]);
							cur2 = cur2.split(";")[0];
						}
						
						if (Jump) {
							copyCond += cur2;
							Jump = false;
							continue;
						}
						if (!cur2.contains("PO") && !ContainsPO) {
							copyCond += cur2 + "!=";
							Jump = true;
							continue;
						}
						if (cur2.contains("=="))
							continue;
						ContainsPO = false;
						if (perso.hasItemTemplate(Integer.parseInt(cur2), q)) {
							copyCond += Integer.parseInt(cur2) + "!="
									+ Integer.parseInt(cur2);
						} else {
							copyCond += Integer.parseInt(cur2) + "!=" + 0;
						}
					}
				}
				copyCond += "||";
			}
		} else {
			CutFinalLenght = false;
			if (cond.contains("==")) {
				for (String cur : cond.split("==")) {
					if (cur.contains("PO")) {
						continue;
					}
					if (cur.contains("!="))
						continue;
					int q = 1;
					if(cur.contains(";")) // Quantité spécifiée
					{
						q = Integer.parseInt(cur.split(";")[1]);
						cur = cur.split(";")[0];
					}
					if (perso.hasItemTemplate(Integer.parseInt(cur), q)) {
						copyCond += Integer.parseInt(cur) + "=="
								+ Integer.parseInt(cur);
					} else {
						copyCond += Integer.parseInt(cur) + "==" + 0;
					}
				}
			}
			if (cond.contains("!=")) {
				for (String cur : cond.split("!=")) {
					if (cur.contains("PO")) {
						continue;
					}
					if (cur.contains("=="))
						continue;
					int q = 1;
					if(cur.contains(";")) // Quantité spécifiée
					{
						q = Integer.parseInt(cur.split(";")[1]);
						cur = cur.split(";")[0];
					}
					
					if (perso.hasItemTemplate(Integer.parseInt(cur), q)) {
						copyCond += Integer.parseInt(cur) + "!="
								+ Integer.parseInt(cur);
					} else {
						copyCond += Integer.parseInt(cur) + "!=" + 0;
					}
				}
			}
		}
		if (CutFinalLenght) {
			finalLength = (copyCond.length() - 2);// On retire les deux derniers
													// carract�res (|| ou &&)
			copyCond = copyCond.substring(0, finalLength);
		}
		if (Ancestra.CONFIG_DEBUG)
			GameServer.addToLog("Returned Cond : " + copyCond);
		return copyCond;
	}

//public static String haveQE(String cond, Player perso) // Condition quete
//{
//	boolean Jump = false;
//	boolean ContainsQE = false;
//	boolean CutFinalLenght = true;
//	String copyCond = "";
//	int finalLength = 0;
//	
//	if (Ancestra.CONFIG_DEBUG)
//		GameServer.addToLog("Entered Cond : " + cond);
//
//	if (cond.contains("&&")) {
//			for (String cur : cond.split("&&")) {
//				if (cond.contains("==")) {
//					for (String cur2 : cur.split("==")) {
//						if (cur2.contains("QE")) {
//							ContainsQE = true;
//							continue;
//						}
//					
//						int step = -1;
//						if(cur2.contains(";")) // Etape spécifiée
//						{
//							step = Integer.parseInt(cur2.split(";")[1]);
//							cur2 = cur2.split(";")[0];
//						}
//					
//						if (Jump) {
//							copyCond += cur2;
//							Jump = false;
//							continue;
//						}
//						if (!cur2.contains("QE") && !ContainsQE) {
//							copyCond += cur2 + "==";
//							Jump = true;
//							continue;
//						}
//						if (cur2.contains("!="))
//							continue;
//						ContainsQE = false;
//						if (perso.hasQuest(Integer.parseInt(cur2), step)) {
//							copyCond += Integer.parseInt(cur2) + "=="
//									+ Integer.parseInt(cur2);
//						} else {
//							copyCond += Integer.parseInt(cur2) + "==" + 0;
//						}
//					}
//			}
//			if (cond.contains("!=")) {
//					for (String cur2 : cur.split("!=")) {
//						if (cur2.contains("QE")) {
//							ContainsQE = true;
//							continue;
//						}
//						int step = 1;
//						if(cur2.contains(";")) // Etape spécifiée
//						{
//							step = Integer.parseInt(cur2.split(";")[1]);
//							cur2 = cur2.split(";")[0];
//						}
//					
//						if (Jump) {
//							copyCond += cur2;
//							Jump = false;
//							continue;
//						}
//						if (!cur2.contains("QE") && !ContainsQE) {
//							copyCond += cur2 + "!=";
//							Jump = true;
//							continue;
//						}
//						if (cur2.contains("=="))
//							continue;
//						ContainsQE = false;
//						if (perso.hasQuest(Integer.parseInt(cur2), step)) {
//							copyCond += Integer.parseInt(cur2) + "!="
//									+ Integer.parseInt(cur2);
//						} else {
//							copyCond += Integer.parseInt(cur2) + "!=" + 0;
//						}
//					}
//			}
//			copyCond += "&&";
//		}
//	} else if (cond.contains("||")) {
//		for (String cur : cond.split("\\|\\|")) {
//			if (cond.contains("==")) {
//				for (String cur2 : cur.split("==")) {
//					if (cur2.contains("QE")) {
//						ContainsQE = true;
//						continue;
//					}
//					int step = 1;
//					if(cur2.contains(";")) // Etape spécifiée
//					{
//						step = Integer.parseInt(cur2.split(";")[1]);
//						cur2 = cur2.split(";")[0];
//					}
//					
//					if (Jump) {
//						copyCond += cur2;
//						Jump = false;
//						continue;
//					}
//					if (!cur2.contains("QE") && !ContainsQE) {
//						copyCond += cur2 + "==";
//						Jump = true;
//						continue;
//					}
//					if (cur2.contains("!="))
//						continue;
//					ContainsQE = false;
//					if (perso.hasQuest(Integer.parseInt(cur2), step)) {
//						copyCond += Integer.parseInt(cur2) + "=="
//								+ Integer.parseInt(cur2);
//					} else {
//						copyCond += Integer.parseInt(cur2) + "==" + 0;
//					}
//				}
//			}
//			if (cond.contains("!=")) {
//				for (String cur2 : cur.split("!=")) {
//					if (cur2.contains("QE")) {
//						ContainsQE = true;
//						continue;
//					}
//					int step = 1;
//					if(cur2.contains(";")) // Etape spécifiée
//					{
//						step = Integer.parseInt(cur2.split(";")[1]);
//						cur2 = cur2.split(";")[0];
//					}
//					
//					if (Jump) {
//						copyCond += cur2;
//						Jump = false;
//						continue;
//					}
//					if (!cur2.contains("QE") && !ContainsQE) {
//						copyCond += cur2 + "!=";
//						Jump = true;
//						continue;
//					}
//					if (cur2.contains("=="))
//						continue;
//					ContainsQE = false;
//					if (perso.hasQuest(Integer.parseInt(cur2), step)) {
//						copyCond += Integer.parseInt(cur2) + "!="
//								+ Integer.parseInt(cur2);
//					} else {
//						copyCond += Integer.parseInt(cur2) + "!=" + 0;
//					}
//				}
//			}
//			copyCond += "||";
//		}
//	} else {
//		CutFinalLenght = false;
//		if (cond.contains("==")) {
//			for (String cur : cond.split("==")) {
//				if (cur.contains("QE")) {
//					continue;
//				}
//				if (cur.contains("!="))
//					continue;
//				int step = 1;
//				if(cur.contains(";")) // Etape spécifiée
//				{
//					step = Integer.parseInt(cur.split(";")[1]);
//					cur = cur.split(";")[0];
//				}
//				if (perso.hasQuest(Integer.parseInt(cur), step)) {
//					copyCond += Integer.parseInt(cur) + "=="
//							+ Integer.parseInt(cur);
//				} else {
//					copyCond += Integer.parseInt(cur) + "==" + 0;
//				}
//			}
//		}
//		if (cond.contains("!=")) {
//			for (String cur : cond.split("!=")) {
//				if (cur.contains("QE")) {
//					continue;
//				}
//				if (cur.contains("=="))
//					continue;
//				int step = 1;
//				if(cur.contains(";")) // Etape spécifiée
//				{
//					step = Integer.parseInt(cur.split(";")[1]);
//					cur = cur.split(";")[0];
//				}
//				
//				if (perso.hasQuest(Integer.parseInt(cur), step)) {
//					copyCond += Integer.parseInt(cur) + "!="
//							+ Integer.parseInt(cur);
//				} else {
//					copyCond += Integer.parseInt(cur) + "!=" + 0;
//				}
//			}
//		}
//	}
//	if (CutFinalLenght) {
//		finalLength = (copyCond.length() - 2);// On retire les deux derniers
//		// carract�res (|| ou &&)
//		copyCond = copyCond.substring(0, finalLength);
//	}
//	if (Ancestra.CONFIG_DEBUG)
//		GameServer.addToLog("Returned Cond : " + copyCond);
//	return copyCond;
//}

	public static String canPN(String cond, Player perso)// On remplace le
																// PN par 1 et
																// si le nom
																// correspond ==
																// 1 sinon == 0
	{
		String copyCond = "";
		for (String cur : cond.split("==")) {
			if (cur.contains("PN")) {
				copyCond += "1==";
				continue;
			}
			if (perso.getName().toLowerCase().compareTo(cur) == 0) {
				copyCond += "1";
			} else {
				copyCond += "0";
			}
		}
		return copyCond;
	}
}
