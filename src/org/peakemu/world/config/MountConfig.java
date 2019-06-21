/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.config;

import fr.quatrevieux.crisis.config.AbstractConfigPackage;
import fr.quatrevieux.crisis.config.DefaultValue;
import fr.quatrevieux.crisis.config.item.BoolConfigItem;
import fr.quatrevieux.crisis.config.item.IntConfigItem;
import fr.quatrevieux.crisis.config.item.StringConfigItem;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountConfig extends AbstractConfigPackage{
    @DefaultValue("true")
    private BoolConfigItem allowCreateMountWithFakeCertif;
    
    @DefaultValue("Sans nom")
    private StringConfigItem defaultName;
    
    @DefaultValue("false")
    private BoolConfigItem loadMountParksWithMapData;
    
    @DefaultValue("3000000")
    private IntConfigItem defaultMountParkPrice;
    
    @DefaultValue("5")
    private IntConfigItem defaultMountParkSize;
    
    @DefaultValue("1")
    private IntConfigItem startMountLevel;
    
    @DefaultValue("false")
    private BoolConfigItem addXpOnMaxLevel;

    public boolean getAllowCreateMountWithFakeCertif() {
        return allowCreateMountWithFakeCertif.getValue();
    }

    public String getDefaultName() {
        return defaultName.getValue();
    }

    public boolean getLoadMountParksWithMapData() {
        return loadMountParksWithMapData.getValue();
    }

    public int getDefaultMountParkPrice() {
        return defaultMountParkPrice.getValue();
    }

    public int getDefaultMountParkSize() {
        return defaultMountParkSize.getValue();
    }

    public int getStartMountLevel() {
        return startMountLevel.getValue();
    }

    public boolean getAddXpOnMaxLevel() {
        return addXpOnMaxLevel.getValue();
    }
}
