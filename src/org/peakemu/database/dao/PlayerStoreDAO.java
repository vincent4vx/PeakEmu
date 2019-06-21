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
import org.peakemu.common.util.Pair;
import org.peakemu.database.Database;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.PlayerStore;
import org.peakemu.world.ItemStorage;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PlayerStoreDAO {
    final private Database database;
    final private InventoryDAO inventoryDAO;

    public PlayerStoreDAO(Database database, InventoryDAO inventoryDAO) {
        this.database = database;
        this.inventoryDAO = inventoryDAO;
    }
    
    public PlayerStore getStoreByPlayerId(int playerId){
        ItemStorage storage = inventoryDAO.getPlayerStoreStorage(playerId);
        Map<Item, Long> prices = new HashMap<>();
        
        try(PreparedStatement stmt = database.prepare("SELECT item_id, price FROM player_store WHERE owner = ?")){
            stmt.setString(1, InventoryDAO.OWNER_TYPE_PLAYER_STORE + playerId);
            
            try(ResultSet RS = stmt.executeQuery()){
                while(RS.next()){
                    int itemId = RS.getInt("item_id");
                    long price = RS.getLong("price");
                    Item item = storage.get(itemId);
                    prices.put(item, price);
                }
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return new PlayerStore(storage, prices);
    }
    
    public void save(PlayerStore store){
        try{
            try(PreparedStatement stmt = database.prepare("DELETE FROM player_store WHERE owner = ?")){
                stmt.setString(1, store.getOwner());
                stmt.execute();
            }
            
            try(PreparedStatement stmt = database.prepare("REPLACE INTO player_store(owner, item_id, price) VALUES(?,?,?)")){
                for(Pair<Item, Long> sell : store.getItems()){
                    stmt.setString(1, store.getOwner());
                    stmt.setInt(2, sell.getFirst().getGuid());
                    stmt.setLong(3, sell.getSecond());
                    stmt.execute();
                }
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
    
    public void delete(PlayerStore store){
        try(PreparedStatement stmt = database.prepare("DELETE FROM player_store WHERE owner = ?")){
            stmt.setString(1, store.getOwner());
            stmt.execute();
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        inventoryDAO.delete(store.getItemStorage());
    }
}
