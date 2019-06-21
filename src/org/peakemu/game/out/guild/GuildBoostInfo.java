/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.guild;

import org.peakemu.database.parser.SpellParser;
import org.peakemu.objects.Guild;
import org.peakemu.world.enums.Effect;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GuildBoostInfo {
    private Guild guild;

    public GuildBoostInfo(Guild guild) {
        this.guild = guild;
    }

    @Override
    public String toString() {
        return "gIB" + guild.getMaxCollectors() + "|" 
            + guild.getCollectorsCount() + "|" 
            + 100 * guild.getLevel() + "|" 
            + guild.getLevel() + "|" 
            + guild.get_Stats(Effect.ADD_PODS) + "|"
            + guild.get_Stats(Effect.ADD_PROS) + "|"
            + guild.get_Stats(Effect.ADD_SAGE) + "|"
            + guild.getMaxCollectors() + "|"
            + guild.getCapital() + "|"
            + guild.getCollectorCost() + "|"
            + SpellParser.serializeCollectorSpells(guild.getSpells())
        ;
    }
    
    
}
