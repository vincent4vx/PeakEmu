package org.peakemu.common;

import java.io.File;
import org.peakemu.world.World;
import org.peakemu.game.GameServer;
//metier
import java.util.Map;
import java.util.TreeMap;


import org.peakemu.objects.player.Player;

public class Constants
{
    static final public String CONFIG_FILE = "config.xml";
    static final public String CONFIG_ROOT_NODE = "config";
    
    static final public File ERROR_LOG_DIR = new File("logs/errors");
    static final public File GAME_LOG_DIR = new File("logs/game");
    static final public File REALM_LOG_DIR = new File("logs/realm");
    static final public File WORLD_LOG_DIR = new File("logs/world");
    
	//DEBUG
	public static int DEBUG_MAP_LIMIT 	=	20000;
	//Server
	public static final String SERVER_VERSION	=	"3.6.11-beta";
	public static final String SERVER_MAKER		=	"Lenders & v4vx";
	//Versions
	public static final	String CLIENT_VERSION	=	"1.29.1";
	public static final boolean IGNORE_VERSION 		= false;
	//ZAAPI <alignID,{mapID,mapID,...,mapID}>
	public static Map<Integer, String> ZAAPI = new TreeMap<Integer, String>();
	//ZAAP <mapID,cellID>
	public static Map<Integer, Integer> ZAAPS = new TreeMap<Integer, Integer>();
	//BANIP
	public static String BAN_IP = "";
	//Energie
	public static final String ALL_PHOENIX = "-11;-54|2;-12|-41;-17|5;-9|25;-4|36;5|12;12|10;19|-10;13|-14;31|-43;0|-60;-3|-58;18|24;-43|27;-33";
	
	public static boolean IPcompareToBanIP(String ip)
	{
		String[] split = BAN_IP.split(",");
		for(String ipsplit : split)
		{
			if(ip.compareTo(ipsplit) == 0) return true;
		}
		
		return false;
	}
	
	//Valeur des droits de guilde
	public static int G_BOOST = 2;			//G�rer les boost
	public static int G_RIGHT = 4;			//G�rer les droits
	public static int G_INVITE = 8;			//Inviter de nouveaux membres
	public static int G_BAN = 16;				//Bannir
	public static int G_ALLXP = 32;			//G�rer les r�partitions d'xp
	public static int G_HISXP = 256;			//G�rer sa r�partition d'xp
	public static int G_RANK = 64;			//G�rer les rangs
	public static int G_POSPERCO = 128;		//Poser un percepteur
	public static int G_COLLPERCO = 512;		//Collecter les percepteurs
	public static int G_USEENCLOS = 4096;		//Utiliser les enclos
	public static int G_AMENCLOS = 8192;		//Am�nager les enclos
	public static int G_OTHDINDE = 16384;		//G�rer les montures des autres membres
	
	//Valeur des droits de maison
	public static int H_GBLASON = 2; //Afficher blason pour membre de la guilde
	public static int H_OBLASON = 4; //Afficher blason pour les autres
	public static int H_GNOCODE = 8; //Entrer sans code pour la guilde
	public static int H_OCANTOPEN = 16; //Entrer impossible pour les non-guildeux
	public static int C_GNOCODE = 32; //Coffre sans code pour la guilde
	public static int C_OCANTOPEN = 64; //Coffre impossible pour les non-guildeux
	public static int H_GREPOS = 256; //Guilde droit au repos
	public static int H_GTELE = 128; //Guilde droit a la TP
	
	//FIGHT
	public static final int TIME_BY_TURN			= 29*1000;
	public static final int FIGHT_TYPE_CHALLENGE 	= 0;//D�fies
	public static final int FIGHT_TYPE_AGRESSION 	= 1;//Aggros
	public static final int FIGHT_TYPE_CONQUETE 	= 2; //Prismes
	public static final int FIGHT_TYPE_MXVM			= 3;//??
	public static final int FIGHT_TYPE_PVM			= 4;//PvM
	public static final int FIGHT_TYPE_PVT			= 5;//Percepteur
	public static final int FIGHT_TYPE_PVMU			= 6;//??
	public static final int FIGHT_STATE_INIT		= 1;
	public static final int FIGHT_STATE_PLACE		= 2;
	public static final int FIGHT_STATE_ACTIVE 		= 3;
	public static final int FIGHT_STATE_FINISHED	= 4;
	//Jobs
	public static final int JOB_BASE				= 1;
	public static final int JOB_BUCHERON			= 2;
	public static final int JOB_F_EPEE				= 11;
	public static final int JOB_S_ARC				= 13;
	public static final int JOB_F_MARTEAU			= 14;
	public static final int JOB_CORDONIER			= 15;
	public static final int JOB_BIJOUTIER			= 16;
	public static final int JOB_F_DAGUE				= 17;
	public static final int JOB_S_BATON				= 18;
	public static final int JOB_S_BAGUETTE			= 19;
	public static final int JOB_F_PELLE				= 20;
	public static final int JOB_MINEUR				= 24;
	public static final int JOB_BOULANGER			= 25;
	public static final int JOB_ALCHIMISTE			= 26;
	public static final int JOB_TAILLEUR			= 27;
	public static final int JOB_PAYSAN				= 28;
	public static final int JOB_F_HACHES			= 31;
	public static final int JOB_PECHEUR				= 36;
	public static final int JOB_CHASSEUR			= 41;
	public static final int JOB_FM_DAGUE			= 43;
	public static final int JOB_FM_EPEE				= 44;
	public static final int JOB_FM_MARTEAU			= 45;
	public static final int JOB_FM_PELLE			= 46;
	public static final int JOB_FM_HACHES			= 47;
	public static final int JOB_SM_ARC				= 48;
	public static final int JOB_SM_BAGUETTE			= 49;
	public static final int JOB_SM_BATON			= 50;
	public static final int JOB_BOUCHER				= 56;
	public static final int JOB_POISSONNIER			= 58;
	public static final int JOB_F_BOUCLIER			= 60;
	public static final int JOB_CORDOMAGE			= 62;
	public static final int JOB_JOAILLOMAGE			= 63;
	public static final int JOB_COSTUMAGE			= 64;
	public static final int JOB_BRICOLEUR			= 65;
	public static final int JOB_JOAILLER			= 66;
	public static final int JOB_BIJOUTIER2			= 67;
	
		
		
		//Types
		public static final int ITEM_TYPE_AMULETTE			= 1;
		public static final int ITEM_TYPE_ARC				= 2;
		public static final int ITEM_TYPE_BAGUETTE			= 3;
		public static final int ITEM_TYPE_BATON				= 4;
		public static final int ITEM_TYPE_DAGUES			= 5;
		public static final int ITEM_TYPE_EPEE				= 6;
		public static final int ITEM_TYPE_MARTEAU			= 7;
		public static final int ITEM_TYPE_PELLE				= 8;
		public static final int ITEM_TYPE_ANNEAU			= 9;
		public static final int ITEM_TYPE_CEINTURE			= 10;
		public static final int ITEM_TYPE_BOTTES			= 11;
		public static final int ITEM_TYPE_POTION			= 12;
		public static final int ITEM_TYPE_PARCHO_EXP		= 13;
		public static final int ITEM_TYPE_DONS				= 14;
		public static final int ITEM_TYPE_RESSOURCE			= 15;
		public static final int ITEM_TYPE_COIFFE			= 16;
		public static final int ITEM_TYPE_CAPE				= 17;
		public static final int ITEM_TYPE_FAMILIER			= 18;
		public static final int ITEM_TYPE_HACHE				= 19;
		public static final int ITEM_TYPE_OUTIL				= 20;
		public static final int ITEM_TYPE_PIOCHE			= 21;
		public static final int ITEM_TYPE_FAUX				= 22;
		public static final int ITEM_TYPE_DOFUS				= 23;
		public static final int ITEM_TYPE_QUETES			= 24;
		public static final int ITEM_TYPE_DOCUMENT			= 25;
		public static final int ITEM_TYPE_FM_POTION			= 26;
		public static final int ITEM_TYPE_TRANSFORM			= 27;
		public static final int ITEM_TYPE_BOOST_FOOD		= 28;
		public static final int ITEM_TYPE_BENEDICTION		= 29;
		public static final int ITEM_TYPE_MALEDICTION		= 30;
		public static final int ITEM_TYPE_RP_BUFF			= 31;
		public static final int ITEM_TYPE_PERSO_SUIVEUR		= 32;
		public static final int ITEM_TYPE_PAIN				= 33;
		public static final int ITEM_TYPE_CEREALE			= 34;
		public static final int ITEM_TYPE_FLEUR				= 35;
		public static final int ITEM_TYPE_PLANTE			= 36;
		public static final int ITEM_TYPE_BIERE				= 37;
		public static final int ITEM_TYPE_BOIS				= 38;
		public static final int ITEM_TYPE_MINERAIS			= 39;
		public static final int ITEM_TYPE_ALLIAGE			= 40;
		public static final int ITEM_TYPE_POISSON			= 41;
		public static final int ITEM_TYPE_BONBON			= 42;
		public static final int ITEM_TYPE_POTION_OUBLIE		= 43;
		public static final int ITEM_TYPE_POTION_METIER		= 44;
		public static final int ITEM_TYPE_POTION_SORT		= 45;
		public static final int ITEM_TYPE_FRUIT				= 46;
		public static final int ITEM_TYPE_OS				= 47;
		public static final int ITEM_TYPE_POUDRE			= 48;
		public static final int ITEM_TYPE_COMESTI_POISSON	= 49;
		public static final int ITEM_TYPE_PIERRE_PRECIEUSE	= 50;
		public static final int ITEM_TYPE_PIERRE_BRUTE		=51;
		public static final int ITEM_TYPE_FARINE			=52;
		public static final int ITEM_TYPE_PLUME				=53;
		public static final int ITEM_TYPE_POIL				=54;
		public static final int ITEM_TYPE_ETOFFE			=55;
		public static final int ITEM_TYPE_CUIR				=56;
		public static final int ITEM_TYPE_LAINE				=57;
		public static final int ITEM_TYPE_GRAINE			=58;
		public static final int ITEM_TYPE_PEAU				=59;
		public static final int ITEM_TYPE_HUILE				=60;
		public static final int ITEM_TYPE_PELUCHE			=61;
		public static final int ITEM_TYPE_POISSON_VIDE		=62;
		public static final int ITEM_TYPE_VIANDE			=63;
		public static final int ITEM_TYPE_VIANDE_CONSERVEE	=64;
		public static final int ITEM_TYPE_QUEUE				=65;
		public static final int ITEM_TYPE_METARIA			=66;
		public static final int ITEM_TYPE_LEGUME			=68;
		public static final int ITEM_TYPE_VIANDE_COMESTIBLE	=69;
		public static final int ITEM_TYPE_TEINTURE			=70;
		public static final int ITEM_TYPE_EQUIP_ALCHIMIE	=71;
		public static final int ITEM_TYPE_OEUF_FAMILIER		=72;
		public static final int ITEM_TYPE_MAITRISE			=73;
		public static final int ITEM_TYPE_FEE_ARTIFICE		=74;
		public static final int ITEM_TYPE_PARCHEMIN_SORT	=75;
		public static final int ITEM_TYPE_PARCHEMIN_CARAC	=76;
		public static final int ITEM_TYPE_CERTIFICAT_CHANIL	=77;
		public static final int ITEM_TYPE_RUNE_FORGEMAGIE	=78;
		public static final int ITEM_TYPE_BOISSON			=79;
		public static final int ITEM_TYPE_OBJET_MISSION		=80;
		public static final int ITEM_TYPE_SAC_DOS			=81;
		public static final int ITEM_TYPE_BOUCLIER			=82;
		public static final int ITEM_TYPE_PIERRE_AME		=83;
		public static final int ITEM_TYPE_CLEFS				=84;
		public static final int ITEM_TYPE_PIERRE_AME_PLEINE	=85;
		public static final int ITEM_TYPE_POPO_OUBLI_PERCEP	=86;
		public static final int ITEM_TYPE_PARCHO_RECHERCHE	=87;
		public static final int ITEM_TYPE_PIERRE_MAGIQUE	=88;
		public static final int ITEM_TYPE_CADEAUX			=89;
		public static final int ITEM_TYPE_FANTOME_FAMILIER	=90;
		public static final int ITEM_TYPE_DRAGODINDE		=91;
		public static final int ITEM_TYPE_BOUFTOU			=92;
		public static final int ITEM_TYPE_OBJET_ELEVAGE		=93;
		public static final int ITEM_TYPE_OBJET_UTILISABLE	=94;
		public static final int ITEM_TYPE_PLANCHE			=95;
		public static final int ITEM_TYPE_ECORCE			=96;
		public static final int ITEM_TYPE_CERTIF_MONTURE	=97;
		public static final int ITEM_TYPE_RACINE			=98;
		public static final int ITEM_TYPE_FILET_CAPTURE		=99;
		public static final int ITEM_TYPE_SAC_RESSOURCE		=100;
		public static final int ITEM_TYPE_ARBALETE			=102;
		public static final int ITEM_TYPE_PATTE				=103;
		public static final int ITEM_TYPE_AILE				=104;
		public static final int ITEM_TYPE_OEUF				=105;
		public static final int ITEM_TYPE_OREILLE			=106;
		public static final int ITEM_TYPE_CARAPACE			=107;
		public static final int ITEM_TYPE_BOURGEON			=108;
		public static final int ITEM_TYPE_OEIL				=109;
		public static final int ITEM_TYPE_GELEE				=110;
		public static final int ITEM_TYPE_COQUILLE			=111;
		public static final int ITEM_TYPE_PRISME			=112;
		public static final int ITEM_TYPE_OBJET_VIVANT		=113;
		public static final int ITEM_TYPE_ARME_MAGIQUE		=114;
		public static final int ITEM_TYPE_FRAGM_AME_SHUSHU	=115;
		public static final int ITEM_TYPE_POTION_FAMILIER	=116;
		
	//Alignement
    public static final int ALIGNEMENT_NONE         = -1;
	public static final int ALIGNEMENT_NEUTRE		=	0;
	public static final int ALIGNEMENT_BONTARIEN	=	1;
	public static final int ALIGNEMENT_BRAKMARIEN	=	2;
	public static final int ALIGNEMENT_MERCENAIRE	=	3;
	
	//Elements 
	public static final int ELEMENT_NULL		=	-1;
	public static final int ELEMENT_NEUTRE		= 	0;
	public static final int ELEMENT_TERRE		= 	1;
	public static final int ELEMENT_EAU			= 	2;
	public static final int ELEMENT_FEU			= 	3;
	public static final int ELEMENT_AIR			= 	4;
	//Classes
	public static final int CLASS_FECA			= 	1;
	public static final int CLASS_OSAMODAS		= 	2;
	public static final int CLASS_ENUTROF		= 	3;
	public static final int CLASS_SRAM			=	4;
	public static final int CLASS_XELOR			=	5;
	public static final int CLASS_ECAFLIP		=	6;
	public static final int CLASS_ENIRIPSA		=	7;
	public static final int CLASS_IOP			=	8;
	public static final int CLASS_CRA			=	9;
	public static final int CLASS_SADIDA		= 	10;
	public static final int CLASS_SACRIEUR		=	11;
	public static final int CLASS_PANDAWA		=	12;
	//Sexes
	public static final int SEX_MALE 			=	0;
	public static final int SEX_FEMALE			=	1;
	//GamePlay
	public static final int MAX_EFFECTS_ID 		=	1500;
	//Buff a v�rifier en d�but de tour
	public static final int[] BEGIN_TURN_BUFF	=	{91,92,93,94,95,96,97,98,99,100,108};
	//Buff des Armes
	public static final int[] ARMES_EFFECT_IDS	=	{91,92,93,94,95,96,97,98,99,100,101};
	//Buff a ne pas booster en cas de CC
	public static final int[] NO_BOOST_CC_IDS	=	{101};
	//Invocation Statiques
	public static final int[] STATIC_INVOCATIONS 		= 	{282,556};//Arbre et Cawotte s'tout :p
	// Délais des timers de regen
	// En étant assis : 1 pdv/sec
	public static int SIT_TIMER_REGEN = 1000;
	// En étant debut : 0.5 pdv/sec (1 pdv/2sec) 
	public static int STAND_TIMER_REGEN = 2000;
	// IO : Bornes Id
	public static int IO_GUILD_ID = 1324; // Borne création guilde
	public static int IO_PHOENIX_ID = 542; // Borne phoenix
	// Nom des documents (swfs) : Documents d'avis de recherche
	public static String HUNT_DETAILS_DOC    = "71_0706251229"; // Pancarte d'explications
	public static String HUNT_FRAKACIA_DOC   = "63_0706251124";  // Frakacia Leukocythine
	public static String HUNT_AERMYNE_DOC 	 = "100_0706251214"; // Aermyne 'Braco' Scalptaras
	public static String HUNT_MARZWEL_DOC    = "96_0706251201";  // Marzwel le Gobelin
	public static String HUNT_BRUMEN_DOC     = "68_0706251126";  // Brumen Tinctorias
	public static String HUNT_MUSHA_DOC      = "94_0706251138";  // Musha l'Oni
	public static String HUNT_OGIVOL_DOC     = "69_0706251058";  // Ogivol Scarlacin
	public static String HUNT_PADGREF_DOC    = "61_0802081743";  // Padgref Demoel
	public static String HUNT_QILBIL_DOC     = "67_0706251223";  // Qil Bil
	public static String HUNT_ROK_DOC        = "93_0706251135";  // Rok Gnorok
	public static String HUNT_ZATOISHWAN_DOC = "98_0706251211";  // Zatoïshwan
	public static String HUNT_LETHALINE_DOC  = "65_0706251123";  // Léthaline Sigisbul
	public static String HUNT_NERVOES_DOC    = "64_0706251123";  // Nervoes Brakdoun
	
	// {(int)BorneId, (int)CellId, (str)SwfDocName, (int)MobId, (int)ItemFollow, (int)QuestId
	public static String[][] HUNTING_QUESTS = 
	{
		{"1988", "234", HUNT_DETAILS_DOC,    "-1",  "-1",  "-1"},
		{"1986", "161", HUNT_FRAKACIA_DOC,   "460", "6871", "30"},
		{"1985", "119", HUNT_QILBIL_DOC,     "481", "6873", "32"},
		{"1986", "120", HUNT_AERMYNE_DOC,    "446", "7350", "119"},
		{"1985", "149", HUNT_MARZWEL_DOC,    "554", "7353", "117"},
		{"1986", "150", HUNT_BRUMEN_DOC,     "464", "6874", "33"},
		{"1986", "179", HUNT_MUSHA_DOC,      "552", "7352", "116"},
		{"1986", "180", HUNT_OGIVOL_DOC,     "462", "6876", "35"},
		{"1985", "269", HUNT_PADGREF_DOC,    "459", "6870", "29"},
		{"1986", "270", HUNT_ROK_DOC,        "550", "7351", "115"},
		{"1985", "299", HUNT_ZATOISHWAN_DOC, "555", "7354", "118"},
		{"1986", "300", HUNT_LETHALINE_DOC,  "-1",  "-1",   "-1"},
		{"1985", "329", HUNT_NERVOES_DOC,    "-1",  "-1",   "-1"}
	};

	public static int getQuestByMobSkin(int mobSkin)
	{
		for(int v = 0;v < HUNTING_QUESTS.length;v++)
		{
			if(World.getMonstre(Integer.parseInt(HUNTING_QUESTS[v][3])) != null && World.getMonstre(Integer.parseInt(HUNTING_QUESTS[v][3])).getGfxID() == mobSkin)
			{
				return Integer.parseInt(HUNTING_QUESTS[v][5]);
			}
		}
		
		return -1;
	}
	
	public static int getSkinByHuntMob(int mobId)
	{
		for(int v = 0;v < HUNTING_QUESTS.length;v++)
			if(Integer.parseInt(HUNTING_QUESTS[v][3]) == mobId)
				return World.getMonstre(mobId).getGfxID();
		
		return -1;
	}
	
	public static int getItemByHuntMob(int mobId)
	{
		for(int v = 0;v < HUNTING_QUESTS.length;v++)
			if(Integer.parseInt(HUNTING_QUESTS[v][3]) == mobId)
				return Integer.parseInt(HUNTING_QUESTS[v][4]);
		
		return -1;
	}
	
	public static int getItemByMobSkin(int mobSkin)
	{
		for(int v = 0;v < HUNTING_QUESTS.length;v++)
			if(World.getMonstre(Integer.parseInt(HUNTING_QUESTS[v][3])) != null && World.getMonstre(Integer.parseInt(HUNTING_QUESTS[v][3])).getGfxID() == mobSkin)
				return Integer.parseInt(HUNTING_QUESTS[v][4]);
		
		return -1;
	}
	
	public static String getDocNameByBornePos(int borneId, int cellid)
	{
		for(int v = 0;v < HUNTING_QUESTS.length;v++)
			if(Integer.parseInt(HUNTING_QUESTS[v][0]) == borneId && Integer.parseInt(HUNTING_QUESTS[v][1]) == cellid)
				return HUNTING_QUESTS[v][2];
		
		return "";
	}
    
	//Action de M�tier {skillID,objetRecolt�,objSp�cial}
	public static final int[][] JOB_ACTION =
	{
		//Bucheron
		{101},{6,303},{39,473},{40,476},{10,460},{141,2357},{139,2358},{37,471},{154,7013},{33,461},{41,474},{34,449},{174,7925},{155,7016},{38,472},{35,470},{158,7963},
		//Mineur
		{48},{32},{24,312},{25,441},{26,442},{28,443},{56,445},{162,7032},{55,444},{29,350},{31,446},{30,313},{161,7033},
		//P�cheur
		{133},{128,598,1786},{128,1757,1759},{128,1750,1754},{124,603,1762},{124,1782,1790},{124,1844,607},{136,2187},{125,1847,1849},{125,1794,1796},{140,1799,1759},{129,600,1799},{129,1805,1807},{126,1779,1792},{130,1784,1788},{127,1801,1803},{131,602,1853},
		//Alchi
		{23},{68,421},{69,428},{71,395},{72,380},{73,593},{74,594},{160,7059},
		//Paysan
		{122},{47},{45,289,2018},{53,400,2032},{57,533,2036},{46,401,2021},{50,423,2026},{52,532,2029},{159,7018},{58,405},{54,425,2035},
		//Boulanger
		{109},{27},
		//Poissonier
		{135},
		//Boucher
		{132},
		//Chasseur
		{134},
		//Tailleur
		{64},{123},{63},
		//Bijoutier
		{11},{12},
		//Cordonnier
		{13},{14},
		//Forgeur Ep�e
		{145},{20},
		//Forgeur Marteau
		{144},{19},
		//Forgeur Dague
		{142},{18},
		//Forgeur Pelle
		{146},{21},
		//Forgeur Hache
		{65},{143},
		//Forgemage de Hache
		{115},
		//Forgemage de dagues
		{1},
		//Forgemage de marteau
		{116},
		//Forgemage d'�p�e
		{113},
		//Forgemage Pelle
		{117},
		//SculpteMage baton
		{120},
		//Sculptemage de baguette
		{119},
		//Sculptemage d'arc
		{118},
		//Costumage
		{165},{166},{167},
		//Cordomage
		{163},{164},
		//Joyaumage
		{169},{168},
		//Bricoleur
		{171},{182}
	};
	
	//protector
	public static final int[][] JOB_PROTECTORS = //{protectorId, itemId}
		{
			{684,7941},{684,2018},//bl� sac + or
			{685,7942},{685,2032},//orge
			{686,7943},{686,1671},//avoine
			{687,7944},{687,2021},//houblon
			{688,7945},{688,2026},//lin
			{689,7946},{689,2029},//seigle
			{690,7947},//riz
			{691,7948},//malt
			{692,7949},{692,2035},//chanvre
			{693,7971},//Fer
			{694,7972},//Cuivre
			{695,7973},//Bronze
			{696,7974},//kobalte
			{697,7975},//manganese
			{698,7976},//etain
			{699,7977},//silicate
			{700,7978},//argent
			{701,7979},//bauxite
			{702,7980},//or
			{703,7981},//dolomite
			{704,7945},//Lin
			{705,7949},//chanvre
			{706,7966},//trefle
			{707,7967},//menthe
			{708,7968},//orchide
			{709,7969},//edelweiss
			{710,7970},//graine de pandouille
			{711,7950},//frene
			{712,7951},//chataignier
			{713,7952},//noyer
			{714,7953},//chene
			{715,7958},//bambou
			{716,7955},//oliviet
			{717,7956},//erable
			{718,7957},//If
			{719,7954},//bambou
			{720,7996},//Kalyptus
			{721,7959},//merisier
			{722,7960},//ebene
			{723,7961},//bambou sombre
			{724,7962},//orme
			{725,7963},//bambou sacre
			{726,7982},{726,1790},//goujons
			{727,7983},{727,1846},//truite
			{728,7984},//poisson chaton
			{729,7985},//greu-vette
			{730,7986},{730,1759},//crabe
			{731,7987},//poissan pane
			{732,7988},{732,1849},//brochet
			{733,7989},{733,1796},//carpe
			{734,7990},{734,1807},//sardine
			{735,7991},{735,1799},//kralamoure
			{736,7992},{736,1792},//Bar Iton
			{737,7993},{737,1788},//raie
			{738,7994},{738,1803},//perche
			{739,7996},{739,1853}//requin
		};
	 
		public static int getProtectorLvl(int lvl)
		{
			if(lvl < 40)
				return 10;
			if(lvl < 80)
				return 20;
			if(lvl < 120)
				return 30;
			if(lvl < 160)
				return 40;
			if(lvl <= 200)
				return 50;
			return 10;
		}
	
	//Buff d�clench� en cas de frappe
	public static final int[] ON_HIT_BUFFS		=	{9,79,107,788};
	
	
	//Effets ID & Buffs
	public static final int EFFECT_PASS_TURN		= 	140;
	//Capture
	public static final int CAPTURE_MONSTRE			=	623;
	
	//pdv rendu
	public static final int PDV_RENDU = 108;
	//vole kamas
	public static final int VOLE_KAMAS = 130;
	
	//Methodes

	public static TreeMap<Integer, Character> getStartSortsPlaces(int classID)
	{
		TreeMap<Integer,Character> start = new TreeMap<Integer,Character>();
		switch(classID)
		{
			case CLASS_FECA:
				start.put(3,'b');//Attaque Naturelle
				start.put(6,'c');//Armure Terrestre
				start.put(17,'d');//Glyphe Agressif
			break;
			case CLASS_SRAM:
				start.put(61,'b');//Sournoiserie
				start.put(72,'c');//Invisibilit�
				start.put(65,'d');//Piege sournois
			break;
			case CLASS_ENIRIPSA:
				start.put(125,'b');//Mot Interdit
				start.put(128,'c');//Mot de Frayeur
				start.put(121,'d');//Mot Curatif
			break;
			case CLASS_ECAFLIP:
				start.put(102,'b');//Pile ou Face
				start.put(103,'c');//Chance d'ecaflip
				start.put(105,'d');//Bond du felin
			break;
			case CLASS_CRA:
				start.put(161,'b');//Fleche Magique
				start.put(169,'c');//Fleche de Recul
				start.put(164,'d');//Fleche Empoisonn�e(ex Fleche chercheuse)
			break;
			case CLASS_IOP:
				start.put(143,'b');//Intimidation
				start.put(141,'c');//Pression
				start.put(142,'d');//Bond
			break;
			case CLASS_SADIDA:
				start.put(183,'b');//Ronce
				start.put(200,'c');//Poison Paralysant
				start.put(193,'d');//La bloqueuse
			break;
			case CLASS_OSAMODAS:
				start.put(34,'b');//Invocation de tofu
				start.put(21,'c');//Griffe Spectrale
				start.put(23,'d');//Cri de l'ours
			break;
			case CLASS_XELOR:
				start.put(82,'b');//Contre
				start.put(81,'c');//Ralentissement
				start.put(83,'d');//Aiguille
			break;
			case CLASS_PANDAWA:
				start.put(686,'b');//Picole
				start.put(692,'c');//Gueule de bois
				start.put(687,'d');//Poing enflamm�
			break;
			case CLASS_ENUTROF:
				start.put(51,'b');//Lancer de Piece
				start.put(43,'c');//Lancer de Pelle
				start.put(41,'d');//Sac anim�
			break;
			case CLASS_SACRIEUR:
				start.put(432,'b');//Pied du Sacrieur
				start.put(431,'c');//Chatiment Os�
				start.put(434,'d');//Attirance
			break;
		}
		return start;
	}
	
	public static int getBasePdv(int classID)
	{
		return 50;
	}

	public static int getAggroByLevel(int lvl)
	{
		int aggro = 0;
		aggro = (int)(lvl/50);
		if(lvl>500)
			aggro = 3;
		return aggro;
    }

	public static int getGlyphColor(int spell)
	{
		switch(spell)
		{
			case 10://Enflamm�
			case 2033://Dopeul
				return 4;//Rouge
			case 12://Aveuglement
			case 2034://Dopeul
				return 3;
			case 13://Silence
			case 2035://Dopeul
				return 6;//Bleu
			case 15://Immobilisation
			case 2036://Dopeul
				return 5;//Vert
			case 17://Aggressif
			case 2037://Dopeul
				return 2;
			//case 476://Blop
			default:
				return 4;
		}
	}

	public static int getTrapsColor(int spell)
	{
		switch(spell)
		{
			case 65://Sournois
				return 7;
			case 69://Immobilisation
				return 10;
			case 71://Empoisonn�e
			case 2068://Dopeul
				return 9;
			case 73://Repulsif
				return 12;
			case 77://Silence
			case 2071://Dopeul
				return 11;
			case 79://Masse
			case 2072://Dopeul
				return 8;
			case 80://Mortel
				return 13;
			default:
				return 7;
		}
	}

	public static int getTotalCaseByJobLevel(int lvl)
	{
		if(lvl <10)return 2;
		if(lvl == 100)return 9;
		return (int)(lvl/20)+3;
	}
	
	public static int getChanceForMaxCase(int lvl)
	{
		if(lvl <10)return 50;
		return  54 + (int)((lvl/10)-1)*5;
	}
	
	public static int calculXpWinCraft(int lvl,int numCase)
	{
		if(lvl == 100)return 0;
		switch(numCase)
		{
			case 1:
				if(lvl<10)return 1;
			return 0;
			case 2:
				if(lvl<60)return 10;
			return 0;
			case 3:
				if(lvl>9 && lvl<80)return 25;
			return 0;
			case 4:
				if(lvl > 19)return 50;
			return 0;
			case 5:
				if(lvl > 39)return 100;
			return 0;
			case 6:
				if(lvl > 59)return 250;
			return 0;
			case 7:
				if(lvl > 79)return 500;
			return 0;
			case 8:
				if(lvl > 99)return 1000;
			return 0;
		}
		return 0;
	}

	public static boolean isJobAction(int a)
	{
		for(int v = 0;v < JOB_ACTION.length;v++)
		{
			if(JOB_ACTION[v][0] == a)return true;
		}
		return false;
	}

	public static int getObjectByJobSkill(int skID,boolean special)
	{
		for(int v = 0;v < JOB_ACTION.length;v++)if(JOB_ACTION[v][0] == skID)return (JOB_ACTION[v].length>1 && special?JOB_ACTION[v][2]:JOB_ACTION[v][1]);
		return -1;
	}

	public static int getChanceByNbrCaseByLvl(int lvl, int nbr)
	{
		if(nbr <= getTotalCaseByJobLevel(lvl)-2)return 100;//99.999... normalement, mais osef
		return getChanceForMaxCase(lvl);
	}

	public static boolean isMageJob(int id)
	{
		if((id>12 && id <50) || (id>61 && id <65))return true;
		return false;
	}
	
	public static void applyPlotIOAction(Player perso,int mID, int cID)
	{
		//G�re les differentes actions des "bornes" (IO des �motes)
		switch(mID)
		{
		case 2196://Cr�ation de guilde
			if(perso.isAway())return;
			if(perso.getGuild() != null || perso.getGuildMember() != null)
			{
				SocketManager.GAME_SEND_gC_PACKET(perso, "Ea");
				return;
			}
			if(!perso.hasItemTemplate(1575,1))//Guildalogemme
			{
				SocketManager.GAME_SEND_Im_PACKET(perso, "14");
				return;
			}
			SocketManager.GAME_SEND_gn_PACKET(perso);
		break;
		default:
			GameServer.addToLog("PlotIOAction non gere pour la map "+mID+" cell="+cID);
		}
	}
	
	//Baskwo:Concasseur
    public static int StatsToRunes(int Stat){
	switch(Stat){
	case 111://Pa
		return 1557;
	case 112: // Dommages
		return 7435;
	case 117://PO
		return 7438;
	case 118:// + 1 Force
		return 1519;
	case 119: //Agi
		return 1524;
	case 123://chance
		return 1525;
	case 124://Sagesse
		return 1521;
	case 125://vita
		return 1523;
	case 126: // intel
		return 1522;
	case 128://PM
		return 1558;
	case 138://Dommage %
		return 7436;
	case 158://pods
		return 7443;
	case 174://initiative
		return 7448;
	case 176://prospection
		return 7451;
	case 178:
		return 7434;
	case 182:// invoc
		return 7442;
	//R� Feu, Air, Eau, Terre, Neutre
	case 220:
		return 7447;
	case 225:
		return 7446;
	case 240:
		return 7452;
	case 241:
		return 7453;
	case 242:
		return 7454;
	case 243:
		return 7455;
	case 244:
		return 7456;
	//R� % Feu, Air, Eau, Terre, Neutre
	case 210:
		return 7457;
	case 211:
		return 7458;
	case 212:
		return 7560;
	case 213:
		return 7459;
	case 214:
		return 7460;
	case 666://Dommage renvoy�s
		return 7437;
	default:
		System.out.println("Rune du stat" + Stat + " non implant�e");
		return 0;
	}
}
	
}