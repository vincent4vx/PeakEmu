/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.common.Constants;
import org.peakemu.common.SocketManager;
import org.peakemu.game.out.exchange.ExchangeUpdateStorage;
import org.peakemu.game.out.exchange.ExchangeLeaved;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Inventory;
import org.peakemu.world.ItemStorage;
import org.peakemu.world.StorageInventory;
import org.peakemu.world.enums.InventoryPosition;
import org.peakemu.world.listener.ExchangeInventoryListener;
import org.peakemu.world.listener.InventoryListener;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
abstract public class AbstractStorageExchange implements StorageExchange{
    final protected Player player;
    final protected StorageInventory distantStorage;
    
    final private InventoryListener inventoryListener;

    public AbstractStorageExchange(Player player, StorageInventory distantStorage) {
        this.player = player;
        this.distantStorage = distantStorage;
        inventoryListener = new ExchangeInventoryListener(player.getAccount().getGameThread());
        distantStorage.getItems().addListener(inventoryListener);
    }

    @Override
    public Inventory getOwnInventory() {
        return player.getItems();
    }

    @Override
    public long getOwnKamas() {
        return player.getKamas();
    }

    @Override
    public void setOwnKamas(long kamas) {
        player.setKamas(kamas);
    }

    @Override
    public void addItem(int itemId, int qte) {
        Item item = getOwnInventory().get(itemId);
        if(item == null || qte < 1 || item.getPosition() != InventoryPosition.NO_EQUIPED){
            player.send(new ExchangeUpdateStorage(false));
            return;
        }
        
        if(qte > item.getQuantity())
            qte = item.getQuantity();
        
        getOwnInventory().changeQuantity(item, item.getQuantity() - qte);
        Item exchangeItem = item.cloneItem(qte);
        getDistantInventory().addItem(exchangeItem);
    }

    @Override
    public void removeItem(int itemId, int qte) {
        Item item = getDistantInventory().get(itemId);
        if(item == null || qte < 1){
            player.send(new ExchangeUpdateStorage(false));
            return;
        }
            
        if(qte > item.getQuantity())
            qte = item.getQuantity();
        
        getDistantInventory().changeQuantity(item, item.getQuantity() - qte);
        Item ownItem = item.cloneItem(qte);
        getOwnInventory().addItem(ownItem);
    }

    @Override
    public void addKamas(long qte) {
        if(getOwnKamas() < qte)
            qte = getOwnKamas();
        
        setDistantKamas(qte + getDistantKamas());
        setOwnKamas(getOwnKamas() - qte);
        
        player.send(new ExchangeUpdateStorage().setKamas(getDistantKamas()));
        SocketManager.GAME_SEND_STATS_PACKET(player);
    }

    @Override
    public void removeKamas(long qte) {
        if(getDistantKamas() < qte)
            qte = getDistantKamas();
        
        setDistantKamas(getDistantKamas() - qte);
        setOwnKamas(getOwnKamas() + qte);
        
        player.send(new ExchangeUpdateStorage().setKamas(getDistantKamas()));
        SocketManager.GAME_SEND_STATS_PACKET(player);
    }

    @Override
    public ItemStorage getDistantInventory() {
        return distantStorage.getItems();
    }

    @Override
    public long getDistantKamas() {
        return distantStorage.getKamas();
    }

    @Override
    public void setDistantKamas(long kamas) {
        distantStorage.setKamas(kamas);
    }

    @Override
    public void cancel() {
        getDistantInventory().removeListener(inventoryListener);
        player.send(new ExchangeLeaved());
    }
}
