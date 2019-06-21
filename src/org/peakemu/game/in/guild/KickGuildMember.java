/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.guild;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.guild.GuildMemberKicked;
import org.peakemu.game.out.guild.GuildUpdateError;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.player.GuildMember;
import org.peakemu.world.enums.GuildRight;
import org.peakemu.world.handler.GuildHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class KickGuildMember implements InputPacket<GameClient>{
    final private GuildHandler guildHandler;

    public KickGuildMember(GuildHandler guildHandler) {
        this.guildHandler = guildHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getGuild() == null)
            return;
        
        GuildMember member = null;
        
        for(GuildMember gm : client.getPlayer().getGuild().getMembers()){
            if(gm.getPlayer().getName().equalsIgnoreCase(args)){
                member = gm;
                break;
            }
        }
        
        if(member == null){
            client.send(new GuildUpdateError(GuildUpdateError.CANT_BANN_FROM_GUILD_NOT_MEMBER));
            return;
        }
        
        if(!client.getPlayer().getGuildMember().hasRight(GuildRight.BAN) && client.getPlayer() != member.getPlayer()){ //not self-kick and no right
            client.send(new GuildUpdateError(GuildUpdateError.NOT_ENOUGHT_RIGHTS_FROM_GUILD));
            return;
        }
        
        if(member.getRank() == 1 && member.getGuild().getSize() > 1){ //can't kick boss on non empty guild
            return;
        }
        
        guildHandler.kickGuild(member);
        
        Object packet = new GuildMemberKicked(client.getPlayer(), member.getPlayer());
        client.send(packet);
        
        if(member.getPlayer().isOnline() && member.getPlayer() != client.getPlayer())
            member.getPlayer().send(packet);
    }

    @Override
    public String header() {
        return "gK";
    }
    
}
