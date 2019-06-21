/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.guild;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.guild.GuildStats;
import org.peakemu.game.out.guild.JoinGuildResponse;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Guild;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Request;
import org.peakemu.world.handler.GuildHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class JoinGuildAccept implements InputPacket<GameClient>{
    final private GuildHandler guildHandler;

    public JoinGuildAccept(GuildHandler guildHandler) {
        this.guildHandler = guildHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getGuild() != null)
            return;
        
        Request request = client.getPlayer().getRequest();
        
        if(request == null || !(request instanceof Guild.InvitationRequest))
            return;
        
        if(!request.getTarget().equals(client.getPlayer())) //only target can accept
            return;
        
        guildHandler.joinGuild(client.getPlayer(), request.getPerformer().getGuild());
        
        client.send(new GuildStats(client.getPlayer().getGuildMember()));
        request.accept();
    }

    @Override
    public String header() {
        return "gJK";
    }
    
}
