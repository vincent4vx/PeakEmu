/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.common.Constants;
import org.peakemu.common.util.Pair;
import org.peakemu.game.out.exchange.ExchangeLeaved;
import org.peakemu.game.out.exchange.ExchangeMoveError;
import org.peakemu.game.out.exchange.PlayerShopUpdated;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.objects.player.PlayerStore;
import org.peakemu.world.enums.ExchangeType;
import org.peakemu.world.enums.InventoryPosition;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PlayerStoreSellExchange implements StoreSellExchange{
    final private Player seller;
    final private PlayerStore store;

    public PlayerStoreSellExchange(Player seller, PlayerStore store) {
        this.seller = seller;
        this.store = store;
    }

    @Override
    public void sellItem(int itemId, int qte, long price) {
        if(qte == 0){ //update price
            Item item = store.getItem(itemId);
            
            if(item == null || price < 1)
                return;
            
            store.setPrice(item, price);
            seller.send(new PlayerShopUpdated(PlayerShopUpdated.ACTION_ADD, item, price));
            return;
        }
        
        Item item = seller.getItems().get(itemId);
        
        if(item == null || qte < 0 || price < 1 || item.getPosition() != InventoryPosition.NO_EQUIPED){
            seller.send(new ExchangeMoveError());
            return;
        }
        
        qte = qte > item.getQuantity() ? item.getQuantity() : qte;
        
        seller.getItems().changeQuantity(item, item.getQuantity() - qte);
        
        Item sold = item.cloneItem(qte);
        sold = store.addItem(sold, price);
        seller.send(new PlayerShopUpdated(PlayerShopUpdated.ACTION_ADD, sold, price));
    }

    @Override
    public void removeSellItem(int itemId, int qte) {
        Pair<Item, Long> item = store.getItemSellById(itemId);
        
        if(item == null || qte < 1){
            seller.send(new ExchangeMoveError());
            return;
        }
        
        if(qte > item.getFirst().getQuantity())
            qte = item.getFirst().getQuantity();
        
        store.removeItem(item.getFirst(), qte);
        seller.getItems().addItem(item.getFirst().cloneItem(qte));
        
        if(item.getFirst().getQuantity() == 0){
            seller.send(new PlayerShopUpdated(PlayerShopUpdated.ACTION_REMOVE, item.getFirst(), 0));
        }else{
            seller.send(new PlayerShopUpdated(PlayerShopUpdated.ACTION_ADD, item.getFirst(), item.getSecond()));
        }
    }

    @Override
    public void cancel() {
        seller.setCurExchange(null);
        seller.send(new ExchangeLeaved());
    }

    @Override
    public ExchangeType getType() {
        return ExchangeType.PLAYER_STORE_SELL;
    }
}
