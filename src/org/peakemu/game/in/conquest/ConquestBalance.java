/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.conquest;

import org.peakemu.common.Constants;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.conquest.AlignementBalance;
import org.peakemu.network.InputPacket;
import org.peakemu.world.handler.AlignementHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ConquestBalance implements InputPacket<GameClient>{
    final private AlignementHandler alignementHandler;

    public ConquestBalance(AlignementHandler alignementHandler) {
        this.alignementHandler = alignementHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getAlignement() == Constants.ALIGNEMENT_NEUTRE)
            return;
        
        client.send(new AlignementBalance(
            alignementHandler.getWorldRate(client.getPlayer().getAlignement()), 
            alignementHandler.getAreaRate(client.getPlayer().getMap().getSubArea().getArea(), client.getPlayer().getAlignement())
        ));
    }

    @Override
    public String header() {
        return "Cb";
    }
    
}
