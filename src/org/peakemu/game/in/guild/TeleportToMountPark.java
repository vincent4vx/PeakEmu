/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.guild;

import org.peakemu.database.dao.MapDAO;
import org.peakemu.database.dao.MountParkDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.MountPark;
import org.peakemu.world.GameMap;
import org.peakemu.world.handler.PlayerHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class TeleportToMountPark implements InputPacket<GameClient>{
    final private MountParkDAO mountParkDAO;
    final private MapDAO mapDAO;
    final private PlayerHandler playerHandler;

    public TeleportToMountPark(MountParkDAO mountParkDAO, MapDAO mapDAO, PlayerHandler playerHandler) {
        this.mountParkDAO = mountParkDAO;
        this.mapDAO = mapDAO;
        this.playerHandler = playerHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        if(client.getPlayer().getGuild() == null){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(135));
            return;
        }
        
        GameMap map;
        
        try{
            map = mapDAO.getMapById(Short.parseShort(args));
        }catch(NumberFormatException e){
            return;
        }
        
        if(map == null){
            return;
        }
        
        MountPark park = mountParkDAO.getMountParkByMap(map);
        
        if(park == null)
            return;
        
        if(!client.getPlayer().getGuild().equals(park.getGuild())){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(100));
            return;
        }
        
        Item item = client.getPlayer().getItems().getItemByTemplate(9035);
        
        if(item == null){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(159));
            return;
        }
        
        client.getPlayer().getItems().changeQuantity(item, item.getQuantity() - 1);
        playerHandler.teleport(client.getPlayer(), map, park.getCell());
    }

    @Override
    public String header() {
        return "gf";
    }
    
}
