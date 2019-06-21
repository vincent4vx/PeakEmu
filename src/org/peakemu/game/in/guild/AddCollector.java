/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.guild;

import org.peakemu.common.util.Util;
import org.peakemu.database.dao.CollectorDAO;
import org.peakemu.database.dao.GuildDAO;
import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.guild.CollectorInfoMessage;
import org.peakemu.game.out.guild.InfosTaxCollectorsMovement;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Collector;
import org.peakemu.objects.Guild;
import org.peakemu.world.enums.GuildRight;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AddCollector implements InputPacket<GameClient> {
    final private GuildDAO guildDAO;
    final private CollectorDAO collectorDAO;
    final private PlayerDAO playerDAO;

    public AddCollector(GuildDAO guildDAO, CollectorDAO collectorDAO, PlayerDAO playerDAO) {
        this.guildDAO = guildDAO;
        this.collectorDAO = collectorDAO;
        this.playerDAO = playerDAO;
    }

    @Override
    public void parse(String args, GameClient client) {
        if (client.getPlayer() == null || client.getPlayer().getGuild() == null || client.getPlayer().isAway()) {
            return;
        }
        
        Guild guild = client.getPlayer().getGuild();

        if (!client.getPlayer().getGuildMember().hasRight(GuildRight.POSPERCO)) {
            return;//Pas le droit de le poser
        }
        
        if (guild.getSize() < 10) {
            return;//Guilde invalide
        }
        
        if (client.getPlayer().getKamas() < guild.getCollectorCost())//Kamas insuffisants
        {
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(82));
            return;
        }
        
        if (client.getPlayer().getMap().getCollector() != null)//La carte possède un perco
        {
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(168, 1));
            return;
        }
        
        if (client.getPlayer().getMap().get_placesStr().length() < 5)//La map ne possède pas de "places"
        {
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(13));
            return;
        }
        
        if (guild.getCollectorsCount() >= guild.getMaxCollectors()) {
            return;//Limite de percepteur
        }
        
        short random1 = (short) (Util.rand(1, 39));
        short random2 = (short) (Util.rand(1, 71));
        
        Collector collector = new Collector(-1, client.getPlayer().getMap(), client.getPlayer().getCell(), (byte) 3, guild, random1, random2, null, 0, 0);
        collector = collectorDAO.insert(collector);
        
        if(collector == null)
            return;
        
        client.getPlayer().removeKamas(guild.getCollectorCost());
        guild.sendToMembers(new InfosTaxCollectorsMovement(guild));
        guild.sendToMembers(new CollectorInfoMessage(collector, CollectorInfoMessage.ADD, client.getPlayer()));
        guildDAO.save(guild);
        playerDAO.save(client.getPlayer());
    }

    @Override
    public String header() {
        return "gH";
    }

}
