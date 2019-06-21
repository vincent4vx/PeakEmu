package org.peakemu.common;


import org.peakemu.Ancestra;
import org.peakemu.world.GameMap;
import org.peakemu.objects.Collector;
import org.peakemu.objects.player.Player;
import org.peakemu.objects.Guild;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import org.peakemu.common.util.Util;


import org.peakemu.objects.player.GuildMember;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.fight.fighter.Fighter;

public class Formulas {

    @Deprecated
	public static int getRandomValue(int i1,int i2)
	{
        return Util.rand(i1, i2);
	}
	
	// Random float between 0 & 100
	public static float getFloatRandom()
	{
		Random rand = new Random();
		//return (rand.nextInt((i2-i1)+1))+i1;
		//FIXME: RandomFloat retourné entre 0 & 99 (au lieu de 100). Gênant ? 
		return rand.nextFloat()*100;
	}
	
	public static int getRandomJet(String jet)//1d5+6
	{
		try
		{
			int num = 0;
			int des = Integer.parseInt(jet.split("d")[0]);
			int faces = Integer.parseInt(jet.split("d")[1].split("\\+")[0]);
			int add = Integer.parseInt(jet.split("d")[1].split("\\+")[1]);
			for(int a=0;a<des;a++)
			{
				num += getRandomValue(1,faces);
			}
			num += add;
			return num;
		}catch(NumberFormatException e){return -1;}
	}
	public static int getMiddleJet(String jet)//1d5+6
	{
		try
		{
			int num = 0;
			int des = Integer.parseInt(jet.split("d")[0]);
			int faces = Integer.parseInt(jet.split("d")[1].split("\\+")[0]);
			int add = Integer.parseInt(jet.split("d")[1].split("\\+")[1]);
			num += ((1+faces)/2)*des;//on calcule moyenne
			num += add;
			return num;
		}catch(NumberFormatException e){return 0;}
	}
	
	public static int getTacleChance(Fighter tacleur, ArrayList<Fighter> tacle)
	{
		//%Esquive = 300 * (Agi + 25) / (Agi + Agi Ennemi + 50) - 100
			int agiTR = tacleur.getTotalStats().getEffect(Effect.ADD_AGIL);
			int agiT = 0;
				for(Fighter T : tacle)
					agiT += T.getTotalStats().getEffect(Effect.ADD_AGIL);
				return (int)(300 * agiTR + 25) / (agiTR + agiT + 50) - 100;
	}
	
	public static int calculFinalHeal(Player caster,int jet)
	{
		int statC = caster.getTotalStats().getEffect(Effect.ADD_INTE);
		int soins = caster.getTotalStats().getEffect(Effect.ADD_SOIN);
		if(statC<0)statC=0;
		return (int)(jet * (100 + statC) / 100 + soins);
	}

	public static int calculZaapCost(GameMap map1,GameMap map2)
	{
		return (int) (10*(Math.abs(map2.getX()-map1.getX())+Math.abs(map2.getY()-map1.getY())-1));
	}

	public static int getPointsLost(char z, int value, Fighter caster,Fighter target)
	{
		float esquiveC = z=='a'?caster.getTotalStats().getEffect(Effect.ADD_AFLEE):caster.getTotalStats().getEffect(Effect.ADD_MFLEE);
		float esquiveT = z=='a'?target.getTotalStats().getEffect(Effect.ADD_AFLEE):target.getTotalStats().getEffect(Effect.ADD_MFLEE);
		float ptsMax = z=='a'?target.getBaseStats().getEffect(Effect.ADD_PA):target.getBaseStats().getEffect(Effect.ADD_PM);
		
		int retrait = 0;

		for(int i = 0; i < value;i++)
		{
			if(ptsMax == 0 && target.getMonster() != null)
			{
				ptsMax= z=='a'?target.getMonster().getPA():target.getMonster().getPM();
			}
			
			float pts = z =='a'?target.getPA():target.getPM();
			float ptsAct = pts - retrait;
			
			if(esquiveT == 0)esquiveT=1;
			if(esquiveC == 0)esquiveC=1;

			float a = (float)(esquiveC/esquiveT);
			float b = (ptsAct/ptsMax);

			float pourcentage = (float)(a*b*50);
			int chance = (int)Math.ceil(pourcentage);
			
			/*
			System.out.println("Esquive % : "+a+" Facteur PA/PM : "+b);
			System.out.println("ptsMax : "+ptsMax+" ptsAct : "+ptsAct);
			System.out.println("Chance d'esquiver le "+(i+1)+" eme PA/PM : "+chance);
			*/
			
			if(chance <0)chance = 0;
			if(chance >100)chance = 100;

			int jet = getRandomValue(0, 99);
			if(jet<chance)
			{
				retrait++;
			}
		}
		return retrait;
	}
	
	public static long getXpWinPerco(Collector perco, Collection<Fighter> winners,Collection<Fighter> loosers,long groupXP)
	{
			Guild G = perco.getGuild();
			float sag = G.get_Stats(Effect.ADD_SAGE);
			float coef = (sag + 100)/100;
			int taux = Ancestra.XP_PVM;
			long xpWin = 0;
			int lvlmax = 0;
			for(Fighter entry : winners)
			{
				if(entry.get_lvl() > lvlmax)
					lvlmax = entry.get_lvl();
			}
			int nbbonus = 0;
			for(Fighter entry : winners)
			{
				if(entry.get_lvl() > (lvlmax / 3))
					nbbonus += 1;				
			}
			
			double bonus = 1;
			if(nbbonus == 2)
				bonus = 1.1;
			if(nbbonus == 3)
				bonus = 1.3;
			if(nbbonus == 4)
				bonus = 2.2;
			if(nbbonus == 5)
				bonus = 2.5;
			if(nbbonus == 6)
				bonus = 2.8;
			if(nbbonus == 7)
				bonus = 3.1;
			if(nbbonus >= 8)
				bonus = 3.5;
			
			int lvlLoosers = 0;
			for(Fighter entry : loosers)
				lvlLoosers += entry.get_lvl();
			int lvlWinners = 0;
			for(Fighter entry : winners)
				lvlWinners += entry.get_lvl();
			double rapport = 1+((double)lvlLoosers/(double)lvlWinners);
			if (rapport <= 1.3)
				rapport = 1.3;
			/*
			if (rapport > 5)
				rapport = 5;
			//*/
			int lvl = G.getLevel();
			double rapport2 = 1 + ((double)lvl / (double)lvlWinners);

			xpWin = (long) (groupXP * rapport * bonus * taux *coef * rapport2);
			
			/*/ DEBUG XP
			System.out.println("=========");
			System.out.println("groupXP: "+groupXP);
			System.out.println("rapport1: "+rapport);
			System.out.println("bonus: "+bonus);
			System.out.println("taux: "+taux);
			System.out.println("coef: "+coef);
			System.out.println("rapport2: "+rapport2);
			System.out.println("xpWin: "+xpWin);
			System.out.println("=========");
			//*/
			return xpWin;	
	}
	
	public static int calculHonorWinPrisms(ArrayList<Fighter> winner, ArrayList<Fighter> looser, Fighter F) {
		float totalGradeWin = 0;
		float totalLevelWin = 0;
		float totalGradeLoose = 0;
		float totalLevelLoose = 0;
		boolean Prisme = false;
		int fighters = 0;
		for (Fighter  f : winner) {
			if (f.getPlayer() == null && f.getPrism() == null)
				continue;
			if (f.getPlayer() != null) {
				totalLevelWin += f.get_lvl();
				totalGradeWin += f.getPlayer().getGrade();
			} else {
				Prisme = true;
				totalLevelWin += 200;
				totalGradeWin += (f.getPrism().getLevel() * 20) + 100;
			}
		}
		for (Fighter f : looser) {
			if (f.getPlayer() == null && f.getPrism() == null)
				continue;
			if (f.getPlayer() != null) {
				totalLevelLoose += f.get_lvl();
				totalGradeLoose += f.getPlayer().getGrade();
				fighters++;
			} else {
				Prisme = true;
				totalLevelLoose += 200;
				totalGradeLoose += (f.getPrism().getLevel() * 15) + 100;
			}
		}
		if (!Prisme)
			if (totalLevelWin - totalLevelLoose > 15 * fighters)
				return 0;
		int base = (int) (100 * (float) ( (totalGradeLoose * totalLevelLoose) / (totalGradeWin * totalLevelWin)))
				/ winner.size();
		if (looser.contains(F))
			base = -base;
		return base * Ancestra.HONOR;
	}
	
	public static long getXpWinPvm2(Fighter perso, Collection<Fighter> winners,Collection<Fighter> loosers,long groupXP)
	{
		if(perso.getPlayer()== null)return 0;
        
		if(winners.contains(perso))//Si winner
		{
			float sag = perso.getTotalStats().getEffect(Effect.ADD_SAGE);
			float coef = (sag + 100)/100;
			int taux = Ancestra.XP_PVM;
			long xpWin = 0;
			int lvlmax = 0;
			for(Fighter entry : winners)
			{
				if(entry.get_lvl() > lvlmax)
					lvlmax = entry.get_lvl();
			}
			int nbbonus = 0;
			for(Fighter entry : winners)
			{
				if(entry.get_lvl() > (lvlmax / 3))
					nbbonus += 1;				
			}
			
			double bonus = 1;
			if(nbbonus == 2)
				bonus = 1.1;
			if(nbbonus == 3)
				bonus = 1.3;
			if(nbbonus == 4)
				bonus = 2.2;
			if(nbbonus == 5)
				bonus = 2.5;
			if(nbbonus == 6)
				bonus = 2.8;
			if(nbbonus == 7)
				bonus = 3.1;
			if(nbbonus >= 8)
				bonus = 3.5;
			
			int lvlLoosers = 0;
			for(Fighter entry : loosers)
				lvlLoosers += entry.get_lvl();
			int lvlWinners = 0;
			for(Fighter entry : winners)
				lvlWinners += entry.get_lvl();
			double rapport = 1+((double)lvlLoosers/(double)lvlWinners);
			if (rapport <= 1.3)
				rapport = 1.3;
			/*
			if (rapport > 5)
				rapport = 5;
			//*/
			int lvl = perso.get_lvl();
			double rapport2 = 1 + ((double)lvl / (double)lvlWinners);

			xpWin = (long) (groupXP * rapport * bonus * taux *coef * rapport2);
			
			/*/ DEBUG XP
			System.out.println("=========");
			System.out.println("groupXP: "+groupXP);
			System.out.println("rapport1: "+rapport);
			System.out.println("bonus: "+bonus);
			System.out.println("taux: "+taux);
			System.out.println("coef: "+coef);
			System.out.println("rapport2: "+rapport2);
			System.out.println("xpWin: "+xpWin);
			System.out.println("=========");
			//*/
			return xpWin;	
		}
		return 0;
	}
	
	public static long getXpWinPvm(Fighter perso, ArrayList<Fighter> winners,ArrayList<Fighter> loosers,long groupXP)
	{
		if(perso.getPlayer()== null)return 0;
		if(winners.contains(perso))//Si winner
		{
			float sag = perso.getTotalStats().getEffect(Effect.ADD_SAGE);
			float coef = (sag + 100)/100;
			int taux = Ancestra.XP_PVM;
			long xpWin = 0;
			int lvlmax = 0;
			for(Fighter entry : winners)
			{
				if(entry.get_lvl() > lvlmax)
					lvlmax = entry.get_lvl();
			}
			int nbbonus = 0;
			for(Fighter entry : winners)
			{
				if(entry.get_lvl() > (lvlmax / 3))
					nbbonus += 1;				
			}
			
			double bonus = 1;
			if(nbbonus == 2)
				bonus = 1.1;
			if(nbbonus == 3)
				bonus = 1.3;
			if(nbbonus == 4)
				bonus = 2.2;
			if(nbbonus == 5)
				bonus = 2.5;
			if(nbbonus == 6)
				bonus = 2.8;
			if(nbbonus == 7)
				bonus = 3.1;
			if(nbbonus >= 8)
				bonus = 3.5;
			
			int lvlLoosers = 0;
			for(Fighter entry : loosers)
				lvlLoosers += entry.get_lvl();
			int lvlWinners = 0;
			for(Fighter entry : winners)
				lvlWinners += entry.get_lvl();
			double rapport = 1+((double)lvlLoosers/(double)lvlWinners);
			if (rapport <= 1.3)
				rapport = 1.3;
			
			int lvl = perso.get_lvl();
			double rapport2 = 1 + ((double)lvl / (double)lvlWinners);

			xpWin = (long) (groupXP * rapport * bonus * taux *coef * rapport2);
			
			return xpWin;	
		}
		return 0;
	}
	public static long getGuildXpWin(Fighter perso, AtomicReference<Long> xpWin)
	{
		if(perso.getPlayer()== null)return 0;
		if(perso.getPlayer().getGuildMember() == null)return 0;
		

		GuildMember gm = perso.getPlayer().getGuildMember();
		
		double xp = (double)xpWin.get(), Lvl = perso.get_lvl(),LvlGuild = perso.getPlayer().getGuild().getLevel(),pXpGive = (double)gm.getPXpGive()/100;
		
		double maxP = xp * pXpGive * 0.10;	//Le maximum donn� � la guilde est 10% du montant pr�lev� sur l'xp du combat
		double diff = Math.abs(Lvl - LvlGuild);	//Calcul l'�cart entre le niveau du personnage et le niveau de la guilde
		double toGuild;
		if(diff >= 70)
		{
			toGuild = maxP * 0.10;	//Si l'�cart entre les deux level est de 70 ou plus, l'experience donn�e a la guilde est de 10% la valeur maximum de don
		}
		else if(diff >= 31 && diff <= 69)
		{
			toGuild = maxP - ((maxP * 0.10) * (Math.floor((diff+30)/10)));
		}
		else if(diff >= 10 && diff <= 30)
		{
			toGuild = maxP - ((maxP * 0.20) * (Math.floor(diff/10))) ;
		}
		else	//Si la diff�rence est [0,9]
		{
			toGuild = maxP;
		}
		xpWin.set((long)(xp - xp*pXpGive));
		return (long) Math.round(toGuild);
	}
	
	public static long getMountXpWin(Fighter perso, AtomicReference<Long> xpWin)
	{
		if(perso.getPlayer()== null)return 0;
		if(perso.getPlayer().getMount() == null)return 0;
		

		int diff = Math.abs(perso.get_lvl() - perso.getPlayer().getMount().getLevel());
		
		double coeff = 0;
		double xp = (double) xpWin.get();
		double pToMount = (double)perso.getPlayer().getMountXpGive() / 100 + 0.2;
		
		if(diff >= 0 && diff <= 9)
			coeff = 0.1;
		else if(diff >= 10 && diff <= 19)
			coeff = 0.08;
		else if(diff >= 20 && diff <= 29)
			coeff = 0.06;
		else if(diff >= 30 && diff <= 39)
			coeff = 0.04;
		else if(diff >= 40 && diff <= 49)
			coeff = 0.03;
		else if(diff >= 50 && diff <= 59)
			coeff = 0.02;
		else if(diff >= 60 && diff <= 69)
			coeff = 0.015;
		else
			coeff = 0.01;
		
		if(pToMount > 0.2)
			xpWin.set((long)(xp - (xp*(pToMount-0.2))));
		
		return (long)Math.round(xp * pToMount * coeff);
	}

	public static int getKamasWin(Fighter i, Collection<Fighter> winners, int min, int max)
	{
		return Util.rand(min, max)*Ancestra.KAMAS;
	}
	
	public static int getKamasWinPerco(int maxk, int mink)
	{
		//maxk++;
		int rkamas = (int)(Math.random() * (maxk-mink)) + mink;
		return rkamas*Ancestra.KAMAS;
	}
	
	public static int calculElementChangeChance(int lvlM,int lvlA,int lvlP)
	{
		int K = 350;
		if(lvlP == 1)K = 100;
		else if (lvlP == 25)K = 175;
		else if (lvlP == 50)K = 350;
		return (int)((lvlM*100)/(K + lvlA));
	}

	public static int calculHonorWin(Collection<Fighter> winners, Collection<Fighter> loosers, Fighter F){
		float totalGradeWin = 0;
		float totalLevelWin = 0;
		float totalGradeLoose = 0;
		float totalLevelLoose = 0;
		for(Fighter f : winners)
		{
			if(f.getPlayer() == null )continue;
			totalLevelWin += f.get_lvl();
			totalGradeWin += f.getPlayer().getGrade();

		}
		for(Fighter f : loosers)
		{
			if(f.getPlayer() == null)continue;
			totalLevelLoose += f.get_lvl();
			totalGradeLoose += f.getPlayer().getGrade();

		}
		
		if(totalLevelWin-totalLevelLoose > 15) return 0;

		int base = (int)(100 * (float)(totalGradeLoose/totalGradeWin))/winners.size();
		if(loosers.contains(F))base = -base;
		return base * Ancestra.HONOR;
	}
	
	public static int calculDeshonorWin(ArrayList<Fighter> winners,ArrayList<Fighter> loosers,Fighter F, Player perso)
	{
		int lvlPerso = perso.getLevel();
		int rankPerso = perso.getAlignLevel();
		int calcul = lvlPerso + rankPerso + 200;
		
		for(Fighter f : loosers)
		{
			if(f.getPlayer() == null)continue;
			calcul -= f.get_lvl();
			calcul -= f.getPlayer().getGrade();

		}
		return calcul * Ancestra.HONOR;
	}
	
	public static int totalCaptChance(int pierreChance, Player p)
	{
		int sortChance = 0;

		switch(p.getSpellById(413).getLevel())
		{
			case 1:
				sortChance = 1;
				break;
			case 2:
				sortChance = 3;
				break;
			case 3:
				sortChance = 6;
				break;
			case 4:
				sortChance = 10;
				break;
			case 5:
				sortChance = 15;
				break;
			case 6:
				sortChance = 25;
				break;
		}
		
		return sortChance + pierreChance;
	}
	
	public static String parseReponse(String reponse)
	{
		StringBuilder toReturn = new StringBuilder("");
		
		String[] cut = reponse.split("[%]");
		
		if(cut.length == 1)return reponse;
		
		toReturn.append(cut[0]);
		
		char charact;
		for (int i = 1; i < cut.length; i++)
		{
			charact = (char) Integer.parseInt(cut[i].substring(0, 2),16);
			toReturn.append(charact).append(cut[i].substring(2));
		}
		
		return toReturn.toString();
	}
	
	public static int spellCost(int nb)
	{
		int total = 0;
		for (int i = 1; i < nb ; i++)
		{
			total += i;
		}
		
		return total;
	}
	
	public static int ChanceFM(int poidItemBase, int poidItemActual, int poidBaseJet, int poidActualJet, double poidRune, int Puis, double Coef)
	{
		int Chance = 0;
		int a = (poidItemBase+poidBaseJet+(Puis*2));
		int b = (int) (Math.sqrt(poidItemActual+poidActualJet+poidRune));
		if(b <= 0) b = 1;
		Chance = (int) Math.floor((a/b)*Coef);
		
		//DEBUG :
		System.out.println("A : "+a);
		System.out.println("B : "+b);
		return Chance;
	}
	
	public static int getTraqueXP(int lvl)
	{
		if(lvl < 60)return 65000 * 1;
		if(lvl < 70)return 90000 * 1;
		if(lvl < 80)return 120000 * 1;
		if(lvl < 90)return 160000 * 1;
		if(lvl < 100)return 210000 * 1;
		if(lvl < 110)return 270000 * 1;
		if(lvl < 120)return 350000 * 1;
		if(lvl < 130)return 440000 * 1;
		if(lvl < 140)return 540000 * 1;
		if(lvl < 150)return 650000 * 1;
		if(lvl < 155)return 760000 * 1;
		if(lvl < 160)return 880000 * 1;
		if(lvl < 165)return 1000000 * 1;
		if(lvl < 170)return 1130000 * 1;
		if(lvl < 175)return 1300000 * 1;
		if(lvl < 180)return 1500000 * 1;
		if(lvl < 185)return 1700000 * 1;
		if(lvl < 190)return 2000000 * 1;
		if(lvl < 195)return 2500000 * 1;
		return 3000000 * 1;
	}
	
	public static int getLoosEnergy(int lvl, boolean isAgression, boolean isPerco)
	{
		if(isAgression)
			return (10*lvl*24)/20; // perte d'energie aggro: niveau 200 -2400
		if(isPerco)
			return ((10*lvl*4)/2); //perte d'energie perco: niveau 200 -4000
		
		return (10*lvl*5)/8; // perte d'energie pvm: au niveau 200 -1250
	}
	
	public static int getShutDownTime(int Secs)//secs
	{
		if (Secs <= 10) {
			return 1; //toute les sec
		}
		if (Secs <= 600) {
			return (1 * 60); //toute les min
		}
		if (Secs <= 1800) {
			return (5 * 60); //toute les 5 min
		}
		if (Secs <= 3600) {
			return (10 * 60); //toute les 10 min
		}
		if (Secs <= (3600 * 2)) {
			return (30 * 60); //toute les 30 min
		}
		if (Secs <= (3600 * 5)) {
			return (60 * 60); //toute les h
		}
		if (Secs <= (3600 * 24)) {
			return (60 * 60 * 2); //toute les 2h
		}
		if (Secs > (3600 * 24)) {
			return (60 * 60 * 4); //toute les 4h
		}		//le reste ne devrait pas arriver
		return 0;
	}

	public static String getShutDownName(int Secs)//secs
	{
		int hours = 0;
		int mins = 0;
		StringBuilder Tname = new StringBuilder("");
		if (Secs < 1) {
			return "0 secondes";
		}
		if (Secs >= 1 && Secs <= 10) {
			if (Secs == 1) {
				Tname.append("1 seconde");
			} else {
				Tname.append(Secs).append(" secondes");
			}
			return Tname.toString();
		} else {
			Tname.append("environ ");
			if (Secs >= 3600) {
				hours = Math.round(Secs / 3600);
				Secs -= (hours * 3600);
				if (hours == 1) {
					Tname.append("1 heure");
				} else {
					Tname.append(hours).append(" heures");
				}
			}
			if (hours > 0 && Secs >= 60) {
				Tname.append(" et ");
			}
			if (Secs >= 60) {
				mins = Math.round(Secs / 60);
				Secs -= (mins * 60);
				if (mins == 1) {
					Tname.append("1 minute");
				} else {
					Tname.append(mins).append(" minutes");
				}
			}
		}
		return Tname.toString();
	}
}
