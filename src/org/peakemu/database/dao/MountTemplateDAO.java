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
import org.peakemu.database.parser.StatsParser;
import org.peakemu.world.MountTemplate;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountTemplateDAO {
    final private Database database;
    final private ItemDAO itemDAO;
    
    final private Map<Integer, MountTemplate> templates = new HashMap<>();

    public MountTemplateDAO(Database database, ItemDAO itemDAO) {
        this.database = database;
        this.itemDAO = itemDAO;
    }
    
    private MountTemplate createByRS(ResultSet RS) throws SQLException{
        MountTemplate template = new MountTemplate(
            RS.getInt("mount_id"), 
            RS.getString("mount_name"), 
            StatsParser.parseCollectorStats(RS.getString("mount_final_stats")), 
            itemDAO.getById(RS.getInt("mount_parchment"))
        );
        
        templates.put(template.getId(), template);
        return template;
    }
    
    public Collection<MountTemplate> getAll(){
        if(!templates.isEmpty())
            return templates.values();
        
        try(ResultSet RS = database.query("SELECT * FROM mount_template")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return templates.values();
    }
    
    public void saveMountTemplate(MountTemplate template){
        try(PreparedStatement stmt = database.prepare("UPDATE mount_template SET mount_final_stats = ?, mount_parchment = ? WHERE mount_id = ?")){
            stmt.setString(1, StatsParser.serializeCollectorStats(template.getFinalStats()));
            stmt.setInt(2, template.getParchment().getID());
            stmt.setInt(3, template.getId());
            stmt.execute();
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
    
    public MountTemplate getMountTemplateById(int id){
        if(templates.containsKey(id))
            return templates.get(id);
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM mount_template WHERE mount_id = ?")){
            stmt.setInt(1, id);
            
            try(ResultSet RS = stmt.executeQuery()){
                if(!RS.next())
                    return null;
                
                return createByRS(RS);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
            return null;
        }
    }
}
