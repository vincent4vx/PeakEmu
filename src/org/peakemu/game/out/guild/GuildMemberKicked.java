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
public class GuildMemberKicked {
    private Player performer;
    private Player target;

    public GuildMemberKicked(Player performer, Player target) {
        this.performer = performer;
        this.target = target;
    }

    @Override
    public String toString() {
        return "gKK" + performer.getName() + "|" + target.getName();
    }
    
    
}
