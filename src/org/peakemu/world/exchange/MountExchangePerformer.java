/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.game.out.exchange.ExchangeCreated;
import org.peakemu.game.out.exchange.ExchangeList;
import org.peakemu.game.out.exchange.MountPods;
import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountExchangePerformer implements ExchangePerformer{

    @Override
    public ExchangeType getExchangeType() {
        return ExchangeType.MOUNT;
    }

    @Override
    public void request(Player player, String[] args) {
        if(player.getMount() == null)
            return;
        
        player.setCurExchange(new MountExchange(player, player.getMount()));
        player.send(new ExchangeCreated(getExchangeType()));
        player.send(new ExchangeList(player.getMount().getItems(), -1));
        player.send(new MountPods(player.getMount()));
    }

    @Override
    public void leave(Player player, ExchangeBase exchange) {
        exchange.cancel();
    }

    @Override
    public void validate(Player player, ExchangeBase exchange) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
