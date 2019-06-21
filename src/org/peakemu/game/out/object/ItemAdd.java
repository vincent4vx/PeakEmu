/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.object;

import org.peakemu.common.Constants;
import org.peakemu.database.parser.StatsParser;
import org.peakemu.objects.item.Item;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ItemAdd {
    private Item item;

    public ItemAdd(Item item) {
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
        return "OAKO" + item.getPacket();
    }
    
    
}
