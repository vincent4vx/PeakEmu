/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.exchange;

import java.util.Collection;
import org.peakemu.game.out.mount.MountParser;
import org.peakemu.objects.Mount;
import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountParkOpened {
    private Collection<Mount> mounts;

    public MountParkOpened(Collection<Mount> mounts) {
        this.mounts = mounts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ECK").append(ExchangeType.MOUNT_PARK.getId()).append('|');
        
        boolean b = false;
        
        for(Mount mount : mounts){
            if(b)
                sb.append(';');
            else
                b = true;
            
            sb.append(MountParser.parseMount(mount));
        }
        
        return sb.toString();
    }
}
