package org.peakemu.game;

import org.peakemu.Ancestra;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import org.peakemu.Peak;
import org.peakemu.common.Logger.Level;
import fr.quatrevieux.crisis.config.ConfigException;
import fr.quatrevieux.crisis.config.RootConfig;
import org.peakemu.game.in.Ping;
import org.peakemu.game.in.QuickPing;
import org.peakemu.game.in.exchange.AcceptExchange;
import org.peakemu.game.in.account.AccountToken;
import org.peakemu.game.in.account.BoostStat;
import org.peakemu.game.in.basic.AdminCommand;
import org.peakemu.game.in.account.CreateCharacter;
import org.peakemu.game.in.account.DeleteCharacter;
import org.peakemu.game.in.game.CreateGame;
import org.peakemu.game.in.guild.CreateGuild;
import org.peakemu.game.in.game.EndGameAction;
import org.peakemu.game.in.exchange.ExchangeMoveKamas;
import org.peakemu.game.in.exchange.ExchangeMoveObject;
import org.peakemu.game.in.exchange.ExchangeRequest;
import org.peakemu.game.in.guild.GuildInfos;
import org.peakemu.game.in.game.InitialiseMap;
import org.peakemu.game.in.guild.JoinGuildAccept;
import org.peakemu.game.in.guild.JoinGuildRefuse;
import org.peakemu.game.in.guild.JoinGuildRequest;
import org.peakemu.game.in.account.ListCharacters;
import org.peakemu.game.in.object.MoveItem;
import org.peakemu.game.in.account.RegionVersion;
import org.peakemu.game.in.account.SelectCharacter;
import org.peakemu.game.in.basic.ChatMessage;
import org.peakemu.game.in.basic.SmileyPacket;
import org.peakemu.game.in.basic.TeleportOnWorldMap;
import org.peakemu.game.in.basic.WhoIs;
import org.peakemu.game.in.conquest.ConquestBalance;
import org.peakemu.game.in.conquest.ConquestBonus;
import org.peakemu.game.in.conquest.ConquestInfoJoin;
import org.peakemu.game.in.conquest.ConquestInfoWorld;
import org.peakemu.game.in.conquest.ConquestJoinFight;
import org.peakemu.game.in.dialog.CreateDialog;
import org.peakemu.game.in.dialog.DialogResponse;
import org.peakemu.game.in.dialog.LeaveDialog;
import org.peakemu.game.in.exchange.AskSetSeller;
import org.peakemu.game.in.exchange.BuyItem;
import org.peakemu.game.in.exchange.EndCraftLoop;
import org.peakemu.game.in.exchange.EquipMountFromPark;
import org.peakemu.game.in.exchange.GetCertifFromMountPark;
import org.peakemu.game.in.game.StartGameAction;
import org.peakemu.game.in.exchange.LeaveExchange;
import org.peakemu.game.in.exchange.ListExchange;
import org.peakemu.game.in.exchange.PutCertifToMountPark;
import org.peakemu.game.in.exchange.PutEquipedMountToPark;
import org.peakemu.game.in.exchange.SellItem;
import org.peakemu.game.in.exchange.SetSeller;
import org.peakemu.game.in.exchange.StartCraftLoop;
import org.peakemu.game.in.fight.LeaveFight;
import org.peakemu.game.in.waypoint.LeavePrism;
import org.peakemu.game.in.waypoint.LeaveZaap;
import org.peakemu.game.in.waypoint.LeaveZaapi;
import org.peakemu.game.in.guild.RemoveCollector;
import org.peakemu.game.in.fight.ToggleFightLock;
import org.peakemu.game.in.fight.ToggleLockSpectator;
import org.peakemu.game.in.fight.ToogleFightHelp;
import org.peakemu.game.in.fight.ToogleFightOnlyGroup;
import org.peakemu.game.in.object.UseObject;
import org.peakemu.game.in.waypoint.UsePrism;
import org.peakemu.game.in.waypoint.UseZaap;
import org.peakemu.game.in.waypoint.UseZaapi;
import org.peakemu.game.in.exchange.ValidateExchange;
import org.peakemu.game.in.friend.AddEnemy;
import org.peakemu.game.in.friend.AddFriend;
import org.peakemu.game.in.friend.ListEnemies;
import org.peakemu.game.in.friend.ListFriends;
import org.peakemu.game.in.friend.RemoveEnemy;
import org.peakemu.game.in.friend.RemoveFriend;
import org.peakemu.game.in.friend.SeeFriendConnection;
import org.peakemu.game.in.game.FightChangePlace;
import org.peakemu.game.in.game.FighterIsReady;
import org.peakemu.game.in.game.ShowFightCell;
import org.peakemu.game.in.gameaction.GameActionRegistry;
import org.peakemu.game.in.group.AcceptGroupInvitation;
import org.peakemu.game.in.group.InviteGroup;
import org.peakemu.game.in.group.RefuseGroupInvitation;
import org.peakemu.game.in.guild.AddCollector;
import org.peakemu.game.in.guild.BoostCollectorsSpell;
import org.peakemu.game.in.guild.BoostGuild;
import org.peakemu.game.in.guild.DefendCollector;
import org.peakemu.game.in.guild.KickGuildMember;
import org.peakemu.game.in.guild.LeaveGuildCreate;
import org.peakemu.game.in.guild.TeleportToMountPark;
import org.peakemu.game.in.guild.UpdateGuildProfile;
import org.peakemu.game.in.mount.BuyMountPark;
import org.peakemu.game.in.mount.FreeMount;
import org.peakemu.game.in.mount.MountChangeName;
import org.peakemu.game.in.mount.MountDescription;
import org.peakemu.game.in.mount.RideMount;
import org.peakemu.game.in.mount.UpdateMountParkPrice;
import org.peakemu.game.in.object.DeleteObject;
import org.peakemu.game.in.object.DropObject;
import org.peakemu.game.in.spell.BoostSpell;
import org.peakemu.game.in.spell.MoveSpell;
import org.peakemu.network.DofusServer;
import org.peakemu.network.InputPacketRegistry;
import org.peakemu.world.config.WorldConfig;


public class GameServer extends DofusServer<GameClient>{
    final private GameServerConfig config;
    final private Peak peak;
    
    final private InputPacketRegistry inputPacketRegistry = new InputPacketRegistry();
    final private GameActionRegistry gameActionRegistry;
    
    private Timer _loadActionTimer;
    private Timer _reloadMobTimer;
    private Timer _loadMoveMobTimer;
    private long _startTime;
    private int _maxPlayer = 0;

    public GameServer(RootConfig rootConfig, Peak peak) throws InstantiationException, IllegalAccessException, ConfigException, IOException, Exception {
        super(rootConfig.getPackage(GameServerConfig.class).getPort(), peak.getWorld().getSessionHandler());
        config = rootConfig.getPackage(GameServerConfig.class);
        this.peak = peak;

//        _loadActionTimer = new Timer();
//        _loadActionTimer.schedule(new TimerTask() {
//            public void run() {
//                Database.LOAD_ACTION();
//                GameServer.addToLog("Les live actions ont ete appliquees");
//            }
//        }, Ancestra.CONFIG_LOAD_DELAY, Ancestra.CONFIG_LOAD_DELAY);
//
//        _loadMoveMobTimer = new Timer();
//        _loadMoveMobTimer.schedule(new TimerTask() {
//            public void run() {
//                World.RefreshMoveMobsOnMaps();
//            }
//        }, 60000, 60000);
//
//        _reloadMobTimer = new Timer();
//        _reloadMobTimer.schedule(new TimerTask() {
//            public void run() {
//                World.getInstance().RefreshAllMob();
//                GameServer.addToLog("La recharge des mobs est finie");
//            }
//        }, Ancestra.CONFIG_RELOAD_MOB_DELAY, Ancestra.CONFIG_RELOAD_MOB_DELAY);

        _startTime = System.currentTimeMillis();
        registerPackets();
        gameActionRegistry = new GameActionRegistry(peak);
    }

    @Override
    protected GameClient createClient(Socket socket) throws IOException {
        return new GameClient(
            socket, 
            inputPacketRegistry, 
            peak.getWorld().getSessionHandler(), 
            peak.getDao().getPlayerDAO(),
            peak.getDao().getAccountDAO(),
            gameActionRegistry
        );
    }

    public long getStartTime() {
        return _startTime;
    }

    public int getMaxPlayer() {
        return _maxPlayer;
    }

    public synchronized static void addToLog(String str) {
        System.out.println(str);
        if (Ancestra.canLog) {
            Peak.worldLog.addToLog(Level.INFO, str);
        }
    }

    public synchronized static void addToSockLog(String str) {
        if (Ancestra.CONFIG_DEBUG) {
            System.out.println(str);
        }
        if (Ancestra.canLog) {
            Peak.gameLog.addToLog(Level.INFO, str);
        }
    }

    public static String getServerTime() {
        Date actDate = new Date();
        return "BT" + (actDate.getTime() + 3600000);
    }

    public static String getServerDate() {
        Date actDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd");
        String jour = Integer.parseInt(dateFormat.format(actDate)) + "";
        while (jour.length() < 2) {
            jour = "0" + jour;
        }
        dateFormat = new SimpleDateFormat("MM");
        String mois = (Integer.parseInt(dateFormat.format(actDate)) - 1) + "";
        while (mois.length() < 2) {
            mois = "0" + mois;
        }
        dateFormat = new SimpleDateFormat("yyyy");
        String annee = (Integer.parseInt(dateFormat.format(actDate)) - 1370) + "";
        return "BD" + annee + "|" + mois + "|" + jour;
    }
    
    private void registerPackets(){
        inputPacketRegistry.addPacket(new Ping());
        inputPacketRegistry.addPacket(new QuickPing());
        
        //Account (A)
        inputPacketRegistry.addPacket(new AccountToken(peak.getWorld().getSessionHandler()));
        inputPacketRegistry.addPacket(new RegionVersion(config));
        inputPacketRegistry.addPacket(new SelectCharacter(peak.getDao().getPlayerDAO(), peak.getWorld().getConfig(), peak.getWorld().getJobHandler(), peak.getWorld().getAreaHandler()));
        inputPacketRegistry.addPacket(new ListCharacters());
        inputPacketRegistry.addPacket(new DeleteCharacter(peak.getWorld().getPlayerHandler()));
        inputPacketRegistry.addPacket(new BoostStat(peak.getWorld().getPlayerHandler()));
        try{
            inputPacketRegistry.addPacket(new CreateCharacter(peak.getDao().getPlayerDAO(), peak.getWorld().getPlayerHandler(), peak.getConfig().getPackage(WorldConfig.class).getCharacterCreation()));
        }catch(Exception e){}
        
        //Game (G)
        inputPacketRegistry.addPacket(new CreateGame());
        inputPacketRegistry.addPacket(new StartGameAction());
        inputPacketRegistry.addPacket(new EndGameAction());
        inputPacketRegistry.addPacket(new InitialiseMap(peak.getDao().getMountParkDAO()));
        inputPacketRegistry.addPacket(new ShowFightCell());
        inputPacketRegistry.addPacket(new FightChangePlace());
        inputPacketRegistry.addPacket(new FighterIsReady());
        
        //Objects (O)
        inputPacketRegistry.addPacket(new MoveItem());
        inputPacketRegistry.addPacket(new UseObject(peak.getWorld().getActionHandler()));
        inputPacketRegistry.addPacket(new DeleteObject());
        inputPacketRegistry.addPacket(new DropObject());
        
        //Basic (B)
        inputPacketRegistry.addPacket(new AdminCommand(peak.getCommandHandler()));
        inputPacketRegistry.addPacket(new ChatMessage(peak.getWorld().getChatHandler(), peak.getWorld().getSessionHandler()));
        inputPacketRegistry.addPacket(new TeleportOnWorldMap(peak.getCommandHandler()));
        inputPacketRegistry.addPacket(new WhoIs(peak.getWorld().getSessionHandler()));
        inputPacketRegistry.addPacket(new SmileyPacket());
        
        //Exchange (E)
        inputPacketRegistry.addPacket(new ExchangeMoveObject());
        inputPacketRegistry.addPacket(new LeaveExchange(peak.getWorld().getExchangeHandler()));
        inputPacketRegistry.addPacket(new ExchangeMoveKamas());
        inputPacketRegistry.addPacket(new ExchangeRequest(peak.getWorld().getExchangeHandler()));
        inputPacketRegistry.addPacket(new AcceptExchange());
        inputPacketRegistry.addPacket(new ValidateExchange(peak.getWorld().getExchangeHandler()));
        inputPacketRegistry.addPacket(new AskSetSeller(peak.getWorld().getConfig().getStore()));
        inputPacketRegistry.addPacket(new SetSeller(peak.getWorld().getConfig().getStore()));
        inputPacketRegistry.addPacket(new BuyItem());
        inputPacketRegistry.addPacket(new SellItem());
        inputPacketRegistry.addPacket(new PutCertifToMountPark(peak.getWorld().getConfig().getMount(), peak.getDao().getMountParkDAO(), peak.getWorld().getMountHandler()));
        inputPacketRegistry.addPacket(new EquipMountFromPark(peak.getDao().getMountParkDAO()));
        inputPacketRegistry.addPacket(new PutEquipedMountToPark(peak.getDao().getMountParkDAO()));
        inputPacketRegistry.addPacket(new GetCertifFromMountPark(peak.getDao().getMountParkDAO(), peak.getWorld().getItemHandler()));
        inputPacketRegistry.addPacket(new StartCraftLoop());
        inputPacketRegistry.addPacket(new EndCraftLoop());
        inputPacketRegistry.addPacket(new ListExchange());
        
        //Fight
        inputPacketRegistry.addPacket(new ToogleFightHelp());
        inputPacketRegistry.addPacket(new ToggleFightLock());
        inputPacketRegistry.addPacket(new ToogleFightOnlyGroup());
        inputPacketRegistry.addPacket(new ToggleLockSpectator());
        inputPacketRegistry.addPacket(new LeaveFight());
        
        //Waypoints (W)
        inputPacketRegistry.addPacket(new UseZaap(peak.getWorld().getWaypointHandler()));
        inputPacketRegistry.addPacket(new LeaveZaap());
        inputPacketRegistry.addPacket(new UseZaapi(peak.getWorld().getWaypointHandler()));
        inputPacketRegistry.addPacket(new LeaveZaapi());
        inputPacketRegistry.addPacket(new UsePrism(peak.getWorld().getWaypointHandler(), peak.getWorld().getMapHandler()));
        inputPacketRegistry.addPacket(new LeavePrism());
        
        //Guild (g)
        inputPacketRegistry.addPacket(new RemoveCollector(peak.getDao().getCollectorDAO()));
        inputPacketRegistry.addPacket(new CreateGuild(peak.getWorld().getGuildHandler()));
        inputPacketRegistry.addPacket(new GuildInfos(peak.getWorld().getGuildHandler(), peak.getWorld().getExpHandler()));
        inputPacketRegistry.addPacket(new JoinGuildRequest(peak.getWorld().getSessionHandler()));
        inputPacketRegistry.addPacket(new JoinGuildRefuse());
        inputPacketRegistry.addPacket(new JoinGuildAccept(peak.getWorld().getGuildHandler()));
        inputPacketRegistry.addPacket(new UpdateGuildProfile(peak.getDao().getGuildMemberDAO()));
        inputPacketRegistry.addPacket(new KickGuildMember(peak.getWorld().getGuildHandler()));
        inputPacketRegistry.addPacket(new LeaveGuildCreate());
        inputPacketRegistry.addPacket(new BoostGuild(peak.getDao().getGuildDAO()));
        inputPacketRegistry.addPacket(new AddCollector(peak.getDao().getGuildDAO(), peak.getDao().getCollectorDAO(), peak.getDao().getPlayerDAO()));
        inputPacketRegistry.addPacket(new BoostCollectorsSpell(peak.getDao().getGuildDAO(), peak.getWorld().getSpellHandler()));
        inputPacketRegistry.addPacket(new DefendCollector());
        inputPacketRegistry.addPacket(new TeleportToMountPark(peak.getDao().getMountParkDAO(), peak.getDao().getMapDAO(), peak.getWorld().getPlayerHandler()));
        
        //Friend (F)
        inputPacketRegistry.addPacket(new AddFriend(peak.getWorld().getSessionHandler(), peak.getWorld().getAccountHandler()));
        inputPacketRegistry.addPacket(new ListFriends());
        inputPacketRegistry.addPacket(new RemoveFriend(peak.getWorld().getAccountHandler()));
        inputPacketRegistry.addPacket(new AddEnemy(peak.getWorld().getSessionHandler(), peak.getWorld().getAccountHandler()));
        inputPacketRegistry.addPacket(new ListEnemies());
        inputPacketRegistry.addPacket(new RemoveEnemy(peak.getWorld().getAccountHandler()));
        inputPacketRegistry.addPacket(new SeeFriendConnection());
        
        //group (P)
        inputPacketRegistry.addPacket(new InviteGroup(peak.getWorld().getSessionHandler(), peak.getWorld().getConfig()));
        inputPacketRegistry.addPacket(new RefuseGroupInvitation());
        inputPacketRegistry.addPacket(new AcceptGroupInvitation());
        
        
        //dialogs (D)
        inputPacketRegistry.addPacket(new CreateDialog(peak.getWorld().getNpcHandler()));
        inputPacketRegistry.addPacket(new DialogResponse(peak.getWorld().getActionHandler()));
        inputPacketRegistry.addPacket(new LeaveDialog());
        
        //Mount (R)
        inputPacketRegistry.addPacket(new MountDescription(peak.getDao().getMountDAO()));
        inputPacketRegistry.addPacket(new BuyMountPark(peak.getDao().getMountParkDAO(), peak.getWorld().getGuildHandler()));
        inputPacketRegistry.addPacket(new UpdateMountParkPrice(peak.getDao().getMountParkDAO()));
        inputPacketRegistry.addPacket(new FreeMount(peak.getDao().getMountDAO()));
        inputPacketRegistry.addPacket(new RideMount());
        inputPacketRegistry.addPacket(new MountChangeName(peak.getDao().getMountDAO()));
        
        //Conquest (C)
        inputPacketRegistry.addPacket(new ConquestBalance(peak.getWorld().getAlignementHandler()));
        inputPacketRegistry.addPacket(new ConquestInfoWorld(peak.getWorld().getAlignementHandler(), peak.getWorld().getAreaHandler()));
        inputPacketRegistry.addPacket(new ConquestInfoJoin());
        inputPacketRegistry.addPacket(new ConquestBonus(peak.getWorld().getAlignementHandler()));
        inputPacketRegistry.addPacket(new ConquestJoinFight(peak.getWorld().getPlayerHandler()));
        
        //Spell
        inputPacketRegistry.addPacket(new BoostSpell(peak.getWorld().getSpellHandler(), peak.getDao().getPlayerDAO()));
        inputPacketRegistry.addPacket(new MoveSpell());
    }

}
