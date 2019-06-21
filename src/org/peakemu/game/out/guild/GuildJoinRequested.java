/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.guild;

import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GuildJoinRequested {
    private Player target;

    public GuildJoinRequested(Player target) {
        this.target = target;
    }
    
    

    @Override
    public String toString() {
        return "gJR" + target.getName();
    }
    
}
