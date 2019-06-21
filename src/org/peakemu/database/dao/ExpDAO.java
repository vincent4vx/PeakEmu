/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.peakemu.Peak;
import org.peakemu.database.Database;
import org.peakemu.world.ExpLevel;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ExpDAO {
    final private Database database;
    
    final private List<ExpLevel> levels = new ArrayList<>();

    public ExpDAO(Database database) {
        this.database = database;
    }
    
    public List<ExpLevel> getAll(){
        if(!levels.isEmpty())
            return levels;
        
        try(ResultSet RS = database.query("SELECT * FROM experience WHERE lvl >= 1 ORDER BY lvl")){
            ExpLevel lastLevel = null;
            
            while(RS.next()){
                ExpLevel level = new ExpLevel(
                    RS.getInt("lvl"), 
                    RS.getLong("perso"), 
                    RS.getLong("metier"), 
                    RS.getLong("dinde"), 
                    RS.getInt("pvp")
                );
                
                if(lastLevel != null)
                    lastLevel.setNext(level);
                
                lastLevel = level;
                levels.add(level);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return levels;
    }
    
    public ExpLevel getLevel(int level){
        return getAll().get(level - 1);
    }
}
