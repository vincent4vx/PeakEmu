package org.peakemu.world.fight;

import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.fighter.PlayerFighter;
import org.peakemu.world.fight.team.ChallengeTeam;
import org.peakemu.world.fight.team.FightTeam;
import org.peakemu.objects.player.Player;
import org.peakemu.world.GameMap;
import org.peakemu.common.CryptManager;
import org.peakemu.common.Constants;
import org.peakemu.Ancestra;
import org.peakemu.common.SocketManager;
import org.peakemu.maputil.OldPathfinding;
import org.peakemu.common.Formulas;
import org.peakemu.common.IA;
import org.peakemu.game.GameServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.HashMap;

import javax.swing.Timer;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.Util;
import org.peakemu.game.in.gameaction.GameActionArg;
import org.peakemu.game.in.gameaction.Move;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.game.AddSprites;
import org.peakemu.game.out.game.FightCreated;
import org.peakemu.game.out.game.FightStartPlaces;
import org.peakemu.game.out.game.GameActionFinish;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.game.out.game.GameActionStart;
import org.peakemu.game.out.game.FightCellShown;
import org.peakemu.game.out.game.FightLeaved;
import org.peakemu.game.out.game.FightStarted;
import org.peakemu.game.out.game.FightTurnFinished;
import org.peakemu.game.out.game.FightTurnList;
import org.peakemu.game.out.game.FightTurnMiddle;
import org.peakemu.game.out.game.FightTurnReady;
import org.peakemu.game.out.game.FightTurnStarted;
import org.peakemu.game.out.game.FighterPosition;
import org.peakemu.game.out.game.JoinFightOk;
import org.peakemu.game.out.game.RemoveSprite;
import org.peakemu.game.out.game.TeamFighters;
import org.peakemu.maputil.CellChecker;
import org.peakemu.maputil.PathValidator;

import org.peakemu.objects.item.PierreAme;
import org.peakemu.objects.item.Weapon;
import org.peakemu.world.MapCell;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.SpellLevel;

import org.peakemu.world.enums.ItemType;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.FightType;
import org.peakemu.world.enums.FighterState;
import org.peakemu.world.enums.InventoryPosition;
import org.peakemu.world.fight.effect.Buff;
import org.peakemu.world.fight.effect.EffectScope;
import org.peakemu.world.fight.effect.EffectUtil;
import org.peakemu.world.fight.effect.FightEffect;
import org.peakemu.world.fight.fighter.MonsterFighter;
import org.peakemu.world.fight.object.FightObject;
import org.peakemu.world.fight.object.FightObjectList;
import org.peakemu.world.fight.object.Glyphe;
import org.peakemu.world.fight.object.Trap;
import org.peakemu.world.handler.fight.FightHandler;

abstract public class Fight {
    final int _id;
    final protected FightHandler fightHandler;
    final private List<FightTeam> teams = new ArrayList<>(2);
    Map<Integer, Fighter> deadList = new TreeMap<Integer, Fighter>();
    FightSpectators spectators = new FightSpectators(this);
    final GameMap map;
    final GameMap oldMap;
    int state = 0;
    final FightType type;
    int _curPlayer;
    long startTime = 0;
    final private long initTime = System.currentTimeMillis();
    int _curFighterUsedPA;
    int _curFighterUsedPM;
    List<Fighter> _ordreJeu = new ArrayList<Fighter>();
    Timer _turnTimer;
    private PendingAction pendingAction = null;

    final private FightObjectList fightObjectList = new FightObjectList(this);

    ArrayList<Fighter> _captureur = new ArrayList<Fighter>(8);	//CrÃ©ation d'une liste de longueur 8. Les combats contiennent un max de 8 Attaquant
    boolean isCapturable = false;
    int captWinner = -1;
    PierreAme pierrePleine;

    Map<Integer, MapCell> _raulebaque = new TreeMap<Integer, MapCell>();

    Map<String, Integer> _ips = new HashMap<String, Integer>(); //stocke les ip des persos en combat

    public Fight(FightHandler fightHandler, int id, FightType type, GameMap map) {
        this.fightHandler = fightHandler;
        this._id = id;
        this.map = new GameMap(map);
        this.oldMap = map;
        this.type = type;
        state = Constants.FIGHT_STATE_INIT;
    }

    protected void addTeam(FightTeam team) {
        team.setNumber(teams.size());
        teams.add(team);
    }

    protected void initFight() {
        oldMap.sendToMap(new FightCreated(this));
        set_state(Constants.FIGHT_STATE_PLACE);

        //add init fighters on random cells
        for (FightTeam team : teams) {
            for (Fighter fighter : new ArrayList<>(team.getFighters())) {
                MapCell cell = getRandomCell(team.getStartCells());

                if (cell == null) {
                    Peak.errorLog.addToLog(Logger.Level.ERROR, "No cell found");
                    team.removeFighter(fighter);
                    continue;
                }

                fighter.set_fightCell(cell);
                cell.addFighter(fighter);
                fighter.setFight(this);
                fighter.setTeam(team);
            }
        }

        sendToFight(new JoinFightOk(this, false));

        for (FightTeam team : teams) {
            team.sendToTeam(new FightStartPlaces(map.get_placesStr(), team.getStartCellsIndex()));
            oldMap.sendToMap(TeamFighters.allTeam(team));
        }

        sendToFight(new AddSprites(getAllFighters()));
        SocketManager.GAME_SEND_FIGHT_OBJECTS_GDS_PACKETS(this.map, this);

        if (getPlacementTime() > 0) {
            _turnTimer = new Timer(getPlacementTime(), new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    startFight();
                }
            });
            _turnTimer.start();
        }
    }

    public List<FightTeam> getTeams() {
        return teams;
    }

    boolean testIp(String ip) {
        if (Ancestra.FIGHT_IP <= 0) {
            return true;
        }

        if (!_ips.containsKey(ip)) {
            _ips.put(ip, 1);
            return true;
        }

        int num = _ips.get(ip);
        if (num >= Ancestra.FIGHT_IP) {
            return false;
        }

        _ips.put(ip, num + 1);
        return true;
    }

    public GameMap get_map() {
        return map;
    }

    MapCell getRandomCell(List<MapCell> cells) {
        Random rand = new Random();
        MapCell cell;
        if (cells.isEmpty()) {
            return null;
        }
        int limit = 0;
        do {
            int id = rand.nextInt(cells.size() - 1);
            cell = cells.get(id);
            limit++;
        } while ((cell == null || !cell.getFighters().isEmpty()) && limit < 80);
        if (limit == 80) {
            if (Ancestra.CONFIG_DEBUG) {
                GameServer.addToLog("Case non trouve dans la liste");
            }
            return null;
        }
        return cell;
    }

    ArrayList<MapCell> parsePlaces(int num) {
        return CryptManager.parseStartCell(map, num);
    }

    public int get_id() {
        return _id;
    }

    @Deprecated
    public ArrayList<Fighter> getFighters(int teams)//teams entre 0 et 7, binaire([spec][t2][t1]);
    {
        ArrayList<Fighter> fighters = new ArrayList<Fighter>();

//        if (teams - 4 >= 0) {
//            for (Entry<Integer, Player> entry : _spec.entrySet()) {
//                fighters.add(new Fighter(entry.getValue()));
//            }
//            teams -= 4;
//        }
        if (teams - 2 >= 0) {
            fighters.addAll(this.teams.get(1).getFighters());
            teams -= 2;
        }
        if (teams - 1 >= 0) {
            fighters.addAll(this.teams.get(0).getFighters());
        }
        return fighters;
    }

    public Collection<Fighter> getEnemies(FightTeam team) {
        Collection<Fighter> enemies = new ArrayList<>();

        for (FightTeam ft : teams) {
            if (ft != team) {
                enemies.addAll(ft.getFighters());
            }
        }

        return enemies;
    }

    public Collection<Fighter> getAllFighters() {
        Collection<Fighter> fighters = new ArrayList<>();

        for (FightTeam ft : teams) {
            fighters.addAll(ft.getFighters());
        }

        return fighters;
    }

    public synchronized void changePlace(Fighter fighter, MapCell cell) {
        FightTeam team = fighter.getTeam();

        if (getState() != 2 || isOccuped(cell.getID()) || !team.getStartCells().contains(cell) || fighter.isReady()) {
            return;
        }

        fighter.getCell().getFighters().clear();
        fighter.set_fightCell(cell);

        cell.addFighter(fighter);
        sendToFight(new FighterPosition(fighter));
    }

    public boolean isOccuped(int cell) {
        return map.getCell(cell).getFighters().size() > 0;
    }

    public boolean isFreeCell(MapCell cell) {
        return cell.isWalkable(true) && cell.getFighters().isEmpty();
    }

    public void verifIfAllReady() {
        for (FightTeam team : teams) {
            if (!team.isReady()) {
                return;
            }
        }

        startFight();
    }

    void startFight() {
        if (state >= Constants.FIGHT_STATE_ACTIVE) {
            return;
        }
        
        state = Constants.FIGHT_STATE_ACTIVE;
        startTime = System.currentTimeMillis();
        SocketManager.GAME_SEND_GAME_REMFLAG_PACKET_TO_MAP(oldMap, _id);

        sendToFight(new FighterPosition(getAllFighters()));
        sendToFight(new FightStarted());
        InitOrdreJeu();
        _curPlayer = -1;
        sendToFight(new FightTurnList(_ordreJeu));
        sendToFight(new FightTurnMiddle(this));
        if (_turnTimer != null) {
            _turnTimer.stop();
        }
        _turnTimer = null;
        _turnTimer = new Timer(Constants.TIME_BY_TURN, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                endTurn();
            }
        });
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToLog("Debut du combat");
        }
        for (Fighter F : getAllFighters()) {
            Player perso1 = F.getPlayer();
            if (perso1 == null) {
                continue;
            }
            if (perso1.isOnMount()) {
                F.addState(FighterState.CHEVAUCHANT);
            }
        }
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }

        startTurn();

        for (Fighter F : getAllFighters()) {
            if (F == null) {
                continue;
            }
            _raulebaque.put(F.getSpriteId(), F.getCell());
        }
    }

    void startTurn() {
        if (state >= Constants.FIGHT_STATE_FINISHED) {
            return;
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
        }

        _curPlayer++;
        if (_curPlayer >= _ordreJeu.size()) {
            _curPlayer = 0;
        }

        _ordreJeu.get(_curPlayer).setCurPA(_ordreJeu.get(_curPlayer).getPA());
        _ordreJeu.get(_curPlayer).setCurPM(_ordreJeu.get(_curPlayer).getPM());
        _curFighterUsedPA = 0;
        _curFighterUsedPM = 0;

        if (_ordreJeu.get(_curPlayer).isDead())//Si joueur mort
        {
            if (Ancestra.CONFIG_DEBUG) {
                GameServer.addToLog("(" + _curPlayer + ") Fighter ID=  " + _ordreJeu.get(_curPlayer).getSpriteId() + " est mort");
            }
            endTurn();
            return;
        }

        for (Buff buff : _ordreJeu.get(_curPlayer).getBuffs()) {
            if (buff.isValid()) {
                fightHandler.applyBuffOnStartTurn(buff);
            }
        }

        if (state == Constants.FIGHT_STATE_FINISHED) {
            return;
        }

        if (_ordreJeu.get(_curPlayer).getPDV() <= 0) {
            onFighterDie(_ordreJeu.get(_curPlayer), _ordreJeu.get(_curPlayer));
        }

        //On actualise les sorts launch
        _ordreJeu.get(_curPlayer).ActualiseLaunchedSort();
        //reset des Max des Chatis
        _ordreJeu.get(_curPlayer).get_chatiValue().clear();
        
        for(Glyphe glyphe : fightObjectList.getGlyphes(_ordreJeu.get(_curPlayer).getCell())){
            glyphe.trigger(_ordreJeu.get(_curPlayer), this, fightHandler);
        }
        
        if (_ordreJeu == null || _ordreJeu.isEmpty()) {
            return;
        }
        if (_ordreJeu.size() < _curPlayer) {
            return;
        }
        if (_ordreJeu.get(_curPlayer) == null) {
            return;
        }
        if (_ordreJeu.get(_curPlayer).isDead())//Si joueur mort
        {
            if (Ancestra.CONFIG_DEBUG) {
                GameServer.addToLog("(" + _curPlayer + ") Fighter ID=  " + _ordreJeu.get(_curPlayer).getSpriteId() + " est mort");
            }
            endTurn();
            return;
        }

        if (_ordreJeu.get(_curPlayer).getPlayer() != null) {
            SocketManager.GAME_SEND_STATS_PACKET(_ordreJeu.get(_curPlayer).getPlayer());
        }
        if (_ordreJeu.get(_curPlayer).hasBuff(Effect.SKIP_TURN))//Si il doit passer son tour
        {
            if (Ancestra.CONFIG_DEBUG) {
                GameServer.addToLog("(" + _curPlayer + ") Fighter ID= " + _ordreJeu.get(_curPlayer).getSpriteId() + " passe son tour");
            }
            endTurn();
            return;
        }

        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToLog("(" + _curPlayer + ")Debut du tour de Fighter ID= " + _ordreJeu.get(_curPlayer).getSpriteId());
        }

        sendToFight(new FightTurnStarted(_ordreJeu.get(_curPlayer), Constants.TIME_BY_TURN)); //TODO config turn time
        _turnTimer.restart();
        try {
            Thread.sleep(650);
        } catch (InterruptedException e1) {
        }

        _ordreJeu.get(_curPlayer).setCanPlay(true);

        if (_ordreJeu.get(_curPlayer).getPlayer() == null || _ordreJeu.get(_curPlayer).isDouble() || _ordreJeu.get(_curPlayer).getCollector() != null || (((Fighter) this._ordreJeu.get(this._curPlayer)).getPrism() != null))//Si ce n'est pas un joueur
        {
            new IA.IAThread(_ordreJeu.get(_curPlayer), this);
        }
    }

    public int getCurFighterUsedPA() {
        return _curFighterUsedPA;
    }

    public int getCurFighterUsedPM() {
        return _curFighterUsedPM;
    }

    public void endTurn() {
        try {
            if (_curPlayer == -1) {
                return;
            }

            if (_ordreJeu == null || _ordreJeu.isEmpty() || _ordreJeu.get(_curPlayer) == null) {
                return;
            }

            if (state >= Constants.FIGHT_STATE_FINISHED) {
                return;
            }

            if (_ordreJeu.get(_curPlayer).isDead()) {
                startTurn();
                return;
            }

            _turnTimer.stop();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
            }

            if (pendingAction != null) {
                finishPendingAction();
            }

            sendToFight(new FightTurnFinished(getCurFighter()));

            _ordreJeu.get(_curPlayer).setCanPlay(false);

            for (Buff buff : _ordreJeu.get(_curPlayer).getBuffs()) {
                fightHandler.applyBuffOnEndTurn(buff);
            }

            if (state != Constants.FIGHT_STATE_ACTIVE) {
                return;
            }

            fightObjectList.refreshGlyphes(_ordreJeu.get(_curPlayer));
            
            if (_ordreJeu.get(_curPlayer).getPDV() <= 0) {
                onFighterDie(_ordreJeu.get(_curPlayer), _ordreJeu.get(_curPlayer));
            }
            if (Constants.IGNORE_VERSION) //reset des valeurs
            {
                _curFighterUsedPA = 0;
            }
            _curFighterUsedPM = 0;
            _ordreJeu.get(_curPlayer).setCurPA(_ordreJeu.get(_curPlayer).getTotalStats().getEffect(Effect.ADD_PA));
            _ordreJeu.get(_curPlayer).setCurPM(_ordreJeu.get(_curPlayer).getTotalStats().getEffect(Effect.ADD_PM));

            for (Buff buff : _ordreJeu.get(_curPlayer).refreshBuffs()) {
                fightHandler.onBuffEnd(buff);
            }

            if (_ordreJeu.get(_curPlayer).getPlayer() != null) {
                if (_ordreJeu.get(_curPlayer).getPlayer().isOnline()) {
                    SocketManager.GAME_SEND_STATS_PACKET(_ordreJeu.get(_curPlayer).getPlayer());
                }
            }

            sendToFight(new FightTurnMiddle(this));
            sendToFight(new FightTurnReady(getCurFighter()));
            if (Ancestra.CONFIG_DEBUG) {
                GameServer.addToLog("(" + _curPlayer + ")Fin du tour de Fighter ID= " + _ordreJeu.get(_curPlayer).getSpriteId());
            }
            startTurn();
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            endTurn();
        }
    }

    void InitOrdreJeu() {
        //TODO: real sort order
        for (FightTeam team : teams) {
            for (Fighter fighter : team.getFighters()) {
                _ordreJeu.add(fighter);
            }
        }

        Collections.sort(_ordreJeu, new Comparator<Fighter>() {
            @Override
            public int compare(Fighter t, Fighter t1) {
                return t1.getInitiative() - t.getInitiative();
            }
        });
    }

    public boolean joinTeam(Player player, FightTeam team) {
        if(state > Constants.FIGHT_STATE_PLACE)
            return false;
        
        if (!team.canAddFighter(player)) {
            return false;
        }

        PlayerFighter fighter = new PlayerFighter(player);

        MapCell cell = getRandomCell(team.getStartCells());

        if (cell == null) {
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "No free cells found !");
            return false;
        }

        player.stopAllRegen();
        SocketManager.GAME_SEND_ILF_PACKET(player, 0);
        player.getMap().removePlayer(player);

        fighter.setFight(this);
        fighter.set_fightCell(cell);
        cell.addFighter(fighter);
        fighter.setTeam(team);
        team.addFighter(fighter);
        player.setFighter(fighter);

        //sending packets
        player.send(new JoinFightOk(this, false));
        SocketManager.GAME_SEND_FIGHT_PLACES_PACKET(player.getAccount().getGameThread(), map.get_placesStr(), team.getStartCellsIndex());
        oldMap.sendToMap(TeamFighters.addFighter(team, fighter));
        sendToFight(new AddSprites(fighter));
        player.send(new AddSprites(getAllFighters()));
        //IOs walkables en combat
        SocketManager.GAME_SEND_FIGHT_OBJECTS_GDS_PACKETS(this.map, this);
        
        return true;
    }

    public void addFighterInTeam(Fighter fighter, FightTeam team) {
        fighter.setFight(this);
        team.addFighter(fighter);
    }

    void set_state(int _state) {
        this.state = _state;
    }

    public int getState() {
        return state;
    }

    public FightType get_type() {
        return type;
    }

    public List<Fighter> get_ordreJeu() {
        return _ordreJeu;
    }

    public Map<Integer, MapCell> get_raulebaque() {
        return _raulebaque;
    }

    private void setPendingAction(int actionId, int pa, int pm) {
        Peak.worldLog.addToLog(Logger.Level.DEBUG, "Add pending action %d for fighter %d", actionId, _ordreJeu.get(_curPlayer).getSpriteId());
        pendingAction = new PendingAction(actionId, pa, pm);

        Fighter fighter = _ordreJeu.get(_curPlayer);

        if (fighter.getPlayer() != null) {
            sendToFight(new GameActionStart(fighter.getSpriteId()));
        }
    }

    private void finishPendingAction() {
        if (pendingAction == null || _curPlayer == -1) {
            return;
        }

        Fighter fighter = _ordreJeu.get(_curPlayer);

        if (fighter == null) {
            return;
        }

        if (pendingAction.getPa() > 0) {
            sendToFight(GameActionResponse.loosePAOnAction(fighter, pendingAction.getPa()));
            fighter.removeCurPA(pendingAction.getPa());
        }

        if (pendingAction.getPm() > 0) {
            sendToFight(GameActionResponse.loosePMOnAction(fighter, pendingAction.getPm()));
            fighter.removeCurPM(pendingAction.getPm());
        }

        if (fighter.getPlayer() != null) {
            sendToFight(new GameActionFinish(pendingAction.getActionId(), fighter.getSpriteId()));
        }
        
        PendingAction action = pendingAction;
        pendingAction = null;

        switch (action.getActionId()) {
            case PendingAction.MOVE: {
                onPlaceChange(fighter, fighter.getCell());
            }
            break;
        }
    }

    public boolean fighterDeplace(Fighter f, List<MapCell> path) {
        if (_ordreJeu.size() <= _curPlayer) {
            return false;
        }

        if (_ordreJeu.get(_curPlayer) == null) {
            return false;
        }
        
        if(!f.canPlay())
            return false;

        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToLog("(" + _curPlayer + ")Tentative de deplacement de Fighter ID= " + f.getSpriteId() + " a partir de la case " + f.getCell().getID());
        }

        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToLog("Path: " + path);
        }

        if (pendingAction != null || _ordreJeu.get(_curPlayer).getSpriteId() != f.getSpriteId() || state != Constants.FIGHT_STATE_ACTIVE) {
            if (pendingAction != null) {
                if (Ancestra.CONFIG_DEBUG) {
                    GameServer.addToLog("Echec du deplacement: il y deja une action en cours");
                }
            }
            if (_ordreJeu.get(_curPlayer).getSpriteId() != f.getSpriteId()) {
                if (Ancestra.CONFIG_DEBUG) {
                    GameServer.addToLog("Echec du deplacement: ce n'est pas a ce joueur de jouer");
                }
            }
            if (state != Constants.FIGHT_STATE_ACTIVE) {
                if (Ancestra.CONFIG_DEBUG) {
                    GameServer.addToLog("Echec du deplacement: le combat n'est pas en cours");
                }
            }
            return false;
        }

        ArrayList<Fighter> tacle = OldPathfinding.getEnemyFighterArround(f.getCell().getID(), map, this);
        if (tacle != null && !f.hasState(FighterState.ENRACINE))//Tentative de Tacle : Si stabilisation alors pas de tacle possible
        {
            for (Fighter T : tacle)//Les stabilisés ne taclent pas
            {
                if (T.hasState(FighterState.ENRACINE)) {
                    tacle.remove(T);
                }
            }
            if (!tacle.isEmpty())//Si tous les tacleur ne sont pas stabilisés
            {
                if (Ancestra.CONFIG_DEBUG) {
                    GameServer.addToLog("Le personnage est a cote de (" + tacle.size() + ") ennemi(s)");// ("+tacle.getPacketsName()+","+tacle.getCell().getSpriteId()+") => Tentative de tacle:");
                }
                int chance = Formulas.getTacleChance(f, tacle);
                int rand = Formulas.getRandomValue(0, 99);
                if (rand > chance) {
                    sendToFight(GameActionResponse.fighterTackled(f));
                    int pertePA = _ordreJeu.get(_curPlayer).getCurPA() * chance / 100;

                    if (pertePA < 0) {
                        pertePA = -pertePA;
                    }
                    if (_ordreJeu.get(_curPlayer).getCurPM() < 0) {
                        _ordreJeu.get(_curPlayer).setCurPM(0); // -_curFighterPM :: 0 c'est plus simple :)
                    }
                    sendToFight(GameActionResponse.loosePAOnAction(f, pertePA));
                    sendToFight(GameActionResponse.loosePMOnAction(f, f.getCurPM()));

                    _ordreJeu.get(_curPlayer).setCurPM(0);
                    _ordreJeu.get(_curPlayer).setCurPA(_ordreJeu.get(_curPlayer).getCurPA() - pertePA);
                    if (Ancestra.CONFIG_DEBUG) {
                        GameServer.addToLog("Echec du deplacement: fighter tacle");
                    }
                    return false;
                }
            }
        }
        
        path = PathValidator.validatePath(path, CellChecker.forFight(this, f));
        int nStep = path.size() - 1; //path contains the start cell
        
        if(nStep < 1){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Invalid path");
            return false;
        }
        
        if(!path.get(0).equals(f.getCell())){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Invalid start cell");
            return false;
        }
        
        if(nStep > f.getCurPM()){ //path always contains the start cell
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Too away cell (%d)", nStep);
            return false;
        }

        GameActionResponse response;
        
        try{
            response = GameActionResponse.move(f, path);
        }catch(Exception e){
            Peak.errorLog.addToLog(e);
            return false;
        }

        _curFighterUsedPM += nStep;
        setPendingAction(PendingAction.MOVE, 0, nStep);

        //Si le joueur n'est pas invisible
        if (!_ordreJeu.get(_curPlayer).isHidden()) {
            sendToFight(response);
        } else//Si le joueur est planqué x)
        {
            if (_ordreJeu.get(_curPlayer).getPlayer() != null) {
                _ordreJeu.get(_curPlayer).getPlayer().send(response);
            }
        }

        //Si porté
//        Fighter po = _ordreJeu.get(_curPlayer).get_holdedBy();
//        if (po != null
//            && _ordreJeu.get(_curPlayer).isState(Constants.ETAT_PORTE)
//            && po.isState(Constants.ETAT_PORTEUR)) {
//            System.out.println("Porteur: " + po.getPacketsName());
//            System.out.println("Cell du Porteur " + po.getCell().getID());
//
//            //si le joueur va bouger
//            if (nextCellID != po.getCell().getID()) {
//                //on retire les états
//                po.setState(Constants.ETAT_PORTEUR, 0);
//                _ordreJeu.get(_curPlayer).setState(Constants.ETAT_PORTE, 0);
//                //on retire dé lie les 2 fighters
//                po.set_isHolding(null);
//                _ordreJeu.get(_curPlayer).set_holdedBy(null);
//                //La nouvelle case sera définie plus tard dans le code
//                //On envoie les packets
//                SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 950, po.getSpriteId() + "", po.getSpriteId() + "," + Constants.ETAT_PORTEUR + ",0");
//                SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 950, _ordreJeu.get(_curPlayer).getSpriteId() + "", _ordreJeu.get(_curPlayer).getSpriteId() + "," + Constants.ETAT_PORTE + ",0");
//            }
//        }

        _ordreJeu.get(_curPlayer).getCell().removeFighter(f);
        _ordreJeu.get(_curPlayer).set_fightCell(path.get(path.size() - 1));
        _ordreJeu.get(_curPlayer).getCell().addFighter(f);
        
//        if (po != null) {
//            po.getCell().addFighter(po);// même erreur que tantôt, bug ou plus de fighter sur la case
//        }

        //Si porteur
//        po = _ordreJeu.get(_curPlayer).get_isHolding();
//        if (po != null
//            && _ordreJeu.get(_curPlayer).isState(Constants.ETAT_PORTEUR)
//            && po.isState(Constants.ETAT_PORTE)) {
//            //on déplace le porté sur la case
//            po.set_fightCell(_ordreJeu.get(_curPlayer).getCell());
//            if (Ancestra.CONFIG_DEBUG) {
//                GameServer.addToLog(po.getPacketsName() + " se deplace vers la case " + nextCellID);
//            }
//        }

        if (f.getPlayer() == null) {
            try {
                Thread.sleep(900 + 100 * nStep);//Estimation de la durée du déplacement
            } catch (InterruptedException e) {
            }

            try {
                onFighterEndMove(f);
            } catch (Exception e) {
                Peak.errorLog.addToLog(e);
            }
            return true;
        }
        
        //set a GA for PlayerFighter to receive the GK packet
        GameActionArg GA = new GameActionArg(f.getPlayer().getAccount().getGameThread(), Move.ACTION_ID, "");
        GA.attach("REAL_PATH", path);
        f.getPlayer().getAccount().getGameThread().getGameActionHandler().setCurrentAction(GA);
        return true;
    }

    public void onFighterEndMove(Fighter fighter) {
        if (pendingAction == null || _ordreJeu.get(_curPlayer) != fighter || state != Constants.FIGHT_STATE_ACTIVE) {
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Invalid fighter or state");
            return;
        }

        Peak.worldLog.addToLog(Logger.Level.DEBUG, "End fight move for %d", fighter.getSpriteId());

        finishPendingAction();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }

    public void onGK(Player perso) {
        onFighterEndMove(perso.getFighter());
    }

    public void playerPass(Player _perso) {
        Fighter f = getFighterByPerso(_perso);
        if (f == null) {
            return;
        }
        if (!f.canPlay()) {
            return;
        }
        endTurn();
    }
    
    private boolean checkStates(SpellLevel spell, Fighter fighter){
        if(!fighter.hasAllStats(spell.getRequiredStates()))
            return false;
        
        for(FighterState forbidden : spell.getForbiddenStates()){
            if(fighter.hasState(forbidden))
                return false;
        }
        
        return true;
    }

    public int tryCastSpell(Fighter fighter, SpellLevel Spell, int caseID) {
        if (pendingAction != null) {
            return 10;
        }

        if (Spell == null) {
            return 10;
        }

        //if(fighter == null || Spell == null) return 10;
        MapCell Cell = map.getCell(caseID);

        try {
            if (canCastSpell(fighter, Spell, Cell, -1)) {
                if (fighter.getPlayer() != null) {
                    SocketManager.GAME_SEND_STATS_PACKET(fighter.getPlayer());
                }

                if (Ancestra.CONFIG_DEBUG) {
                    GameServer.addToLog(fighter.getPacketsName() + " tentative de lancer le sort " + Spell.getSpellID() + " sur la case " + caseID);
                }

                setPendingAction(PendingAction.CAST, Spell.getPACost(), 0);

                _curFighterUsedPA += Spell.getPACost();

                boolean isEc = Spell.getTauxEC() != 0 && Formulas.getRandomValue(1, Spell.getTauxEC()) == Spell.getTauxEC();
                if (isEc) {
                    if (Ancestra.CONFIG_DEBUG) {
                        GameServer.addToLog(fighter.getPacketsName() + " Echec critique sur le sort " + Spell.getSpellID());
                    }

                    sendToFight(GameActionResponse.criticalMiss(fighter, Spell.getSpellID()));
                } else {
                    boolean isCC = fighter.testIfCC(Spell.getTauxCC());
                    sendToFight(GameActionResponse.launchSpell(fighter, Spell, Cell));
                    
                    if (isCC) {
                        sendToFight(GameActionResponse.criticalHit(fighter, Spell, Cell));
                    }
                    
                    Collection<SpellEffect> effects = isCC ? Spell.getCriticalSpellEffects() : Spell.getSpellEffects();
                    
                    //Si le joueur est invi, on montre la case
                    if (fighter.isHidden()) {
                        sendToFight(new FightCellShown(fighter, fighter.getCell()));
                    }

                    for (SpellEffect nse : EffectUtil.selectConditionalEffects(effects)) {
                        fightHandler.applyEffectToFight(this, nse, Cell, fighter);
                    }
                }

                //Refresh des Stats
                //refreshCurPlayerInfos();
                if (!isEc) {
                    fighter.addLaunchedSort(Cell.getFirstFighter(), Spell);
                }
                //fighter.addLaunchedSort(Cell.getFirstFighter(),Spell);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                };
                if ((isEc && Spell.isEcEndTurn())) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    };
                    if (fighter.getMonster() != null || fighter.isInvocation())//Mob, Invoque
                    {
                        return 5;
                    } else {
                        endTurn();
                        return 5;
                    }
                }
            } else if (fighter.getMonster() != null || fighter.isInvocation()) {
                return 10;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
        } finally {
            if (pendingAction != null) {
                finishPendingAction();
            }
        }
        return 0;
    }

    public boolean canCastSpell(Fighter fighter, SpellLevel spell, MapCell cell, int launchCase) {
        int ValidlaunchCase;
        if (launchCase <= -1) {
            ValidlaunchCase = fighter.getCell().getID();
        } else {
            ValidlaunchCase = launchCase;
        }

        Fighter f = _ordreJeu.get(_curPlayer);
        Player perso = fighter.getPlayer();
        //Si le sort n'est pas existant
        if (spell == null) {
            if (Ancestra.CONFIG_DEBUG) {
                GameServer.addToLog("(" + _curPlayer + ") Sort non existant");
            }
            if (perso != null) {
                SocketManager.GAME_SEND_Im_PACKET(perso, "1169");
            }
            return false;
        }
        //Si ce n'est pas au joueur de jouer
        if (f == null || f.getSpriteId() != fighter.getSpriteId()) {
            if (Ancestra.CONFIG_DEBUG) {
                GameServer.addToLog("Ce n'est pas au joueur. Doit jouer :(" + f.getSpriteId() + "). Fautif :(" + fighter.getSpriteId() + ")");
            }
            if (perso != null) {
                SocketManager.GAME_SEND_Im_PACKET(perso, "1175");
            }
            return false;
        }
        
        if(!checkStates(spell, fighter)){
            if(perso != null)
                perso.send(new InfoMessage(InfoMessage.Type.INFO).addMessage(116));
            
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Invalid fighter state");
            return false;
        }
        
        //Si le joueur n'a pas assez de PA
        if (_ordreJeu.get(_curPlayer).getCurPA() < spell.getPACost()) {
            if (Ancestra.CONFIG_DEBUG) {
                GameServer.addToLog("(" + _curPlayer + ") Le joueur n'a pas assez de PA (" + _ordreJeu.get(_curPlayer).getCurPA() + "/" + spell.getPACost() + ")");
            }
            if (perso != null) {
                SocketManager.GAME_SEND_Im_PACKET(perso, "1170;" + _ordreJeu.get(_curPlayer).getCurPA() + "~" + spell.getPACost());
            }
            return false;
        }
        //Si la cellule visée n'existe pas
        if (cell == null) {
            if (Ancestra.CONFIG_DEBUG) {
                GameServer.addToLog("(" + _curPlayer + ") La cellule visee n'existe pas");
            }
            if (perso != null) {
                SocketManager.GAME_SEND_Im_PACKET(perso, "1172");
            }
            return false;
        }
        //Si la cellule visée n'est pas alignée avec le joueur alors que le sort le demande
        if (spell.isLineLaunch() && !OldPathfinding.casesAreInSameLine(map, ValidlaunchCase, cell.getID(), 'z')) {
            if (Ancestra.CONFIG_DEBUG) {
                GameServer.addToLog("(" + _curPlayer + ") Le sort demande un lancer en ligne, or la case n'est pas alignee avec le joueur");
            }
            if (perso != null) {
                SocketManager.GAME_SEND_Im_PACKET(perso, "1173");
            }
            return false;
        }
        //Si le sort demande une ligne de vue et que la case demandée n'en fait pas partie
        if (spell.isLineOfSight() && !OldPathfinding.checkLoS(map, ValidlaunchCase, cell.getID(), fighter)) {
            if (Ancestra.CONFIG_DEBUG) {
                GameServer.addToLog("(" + _curPlayer + ") Le sort demande une ligne de vue, mais la case visee n'est pas visible pour le joueur");
            }
            if (perso != null) {
                SocketManager.GAME_SEND_Im_PACKET(perso, "1174");
            }
            return false;
        }

        int dist = OldPathfinding.getDistanceBetween(map, ValidlaunchCase, cell.getID());
        int MaxPO = spell.getMaxPO();
        if (spell.canBoostRange()) {
            MaxPO += fighter.getTotalStats().getEffect(Effect.ADD_PO);
        }
        //Vérification Portée mini / maxi
        if (dist < spell.getMinPO() || dist > MaxPO) {
            if (Ancestra.CONFIG_DEBUG) {
                GameServer.addToLog("(" + _curPlayer + ") La case est trop proche ou trop eloignee Min: " + spell.getMinPO() + " Max: " + spell.getMaxPO() + " Dist: " + dist);
            }
            if (perso != null) {
                SocketManager.GAME_SEND_Im_PACKET(perso, "1171;" + spell.getMinPO() + "~" + spell.getMaxPO() + "~" + dist);
                sendToFight(GameActionResponse.noGameAction());
            }
            return false;
        }
        //vérification cooldown
        if (!LaunchedSort.coolDownGood(fighter, spell.getSpellID())) {
            return false;
        }
        //vérification nombre de lancer par tour
        int nbLancer = spell.getMaxLaunchbyTurn();
        if (nbLancer - LaunchedSort.getNbLaunch(fighter, spell.getSpellID()) <= 0 && nbLancer > 0) {
            return false;
        }
        //vérification nombre de lancer par cible
        Fighter target = cell.getFirstFighter();
        int nbLancerT = spell.getMaxLaunchbyByTarget();
        if (nbLancerT - LaunchedSort.getNbLaunchTarget(fighter, target, spell.getSpellID()) <= 0 && nbLancerT > 0) {
            return false;
        }
        return true;
    }

    /**
     * Generate and set rewards
     *
     * @param winners
     * @param loosers
     * @return
     */
    abstract protected Collection<FightRewards> getAllRewards(Collection<Fighter> winners, Collection<Fighter> loosers);

    /**
     * Parse one fighter rewards, for GE packet (Do not give rewards here !!!)
     *
     * @param rewards
     * @return
     */
    abstract protected String parseRewards(FightRewards rewards);

    public String getGE(Collection<FightRewards> rewards) {
        long time = System.currentTimeMillis() - startTime;

        StringBuilder packet = new StringBuilder();
        packet.append("GE").append(time).append("|").append(_id).append("|").append(getGESheetType());

        for (FightRewards r : rewards) {
            packet.append('|').append(parseRewards(r));
        }

        return packet.toString();
    }
    
    abstract protected int getGESheetType();

    private FightTeam getWinTeam() {
        for (FightTeam team : teams) {
            if (!team.isAllDead()) {
                return team;
            }
        }

        return null;
    }

    public void sendToFight(Object packet) {
        for (FightTeam team : teams) {
            team.sendToTeam(packet);
        }

        spectators.sendToSpectators(packet);
    }
    
    public void sendToFight(Object packet, OutputFilter filter){
        String sPacket = packet.toString();
        
        for(Fighter fighter : getAllFighters()){
            if(!filter.canSendToFighter(fighter))
                continue;
            
            if(fighter.getPlayer() == null)
                continue;
            
            fighter.getPlayer().send(sPacket);
        }
        
        if(filter.canSendToSpectator()){
            spectators.sendToSpectators(sPacket);
        }
    }

    public void verifIfTeamAllDead() {
        if (state >= Constants.FIGHT_STATE_FINISHED) {
            return;
        }

        if (!verifyStillInFight()) {
            state = Constants.FIGHT_STATE_FINISHED;

            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Fight %d terminated", _id);

            _turnTimer.stop();
            _curPlayer = -1;
            _turnTimer = null;

            FightTeam winTeam = getWinTeam();
            Collection<Fighter> winners = winTeam.getFighters();
            Collection<Fighter> loosers = getEnemies(winTeam);

            Collection<FightRewards> rewards = getAllRewards(winners, loosers);
            fightHandler.giveRewards(rewards);
            
            oldMap.removeFight(_id);
            _ordreJeu.clear();

            try {
                applyDefaultEndFightActionsToLoosers(loosers);
                applyDefaultEndFightActionsToWinners(winners);
                applyDefaultEndFightActionsToAll(getAllFighters());
            } catch (Exception e) {
                Peak.errorLog.addToLog(e);
            }

            fightHandler.applyEndFightActions(this, winners);
            fightHandler.kickAllFighters(getAllFighters());
            spectators.unsetFight();

            sendToFight(getGE(rewards));

            clearFight();
        }
    }

    abstract protected void applyDefaultEndFightActionsToLoosers(Collection<Fighter> loosers);

    abstract protected void applyDefaultEndFightActionsToWinners(Collection<Fighter> winners);

    abstract protected void applyDefaultEndFightActionsToAll(Collection<Fighter> fighters);

    protected void onFighterLeaved(Fighter fighter) {
        Peak.worldLog.addToLog(Logger.Level.DEBUG, "Applying leave actions on %s", fighter.getPacketsName());
        fighter.setDead(true);
        fighter.getTeam().removeFighter(fighter);
        applyDefaultEndFightActionsToLoosers(Collections.singletonList(fighter));
        applyDefaultEndFightActionsToAll(Collections.singletonList(fighter));
        fightHandler.kickAllFighters(Collections.singletonList(fighter));
    }
    
    private void monsterEmoteOnDie(Fighter dead){
        final Integer[] happyEmotes = new Integer[]{1,4};
        final Integer[] angryEmotes = new Integer[]{2,3,5,7,8,9};
        
        for(Fighter fighter : getAllFighters()){
            if(fighter.isDead())
                continue;
            
            if(!(fighter instanceof MonsterFighter))
                continue;
            
            if(fighter.isHidden())
                continue;
            
            if(Util.randBool(fightHandler.getConfig().getMonsterEmoteChance())){
                Integer[] emotes = dead.getTeam().equals(fighter.getTeam()) ?
                    angryEmotes : happyEmotes;
                
                SocketManager.GAME_SEND_EMOTICONE_TO_FIGHT(this, 7, fighter.getSpriteId(), Util.rand(emotes));
            }
        }
    }

    public void onFighterDie(Fighter target, Fighter caster) {
        if (target.isReallyDead()) //already concidered as dead
        {
            return;
        }

        target.setIsDead(true);
        deadList.put(target.getSpriteId(), target);//on ajoute le joueur ï¿½ la liste des cadavres ;)
        sendToFight(GameActionResponse.fighterDie(caster, target));
        target.getCell().getFighters().clear();// Supprime tout causait bug si portï¿½/porteur

        if (target.hasState(FighterState.PORTEUR)) {
            Fighter f = target.get_isHolding();

            f.set_fightCell(f.getCell());
            f.getCell().addFighter(f);

            f.removeState(FighterState.PORTE);
            target.removeState(FighterState.PORTEUR);

            f.set_holdedBy(null);
            target.set_isHolding(null);
        }

        for (Fighter fighter : new ArrayList<>(target.getTeam().getFighters())) { //copy fighters
            if (fighter.getInvocator() == null) {
                continue;
            }
            if (fighter.getPDV() == 0) {
                continue;
            }
            if (fighter.isDead()) {
                continue;
            }
            if (fighter.getInvocator().getSpriteId() == target.getSpriteId())//si il a ï¿½tï¿½ invoquï¿½ par le joueur mort
            {
                try {
                    onFighterDie(fighter, caster);

                    int index = _ordreJeu.indexOf(fighter);
                    if (index != -1) {
                        _ordreJeu.remove(index);
                    }

                    target.getTeam().removeFighter(fighter);
                    sendToFight(GameActionResponse.fakePacket(target, new FightTurnList(_ordreJeu)));
                } catch (Exception e) {
                    Peak.errorLog.addToLog(e);
                }
            }
        }
        
        monsterEmoteOnDie(target);

        if (target.getMonster() != null) {
            //Si c'est une invocation, on la retire de la liste
            try {
                boolean isStatic = false;
                for (int id : Constants.STATIC_INVOCATIONS) {
                    if (id == target.getMonster().getTemplate().getID()) {
                        isStatic = true;
                    }
                }
                if (target.isInvocation() && !isStatic) {
                    //Il ne peut plus jouer, et est mort on revient au joueur prï¿½cedent pour que le startTurn passe au suivant
                    if (!target.canPlay() && _ordreJeu.get(_curPlayer).getSpriteId() == target.getSpriteId()) {
                        _curPlayer--;
                    }
                    //Il peut jouer, et est mort alors on passe son tour pour que l'autre joue, puis on le supprime de l'index sans problï¿½mes
                    if (target.canPlay() && _ordreJeu.get(_curPlayer).getSpriteId() == target.getSpriteId()) {
                        endTurn();
                    }

                    //On ne peut pas supprimer l'index tant que le tour du prochain joueur n'est pas lancï¿½
                    int index = _ordreJeu.indexOf(target);

                    //Si le joueur courant a un index plus ï¿½levï¿½, on le diminue pour ï¿½viter le outOfBound
                    if (_curPlayer > index) {
                        _curPlayer--;
                    }

                    if (index != -1) {
                        _ordreJeu.remove(index);
                    }

                    for (FightTeam team : teams) {
                        team.removeFighter(target);
                    }

                    sendToFight(GameActionResponse.fakePacket(target, new FightTurnList(_ordreJeu)));
                }
            } catch (Exception e) {
                Peak.errorLog.addToLog(e);
            }
        }

        fightObjectList.removeObjectsOnFighterDeath(target);
        
        verifIfTeamAllDead();
        
        if(state == Constants.FIGHT_STATE_ACTIVE){
            if (target.canPlay() && _ordreJeu.get(_curPlayer).getSpriteId() == target.getSpriteId()) {
                endTurn();
            }
            debuffOnDie(target);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }

    private void debuffOnDie(Fighter fighter) {
        for (Fighter target : getAllFighters()) {
            if (target.equals(fighter)) {
                continue;
            }

            if (target.isDead()) {
                continue;
            }

            for (Buff buff : target.removeBuffsByCaster(fighter)) {
                fightHandler.onBuffEnd(buff);
                //TODO: hide buff in fight
            }
        }
    }

    public void tryCaC(Player perso, int cellID) {
        Fighter caster = getFighterByPerso(perso);

        if (caster == null) {
            return;
        }

        if (_ordreJeu.get(_curPlayer).getSpriteId() != caster.getSpriteId())//Si ce n'est pas a lui de jouer
        {
            return;
        }

        if (perso.getItemByPos(InventoryPosition.ARME) == null)//S'il n'a pas de CaC
        {
            tryCastSpell(caster, fightHandler.getDefaultMeleeSpell(), cellID);
        } else {
            try{
                Weapon arme = (Weapon) perso.getItems().getItemByPos(InventoryPosition.ARME);
                int dist = OldPathfinding.getDistanceBetween(map, caster.getCell().getID(), cellID);
                int MaxPO = arme.getTemplate().getPOmax();
                int MinPO = arme.getTemplate().getPOmin();
                if (dist < MinPO || dist > MaxPO) {
                    SocketManager.GAME_SEND_Im_PACKET(perso, "1171;" + MinPO + "~" + MaxPO + "~" + dist);
                    sendToFight(GameActionResponse.noGameAction());
                    return;
                }
                
                //Pierre d'âmes = EC
                if (arme.getTemplate().getType() == ItemType.PIERRE_AME) {
                    sendToFight(GameActionResponse.weaponCriticalMiss(caster));
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                    }
                    endTurn();
                }

                int PACost = arme.getTemplate().getPACost();

                if (_ordreJeu.get(_curPlayer).getCurPA() < PACost)//S'il n'a pas assez de PA
                {
                    return;
                }
                
                setPendingAction(PendingAction.CAST, PACost, 0);
                boolean isEc = arme.getTemplate().getTauxEC() != 0 && Formulas.getRandomValue(1, arme.getTemplate().getTauxEC()) == arme.getTemplate().getTauxEC();
                if (isEc) {
                    sendToFight(GameActionResponse.weaponCriticalMiss(caster));
                    endTurn();
                } else {
                    sendToFight(GameActionResponse.useWeapon(caster, map.getCell(cellID)));
                    boolean isCC = caster.testIfCC(arme.getTemplate().getTauxCC());
                    if (isCC) {
                        sendToFight(GameActionResponse.weaponCriticalHit(caster));
                    }
    //
    //                //Si le joueur est invisible
    //                if (caster.isHide()) {
    //                    caster.unHide(PACost);
    //                }

                    Collection<SpellEffect> effets = isCC ? arme.getSpellEffects() : arme.getCriticalEffects();

                    for (SpellEffect SE : effets) {
                        if (state != Constants.FIGHT_STATE_ACTIVE) {
                            break;
                        }
                        fightHandler.applyEffectToFight(this, SE, map.getCell(cellID), caster);
                    }
                }
            }finally{
                finishPendingAction();
            }
        }
    }

    public Fighter getFighterByPerso(Player perso) {
        for (FightTeam team : teams) {
            for (Fighter fighter : team.getFighters()) {
                if (fighter.getPlayer() == perso) {
                    return fighter;
                }
            }
        }
        return null;
    }

    public Fighter getFighterById(int id) {
        for (FightTeam team : teams) {
            for (Fighter fighter : team.getFighters()) {
                if (fighter.getSpriteId() == id) {
                    return fighter;
                }
            }
        }
        return null;
    }

    public Fighter getCurFighter() {
        if(_ordreJeu == null || _ordreJeu.isEmpty() || _curPlayer == -1)
            return null;
        
        return _ordreJeu.get(_curPlayer);
    }

    public void refreshCurPlayerInfos() {
        _ordreJeu.get(_curPlayer).setCurPA(_ordreJeu.get(_curPlayer).getTotalStats().getEffect(Effect.ADD_PA) - _curFighterUsedPA);
        _ordreJeu.get(_curPlayer).setCurPM(_ordreJeu.get(_curPlayer).getTotalStats().getEffect(Effect.ADD_PM) - _curFighterUsedPM);
    }

    public void kickFighter(Fighter performer, Fighter target) {
        if (performer == null || performer.getTeam() != target.getTeam()) //not same team
        {
            return;
        }

        if (!(performer.getTeam() instanceof ChallengeTeam)) //shouldn't arrived
        {
            return;
        }

        if (((ChallengeTeam) (performer.getTeam())).getInit() != performer) //performer is not team master
        {
            return;
        }

        Peak.worldLog.addToLog(Logger.Level.DEBUG, "Fighter %s kick %s", performer.getPacketsName(), target.getPacketsName());

        target.getTeam().removeFighter(target);

        sendToFight(new RemoveSprite(target));
        Player player = target.getPlayer();

        if (player == null) //shouldn't arrived
        {
            return;
        }

        player.setFighter(null);
        player.setFight(null);
        player.set_duelID(-1);
        player.setAway(false);
        player.setSitted(false);

        SocketManager.GAME_SEND_GV_PACKET(player);
    }

    public void leaveFight(Fighter fighter) {
        Peak.worldLog.addToLog(Logger.Level.DEBUG, "Fighter %s leave fight", fighter.getPacketsName());

        switch (state) {
            case Constants.FIGHT_STATE_PLACE:
                leaveFightDuringPlacement(fighter);
                break;
            case Constants.FIGHT_STATE_ACTIVE:
                leaveFightOnActive(fighter);
                break;
            default:
                Peak.worldLog.addToLog(Logger.Level.ERROR, "Fight state %d can't handle leave", state);
        }
    }

    protected void leaveFightDuringPlacement(Fighter fighter) {
        if(canCancel()){ //can leave fight (no disavantages)
            if(fighter.getTeam() instanceof ChallengeTeam && ((ChallengeTeam) fighter.getTeam()).getInit() == fighter){ //team master => disolve team
                ChallengeTeam team = (ChallengeTeam)fighter.getTeam();
                teams.remove(team); //remove the team
                fightHandler.kickAllFighters(team.getFighters());
                
                for(Fighter f : team.getFighters()){
                    sendToFight(new RemoveSprite(f));
                }
                
                team.sendToTeam(new FightLeaved());
            }else{
                fighter.getTeam().removeFighter(fighter);
                sendToFight(new RemoveSprite(fighter));
                oldMap.sendToMap(TeamFighters.removeFighter(fighter.getTeam(), fighter));
                fightHandler.kickAllFighters(Collections.singletonList(fighter));
                
                if(fighter.getPlayer() != null)
                    fighter.getPlayer().send(new FightLeaved());
            }
            
            //end fight if the fight is not valid
            if(!verifyStillInFight()){
                state = Constants.FIGHT_STATE_FINISHED;
                SocketManager.GAME_SEND_GAME_REMFLAG_PACKET_TO_MAP(this.oldMap, _id);
                clearFight();
                fightHandler.kickAllFighters(getAllFighters());
                sendToFight(new FightLeaved());
            }
        }else{ //can't leave
            sendToFight(new RemoveSprite(fighter));
            oldMap.sendToMap(TeamFighters.removeFighter(fighter.getTeam(), fighter));
            onFighterLeaved(fighter);
            
            if(fighter.getPlayer() != null)
                fighter.getPlayer().send(new FightLeaved());
            
            //Invalid fight (all fighters has leaved)
            if(!verifyStillInFight()){
                verifIfTeamAllDead();
                return;
            }
        }
    }

    protected void leaveFightOnActive(Fighter fighter) {
        onFighterDie(fighter, fighter);

        if (!verifyStillInFight()) {
            verifIfTeamAllDead();
            return;
        }

        sendToFight(new RemoveSprite(fighter));

        Player P = fighter.getPlayer();
        P.set_duelID(-1);
        P.setFight(null);
        P.setSitted(false);
        P.setAway(false);

        //si c'était a son tour de jouer
        if (_ordreJeu.get(_curPlayer) == null) {
            return;
        }
        if (_ordreJeu.get(_curPlayer).getSpriteId() == fighter.getSpriteId()) {
            endTurn();
        }

        onFighterLeaved(fighter);

        if (P.isOnline()) {
            SocketManager.GAME_SEND_GV_PACKET(P);
        }
    }

    public String getGTL() {
        String packet = "GTL";
        for (Fighter f : get_ordreJeu()) {
            packet += "|" + f.getSpriteId();
        }
        return packet + (char) 0x00;
    }

    public int getNextLowerFighterGuid() {
        int g = -1;
        for (Fighter f : getAllFighters()) {
            if (f.getSpriteId() < g) {
                g = f.getSpriteId();
            }
        }
        g--;
        return g;
    }

    public String parseFightInfos() {
        StringBuilder infos = new StringBuilder();
        infos.append(_id).append(";");
        long time = System.nanoTime() - startTime;
        infos.append((startTime == 0 ? "-1" : time)).append(";");

        for (FightTeam team : teams) {
            infos.append(team.getType()).append(',')
                .append(team.getAlignement()).append(',')
                .append(team.getSize()).append(';');
        }

        return infos.toString();
    }

    /**
     * Check if there at least 2 differents teams in fight
     * AND at least one player
     * @return 
     */
    public boolean verifyStillInFight(){
        int count = 0;
        int playerCount = 0;
        for (FightTeam team : teams) {
            if (!team.isAllDead()) {
                ++count;
                for(Fighter fighter : team.getFighters()){
                    if(!fighter.isDead() && fighter instanceof PlayerFighter)
                        ++playerCount;
                }
            }
        }
        
        return playerCount > 0 && count >= 2;
    }

    public Map<Integer, Fighter> getDeadList() {
        return deadList;
    }

    public void delOneDead(Fighter target) {
        deadList.remove(target.getSpriteId());
    }

    public GameMap getOldMap() {
        return oldMap;
    }

    abstract public boolean canCancel();

    public long getStartTime() {
        return startTime;
    }

    abstract public int getPlacementTime();

    public long getRemaningTime() {
        long time = initTime + getPlacementTime() - System.currentTimeMillis();
        return time < 0 ? 0 : time;
    }

    protected void clearFight() {
        oldMap.removeFight(_id);
        _ordreJeu.clear();
        _turnTimer = null;
        deadList.clear();
        spectators.clear();
        fightObjectList.clear();
    }

    public void applyBuffsOnDammages(EffectScope scope) {
        //apply change target effects before others
        for (Effect effect : FightEffect.CHANGE_TARGET_EFFECTS) {
            for (Buff buff : scope.getTarget().getBuffs()) {
                if (buff.getSpellEffect().getEffect() == effect) {
                    fightHandler.applyBuffOnDammages(buff, scope);
                    break;
                }
            }
        }

        for (Buff buff : new ArrayList<>(scope.getTarget().getBuffs())) {
            if (!FightEffect.isChangeTargetEffect(buff.getSpellEffect().getEffect())) {
                fightHandler.applyBuffOnDammages(buff, scope);
            }
        }

        scope.sendBuffResults();
    }

    public void onPlaceChange(Fighter fighter, MapCell cell) {
        if (!fighter.getCell().equals(cell)) {
            fighter.getCell().removeFighter(fighter);
            fighter.set_fightCell(cell);
            cell.addFighter(fighter);
        }

        for(Trap trap : fightObjectList.getTraps(cell)){
            fightObjectList.removeFightObject(trap);
            trap.trigger(fighter, this, fightHandler);
        }
    }
    
    public void addFightObject(FightObject fightObject){
        fightObjectList.addFightObject(fightObject);
    }
    
    public boolean cellContainsObject(MapCell cell){
        return !fightObjectList.isFreeCell(cell);
    }

    public boolean isOnTrap(MapCell cell) {
        return fightObjectList.isOnTrap(cell);
    }

    public FightSpectators getSpectators() {
        return spectators;
    }
}
