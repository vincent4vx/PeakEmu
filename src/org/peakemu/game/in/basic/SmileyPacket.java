/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.peakemu.game.in.basic;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.basic.ChatSmileySent;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Gaëtan
 */
public class SmileyPacket implements InputPacket<GameClient>{
   
    @Override
    public String header(){
        return "BS";
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getMap() == null)
            return;
        
        int smiley;
        
        try{
            smiley = Integer.parseInt(args);
        }catch(NumberFormatException e){
            return;
        }
        
        if(smiley < 1 || smiley > 15)
            return;
        
        client.getPlayer().getMap().sendToMap(new ChatSmileySent(client.getPlayer(), smiley));
    }
}
