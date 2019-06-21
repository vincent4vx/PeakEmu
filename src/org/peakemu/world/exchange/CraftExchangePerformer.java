/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CraftExchangePerformer implements ExchangePerformer{

    @Override
    public ExchangeType getExchangeType() {
        return ExchangeType.FACTORY_TABLE;
    }

    @Override
    public void request(Player player, String[] args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void leave(Player player, ExchangeBase exchange) {
        exchange.cancel();
    }

    @Override
    public void validate(Player player, ExchangeBase exchange) {
        CraftExchange real = (CraftExchange)exchange;
        real.doCraft();
    }
    
}
