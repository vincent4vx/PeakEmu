/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects.item.factory;

import org.peakemu.objects.item.Item;
import org.peakemu.objects.item.Weapon;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.StatsTemplate;
import org.peakemu.world.enums.InventoryPosition;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class WeaponFactory implements ItemFactory{

    @Override
    public Item createItem(String owner, int Guid, ItemTemplate template, int qua, InventoryPosition pos, StatsTemplate statsTemplate) {
        return new Weapon(owner, Guid, template, qua, pos, statsTemplate);
    }
    
}
