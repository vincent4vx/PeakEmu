package org.peakemu;

import org.peakemu.game.GameServer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.peakemu.common.Constants;
import org.peakemu.common.Logger;
import fr.quatrevieux.crisis.config.RootConfig;

import org.peakemu.realm.RealmServer;

public class Ancestra {
    private RootConfig config;
    
    static public int CONFIG_GAME_PORT = 444;
    static public String GAMESERVER_IP = "";

    private static final String CONFIG_FILE = "config.txt";
    public static boolean isInit = false;
    public static long FLOOD_TIME = 60000;
    public static String CONFIG_MOTD_COLOR = "";
    public static boolean CONFIG_DEBUG = false;
    public static boolean CONFIG_POLICY = false;
    public static int CONFIG_START_LEVEL = 1;
    public static int CONFIG_START_KAMAS = 0;
    public static int CONFIG_SAVE_TIME = 10 * 60 * 10000;
    public static int CONFIG_DROP = 1;
    public static boolean CONFIG_ZAAP = false;
    public static int CONFIG_LOAD_DELAY = 60000;
    public static int CONFIG_RELOAD_MOB_DELAY = 18000000;
    public static int CONFIG_PLAYER_LIMIT = 30;
    public static boolean CONFIG_IP_LOOPBACK = true;
    public static boolean ALLOW_MULE_PVP = false;
    public static int XP_PVM = 1;
    public static int KAMAS = 1;
    public static int HONOR = 1;
    public static int XP_METIER = 1;
    public static int FM = 1;
    public static boolean CONFIG_USE_MOBS = false;
    public static boolean CONFIG_USE_IP = false;
    public static GameServer gameServer;
    public static RealmServer realmServer;
    public static boolean isRunning = false;
//    public static BufferedWriter Log_GameSock;
//    public static BufferedWriter Log_Game;
//    public static BufferedWriter Log_Realm;
//    public static BufferedWriter Log_MJ;
//    public static BufferedWriter Log_RealmSock;
//    public static BufferedWriter Log_Shop;
    public static boolean canLog;
    public static boolean isSaving = false;
    public static boolean AURA_SYSTEM = false;
    //Arene
    public static ArrayList<Integer> arenaMap = new ArrayList<Integer>(8);
    public static int CONFIG_ARENA_TIMER = 10 * 60 * 1000;// 10 minutes
    //BDD
    public static int CONFIG_DB_COMMIT = 30 * 1000;
    //Inactivit�
    public static int CONFIG_MAX_IDLE_TIME = 1200000;//En millisecondes 20min ici
    //UseCompactDATA
    public static boolean CONFIG_SOCKET_USE_COMPACT_DATA = false;
    public static int CONFIG_SOCKET_TIME_COMPACT_DATA = 200;
    public static int CONFIG_NB_COMPTE_PLAYER = 5;
    //Prismes
    public static int PRISMES_DELAIS_NEW_POSE = 60;
    private static Map<Integer, Long> WhenHasPosePrism = new HashMap<Integer, Long>();
    private static int PosePrism = 0;
    public static ArrayList<Integer> CartesWithoutPrismes = new ArrayList<Integer>();
    //limite ip par combats
    public static int FIGHT_IP = 2;



    public static void loadConfiguration() {
        boolean log = false;
        try {
            BufferedReader config = new BufferedReader(new FileReader(CONFIG_FILE));
            String line = "";
            while ((line = config.readLine()) != null) {
                if (line.split("=").length == 1) {
                    continue;
                }
                String param = line.split("=")[0].trim();
                String value = line.split("=")[1].trim();
                if (param.equalsIgnoreCase("DEBUG")) {
                    if (value.equalsIgnoreCase("true")) {
                        Ancestra.CONFIG_DEBUG = true;
                        System.out.println("Mode Debug: On");
                    }
                } else if (param.equalsIgnoreCase("SEND_POLICY")) {
                    if (value.equalsIgnoreCase("true")) {
                        Ancestra.CONFIG_POLICY = true;
                    }
                } else if (param.equalsIgnoreCase("LOG")) {
                    if (value.equalsIgnoreCase("true")) {
                        log = true;
                    }
                } else if (param.equalsIgnoreCase("START_KAMAs")) {
                    Ancestra.CONFIG_START_KAMAS = Integer.parseInt(value);
                    if (Ancestra.CONFIG_START_KAMAS < 0) {
                        Ancestra.CONFIG_START_KAMAS = 0;
                    }
                    if (Ancestra.CONFIG_START_KAMAS > 1000000000) {
                        Ancestra.CONFIG_START_KAMAS = 1000000000;
                    }
                } else if (param.equalsIgnoreCase("START_LEVEL")) {
                    Ancestra.CONFIG_START_LEVEL = Integer.parseInt(value);
                    if (Ancestra.CONFIG_START_LEVEL < 1) {
                        Ancestra.CONFIG_START_LEVEL = 1;
                    }
                    if (Ancestra.CONFIG_START_LEVEL > 200) {
                        Ancestra.CONFIG_START_LEVEL = 200;
                    }
                } else if (param.equalsIgnoreCase("KAMAS")) {
                    Ancestra.KAMAS = Integer.parseInt(value);
                } else if (param.equalsIgnoreCase("HONOR")) {
                    Ancestra.HONOR = Integer.parseInt(value);
                } else if (param.equalsIgnoreCase("SAVE_TIME")) {
                    Ancestra.CONFIG_SAVE_TIME = Integer.parseInt(value) * 60 * 1000000000;
                } else if (param.equalsIgnoreCase("XP_PVM")) {
                    Ancestra.XP_PVM = Integer.parseInt(value);
                } else if (param.equalsIgnoreCase("FM")) {
                    Ancestra.FM = Integer.parseInt(value);
                } else if (param.equalsIgnoreCase("DROP")) {
                    Ancestra.CONFIG_DROP = Integer.parseInt(value);
                } else if (param.equalsIgnoreCase("MAPS_NO_PRISMES")) {
                    for (String curID : value.split(",")) {
                        Ancestra.CartesWithoutPrismes.add(Integer.parseInt(curID));
                    }
                } else if (param.equalsIgnoreCase("PRISMES_DELAIS_NEW_POSE")) {
                    PRISMES_DELAIS_NEW_POSE = Integer.parseInt(value);
                } else if (param.equalsIgnoreCase("LOCALIP_LOOPBACK")) {
                    if (value.equalsIgnoreCase("true")) {
                        Ancestra.CONFIG_IP_LOOPBACK = true;
                    }
                } else if (param.equalsIgnoreCase("ZAAP")) {
                    if (value.equalsIgnoreCase("true")) {
                        Ancestra.CONFIG_ZAAP = true;
                    }
                } else if (param.equalsIgnoreCase("USE_IP")) {
                    if (value.equalsIgnoreCase("true")) {
                        Ancestra.CONFIG_USE_IP = true;
                    }
                } else if (param.equalsIgnoreCase("MOTD_COLOR")) {
                    Ancestra.CONFIG_MOTD_COLOR = value;
                } else if (param.equalsIgnoreCase("XP_METIER")) {
                    Ancestra.XP_METIER = Integer.parseInt(value);
                } else if (param.equalsIgnoreCase("FLOODER_TIME")) {
                    Ancestra.FLOOD_TIME = Integer.parseInt(value);
                } else if (param.equalsIgnoreCase("USE_MOBS")) {
                    Ancestra.CONFIG_USE_MOBS = value.equalsIgnoreCase("true");
                } else if (param.equalsIgnoreCase("LOAD_ACTION_DELAY")) {
                    Ancestra.CONFIG_LOAD_DELAY = (Integer.parseInt(value) * 1000);
                } else if (param.equalsIgnoreCase("PLAYER_LIMIT")) {
                    Ancestra.CONFIG_PLAYER_LIMIT = Integer.parseInt(value);
                } else if (param.equalsIgnoreCase("ARENA_MAP")) {
                    for (String curID : value.split(",")) {
                        Ancestra.arenaMap.add(Integer.parseInt(curID));
                    }
                } else if (param.equalsIgnoreCase("ARENA_TIMER")) {
                    Ancestra.CONFIG_ARENA_TIMER = Integer.parseInt(value);
                } else if (param.equalsIgnoreCase("AURA_SYSTEM")) {
                    Ancestra.AURA_SYSTEM = value.equalsIgnoreCase("true");
                } else if (param.equalsIgnoreCase("ALLOW_MULE_PVP")) {
                    Ancestra.ALLOW_MULE_PVP = value.equalsIgnoreCase("true");
                } else if (param.equalsIgnoreCase("NB_COMPTE_PLAYER")) {
                    /* -- Add par Lenders --*/
                    Ancestra.CONFIG_NB_COMPTE_PLAYER = Integer.parseInt(value);
                } /* -- Fin add par Lenders -- */ else if (param.equalsIgnoreCase("FIGHT_IP")) { //limite fight perso / ip
                    Ancestra.FIGHT_IP = Integer.parseInt(value);
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Fichier de configuration non existant ou illisible");
            System.out.println("Fermeture du serveur");
            System.exit(1);
        }
        if (CONFIG_DEBUG) {
            Constants.DEBUG_MAP_LIMIT = 20000;
        }
        canLog = true;

    }

    public void setWhenHasPosePrism(Map<Integer, Long> whenHasPosePrism) {
        WhenHasPosePrism = whenHasPosePrism;
    }

    public static Map<Integer, Long> getWhenHasPosePrism() {
        return WhenHasPosePrism;
    }

    public static void setPosePrism(int posePrism) {
        PosePrism = posePrism;
    }

    public static int getPosePrism() {
        return PosePrism;
    }

    public static void addToMjLog(String str) {
        if (!canLog) {
            return;
        }
        Peak.worldLog.addToLog(Logger.Level.INFO, str);
    }

    public static void addToShopLog(String str) {
        if (!canLog) {
            return;
        }
        Peak.worldLog.addToLog(Logger.Level.INFO, str);
    }

    public static String makeHeader() {
        StringBuilder mess = new StringBuilder();
        mess.append("Peak Emu v" + Constants.SERVER_VERSION);
        mess.append("\nPar " + Constants.SERVER_MAKER + " pour Dofus " + Constants.CLIENT_VERSION);
        return mess.toString();
    }
}
