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
import org.peakemu.objects.Collector;
import org.peakemu.objects.Guild;
import org.peakemu.world.GameMap;
import org.peakemu.world.Inventory;
import org.peakemu.world.ItemStorage;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CollectorDAO {
    final private Database database;
    final private GuildDAO guildDAO;
    final private MapDAO mapDAO;
    final private InventoryDAO inventoryDAO;
    
    final private Map<Integer, Collector> collectors = new HashMap<>();

    public CollectorDAO(Database database, GuildDAO guildDAO, MapDAO mapDAO, InventoryDAO inventoryDAO) {
        this.database = database;
        this.guildDAO = guildDAO;
        this.mapDAO = mapDAO;
        this.inventoryDAO = inventoryDAO;
    }
    
    private Collector createByRS(ResultSet RS) throws SQLException{
        GameMap map = mapDAO.getMapById(RS.getShort("mapid"));
        MapCell cell = map.getCell(RS.getInt("cellid"));
        Guild guild = guildDAO.getGuildById(RS.getInt("guild_id"));
        ItemStorage inventory = inventoryDAO.getCollectorStorage(RS.getInt("guid"));
        
        Collector collector = new Collector(
            RS.getInt("guid"),
            map,
            cell,
            RS.getByte("orientation"),
            guild,
            RS.getShort("N1"),
            RS.getShort("N2"),
            inventory,
            RS.getLong("kamas"),
            RS.getLong("xp")
        );
        
        guild.addCollector(collector);
        collector.setSpriteId(map.getNextSpriteId());
        map.addSprite(collector);
        
        collectors.put(collector.getGuid(), collector);
        return collector;
    }
    
    public Collection<Collector> getAll(){
        if(!collectors.isEmpty())
            return collectors.values();
        
        try(ResultSet RS = database.query("SELECT * FROM percepteurs")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return collectors.values();
    }
    
    public Collector insert(Collector collector){
        try(PreparedStatement stmt = database.prepareInsert("INSERT INTO percepteurs(mapid, cellid, orientation, guild_id, N1, N2, kamas, xp) VALUES(?,?,?,?,?,?,?,?)")){
            int i = 0;
            stmt.setShort(++i, collector.getMap().getId());
            stmt.setInt(++i, collector.getCell().getID());
            stmt.setByte(++i, collector.getOrientation());
            stmt.setInt(++i, collector.getGuild().getId());
            stmt.setShort(++i, collector.get_N1());
            stmt.setShort(++i, collector.get_N2());
            stmt.setLong(++i, collector.getKamas());
            stmt.setLong(++i, collector.getXp());
            stmt.executeUpdate();
            
            try(ResultSet RS = stmt.getGeneratedKeys()){
                RS.next();
                int id = RS.getInt(1);
                collector = new Collector(
                    id, 
                    collector.getMap(), 
                    collector.getCell(), 
                    collector.getOrientation(), 
                    collector.getGuild(), 
                    collector.get_N1(), 
                    collector.get_N2(), 
                    inventoryDAO.getInventory(InventoryDAO.OWNER_TYPE_COLLECTOR + id),
                    collector.getKamas(),
                    collector.getXp()
                );
                collectors.put(id, collector);
                collector.getGuild().addCollector(collector);
                collector.setSpriteId(collector.getMap().getNextSpriteId());
                collector.getMap().addSprite(collector);
                return collector;
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
            return null;
        }
    }
    
    public void save(Collector collector){
        String baseQuery = "UPDATE `percepteurs` SET "
            + "`kamas` = ?,"
            + "`xp` = ?"
            + " WHERE guid = ?;";

        try(PreparedStatement stmt = database.prepare(baseQuery)) {
            stmt.setLong(1, collector.getKamas());
            stmt.setLong(2, collector.getXp());
            stmt.setInt(3, collector.getGuid());

            stmt.execute();
        } catch (SQLException e) {
            Peak.errorLog.addToLog(e);
        }
        
        inventoryDAO.save(collector.getItems());
    }
    
    public void remove(Collector collector){
        collectors.remove(collector.getGuid());
        
        try(PreparedStatement stmt = database.prepare("DELETE FROM percepteurs WHERE guid = ?")){
            stmt.setInt(1, collector.getGuid());
            stmt.execute();
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        collector.getGuild().removeCollector(collector);
        collector.getMap().removeSprite(collector);
        inventoryDAO.delete(collector.getItems());
    }
}
