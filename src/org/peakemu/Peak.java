/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu;

import static org.peakemu.Ancestra.gameServer;
import static org.peakemu.Ancestra.isInit;
import static org.peakemu.Ancestra.isRunning;
import static org.peakemu.Ancestra.loadConfiguration;
import static org.peakemu.Ancestra.makeHeader;
import static org.peakemu.Ancestra.realmServer;
import org.peakemu.command.CommandHandler;
import org.peakemu.common.Constants;
import org.peakemu.common.Logger;
import org.peakemu.database.Database;
import fr.quatrevieux.crisis.config.ConfigException;
import org.peakemu.world.World;
import fr.quatrevieux.crisis.config.RootConfig;
import fr.quatrevieux.crisis.config.data.ConfigNode;
import fr.quatrevieux.crisis.config.data.xml.ConfigXMLHandler;
import org.peakemu.database.dao.DAOFactory;
import org.peakemu.game.GameServer;
import org.peakemu.realm.RealmServer;
import org.peakemu.world.handler.AreaHandler;
import org.peakemu.world.handler.MapHandler;
import org.peakemu.world.config.WorldConfig;
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
import org.peakemu.world.handler.SessionHandler;
import org.peakemu.world.handler.SpellHandler;
import org.peakemu.world.handler.VarHandler;
import org.peakemu.world.handler.WaypointHandler;
import org.peakemu.world.handler.fight.RewardsHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Peak {
    private RootConfig config;
    private ConfigNode rootConfigNode;
    private Database database;
    private DAOFactory dao;
    private World world;
    private CommandHandler commandHandler;
    
    final static public Logger errorLog = new Logger(Constants.ERROR_LOG_DIR);
    final static public Logger gameLog = new Logger(Constants.GAME_LOG_DIR);
    final static public Logger realmLog = new Logger(Constants.REALM_LOG_DIR);
    final static public Logger worldLog = new Logger(Constants.WORLD_LOG_DIR);

    public Peak(){
        try{
            System.out.println("==============================================================");
            System.out.println(makeHeader());
            System.out.println("==============================================================");

            loadConfig();
            loadDatabase();

            System.out.println("Chargement de la configuration (Ancestra) :");
            loadConfiguration();
            isInit = true;
            System.out.println("Configuration Ok !");

            loadWorld();
            commandHandler = new CommandHandler(this);

            isRunning = true;
            System.out.println("Lancement du serveur de Jeu : ");
            gameServer = new GameServer(config, this);
            System.out.println("Lancement du serveur de Connexion : ");
            realmServer = new RealmServer(config, this);

            System.out.println("En attente de connexions");
            
            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run(){
                    System.out.println("Fermeture du serveur...");
                    isRunning = false;
                    world.getSessionHandler().kickAll();
                    gameServer.close();
                    realmServer.close();
                    world.stop();
                    System.out.println("Serveur fermé");
                }
            });
        }catch(Exception e){
            errorLog.addToLog(e);
            System.exit(1);
        }
    }

    public RootConfig getConfig() {
        return config;
    }

    public Database getDatabase() {
        return database;
    }

    public DAOFactory getDao() {
        return dao;
    }

    public World getWorld() {
        return world;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }
    
    private void loadConfig(){
        try{
            System.out.print("Chargement de la configuration : ");
            ConfigXMLHandler parser = new ConfigXMLHandler(Constants.CONFIG_FILE);
            rootConfigNode = parser.getRoot();
            config = new RootConfig(rootConfigNode);
            System.out.println("Ok !");
        }catch(Exception ex){
            errorLog.addToLog(Logger.Level.ERROR, "Impossible de charger la configuration");
            errorLog.addToLog(ex);
            System.exit(1);
        }
    }
    
    private void loadDatabase(){
        try{
            System.out.print("Connexion à la base de données : ");
            database = new Database(config);
            System.out.println("Ok !");
            dao = new DAOFactory(database);
        }catch(Exception e){
            errorLog.addToLog(Logger.Level.ERROR, "Connexion à la BD impossible");
            errorLog.addToLog(e);
            System.exit(1);
        }
    }
    
    private void loadWorld() throws InstantiationException, IllegalAccessException, ConfigException{
        System.out.println("==> Création du Monde <==");
        long startTime = System.currentTimeMillis();
        
        WorldConfig worldConfig = config.getPackage(WorldConfig.class);
        
        ExpHandler expHandler = new ExpHandler(dao.getExpDAO());
        SpellHandler spellHandler = new SpellHandler(dao.getSpellDAO());
        AreaHandler areaHandler = new AreaHandler(dao.getAreaDAO(), dao.getSubAreaDAO());
        MapHandler mapHandler = new MapHandler(dao.getMapDAO(), dao.getTriggerDAO());
        SessionHandler sessionHandler = new SessionHandler(config.getPackage(WorldConfig.class), dao.getPlayerDAO(), dao.getAccountDAO());
        AccountHandler accountHandler = new AccountHandler(dao.getAccountDAO(), dao.getFriendListDAO());
        GuildHandler guildHandler = new GuildHandler(dao.getGuildDAO(), dao.getGuildMemberDAO(), dao.getCollectorDAO(), dao.getMountParkDAO(), dao.getSpellDAO(), expHandler, worldConfig.getGuild());
        PlayerHandler playerHandler = new PlayerHandler(dao.getAccountDAO(), dao.getPlayerDAO(), dao.getClassDataDAO(), config.getPackage(WorldConfig.class).getCharacterCreation(), mapHandler, guildHandler, spellHandler, expHandler, worldConfig.getPlayer());
        ItemHandler itemHandler = new ItemHandler(dao.getItemDAO(), dao.getItemSetDAO(), dao.getUseItemActionDAO(), dao.getInventoryDAO(), this);
        InteractiveObjectHandler interactiveObjectHandler = new InteractiveObjectHandler(dao.getIoTemplateDAO(), this, dao.getJobSkillDAO());
        MonsterHandler monsterHandler = new MonsterHandler(dao.getMonsterDAO());
        LifeHandler lifeHandler = new LifeHandler(config.getPackage(WorldConfig.class).getLife(), mapHandler, monsterHandler, dao.getFixedMonsterGroupDAO());
        ActionHandler actionHandler = new ActionHandler(this, dao.getLiveActionDAO(), config.getPackage(WorldConfig.class));
        AlignementHandler alignementHandler = new AlignementHandler(areaHandler, dao.getPrismDAO(), sessionHandler, expHandler);
        MountHandler mountHandler = new MountHandler(dao.getMountParkDAO(), dao.getMountDAO(), mapHandler, config.getPackage(WorldConfig.class).getMount(), dao.getMountTemplateDAO(), expHandler);
        WaypointHandler waypointHandler = new WaypointHandler(dao.getWaypointDAO(), playerHandler, mapHandler, alignementHandler);
        ExchangeHandler exchangeHandler = new ExchangeHandler(this);
        ChatHandler chatHandler = new ChatHandler(this, config.getPackage(WorldConfig.class).getChat());
        VarHandler varHandler = new VarHandler(this);
        NPCHandler npcHandler = new NPCHandler(dao.getNpcQuestionDAO(), dao.getNpcResponseDAO(), dao.getNpcTemplateDAO(), dao.getNpcDAO(), varHandler);
        JobHandler jobHandler = new JobHandler(config.getPackage(WorldConfig.class).getJob(), dao.getJobDAO(), dao.getJobSkillDAO(), dao.getCraftDAO(), config.getPackage(WorldConfig.class).getRate(), expHandler, worldConfig.getPlayer());
        
        
        //Fight
        RewardsHandler rewardsHandler = new RewardsHandler(playerHandler, guildHandler, mountHandler, itemHandler, alignementHandler);
        FightHandler fightHandler = new FightHandler(rewardsHandler, actionHandler, dao.getEndFightActionDAO(), dao.getPlayerDAO(), playerHandler, dao.getCollectorDAO(), dao.getGuildDAO(), dao.getGuildMemberDAO(), dao.getMonsterDAO(), alignementHandler, spellHandler, config.getPackage(WorldConfig.class).getRate(), config.getPackage(WorldConfig.class).getFight(), expHandler);
        
        
        world = new World(
            areaHandler,
            mapHandler,
            config.getPackage(WorldConfig.class),
            sessionHandler,
            accountHandler,
            playerHandler,
            itemHandler,
            interactiveObjectHandler,
            lifeHandler,
            actionHandler,
            fightHandler,
            waypointHandler,
            monsterHandler,
            alignementHandler,
            guildHandler,
            exchangeHandler,
            chatHandler,
            varHandler,
            npcHandler,
            mountHandler,
            jobHandler,
            spellHandler,
            expHandler
        );
        
        world.createWorld();
        
        long endTime = System.currentTimeMillis();
        long differenceTime = (endTime - startTime) / 1000;
        System.out.println("Monde chargée en : " + differenceTime + " s");
    }

    public Logger getErrorLog() {
        return errorLog;
    }
    
    public static void main(String[] args) throws Exception {
        new Peak();
    }
}
