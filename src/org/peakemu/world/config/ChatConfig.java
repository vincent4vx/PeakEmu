/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.config;

import fr.quatrevieux.crisis.config.AbstractConfigPackage;
import fr.quatrevieux.crisis.config.DefaultValue;
import fr.quatrevieux.crisis.config.item.IntConfigItem;
import fr.quatrevieux.crisis.config.item.StringConfigItem;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ChatConfig extends AbstractConfigPackage{
    @DefaultValue(".")
    private StringConfigItem commandStart;
    
    @DefaultValue("45000")
    private IntConfigItem tradeFloodTime;
    
    @DefaultValue("45000")
    private IntConfigItem recruitmentFloodTime;
    
    @DefaultValue("5000")
    private IntConfigItem alignmentFloodTime;

    public String getCommandStart() {
        return commandStart.getValue();
    }

    public int getTradeFloodTime() {
        return tradeFloodTime.getValue();
    }

    public int getRecruitmentFloodTime() {
        return recruitmentFloodTime.getValue();
    }

    public int getAlignmentFloodTime() {
        return alignmentFloodTime.getValue();
    }
    
    
}
