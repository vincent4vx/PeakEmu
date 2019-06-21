/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.exchange;

import org.peakemu.database.dao.MountParkDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.exchange.AlterMountPark;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Mount;
import org.peakemu.objects.item.Item;
import org.peakemu.world.MountTemplate;
import org.peakemu.world.config.MountConfig;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.exchange.MountParkExchange;
import org.peakemu.world.handler.MountHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PutCertifToMountPark implements InputPacket<GameClient>{
    final private MountConfig config;
    final private MountParkDAO mountParkDAO;
    final private MountHandler mountHandler;

    public PutCertifToMountPark(MountConfig config, MountParkDAO mountParkDAO, MountHandler mountHandler) {
        this.config = config;
        this.mountParkDAO = mountParkDAO;
        this.mountHandler = mountHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getCurExchange() == null || !(client.getPlayer().getCurExchange() instanceof MountParkExchange))
            return;
        
        MountParkExchange exchange = (MountParkExchange) client.getPlayer().getCurExchange();
        
        if(exchange.getMountPark().isFull()){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(107));
            return;
        }
        
        int itemId;
        
        try{
            itemId = Integer.parseInt(args);
        }catch(NumberFormatException e){
            return;
        }
        
        Item item = client.getPlayer().getItems().get(itemId);
        
        if(item == null){
            return;
        }
        
        int mountId = item.getStats().getEffect(Effect.MOUNT);
        Mount mount = mountHandler.getMountById(mountId);
        
        if(mount == null){
            if(!config.getAllowCreateMountWithFakeCertif()){
                client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(108));
                return;
            }
            
            MountTemplate template = mountHandler.getMountTemplateByParchment(item.getTemplate());
            
            if(template == null)
                return;
            
            mount = mountHandler.createMount(template);
            
            if(mount == null)
                return;
        }
        
        if(exchange.getMountPark().containsMount(mount))
            return;
        
        client.getPlayer().getItems().remove(item);
        exchange.getMountPark().addMount(mount, client.getPlayer());
        mountParkDAO.addMountInPark(mount, exchange.getMountPark(), client.getPlayer());
        client.send(new AlterMountPark(AlterMountPark.ACTION_ADD, mount));
    }

    @Override
    public String header() {
        return "ErC";
    }
    
}
