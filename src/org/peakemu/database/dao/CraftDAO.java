/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.database.Database;
import org.peakemu.database.parser.CraftParser;
import org.peakemu.world.Craft;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CraftDAO {
    final private Database database;
    final private ItemDAO itemDAO;
    
    final private Map<Integer, Craft> crafts = new HashMap<>();

    public CraftDAO(Database database, ItemDAO itemDAO) {
        this.database = database;
        this.itemDAO = itemDAO;
    }
    
    private Craft createByRS(ResultSet RS) throws SQLException{
        Craft craft = new Craft(
            itemDAO.getById(RS.getInt("id")), 
            CraftParser.getRecipe(itemDAO, RS.getString("craft"))
        );
        
        crafts.put(craft.getItem().getID(), craft);
        return craft;
    }
    
    public Collection<Craft> getAll(){
        if(!crafts.isEmpty())
            return crafts.values();
        
        try(ResultSet RS = database.query("SELECT * FROM crafts")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return crafts.values();
    }
    
    public Craft getCraftById(int id){
        return crafts.get(id);
    }
}
