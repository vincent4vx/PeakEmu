/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.SocketManager;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.enums.InventoryPosition;
import org.peakemu.world.enums.ItemType;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Candy implements ActionPerformer {

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        if(item.getType() != ItemType.BONBON){
            Peak.worldLog.addToLog(Logger.Level.ERROR, "%s not a Candy", item.getTemplate().getName());
            return false;
        }
        
        InventoryPosition position = caster.getItems().getFreeBuffPosition();
        
        if(position == null){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "No free place available");
            return false;
        }
        
        if(caster.hasEquiped(item.getTemplate())){
            return false;
        }
        
        caster.getItems().moveItem(item, position, 1);
        SocketManager.GAME_SEND_STATS_PACKET(caster);
        
        return true;
    }

    @Override
    public int actionId() {
        return -10;
    }

}
