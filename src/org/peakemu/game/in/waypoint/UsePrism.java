/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.waypoint;

import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Prism;
import org.peakemu.world.handler.MapHandler;
import org.peakemu.world.handler.WaypointHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class UsePrism implements InputPacket<GameClient>{
    final private WaypointHandler waypointHandler;
    final private MapHandler mapHandler;

    public UsePrism(WaypointHandler waypointHandler, MapHandler mapHandler) {
        this.waypointHandler = waypointHandler;
        this.mapHandler = mapHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        Prism prism = client.getPlayer().getMap().getPrism();
        
        if(prism == null)
            return;
        
        short mapId;
        
        try{
            mapId = Short.parseShort(args);
        }catch(NumberFormatException e){
            return;
        }
        
        if(mapHandler.getMap(mapId) == null)
            return;
        
        Prism target = mapHandler.getMap(mapId).getPrism();
        
        if(target == null)
            return;
        
        waypointHandler.usePrism(client.getPlayer(), prism, target);
    }

    @Override
    public String header() {
        return "Wp";
    }
    
}
