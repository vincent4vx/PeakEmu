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
import org.peakemu.objects.Guild;
import org.peakemu.objects.MountPark;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.GuildHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class BuyMountPark implements InputPacket<GameClient>{
    final private MountParkDAO mountParkDAO;
    final private GuildHandler guildHandler;

    public BuyMountPark(MountParkDAO mountParkDAO, GuildHandler guildHandler) {
        this.mountParkDAO = mountParkDAO;
        this.guildHandler = guildHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        if(client.getPlayer().getDeshonor() >= 5){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(83));
            return;
        }
        
        MountPark park = mountParkDAO.getMountParkByMap(client.getPlayer().getMap());
        
        if(park == null){
            return;
        }
        
        if(park.isPublic()){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(96));
            return;
        }
        
        if(park.getPrice() <= 0){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(97));
            return;
        }
        
        if(client.getPlayer().getGuild() == null){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(135));
            return;
        }
        
        if(client.getPlayer().getGuildMember().getRank() != 1){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(98));
            return;
        }
        
        if(client.getPlayer().getKamas() < park.getPrice()){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(128));
            return;
        }
        
        if(client.getPlayer().getMount() == null){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(119));
            return;
        }
        
        Guild guild = client.getPlayer().getGuild();
        
        if(guildHandler.getMountParks(guild).size() >= guild.getMaxMountParks()){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(103));
            return;
        }
        
        if(park.getGuild() != null){
            Player seller = park.getGuild().getLeader();
            seller.getAccount().getBank().setKamas(seller.getAccount().getBank().getKamas() + park.getPrice());
            park.setGuild(null);
        }
        
        client.getPlayer().removeKamas(park.getPrice());
        park.setGuild(guild);
        park.setPrice(0);
        
        mountParkDAO.save(park);
        client.getPlayer().getMap().sendToMap(new AddMountPark(park));
        client.send(new MountParkBuyLeaved());
    }

    @Override
    public String header() {
        return "Rb";
    }
    
}
