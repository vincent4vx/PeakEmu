/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.game;

import org.peakemu.common.Constants;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.game.FighterReady;
import org.peakemu.network.InputPacket;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.PlayerFighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FighterIsReady implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getFighter() == null)
            return;
        
        Fight fight = client.getPlayer().getFight();
        
        if(fight.getState() != Constants.FIGHT_STATE_PLACE)
            return;
        
        PlayerFighter fighter = client.getPlayer().getFighter();
        
        fighter.setReady(args.equals("1"));
        
        fight.sendToFight(new FighterReady(fighter));
        fight.verifIfAllReady();
    }

    @Override
    public String header() {
        return "GR";
    }
    
}
