/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.peakemu.common.util.Pair;
import org.peakemu.objects.item.Item;
import org.peakemu.world.ItemStorage;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PlayerStore {
    final private ItemStorage items;
    final private Map<Item, Long> prices;

    public PlayerStore(ItemStorage items, Map<Item, Long> prices) {
        this.items = items;
        this.prices = prices;
    }
    
    public Item addItem(Item item, long price){
        Item realItem = items.addItem(item);
        
        if(realItem != null)
            prices.put(realItem, price);
        
        return realItem;
    }
    
    public void removeItem(Item item, int qte){
        items.changeQuantity(item, item.getQuantity() - qte);
        
        if(item.getQuantity() == 0){
            prices.remove(item);
        }
    }
    
    public void setPrice(Item item, long price){
        prices.put(item, price);
    }

    public Pair<Item, Long> getItemSellById(int id){
        Item item = items.get(id);
        
        if(item == null)
            return null;
        
        if(!prices.containsKey(item))
            return null;
        
        long price = prices.get(item);
        
        return new Pair<>(item, price);
    }
    
    public Item getItem(int id){
        return items.get(id);
    }
    
    public long getPrice(Item item){
        return prices.get(item);
    }
    
    public Collection<Pair<Item, Long>> getItems(){
        Collection<Pair<Item, Long>> list = new ArrayList<>(prices.size());
        
        for(Map.Entry<Item, Long> entry : prices.entrySet()){
            list.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        
        return list;
    }
    
    public String getOwner(){
        return items.getOwner();
    }
    
    public boolean isEmpty(){
        return prices.isEmpty();
    }
    
    public long getTotalPrice(){
        long total = 0;
        
        for(Map.Entry<Item, Long> entry : prices.entrySet()){
            total += (entry.getValue() * entry.getKey().getQuantity());
        }
        
        return total;
    }

    public ItemStorage getItemStorage() {
        return items;
    }
}
