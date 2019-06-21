/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects;

import org.peakemu.world.ItemStorage;
import org.peakemu.world.StorageInventory;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Bank implements StorageInventory{
    final private ItemStorage storage;
    private long kamas;

    public Bank(ItemStorage inventory, long kamas) {
        this.storage = inventory;
        this.kamas = kamas;
    }

    @Override
    public ItemStorage getItems() {
        return storage;
    }

    @Override
    public long getKamas() {
        return kamas;
    }

    @Override
    public void setKamas(long kamas) {
        this.kamas = kamas;
    }
    
    public int getCost(){
        return getItems().getItems().size();
    }
}
