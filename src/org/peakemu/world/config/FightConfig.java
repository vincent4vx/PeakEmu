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
public class FightConfig extends AbstractConfigPackage{
    @DefaultValue("60000")
    private IntConfigItem conquestPlacementTime;
    
    @DefaultValue("45000")
    private IntConfigItem defaultPlacementTime;
    
    @DefaultValue("10")
    private IntConfigItem monsterEmoteChance;

    public int getConquestPlacementTime() {
        return conquestPlacementTime.getValue();
    }

    public int getDefaultPlacementTime() {
        return defaultPlacementTime.getValue();
    }

    public int getMonsterEmoteChance() {
        return monsterEmoteChance.getValue();
    }
    
}
