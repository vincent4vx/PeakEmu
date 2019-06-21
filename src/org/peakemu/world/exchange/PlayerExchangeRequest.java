/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.game.out.exchange.ExchangeCreated;
import org.peakemu.game.out.exchange.ExchangeLeaved;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Request;
import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PlayerExchangeRequest implements Request{
    final private Player performer;
    final private Player target;

    public PlayerExchangeRequest(Player requester, Player target) {
        this.performer = requester;
        this.target = target;
    }

    @Override
    public Player getPerformer() {
        return performer;
    }

    @Override
    public Player getTarget() {
        return target;
    }

    @Override
    public void cancel() {
        performer.setCurExchange(null);
        performer.setRequest(null);
        target.setCurExchange(null);
        target.setRequest(null);
        performer.send(new ExchangeLeaved());
        target.send(new ExchangeLeaved());
    }

    @Override
    public void accept() {
        PlayerExchangeManager exchangeManager = new PlayerExchangeManager();
        performer.setCurExchange(exchangeManager.createExchange(performer));
        target.setCurExchange(exchangeManager.createExchange(target));
        performer.send(new ExchangeCreated(ExchangeType.PLAYER_EXCHANGE));
        target.send(new ExchangeCreated(ExchangeType.PLAYER_EXCHANGE));
        performer.setRequest(null);
        target.setRequest(null);
    }

    @Override
    public void refuse() {
        cancel();
    }
}
