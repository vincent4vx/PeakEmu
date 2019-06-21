/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.database.Database;
import org.peakemu.world.GameMap;
import org.peakemu.world.MapCell;
import org.peakemu.world.handler.WaypointHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class WaypointDAO {
    final private Database database;
    final private MapDAO mapDAO;
    
    final private Collection<WaypointHandler.Waypoint> zaapis = Collections.synchronizedCollection(new ArrayList<WaypointHandler.Waypoint>());

    public WaypointDAO(Database database, MapDAO mapDAO) {
        this.database = database;
        this.mapDAO = mapDAO;
    }
    
    private WaypointHandler.Waypoint createByRS(ResultSet RS) throws SQLException{
        GameMap map = mapDAO.getMapById(RS.getShort("mapID"));
        
        if(map == null){
            Peak.worldLog.addToLog(Logger.Level.ERROR, "MapId %d doesn't exists", RS.getInt("mapid"));
            return null;
        }
        
        MapCell cell = map.getCell(RS.getInt("cellID"));
        
        if(cell == null){
            Peak.worldLog.addToLog(Logger.Level.ERROR, "Invalid MapCell %d on map %d", RS.getInt("cellID"), map.getId());
            return null;
        }
        
        return new WaypointHandler.Waypoint(map, cell);
    }
    
    public Collection<WaypointHandler.Waypoint> getAllZaapis(){
        if(!zaapis.isEmpty())
            return zaapis;
        
        try(ResultSet RS = database.query("SELECT * FROM zaapi")){
            while(RS.next()){
                WaypointHandler.Waypoint waypoint = createByRS(RS);
                
                if(waypoint != null)
                    zaapis.add(waypoint);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return zaapis;
    }
    
    public void createZaapi(WaypointHandler.Waypoint waypoint){
        try(PreparedStatement stmt = database.prepare("INSERT INTO zaapi(mapID, cellID) VALUES(?,?)")){
            stmt.setInt(1, waypoint.getMap().getId());
            stmt.setInt(2, waypoint.getCell().getID());
            stmt.executeUpdate();
            zaapis.add(waypoint);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
}
