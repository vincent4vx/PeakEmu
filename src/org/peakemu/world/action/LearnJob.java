/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.Peak;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.JobStats;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Job;
import org.peakemu.world.MapCell;
import org.peakemu.world.config.JobConfig;
import org.peakemu.world.handler.JobHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class LearnJob implements ActionPerformer {
    final private JobHandler jobHandler;
    final private JobConfig config;

    public LearnJob(JobHandler jobHandler, JobConfig config) {
        this.jobHandler = jobHandler;
        this.config = config;
    }

    @Override
    public int actionId() {
        return 6;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        try {
            int mID = Integer.parseInt(action.getArgs());
            Job job = jobHandler.getJobById(mID);
            
            if(!job.isMageJob()){
                for(JobStats jobStats : caster.getJobs()){
                    if(!jobStats.getJob().isMageJob() && jobStats.getLevel() < config.getMinJobsLevelToLearnNew()){
                        caster.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(8, config.getMinJobsLevelToLearnNew()));
                        return false;
                    }
                }
            }else{
                if(!caster.hasJob(job.getBaseJob())){
                    caster.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(2));
                    return false;
                }
                
                if(caster.getJobStats(job.getBaseJob()).getLevel() < config.getMinMageJobLevel()){
                    caster.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(8, config.getMinMageJobLevel()));
                    return false;
                }
            }
            
            jobHandler.learnJob(caster, job);
            return true;
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            return false;
        }
    }

}
