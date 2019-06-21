/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.object;

import java.util.Set;
import org.peakemu.world.ItemSet;
import org.peakemu.world.ItemTemplate;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class EquipedItemSet {
    private ItemSet itemSet;
    private Set<ItemTemplate> equiped;

    public EquipedItemSet(ItemSet itemSet, Set<ItemTemplate> equiped) {
        this.itemSet = itemSet;
        this.equiped = equiped;
    }

    public ItemSet getItemSet() {
        return itemSet;
    }

    public void setItemSet(ItemSet itemSet) {
        this.itemSet = itemSet;
    }

    public Set<ItemTemplate> getEquiped() {
        return equiped;
    }

    public void setEquiped(Set<ItemTemplate> equiped) {
        this.equiped = equiped;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        
        sb.append("OS");
        
        if(equiped.isEmpty())
            sb.append('-');
        else 
            sb.append('+');
        
        sb.append(itemSet.getId());
        
        if(!equiped.isEmpty()){
            sb.append('|');
            
            boolean f = false;
            for (ItemTemplate it : equiped) {
                if(f){
                    sb.append(';');
                }else{
                    f = true;
                }
                
                sb.append(it.getID());
            }
            
            sb.append('|').append(itemSet.getBonusStatByItemNumb(equiped.size()).parseToItemSetStats());
        }
        
        return sb.toString();
    }
    
    
}
