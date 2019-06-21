/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.realm.out;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.common.util.Pair;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ServerList {
    final static public long ONE_YEAR = 31536000000l;
    
    private long aboTime;
    final private Collection<Pair<Integer, Integer>> servers = new ArrayList<>();

    public ServerList(long aboTime) {
        this.aboTime = aboTime;
    }

    public long getAboTime() {
        return aboTime;
    }

    public void setAboTime(long aboTime) {
        this.aboTime = aboTime;
    }
    
    public void addServer(int serverId, int nbPerso){
        servers.add(new Pair<>(serverId, nbPerso));
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder(64);
        
        ret.append("AxK").append(aboTime);
        
        for (Pair<Integer, Integer> server : servers) {
            ret.append('|').append(server.getFirst()).append(',').append(server.getSecond());
        }
        
        return ret.toString();
    }
}
