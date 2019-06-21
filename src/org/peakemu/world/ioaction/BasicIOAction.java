/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.ioaction;

import org.peakemu.game.in.gameaction.GameActionArg;
import org.peakemu.objects.player.JobStats;
import org.peakemu.objects.player.Player;
import org.peakemu.world.IOActionPerformer;
import org.peakemu.world.JobSkill;
import org.peakemu.world.MapCell;
import org.peakemu.world.enums.IOState;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public abstract class BasicIOAction implements IOActionPerformer{
    @Override
    public boolean canDoAction(JobSkill skill, MapCell cell, Player player, GameActionArg arg) {
        if(skill.getJob().isBaseJob()){
            if(skill.getLevel() > player.getLevel())
                return false;
        }else{
            JobStats js = player.getJobStats(skill.getJob());
            
            if(js == null || skill.getLevel() > js.getLevel())
                return false;
        }
        
        return cell.getObject().getState() == IOState.FULL;
    }

    @Override
    public void finish(JobSkill skill, MapCell cell, Player player, GameActionArg arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
