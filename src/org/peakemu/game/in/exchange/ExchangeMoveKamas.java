/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.exchange;

import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.world.exchange.ExchangeKamas;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ExchangeMoveKamas implements InputPacket<GameClient>{
    
    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getCurExchange() == null || !(client.getPlayer().getCurExchange() instanceof ExchangeKamas))
            return;
        
        ExchangeKamas exchange = (ExchangeKamas) client.getPlayer().getCurExchange();
        
        long qte = Long.parseLong(args);
        
        if(qte >= 0)
            exchange.addKamas(qte);
        else
            exchange.removeKamas(-qte);
    }

    @Override
    public String header() {
        return "EMG";
    }
    
}
