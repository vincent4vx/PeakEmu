/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.guild;

import org.peakemu.objects.player.GuildMember;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GuildStats {
    private GuildMember guildMember;

    public GuildStats(GuildMember guildMember) {
        this.guildMember = guildMember;
    }

    @Override
    public String toString() {
        return "gS" + guildMember.getGuild().getName() + "|" + guildMember.getGuild().getEmbem().replace(',', '|') + "|" + Integer.toString(guildMember.getRights(), 36);
    }
    
    
}
