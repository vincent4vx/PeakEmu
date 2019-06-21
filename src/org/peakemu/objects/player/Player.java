package org.peakemu.objects.player;

import org.peakemu.world.Job;
import org.peakemu.world.GameMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
//Quests
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.peakemu.common.Constants;
import org.peakemu.database.Database;
import org.peakemu.common.SocketManager;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.game.AlterSprite;
import org.peakemu.game.out.object.EquipedItemSet;
import org.peakemu.game.out.account.AlterRestrictions;
import org.peakemu.game.out.guild.GuildStats;
import org.peakemu.game.out.SpellList;
import org.peakemu.game.out.account.NewLevel;
import org.peakemu.game.out.game.SpriteParser;
import org.peakemu.game.out.mount.MountEquiped;
import org.peakemu.objects.Account;
import org.peakemu.objects.Collector;
import org.peakemu.objects.Mount;
import org.peakemu.world.fight.Fight;
import org.peakemu.objects.Guild;
import org.peakemu.objects.NPC_Exchange;
import org.peakemu.objects.item.BuffItem;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.item.MutationItem;
import org.peakemu.world.ExpLevel;
import org.peakemu.world.Inventory;
import org.peakemu.world.ItemSet;
import org.peakemu.world.MapCell;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.NPCQuestion;
import org.peakemu.world.Request;
import org.peakemu.world.Restrictions;
import org.peakemu.world.Spell;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.Sprite;
import org.peakemu.world.Stats;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.InventoryPosition;
import org.peakemu.world.enums.PlayerRace;
import org.peakemu.world.enums.SpriteTypeEnum;
import org.peakemu.world.exchange.ExchangeBase;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.fighter.PlayerFighter;
import org.peakemu.world.handler.WaypointHandler;

public class Player implements Sprite{

    final private int id;
    final private String _name;
    final private int _sexe;
    final private int _color1;
    final private int _color2;
    final private int _color3;
    private int size;
    private long _kamas;
    private int _spellPts;
    private int _capital;
    private int _energy;
    private ExpLevel expLevel;
    private long _curExp;
    private int _orientation = 1;
    private Account account;
    private boolean _canAggro = true;
    private String _emotes = "7667711";

    //Variables d'ali
    private byte _align = 0;
    private int _deshonor = 0;
    private int _honor = 0;
    private boolean _showWings = false;
    private int _aLvl = 0;
	//Fin ali

    private GuildMember _guildMember;
    private boolean _showFriendConnection;
    private String _canaux;

    private Fight fight;
    private PlayerFighter fighter = null;
    private boolean _away;
    private GameMap map;
    private MapCell cell;
    private boolean _sitted = false;
    // régnération des pvs
    private long _startRegenTime = 0; // Timestamp du début de la régénération
    private int _curRegenTimer = 0; // Délai actuel (1 pdv/1000 ms ? 2000 ? ...)

    private Group _group;
    private int _duelID = -1;
    private Inventory items;
    private String _savePos;
    private int _emoteActive = 0;
    //PDV
    private int _PDV;
    private int _PDVMAX;
    //Echanges
    private int _isTradingWith = 0;
    private ExchangeBase curExchange;
    // Npc_Exchange
    private NPC_Exchange _curNpcExchange;
    private NPCQuestion curQuestion = null;
    //Invitation
    private Request request = null;
    final private Collection<JobStats> jobs;
    //Monture
    private Mount mount;
    private int _mountXpGive = 0;
    private boolean _onMount = false;
    //Zaap
    private WaypointHandler.Waypoint currentWaypoint;
    final private Set<WaypointHandler.Waypoint> zaaps;
    //Disponibilité
    public boolean _isAbsent = false;
    public boolean _isInvisible = false;
    //Sort
    public boolean _seeSpell = false;
    private boolean _isForgetingSpell = false;
    //Double
    public boolean _isClone = false;
    //Percepteurs
    private int _isOnPercepteurID = 0;
    //Traque
    private traque _traqued = null;
	//Titre
    //private byte _title = 0;
    private String _title;
    //Inactivité
    protected long _lastPacketTime;
    //Suiveur - Suivi
    public Map<Integer, Player> _Follower = new TreeMap<Integer, Player>();
    public Player _Follows = null;
	//Fantome
    private int _isDead = 0;
    final private Restrictions restrictions = Restrictions.createDefaultRestrictions();
    //Marchand
    final private PlayerStore store;
    private Seller seller = null;
    //For View : AncestraR 0.6
    private boolean forView;
    //Percepteur sauvegarde positin
    private String _savedPos;
    //Bloquer l'aggro en boucle:
    private int _lastPersoAgro = 0;
    private Mount _curMount;
    // Quests
    private Map<Integer, Map<String, String>> _quests = new HashMap<Integer, Map<String, String>>();
    // Pnjs suiveurs
    private String _followers = "";
    
    //PlayerData
    final private BaseCharacter baseCharacter;
    private PlayerData currentPlayerData = null;

    public static class traque {

        private long _time;
        private Player _traqued;

        public traque(long time, Player p) {
            this._time = time;
            this._traqued = p;
        }

        public void set_traqued(Player tempP) {
            _traqued = tempP;
        }

        public Player get_traqued() {
            return _traqued;
        }

        public long get_time() {
            return _time;
        }

        public void set_time(long time) {
            _time = time;
        }
    }

    public Player(int _guid, String _name, int _sexe, PlayerRace _classe,
            int _color1, int _color2, int _color3, long _kamas, int pts, int _capital, int _energy, ExpLevel level, long exp,
            int _size, int _gfxid, byte alignement, Account account, Stats baseStats,
            byte seeFriend, byte seeAlign, byte seeSeller, String canaux, GameMap map, MapCell cell, Inventory items, PlayerStore store, int pdvPer, SpellBook spells, String savePos,
            //metier
            Collection<JobStats> jobs,
            int mountXp, Mount mount, int honor, int deshonor, int alvl, Set<WaypointHandler.Waypoint> zaaps, String title, boolean isSeller, String quests) {
        this.id = _guid;
        this._name = _name;
        this._sexe = _sexe;
        this._color1 = _color1;
        this._color2 = _color2;
        this._color3 = _color3;
        this._kamas = _kamas;
        this._spellPts = pts;
        this._capital = _capital;
        this._align = alignement;
        this._honor = honor;
        this._deshonor = deshonor;
        this._aLvl = alvl;
        this._energy = _energy;
        this.expLevel = level;
        this._curExp = exp;
        this.items = items;
        this.mount = mount;
        this._mountXpGive = mountXp;
        this.account = account;
        this.size = _size;
        baseCharacter = new BaseCharacter(this, _classe, spells, baseStats, _gfxid);
        this._showFriendConnection = seeFriend == 1;
        
        if (this.getAlignement() != 0) {
            this._showWings = seeAlign == 1;
        } else {
            this._showWings = false;
        }
        this._canaux = canaux;
        this.map = map;
        this._savePos = savePos;
        this.cell = cell;
        
        this.zaaps = zaaps;
        this.store = store;
        
        this._PDVMAX = getTotalStats().getEffect(Effect.ADD_VITA);
        this._PDV = (_PDVMAX * pdvPer) / 100;

        this._title = title;
        
        if (_energy == 0) {
            setDead();
        }
        
        if(isSeller)
            setSeller();
        
        this.jobs = jobs;

        //_quests = quests;
//        this.initializeQuests(quests);
        
        refreshStats();
    }

    public void regenLife() {
        //Joueur pas en jeu
        if (map == null) {
            return;
        }
        //Pas de regen en combat
        if (fight != null) {
            return;
        }
        //Déjà Full PDV
        if (_PDV == _PDVMAX) {
            return;
        }
        if (!_sitted) {
            return; // non assis : pas de regen
        }
        _PDV += 1;
    }

    public int getId() {
        return id;
    }

    public boolean isOnline() {
        if(account.getGameThread() == null || account.getGameThread().isClosed())
            return false;
        
        return equals(account.getGameThread().getPlayer());
    }

    public void setGroup(Group g) {
        _group = g;
    }

    public Group getGroup() {
        return _group;
    }

    public String getSavePos() {
        return _savePos;
    }

    public void setSavePos(String savePos) {
        _savePos = savePos;
    }

    public int get_isTradingWith() {
        return _isTradingWith;
    }

    public void set_isTradingWith(int tradingWith) {
        _isTradingWith = tradingWith;
    }

    public NPCQuestion getCurQuestion() {
        return curQuestion;
    }

    public void setCurQuestion(NPCQuestion curQuestion) {
        this.curQuestion = curQuestion;
    }

    public long getKamas() {
        if (_kamas < 0) {
            return 0;
        }
        return _kamas;
    }

    public void setKamas(long l) {
        this._kamas = l;
        
        if(this._kamas < 0)
            this._kamas = 0;
        
        SocketManager.GAME_SEND_STATS_PACKET(this);
    }
    
    public void removeKamas(long nb){
        setKamas(getKamas() - nb);
    }

    public Account getAccount() {
        return account;
    }

    public int getSpellPoints() {
        return _spellPts;
    }

    public void setSpellPoints(int pts) {
        _spellPts = pts;
    }

    public Guild getGuild() {
        if (_guildMember == null) {
            return null;
        }
        return _guildMember.getGuild();
    }

    public void setGuildMember(GuildMember _guild) {
        this._guildMember = _guild;
    }

    public int get_duelID() {
        return _duelID;
    }

    public Fight getFight() {
        return fight;
    }
    
    public boolean isOnFight(){
        return fight != null;
    }

    public void set_duelID(int _duelid) {
        _duelID = _duelid;
    }

    public int getEnergy() {
        return _energy;
    }

    public boolean showFriendConnection() {
        return _showFriendConnection;
    }

    public boolean showSpells() {
        return _seeSpell;
    }

    public boolean showWings() {
        return _showWings;
    }

    public String getChanels() {
        return _canaux;
    }

    public void setEnergy(int _energy) {
        this._energy = _energy;
    }

    public int getLevel() {
        return expLevel.level;
    }

    public ExpLevel getExpLevel() {
        return expLevel;
    }

    public void setExpLevel(ExpLevel expLevel) {
        this.expLevel = expLevel;
        send(new NewLevel(expLevel.level));
    }

    public long getCurExp() {
        return _curExp;
    }

    @Override
    public MapCell getCell() {
        return cell;
    }

    public void setCell(MapCell cell) {
        this.cell = cell;
    }

    public void setCurExp(long exp) {
        _curExp = exp;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int _size) {
        this.size = _size;
    }

    public void setFight(Fight _fight) {
        this.fight = _fight;
    }

    public PlayerFighter getFighter() {
        return fighter;
    }

    public void setFighter(PlayerFighter fighter) {
        this.fighter = fighter;
    }

    public int getGfxID() {
        return getPlayerData().getGfxID();
    }

    public void setGfxID(int _gfxid) {
        baseCharacter.setGfxID(_gfxid);
    }

    @Override
    public int getSpriteId() {
        return id;
    }

    public GameMap getMap() {
        return map;
    }

    public String getName() {
        return _name;
    }

    public boolean isAway() {
        return _away || !isOnline() || getRequest() != null 
            || getCurExchange() != null || getCurQuestion() != null
            || getFight() != null;
    }

    public void setAway(boolean _away) {
        this._away = _away;
    }

    public boolean isSitted() {
        return _sitted;
    }

    public int getGender() {
        return _sexe;
    }

    public PlayerRace getRace() {
        return baseCharacter.getRace();
    }

    public int getColor1() {
        return _color1;
    }

    public int getColor2() {
        return _color2;
    }

    public int getColor3() {
        return _color3;
    }

    public Stats getBaseStats() {
        return getPlayerData().getBaseStats();
    }

    public int getCapitalPoints() {
        return _capital;
    }
    
    public boolean hasSpell(Spell spell){
        return getPlayerData().getSpellBook().hasLearnedSpell(spell)
            && getPlayerData().getSpellBook().getSpellById(spell.getSpellID()).getMinPlayerLevel() <= getLevel();
    }
    
    /**
     * learn a new spell (i.e. the player don't have the spell, and set the spell at lvl 1)
     * @param spell
     * @return false if the player already has the spell or don't have the min required level. in other case true
     */
    public boolean learnSpell(Spell spell){
        if(!getPlayerData().canLearnSpells())
            return false;
        
        if(getLevel() < spell.getMinPlayerLevel())
            return false;
        
        return getPlayerData().getSpellBook().learnSpell(spell);
    }

    public boolean boostSpell(Spell spell) {
        if(!getPlayerData().canLearnSpells())
            return false;
        
        if(!hasSpell(spell))
            return false;
        
        SpellLevel spellLevel = getSpellById(spell.getSpellID());
        
        if (spellLevel == null)
            return false;
        
        if(_spellPts < spellLevel.getLevel())
            return false;
        
        if(getLevel() < spellLevel.getNextLevel().getMinPlayerLevel())
            return false;
        
        if(spellLevel.isMaxLevel())
            return false;
        
        if(!getPlayerData().getSpellBook().boostSpell(spell))
            return false;
        
        _spellPts -= spellLevel.getLevel();
        return true;
    }
    
    public Map<Integer, SpellLevel> getSpells(){
        return getPlayerData().getSpellBook().getSpells();
    }
    
    public char getSpellPosition(int spellId){
        return getPlayerData().getSpellBook().getSpellPosition(spellId);
    }

    public void setSpellPosition(int SpellID, char Place) {
        getPlayerData().getSpellBook().setSpellPosition(SpellID, Place);
    }

    public SpellLevel getSpellById(int spellID) {
        return getSpells().get(spellID);
    }

    public String parseALK() {
        StringBuilder perso = new StringBuilder();
        perso.append("|");
        perso.append(this.id).append(";");
        perso.append(this._name).append(";");
        perso.append(getLevel()).append(";");
        perso.append(getGfxID()).append(";");
        perso.append((this._color1 != -1 ? Integer.toHexString(this._color1) : "-1")).append(";");
        perso.append((this._color2 != -1 ? Integer.toHexString(this._color2) : "-1")).append(";");
        perso.append((this._color3 != -1 ? Integer.toHexString(this._color3) : "-1")).append(";");
        perso.append(SpriteParser.stuffToString(items)).append(";");
        perso.append(isSeller() ? "1" : "0").append(";");
        perso.append("1;");//ServerID
        perso.append(";");//DeathCount	this.deathCount;
        perso.append(";");//LevelMax
        return perso.toString();
    }

    public void OnJoinGame() {
        if (account.getGameThread() == null) {
            return;
        }
        GameClient out = account.getGameThread();

        if (mount != null) {
            send(new MountEquiped(mount));
        }
        SocketManager.GAME_SEND_Rx_PACKET(this);
        
        //Envoie des bonus pano si besoin
        for (Entry<ItemSet, Set<ItemTemplate>> entry : items.getEquipedItemSets().entrySet()) {
            out.send(new EquipedItemSet(entry.getKey(), entry.getValue()));
        }
        
        SocketManager.GAME_SEND_ALIGNEMENT(out, _align);
        SocketManager.GAME_SEND_ADD_CANAL(out, _canaux + "^" + (account.get_gmLvl() > 0 ? "@¤" : ""));
        if (_guildMember != null) {
            out.send(new GuildStats(_guildMember));
        }
        
        send(new SpellList(this));
        SocketManager.GAME_SEND_EMOTE_LIST(this, _emotes, "0");
        SocketManager.GAME_SEND_Ow_PACKET(this);
        SocketManager.GAME_SEND_SEE_FRIEND_CONNEXION(out, _showFriendConnection);

        //Messages de bienvenue
        SocketManager.GAME_SEND_Im_PACKET(this, "189");
        if (!account.getLastConnectionDate().equals("") && !account.get_lastIP().equals("")) {
            SocketManager.GAME_SEND_Im_PACKET(this, "0152;" + account.getLastConnectionDate() + "~" + account.get_lastIP());
        }
        SocketManager.GAME_SEND_Im_PACKET(this, "0153;" + account.get_curIP());
		//Fin messages
        //Actualisation de l'ip
        account.setLastIP(account.get_curIP());

        //Mise a jour du lastConnectionDate
        Date actDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd");
        String jour = dateFormat.format(actDate);
        dateFormat = new SimpleDateFormat("MM");
        String mois = dateFormat.format(actDate);
        dateFormat = new SimpleDateFormat("yyyy");
        String annee = dateFormat.format(actDate);
        dateFormat = new SimpleDateFormat("HH");
        String heure = dateFormat.format(actDate);
        dateFormat = new SimpleDateFormat("mm");
        String min = dateFormat.format(actDate);
        account.setLastConnectionDate(annee + "~" + mois + "~" + jour + "~" + heure + "~" + min);

		//World.showPrismes(this);
        //Actualisation dans la DB
        Database.UPDATE_LASTCONNECTION_INFO(account);
        
        if (_energy > 0 && _energy < 2000) {
            SocketManager.send(this, "M111|" + _energy);
        }

        // regen en étant debout
        setSitted(false);
    }

    public void SetSeeFriendOnline(boolean bool) {
        _showFriendConnection = bool;
    }

    public String parseToOa() {
        StringBuilder packetOa = new StringBuilder();
        packetOa.append("Oa").append(id).append("|").append(SpriteParser.stuffToString(items));
        return packetOa.toString();
    }

    @Override
    public SpriteTypeEnum getSpriteType() {
        return getPlayerData().getSpriteType();
    }

    @Override
    public String getSpritePacket() {
        return getPlayerData().getSpritePacket();
    }

    public String parsecolortomount() {
        return (this._color1 == -1 ? "" : Integer.toHexString(this._color1)) + "," + (this._color2 == -1 ? "" : Integer.toHexString(this._color2)) + "," + (this._color3 == -1 ? "" : Integer.toHexString(this._color3));
    }

    public String parseToMerchant() {
        StringBuilder str = new StringBuilder();
        str.append(cell.getID()).append(";");
        str.append(_orientation).append(";");
        str.append("0").append(";");
        str.append(id).append(";");
        str.append(_name).append(";");
        str.append("-5").append(";");//Merchant identifier
        str.append(getGfxID()).append("^").append(size).append(";");
        str.append((_color1 == -1 ? "-1" : Integer.toHexString(_color1))).append(";");
        str.append((_color2 == -1 ? "-1" : Integer.toHexString(_color2))).append(";");
        str.append((_color3 == -1 ? "-1" : Integer.toHexString(_color3))).append(";");
        str.append(SpriteParser.stuffToString(items)).append(";");//acessories
        str.append((_guildMember != null ? _guildMember.getGuild().getName() : "")).append(";");//guildName
        str.append((_guildMember != null ? _guildMember.getGuild().getEmbem() : "")).append(";");//emblem
        str.append("0;");//offlineType

        return str.toString();
    }

    public String getAsPacket() {
        refreshStats();

        StringBuilder ASData = new StringBuilder();
        ASData.append("As").append(xpString(",")).append("|");
        ASData.append(_kamas).append("|").append(_capital).append("|").append(_spellPts).append("|");
        //FIXME : start fight wing like no deshonor
        ASData.append(_align).append("~").append(_align).append(",").append(_aLvl).append(",").append(getAlignLevel()).append(",").append(_honor).append(",").append(_deshonor + ",").append((_showWings ? "1" : "0")).append("|");
        int pdv = getLifePoints();
        int pdvMax = getMaxLifePoints();
        if (fight != null) {
            Fighter f = fight.getFighterByPerso(this);
            if (f != null) {
                pdv = f.getPDV();
                pdvMax = f.getPDVMAX();
            }
        }
        
        Stats baseStats = getPlayerData().getRaceStats();
        baseStats.addAll(getBaseStats());

        ASData.append(pdv).append(",").append(pdvMax).append("|");
        ASData.append(_energy).append(",10000|");

        ASData.append(getInitiative()).append("|");
        ASData.append(getProspection()).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_PA)).append(",").append(getStuffStats().getEffect(Effect.ADD_PA)).append(",").append(getDonsStats().getEffect(Effect.ADD_PA)).append(",").append(getBuffsStats().getEffect(Effect.ADD_PA)).append(",").append(getTotalStats().getEffect(Effect.ADD_PA)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_PM)).append(",").append(getStuffStats().getEffect(Effect.ADD_PM)).append(",").append(getDonsStats().getEffect(Effect.ADD_PM)).append(",").append(getBuffsStats().getEffect(Effect.ADD_PM)).append(",").append(getTotalStats().getEffect(Effect.ADD_PM)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_FORC)).append(",").append(getStuffStats().getEffect(Effect.ADD_FORC)).append(",").append(getDonsStats().getEffect(Effect.ADD_FORC)).append(",").append(getBuffsStats().getEffect(Effect.ADD_FORC)).append("|");
        ASData.append(getBaseStats().getEffect(Effect.ADD_VITA)).append(",").append(getStuffStats().getEffect(Effect.ADD_VITA)).append(",").append(getDonsStats().getEffect(Effect.ADD_VITA)).append(",").append(getBuffsStats().getEffect(Effect.ADD_VITA)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_SAGE)).append(",").append(getStuffStats().getEffect(Effect.ADD_SAGE)).append(",").append(getDonsStats().getEffect(Effect.ADD_SAGE)).append(",").append(getBuffsStats().getEffect(Effect.ADD_SAGE)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_CHAN)).append(",").append(getStuffStats().getEffect(Effect.ADD_CHAN)).append(",").append(getDonsStats().getEffect(Effect.ADD_CHAN)).append(",").append(getBuffsStats().getEffect(Effect.ADD_CHAN)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_AGIL)).append(",").append(getStuffStats().getEffect(Effect.ADD_AGIL)).append(",").append(getDonsStats().getEffect(Effect.ADD_AGIL)).append(",").append(getBuffsStats().getEffect(Effect.ADD_AGIL)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_INTE)).append(",").append(getStuffStats().getEffect(Effect.ADD_INTE)).append(",").append(getDonsStats().getEffect(Effect.ADD_INTE)).append(",").append(getBuffsStats().getEffect(Effect.ADD_INTE)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_PO)).append(",").append(getStuffStats().getEffect(Effect.ADD_PO)).append(",").append(getDonsStats().getEffect(Effect.ADD_PO)).append(",").append(getBuffsStats().getEffect(Effect.ADD_PO)).append("|");
        ASData.append(baseStats.getEffect(Effect.CREATURE)).append(",").append(getStuffStats().getEffect(Effect.CREATURE)).append(",").append(getDonsStats().getEffect(Effect.CREATURE)).append(",").append(getBuffsStats().getEffect(Effect.CREATURE)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_DOMA)).append(",").append(getStuffStats().getEffect(Effect.ADD_DOMA)).append(",").append(getDonsStats().getEffect(Effect.ADD_DOMA)).append(",").append(getBuffsStats().getEffect(Effect.ADD_DOMA)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_PDOM)).append(",").append(getStuffStats().getEffect(Effect.ADD_PDOM)).append(",").append(getDonsStats().getEffect(Effect.ADD_PDOM)).append(",").append(getBuffsStats().getEffect(Effect.ADD_PDOM)).append("|");
        ASData.append("0,0,0,0|");//Maitrise ?
        ASData.append(baseStats.getEffect(Effect.ADD_PERDOM)).append(",").append(getStuffStats().getEffect(Effect.ADD_PERDOM)).append("," + getDonsStats().getEffect(Effect.ADD_PERDOM)).append(",").append(getBuffsStats().getEffect(Effect.ADD_PERDOM)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_SOIN)).append(",").append(getStuffStats().getEffect(Effect.ADD_SOIN)).append(",").append(getDonsStats().getEffect(Effect.ADD_SOIN)).append(",").append(getBuffsStats().getEffect(Effect.ADD_SOIN)).append("|");
        ASData.append(baseStats.getEffect(Effect.TRAPDOM)).append(",").append(getStuffStats().getEffect(Effect.TRAPDOM)).append(",").append(getDonsStats().getEffect(Effect.TRAPDOM)).append(",").append(getBuffsStats().getEffect(Effect.TRAPDOM)).append("|");
        ASData.append(baseStats.getEffect(Effect.TRAPPER)).append(",").append(getStuffStats().getEffect(Effect.TRAPPER)).append(",").append(getDonsStats().getEffect(Effect.TRAPPER)).append(",").append(getBuffsStats().getEffect(Effect.TRAPPER)).append("|");
        ASData.append(baseStats.getEffect(Effect.RETDOM)).append(",").append(getStuffStats().getEffect(Effect.RETDOM)).append(",").append(getDonsStats().getEffect(Effect.RETDOM)).append(",").append(getBuffsStats().getEffect(Effect.RETDOM)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_CC)).append(",").append(getStuffStats().getEffect(Effect.ADD_CC)).append(",").append(getDonsStats().getEffect(Effect.ADD_CC)).append(",").append(getBuffsStats().getEffect(Effect.ADD_CC)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_EC)).append(",").append(getStuffStats().getEffect(Effect.ADD_EC)).append(",").append(getDonsStats().getEffect(Effect.ADD_EC)).append(",").append(getBuffsStats().getEffect(Effect.ADD_EC)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_AFLEE)).append(",").append(getStuffStats().getEffect(Effect.ADD_AFLEE)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_AFLEE)).append(",").append(getBuffsStats().getEffect(Effect.ADD_AFLEE)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_MFLEE)).append(",").append(getStuffStats().getEffect(Effect.ADD_MFLEE)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_MFLEE)).append(",").append(getBuffsStats().getEffect(Effect.ADD_MFLEE)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_R_NEU)).append(",").append(getStuffStats().getEffect(Effect.ADD_R_NEU)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_NEU)).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_NEU)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_RP_NEU)).append(",").append(getStuffStats().getEffect(Effect.ADD_RP_NEU)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_NEU)).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_NEU)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_R_PVP_NEU)).append(",").append(getStuffStats().getEffect(Effect.ADD_R_PVP_NEU)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_PVP_NEU)).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_PVP_NEU)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_RP_PVP_NEU)).append(",").append(getStuffStats().getEffect(Effect.ADD_RP_PVP_NEU)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_PVP_NEU)).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_PVP_NEU)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_R_TER)).append(",").append(getStuffStats().getEffect(Effect.ADD_R_TER)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_TER)).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_TER)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_RP_TER)).append(",").append(getStuffStats().getEffect(Effect.ADD_RP_TER)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_TER)).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_TER)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_R_PVP_TER)).append(",").append(getStuffStats().getEffect(Effect.ADD_R_PVP_TER)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_PVP_TER)).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_PVP_TER)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_RP_PVP_TER)).append(",").append(getStuffStats().getEffect(Effect.ADD_RP_PVP_TER)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_PVP_TER)).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_PVP_TER)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_R_EAU)).append(",").append(getStuffStats().getEffect(Effect.ADD_R_EAU)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_EAU)).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_EAU)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_RP_EAU)).append(",").append(getStuffStats().getEffect(Effect.ADD_RP_EAU)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_EAU)).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_EAU)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_R_PVP_EAU)).append(",").append(getStuffStats().getEffect(Effect.ADD_R_PVP_EAU)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_PVP_EAU)).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_PVP_EAU)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_RP_PVP_EAU)).append(",").append(getStuffStats().getEffect(Effect.ADD_RP_PVP_EAU)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_PVP_EAU)).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_PVP_EAU)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_R_AIR)).append(",").append(getStuffStats().getEffect(Effect.ADD_R_AIR)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_AIR)).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_AIR)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_RP_AIR)).append(",").append(getStuffStats().getEffect(Effect.ADD_RP_AIR)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_AIR)).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_AIR)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_R_PVP_AIR)).append(",").append(getStuffStats().getEffect(Effect.ADD_R_PVP_AIR)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_PVP_AIR)).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_PVP_AIR)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_RP_PVP_AIR)).append(",").append(getStuffStats().getEffect(Effect.ADD_RP_PVP_AIR)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_PVP_AIR)).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_PVP_AIR)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_R_FEU)).append(",").append(getStuffStats().getEffect(Effect.ADD_R_FEU)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_FEU)).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_FEU)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_RP_FEU)).append(",").append(getStuffStats().getEffect(Effect.ADD_RP_FEU)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_FEU)).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_FEU)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_R_PVP_FEU)).append(",").append(getStuffStats().getEffect(Effect.ADD_R_PVP_FEU)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_PVP_FEU)).append(",").append(getBuffsStats().getEffect(Effect.ADD_R_PVP_FEU)).append("|");
        ASData.append(baseStats.getEffect(Effect.ADD_RP_PVP_FEU)).append(",").append(getStuffStats().getEffect(Effect.ADD_RP_PVP_FEU)).append(",").append(0).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_PVP_FEU)).append(",").append(getBuffsStats().getEffect(Effect.ADD_RP_PVP_FEU)).append("|");
		//stuff de classe

        return ASData.toString();
    }

    public String xpString(String c) {
        long min = expLevel.player;
        long cur = _curExp;
        long max = expLevel.getNext() == null ? -1 : expLevel.getNext().player;
        
        return cur + c + min + c + max;
    }

    public int emoteActive() {
        return _emoteActive;
    }

    public void setEmoteActive(int emoteActive) {
        _emoteActive = emoteActive;
    }

    public Stats getStuffStats() {
        Stats stats = new Stats();
        
        if(getPlayerData().canHaveStuff()){
            Set<ItemSet> itemSetApplied = new HashSet<>();

            for (Item item : items.getEquipedItems()) {
                item.getStatsTemplate().addAllStats(stats);
                ItemSet IS = item.getTemplate().getItemSet();

                if(IS != null && !itemSetApplied.contains(IS)){
                    stats.addAll(IS.getBonusStatByItemNumb(items.getEquipedItemSetTemplates(IS).size()));
                    itemSetApplied.add(IS);
                }
            }
            
            if (isOnMount()) {
                stats.addAll(mount.getStats());
            }
        }
        
        return stats;
    }

    private Stats getBuffsStats() {
        Stats stats = new Stats();
        
        for(BuffItem buff : items.getBuffItems()){
            stats.addAll(buff.getStats());
        }
        
        return stats;
    }

    /**
     * Energie *
     */
    public int isDead() {
        return _isDead;
    }

    public void setDead()//Apres reconnection il n'est plus en tombe mais en fantome
    {
        set_canAggro(false);
        setAway(true);
        _isDead = 2;

        setGfxID(8004);
        restrictions.setDeathRestrictions();
        send(new AlterRestrictions(restrictions));
    }

    public void setFuneralStone() {
        if (isOnMount()) {
            toggleOnMount();
        }
        set_canAggro(false);
        setAway(true);
        _isDead = 1;

        restrictions.setDeathRestrictions();
        send(new AlterRestrictions(restrictions));
        setGfxID(Integer.parseInt(getRace() + "3"));
        SocketManager.MESSAGE_BOX(this.getAccount().getGameThread(), "112");
        SocketManager.GAME_SEND_Im_PACKET(this, "1190;" + _name);
        map.sendToMap(new AlterSprite(this));
    }

    public void setGhost() {
        if (isOnMount()) {
            toggleOnMount();
        }
        set_canAggro(false);
        setAway(true);
        _isDead = 2;

        setGfxID(8004);
        restrictions.setDeathRestrictions();
        send(new AlterRestrictions(restrictions));
        SocketManager.GAME_SEND_INFO_HIGHLIGHT_PACKET(this, Constants.ALL_PHOENIX);
    }

    public void setAlive() {
        if (_isDead == 0 || _isDead == 1) {
            return;
        }
        set_canAggro(true);
        setAway(false);
        _isDead = 0;
        setLifePoints(1);
        setEnergy(1000);
        setGfxID(getRace().ordinal() * 10 + getGender());
        restrictions.unsetDeathRestrictions();
        send(new AlterRestrictions(restrictions));
        send(new InfoMessage(InfoMessage.Type.INFO).addMessage(33));
        SocketManager.GAME_SEND_STATS_PACKET(this);
        map.sendToMap(new AlterSprite(this));
    }

    /**
     * Energie *
     */

    public int getOrientation() {
        return _orientation;
    }

    public void setOrientation(int _orientation) {
        this._orientation = _orientation;
    }

    public int getInitiative() {
        return getPlayerData().getInitiative();
    }

    public int getProspection() {
        int ret = 0;
        Stats tot = getTotalStats();
        ret += tot.getEffect(Effect.ADD_PROS);
        ret += (int) (tot.getEffect(Effect.ADD_CHAN) / 10);
        return ret;
    }

    public Stats getTotalStats() {
        Stats total = new Stats(getPlayerData().getRaceStats());
        total.addAll(getBaseStats());
        total.addAll(getStuffStats());
        total.addAll(getDonsStats());
        total.addAll(getBuffsStats());

        return total;
    }

    private Stats getDonsStats() {
        /* TODO*/
        Stats stats = new Stats();
        
        
        
        return stats;
    }

    public int getPodUsed() {
        int pod = 0;
        for (Item item : items) {
            pod += item.getTemplate().getPod() * item.getQuantity();
        }
        return pod;
    }

    public int getMaxPod() {
        Stats stats = getTotalStats();
        int pods = stats.getEffect(Effect.ADD_PODS);
        pods += stats.getEffect(Effect.ADD_FORC) * 5;
        
        for (JobStats SM : jobs) {
            pods += SM.getLevel() * 5;
            if (SM.getLevel() == 100) {
                pods += 1000;
            }
        }
        return pods;
    }

    public int getLifePoints() {
        return _PDV;
    }

    public void setLifePoints(int _pdv) {
        _PDV = _pdv;
        if (_group != null) {
            SocketManager.GAME_SEND_PM_MOD_PACKET_TO_GROUP(_group, this);
        }
    }

    public int getMaxLifePoints() {
        return _PDVMAX;
    }

    public void setMaxLifePoints(int _pdvmax) {
        _PDVMAX = _pdvmax;
        if (_group != null) {
            SocketManager.GAME_SEND_PM_MOD_PACKET_TO_GROUP(_group, this);
        }
    }

    public boolean inRegeneration(int typeRegen) {
        return _curRegenTimer == typeRegen;
    }

    private void startRegenTimer(int timer) {
        _curRegenTimer = timer;

        // Timestamp du début de la position assise
        _startRegenTime = System.currentTimeMillis();
        // Lancement du timer vita côté client
        SocketManager.GAME_SEND_ILS_PACKET(this, timer);
    }

    private void endRegenTimer(int hpsWon) {
        _startRegenTime = 0;
        _curRegenTimer = 0;
        SocketManager.GAME_SEND_ILF_PACKET(this, hpsWon); // Envoi du nbre de pdv gagnés & arrêt du timer
    }

    public void stopAllRegen() {
        if (_curRegenTimer != 0 && _startRegenTime != 0) {
            setSitted(false);
        }
    }

    // Fuck the nuggets ! Lipton Icetea, c'mon bébé !
    public void setSitted(boolean sit) {
        // Debout & demande de se relever
        if (!_sitted && !sit) {
            if (_curRegenTimer == 0 || _startRegenTime == 0) {
                if (_curRegenTimer != 0 || _startRegenTime != 0) {
                    return;
                }

                // Start timers regen en étant debout
                startRegenTimer(Constants.STAND_TIMER_REGEN);
                return;
            }

            // calcul du regen vita en étant debout ...
            long hpWon = (System.currentTimeMillis() - _startRegenTime) / _curRegenTimer;
            if ((_PDV + hpWon) > _PDVMAX) // Si le gain de vie dépasse _PDVMAX
            {
                hpWon = _PDVMAX - _PDV;
            }

            _PDV += hpWon;
            _startRegenTime = 0;
            _curRegenTimer = 0;
        } // Debout & on demande de s'asseoir
        else if (!_sitted && sit) {
            // Tente de s'asseoir mais déjà assis
            if ((_emoteActive == 1 || _emoteActive == 19) && sit) {
                return;
            }

            // On récupère d'abord les pdvs en étant debout si c'est le cas
            if (inRegeneration(Constants.STAND_TIMER_REGEN)) {
                setSitted(false);
            }

            // Start timers de regen en étant assis ...
            startRegenTimer(Constants.SIT_TIMER_REGEN);
            _sitted = true;
        } // Assis & on se relève
        else if (_sitted && !sit) {
            if (_curRegenTimer == 0 || _startRegenTime == 0) {
                return; // Ne devrait pas arriver
            }
            // Calcul du regen vita en étant assis
            long hpWon = (System.currentTimeMillis() - _startRegenTime) / _curRegenTimer;
            if ((_PDV + hpWon) > _PDVMAX) // Si le gain de vie dépasse _PDVMAX
            {
                hpWon = _PDVMAX - _PDV;
            }

            _PDV += hpWon;
            endRegenTimer((int) hpWon);

            _sitted = false;
            setSitted(false); // Relance fonction pour regen en étant debout
        }
    }

    public byte getAlignement() {
        return _align;
    }

    public int getLifePercent() {
        int pdvper = 100;
        pdvper = (100 * _PDV) / _PDVMAX;
        return pdvper;
    }

    public void emoticone(String str) {
//        try {
//            int id = Integer.parseInt(str);
//            if (fight == null) {
//                SocketManager.GAME_SEND_EMOTICONE_TO_MAP(map, id, id);
//            } else {
//                SocketManager.GAME_SEND_EMOTICONE_TO_FIGHT(fight, 7, id, id);
//            }
//        } catch (NumberFormatException e) {
//            return;
//        };
    }

    public boolean isMuted() {
        return account.isMuted();
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    @Deprecated
    public boolean addObjet(Item newObj, boolean stackIfSimilar) {
        items.addItem(newObj);
        return true;
    }

    public void addItem(Item newObj) {
        items.addItem(newObj);
    }

    public Inventory getItems() {
        return items;
    }

    public String parseItemToASK() {
        StringBuilder str = new StringBuilder();
        
        for (Item obj : items) {
            str.append(obj.getPacket()).append(';');
        }
        
        return str.toString();
    }

    public String getItemsIDSplitByChar(String splitter) {
        StringBuilder str = new StringBuilder();
        
        for (Item entry : items) {
            if (str.length() != 0) {
                str.append(splitter);
            }
            str.append(entry.getGuid());
        }
        
        return str.toString();
    }

    public boolean hasItemGuid(int guid) {
        return items.get(guid) != null ? items.get(guid).getQuantity() > 0 : false;
    }

    public void sellItem(int guid, int qua) {
        if (qua <= 0) {
            return;
        }
        if (items.get(guid).getQuantity() < qua)//Si il a moins d'item que ce qu'on veut Del
        {
            qua = items.get(guid).getQuantity();
        }

        int prix = qua * (items.get(guid).getTemplate().getPrix() / 10);//Calcul du prix de vente (prix d'achat/10)
        int newQua = items.get(guid).getQuantity() - qua;

        if (newQua <= 0)//Ne devrait pas etre <0, S'il n'y a plus d'item apres la vente 
        {
            items.remove(guid);
            SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, guid);
        } else//S'il reste des items apres la vente
        {
            items.get(guid).setQuantity(newQua);
            SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this, items.get(guid));
        }
        _kamas = _kamas + prix;
        SocketManager.GAME_SEND_STATS_PACKET(this);
        SocketManager.GAME_SEND_Ow_PACKET(this);
        SocketManager.GAME_SEND_ESK_PACKEt(this);
    }

    @Deprecated
    public void removeItem(int guid) {
        items.remove(guid);
    }

    @Deprecated
    public void removeItem(int guid, int nombre, boolean send, boolean deleteFromWorld) {
        Item obj = items.get(guid);

        if (nombre > obj.getQuantity()) {
            nombre = obj.getQuantity();
        }
        
        items.changeQuantity(obj, obj.getQuantity() - nombre);
    }

    @Deprecated
    public void deleteItem(int guid) {
        items.remove(guid);
    }

    public Item getItemByPos(InventoryPosition pos) {
        return items.getItemByPos(pos);
    }

    public void refreshStats() {
        checkMutant();
        double actPdvPer = (100 * (double) _PDV) / (double) _PDVMAX;
        _PDVMAX = getTotalStats().getEffect(Effect.ADD_VITA);
        _PDV = (int) Math.round(_PDVMAX * actPdvPer / 100);
    }

    public void addKamas(long l) {
        setKamas(_kamas + l);
    }

    public Item getSimilarItem(Item exObj) {
        return items.getSimilarItem(exObj);
    }
    
    public boolean hasEquiped(ItemTemplate template){
        return items.isEquiped(template);
    }

    public void setCurExchange(ExchangeBase echg) {
        curExchange = echg;
    }

    public ExchangeBase getCurExchange() {
        return curExchange;
    }

    public NPC_Exchange get_curNpcExchange() {
        return _curNpcExchange;
    }

    public void setCurNpcExchange(NPC_Exchange echg) {
        _curNpcExchange = echg;
    }
	//Metier
    
    public boolean hasJob(Job job){
        return getJobStats(job) != null;
    }
    
    public JobStats getJobStats(Job job){
        for(JobStats stats : jobs){
            if(stats.getJob().equals(job))
                return stats;
        }
        return null;
    }

    public Collection<JobStats> getJobs() {
        return jobs;
    }

    public boolean learnJob(Job job, ExpLevel startLevel) {
        if(hasJob(job)){
            send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(11));
            return false;
        }
        
        if(jobs.size() >= 6){
            send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(9));
            return false;
        }
        
        if(job.isMageJob()){
            if(countMageJobs() >= 3){
                send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(9));
                return false;
            }
            
            if(!hasJob(job.getBaseJob())){
                send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(2));
                return false;
            }
        }
        
        jobs.add(new JobStats(job, startLevel, startLevel.job));
        send(new InfoMessage(InfoMessage.Type.INFO).addMessage(2, job.getId()));
        return true;
    }

    public void unlearnJob(JobStats jobStats) {
        jobs.remove(jobStats);
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    public String parseToPM() {
        StringBuilder str = new StringBuilder();
        str.append(id).append(";");
        str.append(_name).append(";");
        str.append(getGfxID()).append(";");
        str.append(_color1).append(";");
        str.append(_color2).append(";");
        str.append(_color3).append(";");
        str.append(SpriteParser.stuffToString(items)).append(";");
        str.append(_PDV).append(",").append(_PDVMAX).append(";");
        str.append(getLevel()).append(";");
        str.append(getInitiative()).append(";");
        str.append(getTotalStats().getEffect(Effect.ADD_PROS)).append(";");
        str.append("0");//Side = ?
        return str.toString();
    }

    @Deprecated
    public int getNumbEquipedItemOfPanoplie(ItemSet is) {
        //TODO need optimisation with Inventory.isEquiped
        int nb = 0;
        for (Item i : items) {
            //On ignore les objets non équipés
            if (i.getPosition() == InventoryPosition.NO_EQUIPED) {
                continue;
            }
            //On prend que les items de la pano demandée, puis on augmente le nombre si besoin
            if (i.getTemplate().getItemSet() == is) {
                nb++;
            }
        }
        return nb;
    }

    public void addCapital(int pts) {
        _capital += pts;
    }
    
    public void removeCapitalPoints(int nb){
        _capital -= nb;
    }

    public void addSpellPoint(int pts) {
        _spellPts += pts;
    }

    public void fullPDV() {
        _PDV = _PDVMAX;
    }

    //TODO: what is that ???
    public void removeByTemplateID(int tID, int count) {
        items.removeByTemplateId(tID, count);
    }

    public String parseJobData() {
        StringBuilder str = new StringBuilder();
        if (jobs.isEmpty()) {
            return "";
        }
        for (JobStats SM : jobs) {
            if (str.length() > 0) {
                str.append(";");
            }
            str.append(SM.getJob().getId()).append(",").append(SM.getXp());
        }
        return str.toString();
    }

    public int countBasicJobs() {
        int i = 0;

        for(JobStats stats : jobs){
            if(!stats.getJob().isMageJob())
                ++i;
        }
        
        return i;
    }

    public int countMageJobs() {
        int i = 0;

        for(JobStats stats : jobs){
            if(stats.getJob().isMageJob())
                ++i;
        }
        
        return i;
    }

    public boolean canAggro() {
        return _canAggro;
    }

    public void set_canAggro(boolean canAggro) {
        _canAggro = canAggro;
    }

    public boolean isOnMount() {
        return getPlayerData().canHaveMount() && _onMount && mount != null;
    }

    public void toggleOnMount() {
        if(fight != null && fight.getState() != Constants.FIGHT_STATE_PLACE)
            return;
        
        if(_onMount == false && !getPlayerData().canHaveMount())
            return;
        
        _onMount = !_onMount;
        
        items.unequipItem(InventoryPosition.FAMILIER);
        
        //on envoie les packets
        if (getFight() != null) {
            fight.sendToFight(new AlterSprite(this));
        } else {
            map.sendToMap(new AlterSprite(this));
        }

        send(new MountEquiped(mount));
        SocketManager.GAME_SEND_Rr_PACKET(this, _onMount ? "+" : "-");
        SocketManager.GAME_SEND_STATS_PACKET(this);
        
        // Lenders, perte d'energie de la mount ;).
        int EnergyoLose = mount.get_energie() - 10;
        mount.setEnergie(EnergyoLose);
    }

    public int getMountXpGive() {
        return _mountXpGive;
    }

    public Mount getMount() {
        return mount;
    }

    public void setMount(Mount mount) {
        if(isOnMount())
            toggleOnMount();
        
        this.mount = mount;
    }

    public void setMountGiveXp(int parseInt) {
        _mountXpGive = parseInt;
    }

    public int getDisgrace() {
        return _deshonor;
    }

    public void resetVars() {
        _isTradingWith = 0;
        _away = false;
        _emoteActive = 0;
        fight = null;
        _duelID = 0;
        curExchange = null;
        _group = null;
        request = null;
        _sitted = false;
        currentWaypoint = null;
        _onMount = false;
        _isOnPercepteurID = 0;
        _isClone = false;
        _isForgetingSpell = false;
        _isAbsent = false;
        _isInvisible = false;
        _Follower.clear();
        _Follows = null;
        _curMount = null;
        //_isGhosts = false;
        _isDead = 0;
    }

    public void addChanel(String chan) {
        if (_canaux.indexOf(chan) >= 0) {
            return;
        }
        _canaux += chan;
        SocketManager.GAME_SEND_cC_PACKET(this, '+', chan);
    }

    public void removeChanel(String chan) {
        _canaux = _canaux.replace(chan, "");
        SocketManager.GAME_SEND_cC_PACKET(this, '-', chan);
    }

    public void modifAlignement(byte a) {
        //Reset Variables
        _honor = 0;
        _deshonor = 0;
        _align = a;
        _aLvl = 1;
		//envoies des packets
        //Im022;10~42 ?
        SocketManager.GAME_SEND_ZC_PACKET(this, a);
        SocketManager.GAME_SEND_STATS_PACKET(this);
        //Im045;50 ?
    }

    public void setDisgrace(int deshonor) {
        _deshonor = deshonor;
    }

    /**
     * 
     * @return
     * @deprecated
     * @see Player#setDisgrace(int) 
     */
    @Deprecated
    public int getDeshonor() {
        return _deshonor;
    }

    public void setShowWings(boolean showWings) {
        _showWings = showWings;
    }

    public int getHonor() {
        return _honor;
    }

    public void setHonor(int honor) {
        _honor = honor;
    }

    public void setAlignLevel(int a) {
        _aLvl = a;
    }

    public int getAlignLevel() {
        return _aLvl;
    }
    
    /**
     * @see Player#getAlignLevel() 
     * @return 
     */
    public int getGrade(){
        return getAlignLevel();
    }

    public void toggleWings(char c) {
        if (_align == Constants.ALIGNEMENT_NEUTRE) {
            return;
        }
        int hloose = _honor * 5 / 100;//FIXME: perte de X% honneur
        switch (c) {
            case '*':
                SocketManager.GAME_SEND_GIP_PACKET(this, hloose);
                return;
            case '+':
                setShowWings(true);
                SocketManager.GAME_SEND_STATS_PACKET(this);
//                Database.SAVE_PERSONNAGE(this, false);
                break;
            case '-':
                if (this.getAlignement() == Constants.ALIGNEMENT_BONTARIEN && (this.getMap().getSubArea().getAlignement() == 2 || this.getMap().getSubArea().getArea().getalignement() == 2)) {
                    SocketManager.GAME_SEND_Im_PACKET(this, "1150");
                    return;
                }
                if (this.getAlignement() == Constants.ALIGNEMENT_BRAKMARIEN && (this.getMap().getSubArea().getAlignement() == 1 || this.getMap().getSubArea().getArea().getalignement() == 1)) {
                    SocketManager.GAME_SEND_Im_PACKET(this, "1150");
                    return;
                }
                setShowWings(false);
                _honor -= hloose;
                SocketManager.GAME_SEND_STATS_PACKET(this);
  //              Database.SAVE_PERSONNAGE(this, false);
                break;
        }
        //SocketManager.GAME_SEND_ALTER_GM_PACKET(_curCarte, this);
    }

    public GuildMember getGuildMember() {
        return _guildMember;
    }

    public void setAccount(Account c) {
        account = c;
    }

    public boolean hasZaap(WaypointHandler.Waypoint waypoint) {
        return zaaps.contains(waypoint);
    }
    
    public void addZaap(WaypointHandler.Waypoint waypoint){
        zaaps.add(waypoint);
    }

    public WaypointHandler.Waypoint getCurrentWaypoint() {
        return currentWaypoint;
    }

    public void setCurrentWaypoint(WaypointHandler.Waypoint currentWaypoint) {
        this.currentWaypoint = currentWaypoint;
    }
    
    public Set<WaypointHandler.Waypoint> getZaaps() {
        return zaaps;
    }

    @Deprecated
    public boolean hasItemTemplate(int i, int q) {
        for (Item obj : items) {
            if (obj.getPosition() != InventoryPosition.NO_EQUIPED) {
                continue;
            }
            if (obj.getTemplate().getID() != i) {
                continue;
            }
            if (obj.getQuantity() >= q) {
                return true;
            }
        }
        return false;
    }

    public void setisForgetingSpell(boolean isForgetingSpell) {
        _isForgetingSpell = isForgetingSpell;
    }

    public boolean isForgetingSpell() {
        return _isForgetingSpell;
    }

    public boolean isDispo(Player sender) {
        if (_isAbsent) {
            return false;
        }

        if (_isInvisible) {
            return account.getFriendList().isFriend(sender.getAccount());
        }

        return true;
    }

    public boolean get_isClone() {
        return _isClone;
    }

    public void set_isClone(boolean isClone) {
        _isClone = isClone;
    }

    public int get_isOnPercepteurID() {
        return _isOnPercepteurID;
    }

    public void set_isOnPercepteurID(int isOnPercepteurID) {
        _isOnPercepteurID = isOnPercepteurID;
    }

    public void set_title(String title) {
        _title = title;
    }

    public String get_title() {
        return _title;
    }

    public long getLastPacketTime() {
        return _lastPacketTime;
    }

    public int get_lastPersoAgro() {
        return _lastPersoAgro;
    }

    public void set_lastPersoAgro(int guid) {
        _lastPersoAgro = guid;
    }

    public void refreshLastPacketTime() {
        _lastPacketTime = System.currentTimeMillis();
    }

    public traque get_traque() {
        return _traqued;
    }

    public void set_traque(traque traq) {
        _traqued = traq;
    }

	//Mariage
//    public void MarryTo(Player wife) {
//        _wife = wife.getSpriteId();
////        Database.SAVE_PERSONNAGE(this, true);
//    }

//    public String get_wife_friendlist() {
//        Player wife = World.getPersonnage(_wife);
//        StringBuilder str = new StringBuilder();
//        if (wife != null) {
//            str.append(wife.getName()).append("|").append(wife.getGfxID()).append("|").append(wife.getColor1()).append("|").append(wife.getColor2()).append("|").append(wife.getColor3()).append("|");
//            if (!wife.isOnline()) {
//                str.append("|");
//            } else {
//                str.append(wife.parse_towife()).append("|");
//            }
//        } else {
//            str.append("|");
//        }
//        return str.toString();
//    }
//
//    public String parse_towife() {
//        int f = 0;
//        if (fight != null) {
//            f = 1;
//        }
//        return _curCarte.getId() + "|" + _lvl + "|" + f;
//    }

//    public void meetWife(Player p)// Se teleporter selon les sacro-saintes autorisations du mariage.
//    {
//        if (p == null) {
//            return; // Ne devrait theoriquement jamais se produire.
//        }
//        int dist = (_curCarte.getX() - p.getMap().getX()) * (_curCarte.getX() - p.getMap().getX())
//                + (_curCarte.getY() - p.getMap().getY()) * (_curCarte.getY() - p.getMap().getY());
//        if (dist > 10)// La distance est trop grande...
//        {
//            if (p.getGender() == 0) {
//                SocketManager.GAME_SEND_Im_PACKET(this, "178");
//            } else {
//                SocketManager.GAME_SEND_Im_PACKET(this, "179");
//            }
//            return;
//        }
//
//        int cellPositiontoadd = Constants.getNearCellidUnused(p);
//        if (cellPositiontoadd == -1) {
//            if (p.getGender() == 0) {
//                SocketManager.GAME_SEND_Im_PACKET(this, "141");
//            } else {
//                SocketManager.GAME_SEND_Im_PACKET(this, "142");
//            }
//            return;
//        }
//
//        teleport(p.getMap().getSpriteId(), (p.getCell().getSpriteId() + cellPositiontoadd));
//    }

//    public void Divorce() {
//        if (isOnline()) {
//            SocketManager.GAME_SEND_Im_PACKET(this, "047;" + World.getPersonnage(_wife).getName());
//        }
//
//        _wife = 0;
////        Database.SAVE_PERSONNAGE(this, true);
//    }

//    public int getWife() {
//        return _wife;
//    }

//    public int setisOK(int ok) {
//        return _isOK = ok;
//    }
//
//    public int getisOK() {
//        return _isOK;
//    }

    public void changeOrientation(int toOrientation) {
        if (this.getOrientation() == 0
                || this.getOrientation() == 2
                || this.getOrientation() == 4
                || this.getOrientation() == 6) {
            this.setOrientation(toOrientation);
            SocketManager.GAME_SEND_eD_PACKET_TO_MAP(getMap(), this.getSpriteId(), toOrientation);
        }
    }

    public Mount getInMount() {
        return _curMount;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    public boolean isForView() {
        return forView;
    }

    public void setSavedPos(String savedPos) {
        this._savedPos = savedPos;
    }

    public String getSavedPos() {
        return this._savedPos;
    }

    // Quests
//    public Map<String, String> get_quest(int questId) {
//        return _quests.get(questId);
//    }
//
//    // a-t-il cet objectif À FAIRE ?
//    public boolean hasObjective(int objectiveId) {
//        for (Entry<Integer, Map<String, String>> entry : _quests.entrySet()) // Chaque quete
//        {
//            if (!entry.getValue().get("curStep").equals("-1") && entry.getValue().get("objectivesToDo").contains(objectiveId + "")) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    public boolean hasQuest(int questId, int stepId) {
//        Map<String, String> questTarget = _quests.get(questId);
//        if (questTarget == null) {
//            return false;
//        }
//
//        if (stepId != -1 && stepId != Integer.parseInt(questTarget.get("curStep"))) // Une étape précise est renseignée
//        {
//            return false;
//        }
//
//        return true;
//    }
//
//    private void initializeQuests(String quests) // Transforme string quest en tableau
//    {
//        if (quests == null) {
//            return;
//        }
//        String[] myQuests = quests.split("\\|");
//
//        for (String quest : myQuests) {
//            if (quest == null || (quest != null && !quest.contains(":")) || (quest != null && quest.isEmpty())) {
//                return;
//            }
//
//            String[] questInfos = quest.split(":");
//            if (questInfos.length < 2) {
//                return;
//            }
//            int questId = Integer.parseInt(questInfos[0]);
//            _quests.put(questId, new HashMap<String, String>());
//
//            if (questInfos[1].equals("-1")) {
//                _quests.get(questId).put("curStep", "-1");
//            } else {
//                String[] questDetails = questInfos[1].split(";");
//                if (questDetails.length < 2) {
//                    return;
//                }
//                _quests.get(questId).put("curStep", questDetails[0]);
//                _quests.get(questId).put("objectivesToDo", questDetails[1]);
//            }
//        }
//    }
//
//    public String questsToString() {
//        String quests = "";
//
//        for (Entry<Integer, Map<String, String>> entry : _quests.entrySet()) {
//            quests += entry.getKey() + ":"; // Id quête
//            if (entry.getValue().get("curStep").equals("-1")) // Quête finie
//            {
//                quests += "-1|";
//            } else // Quete en cours
//            {
//                quests += entry.getValue().get("curStep") + ";";       // Etape actuelle
//                if (!entry.getValue().get("objectivesToDo").equals("")) // d'objectif restant
//                {
//                    quests += entry.getValue().get("objectivesToDo"); // Objectifs restants
//                } else {
//                    quests += " ";
//                }
//                quests += "|";
//            }
//        }
//
//        return quests;
//    }
//
//    public String parseQuestsList() {
//
//        String packet = "QL|";
//
//        for (Entry<Integer, Map<String, String>> entry : _quests.entrySet()) {
//            if (entry != null) {
//                packet += entry.getKey() + ";";
//                if (entry.getValue().get("curStep").equals("-1")) {
//                    packet += "1;";
//                } else {
//                    packet += "0;";
//                }
//
//                packet += "1|";
//            }
//        }
//
//        return packet.substring(0, packet.length() - 1);
//    }
//
//    public String parseQuestStep(int questId) {
//        Map<String, String> questPerso = _quests.get(questId);
//        if (questPerso == null) {
//            return ""; // On a pas la quête demandée
//        }
//
//        Map<String, String> questDetails = World.getQuest(questId);
//        if (questDetails == null) {
//            GameServer.addToLog("Game: Error >> Quete " + questId + " inexistante ");
//            return ""; // La quête demandée n'existe pas
//        }
//        String packet = "QS" + questId + "|";
//
//        String questSteps = questDetails.get("steps"); // Etapes de la quête split par ;
//        String curStep = questPerso.get("curStep");
//        if (curStep.equals("-1")) // Quête finie : on prend la dernière étape
//        {
//            String[] splitSteps = questSteps.split(";");
//            String lastStep = splitSteps[splitSteps.length - 1];
//            curStep = lastStep;
//        }
//        packet += curStep + "|";
//
//        Map<String, String> stepDetails = World.getStep(Integer.parseInt(curStep));
//        String[] allObjectives = stepDetails.get("objectives").split(";");
//
//        for (String curObj : allObjectives) // Chaque objectif de l'étape actuelle
//        {
//            packet += curObj + ",";
//            if (!questPerso.get("objectivesToDo").contains(curObj)) // Objectif fait ? 
//            {
//                packet += "1;";
//            } else {
//                packet += "0;";
//            }
//
//        }
//        packet = packet.substring(0, packet.length() - 1) + "|"; // Enlève le dernier ; en trop
//
//        int index = questSteps.indexOf(curStep);
//        if (index > 0) {
//            index -= 1;
//        }
//        packet += questSteps.substring(0, index) + "|"; // Etapes avant l'actuelle
//        if (questSteps.length() < questSteps.indexOf(curStep) + 1 + curStep.length() + 1) {
//            packet += "|"; // = Etape actuelle est la derniere
//        } else // Il reste des etapes, on les prend
//        {
//            packet += questSteps.substring(questSteps.indexOf(curStep) + curStep.length() + 1) + "|";
//        }
//
//        packet += stepDetails.get("question") + ";"; // TODO params de la question quete
//
//        return packet;
//    }
//
//    public String confirmObjective(int type, String args, String args2) {
//        for (Entry<Integer, Map<String, String>> entry : _quests.entrySet()) // Chaque quête 
//        {
//            if (!entry.getValue().get("curStep").equals("-1")) // Si non terminée
//            {
//                String objectivesToDo = entry.getValue().get("objectivesToDo");
//                String[] splitObjectives = objectivesToDo.split(",");
//                for (String toDo : splitObjectives) {
//                    if (toDo == null || (toDo != null && toDo.equals(" ")) || (toDo != null && toDo.isEmpty())) {
//                        return "";
//                    }
//                    Map<String, String> obj = World.getObjective(Integer.parseInt(toDo));
//                    if (obj == null) {
//                        return ""; // Objectif inexistant
//                    }
//                    if (type == 1 || type == 2 || type == 4 || type == 8) // Objectifs simple : "Aller voir PNJ" OU "Montrer Nbre Item à Pnj" OU "Découvrir carte MAPID" OU "Utiliser objet X" 
//                    {
//                        if (obj.get("type").equals(type + "") && obj.get("args").equals(args)) {
//                            objectivesToDo = objectivesToDo.replaceAll(toDo + ",", "");
//                            objectivesToDo = objectivesToDo.replaceAll("," + toDo, "");
//                            objectivesToDo = objectivesToDo.replaceAll(toDo, "");
//
//                            _quests.get(entry.getKey()).put("objectivesToDo", objectivesToDo);
//                            SocketManager.GAME_SEND_Im_PACKET(this, "055;" + entry.getKey()); // Quete MaJ
//                        }
//                    } else if (type == 3) // "Ramener à PNJ NBRE ITEMS"
//                    { // Args = ItemId,Quantite
//                        if (obj.get("type").equals(type + "") && obj.get("args").equals(args)) {
//                            // On retire l'item demandé
//                            int itemId = 0;
//                            int q = 1;
//
//                            if (args.contains(",")) {
//                                itemId = Integer.parseInt(args.split(",")[0]);
//                                q = Integer.parseInt(args.split(",")[1]);
//                            } else {
//                                itemId = Integer.parseInt(args);
//                            }
//                            removeByTemplateID(itemId, q);
//                            SocketManager.GAME_SEND_Ow_PACKET(this);
//                            SocketManager.GAME_SEND_Im_PACKET(this, "022;" + q + "~" + itemId);
//
//                            objectivesToDo = objectivesToDo.replaceAll(toDo + ",", "");
//                            objectivesToDo = objectivesToDo.replaceAll("," + toDo, "");
//                            objectivesToDo = objectivesToDo.replaceAll(toDo, "");
//
//                            _quests.get(entry.getKey()).put("objectivesToDo", objectivesToDo);
//                            SocketManager.GAME_SEND_Im_PACKET(this, "055;" + entry.getKey()); // Quete MaJ
//                        }
//                    } else if (type == 6) {
//                        for (String curMob : args.split(";")) // Différents mobs vaincus
//                        {
//                            if (curMob == null || (curMob != null && curMob.isEmpty())) {
//                                return "";
//                            }
//
//                            if (obj.get("type").equals(type + "")) {
//                                int mobId = Integer.parseInt(curMob.split(",")[0]);
//                                int curMobId = Integer.parseInt(obj.get("args").split(",")[0]);
//                                int curQ = Integer.parseInt(obj.get("args").split(",")[1]);
//                                int mobQ = Integer.parseInt(curMob.split(",")[1]);
//
//                                if (curMobId == mobId && curQ <= mobQ) {
//                                    // Avis de recherche :  don item follower sur pos bonbon + ajout follower
//                                    int itemFollow;
//                                    @SuppressWarnings("unused")
//                                    int itemFollowPos;
//                                    int skinFollow;
//                                    if ((itemFollow = Constants.getItemByHuntMob(mobId)) != -1 && (itemFollowPos = Constants.getAvailableCandyPos(this)) != -1) {
//                                        ItemTemplate t = World.getObjTemplate(itemFollow);
//                                        Item followObj = t.createNewItem(1, true);
//                                        if (addItem(followObj, true))//Si le joueur n'avait pas d'item similaire
//                                        {
//                                            //World.addItem(followObj, true);
//                                        }
//
//                                        SocketManager.GAME_SEND_Im_PACKET(this, "021;1~" + itemFollow);
//                                    }
////                                    if ((skinFollow = Constants.getSkinByHuntMob(mobId)) != -1) {
////                                        set_follow(skinFollow + "");
////                                    }
//                                    // Fin avis de recherche
//                                    objectivesToDo = objectivesToDo.replaceAll(toDo + ",", "");
//                                    objectivesToDo = objectivesToDo.replaceAll("," + toDo, "");
//                                    objectivesToDo = objectivesToDo.replaceAll(toDo, "");
//
//                                    _quests.get(entry.getKey()).put("objectivesToDo", objectivesToDo);
//                                    SocketManager.GAME_SEND_Im_PACKET(this, "055;" + entry.getKey()); // Quete MaJ
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }
//        }
//
//        return "";
//    }
//
//    public boolean canDoObjective(int type, String args) // Le player peut-il faire cet objectif ? 
//    {
//
//        if (type == 3) // "Ramener à PNJ NBRE ITEMS"
//        { // Args = ItemId,Quantite
//            int itemId = 0;
//            int q = 1;
//
//            if (args.contains(",")) {
//                itemId = Integer.parseInt(args.split(",")[0]);
//                q = Integer.parseInt(args.split(",")[1]);
//            } else {
//                itemId = Integer.parseInt(args);
//            }
//
//            if (!hasItemTemplate(itemId, q)) {
//                return false;
//            }
//
//            return true;
//        }
//
//        return true;
//    }
//
//    public void addNewQuest(int questId) {
//        Map<String, String> questDetails = World.getQuest(questId);
//        if (questDetails == null) // quete inexistante
//        {
//            GameServer.addToLog("Game: Error >> Quete " + questId + " inexistante.");
//            return;
//        }
//        if (_quests.get(questId) != null) // Perso a deja la quete
//        {
//            //SocketManager.GAME_SEND_MESSAGE(this, "Vous avez déjà cette quête !", "FF0000");
//            return;
//        }
//        if (expLevel < Integer.parseInt(questDetails.get("minLvl"))) // Niveau minimum
//        {
//            //SocketManager.GAME_SEND_MESSAGE(this, "Vous devez avoir le niveau minimum de " + questDetails.get("minLvl") + " pour faire cette quête !", "FF0000");
//            SocketManager.GAME_SEND_Im_PACKET(this, "13");
//            return;
//        }
//        if (!questDetails.get("questRequired").equals("0")) // Il faut avoir fait une quete
//        {
//            //SocketManager.GAME_SEND_MESSAGE(this, "Cette quête nécessite de faire une autre quête.", "FF0000");
//            return;
//        }
//
//        String firstStep = questDetails.get("steps").split(";")[0];
//        Map<String, String> stepDetails = World.getStep(Integer.parseInt(firstStep));
//        if (stepDetails == null) // etape inexistante
//        {
//            GameServer.addToLog("Game: Error >> Etape de quete " + firstStep + " inexistante.");
//            return;
//        }
//
//        String objectives = stepDetails.get("objectives").replaceAll(";", ",");
//        _quests.put(questId, new HashMap<String, String>());
//        _quests.get(questId).put("curStep", firstStep);
//        _quests.get(questId).put("objectivesToDo", objectives);
//
//        SocketManager.GAME_SEND_Im_PACKET(this, "054;" + questId); // New quete
//    }
//
//    public void upgradeQuest(int questId) // Monte prochaine étape ou termine quete
//    {
//
//        Map<String, String> questDetails = World.getQuest(questId);
//        if (questDetails == null) // quete inexistante
//        {
//            GameServer.addToLog("Game: Error >> Quete " + questId + " inexistante.");
//            return;
//        }
//        Map<String, String> persoQuest = _quests.get(questId);
//        if (persoQuest == null || (persoQuest != null && persoQuest.get("curStep").equals("-1"))) // Perso n'a pas la quete OU quete deja finie
//        {
//            GameServer.addToLog("Le personnage n'a pas la quete ou l'a terminee");
//            return;
//        }
//
//        String curStep = persoQuest.get("curStep");
//        String questSteps = questDetails.get("steps"); // Etapes de la quete
//        String nextSteps = "";
//        if (!persoQuest.get("objectivesToDo").isEmpty() && !persoQuest.get("objectivesToDo").equals(" ")) {
//            return;
//        }
//        if (questSteps.length() < questSteps.indexOf(curStep) + 1 + curStep.length() + 1) {
//            nextSteps = ""; // = Etape actuelle est la derniere
//        } else // Il reste des etapes
//        {
//            nextSteps = questSteps.substring(questSteps.indexOf(curStep) + curStep.length() + 1);
//        }
//
//        // Récompenses
//        Map<String, String> stepDetails = World.getStep(Integer.parseInt(curStep));
//
//        if (!stepDetails.get("gainKamas").equals("0")) // Gain de Kamas
//        {
//            _kamas += Integer.parseInt(stepDetails.get("gainKamas"));
//
//            SocketManager.GAME_SEND_STATS_PACKET(this);
//            SocketManager.GAME_SEND_Im_PACKET(this, "045;" + stepDetails.get("gainKamas")); // Tu as g² x Kamas
//        }
//
//        if (!stepDetails.get("gainExp").equals("0")) // Gain d'exp
//        {
//            long gain = Integer.parseInt(stepDetails.get("gainExp"));
//            addXp(gain);
//
//            SocketManager.GAME_SEND_STATS_PACKET(this);
//            SocketManager.GAME_SEND_Im_PACKET(this, "08;" + stepDetails.get("gainExp")); // Tu as g² x XP
//        }
//
//        if (stepDetails.get("gainItems") != null) // Gain d'Items
//        {
//            for (String item : stepDetails.get("gainItems").split(";")) // Différents items
//            {
//                if (item == null) {
//                    return;
//                }
//
//                int tID = Integer.parseInt(item.split(",")[0]);
//                int count = Integer.parseInt(item.split(",")[1]);
//
//                ItemTemplate Template = World.getObjTemplate(tID);
//                if (Template == null) {
//                    return;
//                }
//                Item newItem = Template.createNewItem(count, false);
//                if (addItem(newItem, true)) {
//                    //World.addItem(newItem, true);
//                }
//
//                SocketManager.GAME_SEND_Ow_PACKET(this);
//                SocketManager.GAME_SEND_Im_PACKET(this, "021;" + count + "~" + tID);
//            }
//        }
//			  // Fin récompenses
//
//        if (nextSteps.isEmpty()) // Quete terminee
//        {
//            GameServer.addToLog("quete terminee, curStep = -1");
//            SocketManager.GAME_SEND_Im_PACKET(this, "056;" + questId); // Fin quete
//
//            persoQuest.put("curStep", "-1");
//
//        } else // Reste des etapes : on change curStep & objectivesToDo
//        {
//            GameServer.addToLog("Etapes restantes " + nextSteps);
//            String newCurStep = nextSteps.split(";")[0];
//            persoQuest.put("curStep", newCurStep);
//
//            String objectivesToDo = World.getStep(Integer.parseInt(newCurStep)).get("objectives").replaceAll(";", ",");
//            persoQuest.put("objectivesToDo", objectivesToDo);
//
//            SocketManager.GAME_SEND_Im_PACKET(this, "055;" + questId); // Quete MaJ
//        }
//
//        _quests.put(questId, persoQuest);
//    }

    //Baskwo:Concasseur
    private int IsBrakingItem = 0;

    public void setIsBrakingItem(int GuidItemBreaking) {
        IsBrakingItem = GuidItemBreaking;
    }

    public int getIsBrakingItem() {
        return IsBrakingItem;
    }

    public void logout(){
        if(curExchange != null)
            curExchange.cancel();
        curExchange = null;
        
        if(request != null)
            request.cancel();
        
        currentWaypoint = null;
        request = null;
        curQuestion = null;
        
        //leave current fight
        if(fight != null){
            if(fighter != null)
                fight.leaveFight(fighter);
            else
                fight.getSpectators().leave(this);
            
            fight = null;
            fighter = null;
        }
        
        //remove from map
        map.removePlayer(this);
        
        //removing player from collector fights
        if(_guildMember != null){
            for(Collector collector : getGuild().getCollectors()){
                if(collector.getFight() != null && collector.getFight().getState() == Constants.FIGHT_STATE_PLACE){
                    collector.getFight().removeDefender(this);
                }
            }
        }
        
        if(_group != null)
            _group.leave(this);
        
        _group = null;
    }
    
    public void send(Object packet){
        if(isOnline()){
            account.getGameThread().send(packet);
        }
    }

    //========//
    // Mutant //
    //========//
    
    public Mutant getMutant() {
        if(currentPlayerData == null)
            return null;
        
        if(currentPlayerData instanceof Mutant)
            return (Mutant)currentPlayerData;
        
        return null;
    }

    public void setMutant(Mutant mutant) {
        if(isMutant()){
            Mutant last = (Mutant) getPlayerData();
            restrictions.unsetMutantRestrictions(last);
        }
        
        currentPlayerData = mutant;
        
        if(mutant != null)
            restrictions.setMutantRestrictions(mutant);
        
        SocketManager.GAME_SEND_STATS_PACKET(this);
        send(new SpellList(this));
        send(new AlterRestrictions(restrictions));
        map.sendToMap(new AlterSprite(this));
    }
    
    public void unsetMutant(){
        setMutant(null);
    }
    
    public boolean isMutant(){
        return currentPlayerData != null && (currentPlayerData instanceof Mutant);
    }
    
    private void checkMutant(){
        MutationItem mi = null;
        for(BuffItem item : items.getBuffItems()){
            if(!(item instanceof MutationItem)){
                continue;
            }
            
            mi = (MutationItem) item;
            
            if(mi.getMonsterTemplate() == null)
                continue;
        }
        
        if(isMutant()){
            if(mi == null || mi.getMonsterTemplate() == null)
                unsetMutant();
        }else{
            if(mi != null && mi.getMonsterTemplate() != null)
                setMutant(mi.createMutant(this));
        }
    }
    
    //=======//
    // Store //
    //=======//

    public PlayerStore getStore() {
        return store;
    }
    
    public void setSeller(){
        if(!restrictions.canBeMerchant())
            return;
        
        seller = new Seller(this);
        map.removePlayer(this);
        map.addSprite(seller);
    }
    
    public void unsetSeller(){
        if(!isSeller())
            return;
        
        map.removeSprite(seller);
        seller.endAllExchanges();
        seller = null;
    }
    
    public boolean isSeller(){
        return seller != null && restrictions.canBeMerchant();
    }
    
    public Seller getSeller(){
        return seller;
    }
    
    public PlayerData getPlayerData(){
        if(currentPlayerData == null)
            return baseCharacter;
        
        return currentPlayerData;
    }

    public BaseCharacter getBaseCharacter() {
        return baseCharacter;
    }
}
