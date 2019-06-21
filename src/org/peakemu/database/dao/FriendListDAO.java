/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.peakemu.Peak;
import org.peakemu.database.Database;
import org.peakemu.objects.Account;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FriendListDAO {
    final private Database database;
    final private AccountDAO accountDAO;

    public FriendListDAO(Database database, AccountDAO accountDAO) {
        this.database = database;
        this.accountDAO = accountDAO;
    }
    
    public void load(){
        try(ResultSet RS = database.query("SELECT * FROM friend_list")){
            while(RS.next()){
                Account account = accountDAO.getAccountById(RS.getInt("account"));
                Account friend = accountDAO.getAccountById(RS.getInt("friend"));
                boolean isEnemy = RS.getBoolean("is_enemy");
                
                if(account == null || friend == null)
                    continue;
                
                if(isEnemy){
                    account.getFriendList().addEnemy(friend);
                }else{
                    account.getFriendList().addFriend(friend);
                }
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
    
    public boolean insert(Account account, Account friend, boolean isEnemy){
        try(PreparedStatement stmt = database.prepare("INSERT INTO friend_list(account, friend, is_enemy) VALUES(?,?,?)")){
            stmt.setInt(1, account.get_GUID());
            stmt.setInt(2, friend.get_GUID());
            stmt.setBoolean(3, isEnemy);
            stmt.execute();
            return true;
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
            return false;
        }
    }
    
    public boolean delete(Account account, Account friend){
        try(PreparedStatement stmt = database.prepare("DELETE FROM friend_list WHERE account = ? AND friend = ?")){
            stmt.setInt(1, account.get_GUID());
            stmt.setInt(2, friend.get_GUID());
            stmt.execute();
            return true;
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
            return false;
        }
    }
}
