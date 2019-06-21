/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.exchange;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.network.InputPacket;
import org.peakemu.world.config.StoreConfig;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SetSeller implements InputPacket<GameClient>{
    final private StoreConfig config;

    public SetSeller(StoreConfig config) {
        this.config = config;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || !client.getPlayer().getRestrictions().canBeMerchant() || client.getPlayer().isAway())
            return;
        
        if(client.getPlayer().getMap().getSellerCount() >= config.getMaxPlayerStorePerMap()){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(25, config.getMaxPlayerStorePerMap()));
            return;
        }
        
        if(client.getPlayer().getStore().isEmpty()){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(23));
            return;
        }
        
        long toPay = (long) Math.ceil(client.getPlayer().getStore().getTotalPrice() * config.getPlayerStoreTaxRate());
        
        if(toPay > client.getPlayer().getKamas()){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(76));
            return;
        }
        
        client.getPlayer().removeKamas(toPay);
        client.getPlayer().setSeller();
        client.close();
    }

    @Override
    public String header() {
        return "EQ";
    }
    
}
