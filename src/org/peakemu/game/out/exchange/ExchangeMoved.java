/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.exchange;

import org.peakemu.objects.item.Item;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ExchangeMoved {
    private char type;
    private long kamas;
    private Item item;
    private int itemQte;

    public ExchangeMoved(long kamas) {
        this.kamas = kamas;
        type = 'G';
    }

    public ExchangeMoved(Item item, int itemQte) {
        this.item = item;
        this.itemQte = itemQte;
        type = 'O';
    }

    @Override
    public String toString() {
        if(type == 'G')
            return "EMKG" + kamas;
        
        if(itemQte > 0)
            return "EMKO+" + item.getGuid() + "|" + itemQte;
        else
            return "EMKO-" + item.getGuid();
    }
}
