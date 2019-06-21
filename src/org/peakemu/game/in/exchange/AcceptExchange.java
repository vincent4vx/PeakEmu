/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.exchange;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.exchange.ExchangeCreated;
import org.peakemu.network.InputPacket;
import org.peakemu.world.exchange.PlayerExchangeRequest;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AcceptExchange implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getRequest() == null || !(client.getPlayer().getRequest() instanceof PlayerExchangeRequest)){
            client.send(new ExchangeCreated(false));
            return;
        }
        
        client.getPlayer().getRequest().accept();
    }

    @Override
    public String header() {
        return "EA";
    }
    
}
