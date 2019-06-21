/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.game.out.exchange.ExchangeRequestError;
import org.peakemu.game.out.exchange.ExchangeRequestOk;
import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PlayerExchangePerformer implements ExchangePerformer{
    final private PlayerDAO playerDAO;

    public PlayerExchangePerformer(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Override
    public ExchangeType getExchangeType() {
        return ExchangeType.PLAYER_EXCHANGE;
    }

    @Override
    public void request(Player player, String[] args) {
        int guidTarget = Integer.parseInt(args[1]);

        Player target = player.getMap().getPlayer(guidTarget);

        if (target == null || !target.isOnline()) {
            player.send(new ExchangeRequestError(ExchangeRequestError.CANT_EXCHANGE));
            return;
        }

        if (target.isAway() || player.isAway() || target.getRequest() != null) {
            player.send(new ExchangeRequestError(ExchangeRequestError.ALREADY_EXCHANGE));
            return;
        }

        PlayerExchangeRequest trading = new PlayerExchangeRequest(player, target);

        player.setRequest(trading);
        target.setRequest(trading);

        ExchangeRequestOk packet = new ExchangeRequestOk(player.getSpriteId(), guidTarget, ExchangeType.PLAYER_EXCHANGE);
        player.send(packet);
        target.getAccount().getGameThread().send(packet);
    }

    @Override
    public void leave(Player player, ExchangeBase exchange) {
        exchange.cancel();
    }

    @Override
    public void validate(Player player, ExchangeBase exchange) {
        PlayerExchangeManager.PlayerExchange realExchange = (PlayerExchangeManager.PlayerExchange) exchange;
        realExchange.toogleValidate();
        
        if(realExchange.isValidated()){
            for(Player p : realExchange.getPlayers()){
                playerDAO.save(p);
            }
        }
    }
    
    
}
