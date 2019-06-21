/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.listener;

import java.util.Objects;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.object.EquipedItemSet;
import org.peakemu.game.out.object.InventoryWeight;
import org.peakemu.game.out.object.ItemAdd;
import org.peakemu.game.out.object.ItemMove;
import org.peakemu.game.out.object.ItemQuantity;
import org.peakemu.game.out.object.ItemRemove;
import org.peakemu.game.out.object.ItemChanged;
import org.peakemu.objects.item.Item;
import org.peakemu.world.enums.InventoryPosition;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class OutputInventoryListener implements InventoryListener{
    final private GameClient client;

    public OutputInventoryListener(GameClient client) {
        this.client = client;
    }

    @Override
    public void onRemove(Item item) {
        client.send(new ItemRemove(item));
        client.send(new InventoryWeight(client.getPlayer()));
    }

    @Override
    public void onQuantityChange(Item item) {
        client.send(new ItemQuantity(item));
        client.send(new InventoryWeight(client.getPlayer()));
    }

    @Override
    public void onAdd(Item item) {
        client.send(new ItemAdd(item));
        
        if (item.getPosition() != InventoryPosition.NO_EQUIPED && item.getTemplate().getItemSet() != null) {
            client.send(new EquipedItemSet(
                item.getTemplate().getItemSet(), 
                client.getPlayer().getItems().getEquipedItemSetTemplates(item.getTemplate().getItemSet())
            ));
        }
        
        client.send(new InventoryWeight(client.getPlayer()));
    }

    @Override
    public void onMove(Item item) {
        client.send(new ItemMove(item));
        
        //check move itemset
        if (item.getTemplate().getItemSet() != null) {
            client.send(new EquipedItemSet(
                item.getTemplate().getItemSet(), 
                client.getPlayer().getItems().getEquipedItemSetTemplates(item.getTemplate().getItemSet())
            ));
        }
        
        client.send(new InventoryWeight(client.getPlayer()));
    }

    @Override
    public void onChange(Item item) {
        client.send(new ItemChanged(item));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.client);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OutputInventoryListener other = (OutputInventoryListener) obj;
        if (!Objects.equals(this.client, other.client)) {
            return false;
        }
        return true;
    }
}
