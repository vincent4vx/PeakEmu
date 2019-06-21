/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.guild;

import org.peakemu.common.Constants;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.guild.CollectorAttackers;
import org.peakemu.game.out.guild.CollectorDefenders;
import org.peakemu.game.out.guild.GuildBoostInfo;
import org.peakemu.game.out.guild.GuildInfoGeneral;
import org.peakemu.game.out.guild.GuildInfoMembers;
import org.peakemu.game.out.guild.InfosTaxCollectorsMovement;
import org.peakemu.game.out.guild.ListGuildMountParks;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Collector;
import org.peakemu.objects.Guild;
import org.peakemu.world.handler.ExpHandler;
import org.peakemu.world.handler.GuildHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GuildInfos implements InputPacket<GameClient>{
    final private GuildHandler guildHandler;
    final private ExpHandler expHandler;

    public GuildInfos(GuildHandler guildHandler, ExpHandler expHandler) {
        this.guildHandler = guildHandler;
        this.expHandler = expHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        Guild guild = client.getPlayer().getGuild();
        
        if(guild == null)
            return;
        
        switch(args.charAt(0)){
            case 'M':
                client.send(new GuildInfoMembers(guild));
                break;
            case 'B':
                client.send(new GuildBoostInfo(guild));
                break;
            case 'T':
                client.send(new InfosTaxCollectorsMovement(guild));
                for(Collector collector : guild.getCollectors()){
                    if(collector.getFight() != null && collector.getFight().getState() == Constants.FIGHT_STATE_PLACE){
                        client.send(new CollectorAttackers(collector));
                        client.send(new CollectorDefenders(collector));
                    }
                }
//                Collector.parseAttaque(client, client.getGuild().getId());
//                Collector.parseDefense(client, client.getGuild().getId());
                break;
            case 'F':
                client.send(new ListGuildMountParks(guild, guildHandler.getMountParks(guild)));
                break;
            case 'G':
                client.send(new GuildInfoGeneral(guild, expHandler.getLevel(guild.getLevel())));
                break;
        }
    }

    @Override
    public String header() {
        return "gI";
    }
    
}
