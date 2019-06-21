/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.listener;

import org.peakemu.objects.item.Item;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public interface InventoryListener {

    public void onRemove(Item item);

    public void onQuantityChange(Item item);

    public void onAdd(Item item);

    public void onMove(Item item);
    
    public void onChange(Item item);
}
