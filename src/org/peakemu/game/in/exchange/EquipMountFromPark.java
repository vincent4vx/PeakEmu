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
import org.peakemu.game.out.mount.MountEquiped;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Mount;
import org.peakemu.world.enums.GuildRight;
import org.peakemu.world.exchange.MountParkExchange;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class EquipMountFromPark implements InputPacket<GameClient>{
    final private MountParkDAO mountParkDAO;

    public EquipMountFromPark(MountParkDAO mountParkDAO) {
        this.mountParkDAO = mountParkDAO;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getCurExchange() == null || !(client.getPlayer().getCurExchange() instanceof MountParkExchange))
            return;
        
        MountParkExchange exchange = (MountParkExchange) client.getPlayer().getCurExchange();
        
        if(client.getPlayer().getMount() != null)
            return;
        
        int mountId;
        
        try{
            mountId = Integer.parseInt(args);
        }catch(NumberFormatException e){
            return;
        }
        
        Mount mount = exchange.getMountPark().getMountById(mountId);
        
        if(mount == null){
            return;
        }
        
        if(exchange.getMountPark().isPublic()){
            if(!exchange.getMountPark().getOwner(mount).equals(client.getPlayer()))
                return;
        }else if(!client.getPlayer().getGuildMember().hasRight(GuildRight.OTHDINDE)){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(101));
            return;
        }
        
        exchange.getMountPark().removeMount(mount);
        client.send(new AlterMountPark(AlterMountPark.ACTION_REMOVE, mount));
        mountParkDAO.removeMountFromPark(mount, exchange.getMountPark());
        
        client.getPlayer().setMount(mount);
        client.send(new MountEquiped(mount));
        SocketManager.GAME_SEND_Rx_PACKET(client.getPlayer());   
    }

    @Override
    public String header() {
        return "Erg";
    }
    
}
