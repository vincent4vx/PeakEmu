/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.database.Database;
import org.peakemu.database.parser.SpellParser;
import org.peakemu.database.parser.StatsParser;
import org.peakemu.objects.Guild;
import org.peakemu.objects.player.GuildMember;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GuildDAO {
    final private Database database;
    final private SpellDAO spellDAO;
    
    final private Map<Integer, Guild> guilds = new HashMap<>();

    public GuildDAO(Database database, SpellDAO spellDAO) {
        this.database = database;
        this.spellDAO = spellDAO;
    }
    
    private Guild createByRS(ResultSet RS) throws SQLException{
        Guild guild = new Guild(
            RS.getInt("id"),
            RS.getString("name"),
            RS.getString("emblem"),
            RS.getInt("lvl"),
            RS.getLong("xp"),
            RS.getInt("capital"),
            RS.getInt("nbrmax"),
            SpellParser.parseCollectorSpells(spellDAO, RS.getString("sorts")),
            StatsParser.parseCollectorStats(RS.getString("stats"))
        );
        
        guilds.put(guild.getId(), guild);
        return guild;
    }
    
    public Collection<Guild> getAll(){
        if(!guilds.isEmpty())
            return guilds.values();
        
        try(ResultSet RS = database.query("SELECT * FROM guilds")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return guilds.values();
    }
    
    public Guild getGuildById(int guildId){
        if(guilds.containsKey(guildId))
            return guilds.get(guildId);
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM guilds WHERE id = ?")){
            stmt.setInt(1, guildId);
            
            try(ResultSet RS = stmt.executeQuery()){
                if(RS.next()){
                    Guild guild = createByRS(RS);
                    return guild;
                }
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return null;
    }
    
    public boolean checkNameExists(String name){
        try(PreparedStatement stmt = database.prepare("SELECT COUNT(*) FROM guilds WHERE name = ?")){
            stmt.setString(1, name);
            
            try(ResultSet RS = stmt.executeQuery()){
                RS.next();
                return RS.getInt(1) > 0;
            }
        }catch(SQLException e){
            return true;
        }
    }
    
    public boolean checkEmblemExists(String emblem){
        try(PreparedStatement stmt = database.prepare("SELECT COUNT(*) FROM guilds WHERE emblem = ?")){
            stmt.setString(1, emblem);
            
            try(ResultSet RS = stmt.executeQuery()){
                RS.next();
                return RS.getInt(1) > 0;
            }
        }catch(SQLException e){
            return true;
        }
    }
    
    public Guild insert(Guild guild){
        try(PreparedStatement stmt = database.prepareInsert("INSERT INTO guilds(name, emblem, lvl, xp, capital, nbrmax, sorts, stats) VALUES(?,?,?,?,?,?,?,?)")){
            int i = 0;
            stmt.setString(++i, guild.getName());
            stmt.setString(++i, guild.getEmbem());
            stmt.setInt(++i, guild.getLevel());
            stmt.setLong(++i, guild.getXp());
            stmt.setInt(++i, guild.getCapital());
            stmt.setInt(++i, guild.getMaxCollectors());
            stmt.setString(++i, SpellParser.serializeCollectorSpells(guild.getSpells()));
            stmt.setString(++i, StatsParser.serializeCollectorStats(guild.getStats()));
            stmt.executeUpdate();
            
            try(ResultSet RS = stmt.getGeneratedKeys()){
                RS.next();
                int id = RS.getInt(1);
                guild = new Guild(
                    id, 
                    guild.getName(), 
                    guild.getEmbem(), 
                    guild.getLevel(), 
                    guild.getXp(), 
                    guild.getCapital(), 
                    guild.getMaxCollectors(), 
                    guild.getSpells(), 
                    guild.getStats()
                );
                guilds.put(id, guild);
                return guild;
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
            return null;
        }
    }
    
    public void delete(Guild guild){
        try(PreparedStatement stmt = database.prepare("DELETE FROM guilds WHERE id = ?")){
            stmt.setInt(1, guild.getId());
            stmt.executeUpdate();
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }finally{
            guilds.remove(guild.getId());
            //remove guild members from players
            for(GuildMember member : guild.getMembers()){
                member.getPlayer().setGuildMember(null);
            }
        }
    }
    
    public void save(Guild guild){
        String baseQuery = "UPDATE `guilds` SET "
                + "`lvl` = ?,"
                + "`xp` = ?,"
                + "`capital` = ?,"
                + "`nbrmax` = ?,"
                + "`sorts` = ?,"
                + "`stats` = ?"
                + " WHERE id = ?;";
        
        try(PreparedStatement stmt = database.prepare(baseQuery)){
            int i = 0;
            stmt.setInt(++i, guild.getLevel());
            stmt.setLong(++i, guild.getXp());
            stmt.setInt(++i, guild.getCapital());
            stmt.setInt(++i, guild.getMaxCollectors());
            stmt.setString(++i, SpellParser.serializeCollectorSpells(guild.getSpells()));
            stmt.setString(++i, StatsParser.serializeCollectorStats(guild.getStats()));
            stmt.setInt(++i, guild.getId());
            stmt.executeUpdate();
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
}
