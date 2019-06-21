/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.fight;

import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class LeaveFight implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getFight() == null)
            return;
        
        Fight fight = client.getPlayer().getFight();
        
        if(!args.isEmpty()){ //kick other
            int id = Integer.parseInt(args);
            Fighter fighter = fight.getFighterById(id);
            
            if(fighter != null)
                fight.kickFighter(client.getPlayer().getFighter(), fighter);
        }else{
            if(client.getPlayer().getFighter() == null) //not a fighter => spectator
                fight.getSpectators().leave(client.getPlayer());
            else
                fight.leaveFight(client.getPlayer().getFighter());
        }
    }

    @Override
    public String header() {
        return "GQ";
    }
    
}
