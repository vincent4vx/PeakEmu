/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.exchange;

import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.world.exchange.CraftExchange;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class StartCraftLoop implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getCurExchange() == null || !(client.getPlayer().getCurExchange() instanceof CraftExchange))
            return;
        
        CraftExchange exchange = (CraftExchange)client.getPlayer().getCurExchange();
        
        if(exchange.isOnCraftLoop())
            return;
        
        int count;
        
        try{
            count = Integer.parseInt(args);
        }catch(NumberFormatException e){
            return;
        }
        
        if(count <= 0)
            return;
        
        exchange.craftLoop(count);
    }

    @Override
    public String header() {
        return "EMR";
    }
    
}
