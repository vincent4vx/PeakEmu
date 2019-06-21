/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.exchange;

import org.peakemu.common.util.StringUtil;
import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.world.exchange.BuyExchange;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class BuyItem implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getCurExchange() == null || !(client.getPlayer().getCurExchange() instanceof BuyExchange))
            return;
        
        BuyExchange exchange = (BuyExchange) client.getPlayer().getCurExchange();
        
        String[] data = StringUtil.split(args, "|");
        
        int itemId, qte;
        
        try{
            itemId = Integer.parseInt(data[0]);
            qte = Integer.parseInt(data[1]);
        }catch(Exception e){
            return;
        }
        
        exchange.buy(itemId, qte);
    }

    @Override
    public String header() {
        return "EB";
    }
    
}
