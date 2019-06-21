/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.guild;

import org.peakemu.database.dao.GuildDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.guild.GuildBoostInfo;
import org.peakemu.game.out.guild.GuildUpdateError;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Guild;
import org.peakemu.world.Spell;
import org.peakemu.world.enums.GuildRight;
import org.peakemu.world.handler.SpellHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class BoostCollectorsSpell implements InputPacket<GameClient>{
    final private GuildDAO guildDAO;
    final private SpellHandler spellHandler;

    public BoostCollectorsSpell(GuildDAO guildDAO, SpellHandler spellHandler) {
        this.guildDAO = guildDAO;
        this.spellHandler = spellHandler;
    }
    
    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        Guild guild = client.getPlayer().getGuild();
        
        if(guild == null)
            return;
        
        if(!client.getPlayer().getGuildMember().hasRight(GuildRight.BOOST)){
            client.send(new GuildUpdateError(GuildUpdateError.NOT_ENOUGHT_RIGHTS_FROM_GUILD));
            return;
        }
        
        if(guild.getCapital() < 5)
            return;
        
        try{
            Spell spell = spellHandler.getSpellById(Integer.parseInt(args));
            
            if(spell == null)
                return;
            
            if(guild.boostSpell(spell)){
                guild.setCapital(guild.getCapital() - 5);
                guildDAO.save(guild);
                guild.sendToMembers(new GuildBoostInfo(guild));
            }
        }catch(NumberFormatException e){}
    }

    @Override
    public String header() {
        return "gb";
    }
    
}
