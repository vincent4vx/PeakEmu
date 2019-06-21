/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.peakemu.Peak;
import org.peakemu.database.Database;
import org.peakemu.world.action.Action;
import org.peakemu.world.enums.FightType;
import org.peakemu.world.fight.EndFightAction;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class EndFightActionDAO {
    final private Database database;
    
    final private Map<Short, EndFightAction> actions = new ConcurrentHashMap<>();

    public EndFightActionDAO(Database database) {
        this.database = database;
    }
    
    private void createByRS(ResultSet RS) throws SQLException{
        short mapId = RS.getShort("map");
        FightType type = FightType.values()[RS.getInt("fighttype")];
        
        if(!actions.containsKey(mapId))
            actions.put(mapId, new EndFightAction());
        
        actions.get(mapId).addAction(type, new Action(
            RS.getInt("action"), 
            RS.getString("args"), 
            RS.getString("cond")
        ));
    }
    
    public Map<Short, EndFightAction> getAll(){
        if(!actions.isEmpty())
            return actions;
        
        try(ResultSet RS = database.query("SELECT * FROM endfight_action")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return actions;
    }
    
    public EndFightAction getByMap(short map){
        if(actions.containsKey(map))
            return actions.get(map);
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM endfight_action WHERE map = ?")){
            stmt.setShort(1, map);
            
            EndFightAction efa = new EndFightAction();
            actions.put(map, efa);
            
            try(ResultSet RS = stmt.executeQuery()){
                while(RS.next()){
                    createByRS(RS);
                }
            }
            
            return efa;
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
            return new EndFightAction();
        }
    }
}
