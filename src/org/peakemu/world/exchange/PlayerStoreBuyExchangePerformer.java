/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.game.out.exchange.ExchangeCreated;
import org.peakemu.game.out.exchange.PlayerShopList;
import org.peakemu.objects.player.Player;
import org.peakemu.objects.player.Seller;
import org.peakemu.world.Sprite;
import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PlayerStoreBuyExchangePerformer implements ExchangePerformer{
    final private PlayerDAO playerDAO;

    public PlayerStoreBuyExchangePerformer(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Override
    public ExchangeType getExchangeType() {
        return ExchangeType.PLAYER_STORE_BUY;
    }

    @Override
    public void request(Player player, String[] args) {
        if(player.isAway())
            return;
        
        int sellerId = Integer.parseInt(args[1]);
        
        Sprite sprite = player.getMap().getSprite(sellerId);
        
        if(sprite == null || !(sprite instanceof Seller))
            return;
        
        Seller seller = (Seller)sprite;
        
        player.setCurExchange(seller.startExchange(player));
        player.send(new ExchangeCreated(ExchangeType.PLAYER_STORE_BUY));
        player.send(new PlayerShopList(seller.getStore()));
    }

    @Override
    public void leave(Player player, ExchangeBase exchange) {
        exchange.cancel();
        playerDAO.save(player);
        PlayerStoreBuyExchange buyExchange = (PlayerStoreBuyExchange) exchange;
        playerDAO.save(buyExchange.getSeller().getPlayer());
    }

    @Override
    public void validate(Player player, ExchangeBase exchange) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
