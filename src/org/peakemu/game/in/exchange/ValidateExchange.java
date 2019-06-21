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
public class ValidateExchange implements InputPacket<GameClient>{
    final private ExchangeHandler exchangeHandler;

    public ValidateExchange(ExchangeHandler exchangeHandler) {
        this.exchangeHandler = exchangeHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getCurExchange() == null)
            return;
        
        exchangeHandler.validate(client.getPlayer());
    }

    @Override
    public String header() {
        return "EK";
    }
    
}
