/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.object;

import org.peakemu.common.SocketManager;
import org.peakemu.common.util.StringUtil;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.object.ObjectDeleteError;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.item.Item;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class DeleteObject implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        String[] data = StringUtil.split(args, "|", 2);
        
        int id;
        
        try{
            id = Integer.parseInt(data[0]);
        }catch(NumberFormatException e){
            client.send(new ObjectDeleteError());
            return;
        }
        
        int qte = 1;
        
        try{
            qte = Integer.parseInt(data[1]);
        }catch(Exception e){}
        
        Item item = client.getPlayer().getItems().get(id);
        
        if(item == null){
            client.send(new ObjectDeleteError());
            return;
        }
        
        client.getPlayer().getItems().changeQuantity(item, item.getQuantity() - qte);
        SocketManager.GAME_SEND_STATS_PACKET(client.getPlayer());
        SocketManager.GAME_SEND_Ow_PACKET(client.getPlayer());
    }

    @Override
    public String header() {
        return "Od";
    }
    
}
