/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.basic;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.basic.WhoIsError;
import org.peakemu.game.out.basic.WhoIsResponse;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class WhoIs implements InputPacket<GameClient>{
    final private SessionHandler sessionHandler;

    public WhoIs(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        Player target = args.isEmpty() ? client.getPlayer() : sessionHandler.searchPlayer(args);
        
        if(target == null){
            client.send(new WhoIsError(args));
        }else{
            client.send(new WhoIsResponse(target));
        }
    }

    @Override
    public String header() {
        return "BW";
    }
}
