/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level4;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.SocketManager;
import org.peakemu.objects.item.Item;
import org.peakemu.world.enums.InventoryPosition;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class RemAllBuffs implements Command{

    @Override
    public String name() {
        return "REMALLBUFFS";
    }

    @Override
    public String shortDescription() {
        return "[DEBUG] supprime tout les items de buff";
    }

    @Override
    public String help() {
        return "REMALLBUFFS";
    }

    @Override
    public int minLevel() {
        return 4;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        for(InventoryPosition position : InventoryPosition.BUFF_POSITIONS){
            Item item = performer.getPlayer().getItems().getItemByPos(position);
            
            if(item != null)
                performer.getPlayer().getItems().remove(item);
        }
        
        performer.getPlayer().refreshStats();
        SocketManager.GAME_SEND_STATS_PACKET(performer.getPlayer());
    }
    
}
