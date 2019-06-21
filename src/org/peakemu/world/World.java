package org.peakemu.world;

import org.peakemu.world.config.WorldConfig;
import org.peakemu.world.handler.AreaHandler;
import org.peakemu.world.handler.SessionHandler;
import org.peakemu.world.handler.MapHandler;
import org.peakemu.database.Database;
import org.peakemu.Ancestra;
import org.peakemu.objects.Fireworks;
import org.peakemu.objects.Account;
import org.peakemu.objects.player.Player;
import org.peakemu.objects.Guild;
import org.peakemu.game.GameServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.peakemu.Peak;

import org.peakemu.common.Constants;
import org.peakemu.common.Logger;
import org.peakemu.common.SocketManager;
import org.peakemu.game.out.InfoMessage;

import org.peakemu.realm.RealmClient;
import org.peakemu.realm.out.HostList;
import org.peakemu.world.handler.AccountHandler;
import org.peakemu.world.handler.ActionHandler;
import org.peakemu.world.handler.AlignementHandler;
import org.peakemu.world.handler.ChatHandler;
import org.peakemu.world.handler.ExchangeHandler;
import org.peakemu.world.handler.ExpHandler;
import org.peakemu.world.handler.fight.FightHandler;
import org.peakemu.world.handler.GuildHandler;
import org.peakemu.world.handler.InteractiveObjectHandler;
import org.peakemu.world.handler.ItemHandler;
import org.peakemu.world.handler.JobHandler;
import org.peakemu.world.handler.LifeHandler;
import org.peakemu.world.handler.MonsterHandler;
import org.peakemu.world.handler.MountHandler;
import org.peakemu.world.handler.NPCHandler;
import org.peakemu.world.handler.PlayerHandler;
import org.peakemu.world.handler.SpellHandler;
import org.peakemu.world.handler.VarHandler;
import org.peakemu.world.handler.WaypointHandler;

public class World {
    public enum State{
        UNREACHABLE(0),
        REACHABLE(1),
        SAVING(2)
        ;
        final private int stateId;

        private State(int stateId) {
            this.stateId = stateId;
        }

        public int getStateId() {
            return stateId;
        }
        
    }
    @Deprecated
    static private World instance;

    final private AreaHandler areaHandler;
    final private MapHandler mapHandler;
    final private SessionHandler sessionHandler;
    final private AccountHandler accountHandler;
    final private PlayerHandler playerHandler;
    final private ItemHandler itemHandler;
    final private InteractiveObjectHandler interactiveObjectHandler;
    final private LifeHandler lifeHandler;
    final private ActionHandler actionHandler;
    final private FightHandler fightHandler;
    final private WaypointHandler waypointHandler;
    final private MonsterHandler monsterHandler;
    final private AlignementHandler alignementHandler;
    final private GuildHandler guildHandler;
    final private ExchangeHandler exchangeHandler;
    final private ChatHandler chatHandler;
    final private VarHandler varHandler;
    final private NPCHandler npcHandler;
    final private MountHandler mountHandler;
    final private JobHandler jobHandler;
    final private SpellHandler spellHandler;
    final private ExpHandler expHandler;
    final private WorldConfig config;
    
    private State state = State.REACHABLE;
    
    final private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private static ArrayList<Couple<Integer, Integer>> CraftsExchanges = new ArrayList<Couple<Integer, Integer>>(); // NPC_Exchange

    private static Map<Integer, Player> Married = new TreeMap<Integer, Player>();
    private static Map<Integer, Fireworks> Fireworks = new TreeMap<Integer, Fireworks>();
    //Quest
    private static Map<Integer, Map<String, String>> quests = new HashMap<Integer, Map<String, String>>();
    private static Map<Integer, Map<String, String>> questSteps = new HashMap<Integer, Map<String, String>>();
    private static Map<Integer, Map<String, String>> questObjetives = new HashMap<Integer, Map<String, String>>();

    private static int nextHdvID;	//Contient le derniere ID utilisé pour crée un HDV, pour obtenir un ID non utilisé il faut impérativement l'incrémenter
    private static int nextLigneID;	//Contient le derniere ID utilisé pour crée une ligne dans un HDV
    //Full morph
    private static Map<Integer, Map<String, String>> _fullmorphs = new HashMap<Integer, Map<String, String>>();

    private static int saveTry = 1;

    private static byte _GmAccess = 0;

    public static int _time;

    public static Map<Integer, String> _subareaGroups = new HashMap<Integer, String>();

    public World(AreaHandler areaHandler, MapHandler mapHandler, WorldConfig config, SessionHandler sessionHandler, AccountHandler accountHandler, PlayerHandler playerHandler, ItemHandler itemHandler, InteractiveObjectHandler interactiveObjectHandler, LifeHandler lifeHandler, ActionHandler actionHandler, FightHandler fightHandler, WaypointHandler waypointHandler, MonsterHandler monsterHandler, AlignementHandler alignementHandler, GuildHandler guildHandler, ExchangeHandler exchangeHandler, ChatHandler chatHandler, VarHandler varHandler, NPCHandler npcHandler, MountHandler mountHandler, JobHandler jobHandler, SpellHandler spellHandler, ExpHandler expHandler) {
        this.areaHandler = areaHandler;
        this.mapHandler = mapHandler;
        this.config = config;
        this.sessionHandler = sessionHandler;
        this.accountHandler = accountHandler;
        this.playerHandler = playerHandler;
        this.itemHandler = itemHandler;
        this.interactiveObjectHandler = interactiveObjectHandler;
        this.lifeHandler = lifeHandler;
        this.actionHandler = actionHandler;
        this.fightHandler = fightHandler;
        this.waypointHandler = waypointHandler;
        this.monsterHandler = monsterHandler;
        this.alignementHandler = alignementHandler;
        this.guildHandler = guildHandler;
        this.exchangeHandler = exchangeHandler;
        this.chatHandler = chatHandler;
        this.varHandler = varHandler;
        this.npcHandler = npcHandler;
        this.mountHandler = mountHandler;
        this.jobHandler = jobHandler;
        this.spellHandler = spellHandler;
        this.expHandler = expHandler;
        
        instance = this;
    }

    public AreaHandler getAreaHandler() {
        return areaHandler;
    }

    @Deprecated
    public static World getInstance() {
        return instance;
    }

    public SessionHandler getSessionHandler() {
        return sessionHandler;
    }

    public AccountHandler getAccountHandler() {
        return accountHandler;
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public ItemHandler getItemHandler() {
        return itemHandler;
    }

    public InteractiveObjectHandler getInteractiveObjectHandler() {
        return interactiveObjectHandler;
    }

    public ActionHandler getActionHandler() {
        return actionHandler;
    }

    public FightHandler getFightHandler() {
        return fightHandler;
    }

    public WaypointHandler getWaypointHandler() {
        return waypointHandler;
    }

    public MonsterHandler getMonsterHandler() {
        return monsterHandler;
    }

    public AlignementHandler getAlignementHandler() {
        return alignementHandler;
    }

    public GuildHandler getGuildHandler() {
        return guildHandler;
    }

    public ExchangeHandler getExchangeHandler() {
        return exchangeHandler;
    }

    public ChatHandler getChatHandler() {
        return chatHandler;
    }

    public VarHandler getVarHandler() {
        return varHandler;
    }

    public NPCHandler getNpcHandler() {
        return npcHandler;
    }

    public MountHandler getMountHandler() {
        return mountHandler;
    }

    public LifeHandler getLifeHandler() {
        return lifeHandler;
    }

    public JobHandler getJobHandler() {
        return jobHandler;
    }

    public SpellHandler getSpellHandler() {
        return spellHandler;
    }

    public ExpHandler getExpHandler() {
        return expHandler;
    }

    public State getState() {
        return state;
    }
    
    public void setState(State state){
        this.state = state;
        for(RealmClient client : sessionHandler.getRealmClients()){
            client.send(HostList.createByWorld(this));
        }
    }

    public WorldConfig getConfig() {
        return config;
    }
    
    public static class Drop {

        private int _itemID;
        private int _prosp;
        private float _taux;
        private int _max;

        public Drop(int itm, int p, float t, int m) {
            _itemID = itm;
            _prosp = p;
            _taux = t;
            _max = m;
        }

        public void setMax(int m) {
            _max = m;
        }

        public int get_itemID() {
            return _itemID;
        }

        public int getMinProsp() {
            return _prosp;
        }

        public float get_taux() {
            return _taux;
        }

        public int get_max() {
            return _max;
        }
    }
	//PNJ echange

    public static class Couple<L, R> {

        public L first;
        public R second;

        public Couple(L s, R i) {
            this.first = s;
            this.second = i;
        }
    }

    public void createWorld() {
        System.out.println("====>Donnees statique<====");
        
        actionHandler.load();
        exchangeHandler.load();
        varHandler.load();
        
        expHandler.load();
        spellHandler.load();
        monsterHandler.load();
        itemHandler.load();
        areaHandler.load();
        jobHandler.load();
        interactiveObjectHandler.load();
        mapHandler.load();
        alignementHandler.load();
        
        int nbr = 0;
        
        fightHandler.load();
        npcHandler.load();
        
        System.out.print("Chargement des Drops: ");
        Database.LOAD_DROPS();
        System.out.println("Ok !");
        System.out.println("Chargement des Fireworks: ");
        Database.LOAD_FIREWORKS();
        System.out.println(Fireworks.size() + " Fireworks ont ete charges");
        System.out.println("====>Donnees dynamique<====");
        System.out.print("Mise a 0 des logged: ");
        Database.LOGGED_ZERO();
        System.out.println("Ok !");
        
        accountHandler.load();
        mountHandler.loadMounts();
        playerHandler.load();
        guildHandler.load();
        mountHandler.loadParks();
        
        waypointHandler.load();
        
        System.out.println(nbr + " zaapis chargees");
        System.out.print("Chargement des BAN_IP: ");
        nbr = Database.LOAD_BANIP();
        System.out.println(nbr + " BAN_IP chargees");
        //fullmorph
        Database.LOAD_FULLMORPHS();
        //Quests
        Database.LOAD_QUESTS();
        System.out.println(quests.size() + " quetes ont ete chargees.");
        Database.LOAD_QUEST_STEPS();
        Database.LOAD_QUEST_OBJECTIVES();
        
        lifeHandler.initSpwanMonsters();
        lifeHandler.initRefreshIO();
        lifeHandler.initFixedGroups();
        
        System.out.print("GC : ");
        System.gc();
        System.out.println("Ok");
        
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(state != State.SAVING){
                    sessionHandler.sendToOnline(new InfoMessage(InfoMessage.Type.ERROR).addMessage(164));
                    saveAll();
                    sessionHandler.sendToOnline(new InfoMessage(InfoMessage.Type.ERROR).addMessage(165));
                }
            }
        }, config.getSaveDelay(), config.getSaveDelay(), TimeUnit.SECONDS);
        
        actionHandler.initLiveActions();
    }

    @Deprecated
    public static Account getCompte(int guid) {
        return instance.accountHandler.getAccountById(guid);
    }
    //quests

//    public static NPCQuestion getNPCQuestion(int guid, Player player) {
//        NPCQuestion baseQuestion = NPCQuestions.get(guid);

//        Entry<Integer, Map<String, String>> questObjective = getObjectiveByOptQuestion(guid);
//        if (questObjective != null && player.hasObjective(questObjective.getKey())) // Il y a un objectif de quete avec cette question
//        {
//            NPCQuestion questQuestion = new NPCQuestion(guid, questObjective.getValue().get("optAnswer"), "", "", 0);
//            return questQuestion;
//        }
//
//        return baseQuestion;
//    }

    public MapHandler getMapHandler() {
        return mapHandler;
    }

    @Deprecated
    public static Account getCompteByName(String name) {
        return instance.accountHandler.getAccountByName(name);
    }

    @Deprecated
    public static ItemTemplate getObjTemplate(int id) {
        return instance.itemHandler.getTemplateById(id);
    }

    public void saveAll() {
        setState(State.SAVING);

        try {
            GameServer.addToLog("Lancement de la sauvegarde du Monde...");
            Ancestra.isSaving = true;

            sessionHandler.saveAll();
            guildHandler.saveAll();
            alignementHandler.saveAll();
            mountHandler.saveAll();
            
            GameServer.addToLog("Sauvegarde effectuee !");
        } catch (Exception e) {
            Peak.worldLog.addToLog(Logger.Level.ERROR, "Error during saving : %s (go to error log for more details)", e);
            Peak.errorLog.addToLog(e);
        } finally {
            setState(State.REACHABLE);
            Ancestra.isSaving = false;
        }
    }
    
    public void stop(){
        saveAll();
        lifeHandler.stop();
    }

    public static void addCraftExchange(int id, int npc) // NPC_Exchange
    {
        CraftsExchanges.add(new Couple<Integer, Integer>(id, npc));
    }

    public static boolean canDoCraft(int item, int npc) // NPC_Exchange
    {
        for (Couple<Integer, Integer> entry : CraftsExchanges) {
            if (entry.first == item && entry.second == npc) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNpcExchange(int npc) // NPC_Exchange
    {
        for (Couple<Integer, Integer> entry : CraftsExchanges) {
            if (entry.second == npc) {
                return true;
            }
        }

        return false;
    }

    public static Couple<Integer, Integer> getObjectByIngredient(Map<Integer, Integer> ingredients, int npc) // NPC_Exchange
    {
//        for (Map.Entry<Integer, ArrayList<Integer>> entryCraft : Crafts.entrySet()) {
//            for (int curRecetteId : entryCraft.getValue()) {
//                ArrayList<Couple<Integer, Integer>> curRecette = Recettes.get(curRecetteId);
//                if (!canDoCraft(entryCraft.getKey(), npc)) {
//                    continue; // Craft infaisable par ce npc
//                }
//                if (curRecette.size() != ingredients.size()) {
//                    continue;
//                }
//                boolean ok = true;
//                ArrayList<Integer> listNbCraft = new ArrayList<Integer>();
//                for (Couple<Integer, Integer> c : curRecette) {
//                    //si ingredient non pr�sent ou mauvaise quantit�
//                    if (ingredients.get(c.first) == null || (ingredients.get(c.first) != null && ingredients.get(c.first) < c.second)) {
//                        ok = false;
//                    } else { // Le personnage a l'ingrédient courant en quantité suffisante
//                        listNbCraft.add(ingredients.get(c.first) / c.second);
//                    }
//                }
//                if (ok) {
//                    // On détermine combien de fois l'objet peut être crafté avec le nbre d'ingrédients fourni par le player
//                    int maxNbCraft = -1;
//                    for (int curNbCraft : listNbCraft) {
//                        if (maxNbCraft == -1) {
//                            maxNbCraft = curNbCraft;
//                            continue;
//                        } else {
//                            if (curNbCraft > maxNbCraft) {
//                                maxNbCraft = curNbCraft;
//                            }
//                        }
//                    }
//                    for (int curNbCraft : listNbCraft) {
//                        if (curNbCraft < maxNbCraft) {
//                            maxNbCraft = curNbCraft;
//                        }
//                    }
//                    return new Couple<Integer, Integer>(entryCraft.getKey(), maxNbCraft);
//                }
//            }
//        } // Fin boucle tests crafts, aucun trouvé : 
        return null;
    }

    @Deprecated
    public static ItemSet getItemSet(int tID) {
        return instance.itemHandler.getItemSetById(tID);
    }

    public static String PrismesGeoposition(int alignement) {
        /*String str = "";
         boolean first = false;
         int subareas = 0;
         for (SubArea subarea : SubAreas.values()) {
         if (!subarea.getConquistable())
         continue;
         if (first)
         str += ";";
         str += subarea.getSpriteId() + "," + (subarea.getalignement() == 0 ? -1 : subarea.getalignement()) + ",0,";
         if (World.getPrisme(subarea.getPrismeID()) == null)
         str += 0 + ",1";
         else
         str += (subarea.getPrismeID() == 0 ? 0 : World.getPrisme(subarea.getPrismeID()).getMap()) + ",1";
         first = true;
         subareas++;
         }
         if (alignement == 1)
         str += "|" + Area._bontas;
         else if (alignement == 2)
         str += "|" + Area._brakmars;
         str += "|" + Areas.size() + "|";
         first = false;		for (SubArea subarea : SubAreas.values()) {
         //			if (!subarea.getConquistable())
         //				continue;
         //			if (first)
         //				str += ";";
         //			str += subarea.getSpriteId() + "," + (subarea.getalignement() == 0 ? -1 : subarea.getalignement()) + ",0,";
         //			if (World.getPrisme(subarea.getPrismeID()) == null)
         //				str += 0 + ",1";
         //			else
         //				str += (subarea.getPrismeID() == 0 ? 0 : World.getPrisme(subarea.getPrismeID()).getMap()) + ",1";
         //			first = true;
         //			subareas++;
         //		}
         for (Area area : Areas.values()) {
         if (area.getalignement() == 0)
         continue;
         if (first)
         str += ";";
         str += area.getSpriteId() + "," + area.getalignement() + ",1," + (area.getPrismeID() == 0 ? 0 : 1);
         first = true;
         }
         if (alignement == 1)
         str = Area._bontas + "|" + subareas + "|" + (subareas - (SubArea._bontas + SubArea._brakmars)) + "|" + str;
         else if (alignement == 2)
         str = Area._brakmars + "|" + subareas + "|" + (subareas - (SubArea._bontas + SubArea._brakmars)) + "|" + str;
         return str;*/
        return "";
    }

    public void showPrismes(Player perso) {
        for (SubArea subarea : areaHandler.getSubAreas()) {
            if (subarea.getAlignement() != 0)
                SocketManager.GAME_SEND_am_ALIGN_PACKET_TO_SUBAREA(perso, subarea.getId() + "|" + subarea.getAlignement() + "|1");
        }
    }

    @Deprecated
    public static Guild getGuild(int i) {
        return instance.getGuildHandler().getGuildById(i);
    }

    public static int getZaapCellIdByMapId(short i) {
        for (Entry<Integer, Integer> zaap : Constants.ZAAPS.entrySet()) {
            if (zaap.getKey() == i) {
                return zaap.getValue();
            }
        }
        return -1;
    }

    public static boolean isArenaMap(int mapID) {
        for (int curID : Ancestra.arenaMap) {
            if (curID == mapID) {
                return true;
            }
        }
        return false;
    }

    public static byte getGmAccess() {
        return _GmAccess;
    }

    public static void setGmAccess(byte GmAccess) {
        _GmAccess = GmAccess;
    }

    public synchronized static int getNextHdvID()//ATTENTION A NE PAS EXECUTER POUR RIEN CETTE METHODE CHANGE LE PROCHAIN ID DE L'HDV LORS DE SON EXECUTION
    {
        nextHdvID++;
        return nextHdvID;
    }

    public synchronized static void setNextHdvID(int nextID) {
        nextHdvID = nextID;
    }

    public synchronized static int getNextLigneID() {
        nextLigneID++;
        return nextLigneID;
    }

    public synchronized static void setNextLigneID(int ligneID) {
        nextLigneID = ligneID;
    }
	//full morph

    public static void addFullMorph(int morphID, int stuffId, int gfxID, String Spells, String name) {
        if (_fullmorphs.get(morphID) != null) {
            return;
        }

        _fullmorphs.put(morphID, new HashMap<String, String>());

        _fullmorphs.get(morphID).put("stuffId", stuffId + "");
        _fullmorphs.get(morphID).put("gfxid", gfxID + "");
        _fullmorphs.get(morphID).put("spells", Spells);
        _fullmorphs.get(morphID).put("name", name);
    }

    public static Map<String, String> getFullMorph(int morphID) {
        return _fullmorphs.get(morphID);
    }

    public static Map<Integer, Map<String, String>> getFullMorphs() {
        return _fullmorphs;
    }

    //Quests
    public static void addQuest(int questId, String steps, int startQuestion, int endQuestion,
            int minLvl, int questRequired) {
        if (quests.get(questId) != null) {
            return;
        }

        quests.put(questId, new HashMap<String, String>());

        quests.get(questId).put("steps", steps);
        quests.get(questId).put("startQuestion", startQuestion + "");
        quests.get(questId).put("endQuestion", endQuestion + "");
        quests.get(questId).put("minLvl", minLvl + "");
        quests.get(questId).put("questRequired", questRequired + "");
    }

    public static void addQuestStep(int stepId, String objectives,
            int question, int gainExp, int gainKamas, String gainItems) {
        if (questSteps.get(stepId) != null) {
            return;
        }

        questSteps.put(stepId, new HashMap<String, String>());

        questSteps.get(stepId).put("objectives", objectives);
        questSteps.get(stepId).put("question", question + "");
        questSteps.get(stepId).put("gainExp", gainExp + "");
        questSteps.get(stepId).put("gainKamas", gainKamas + "");
        questSteps.get(stepId).put("gainItems", gainItems);
    }

    public static void addQuestObjective(int id, String type, String args,
            int npcTarget, int questionTarget, int answerTarget) {
        if (questObjetives.get(id) != null) {
            return;
        }

        questObjetives.put(id, new HashMap<String, String>());
        questObjetives.get(id).put("type", type);
        questObjetives.get(id).put("args", args);
        questObjetives.get(id).put("npcTarget", npcTarget + "");
        questObjetives.get(id).put("optQuestion", questionTarget + "");
        questObjetives.get(id).put("optAnswer", answerTarget + "");
    }

    public static Map<String, String> getQuest(int questId) {
        return quests.get(questId);
    }

    public static Map<String, String> getStep(int StepId) {
        return questSteps.get(StepId);
    }

    public static Map<String, String> getObjective(int objectiveId) {
        return questObjetives.get(objectiveId);
    }

    public static Map<Integer, Map<String, String>> getObjectiveByNpcTarget(int npcId) {
        Map<Integer, Map<String, String>> results = new HashMap<Integer, Map<String, String>>();

        for (Entry<Integer, Map<String, String>> entry : questObjetives.entrySet()) {
            if (entry.getValue().get("npcTarget").equals(npcId + "")) {
                results.put(entry.getKey(), entry.getValue());
            }
        }
        if (!results.isEmpty()) {
            return results;
        }

        return null; // Aucun objectif avec le pnj
    }

    public static Map<Integer, Map<String, String>> getObjectiveByOptAnswer(int answerId) {

        Map<Integer, Map<String, String>> results = new HashMap<Integer, Map<String, String>>();

        for (Entry<Integer, Map<String, String>> entry : questObjetives.entrySet()) {
            if (entry.getValue().get("optAnswer").equals(answerId + "")) {
                results.put(entry.getKey(), entry.getValue());
            }
        }

        if (!results.isEmpty()) {
            return results;
        }

        return null; // Aucun objectif avec le pnj
    }

    public static Entry<Integer, Map<String, String>> getObjectiveByOptQuestion(int questionId) {

        for (Entry<Integer, Map<String, String>> entry : questObjetives.entrySet()) {
            if (entry.getValue().get("optQuestion").equals(questionId + "")) {
                return entry;
            }
        }

        return null; // Aucun objectif avec le pnj
    }

//    public static Player getMarried(int ordre) {
//        return Married.get(ordre);
//    }
//
//    public static void AddMarried(int ordre, Player player) {
//        Player Perso = Married.get(ordre);
//        if (Perso != null) {
//            if (player.getSpriteId() == Perso.getSpriteId()) // Si c'est le meme joueur...
//            {
//                return;
//            }
//            if (Perso.isOnline())// Si player en ligne...
//            {
//                Married.remove(ordre);
//                Married.put(ordre, player);
//                return;
//            }
//
//            return;
//        } else {
//            Married.put(ordre, player);
//            return;
//        }
//    }

//    public static void PriestRequest(Player player, GameMap carte, int IdPretre) {
//        Player Homme = Married.get(0);
//        Player Femme = Married.get(1);
//        if (Homme.getWife() != 0) {
//            SocketManager.GAME_SEND_MESSAGE_TO_MAP(carte, Homme.getName() + " est deja marie!", Ancestra.CONFIG_MOTD_COLOR);
//            return;
//        }
//        if (Femme.getWife() != 0) {
//            SocketManager.GAME_SEND_MESSAGE_TO_MAP(carte, Femme.getName() + " est deja marie!", Ancestra.CONFIG_MOTD_COLOR);
//            return;
//        }
//        SocketManager.GAME_SEND_cMK_PACKET_TO_MAP(player.getMap(), "", -1, "Prêtre", player.getName() + " acceptez-vous d'épouser " + getMarried((player.get_sexe() == 1 ? 0 : 1)).getName() + " ?");
//        SocketManager.GAME_SEND_WEDDING(carte, 617, (Homme == player ? Homme.getSpriteId() : Femme.getSpriteId()), (Homme == player ? Femme.getSpriteId() : Homme.getSpriteId()), IdPretre);
//    }
//
//    public static void Wedding(Player Homme, Player Femme, int isOK) {
//        if (isOK > 0) {
//            SocketManager.GAME_SEND_cMK_PACKET_TO_MAP(Homme.getMap(), "", -1, "Prêtre", "Je déclare " + Homme.getName() + " et " + Femme.getName() + " unis par les liens sacrés du mariage.");
//            Homme.MarryTo(Femme);
//            Femme.MarryTo(Homme);
//        } else {
//            SocketManager.GAME_SEND_Im_PACKET_TO_MAP(Homme.getMap(), "048;" + Homme.getName() + "~" + Femme.getName());
//        }
//        Married.get(0).setisOK(0);
//        Married.get(1).setisOK(0);
//        Married.clear();
//    }

    public static Fireworks getFireworks(int AnimationId) {
        return Fireworks.get(AnimationId);
    }

    public static void addAnimation(Fireworks animation) {
        Fireworks.put(animation.getId(), animation);
    }
	//PNJ échange

    public double getRate(int alignement) {
        int cant = 0;
        for (SubArea subarea : areaHandler.getSubAreas()) {
            if (subarea.getAlignement() == alignement) {
                cant++;
            }
        }
        if (cant == 0) {
            return 0;
        }
        return Math.rint((10 * cant / 4) / 10);
        //return 100;
    }

    public static double getBalanceArea(Area area, int alignement) {
        int cant = 0;
        for (SubArea subarea : area.getSubAreas()) {
            if (subarea.getArea() == area && subarea.getAlignement() == alignement) {
                cant++;
            }
        }
        if (cant == 0) {
            return 0;
        }
        return Math.rint((1000 * cant / (area.getSubAreas().size())) / 10);
        //return 100;
    }
    
    @Deprecated
    static public GameMap getCarte(short id){
        return instance.getMapHandler().getMap(id);
    }

    @Deprecated
    static public MonsterTemplate getMonstre(int id){
        return instance.monsterHandler.getMonsterById(id);
    }
    
    @Deprecated
    static public Player getPersonnage(int id){
        return instance.playerHandler.getPlayerById(id);
    }
}
