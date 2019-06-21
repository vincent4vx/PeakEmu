/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.Peak;
import org.peakemu.common.SocketManager;
import org.peakemu.game.GameServer;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.objects.player.JobStats;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class UnlearnJob implements ActionPerformer {

    @Override
    public int actionId() {
        return 23;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        try {
            int job = Integer.parseInt(action.getArgs());
            if (job < 1) {
                return false;
            }
            
            JobStats m = null;
            
            for(JobStats js : caster.getJobs()){
                if(js.getJob().getId() == job){
                    m = js;
                    break;
                }
            }
            
            if (m == null) {
                return false;
            }
            caster.unlearnJob(m);
            SocketManager.GAME_SEND_STATS_PACKET(caster);
            return true;
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            return false;
        }
    }

}
