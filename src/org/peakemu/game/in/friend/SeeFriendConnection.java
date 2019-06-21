/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.friend;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.basic.NoOperation;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SeeFriendConnection implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        client.getPlayer().SetSeeFriendOnline(args.equals("+"));
        client.send(new NoOperation());
    }

    @Override
    public String header() {
        return "FO";
    }
    
}
