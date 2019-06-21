/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.mount;

import org.peakemu.objects.MountPark;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AddMountPark {
    private MountPark mountPark;

    public AddMountPark(MountPark mountPark) {
        this.mountPark = mountPark;
    }

    @Override
    public String toString() {
        int owner = 0;
        
        if(mountPark.isPublic())
            owner = -1;
        
        if(mountPark.getGuild() != null)
            owner = mountPark.getGuild().getId();
        
        String packet = "Rp" + owner + ";" + mountPark.getPrice() + ";" + mountPark.getSize() + ";" + mountPark.getObjectNumb() + ";";
        
        if(mountPark.getGuild() != null){
            packet += mountPark.getGuild().getName() + ";" + mountPark.getGuild().getEmbem();
        }
        
        return packet;
    }
    
    
}
