/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import org.peakemu.common.util.Pair;
import org.peakemu.objects.item.Item;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Craft {
    final private ItemTemplate item;
    final private Collection<Pair<ItemTemplate, Integer>> ingredients;

    public Craft(ItemTemplate item, Collection<Pair<ItemTemplate, Integer>> ingredients) {
        this.item = item;
        this.ingredients = ingredients;
    }

    public ItemTemplate getItem() {
        return item;
    }

    public Collection<Pair<ItemTemplate, Integer>> getIngredients() {
        return ingredients;
    }
    
    public int getCraftCases(){
        return ingredients.size();
    }
    
    public boolean corresponds(Map<Item, Integer> craft){
        if(craft.size() != ingredients.size())
            return false;
        
        for(Pair<ItemTemplate, Integer> ingredient : ingredients){
            boolean ok = false;
            for(Entry<Item, Integer> entry : craft.entrySet()){
                if(entry.getKey().getTemplate().equals(ingredient.getFirst())
                    && entry.getValue() == ingredient.getSecond()){
                    ok = true;
                    break;
                }
            }
            
            if(!ok)
                return false;
        }
        
        return true;
    }
}
