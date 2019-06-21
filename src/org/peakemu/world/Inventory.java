/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.objects.item.BuffItem;
import org.peakemu.objects.item.Item;
import org.peakemu.world.enums.InventoryPosition;
import org.peakemu.world.listener.InventoryListener;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Inventory extends ItemStorage{
    final private Map<InventoryPosition, Item> itemsByPos = new EnumMap<>(InventoryPosition.class);

    public Inventory(String owner) {
        super(owner);
    }
    
    /**
     * Add or stack an item into inventory
     * @param newItem
     * @return The real item in inventory (newItem if can't stack, or stacked item)
     */
    @Override
    public Item addItem(Item newItem){
        Item item = super.addItem(newItem);
        
        if(newItem.getPosition() != InventoryPosition.NO_EQUIPED && item.equals(newItem)){
            itemsByPos.put(newItem.getPosition(), newItem);
        }
        
        return item;
    }
    
    public Collection<Item> getEquipedItems(){
        Collection<Item> items = new ArrayList<>(InventoryPosition.STUFF_POSITIONS.length);
        
        for(InventoryPosition position : InventoryPosition.STUFF_POSITIONS){
            Item item = getItemByPos(position);
            
            if(item != null)
                items.add(item);
        }
        
        return items;
    }
    
    public Collection<BuffItem> getBuffItems(){
        Collection<BuffItem> items = new ArrayList<>();
        
        for(InventoryPosition position : InventoryPosition.BUFF_POSITIONS){
            Item item = getItemByPos(position);
            
            if(item == null)
                continue;
            
            BuffItem buffItem = (BuffItem)item;
            
            if(!buffItem.isValid())
                remove(item);
            else
                items.add(buffItem);
        }
        
        return items;
    }
    
    @Override
    public Item remove(int id){
        Item item = super.remove(id);
        
        if(item != null && item.getPosition() != InventoryPosition.NO_EQUIPED){
            itemsByPos.remove(item.getPosition());
        }
        
        return item;
    }
    
    @Override
    public Item remove(Item item){
        return remove(item.getGuid());
    }
    
    public Item getItemByPos(InventoryPosition pos){
        if(pos == InventoryPosition.NO_EQUIPED)
            return null;
        
        return itemsByPos.get(pos);
    }
    
    public boolean isEquiped(int id){
        Item item = get(id);
        
        if(item == null)
            return false;
        
        return item.getPosition() != InventoryPosition.NO_EQUIPED;
    }
    
    public Item unequipItem(InventoryPosition position){
        if(!itemsByPos.containsKey(position))
            return null;
        
        Item item = itemsByPos.remove(position);
        
        Item other = getSimilarItem(item);
        
        if(other != null){ //same item found
            changeQuantity(other, other.getQuantity() + item.getQuantity()); //stack
            remove(item); //remove old item
            return item;
        }
        
        changePosition(item, InventoryPosition.NO_EQUIPED);
        return item;
    }
    
    private void changePosition(Item item, InventoryPosition position){
        itemsByPos.remove(item.getPosition());
        
        if(!position.isValidPosition(item.getType()))
            item.setPosition(InventoryPosition.NO_EQUIPED);
            
        item.setPosition(position);
        
        if(position != InventoryPosition.NO_EQUIPED){
            itemsByPos.put(position, item);
        }
        
        for(InventoryListener listener : listeners){
            listener.onMove(item);
        }
    }
    
    public boolean moveItem(Item item, InventoryPosition newPos, int quantity){
        if(itemsByPos.get(newPos) != null){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Position %s is not empty (%s)", newPos, itemsByPos.get(newPos));
            return false;
        }
        
        if(!newPos.isValidPosition(item.getType())){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Invalid position %s for %s", newPos, item);
            return false;
        }
        
        if(quantity > item.getQuantity())
            quantity = item.getQuantity();
        
        if(newPos == InventoryPosition.NO_EQUIPED){
            Item other = getSimilarItem(item);
            if(other != null){ //stack item
                changeQuantity(other, other.getQuantity() + quantity);
                changeQuantity(item, item.getQuantity() - quantity);
                return true;
            }
        }
        
        if(item.getQuantity() == quantity){ //move all items
            changePosition(item, newPos);
            return true;
        }
        
        //partial move
        Item newItem = item.cloneItem(quantity); //create a new item
        newItem.setPosition(newPos);
        newItem.setOwner(getOwner());
        newItem.setGuid(getNextItemId());
        changeQuantity(item, item.getQuantity() - quantity);
        addItem(newItem);
        
        return true;
    }
    
    public Map<ItemSet, Set<ItemTemplate>> getEquipedItemSets(){
        Map<ItemSet, Set<ItemTemplate>> itemSets = new HashMap<>();
        
        for(Item item : getEquipedItems()){
            ItemSet is = item.getTemplate().getItemSet();
            
            if(is == null)
                continue;
            
            if(!itemSets.containsKey(is)){
                itemSets.put(is, new HashSet<>());
            }
            
            itemSets.get(is).add(item.getTemplate());
        }
        
        return itemSets;
    }
    
    public Set<ItemTemplate> getEquipedItemSetTemplates(ItemSet itemSet){
        Set<ItemTemplate> itemTemplates = new HashSet<>();
        
        for(Item item : getEquipedItems()){
            if(itemSet.equals(item.getTemplate().getItemSet())){
                itemTemplates.add(item.getTemplate());
            }
        }
        
        return itemTemplates;
    }
    
    public boolean isFreePosition(InventoryPosition position){
        return !itemsByPos.containsKey(position);
    }
    
    public InventoryPosition getFreeBuffPosition(){
        for(InventoryPosition p : InventoryPosition.BUFF_POSITIONS){
            if(isFreePosition(p))
                return p;
        }
        
        return null;
    }
    
    public void decrementBuffTurns(){
        for(InventoryPosition position : InventoryPosition.BUFF_POSITIONS){
            BuffItem buff = (BuffItem)getItemByPos(position);
            
            if(buff == null)
                continue;
            
            if(!buff.decrementTurns()){
                remove(buff);
            }else{
                for(InventoryListener listener : listeners){
                    listener.onChange(buff);
                }
            }
        }
    }
    
    public boolean addBuffItem(BuffItem item){
        InventoryPosition position = getFreeBuffPosition();
        
        if(position == null)
            return false;
        
        item.setOwner(getOwner());
        item.setGuid(getNextItemId());
        item.setPosition(position);
        
        addItem(item);
        
        return true;
    }
    
    public boolean isEquiped(ItemTemplate template){
        for(Item item : itemsByPos.values()){
            if(item.getTemplate().equals(template))
                return true;
        }
        
        return false;
    }
}
