/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.Peak;
import org.peakemu.game.GameServer;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.JobStats;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.handler.JobHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class AddJobXP implements ActionPerformer {
    final private JobHandler jobHandler;

    public AddJobXP(JobHandler jobHandler) {
        this.jobHandler = jobHandler;
    }

    @Override
    public int actionId() {
        return 17;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        try {
            int JobID = Integer.parseInt(action.getArgs().split(",")[0]);
            long XpValue = Long.parseLong(action.getArgs().split(",")[1]);
            JobStats m = null;
            
            for(JobStats js : caster.getJobs()){
                if(js.getJob().getId() == JobID){
                    m = js;
                    break;
                }
            }
            
            if (m == null) {
                return false;
            }
            
            jobHandler.addXp(caster, m, XpValue);
            return true;
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            return false;
        }
    }

}
