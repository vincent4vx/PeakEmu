/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.group;

import org.peakemu.objects.player.Group;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GroupList {
    private Group group;

    public GroupList(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        
        sb.append("PM+");
        
        boolean b = false;
        
        for(Player player : group.getPlayers()){
            if(b)
                sb.append('|');
            else
                b = true;
            
            sb.append(player.parseToPM());
        }
        
        return sb.toString();
    }
    
    
}
