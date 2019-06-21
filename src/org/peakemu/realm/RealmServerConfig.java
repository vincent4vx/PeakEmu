/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.realm;

import fr.quatrevieux.crisis.config.AbstractConfigPackage;
import fr.quatrevieux.crisis.config.DefaultValue;
import fr.quatrevieux.crisis.config.item.BoolConfigItem;
import fr.quatrevieux.crisis.config.item.IntConfigItem;
import fr.quatrevieux.crisis.config.item.StringConfigItem;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class RealmServerConfig extends AbstractConfigPackage{
    @DefaultValue("5555")
    private IntConfigItem port;
    
    @DefaultValue("1.29.1")
    private StringConfigItem dofusVersion;
    
    @DefaultValue("true")
    private BoolConfigItem checkDofusVersion;
    
    @DefaultValue("false")
    private BoolConfigItem cryptIp;

    public int getPort() {
        return port.getValue();
    }

    public String getDofusVersion() {
        return dofusVersion.getValue();
    }

    public boolean getCheckDofusVersion() {
        return checkDofusVersion.getValue();
    }

    public boolean getCryptIp() {
        return cryptIp.getValue();
    }
}
