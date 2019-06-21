/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.guild;

import org.peakemu.common.SocketManager;
import org.peakemu.database.Database;
import org.peakemu.database.dao.CollectorDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.guild.CollectorInfoMessage;
import org.peakemu.game.out.guild.InfosTaxCollectorsMovement;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Collector;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Sprite;
import org.peakemu.world.enums.GuildRight;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class RemoveCollector implements InputPacket<GameClient>{
    final private CollectorDAO collectorDAO;

    public RemoveCollector(CollectorDAO collectorDAO) {
        this.collectorDAO = collectorDAO;
    }

    @Override
    public void parse(String args, GameClient client) {
        Player player = client.getPlayer();
        
        if (player == null || player.getGuild() == null || player.getFight() != null || player.isAway()) {
            return;
        }
        
        if (!player.getGuildMember().hasRight(GuildRight.POSPERCO)) {
            return;//On peut le retirer si on a le droit de le poser
        }
        
        int spriteId = Integer.parseInt(args);
        Sprite sprite = player.getMap().getSprite(spriteId);
        
        if(sprite == null || !(sprite instanceof Collector))
            return;
        
        Collector collector = (Collector) sprite;
        
        if(collector.get_inFight() > 0)
            return;
        
        player.getMap().removeSprite(spriteId);
        collectorDAO.remove(collector);
        
        player.getGuild().sendToMembers(new CollectorInfoMessage(collector, CollectorInfoMessage.REMOVE, player));
        player.getGuild().sendToMembers(new InfosTaxCollectorsMovement(player.getGuild()));
    }

    @Override
    public String header() {
        return "gF";
    }
    
}
