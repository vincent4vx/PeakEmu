/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.common.util.Pair;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.exchange.BuyDone;
import org.peakemu.game.out.exchange.ExchangeLeaved;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.objects.player.Seller;
import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PlayerStoreBuyExchange implements BuyExchange{
    final private Seller seller;
    final private Player buyer;

    public PlayerStoreBuyExchange(Seller seller, Player buyer) {
        this.seller = seller;
        this.buyer = buyer;
    }

    @Override
    public void buy(int itemId, int qte) {
        Pair<Item, Long> pair = seller.getStore().getItemSellById(itemId);
        
        if(pair == null){
            buyer.send(new BuyDone(false));
            return;
        }
        
        if(qte > pair.getFirst().getQuantity())
            qte = pair.getFirst().getQuantity();
        
        long price = pair.getSecond() * qte;
        
        if(price > buyer.getKamas()){
            buyer.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(128, price));
            return;
        }
        
        seller.sellItem(pair.getFirst(), qte);
        Item buy = pair.getFirst().cloneItem(qte);
        buyer.getItems().addItem(buy);
        buyer.removeKamas(price);
        buyer.send(new BuyDone(true));
    }

    @Override
    public void cancel() {
        seller.endExchange(this);
        buyer.send(new ExchangeLeaved());
        buyer.setCurExchange(null);
    }

    @Override
    public ExchangeType getType() {
        return ExchangeType.PLAYER_STORE_BUY;
    }

    public Player getBuyer() {
        return buyer;
    }

    public Seller getSeller() {
        return seller;
    }
}
