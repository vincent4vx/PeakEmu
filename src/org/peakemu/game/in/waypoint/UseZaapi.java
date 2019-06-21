/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.waypoint;

import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.world.handler.WaypointHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class UseZaapi implements InputPacket<GameClient>{
    final private WaypointHandler waypointHandler;

    public UseZaapi(WaypointHandler waypointHandler) {
        this.waypointHandler = waypointHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        short map = -1;
        try {
            map = Short.parseShort(args);
        } catch (Exception e) {
            return;
        }
        
        if(client.getPlayer().getCurrentWaypoint() == null)
            return;
        
        waypointHandler.useZaapi(client.getPlayer(), client.getPlayer().getCurrentWaypoint(), map);
    }

    @Override
    public String header() {
        return "Wu";
    }
    
}
