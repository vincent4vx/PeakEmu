/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database;

import fr.quatrevieux.crisis.config.AbstractConfigPackage;
import fr.quatrevieux.crisis.config.DefaultValue;
import fr.quatrevieux.crisis.config.item.StringConfigItem;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class DatabaseConfig extends AbstractConfigPackage{
    @DefaultValue("127.0.0.1")
    private StringConfigItem host;
    
    @DefaultValue("root")
    private StringConfigItem user;
    
    @DefaultValue("")
    private StringConfigItem password;
    
    @DefaultValue("peakemu")
    private StringConfigItem dbname;

    public String getHost() {
        return host.getValue();
    }

    public String getUser() {
        return user.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

    public String getDbname() {
        return dbname.getValue();
    }
}
