/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.mount;

import org.peakemu.database.dao.MountParkDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.mount.AddMountPark;
import org.peakemu.game.out.mount.MountParkBuyLeaved;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.MountPark;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class UpdateMountParkPrice implements InputPacket<GameClient>{
    final private MountParkDAO mountParkDAO;

    public UpdateMountParkPrice(MountParkDAO mountParkDAO) {
        this.mountParkDAO = mountParkDAO;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getGuild() == null)
            return;
        
        Player player = client.getPlayer();
        MountPark park = mountParkDAO.getMountParkByMap(client.getPlayer().getMap());
        
        if(park == null)
            return;
        
        client.send(new MountParkBuyLeaved());
        
        if(park.isPublic()){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(94));
            return;
        }
        
        if(!player.getGuild().equals(park.getGuild())){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(95));
            return;
        }
        
        if(player.getGuildMember().getRank() != 1){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(95));
            return;
        }
        
        long price;
        
        try{
            price = Long.parseLong(args);
        }catch(NumberFormatException e){
            return;
        }
        
        if(price < 0){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(99));
            return;
        }
        
        park.setPrice(price);
        player.getMap().sendToMap(new AddMountPark(park));
        mountParkDAO.save(park);
    }

    @Override
    public String header() {
        return "Rs";
    }
    
}
