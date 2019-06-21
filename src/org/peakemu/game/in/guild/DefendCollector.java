/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.guild;

import org.peakemu.Peak;
import org.peakemu.common.Constants;
import org.peakemu.common.Logger;
import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Collector;
import org.peakemu.world.fight.PvTFight;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class DefendCollector implements InputPacket<GameClient>{
    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getGuild() == null)
            return;
        
        int id;
        
        try{
            id = Integer.parseInt(args.substring(1), 36);
        }catch(NumberFormatException e){
            return;
        }
        
        Collector collector = client.getPlayer().getGuild().getCollectorById(id);
        
        if(collector == null){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Collector %d not found", id);
            return;
        }
        
        PvTFight fight = collector.getFight();
        
        if(fight == null || fight.getState() != Constants.FIGHT_STATE_PLACE)
            return;
        
        switch(args.charAt(0)){
            case 'J':
                fight.addDefender(client.getPlayer());
                break;
            case 'V':
                fight.removeDefender(client.getPlayer());
                break;
            default:
                Peak.gameLog.addToLog(Logger.Level.INFO, "Undefined defend collector action %c", args.charAt(0));
        }
    }

    @Override
    public String header() {
        return "gT";
    }
    
}
