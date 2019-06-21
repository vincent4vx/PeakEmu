/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.game.out.exchange.ExchangeCreated;
import org.peakemu.game.out.exchange.ExchangeList;
import org.peakemu.game.out.exchange.ExchangeRequestError;
import org.peakemu.objects.Collector;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Sprite;
import org.peakemu.world.enums.ExchangeType;
import org.peakemu.world.enums.GuildRight;
import org.peakemu.world.handler.GuildHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CollectorExchangePerformer implements ExchangePerformer{
    final private PlayerDAO playerDAO;
    final private GuildHandler guildHandler;

    public CollectorExchangePerformer(PlayerDAO playerDAO, GuildHandler guildHandler) {
        this.playerDAO = playerDAO;
        this.guildHandler = guildHandler;
    }

    @Override
    public ExchangeType getExchangeType() {
        return ExchangeType.COLLECTOR;
    }

    @Override
    public void request(Player player, String[] args) {
        if(player == null || player.isAway()){
            return;
        }

        int collectorId = Integer.parseInt(args[1]);
        Sprite sprite = player.getMap().getSprite(collectorId);

        if(!(sprite instanceof Collector)){
            player.send(new ExchangeRequestError(ExchangeRequestError.CANT_EXCHANGE));
            return;
        }

        Collector collector = (Collector)sprite;

        if(collector.getGuild() != player.getGuild()){
            player.send(new ExchangeRequestError(ExchangeRequestError.CANT_EXCHANGE));
            return;
        }

        if(collector.isInExchange()){
            player.send(new ExchangeRequestError(ExchangeRequestError.CANT_EXCHANGE));
            return;
        }

        if(!player.getGuildMember().hasRight(GuildRight.COLLPERCO)){
            player.send(new ExchangeRequestError(ExchangeRequestError.CANT_EXCHANGE));
            return;
        }

        player.setCurExchange(new CollectorExchange(collector, player));
        player.send(new ExchangeCreated(ExchangeType.COLLECTOR));
        player.send(ExchangeList.fromCollector(collector));
    }

    @Override
    public void leave(Player player, ExchangeBase exchange) {
        exchange.cancel();
        Collector collector = ((CollectorExchange)exchange).getCollector();
        playerDAO.save(player);
        guildHandler.recoltCollector(player, collector);
    }

    @Override
    public void validate(Player player, ExchangeBase exchange) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
