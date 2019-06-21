/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.guild;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.guild.GuildJoinDistantRequested;
import org.peakemu.game.out.guild.GuildJoinRequested;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.guild.JoinGuildResponse;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Guild;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Request;
import org.peakemu.world.enums.GuildRight;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class JoinGuildRequest implements InputPacket<GameClient>{
    final private SessionHandler sessionHandler;

    public JoinGuildRequest(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getGuild() == null)
            return;
        
        Player target = sessionHandler.searchPlayer(args);
        
        if(target == null || !target.isOnline()){
            client.send(new JoinGuildResponse(JoinGuildResponse.GUILD_JOIN_UNKNOW));
            return;
        }
        
        if(target.getGuild() != null){
            client.send(new JoinGuildResponse(JoinGuildResponse.GUILD_JOIN_ALREADY_IN_GUILD));
            return;
        }
        
        if(!client.getPlayer().getGuildMember().hasRight(GuildRight.INVITE)){
            client.send(new JoinGuildResponse(JoinGuildResponse.GUILD_JOIN_NO_RIGHTS));
            return;
        }
        
        if(target.isAway()){
            client.send(new JoinGuildResponse(JoinGuildResponse.GUILD_JOIN_OCCUPED));
            return;
        }
        
        if(client.getPlayer().getGuild().isFull()){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(55, client.getPlayer().getGuild().getMaxMembers()));
            return;
        }
        
        Request request = new Guild.InvitationRequest(client.getPlayer(), target);
        
        client.getPlayer().setRequest(request);
        target.setRequest(request);
        
        client.send(new GuildJoinRequested(target));
        target.send(new GuildJoinDistantRequested(client.getPlayer()));
    }

    @Override
    public String header() {
        return "gJR";
    }
    
}
