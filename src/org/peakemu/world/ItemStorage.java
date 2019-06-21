/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.peakemu.objects.item.Item;
import org.peakemu.world.enums.InventoryPosition;
import org.peakemu.world.listener.InventoryListener;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ItemStorage implements Iterable<Item>{
    final private Map<Integer, Item> items = new HashMap<>();
    final private String owner;
    
    final protected Collection<InventoryListener> listeners = new HashSet<>();
    
    private int nextItemId = 0;

    public ItemStorage(String owner) {
        this.owner = owner;
    }
    
    /**
     * Add or stack an item into inventory
     * @param newItem
     * @return The real item in inventory (newItem if can't stack, or stacked item)
     */
    public Item addItem(Item newItem){
        if(newItem.getQuantity() < 1)
            return null;
        
        if(!owner.equals(newItem.getOwner()) || newItem.getGuid() == -1){ //not same owner or not in db
            newItem.setOwner(owner); //set to correct values
            newItem.setPosition(InventoryPosition.NO_EQUIPED); //force no equiped
            newItem.setGuid(-1);
        }
        
        if(!newItem.getPosition().isValidPosition(newItem.getType()))
            newItem.setPosition(InventoryPosition.NO_EQUIPED);
        
        //try to stack item
        if(newItem.getPosition() == InventoryPosition.NO_EQUIPED){
            Item oldItem = getSimilarItem(newItem);

            if(oldItem != null){
                changeQuantity(oldItem, oldItem.getQuantity() + newItem.getQuantity());
                return oldItem;
            }
        }
        
        if(newItem.getGuid() == -1){ //new item
            newItem.setGuid(getNextItemId());
        }else if(newItem.getGuid() >= nextItemId){
            nextItemId = newItem.getGuid() + 1;
        }
        
        //can't stack => add item in inventory
        items.put(newItem.getGuid(), newItem);
        
        for(InventoryListener listener : listeners){
            listener.onAdd(newItem);
        }
        
        return newItem;
    }
    
    protected int getNextItemId(){
        return nextItemId++;
    }
    
    public void changeQuantity(Item item, int newQuantity){
        if(newQuantity <= 0){
            item.setQuantity(0);
            remove(item);
            return;
        }
        
        item.setQuantity(newQuantity);
        for(InventoryListener listener : listeners){
            listener.onQuantityChange(item);
        }
    }
    
    public Item get(int id){
        return items.get(id);
    }

    @Override
    public Iterator<Item> iterator() {
        return items.values().iterator();
    }
    
    public Collection<Item> getItems(){
        return items.values();
    }
    
    public Item remove(int id){
        Item item = items.get(id);
        
        if(item == null)
            return null;
        
        items.remove(id);
        
        for(InventoryListener listener : listeners){
            listener.onRemove(item);
        }
        
        return item;
    }
    
    public Item remove(Item item){
        return remove(item.getGuid());
    }
    
    public boolean containsItemId(int id){
        return items.containsKey(id);
    }

    public String getOwner() {
        return owner;
    }
    
    public Item getSimilarItem(Item item){
        for(Item other : items.values()){
            if(other.canStack(item)){
                return other;
            }
        }
        
        return null;
    }
    
    public boolean hasSimilarItem(Item item){
        return getSimilarItem(item) != null;
    }
    
    public void addListener(InventoryListener listener){
        listeners.add(listener);
    }
    
    public void removeListener(InventoryListener listener){
        listeners.remove(listener);
    }
    
    public Item getItemByTemplate(int template){
        for(Item item : getItems()){
            if(item.getTemplate().getID() == template)
                return item;
        }
        
        return null;
    }
    
    public void removeByTemplateId(int template, int qte){
        Item item = getItemByTemplate(template);
        
        if(item == null)
            return;
        
        changeQuantity(item, item.getQuantity() - qte);
    }
    
    public int getUsedPods(){
        int pods = 0;
        
        for(Item item : getItems()){
            pods += item.getQuantity() * item.getTemplate().getPod();
        }
        
        return pods;
    }
    
    public boolean isEmpty(){
        return items.isEmpty();
    }
}
