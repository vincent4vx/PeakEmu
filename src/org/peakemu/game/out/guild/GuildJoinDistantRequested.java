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
public class GuildJoinDistantRequested {
    private Player requester;

    public GuildJoinDistantRequested(Player requester) {
        this.requester = requester;
    }

    @Override
    public String toString() {
        return "gJr" + requester.getSpriteId() + "|" + requester.getName() + "|" + requester.getGuild().getName();
    }
    
    
}
