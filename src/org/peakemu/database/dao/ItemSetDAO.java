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
import org.peakemu.game.GameServer;
import org.peakemu.world.ItemSet;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ItemSetDAO {
    final private Database database;
    
    final private Map<Integer, ItemSet> items = new HashMap<>();

    public ItemSetDAO(Database database) {
        this.database = database;
    }
    
    private ItemSet createByRS(ResultSet RS) throws SQLException{
        ItemSet is = new ItemSet(RS.getInt("id"), RS.getString("bonus"));
        items.put(is.getId(), is);
        return is;
    }
    
    public Collection<ItemSet> getAll(){
        Collection<ItemSet> itemSets = new ArrayList<>();
        
        try(ResultSet RS = database.query("SELECT * FROM itemsets")){
            while(RS.next()){
                itemSets.add(createByRS(RS));
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return itemSets;
    }
    
    public ItemSet getItemSetById(int id){    
        if(items.containsKey(id))
            return items.get(id);
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM itemsets WHERE id = ?")){
            stmt.setInt(1, id);
            try(ResultSet RS = stmt.executeQuery()){
                if(RS.next()){
                    return createByRS(RS);
                }
            }
        } catch (SQLException ex) {
            Peak.errorLog.addToLog(ex);
        }
        
        return null;
    }
}
