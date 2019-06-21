/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.Collection;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class JobSkill {
    final private int id;
    final private Job job;
    final private String name;
    final private Collection<Craft> crafts;
    final private ItemTemplate harvest;
    final private int level;
    
    private IOActionPerformer actionPerformer = null;

    public JobSkill(int id, Job job, String name, Collection<Craft> crafts, ItemTemplate harvest, int level) {
        this.id = id;
        this.job = job;
        this.name = name;
        this.crafts = crafts;
        this.harvest = harvest;
        this.level = level;
    }
    
    public boolean isCraft(){
        return !crafts.isEmpty();
    }
    
    public boolean isHarvest(){
        return harvest != null;
    }

    public int getId() {
        return id;
    }

    public Job getJob() {
        return job;
    }

    public String getName() {
        return name;
    }

    public Collection<Craft> getCrafts() {
        return crafts;
    }

    public ItemTemplate getHarvest() {
        return harvest;
    }

    public IOActionPerformer getActionPerformer() {
        return actionPerformer;
    }

    public void setActionPerformer(IOActionPerformer actionPerformer) {
        this.actionPerformer = actionPerformer;
    }

    public int getLevel() {
        return level;
    }
}
