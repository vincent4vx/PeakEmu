/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.guild;

import org.peakemu.database.dao.GuildDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.guild.GuildBoostInfo;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Guild;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.GuildRight;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class BoostGuild implements InputPacket<GameClient> {
    final private GuildDAO guildDAO;

    public BoostGuild(GuildDAO guildDAO) {
        this.guildDAO = guildDAO;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getGuild() == null)
            return;
        
        if(!client.getPlayer().getGuildMember().hasRight(GuildRight.BOOST)){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(216));
            return;
        }
        
        Guild guild = client.getPlayer().getGuild();
        
        switch (args.charAt(0)) {
            case 'p'://Prospec
                if (guild.getCapital() < 1 || guild.get_Stats(Effect.ADD_PROS) >= 500) {
                    return;
                }
                guild.setCapital(guild.getCapital() - 1);
                guild.upgrade_Stats(Effect.ADD_PROS, 1);
                break;
            case 'x'://Sagesse
                if (guild.getCapital() < 1 || guild.get_Stats(Effect.ADD_SAGE) >= 400) {
                    return;
                }
                guild.setCapital(guild.getCapital() - 1);
                guild.upgrade_Stats(Effect.ADD_SAGE, 1);
                break;
            case 'o'://Pod
                if (guild.getCapital() < 1 || guild.get_Stats(Effect.ADD_PODS) >= 5000) {
                    return;
                }
                guild.setCapital(guild.getCapital() - 1);
                guild.upgrade_Stats(Effect.ADD_PODS, 20);
                break;
            case 'k'://Nb Perco
                if (guild.getCapital() < 10 || guild.getMaxCollectors() >= 50) {
                    return;
                }
               
                guild.setCapital(guild.getCapital() - 10);
                guild.setMaxCollectors(guild.getMaxCollectors() + 1);
                break;
        }
        
        guildDAO.save(guild);
        client.send(new GuildBoostInfo(guild));
    }

    @Override
    public String header() {
        return "gB";
    }

}
