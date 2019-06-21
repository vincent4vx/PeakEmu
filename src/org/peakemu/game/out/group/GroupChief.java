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
public class GroupChief {
    private Group group;

    public GroupChief(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "PL" + group.getChief().getName();
    }
    
    
}
