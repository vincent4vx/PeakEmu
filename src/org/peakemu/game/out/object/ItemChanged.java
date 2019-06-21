/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.object;

import org.peakemu.objects.item.Item;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ItemChanged {
    final private Item item;

    public ItemChanged(Item item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "OC|" + item.getPacket();
    }
    
    
}
