/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

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
public class ItemActionDAO {
    final private Database database;
    
    final private Map<Integer, Collection<Action>> itemActions = new HashMap<>();

    public ItemActionDAO(Database database) {
        this.database = database;
    }
    
    private void createByRS(ResultSet RS) throws SQLException{
        int template = RS.getInt("template");
        
        if(!itemActions.containsKey(template))
            itemActions.put(template, new ArrayList<Action>());
        
        itemActions.get(template).add(new Action(RS.getInt("type"), RS.getString("args"), ""));
    }
    
    public Map<Integer, Collection<Action>> getAll(){
        if(!itemActions.isEmpty())
            return itemActions;
        
        try(ResultSet RS = database.query("SELECT * FROM use_item_actions")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return itemActions;
    }
}
