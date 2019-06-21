/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.realm.out;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.common.util.StringUtil;
import org.peakemu.world.World;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class HostList {
    static public class Host{
        private int id;
        private World.State state;
        private int completion;
        private boolean canLog;

        public Host(int id, World.State state, int completion, boolean canLog) {
            this.id = id;
            this.state = state;
            this.completion = completion;
            this.canLog = canLog;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public World.State getState() {
            return state;
        }

        public void setState(World.State state) {
            this.state = state;
        }

        public int getCompletion() {
            return completion;
        }

        public void setCompletion(int completion) {
            this.completion = completion;
        }

        public boolean isCanLog() {
            return canLog;
        }

        public void setCanLog(boolean canLog) {
            this.canLog = canLog;
        }

        @Override
        public String toString() {
            return id + ";" + state.getStateId() + ";" + completion + ";" + canLog;
        }
    }
    
    final private Collection<Host> list = new ArrayList<>();
    
    public void addHost(Host host){
        list.add(host);
    }

    @Override
    public String toString() {
        return "AH" + StringUtil.join(list, "|");
    }
    
    static public HostList createByWorld(World world){
        HostList hl = new HostList();
        hl.addHost(new Host(1, world.getState(), 110, true));
        return hl;
    }
}
