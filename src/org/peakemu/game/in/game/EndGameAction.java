/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.game;

import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class EndGameAction implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        boolean success = args.charAt(0) == 'K';
        
        if(client.getPlayer() == null){
            return;
        }
        
        client.getGameActionHandler().endCurrent(success, args.substring(1));
    }

    @Override
    public String header() {
        return "GK";
    }
    
}
