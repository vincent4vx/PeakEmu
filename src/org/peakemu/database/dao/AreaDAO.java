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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.peakemu.Peak;
import org.peakemu.database.Database;
import org.peakemu.world.Area;
import org.peakemu.world.SuperArea;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AreaDAO {
    final private Database database;
    final private Map<Integer, Area> areas = new ConcurrentHashMap<>();
    final private Map<Integer, SuperArea> superAreas = new ConcurrentHashMap<>();

    public AreaDAO(Database database) {
        this.database = database;
    }

    private Area createByRS(ResultSet RS) throws SQLException {
        SuperArea sa = getSuperArea(RS.getInt("superarea"));
        
        Area area = new Area(
            RS.getInt("id"),
            RS.getString("name"),
            sa,
            RS.getInt("alignement")
        );
        sa.addArea(area);
        areas.put(area.getId(), area);
        
        return area;
    }
    
    public SuperArea getSuperArea(int id){
        if(!superAreas.containsKey(id)){
            superAreas.put(id, new SuperArea(id));
        }
        
        return superAreas.get(id);
    }

    public Collection<Area> getAll() {
        if(!areas.isEmpty())
            return areas.values();
        
        ResultSet RS = null;

        try {
            RS = database.query("SELECT * from area_data");

            while (RS.next()) {
                createByRS(RS);
            }
        } catch (SQLException e) {
            Peak.errorLog.addToLog(e);
        } finally {
            try{
                RS.close();
            }catch(Exception e){}
        }
        
        return areas.values();
    }
    
    public Area getAreaById(int id){
        if(!areas.containsKey(id)){
            try(PreparedStatement stmt = database.prepare("SELECT * FROM area_data WHERE id = ?")){
                stmt.setInt(1, id);
                try(ResultSet RS = stmt.executeQuery()){
                    if(RS.next())
                        return createByRS(RS);
                }
            }catch(SQLException e){
                Peak.errorLog.addToLog(e);
                return null;
            }
        }
        
        return areas.get(id);
    }
}
