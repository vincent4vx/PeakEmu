package org.peakemu.maputil;

import org.peakemu.world.GameMap;
import org.peakemu.world.fight.Fight;
import org.peakemu.game.GameServer;

import java.util.ArrayList;
import org.peakemu.common.Constants;
import org.peakemu.common.CryptManager;

import org.peakemu.world.MapCell;
import org.peakemu.world.fight.fighter.Fighter;

public class OldPathfinding {
	
	public static ArrayList<Fighter> getEnemyFighterArround(int cellID,GameMap map,Fight fight)
	{
		char[] dirs = {'b','d','f','h'};
		ArrayList<Fighter> enemy = new ArrayList<Fighter>();
		
		for(char dir : dirs)
		{
			Fighter f = map.getCell(GetCaseIDFromDirrection(cellID, dir, map, false)).getFirstFighter();
			if(f != null)
			{
				if(f.getTeam() != fight.getCurFighter().getTeam())
					enemy.add(f);
			}
		}
		if(enemy.size() == 0 || enemy.size() == 4) 
			return null;
		
		return enemy;
	}

	public static boolean isNextTo (int cell1, int cell2)
	{
		if(cell1 + 14 == cell2 || cell1 + 15 == cell2 || cell1 - 14 == cell2 || cell1 -15 ==cell2)
			return true;
		else
			return false;
	}

	public static int GetCaseIDFromDirrection(int CaseID, char Direction,GameMap map, boolean Combat)
	{
		switch (Direction)
        {
            case 'a':
                return Combat ? -1 : CaseID + 1;
            case 'b':
                return CaseID + map.get_w();
            case 'c':
                return Combat ? -1 : CaseID + (map.get_w() * 2 - 1);
            case 'd':
                return  CaseID + (map.get_w() - 1);
            case 'e':
                return Combat ? -1 : CaseID - 1;
            case 'f':
                return CaseID - map.get_w();
            case 'g':
                return Combat ? -1 : CaseID - (map.get_w() * 2 - 1);
            case 'h':
                return  CaseID - map.get_w() + 1;
        }
        return -1; 
	}
	
	public static int getDistanceBetween(GameMap map,int id1,int id2)
	{
		if(id1 == id2)return 0;
		if(map == null)return 0;
		int diffX = Math.abs(getCellXCoord(map, id1) - getCellXCoord(map,id2));
		int diffY = Math.abs(getCellYCoord(map, id1) - getCellYCoord(map,id2));
		return (diffX + diffY);
	}
	
	public static int newCaseAfterPush(GameMap map, MapCell CCase,MapCell TCase, int value)
	{
		//Si c'est les memes case, il n'y a pas a bouger
		if(CCase.getID() == TCase.getID())return 0;
		char c = getDirBetweenTwoCase(CCase.getID(), TCase.getID(), map, true);
		int id = TCase.getID();
		if(value <0)
		{
			c = getOpositeDirection(c);
			value = -value;
		}
		for(int a = 0; a<value;a++)
		{
			int nextCase = GetCaseIDFromDirrection(id, c, map, true);
			if(map.getCell(nextCase) != null && map.getCell(nextCase).isWalkable(true) && map.getCell(nextCase).getFighters().isEmpty())
				id = nextCase;
			else
				return -(value-a);
		}
		
		if(id == TCase.getID())
			id = 0;
		return id;
	}
	
	private static char getOpositeDirection(char c)
	{
		switch(c)
		{
			case 'a':
				return 'e';
			case 'b':
				return 'f';
			case 'c':
				return 'g';
			case 'd':
				return 'h';
			case 'e':
				return 'a';
			case 'f':
				return 'b';
			case 'g':
				return 'c';
			case 'h':
				return 'd';
		}
		return 0x00;
	}

	public static boolean casesAreInSameLine(GameMap map,int c1,int c2,char dir)
	{
		if(c1 == c2)
			return true;
		
		if(dir != 'z')//Si la direction est définie
		{
			for(int a = 0;a<70;a++)
			{
				if(GetCaseIDFromDirrection(c1, dir, map, true) == c2)
					return true;
				if(GetCaseIDFromDirrection(c1, dir, map, true) == -1)
					break;
				c1 = GetCaseIDFromDirrection(c1, dir, map, true);
			}
		}else//Si on doit chercher dans toutes les directions
		{
			char[] dirs = {'b','d','f','h'};
			for(char d : dirs)
			{
				int c = c1;
				for(int a = 0;a<70;a++)
				{
					if(GetCaseIDFromDirrection(c, d, map, true) == c2)
						return true;
					c = GetCaseIDFromDirrection(c, d, map, true);
				}
			}
		}
		return false;
	}

	public static ArrayList<Fighter> getCiblesByZoneByWeapon(Fight fight,int type,MapCell cell,int castCellID)
	{
		ArrayList<Fighter> cibles = new ArrayList<Fighter>();
		char c = getDirBetweenTwoCase(castCellID,cell.getID(),fight.get_map(),true);
		if(c == 0)
		{
			//On cible quand meme le fighter sur la case
			if(cell.getFirstFighter() != null) cibles.add(cell.getFirstFighter());
			return cibles;
		}
		
		switch(type)
		{
			//Cases devant celle ou l'on vise
			case Constants.ITEM_TYPE_MARTEAU:
				Fighter f = getFighter2CellBefore(castCellID,c,fight.get_map());
				if(f != null)
					cibles.add(f);
				Fighter g = get1StFighterOnCellFromDirection(fight.get_map(),castCellID,(char)(c-1)); 
				if(g != null)
					cibles.add(g);//Ajoute case a gauche
				Fighter h = get1StFighterOnCellFromDirection(fight.get_map(),castCellID,(char)(c+1)); 
				if(h != null)
					cibles.add(h);//Ajoute case a droite
				Fighter i = cell.getFirstFighter();
				if(i != null)
					cibles.add(i);
			break;
			case Constants.ITEM_TYPE_BATON:
				Fighter j = get1StFighterOnCellFromDirection(fight.get_map(),castCellID,(char)(c-1)); 
				if(j != null)
					cibles.add(j);//Ajoute case a gauche
				Fighter k = get1StFighterOnCellFromDirection(fight.get_map(),castCellID,(char)(c+1)); 
				if(k != null)
					cibles.add(k);//Ajoute case a droite
				
				Fighter l = cell.getFirstFighter();
				if(l != null)
					cibles.add(l);//Ajoute case cible
			break;
			case Constants.ITEM_TYPE_PIOCHE:
			case Constants.ITEM_TYPE_EPEE:
			case Constants.ITEM_TYPE_FAUX:
			case Constants.ITEM_TYPE_DAGUES:
			case Constants.ITEM_TYPE_BAGUETTE:
			case Constants.ITEM_TYPE_PELLE:
			case Constants.ITEM_TYPE_ARC:
			case Constants.ITEM_TYPE_HACHE:
				Fighter m = cell.getFirstFighter();
				if(m != null)
					cibles.add(m);
			break;	
		}
		return cibles;
	}

	private static Fighter get1StFighterOnCellFromDirection(GameMap map, int id, char c)
	{ 
		if(c == (char)('a'-1))
			c = 'h';
		if(c == (char)('h'+1))
			c = 'a';
		return map.getCell(GetCaseIDFromDirrection(id,c,map,false)).getFirstFighter();
	}

	private static Fighter getFighter2CellBefore(int CellID, char c,GameMap map)
	{
		int new2CellID = GetCaseIDFromDirrection(GetCaseIDFromDirrection(CellID,c,map,false),c,map,false);
		return map.getCell(new2CellID).getFirstFighter();
	}

	public static char getDirBetweenTwoCase(int cell1ID, int cell2ID,GameMap map, boolean Combat)
	{
		ArrayList<Character> dirs = new ArrayList<Character>();
		dirs.add('b');
		dirs.add('d');
		dirs.add('f');
		dirs.add('h');
		if(!Combat)
		{
			dirs.add('a');
			dirs.add('b');
			dirs.add('c');
			dirs.add('d');
		}
		for(char c : dirs)
		{
			int cell = cell1ID;
			for(int i = 0; i <= 64; i++)
			{
				if(GetCaseIDFromDirrection(cell, c, map, Combat) == cell2ID)
					return c;
				cell = GetCaseIDFromDirrection(cell, c, map, Combat);
			}
		}
		return 0;
	}

	public static ArrayList<MapCell> getCellListFromAreaString(GameMap map,int cellID,int castCellID, String zoneStr, int PONum, boolean isCC)
	{
		ArrayList<MapCell> cases = new ArrayList<>();
		int c = PONum;
		if(map.getCell(cellID) == null)return cases;
		cases.add(map.getCell(cellID));
		
		int taille = CryptManager.getIntByHashedValue(zoneStr.charAt(c+1));
		switch(zoneStr.charAt(c))
		{
			case 'C'://Cercle
				for(int a = 0; a < taille;a++)
				{
					char[] dirs = {'b','d','f','h'};
					ArrayList<MapCell> cases2 = new ArrayList<>();//on évite les modifications concurrentes
					cases2.addAll(cases);
					for(MapCell aCell : cases2)
					{
						for(char d : dirs)
						{
							MapCell cell = map.getCell(OldPathfinding.GetCaseIDFromDirrection(aCell.getID(), d, map, true));
							if(cell == null)continue;
							if(!cases.contains(cell))
								cases.add(cell);
						}
					}
				}
			break;
			
			case 'X'://Croix
				char[] dirs = {'b','d','f','h'};
				for(char d : dirs)
				{
					int cID = cellID;
					for(int a = 0; a< taille; a++)
					{
						cases.add(map.getCell(GetCaseIDFromDirrection(cID, d, map, true)));
						cID = GetCaseIDFromDirrection(cID, d, map, true);
					}
				}
			break;
			
			case 'L'://Ligne
				char dir = OldPathfinding.getDirBetweenTwoCase(castCellID, cellID, map,true);
				for(int a = 0; a< taille; a++)
				{
					cases.add(map.getCell(GetCaseIDFromDirrection(cellID, dir, map, true)));
					cellID = GetCaseIDFromDirrection(cellID, dir, map, true);
				}
			break;
			
			case 'P'://Player?
			break;
			
			default:
				GameServer.addToLog("[FIXME]Type de portée non reconnue: "+zoneStr.charAt(0));
			break;
		}
		return cases;
	}

	public static int getCellXCoord(GameMap map, int cellID)
	{
		if(map == null) return 0;
		int w = map.get_w();
		return ((cellID - (w -1) * getCellYCoord(map,cellID)) / w);
	}
	
	public static int getCellYCoord(GameMap map, int cellID)
	{
		int w = map.get_w();
		int loc5 = (int)(cellID/ ((w*2) -1));
		int loc6 = cellID - loc5 * ((w * 2) -1);
		int loc7 = loc6 % w;
		return (loc5 - loc7);
	}
	
	public static boolean checkLoS(GameMap map, int cell1, int cell2,Fighter fighter)
	{
		if(fighter.getPlayer() != null)return true;
		int dist = getDistanceBetween(map,cell1,cell2);
		ArrayList <Integer> los = new ArrayList <Integer>();
		if(dist > 2)
			los = getLoS(cell1,cell2);
		if(los != null && dist > 2)
		{
			for(int i : los)
			{
				if(i != cell1 && i != cell2 && !map.getCell(i).blockLoS() )
					return false;
			}
		}
		if(dist > 2)
		{
			int cell = getNearestCellAround(map,cell2,cell1,null);
			if(cell != -1 && !map.getCell(cell).blockLoS())
				return false;
		}
		
		return true;
	}

	public static int getNearestCellAround(GameMap map,int startCell, int endCell, ArrayList<MapCell> forbidens)
	{
		//On prend la cellule autour de la cible, la plus proche
		int dist = 1000;
		int cellID = startCell;
		if(forbidens == null)forbidens = new ArrayList<>();
		char[] dirs = {'b','d','f','h'};
		for(char d : dirs)
		{
			int c = OldPathfinding.GetCaseIDFromDirrection(startCell, d, map, true);
			int dis = OldPathfinding.getDistanceBetween(map, endCell, c);
			if(dis < dist && map.getCell(c).isWalkable(true) && map.getCell(c).getFirstFighter() == null && !forbidens.contains(map.getCell(c)))
			{
				dist = dis;
				cellID = c;
			}
		}
		//On renvoie -1 si pas trouvé
		return cellID==startCell?-1:cellID;
	}
	
	public static int getNearestCellAroundHorsCombat(GameMap map,int startCell, int endCell, ArrayList<MapCell> forbidens)
	{
		//On prend la cellule autour de la cible, la plus proche
				int dist = 1000;
				int cellID = startCell;
				if(forbidens == null)forbidens = new ArrayList<>();
				char[] dirs = {'a','b','c','d','e','f','g','h'};
				for(char d : dirs)
				{
					int c = OldPathfinding.GetCaseIDFromDirrection(startCell, d, map, false);
					int dis = OldPathfinding.getDistanceBetween(map, endCell, c);
					if(dis < dist && map.getCell(c).isWalkable(true) && !forbidens.contains(map.getCell(c)))
					{
						dist = dis;
						cellID = c;
					}
				}
				//On renvoie -1 si pas trouv�
				return cellID==startCell?-1:cellID;
	}
	
//	public static ArrayList<MapCell> getShortestPathBetween(GameMap map, int start, int dest, int distMax)
//	{	
//		ArrayList<MapCell> curPath = new ArrayList<MapCell>();
//		ArrayList<MapCell> curPath2 = new ArrayList<MapCell>();
//		ArrayList<MapCell> closeCells = new ArrayList<MapCell>();
//		int limit = 1000;
//		//int oldCaseID = start;
//		MapCell curCase = map.getCell(start);
//		int stepNum = 0;
//		boolean stop = false;
//		
//		while(!stop && stepNum++ <= limit)
//		{
//			int nearestCell = getNearestCellAround(map,curCase.getID(),dest,closeCells);
//			if(nearestCell == -1)
//			{
//				closeCells.add(curCase);
//				if(curPath.size() > 0)
//				{
//				 	curPath.remove(curPath.size()-1);
//				 	if(curPath.size()>0)curCase = curPath.get(curPath.size()-1);
//				 	else curCase = map.getCell(start);
//				}
//				else
//				{
//					curCase = map.getCell(start);
//				}
//			}else if(distMax == 0 && nearestCell == dest)
//			{
//			 	curPath.add(map.getCell(dest));
//			 	break;
//			}else if(distMax > OldPathfinding.getDistanceBetween(map, nearestCell, dest))
//			{
//			 	curPath.add(map.getCell(dest));
//			 	break; 
//			}else//on continue
//			{
//				curCase = map.getCell(nearestCell);
//				closeCells.add(curCase);
//				curPath.add(curCase);
//			}
//		}
//		
//		curCase = map.getCell(start);
//		closeCells.clear();
//		if(!curPath.isEmpty())
//		{
//			closeCells.add(curPath.get(0));
//		}
//		
//		while(!stop && stepNum++ <= limit)
//		{
//			
//			int nearestCell = getNearestCellAround(map,curCase.getID(),dest,closeCells);
//			if(nearestCell == -1)
//			{
//				closeCells.add(curCase);
//				if(curPath2.size() > 0)
//				{
//					curPath2.remove(curPath2.size()-1);
//				 	if(curPath2.size()>0)curCase = curPath2.get(curPath2.size()-1);
//				 	else curCase = map.getCell(start);
//				}
//				else//Si retour a zero
//				{
//					curCase = map.getCell(start);
//				}
//			}else if(distMax == 0 && nearestCell == dest)
//			{
//				curPath2.add(map.getCell(dest));
//			 	break;
//			}else if(distMax > OldPathfinding.getDistanceBetween(map, nearestCell, dest))
//			{
//			 	curPath2.add(map.getCell(dest));
//			 	break; 
//			}else//on continue
//			{
//				curCase = map.getCell(nearestCell);
//				closeCells.add(curCase);
//				curPath2.add(curCase);
//			}
//		}
//		
//		if((curPath2.size() < curPath.size() && curPath2.size() > 0) || curPath.isEmpty())
//			curPath = curPath2;
//		return curPath;
//	}
	
	public static ArrayList<Integer> getListCaseFromFighter(Fight fight, Fighter fighter)
	{
		ArrayList<Integer> cells = new ArrayList<Integer>();
		int start = fighter.getCell().getID();
		int[] curPath;
		int i = 0;
		if(fighter.getCurPM() > 0)
			curPath = new int[fighter.getCurPM()];
		else
			return null;
		if(curPath.length == 0)
			return null;
		while(curPath[0] != 5)
		{
			curPath[i]++;
			if(curPath[i] == 5 && i != 0)
			{
				curPath[i] = 0;
				i--;
			}
			else 
			{
				int curCell = getCellFromPath(start,curPath);
				if(fight.get_map().getCell(curCell).isWalkable(true) && fight.get_map().getCell(curCell).getFirstFighter() == null)
				{
					if(!cells.contains(curCell))
					{
						cells.add(curCell);
						if(i < curPath.length - 1)
							i++;
					}
				}
			}
		}
		
		return triCellList(fight, fighter,cells);
	}
        
        public static ArrayList<Integer> getFullPMListCase(Fight fight, Fighter fighter){
		ArrayList<Integer> cells = new ArrayList<Integer>();
		int start = fighter.getCell().getID();
		int[] curPath;
		int i = 0;
		if(fighter.getCurPM() > 0)
			curPath = new int[fighter.getCurPM()];
		else
			return null;
		if(curPath.length == 0)
			return null;
                
                for(int a = 0; a < curPath.length; a++){
                    curPath[a] = 1; //rempli tableau avec des 1
                }
                
		while(curPath[0] != 5)
		{   
                        if(i > 0 && (
                                (curPath[i] == 1 && curPath[i - 1] == 3) 
                                || (curPath[i] == 2 && curPath[i - 1] == 4)
                                || (curPath[i] == 3 && curPath[i - 1] == 1)
                                || (curPath[i] == 4 && curPath[i - 1] == 2)
                            )){
                            curPath[i]++; //si retour en arrière, on passe
                        }
                        
			if(curPath[i] == 5 && i != 0)
			{
				curPath[i] = 1;
				i--;
			}
			else 
			{
				int curCell = getCellFromPath(start,curPath);
				if(fight.get_map().getCell(curCell).isWalkable(true) && fight.get_map().getCell(curCell).getFirstFighter() == null)
				{
					if(!cells.contains(curCell))
					{
						cells.add(curCell);
						if(i < curPath.length - 1)
							i++;
					}
				}
			}
			curPath[i]++;
		}
		
		return triCellList(fight, fighter,cells);
        }
	
	public static int getCellFromPath(int start,int[] path)
	{
		int cell = start,i = 0;
		while(i < path.length)
		{
			if(path[i] == 1)
				cell -= 15;
			if(path[i] == 2)
				cell -= 14;
			if(path[i] == 3)
				cell += 15;
			if(path[i] == 4)
				cell += 14;
			i++;
		}
		return cell;
	}
	
	public static ArrayList<Integer> triCellList(Fight fight, Fighter fighter, ArrayList<Integer> cells)
	{
		ArrayList<Integer> Fcells = new ArrayList<Integer>();
		ArrayList<Integer> copie = cells;
		int dist = 100;
		int curCell = 0;
		int curIndex = 0;
		while(copie.size() > 0)
		{
			dist = 100;
			for(int i : copie)
			{
				int d = getDistanceBetween(fight.get_map(), fighter.getCell().getID(), i);
				if(dist > d)
				{
					dist = d;
					curCell = i;
					curIndex = copie.indexOf(i);
				}
			}
			Fcells.add(curCell);
			copie.remove(curIndex);
		}
		
		return Fcells;
	}
	
	public static boolean isBord1(int id)
	{
		int[] bords = {1,30,59,88,117,146,175,204,233,262,291,320,349,378,407,436,465,15,44,73,102,131,160,189,218,247,276,305,334,363,392,421,450,479};
		ArrayList <Integer> test = new ArrayList <Integer>();
		for(int i : bords)
		{
			test.add(i);
		}
		
		if(test.contains(id))
			return true;
		else 
			return false;
	}
	
	public static boolean isBord2(int id)
	{
		int[] bords = {16,45,74,103,132,161,190,219,248,277,306,335,364,393,422,451,29,58,87,116,145,174,203,232,261,290,319,348,377,406,435,464};
		ArrayList <Integer> test = new ArrayList <Integer>();
		for(int i : bords)
		{
			test.add(i);
		}
		
		if(test.contains(id))
			return true;
		else 
			return false;
	}
	
	public static ArrayList<Integer> getLoS (int cell1, int cell2)
	{
		ArrayList<Integer> Los = new ArrayList<Integer>();
		int cell = cell1;
		boolean next = false;
		int[] dir1 = {1,-1,29,-29,15,14,-15,-14};
		
		for(int i : dir1)
		{
			Los.clear();
			cell = cell1;
			Los.add(cell);
			next = false;
			while(!next)
			{
				cell += i;
				Los.add(cell);
				if(isBord2(cell) || isBord1(cell) || cell <= 0 || cell >= 480)
					next=true;
				if(cell == cell2)
				{
					return Los;
				}
			}
		}
		return null;
	}
	
	public static int ViewOrientation(int o) {
		int ForView = o;
		switch (o) {
			case 0:
				ForView = 1;
				break;
			case 2:
				ForView = 3;
				break;
			case 4:
				ForView = 5;
				break;
			case 6:
				ForView = 7;
				break;
		}
		return ForView;
	}
	
//	public static String getShortestStringPathBetween(GameMap map, int start, int dest, int distMax)
//	{
//		if (start == dest) return null;
//		ArrayList<MapCell> path = getShortestPathBetween(map, start, dest, distMax);
//		if (path == null) return null;
//		String pathstr = "";
//		int curCaseID = start;
//		char curDir = '\000';
//		for (MapCell c : path)
//		{
//			char d = getDirBetweenTwoCase(curCaseID, c.getID(), map, true);
//			if (d == 0) return null;
//			if (curDir != d)
//			{
//				if (path.indexOf(c) != 0)
//					pathstr = pathstr + CryptManager.cellID_To_Code(curCaseID);
//				pathstr = pathstr + d;
//				curDir = d;
//			}
//			curCaseID = c.getID();
//		}
//		if (curCaseID != start)
//		{
//			pathstr = pathstr + CryptManager.cellID_To_Code(curCaseID);
//		}
//		if (pathstr == "") return null;
//		return "a" + CryptManager.cellID_To_Code(start) + pathstr;
//	}
}
