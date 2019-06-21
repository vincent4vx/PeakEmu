/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.guild;

import org.peakemu.objects.Guild;
import org.peakemu.objects.player.GuildMember;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GuildInfoMembers {
    private Guild guild;

    public GuildInfoMembers(Guild guild) {
        this.guild = guild;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        
        sb.append("gIM+");
        
        boolean b = false;
        
        for(GuildMember member : guild.getMembers()){
            if(b)
                sb.append('|');
            else
                b = true;
            
            sb.append(member.getPlayer().getSpriteId()).append(';')
              .append(member.getPlayer().getName()).append(';')
              .append(member.getPlayer().getLevel()).append(';')
              .append(member.getPlayer().getGfxID()).append(';')
              .append(member.getRank()).append(';')
              .append(member.getXpGave()).append(';')
              .append(member.getPXpGive()).append(';')
              .append(member.getRights()).append(';')
              .append(member.getPlayer().isOnline() ? "1" : "0").append(';')
              .append(member.getPlayer().getAlignement()).append(';')
              .append(member.getHoursFromLastCo())
            ;
        }
        
        return sb.toString();
    }
    
    
}
