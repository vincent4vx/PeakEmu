/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.job;

import java.util.Collection;
import java.util.Collections;
import org.peakemu.objects.player.JobStats;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class JobXp {
    private Collection<JobStats> jobs;

    public JobXp(Collection<JobStats> job) {
        this.jobs = job;
    }

    public JobXp(JobStats jobStats) {
        jobs = Collections.singleton(jobStats);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("JX");
        
        for(JobStats stats : jobs){
            long min = stats.getExpLevel().job;
            long xp = stats.getXp();
            long max = stats.getExpLevel().getNext().job == -1 ? min : stats.getExpLevel().getNext().job;
            
            sb.append('|').append(stats.getJob().getId()).append(';').append(stats.getLevel()).append(';').append(min).append(';').append(xp).append(';').append(max);
        }
        
        return sb.toString();
    }
    
    
}
