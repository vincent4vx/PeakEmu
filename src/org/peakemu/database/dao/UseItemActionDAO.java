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
import org.peakemu.database.Database;
import org.peakemu.world.action.Action;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class UseItemActionDAO {
    final private Database database;
    
    final private Map<Integer, Collection<Action>> actions = new HashMap<>();

    public UseItemActionDAO(Database database) {
        this.database = database;
    }
    
    private void createByRS(ResultSet RS) throws SQLException{
        int tpl = RS.getInt("template");
        int action = RS.getInt("type");
        String args = RS.getString("args");
        
        if(!actions.containsKey(tpl))
            actions.put(tpl, new ArrayList<Action>());
        
        actions.get(tpl).add(new Action(action, args, ""));
    }
    
    public Map<Integer, Collection<Action>> getAll(){
        if(!actions.isEmpty())
            return actions;
        
        try(ResultSet RS = database.query("SELECT * FROM use_item_actions")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return actions;
    }
    
    public Collection<Action> getActionsByItem(int item){
        if(actions.containsKey(item))
            return actions.get(item);
        
        Collection<Action> a = new ArrayList<>();
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM use_item_actions WHERE template = ?")){
            stmt.setInt(1, item);
            
            try(ResultSet RS = stmt.executeQuery()){
                actions.put(item, a);
                
                while(RS.next())
                    createByRS(RS);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return a;
    }
}
