/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.game.out.exchange.ExchangeCreated;
import org.peakemu.game.out.exchange.PlayerShopList;
import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PlayerManageStoreExchangePerformer implements ExchangePerformer{

    @Override
    public ExchangeType getExchangeType() {
        return ExchangeType.PLAYER_STORE_SELL;
    }

    @Override
    public void request(Player player, String[] args) {
        if(player == null || player.isAway() || !player.getRestrictions().canBeMerchant())
            return;
        
        player.setCurExchange(new PlayerStoreSellExchange(player, player.getStore()));
        player.send(new ExchangeCreated(ExchangeType.PLAYER_STORE_SELL));
        player.send(new PlayerShopList(player.getStore()));
    }

    @Override
    public void leave(Player player, ExchangeBase exchange) {
        exchange.cancel();
    }

    @Override
    public void validate(Player player, ExchangeBase exchange) {}
    
}
