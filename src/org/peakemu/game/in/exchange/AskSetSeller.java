/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.exchange;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.exchange.SetSellerConfirmation;
import org.peakemu.network.InputPacket;
import org.peakemu.world.config.StoreConfig;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AskSetSeller implements InputPacket<GameClient>{
    final private StoreConfig config;

    public AskSetSeller(StoreConfig config) {
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
        
        long total = client.getPlayer().getStore().getTotalPrice();
        long toPay = (long) Math.ceil(total * config.getPlayerStoreTaxRate());
        double rate = config.getPlayerStoreTaxRate() * 1000;
        client.send(new SetSellerConfirmation(total, rate, toPay));
    }

    @Override
    public String header() {
        return "Eq";
    }
    
}
