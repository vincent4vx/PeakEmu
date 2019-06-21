/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.object;

import org.peakemu.common.util.StringUtil;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.maputil.MapUtil;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.item.Item;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class DropObject implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().isAway())
            return;
        
        int id, qte;
        
        try{
            String[] data = StringUtil.split(args, "|", 2);
            id = Integer.parseInt(data[0]);
            qte = Integer.parseInt(data[1]);
        }catch(Exception e){
            return;
        }
        
        Item item = client.getPlayer().getItems().get(id);
        
        if(item == null){
            return;
        }
        
        if(qte > item.getQuantity())
            qte = item.getQuantity();
        
        MapCell target = MapUtil.getNearFreeCell(client.getPlayer().getCell());
        
        if(target == null){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(145));
            return;
        }
        
        Item drop = item.cloneItem(qte);
        client.getPlayer().getItems().changeQuantity(item, item.getQuantity() - qte);
        
        client.getPlayer().getMap().dropItem(target, drop);
    }

    @Override
    public String header() {
        return "OD";
    }
    
}
