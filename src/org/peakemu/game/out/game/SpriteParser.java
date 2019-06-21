/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import org.peakemu.objects.item.Item;
import org.peakemu.world.Inventory;
import org.peakemu.world.enums.InventoryPosition;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SpriteParser {
    final static private InventoryPosition[] STUFF_POSITIONS = new InventoryPosition[]{
        InventoryPosition.ARME, InventoryPosition.COIFFE, InventoryPosition.CAPE,
        InventoryPosition.FAMILIER, InventoryPosition.BOUCLIER
    };
    
    static public String stuffToString(Inventory inventory){
        StringBuilder sb = new StringBuilder();
        
        boolean b = false;
        for(InventoryPosition position : STUFF_POSITIONS){
            if(b)
                sb.append(',');
            else
                b = true;
            
            Item item = inventory.getItemByPos(position);
            
            if(item != null)
                sb.append(Integer.toHexString(item.getTemplate().getID()));
        }
        
        return sb.toString();
    }
    
    static public boolean isVisibleStuffPosition(InventoryPosition position){
        for (InventoryPosition pos : STUFF_POSITIONS) {
            if(pos == position)
                return true;
        }
        
        return false;
    }
}
