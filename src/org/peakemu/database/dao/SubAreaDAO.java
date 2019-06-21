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
import org.peakemu.world.SubArea;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SubAreaDAO {
    final private Database database;
    final private AreaDAO areaDAO;
    
    final private Map<Integer, SubArea> subAreas = new ConcurrentHashMap<>();

    public SubAreaDAO(Database database, AreaDAO areaDAO) {
        this.database = database;
        this.areaDAO = areaDAO;
    }
    
    private SubArea createByRS(ResultSet RS) throws SQLException{
        Area area = areaDAO.getAreaById(RS.getInt("area"));
        SubArea SA = new SubArea(
            RS.getInt("id"),
            area,
            RS.getInt("alignement"),
            RS.getString("name"),
            RS.getBoolean("isFree")
        );
                
        subAreas.put(SA.getId(), SA);
        
        if(area != null)
            area.addSubArea(SA);
        
        return SA;
    }

    public Collection<SubArea> getAll() {
        if(!subAreas.isEmpty())
            return subAreas.values();
        
        ResultSet RS = null;
        
        try {
            RS = database.query("SELECT * from subarea_data");
            
            while (RS.next()) {
                createByRS(RS);
            }
        } catch (SQLException e) {
            Peak.errorLog.addToLog(e);
        }finally{
            try{
                RS.close();
            }catch(Exception e){}
        }
        
        return subAreas.values();
    }
    
    public SubArea getSubAreaById(int id){
        if(subAreas.containsKey(id))
            return subAreas.get(id);
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM subarea_data WHERE id = ?")){
            stmt.setInt(1, id);
            try(ResultSet RS = stmt.executeQuery()){
                if(RS.next())
                    return createByRS(RS);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return null;
    }
}
