/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.objects.player.Player;
import org.peakemu.world.Inventory;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
abstract public class ExchangeableInventory {
    final private Inventory inventory;
    private long kamas;

    public ExchangeableInventory(Inventory inventory, long kamas) {
        this.inventory = inventory;
        this.kamas = kamas;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public long getKamas() {
        return kamas;
    }

    public void setKamas(long kamas) {
        this.kamas = kamas;
    }
    
    abstract public StorageExchange createExchange(Player player);
}
