/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.job;

import org.peakemu.objects.player.JobStats;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class JobLevelUp {
    private JobStats job;

    public JobLevelUp(JobStats job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "JN" + job.getJob().getId() + "|" + job.getLevel();
    }
    
    
}
