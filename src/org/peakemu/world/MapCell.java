/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import javax.swing.Timer;
import org.peakemu.common.SocketManager;
import org.peakemu.world.fight.fighter.Fighter;

public class MapCell {
    final private int id;
    private Map<Integer, Fighter> _fighters;	//= new TreeMap<Integer, Fighter>();
    final private boolean _Walkable;
    final private boolean _LoS;
    final private GameMap map;
    final private InteractiveObject _object;

    private int _status = 4; // Interrupteur > pour portes : 4 = fermé, 2 = ouvert
    private Timer _timer; // Interrupteur > timer de fermeture
    private String _cellsHidden; // Interrupteur > cellules cachées

    public MapCell(GameMap a_map, int id, boolean _walk, boolean LoS, int idObj, IOTemplate ioTemplate) {
        map = a_map;
        this.id = id;
        this._Walkable = _walk;
        this._LoS = LoS;
        this._object = idObj == -1 ? null : new InteractiveObject(idObj, a_map, this, ioTemplate);
    }
    
    public MapCell(MapCell other, GameMap map){
        this.id = other.id;
        this.map = map;
        this._Walkable = other._Walkable;
        this._LoS = other._LoS;
        this._object = other._object;
    }

    public void switchOn(String cellsHidden) // Interrupteur : ouverture
    {
        if (_status == 2) {
            return; // Déjà ouvert
        }
        // Ouverture 
        _status = 2;
        _cellsHidden = cellsHidden;
        // On stocke le couple interrupteur/porte
        map.addSwitch(id + ";" + _cellsHidden);
        // Cellules marchables
        SocketManager.GAME_SEND_GDC_TO_MAP(map, _cellsHidden, true);
        // Ouverture porte
        SocketManager.GAME_SEND_SWITCH_GDF_TO_MAP(this, map);

        // Timer de fermeture
        _timer = new Timer(30000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _timer.stop();
                _status = 4;
                // Cellules non marchables
                SocketManager.GAME_SEND_GDC_TO_MAP(map, _cellsHidden, false);
                // On ferme
                SocketManager.GAME_SEND_SWITCH_GDF_TO_MAP(map.getCell(id), map);
                // Delete de la carte
                map.removeSwitch(id + ";" + _cellsHidden);
            }
        });
        this._timer.start();
    }

    public int getStatus() // Interrupteur : état de la porte
    {
        return _status;
    }

    public InteractiveObject getObject() {
        return _object;
    }

    public int getID() {
        return id;
    }

    public void addFighter(Fighter fighter) {
        if (_fighters == null) {
            _fighters = new TreeMap<Integer, Fighter>();
        }
        _fighters.put(fighter.getSpriteId(), fighter);
    }

    public void removeFighter(Fighter fighter) {
        _fighters.remove(fighter.getSpriteId());
    }

    public boolean isWalkable(boolean useObject) {
        if (_object != null && useObject) {
            return _Walkable && _object.isWalkable();
        }
        return _Walkable;
    }

    public boolean blockLoS() {
        if (_fighters == null) {
            return _LoS;
        }
        boolean fighter = true;
        for (Map.Entry<Integer, Fighter> f : _fighters.entrySet()) {
            if (!f.getValue().isHidden()) {
                fighter = false;
            }
        }
        return _LoS && fighter;
    }

    public boolean isLoS() {
        return _LoS;
    }

    public Map<Integer, Fighter> getFighters() {
        if (_fighters == null) {
            return Collections.EMPTY_MAP;
        }
        return _fighters;
    }

    public Fighter getFirstFighter() {
        if (_fighters == null) {
            return null;
        }
        for (Map.Entry<Integer, Fighter> entry : _fighters.entrySet()) {
            return entry.getValue();
        }
        return null;
    }

    public GameMap getMap() {
        return map;
    }
    
    public boolean isInteractive(){
        return _object != null;
    }
    
    public boolean isTrigger(){
        return !map.getTriggers(id).isEmpty();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.id;
        hash = 41 * hash + Objects.hashCode(this.map);
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
        final MapCell other = (MapCell) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.map, other.map)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MapCell{" + id + '}';
    }
}
