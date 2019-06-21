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
import java.util.HashMap;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.StringUtil;
import org.peakemu.database.Database;
import org.peakemu.world.NPCQuestion;
import org.peakemu.world.NPCResponse;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class NPCQuestionDAO {
    final private Database database;
    final private NPCResponseDAO responseDAO;
    
    final private Map<Integer, NPCQuestion> questions = new HashMap<>();

    public NPCQuestionDAO(Database database, NPCResponseDAO responseDAO) {
        this.database = database;
        this.responseDAO = responseDAO;
    }
    
    private NPCQuestion createByRS(ResultSet RS) throws SQLException{
        int id = RS.getInt("ID");
        
        if(questions.containsKey(id))
            return questions.get(id);
        
        Collection<NPCResponse> responses = new ArrayList<>();
        
        for(String rId : StringUtil.split(RS.getString("responses"), ";")){
            if(rId.isEmpty())
                continue;
            
            try{
                responses.add(responseDAO.getResponseById(Integer.parseInt(rId)));
            }catch(NumberFormatException e){
                Peak.worldLog.addToLog(Logger.Level.ERROR, "Invalid npc_question response for id %d", id);
            }
        }
        
        NPCQuestion alternative = null;
        int aId = RS.getInt("ifFalse");
        
        if(aId > 0){
            alternative = getQuestionById(aId);
        }
        
        NPCQuestion question = new NPCQuestion(id, responses, RS.getString("params"), RS.getString("cond"), alternative);
        questions.put(id, question);
        return question;
    }
    
    public Collection<NPCQuestion> getAll(){
        if(!questions.isEmpty())
            return questions.values();
        
        try(ResultSet RS = database.query("SELECT * FROM npc_questions")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return questions.values();
    }
    
    public NPCQuestion getQuestionById(int id){
        if(questions.containsKey(id))
            return questions.get(id);
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM npc_questions WHERE ID = ?")){
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
