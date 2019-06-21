/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.account;

import org.peakemu.common.SocketManager;
import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.world.ClassData;
import org.peakemu.world.Stats;
import org.peakemu.world.enums.PlayerStat;
import org.peakemu.world.handler.PlayerHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class BoostStat implements InputPacket<GameClient>{
    final private PlayerHandler playerHandler;

    public BoostStat(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        int statId;
        
        try{
            statId = Integer.parseInt(args);
        }catch(NumberFormatException e){
            return;
        }
        
        PlayerStat stat = PlayerStat.valueOf(statId);
        
        if(stat == null)
            return;
        
        ClassData data = playerHandler.getClassData(client.getPlayer());
        
        Stats stats = client.getPlayer().getBaseCharacter().getBaseStats();
        
        ClassData.BoostInterval interval = data.getRequiredBoostStatsPoints(stat, stats.getEffect(stat.getEffect()));
        
        if(client.getPlayer().getCapitalPoints() < interval.cost)
            return;
        
        stats.addOneStat(stat.getEffect(), interval.statsPerPoints);
        client.getPlayer().removeCapitalPoints(interval.cost);
        
        SocketManager.GAME_SEND_STATS_PACKET(client.getPlayer());
    }

    @Override
    public String header() {
        return "AB";
    }
    
}
