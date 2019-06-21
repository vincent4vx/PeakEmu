/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.mount;

import org.peakemu.objects.Mount;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountDescriptionResponse {
    final private Mount mount;

    public MountDescriptionResponse(Mount mount) {
        this.mount = mount;
    }

    @Override
    public String toString() {
        return "Rd" + MountParser.parseMount(mount);
    }
    
    
}
