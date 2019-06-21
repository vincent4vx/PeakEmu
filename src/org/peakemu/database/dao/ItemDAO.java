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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.peakemu.Peak;
import org.peakemu.database.Database;
import org.peakemu.database.parser.StatsParser;
import org.peakemu.world.ItemSet;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.enums.ItemType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ItemDAO {
    final private Map<Integer, ItemTemplate> items = new ConcurrentHashMap<>();
    
    final private Database database;
    final private ItemSetDAO itemSetDAO;
    final private UseItemActionDAO useItemActionDAO;

    public ItemDAO(Database database, ItemSetDAO itemSetDAO, UseItemActionDAO useItemActionDAO) {
        this.database = database;
        this.itemSetDAO = itemSetDAO;
        this.useItemActionDAO = useItemActionDAO;
    }
    
    private ItemTemplate createByResultSet(ResultSet RS) throws SQLException{
        int itemSetId = RS.getInt("panoplie");
        
        ItemSet is = null;
        
        if(!RS.wasNull())
            is = itemSetDAO.getItemSetById(itemSetId);
        
        ItemTemplate it = new ItemTemplate(
            RS.getInt("id"),
            StatsParser.parseStatsTemplate(RS.getString("statsTemplate")),
            RS.getString("name"),
            ItemType.valueOf(RS.getInt("type")),
            RS.getInt("level"),
            RS.getInt("pod"),
            RS.getInt("prix"),
            is,
            RS.getString("condition"),
            RS.getString("armesInfos"),
            RS.getInt("sold"),
            RS.getInt("avgPrice"),
            useItemActionDAO.getActionsByItem(RS.getInt("id"))
        );
        
        if(is != null){
            is.addItemTemplate(it);
        }
        
        items.put(it.getID(), it);
        return it;
    }
    
    public Collection<ItemTemplate> getAll(){
        ResultSet RS = null;
        Collection<ItemTemplate> itemTemplates = new ArrayList<>();
        
        try {
            RS = database.query("SELECT * from item_template");
            while (RS.next()) {
                itemTemplates.add(createByResultSet(RS));
            }
        } catch (SQLException e) {
            System.out.println("Game: SQL ERROR: " + e.getMessage());
            System.exit(1);
        }finally{
            try{
                RS.close();
            }catch(Exception e){}
        }
        
        return itemTemplates;
    }
    
    public ItemTemplate getById(int id){
        if(items.containsKey(id))
            return items.get(id);
        
        ResultSet RS = null;
        PreparedStatement stmt = null;
        
        try{
            stmt = database.prepare("SELECT * FROM item_template WHERE id = ?");
            stmt.setInt(1, id);
            RS = stmt.executeQuery();
            
            if(!RS.next()){
                return null;
            }
            
            return createByResultSet(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
            return null;
        }finally{
            try{
                RS.close();
                stmt.close();
            }catch(Exception e){}
        }
    }
}
