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
import org.peakemu.world.MapTriggers;
import org.peakemu.world.action.Action;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class TriggerDAO {
    final private Database database;
    
    final private Map<Short, MapTriggers> triggers = new HashMap<>();

    public TriggerDAO(Database database) {
        this.database = database;
    }
    
    private void createByRS(ResultSet RS) throws SQLException{
        short mapId = RS.getShort("MapID");
        short cellId = RS.getShort("CellID");
        Action action = new Action(RS.getInt("ActionID"), RS.getString("ActionsArgs"), RS.getString("Conditions"));
        
        if(!triggers.containsKey(mapId))
            triggers.put(mapId, new MapTriggers());
        
        triggers.get(mapId).addTrigger(cellId, action);
    }
    
    public Collection<MapTriggers> getAll(){
        if(!triggers.isEmpty())
            return triggers.values();
        
        try(ResultSet RS = database.query("SELECT * FROM `scripted_cells`")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return triggers.values();
    }
    
    public MapTriggers getTriggersByMap(short map){
        if(triggers.containsKey(map))
            return triggers.get(map);
        
        MapTriggers mapTriggers = new MapTriggers();
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM `scripted_cells` WHERE MapID = ?")){
            stmt.setShort(1, map);
            
            try(ResultSet RS = stmt.executeQuery()){
                triggers.put(map, mapTriggers);
                
                while(RS.next())
                    createByRS(RS);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return mapTriggers;
    }
}
