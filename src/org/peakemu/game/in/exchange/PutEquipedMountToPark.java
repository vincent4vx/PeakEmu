/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.exchange;

import org.peakemu.common.SocketManager;
import org.peakemu.database.dao.MountParkDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.exchange.AlterMountPark;
import org.peakemu.game.out.mount.MountUnequiped;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Mount;
import org.peakemu.world.exchange.MountParkExchange;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PutEquipedMountToPark implements InputPacket<GameClient>{
    final private MountParkDAO mountParkDAO;

    public PutEquipedMountToPark(MountParkDAO mountParkDAO) {
        this.mountParkDAO = mountParkDAO;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getCurExchange() == null || !(client.getPlayer().getCurExchange() instanceof MountParkExchange))
            return;
        
        MountParkExchange exchange = (MountParkExchange) client.getPlayer().getCurExchange();
        
        int mountId;
        
        try{
            mountId = Integer.parseInt(args);
        }catch(NumberFormatException e){
            return;
        }
        
        if(client.getPlayer().getMount() == null || client.getPlayer().getMount().getId() != mountId){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(104));
            return;
        }
        
        if(client.getPlayer().isOnMount()){
            client.getPlayer().toggleOnMount();
        }
        
        Mount mount = client.getPlayer().getMount();
        
        if(!mount.getItems().isEmpty()){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(106));
            return;
        }
        
        if(exchange.getMountPark().containsMount(mount))
            return;
        
        client.getPlayer().setMount(null);
        
        exchange.getMountPark().addMount(mount, client.getPlayer());
        mountParkDAO.addMountInPark(mount, exchange.getMountPark(), client.getPlayer());
        client.send(new AlterMountPark(AlterMountPark.ACTION_ADD, mount));
        
        client.send(new MountUnequiped());
        SocketManager.GAME_SEND_Rx_PACKET(client.getPlayer());
    }

    @Override
    public String header() {
        return "Erp";
    }
    
}
