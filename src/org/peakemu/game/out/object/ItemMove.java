/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.object;

import org.peakemu.objects.item.Item;
import org.peakemu.world.enums.InventoryPosition;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ItemMove {
    private Item item;

    public ItemMove(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public String toString() {
        String packet = "OM" + item.getGuid() + "|";
        
        if(item.getPosition() != InventoryPosition.NO_EQUIPED)
            packet += item.getPosition().getId();
        
        return packet;
    }
    
    
}
