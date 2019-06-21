/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.exchange;

import org.peakemu.objects.item.Item;
import org.peakemu.world.ItemTemplate;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CraftSuccess {
    private ItemTemplate item;

    public CraftSuccess(ItemTemplate item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "EcK;" + item.getID();
    }
    
    
}
