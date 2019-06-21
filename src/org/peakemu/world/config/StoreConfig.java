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
public class StoreConfig extends AbstractConfigPackage{
    @DefaultValue("0.001")
    private DoubleConfigItem playerStoreTaxRate;
    
    @DefaultValue("5")
    private IntConfigItem maxPlayerStorePerMap;

    public double getPlayerStoreTaxRate() {
        return playerStoreTaxRate.getValue();
    }

    public int getMaxPlayerStorePerMap() {
        return maxPlayerStorePerMap.getValue();
    }
}
