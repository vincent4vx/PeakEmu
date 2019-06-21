/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.peakemu.Peak;
import org.peakemu.database.Database;
import org.peakemu.objects.player.GuildMember;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GuildMemberDAO {
    final private Database database;
    final private PlayerDAO playerDAO;
    final private GuildDAO guildDAO;

    public GuildMemberDAO(Database database, PlayerDAO playerDAO, GuildDAO guildDAO) {
        this.database = database;
        this.playerDAO = playerDAO;
        this.guildDAO = guildDAO;
    }
    
    public void load(){
        try(ResultSet RS = database.query("SELECT * from guild_members")){
            while(RS.next()){
                GuildMember member = new GuildMember(
                    playerDAO.getPlayerById(RS.getInt("player")), 
                    guildDAO.getGuildById(RS.getInt("guild")), 
                    RS.getInt("rank"), 
                    RS.getInt("xpdone"), 
                    RS.getByte("pxp"), 
                    RS.getInt("rights")
                );
                
                member.getGuild().addMember(member);
                member.getPlayer().setGuildMember(member);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
    
    public boolean save(GuildMember guildMember){
        try(PreparedStatement stmt = database.prepare("REPLACE INTO guild_members(player, guild, rank, xpdone, pxp, rights) VALUES(?,?,?,?,?,?)")){
            int i = 0;
            stmt.setInt(++i, guildMember.getPlayer().getSpriteId());
            stmt.setInt(++i, guildMember.getGuild().getId());
            stmt.setInt(++i, guildMember.getRank());
            stmt.setLong(++i, guildMember.getXpGave());
            stmt.setInt(++i, guildMember.getPXpGive());
            stmt.setInt(++i, guildMember.getRights());
            stmt.executeUpdate();
            return true;
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
            return false;
        }
    }
    
    public void delete(GuildMember member){
        try(PreparedStatement stmt = database.prepare("DELETE FROM guild_members WHERE player = ?")){
            stmt.setInt(1, member.getPlayer().getSpriteId());
            stmt.executeUpdate();
            member.getGuild().removeMember(member);
            member.getPlayer().setGuildMember(null);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
}
