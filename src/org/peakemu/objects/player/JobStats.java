/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects.player;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.world.ExpLevel;
import org.peakemu.world.Job;
import org.peakemu.world.JobSkill;

public class JobStats {
    final private Job job;
    private ExpLevel expLevel;
    private long xp;
    private boolean _isCheap = false;
    private boolean _freeOnFails = false;
    private boolean _noRessource = false;

    public JobStats(Job tp, ExpLevel expLevel, long xp) {
        this.job = tp;
        this.expLevel = expLevel;
        this.xp = xp;
        refreshLevel();
    }

    public int getLevel() {
        return expLevel.level;
    }

    public ExpLevel getExpLevel() {
        return expLevel;
    }

    public boolean isCheap() {
        return _isCheap;
    }

    public void setIsCheap(boolean isCheap) {
        _isCheap = isCheap;
    }

    public boolean isFreeOnFails() {
        return _freeOnFails;
    }

    public void setFreeOnFails(boolean freeOnFails) {
        _freeOnFails = freeOnFails;
    }

    public boolean isNoRessource() {
        return _noRessource;
    }

    public void setNoRessource(boolean noRessource) {
        _noRessource = noRessource;
    }

    public long getXp() {
        return xp;
    }

    public boolean addXp(long xp) {
        this.xp += xp;
        return refreshLevel();
    }
    
    private boolean refreshLevel(){
        boolean up = false;
        
        while(expLevel.getNext() != null
            && expLevel.getNext().job != -1
            && xp > expLevel.getNext().job){
            expLevel = expLevel.getNext();
            up = true;
        }
        
        return up;
    }

    public Job getJob() {
        return job;
    }

    public int getOptBinValue() {
        int nbr = 0;
        nbr += (_isCheap ? 1 : 0);
        nbr += (_freeOnFails ? 2 : 0);
        nbr += (_noRessource ? 4 : 0);
        return nbr;
    }

    public void setOptBinValue(int bin) {
        _isCheap = false;
        _freeOnFails = false;
        _noRessource = false;

        if (bin - 4 >= 0) {
            bin -= 4;
            _isCheap = true;
        }
        if (bin - 2 >= 0) {
            bin -= 2;
            _freeOnFails = true;
        }
        if (bin - 1 >= 0) {
            bin--;
            _noRessource = true;
        }
    }
    
    public Collection<JobSkill> getAvailableSkills(){
        Collection<JobSkill> skills = new ArrayList<>();
        
        for(JobSkill skill : job.getSkills()){
            if(getLevel() >= skill.getLevel())
                skills.add(skill);
        }
        
        return skills;
    }
}
