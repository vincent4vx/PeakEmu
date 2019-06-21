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
import org.peakemu.world.NPCResponse;
import org.peakemu.world.action.Action;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class NPCResponseDAO {
    final private Database database;
    
    final private Map<Integer, NPCResponse> responses = new HashMap<>();

    public NPCResponseDAO(Database database) {
        this.database = database;
    }
    
    private void createByRS(ResultSet RS) throws SQLException{
        int id = RS.getInt("ID");
        
        if(!responses.containsKey(id))
            responses.put(id, new NPCResponse(id));
        
        responses.get(id).addAction(new Action(
            RS.getInt("type"), 
            RS.getString("args"), 
            ""
        ));
    }
    
    public Collection<NPCResponse> getAll(){
        if(!responses.isEmpty())
            return responses.values();
        
        try(ResultSet RS = database.query("SELECT * FROM npc_reponses_actions")){
            while(RS.next()){
                createByRS(RS);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return responses.values();
    }
    
    public NPCResponse getResponseById(int id){
        if(responses.containsKey(id))
            return responses.get(id);
        
        NPCResponse response = new NPCResponse(id);
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM npc_reponses_actions WHERE ID = ?")){
            stmt.setInt(1, id);
            
            try(ResultSet RS = stmt.executeQuery()){
                responses.put(id, response);
                while(RS.next())
                    createByRS(RS);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return response;
    }
}
