package org.peakemu.objects;

import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.GameMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import org.peakemu.world.ItemStorage;
import org.peakemu.world.MapCell;
import org.peakemu.world.MoveableSprite;
import org.peakemu.world.StorageInventory;
import org.peakemu.world.World;
import org.peakemu.world.World.Drop;
import org.peakemu.world.enums.SpriteTypeEnum;
import org.peakemu.world.exchange.StorageExchange;
import org.peakemu.world.fight.PvTFight;

public class Collector implements MoveableSprite, StorageInventory {
    final private int _guid;
    final private GameMap map;
    private MapCell cell;
    private byte _orientation;
    final private Guild guild;
    private short _N1 = 0;
    private short _N2 = 0;
    private byte _inFight = 0;
    final private ItemStorage storage;
    private long _kamas = 0;
    private long _xp = 0;
    private boolean inExchange = false;
    //Les logs
    private Map<Integer, Item> _LogObjets = new TreeMap<Integer, Item>();
    private long _LogXP = 0;
    //défense
    private Map<Integer, Player> _DefensepersosID = new TreeMap<Integer, Player>();
    
    private int spriteId = 0;
    private PvTFight fight = null;
    
    private StorageExchange exchange = null;

    public Collector(int guid, GameMap map, MapCell cell, byte orientation, Guild guild, short N1, short N2, ItemStorage storage, long kamas, long xp) {
        this._guid = guid;
        this._orientation = orientation;
        this.guild = guild;
        _N1 = N1;
        _N2 = N2;
        this.map = map;
        this.cell = cell;
        this.storage = storage;
        _xp = xp;
        _kamas = kamas;
    }

    public ArrayList<Drop> getDrops() {
        ArrayList<Drop> toReturn = new ArrayList<World.Drop>();
        for (Item obj : storage) {
            toReturn.add(new Drop(obj.getTemplate().getID(), 0, 100, obj.getQuantity()));
        }
        return toReturn;
    }

    @Override
    public long getKamas() {
        return _kamas;
    }

    @Override
    public void setKamas(long kamas) {
        this._kamas = kamas;
    }

    public GameMap getMap() {
        return map;
    }

    @Override
    public MapCell getCell() {
        return cell;
    }

    @Override
    public void setCell(MapCell cell) {
        this.cell = cell;
    }

    public long getXp() {
        return _xp;
    }

    public void setXp(long xp) {
        this._xp = xp;
    }

    @Override
    public SpriteTypeEnum getSpriteType() {
        return SpriteTypeEnum.COLLECTOR;
    }

    @Override
    public int getSpriteId() {
        if(spriteId == 0)
            throw new IllegalStateException("Sprite not initialized for Collector " + _guid);
        
        return spriteId;
    }

    public void setSpriteId(int spriteId) {
        this.spriteId = spriteId;
    }

    @Override
    public String getSpritePacket() {
        StringBuilder sock = new StringBuilder(64);
        sock.append(cell.getID()).append(";");
        sock.append(_orientation).append(";");
        sock.append("0").append(";");
        sock.append(getSpriteId()).append(";");
        sock.append(_N1).append(",").append(_N2).append(";");
        sock.append(getSpriteType().toInt()).append(";");
        sock.append("6000^130;");
        sock.append(guild.getLevel()).append(";");
        sock.append(guild.getName()).append(";").append(guild.getEmbem());
        return sock.toString();
    }

    public int get_inFight() {
        return _inFight;
    }

    public void set_inFight(byte fight) {
        _inFight = fight;
    }

    public int getGuid() {
        return _guid;
    }

    public short get_N1() {
        return _N1;
    }

    public short get_N2() {
        return _N2;
    }

    public void LogXpDrop(long Xp) {
        _LogXP += Xp;
    }

    public void LogObjetDrop(int guid, Item obj) {
        _LogObjets.put(guid, obj);
    }

    public long get_LogXp() {
        return _LogXP;
    }

    public String get_LogItems() {
        StringBuilder str = new StringBuilder();
        if (_LogObjets.isEmpty()) {
            return "";
        }
        for (Item obj : _LogObjets.values()) {
            str.append(";").append(obj.getTemplate().getID()).append(",").append(obj.getQuantity());
        }
        return str.toString();
    }

    public void addItem(Item newObj) {
        storage.addItem(newObj);
    }

    public void setInExchange(boolean Exchange) {
        inExchange = Exchange;
    }

    public boolean isInExchange() {
        return inExchange;
    }

    public void clearDefenseFight() {
        _DefensepersosID.clear();
    }

    public Map<Integer, Player> getDefenseFight() {
        return _DefensepersosID;
    }

    public Guild getGuild() {
        return guild;
    }

    public byte getOrientation() {
        return _orientation;
    }

    @Override
    public ItemStorage getItems() {
        return storage;
    }

    public PvTFight getFight() {
        return fight;
    }

    public void setFight(PvTFight fight) {
        this.fight = fight;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this._guid;
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
        final Collector other = (Collector) obj;
        if (this._guid != other._guid) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canMove() {
        return true;
    }
}
