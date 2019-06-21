/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.exchange;

import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.world.handler.ExchangeHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class LeaveExchange implements InputPacket<GameClient>{
    final private ExchangeHandler exchangeHandler;

    public LeaveExchange(ExchangeHandler exchangeHandler) {
        this.exchangeHandler = exchangeHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        
        if(client.getPlayer().getRequest() != null)
            client.getPlayer().getRequest().cancel();
        
        exchangeHandler.leave(client.getPlayer());
        
        client.getPlayer().setAway(false);
        client.getPlayer().setRequest(null);
        client.getPlayer().setCurExchange(null);
    }

    @Override
    public String header() {
        return "EV";
    }
    
}
