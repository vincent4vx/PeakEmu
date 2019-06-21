/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.game;

import org.peakemu.common.SocketManager;
import org.peakemu.database.Database;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.account.AlterRestrictions;
import org.peakemu.game.out.fight.FightCount;
import org.peakemu.game.out.game.MapData;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CreateGame implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().isOnFight()){
            return;
        }
        
        Player player = client.getPlayer();
        SocketManager.GAME_SEND_GAME_CREATE(client, player.getName());
        SocketManager.GAME_SEND_STATS_PACKET(player);
        Database.LOG_OUT(client.getAccount().get_GUID(), 1);
        client.send(new MapData(player.getMap()));
        client.send(new FightCount(player.getMap().getFights().size()));
        player.getMap().addPlayer(player);
        client.send(new AlterRestrictions(client.getPlayer().getRestrictions()));
    }

    @Override
    public String header() {
        return "GC";
    }
    
}
