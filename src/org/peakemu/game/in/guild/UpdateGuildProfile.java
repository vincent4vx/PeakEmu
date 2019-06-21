/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.guild;

import org.peakemu.common.util.StringUtil;
import org.peakemu.database.dao.GuildMemberDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.guild.GuildInfoMembers;
import org.peakemu.game.out.guild.GuildUpdateError;
import org.peakemu.game.out.guild.GuildStats;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.player.GuildMember;
import org.peakemu.world.enums.GuildRight;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class UpdateGuildProfile implements InputPacket<GameClient>{
    final private GuildMemberDAO guildMemberDAO;

    public UpdateGuildProfile(GuildMemberDAO guildMemberDAO) {
        this.guildMemberDAO = guildMemberDAO;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getGuild() == null)
            return;
        
        GuildMember performer = client.getPlayer().getGuildMember();
        
        String[] infos = StringUtil.split(args, "|");
        int guid = Integer.parseInt(infos[0]);
        int rank = Integer.parseInt(infos[1]);
        byte xpGive = Byte.parseByte(infos[2]);
        int rights = Integer.parseInt(infos[3]);
        
        GuildMember target = performer.getGuild().getMember(guid);
        
        if(target == null){
            client.send(new GuildUpdateError(GuildUpdateError.CANT_BANN_FROM_GUILD_NOT_MEMBER));
            return;
        }
        
        if(rank != target.getRank() && !performer.hasRight(GuildRight.RANK)){
            client.send(new GuildUpdateError(GuildUpdateError.NOT_ENOUGHT_RIGHTS_FROM_GUILD));
            return;
        }
        
        if(xpGive != target.getPXpGive()){
            if(!performer.hasRight(GuildRight.ALLXP)){
                if(!performer.equals(target) || performer.hasRight(GuildRight.HISXP)){
                    client.send(new GuildUpdateError(GuildUpdateError.NOT_ENOUGHT_RIGHTS_FROM_GUILD));
                    return;
                }
            }
        }
        
        if(rights != target.getRights() && !performer.hasRight(GuildRight.RIGHT)){
            client.send(new GuildUpdateError(GuildUpdateError.NOT_ENOUGHT_RIGHTS_FROM_GUILD));
            return;
        }
        
        if(target.getRank() == 1){ //the target is the boss => can't modify rank or rights
            if(rank != target.getRank()){
                client.send(new GuildUpdateError(GuildUpdateError.NOT_ENOUGHT_RIGHTS_FROM_GUILD));
                return;
            }
            
            if(rights != target.getRights()){
                client.send(new GuildUpdateError(GuildUpdateError.NOT_ENOUGHT_RIGHTS_FROM_GUILD));
                return;
            }
        }
        
        if(rank == 1 && target.getRank() != 1){ //change boss
            if(performer.getRank() != 1){ //only boss can change boss
                client.send(new GuildUpdateError(GuildUpdateError.NOT_ENOUGHT_RIGHTS_FROM_GUILD));
                return;
            }
            
            performer.setRank(2); //set old boss to second
            performer.setRights(GuildRight.getAllRights()); //set all rights
            client.send(new GuildStats(performer));
            guildMemberDAO.save(performer);
            
            rights = GuildRight.BOSS.toInt(); //boss should have BOSS rights
        }
        
        target.setRank(rank);
        target.setRights(rights);
        target.setpXpGive(xpGive);
        guildMemberDAO.save(target);
        
        if(target.getPlayer().isOnline()){
            target.getPlayer().send(new GuildStats(target));
        }
        
        performer.getGuild().sendToMembers(new GuildInfoMembers(performer.getGuild()));
    }

    @Override
    public String header() {
        return "gP";
    }
    
}
