/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.guild;

import org.peakemu.common.util.StringUtil;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.guild.GuildCreationError;
import org.peakemu.game.out.guild.GuildStats;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Guild;
import org.peakemu.world.handler.GuildHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CreateGuild implements InputPacket<GameClient> {
    final private GuildHandler guildHandler;

    public CreateGuild(GuildHandler guildHandler) {
        this.guildHandler = guildHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if (client.getPlayer() == null) {
            return;
        }
        
        if (client.getPlayer().getGuildMember() != null) {
            client.send(new GuildCreationError(GuildCreationError.GUILD_CREATE_ALLREADY_IN_GUILD));
            return;
        }
        
        if (client.getPlayer().getFight() != null || client.getPlayer().isAway()) {
            return;
        }
        
        String[] infos = StringUtil.split(args, "|", 5);
        //base 10 => 36
        String bgID = Integer.toString(Integer.parseInt(infos[0]), 36);
        String bgCol = Integer.toString(Integer.parseInt(infos[1]), 36);
        String embID = Integer.toString(Integer.parseInt(infos[2]), 36);
        String embCol = Integer.toString(Integer.parseInt(infos[3]), 36);
        String name = infos[4];
        
        Guild guild = guildHandler.createGuild(client.getPlayer(), bgID, bgCol, embID, embCol, name);
        
        if(guild == null || client.getPlayer().getGuildMember() == null) //error
            return;
        
        client.send(new GuildStats(client.getPlayer().getGuildMember()));
    }

    @Override
    public String header() {
        return "gC";
    }

}
