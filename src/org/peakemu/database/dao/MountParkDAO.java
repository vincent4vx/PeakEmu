/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.database.Database;
import org.peakemu.objects.Guild;
import org.peakemu.objects.Mount;
import org.peakemu.world.GameMap;
import org.peakemu.world.MapCell;
import org.peakemu.objects.MountPark;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountParkDAO {
    final private Database database;
    final private MapDAO mapDAO;
    final private GuildDAO guildDAO;
    final private MountDAO mountDAO;
    final private PlayerDAO playerDAO;
    
    final private Map<GameMap, MountPark> parks = new HashMap<>();

    public MountParkDAO(Database database, MapDAO mapDAO, GuildDAO guildDAO, MountDAO mountDAO, PlayerDAO playerDAO) {
        this.database = database;
        this.mapDAO = mapDAO;
        this.guildDAO = guildDAO;
        this.mountDAO = mountDAO;
        this.playerDAO = playerDAO;
    }
    
    private MountPark createByRS(ResultSet RS) throws SQLException{
        GameMap map = mapDAO.getMapById(RS.getShort("mapid"));
        MapCell cell = map.getCell(RS.getInt("cellid"));
        Guild guild = guildDAO.getGuildById(RS.getInt("guild"));
        
        if(cell == null){
            Peak.worldLog.addToLog(Logger.Level.ERROR, "Invalid cell %d on map %d", RS.getInt("cellid"), map.getId());
            return null;
        }
        
        MountPark park = new MountPark(
            guild,
            map,
            cell,
            RS.getInt("size"),
            RS.getLong("price"),
            RS.getBoolean("public")
        );
        
        populateMountPark(park);
        
        parks.put(map, park);
        return park;
    }
    
    public Collection<MountPark> getAll(){
        if(!parks.isEmpty())
            return parks.values();
        
        try(ResultSet RS = database.query("SELECT * FROM mountpark_data")){
            while(RS.next()){
                createByRS(RS);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return parks.values();
    }
    
    public void save(MountPark park){
        try{
            try(PreparedStatement stmt = database.prepare("REPLACE INTO mountpark_data(mapid, cellid, size, guild, public, price) VALUES(?,?,?,?,?,?)")){
                int i = 0;
                stmt.setShort(++i, park.getMap().getId());
                stmt.setInt(++i, park.getCell().getID());
                stmt.setInt(++i, park.getSize());

                if(park.getGuild() != null)
                    stmt.setInt(++i, park.getGuild().getId());
                else
                    stmt.setNull(++i, Types.INTEGER);

                stmt.setBoolean(++i, park.isPublic());
                stmt.setLong(++i, park.getPrice());
                stmt.execute();
                parks.put(park.getMap(), park);
            }
            
            try(PreparedStatement stmt = database.prepare("DELETE FROM mountpark_mounts WHERE mountpark = ?")){
                stmt.setInt(1, park.getMap().getId());
                stmt.execute();
            }
            
            try(PreparedStatement stmt = database.prepare("REPLACE INTO mountpark_mounts(mountpark, mount, owner) VALUES(?,?,?)")){
                for(Map.Entry<Mount, Player> entry : park.getMountsWithOwner().entrySet()){
                    stmt.setShort(1, park.getMap().getId());
                    stmt.setInt(2, entry.getKey().getId());
                    stmt.setInt(3, entry.getValue().getId());
                    stmt.execute();
                }
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
    
    public MountPark getMountParkByMap(GameMap map){
        return getMountParkByMap(map, false);
    }
    
    public MountPark getMountParkByMap(GameMap map, boolean loadIfNeeded){
        if(parks.containsKey(map))
            return parks.get(map);
        
        if(!loadIfNeeded)
            return null;
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM mountpark_data WHERE mapid = ?")){
            stmt.setShort(1, map.getId());
            
            try(ResultSet RS = stmt.executeQuery()){
                if(RS.next())
                    return createByRS(RS);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return null;
    }
    
    private void populateMountPark(MountPark mountPark){
        try(PreparedStatement stmt = database.prepare("SELECT * FROM mountpark_mounts WHERE mountpark = ?")){
            stmt.setShort(1, mountPark.getMap().getId());
            
            try(ResultSet RS = stmt.executeQuery()){
                while(RS.next()){
                    Mount mount = mountDAO.getMountById(RS.getInt("mount"));
                    Player owner = playerDAO.getPlayerById(RS.getInt("owner"));
                    mountPark.addMount(mount, owner);
                }
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
    
    public void addMountInPark(Mount mount, MountPark park, Player owner){
        try(PreparedStatement stmt = database.prepare("INSERT INTO mountpark_mounts(mountpark, mount, owner) VALUES(?,?,?)")){
            stmt.setShort(1, park.getMap().getId());
            stmt.setInt(2, mount.getId());
            stmt.setInt(3, owner.getId());
            stmt.execute();
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
    
    public void removeMountFromPark(Mount mount, MountPark mountPark){
        try(PreparedStatement stmt = database.prepare("DELETE FROM mountpark_mounts WHERE mount = ? AND mountpark = ?")){
            stmt.setInt(1, mount.getId());
            stmt.setShort(2, mountPark.getMap().getId());
            stmt.execute();
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
}
