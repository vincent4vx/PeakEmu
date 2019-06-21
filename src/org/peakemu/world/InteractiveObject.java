/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import org.peakemu.common.SocketManager;
import org.peakemu.world.enums.IOState;
import org.peakemu.world.enums.IOType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class InteractiveObject {
    private int _id;
    private IOState state;
    private GameMap _map;
    private MapCell _cell;
    private boolean _interactive = true;
    //private Timer _respawnTimer;
    private IOTemplate _template;
    public int _respawnDelay;
    public Thread _respawnThread;
    private IOType type;
    private long stateChangeTime = System.currentTimeMillis();

    public InteractiveObject(int id, GameMap a_map, MapCell a_cell, IOTemplate template) {
        _id = id;
        _map = a_map;
        _cell = a_cell;
        state = IOState.FULL;
        _template = template;
        type = IOType.valueOf(id);
    }
    
    public InteractiveObject(InteractiveObject other){
        this(other._id, other._map, other._cell, other._template);
    }

    public boolean templateExists() {
        return _template != null;
    }

    public void respawn() {
        if (state != IOState.EMPTY && state != IOState.EMPTY2) {
            return;
        }

        setState(IOState.FULLING);
        _interactive = true;
        SocketManager.GAME_SEND_GDF_PACKET_TO_MAP(_map, _cell);
        setState(IOState.FULL);
    }

    public int getID() {
        return _id;
    }

    public boolean isInteractive() {
        return _interactive;
    }

    public void setInteractive(boolean b) {
        _interactive = b;
    }

    public IOState getState() {
        return state;
    }

    public void setState(IOState state) {
        this.state = state;
        stateChangeTime = System.currentTimeMillis();
    }

    public long getStateChangeTime() {
        return stateChangeTime;
    }
    
    public int getRespawnDelay(){
        return _template.getRespawnTime();
    }

    public int getUseDuration() {
        int duration = 1500;
        if (_template != null) {
            duration = _template.getDuration();
        }
        return duration;
    }

    public int getUnknowValue() {
        int unk = 4;
        if (_template != null) {
            unk = _template.getUnk();
        }
        return unk;
    }

    public boolean isWalkable() {
        if (_template == null) {
            return false;
        }
        
        return _template.isWalkable();
    }

    public IOType getType() {
        return type;
    }
}
