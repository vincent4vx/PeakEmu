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
import org.peakemu.Peak;
import org.peakemu.database.Database;
import org.peakemu.world.LiveAction;
import org.peakemu.world.action.Action;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class LiveActionDAO {
    final private Database database;
    final private PlayerDAO playerDAO;

    public LiveActionDAO(Database database, PlayerDAO playerDAO) {
        this.database = database;
        this.playerDAO = playerDAO;
    }
    
    public Collection<LiveAction> load(){
        Collection<LiveAction> liveActions = new ArrayList<>();
        
        try(ResultSet RS = database.query("SELECT * FROM live_action")){
            while(RS.next()){
                liveActions.add(new LiveAction(
                    RS.getInt("id"), 
                    playerDAO.getPlayerById(RS.getInt("target")), 
                    new Action(RS.getInt("actionId"), RS.getString("args"), "")
                ));
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return liveActions;
    }
    
    public void delete(Collection<LiveAction> liveActions){
        try(PreparedStatement stmt = database.prepare("DELETE FROM live_action WHERE id = ?")){
            for(LiveAction la : liveActions){
                stmt.setInt(1, la.getId());
                stmt.execute();
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
}
