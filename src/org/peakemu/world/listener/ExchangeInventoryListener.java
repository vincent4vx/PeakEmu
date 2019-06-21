/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.listener;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.exchange.ExchangeUpdateStorage;
import org.peakemu.objects.item.Item;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ExchangeInventoryListener implements InventoryListener{
    final private GameClient client;

    public ExchangeInventoryListener(GameClient client) {
        this.client = client;
    }

    @Override
    public void onRemove(Item item) {
        client.send(new ExchangeUpdateStorage().setAction(ExchangeUpdateStorage.ACTION_REMOVE).setItem(item));
    }

    @Override
    public void onQuantityChange(Item item) {
        client.send(new ExchangeUpdateStorage().setAction(ExchangeUpdateStorage.ACTION_ADD).setItem(item));
    }

    @Override
    public void onAdd(Item item) {
        client.send(new ExchangeUpdateStorage().setAction(ExchangeUpdateStorage.ACTION_ADD).setItem(item));
    }

    @Override
    public void onMove(Item item) {
        throw new UnsupportedOperationException("Can't move on exchange"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onChange(Item item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
