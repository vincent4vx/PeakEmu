/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.database.Database;
import org.peakemu.database.parser.StatsParser;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.item.factory.DefaultFactory;
import org.peakemu.objects.item.factory.ItemFactory;
import org.peakemu.world.Inventory;
import org.peakemu.world.ItemStorage;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.StatsTemplate;
import org.peakemu.world.enums.InventoryPosition;
import org.peakemu.world.enums.ItemType;
import org.peakemu.world.listener.InventoryListener;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class InventoryDAO {
    final private Database database;
    final private ItemDAO itemDAO;
    final private InventoryListener inventoryListener;
    
    final static public String OWNER_TYPE_PLAYER       = "INVENTORY";
    final static public String OWNER_TYPE_BANK         = "BANK";
    final static public String OWNER_TYPE_COLLECTOR    = "COLLECTOR";
    final static public String OWNER_TYPE_PLAYER_STORE = "STORE";
    final static public String OWNER_TYPE_MOUNT        = "MOUNT";
    
    final private Map<ItemType, ItemFactory> factories = new HashMap<>();
    final private ItemFactory defaultFactory = new DefaultFactory();

    public InventoryDAO(Database database, ItemDAO itemDAO) {
        this.database = database;
        this.itemDAO = itemDAO;
        inventoryListener = new InventoryListener() {

            @Override
            public void onRemove(Item item) {
                delete(item);
            }

            @Override
            public void onQuantityChange(Item item) {
                save(item);
            }

            @Override
            public void onAdd(Item item) {
                save(item);
            }

            @Override
            public void onMove(Item item) {
                save(item);
            }

            @Override
            public void onChange(Item item) {
                save(item);
            }
        };
    }
    
    public ItemFactory getFactory(ItemType type){
        if(!factories.containsKey(type))
            return defaultFactory;
        
        return factories.get(type);
    }
    
    public void setFactory(ItemType type, ItemFactory factory){
        factories.put(type, factory);
    }
    
    public Item createByRS(ResultSet RS) throws SQLException{
        int guid = RS.getInt("guid");
        int tempID = RS.getInt("template");
        int qua = RS.getInt("qua");
        int pos = RS.getInt("pos");
        StatsTemplate stats = StatsParser.parseStatsTemplate(RS.getString("stats"));

        ItemTemplate template = itemDAO.getById(tempID);

        if(template == null)
            return null;
        
        InventoryPosition position = InventoryPosition.fromId(pos);
        
        if(position == null)
            position = InventoryPosition.NO_EQUIPED;

        return getFactory(template.getType()).createItem(
            RS.getString("owner"), 
            guid, 
            template, 
            qua, 
            position, 
            stats
        );
    }
    
    public Inventory getInventory(String owner){
        return getStorage(new Inventory(owner));
    }
    
    public<T extends ItemStorage> T getStorage(T storage){
        String req = "SELECT * FROM items WHERE owner = ?";
        
        try(PreparedStatement stmt = database.prepare(req)) {
            stmt.setString(1, storage.getOwner());
            try(ResultSet RS = stmt.executeQuery()){
                while (RS.next()) {
                    Item item = createByRS(RS);

                    if(item != null)
                        storage.addItem(item);
                }
            }
        } catch (SQLException e) {
            Peak.errorLog.addToLog(e);
        }
        
        storage.addListener(inventoryListener);
        return storage;
    }
    
    public ItemStorage getBankStorage(int accountId){
        return getStorage(new ItemStorage(OWNER_TYPE_BANK + accountId));
    }
    
    public ItemStorage getCollectorStorage(int collectorId){
        return getStorage(new ItemStorage(OWNER_TYPE_COLLECTOR + collectorId));
    }
    
    public ItemStorage getPlayerStoreStorage(int playerId){
        return getStorage(new ItemStorage(OWNER_TYPE_PLAYER_STORE + playerId));
    }
    
    public ItemStorage getMountStorage(int mountId){
        return getStorage(new ItemStorage(OWNER_TYPE_MOUNT + mountId));
    }
    
    public void save(Item item){
        PreparedStatement stmt = null;
        
        try{
            stmt = database.prepare("REPLACE INTO items(guid, owner, template, qua, pos, stats) VALUES(?,?,?,?,?,?)");
            stmt.setInt(1, item.getGuid());
            stmt.setString(2, item.getOwner());
            stmt.setInt(3, item.getTemplate().getID());
            stmt.setInt(4, item.getQuantity());
            stmt.setInt(5, item.getPosition().getId());
            stmt.setString(6, StatsParser.statsTemplateToString(item.getStatsTemplate()));
            stmt.executeUpdate();
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }finally{
            try{
                stmt.close();
            }catch(Exception e){}
        }
    }
    
    public void delete(Item item){
        PreparedStatement stmt = null;
        
        try{
            stmt = database.prepare("DELETE FROM items WHERE guid = ? AND owner = ?");
            stmt.setInt(1, item.getGuid());
            stmt.setString(2, item.getOwner());
            stmt.executeUpdate();
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }finally{
            try{
                stmt.close();
            }catch(Exception e){}
        }
    }
    
    public void save(ItemStorage inventory){
        PreparedStatement stmt = null;
        
        try{
            stmt = database.prepare("DELETE FROM items WHERE owner = ?");
            stmt.setString(1, inventory.getOwner());
            stmt.executeUpdate();
            
            try{
                stmt.close();
            }catch(Exception e){}
            
            stmt = database.prepare("REPLACE INTO items(guid, owner, template, qua, pos, stats) VALUES(?,?,?,?,?,?)");
            
            for(Item item : inventory){
                stmt.setInt(1, item.getGuid());
                stmt.setString(2, item.getOwner());
                stmt.setInt(3, item.getTemplate().getID());
                stmt.setInt(4, item.getQuantity());
                stmt.setInt(5, item.getPosition().getId());
                stmt.setString(6, StatsParser.statsTemplateToString(item.getStatsTemplate()));
                stmt.executeUpdate();
            }
        }catch(Exception e){
            Peak.errorLog.addToLog(e);
        }finally{
            try{
                stmt.close();
            }catch(Exception e){
            }
        }
    }
    
    public void delete(ItemStorage inventory){
        inventory.removeListener(inventoryListener);
        try(PreparedStatement stmt = database.prepare("DELETE FROM items WHERE owner = ?")){
            stmt.setString(1, inventory.getOwner());
            stmt.execute();
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }

    public InventoryListener getInventoryListener() {
        return inventoryListener;
    }
}
