/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.conquest;

import org.peakemu.common.Constants;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.conquest.PrismAttackers;
import org.peakemu.game.out.conquest.PrismDefenders;
import org.peakemu.game.out.conquest.PrismInfosJoined;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Prism;
import org.peakemu.world.fight.ConquestFight;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ConquestInfoJoin implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getAlignement() == Constants.ALIGNEMENT_NEUTRE)
            return;
        
        Prism prism = client.getPlayer().getMap().getSubArea().getPrism();
        
        if(prism == null || prism.getAlignement() != client.getPlayer().getAlignement()){
            client.send(new PrismInfosJoined(PrismInfosJoined.NONE));
            return;
        }
        
        if(!prism.isOnFight()){
            client.send(new PrismInfosJoined(PrismInfosJoined.NOFIGHT));
            return;
        }
        
        ConquestFight fight = prism.getFight();
        
        if(fight.getState() > Constants.FIGHT_STATE_PLACE){
            client.send(new PrismInfosJoined(PrismInfosJoined.INFIGHT));
            return;
        }
        
        client.send(new PrismInfosJoined((int)fight.getRemaningTime(), fight.getPlacementTime(), 7));
        client.send(PrismDefenders.addAllDefenders(fight));
        client.send(PrismAttackers.addAllAttackers(fight));
    }

    @Override
    public String header() {
        return "CIJ";
    }
    
}
