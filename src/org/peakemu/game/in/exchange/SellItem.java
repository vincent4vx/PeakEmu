/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.exchange;

import org.peakemu.common.util.StringUtil;
import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.world.exchange.SellExchange;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SellItem implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getCurExchange() == null || !(client.getPlayer().getCurExchange() instanceof SellExchange))
            return;
        
        SellExchange sellExchange = (SellExchange)client.getPlayer().getCurExchange();
        
        int item, qte;
        
        try{
            String[] data = StringUtil.split(args, "|", 2);
            item = Integer.parseInt(data[0]);
            qte = Integer.parseInt(data[1]);
        }catch(Exception e){
            return;
        }
        
        sellExchange.sell(item, qte);
    }

    @Override
    public String header() {
        return "ES";
    }
    
}
