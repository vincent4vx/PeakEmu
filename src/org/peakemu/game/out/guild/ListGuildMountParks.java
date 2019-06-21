/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.guild;

import java.util.Collection;
import org.peakemu.objects.Guild;
import org.peakemu.objects.MountPark;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ListGuildMountParks {
    private Guild guild;
    private Collection<MountPark> parks;

    public ListGuildMountParks(Guild guild, Collection<MountPark> parks) {
        this.guild = guild;
        this.parks = parks;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("gIF").append(guild.getMaxMountParks());
        
        for(MountPark park : parks){
            sb.append('|').append(park.getMap().getId()).append(';').append(park.getSize()).append(';').append(park.getObjectNumb());
        }
        
        return sb.toString();
    }
    
    
}
