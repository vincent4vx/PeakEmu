/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.waypoint;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.waypoint.ZaapLeaved;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class LeaveZaap implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() != null)
            client.getPlayer().setCurrentWaypoint(null);
        
        client.send(new ZaapLeaved());
    }

    @Override
    public String header() {
        return "WV";
    }
    
}
