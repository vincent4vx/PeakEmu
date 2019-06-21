/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.waypoint;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.waypoint.PrismLeaved;
import org.peakemu.game.out.waypoint.PrismList;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class LeavePrism implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        client.send(new PrismLeaved());
    }

    @Override
    public String header() {
        return "Ww";
    }
    
}
