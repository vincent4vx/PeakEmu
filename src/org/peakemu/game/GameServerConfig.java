/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game;

import fr.quatrevieux.crisis.config.AbstractConfigPackage;
import fr.quatrevieux.crisis.config.DefaultValue;
import fr.quatrevieux.crisis.config.item.IntConfigItem;
import fr.quatrevieux.crisis.config.item.StringConfigItem;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GameServerConfig extends AbstractConfigPackage{
    @DefaultValue("444")
    private IntConfigItem port;
    
    @DefaultValue("127.0.0.1")
    private StringConfigItem ip;
    
    @DefaultValue("0")
    private IntConfigItem regionalVersion;

    public int getPort() {
        return port.getValue();
    }

    public String getIp() {
        return ip.getValue();
    }

    public int getRegionalVersion() {
        return regionalVersion.getValue();
    }
    
}
