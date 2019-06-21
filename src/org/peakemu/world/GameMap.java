package org.peakemu.world;

import org.peakemu.objects.Monster;
import org.peakemu.objects.MonsterGroup;
import org.peakemu.common.SocketManager;
import org.peakemu.common.Formulas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.TreeMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.peakemu.Peak;
import org.peakemu.common.Constants;
import org.peakemu.common.Logger;
import org.peakemu.game.out.game.AddSprites;
import org.peakemu.game.out.fight.FightCount;
import org.peakemu.game.out.game.AddCellObjects;
import org.peakemu.game.out.game.RemoveCellObject;
import org.peakemu.game.out.game.RemoveSprite;
import org.peakemu.maputil.MapUtil;
import org.peakemu.objects.Collector;
import org.peakemu.objects.item.Item;
import org.peakemu.world.fight.Fight;
import org.peakemu.objects.player.Player;
import org.peakemu.objects.Prism;
import org.peakemu.objects.player.Seller;
import org.peakemu.world.action.Action;
import org.peakemu.world.enums.InventoryPosition;

public class GameMap {

    final private short _id;
    final private String _date;
    final private byte _w;
    final private byte _h;
    final private String _key;
    private String _placesStr;
    final private List<MapCell> cells = new ArrayList<>();
    final private Map<Integer, Fight> _fights = new TreeMap<Integer, Fight>();
    final private List<Monster> availableMonsters;
    private int _nextObjectID = -1;
    final private Point position;
    final private SubArea subArea;
    private byte _maxGroup = 3;
    private byte groupMaxSize;
    private int _maxTeam0 = 0;
    private int _maxTeam1 = 0;
    private ArrayList<String> _switchs = new ArrayList<String>(); // Interrupteurs : liste des portes ouvertes
    final private MapTriggers triggers;
    final private Set<Player> players = new HashSet<>();
    
    final private Map<Integer, Sprite> sprites = new ConcurrentHashMap<>();
    final private Map<MapCell, Item> dropItems = new ConcurrentHashMap<>();
    
    private int nextFightId = Integer.MIN_VALUE;

    public void addSwitch(String newSwitch) // Interrupteur
    {
        _switchs.add(newSwitch);
    }

    public void removeSwitch(String toRemove) // Interrupteur
    {
        _switchs.remove(toRemove);
    }

    public void parseSwitchsToGDC(Player target) // Interrupteur : rend cellules marchables
    {
        for (String curSwitch : _switchs) //Chaque interrupteur / porte de la map
        {
            try {
                String cellids = curSwitch.split(";")[1];
                SocketManager.GAME_SEND_GDC_TO_PERSO(target, cellids, true);
            } catch (Exception e) {
            }

        }
    }

    public void parseSwitchsToGDF(Player target) // Interrupteur : ouvre les différentes portes
    {
        for (String curSwitch : _switchs) //Chaque interrupteur / porte de la map
        {
            try {
                int cellidPorte = Integer.parseInt(curSwitch.split(";")[0]);
                SocketManager.GAME_SEND_SWITCH_GDF_TO_PERSO(getCell(cellidPorte), target);
            } catch (Exception e) {
            }
        }
    }

    public GameMap(short _id, String _date, byte _w, byte _h, String _key, String places, List<Monster> monsters, Point position, SubArea subArea, byte maxGroup, byte maxSize, MapTriggers triggers) {
        this._id = _id;
        this._date = _date;
        this._w = _w;
        this._h = _h;
        this._key = _key;
        this._placesStr = places;
        this._maxGroup = maxGroup;
        this.groupMaxSize = maxSize;
        this.position = position;
        this.subArea = subArea;
        this.triggers = triggers;

        try {
            String[] split = places.split("\\|");
            this._maxTeam0 = (split[0].length() / 2);
            this._maxTeam1 = (split[1].length() / 2);
        } catch (Exception e) {
        }
        
        this.availableMonsters = monsters;
    }
    
    public GameMap(GameMap other){
        this._id = other._id;
        this._date = other._date;
        this._key = other._key;
        this.position = other.position;
        this.subArea = other.subArea;
        this._placesStr = other._placesStr;
        this._maxTeam0 = other._maxTeam0;
        this._maxTeam1 = other._maxTeam1;
        this._w = other._w;
        this._h = other._h;
        this.triggers = other.triggers;
        this.availableMonsters = other.availableMonsters;
        
        for(MapCell cell : other.cells){
            this.cells.add(new MapCell(cell, this));
        }
    }

    public SubArea getSubArea() {
        return subArea;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }
    
    public void spawnGroup(MonsterGroup group){
        if(!group.isFix()){
            if(getMonsterGroupsCount() >= _maxGroup) //Check number of groups ONLY for non-fixed
                return;

            MapCell cell = getRandomFreeCell();

            if(cell == null){
                Peak.worldLog.addToLog(Logger.Level.DEBUG, "No free cell found on map %d", getId());
                return;
            }

            group.setCell(cell);
        }
        
        group.setSpriteId(getNextSpriteId());
        addSprite(group);
    }
    
    public int getNextSpriteId(){
        return _nextObjectID--;
    }
    
    public void addSprite(Sprite sprite){
        sprites.put(sprite.getSpriteId(), sprite);
        sendToMap(new AddSprites(sprite));
    }
    
    public void removeSprite(int spriteId){
        Sprite sprite = sprites.remove(spriteId);
        
        if(sprite != null)
            sendToMap(new RemoveSprite(sprite));
    }
    
    public boolean containsSprite(Sprite sprite){
        return sprites.containsKey(sprite.getSpriteId());
    }
    
    public void removeSprite(Sprite sprite){
        removeSprite(sprite.getSpriteId());
    }
    
    public Sprite getSprite(int spriteId){
        return sprites.get(spriteId);
    }
    
    public Prism getPrism(){
        for(Sprite sprite : sprites.values()){
            if(sprite instanceof Prism)
                return (Prism) sprite;
        }
        
        return null;
    }

    public void setPlaces(String place) {
        _placesStr = place;
    }

    public void removeFight(int id) {
        _fights.remove(id);
        sendToMap(new FightCount(_fights.size()));
    }

    public MapCell getCell(int id) {
        if(id >= cells.size() || id < 0)
            return null;
        
        return cells.get(id);
    }

    public Set<Player> getPersos() {
        return players;
    }
    
    public Player getPlayer(int id){
        for(Player player : players){
            if(player.getSpriteId() == id)
                return player;
        }
        
        return null;
    }

    public short getId() {
        return _id;
    }

    public String get_date() {
        return _date;
    }

    public byte get_w() {
        return _w;
    }

    public byte get_h() {
        return _h;
    }

    public String get_key() {
        return _key;
    }

    public String get_placesStr() {
        return _placesStr;
    }

    public void addPlayer(Player player) {
        cleanOfflinePlayers();
        sendToMap(new AddSprites(player));
        players.add(player);
    }
    
    public void removePlayer(Player player){
        if(players.remove(player)){
            sendToMap(new RemoveSprite(player));
        }
    }

    public void cleanOfflinePlayers() {
        Iterator<Player> it = players.iterator();
        while(it.hasNext()){
            Player player = it.next();
            
            if(player.isOnline())
                continue;
            
            it.remove();
            sendToMap(new RemoveSprite(player));
        }
    }
    
    public Collection<Sprite> getAllSprites(){
        Collection<Sprite> sprites = new ArrayList<>();
        
        sprites.addAll(players);
        sprites.addAll(this.sprites.values());
        
        return sprites;
    }

    public Collection<Sprite> getNonPlayerSprites() {
        return sprites.values();
    }

    public int getGoodCellid(int cellid) // Corrige spawn mobs sur cellule inexistante
    {
        MapCell curCell = getCell(cellid);
        if ((curCell != null && !curCell.isWalkable(true)) || curCell == null) {
            do {
                int randomCellid = Formulas.getRandomValue(0, cells.size() - 1);
                MapCell newCell = getCell(randomCellid);
                if (newCell != null) {
                    if (newCell.isWalkable(true)) {
                        curCell = newCell;
                    }
                }

            } while ((curCell != null && !curCell.isWalkable(true)) || curCell == null);
        }
        return curCell.getID();
    }

    public String getObjectsGDsPackets() {
        StringBuilder toreturn = new StringBuilder();
        boolean first = true;
        for (MapCell cell : cells) {
            if (cell.getObject() != null) {
                if (!first) {
                    toreturn.append((char) 0x00);
                }
                first = false;
                int cellID = cell.getID();
                InteractiveObject object = cell.getObject();
                toreturn.append("GDF|").append(cellID).append(";").append(object.getState().getValue()).append(";").append((object.isInteractive() ? "1" : "0"));
            }
        }
        return toreturn.toString();
    }

    //IOs walkables en combat
    public String getObjectsGDsPacketsInFight() {
        StringBuilder toreturn = new StringBuilder();
        boolean first = true;
        for (MapCell cell : cells) {
            if (cell.getObject() != null) {
                if (!first) {
                    toreturn.append((char) 0x00);
                }
                first = false;
                int cellID = cell.getID();
                toreturn.append("GDF|").append(cellID).append(";0;0");
            }
        }
        return toreturn.toString();
    }

    public int getNbrFight() {
        return _fights.size();
    }

    public Map<Integer, Fight> get_fights() {
        return _fights;
    }
    
    public MapCell getRandomFreeCell(){
        return MapUtil.getRandomFreeCell(this, cells);
    }
    
    public Player getFirstPlayerOnCell(int cellId){
        for(Player player : players){
            if(player.getCell().getID() == cellId)
                return player;
        }
        
        return null;
    }
    
    synchronized public int getNextFightId(){
        return nextFightId++;
    }
    
    public void addFight(Fight fight){
        _fights.put(fight.get_id(), fight);
        sendToMap(new FightCount(_fights.size()));
    }

    @Deprecated
    public GameMap getMapCopy() {
        return new GameMap(this);
    }

    public InteractiveObject getMountParkDoor() {
        for (MapCell c : cells) {
            if (c.getObject() == null) {
                continue;
            }
            //Si enclose
            if (c.getObject().getID() == 6763
                    || c.getObject().getID() == 6766
                    || c.getObject().getID() == 6767
                    || c.getObject().getID() == 6772) {
                return c.getObject();
            }

        }
        return null;
    }

    public int getMaxGroupNumb() {
        return _maxGroup;
    }

    public void setMaxGroup(byte id) {
        _maxGroup = id;
    }

    public Fight getFight(int id) {
        return _fights.get(id);
    }
    
    public Collection<Fight> getFights(){
        return _fights.values();
    }

    public List<MapCell> getCells() {
        return cells;
    }
    
    public void addCell(MapCell cell){
        cells.add(cell);
    }

    public int getSellerCount() {
        int count = 0;
        
        for(Sprite sprite : sprites.values()){
            if(sprite instanceof Seller)
                ++count;
        }
        
        return count;
    }

    public int get_maxTeam1() {
        return _maxTeam1;
    }

    public int get_maxTeam0() {
        return _maxTeam0;
    }

    public Set<Action> getTriggers(int cellId){
        return triggers.getTriggersByCell(cellId);
    }
    
    public void sendToMap(Object packet){
        String s = packet.toString();
        for(Player player : players){
            player.send(s);
        }
    }
    
    public List<Monster> getAvailableMonsters(){
        return availableMonsters;
    }

    public byte getGroupMaxSize() {
        return groupMaxSize;
    }
    
    public Collector getCollector(){
        for(Sprite sprite : sprites.values()){
            if(sprite instanceof Collector)
                return (Collector) sprite;
        }
        
        return null;
    }
    
    public boolean isFreeCell(MapCell cell){
        if(!cell.isWalkable(true))
            return false;
        
        if(dropItems.containsKey(cell))
            return false;
        
        for(Sprite sprite : sprites.values()){
            if(sprite.getCell().equals(cell))
                return false;
        }
        
        for(Player player : players){
            if(player.getCell().equals(cell))
                return false;
        }
        
        return true;
    }
    
    public Collection<MonsterGroup> getMonsterGroups(){
        Collection<MonsterGroup> groups = new ArrayList<>();
        
        for(Sprite sprite : sprites.values()){
            if(sprite instanceof MonsterGroup)
                groups.add((MonsterGroup) sprite);
        }
        
        return groups;
    }
    
    public int getMonsterGroupsCount(){
        int count = 0;
        
        for(Sprite sprite : sprites.values()){
            if(sprite instanceof MonsterGroup)
                ++count;
        }
        
        return count;
    }
    
    //============//
    // Drop items //
    //============//
    
    public Map<MapCell, Item> getDropItems() {
        return dropItems;
    }
    
    public void dropItem(MapCell cell, Item item){
        item.setPosition(InventoryPosition.NO_EQUIPED);
        item.setOwner("MAP");
        dropItems.put(cell, item);
        sendToMap(new AddCellObjects().addObject(cell, item));
    }
    
    public Item removeDropItem(MapCell cell){
        Item item = dropItems.remove(cell);
        
        if(item != null)
            sendToMap(new RemoveCellObject(cell));
        
        return item;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + this._id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameMap other = (GameMap) obj;
        if (this._id != other._id) {
            return false;
        }
        return true;
    }
}
