/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.config;

import fr.quatrevieux.crisis.config.AbstractConfigPackage;
import fr.quatrevieux.crisis.config.DefaultValue;
import fr.quatrevieux.crisis.config.item.DoubleConfigItem;
import fr.quatrevieux.crisis.config.item.IntConfigItem;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class JobConfig extends AbstractConfigPackage{
    @DefaultValue("12000")
    private IntConfigItem defaultHarvestTime;
    
    @DefaultValue("100")
    private IntConfigItem winHarvestTimeWinPerLevel;
    
    @DefaultValue("0.2")
    private DoubleConfigItem winHarvestDropPerLevel;
    
    @DefaultValue("1")
    private IntConfigItem harvestDropBase;
    
    @DefaultValue("1")
    private IntConfigItem harvestDropMaxBonus;
    
    @DefaultValue("0.5")
    private DoubleConfigItem harvestWinXpPerSkillLevel;
    
    @DefaultValue("10")
    private IntConfigItem baseHarvestWinXp;
    
    @DefaultValue("30")
    private IntConfigItem minJobsLevelToLearnNew;
    
    @DefaultValue("65")
    private IntConfigItem minMageJobLevel;
    
    @DefaultValue("1")
    private IntConfigItem startJobLevel;

    public int getDefaultHarvestTime() {
        return defaultHarvestTime.getValue();
    }

    public int getWinHarvestTimeWinPerLevel() {
        return winHarvestTimeWinPerLevel.getValue();
    }

    public double getWinHarvestDropPerLevel() {
        return winHarvestDropPerLevel.getValue();
    }

    public int getHarvestDropBase() {
        return harvestDropBase.getValue();
    }

    public int getHarvestDropMaxBonus() {
        return harvestDropMaxBonus.getValue();
    }

    public double getHarvestWinXpPerSkillLevel() {
        return harvestWinXpPerSkillLevel.getValue();
    }

    public int getBaseHarvestWinXp() {
        return baseHarvestWinXp.getValue();
    }

    public int getMinJobsLevelToLearnNew() {
        return minJobsLevelToLearnNew.getValue();
    }

    public int getMinMageJobLevel() {
        return minMageJobLevel.getValue();
    }

    public int getStartJobLevel() {
        return startJobLevel.getValue();
    }
}
