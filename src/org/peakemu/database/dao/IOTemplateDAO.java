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
import org.peakemu.world.IOTemplate;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class IOTemplateDAO {
    final private Database database;

    final private Map<Integer, IOTemplate> templates = new HashMap<>();
    final private IOTemplate defaultTemplate = new IOTemplate(-1, 1500, 1500, -1, true);

    public IOTemplateDAO(Database database) {
        this.database = database;
    }

    private IOTemplate createByRS(ResultSet RS) throws SQLException {
        IOTemplate template = new IOTemplate(
            RS.getInt("id"),
            RS.getInt("respawn"),
            RS.getInt("duration"),
            RS.getInt("unknow"),
            RS.getBoolean("walkable")
        );
        
        templates.put(template.getId(), template);
        return template;
    }
    
    public Collection<IOTemplate> getAll(){
        if(!templates.isEmpty())
            return templates.values();
        
        try(ResultSet RS = database.query("SELECT * from interactive_objects_data")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return templates.values();
    }
    
    public IOTemplate getIOTemplateById(int id){
        if(templates.containsKey(id)){
            return templates.get(id);
        }
        
        try(PreparedStatement stmt = database.prepare("SELECT * from interactive_objects_data WHERE id = ?")){
            stmt.setInt(1, id);
            try(ResultSet RS = stmt.executeQuery()){
                if(RS.next()){
                    return createByRS(RS);
                }else{
                    templates.put(id, defaultTemplate);
                }
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return defaultTemplate;
    }
}
