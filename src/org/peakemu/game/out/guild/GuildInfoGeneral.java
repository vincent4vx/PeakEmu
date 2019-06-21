/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.guild;

import org.peakemu.objects.Guild;
import org.peakemu.world.ExpLevel;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GuildInfoGeneral {
    final private Guild guild;
    final private ExpLevel expLevel;

    public GuildInfoGeneral(Guild guild, ExpLevel expLevel) {
        this.guild = guild;
        this.expLevel = expLevel;
    }

    @Override
    public String toString() {
        long xpMin = expLevel.guild;
        long xpMax = expLevel.getNext() != null ? expLevel.getNext().guild : xpMin;
        
        StringBuilder packet = new StringBuilder();
        return packet.append("gIG").append((guild.getSize() > 9 ? 1 : 0)).append("|").append(guild.getLevel()).append("|").append(xpMin).append("|").append(guild.getXp()).append("|").append(xpMax).toString();
    }
    
}
