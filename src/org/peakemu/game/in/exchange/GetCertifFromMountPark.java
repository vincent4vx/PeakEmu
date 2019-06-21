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
import org.peakemu.objects.item.Item;
import org.peakemu.objects.Mount;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.GuildRight;
import org.peakemu.world.exchange.MountParkExchange;
import org.peakemu.world.handler.ItemHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GetCertifFromMountPark implements InputPacket<GameClient>{
    final private MountParkDAO mountParkDAO;
    final private ItemHandler itemHandler;

    public GetCertifFromMountPark(MountParkDAO mountParkDAO, ItemHandler itemHandler) {
        this.mountParkDAO = mountParkDAO;
        this.itemHandler = itemHandler;
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
        
        Mount mount = exchange.getMountPark().getMountById(mountId);
        
        if(mount == null)
            return;
        
        if(exchange.getMountPark().isPublic()){
            if(!exchange.getMountPark().getOwner(mount).equals(client.getPlayer()))
                return;
        }else if(!client.getPlayer().getGuildMember().hasRight(GuildRight.OTHDINDE)){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(101));
            return;
        }
        
        exchange.getMountPark().removeMount(mount);
        mountParkDAO.removeMountFromPark(mount, exchange.getMountPark());
        client.send(new AlterMountPark(AlterMountPark.ACTION_REMOVE, mount));
        
        ItemTemplate tpl = mount.getTemplate().getParchment();
        
        if(tpl == null)
            return;
        
        Item item = itemHandler.createNewItem(tpl, 1, false);
        item.getStatsTemplate().setSimpleStat(Effect.MOUNT, mountId);
        item.getStatsTemplate().setText(Effect.MOUNT_OWNER, client.getPlayer().getName());
        item.getStatsTemplate().setText(Effect.MOUNT_NAME, mount.getName());
        client.getPlayer().getItems().addItem(item);
    }

    @Override
    public String header() {
        return "Erc";
    }
    
}
