/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects.item;

import org.peakemu.world.ItemTemplate;
import org.peakemu.world.StatsTemplate;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.InventoryPosition;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class BuffItem extends Item{

    public BuffItem(String owner, int Guid, ItemTemplate template, int qua, InventoryPosition pos, StatsTemplate statsTemplate) {
        super(owner, Guid, template, qua, pos, statsTemplate);
    }
    
    public boolean isValid(){
        StatsTemplate.StatEntry entry = statsTemplate.findFirstEntry(Effect.ADD_TOURS);
        
        if(entry == null) //not limited by turns
            return true;
        
        return entry.getSpecial() > 0;
    }
    
    public void setTurns(int turns){
        statsTemplate.setEntry(new StatsTemplate.StatEntry(Effect.ADD_TOURS, 0, 0, turns, ""));
    }
    
    public boolean decrementTurns(){
        StatsTemplate.StatEntry entry = statsTemplate.findFirstEntry(Effect.ADD_TOURS);
        
        if(entry == null)
            return true;
        
        int newVal = entry.getSpecial() - 1;
        setTurns(newVal);
        
        return newVal > 0;
    }
}
