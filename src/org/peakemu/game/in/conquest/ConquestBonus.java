/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.conquest;

import org.peakemu.common.Constants;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.conquest.ConquestBonusResponse;
import org.peakemu.network.InputPacket;
import org.peakemu.world.handler.AlignementHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ConquestBonus implements InputPacket<GameClient>{
    final private AlignementHandler alignementHandler;

    public ConquestBonus(AlignementHandler alignementHandler) {
        this.alignementHandler = alignementHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getAlignement() == Constants.ALIGNEMENT_NEUTRE)
            return;
        
        //TODO: real alignement bonus
        int rate = 0;//alignementHandler.getWorldRate(client.getPlayer().getAlignement());
        int xrate = 0;//(int)(client.getPlayer().getGrade() / 2.5) + 1;
        
        ConquestBonusResponse response = new ConquestBonusResponse();
        
        client.send(
            response.setAlignBonus(new ConquestBonusResponse.Data(rate, rate, rate))
                    .setRankMultiplicator(new ConquestBonusResponse.Data(xrate, xrate, xrate))
                    .setAlignMalus(new ConquestBonusResponse.Data(rate, rate, rate))
        );
    }

    @Override
    public String header() {
        return "CB";
    }
    
}
