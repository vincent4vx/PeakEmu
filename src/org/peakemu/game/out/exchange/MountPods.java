/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.exchange;

import org.peakemu.objects.Mount;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountPods {
    private Mount mount;

    public MountPods(Mount mount) {
        this.mount = mount;
    }

    @Override
    public String toString() {
        return "Ew" + mount.getItems().getUsedPods() + ";" + mount.getMaxPod();
    }
    
    
}
