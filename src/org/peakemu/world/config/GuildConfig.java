/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.config;

import fr.quatrevieux.crisis.config.AbstractConfigPackage;
import fr.quatrevieux.crisis.config.DefaultValue;
import fr.quatrevieux.crisis.config.item.IntConfigItem;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GuildConfig extends AbstractConfigPackage{
    @DefaultValue("5")
    private IntConfigItem capitalPointsPerLevel;

    public int getCapitalPointsPerLevel() {
        return capitalPointsPerLevel.getValue();
    }
}
