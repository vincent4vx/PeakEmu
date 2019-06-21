/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.group;

import org.peakemu.objects.player.Group;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GroupCreated {
    private Group group;

    public GroupCreated(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "PCK" + group.getChief().getName();
    }
    
    
}
