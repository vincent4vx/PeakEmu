/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.config;

import fr.quatrevieux.crisis.config.AbstractConfigPackage;
import fr.quatrevieux.crisis.config.DefaultValue;
import fr.quatrevieux.crisis.config.item.DoubleConfigItem;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class RateConfig extends AbstractConfigPackage{
    @DefaultValue("1")
    public DoubleConfigItem jobXp;
    
    @DefaultValue("1")
    public DoubleConfigItem honour;

    public double getJobXp() {
        return jobXp.getValue();
    }

    public double getHonour() {
        return honour.getValue();
    }
}
