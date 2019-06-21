/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.waypoint;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.waypoint.ZaapiLeaved;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class LeaveZaapi implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() != null)
            client.getPlayer().setCurrentWaypoint(null);
        
        client.send(new ZaapiLeaved());
    }

    @Override
    public String header() {
        return "Wv";
    }
    
}
