/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.info;

import org.peakemu.objects.player.Player;
import org.peakemu.world.ItemTemplate;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CraftedObject {
    private Player crafter;
    private ItemTemplate item;
    private boolean success;

    public CraftedObject(Player crafter, ItemTemplate item, boolean success) {
        this.crafter = crafter;
        this.item = item;
        this.success = success;
    }

    @Override
    public String toString() {
        return "IO" + crafter.getId() + "|" + (success ? "+" : "-") + item.getID();
    }
    
    
    
}
