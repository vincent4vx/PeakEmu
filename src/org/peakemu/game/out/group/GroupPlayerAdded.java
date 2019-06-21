/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.group;

import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GroupPlayerAdded {
    private Player player;

    public GroupPlayerAdded(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "PM+" + player.parseToPM();
    }
    
    
}
