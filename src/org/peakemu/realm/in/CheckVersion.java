/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.realm.in;

import org.peakemu.network.InputPacket;
import org.peakemu.realm.RealmClient;
import org.peakemu.realm.RealmServerConfig;
import org.peakemu.realm.out.BadVersion;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CheckVersion implements InputPacket<RealmClient>{
    final private RealmServerConfig config;

    public CheckVersion(RealmServerConfig config) {
        this.config = config;
    }

    @Override
    public void parse(String args, RealmClient client) {
        if(config.getCheckDofusVersion() && !args.equals(config.getDofusVersion())){
            client.send(new BadVersion(config.getDofusVersion()));
            client.close();
        }
    }

    @Override
    public String header() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
