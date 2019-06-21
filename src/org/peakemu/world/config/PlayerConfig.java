/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.config;

import java.util.List;
import fr.quatrevieux.crisis.config.AbstractConfigPackage;
import fr.quatrevieux.crisis.config.DefaultValue;
import fr.quatrevieux.crisis.config.item.BoolConfigItem;
import fr.quatrevieux.crisis.config.item.IntConfigItem;
import fr.quatrevieux.crisis.config.item.IntListConfigItem;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PlayerConfig extends AbstractConfigPackage{
    @DefaultValue("5")
    private IntConfigItem capitalPointsPerLevel;
    
    @DefaultValue("1")
    private IntConfigItem spellPointsPerLevel;
    
    @DefaultValue("false")
    private BoolConfigItem addXpOnMaxLevel;
    
    @DefaultValue("false")
    private BoolConfigItem addHonorOnMax;
    
    @DefaultValue("false")
    private BoolConfigItem addJobXpOnMaxLevel;
    
    @DefaultValue("100")
    private IntListConfigItem bonusApLevels;
    
    @DefaultValue("true")
    private BoolConfigItem enableEnergy;

    public int getCapitalPointsPerLevel() {
        return capitalPointsPerLevel.getValue();
    }

    public int getSpellPointsPerLevel() {
        return spellPointsPerLevel.getValue();
    }

    public boolean getAddXpOnMaxLevel() {
        return addXpOnMaxLevel.getValue();
    }

    public List<Integer> getBonusApLevels() {
        return bonusApLevels.getValue();
    }

    public boolean getAddHonorOnMax() {
        return addHonorOnMax.getValue();
    }

    public boolean getAddJobXpOnMaxLevel() {
        return addJobXpOnMaxLevel.getValue();
    }

    public boolean getEnableEnergy() {
        return enableEnergy.getValue();
    }
}
