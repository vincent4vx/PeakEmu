/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.peakemu.Peak;
import org.peakemu.database.Database;
import org.peakemu.objects.Account;
import org.peakemu.objects.Bank;
import org.peakemu.world.ItemStorage;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AccountDAO {

    final private Database database;
    final private InventoryDAO inventoryDAO;

    final private Map<String, Account> accountsByName = new ConcurrentHashMap<>();
    final private Map<Integer, Account> accountsById = new ConcurrentHashMap<>();
    final private Map<String, Account> accountsByPseudo = new ConcurrentHashMap<>();

    final static public String TABLE_NAME = "accounts";

    public AccountDAO(Database database, InventoryDAO inventoryDAO) {
        this.database = database;
        this.inventoryDAO = inventoryDAO;
    }

    private Account createByRS(ResultSet RS) throws SQLException {
        ItemStorage bankStorage = inventoryDAO.getBankStorage(RS.getInt("guid"));
        Bank bank = new Bank(bankStorage, RS.getLong("bankKamas"));

        Account account = new Account(
            RS.getInt("guid"),
            RS.getString("account").toLowerCase(),
            RS.getString("pass"),
            RS.getString("pseudo"),
            RS.getString("question"),
            RS.getString("reponse"),
            RS.getInt("level"),
            RS.getInt("vip"),
            RS.getBoolean("banned"),
            RS.getString("lastIP"),
            RS.getString("lastConnectionDate"),
            bank,
            RS.getInt("cadeau")
        );

        accountsById.put(account.get_GUID(), account);
        accountsByName.put(account.getName().toLowerCase(), account);
        accountsByPseudo.put(account.get_pseudo().toLowerCase(), account);

        return account;
    }

    public Account getAccountById(int id) {
        if (accountsById.containsKey(id)) {
            return accountsById.get(id);
        }

        PreparedStatement stmt = null;
        ResultSet RS = null;

        try {
            stmt = database.prepare("SELECT * FROM " + TABLE_NAME + " WHERE guid = ?");
            stmt.setInt(1, id);
            RS = stmt.executeQuery();

            if (RS.next()) {
                return createByRS(RS);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Peak.errorLog.addToLog(ex);
            return null;
        } finally {
            try {
                stmt.close();
                RS.close();
            } catch (Exception e) {
            }
        }
    }

    public Account getAccountByName(String name) {
        if (accountsByName.containsKey(name.toLowerCase())) {
            return accountsByName.get(name.toLowerCase());
        }

        PreparedStatement stmt = null;
        ResultSet RS = null;

        try {
            stmt = database.prepare("SELECT * FROM " + TABLE_NAME + " WHERE account = ?");
            stmt.setString(1, name);
            RS = stmt.executeQuery();

            if (RS.next()) {
                return createByRS(RS);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Peak.errorLog.addToLog(ex);
            return null;
        } finally {
            try {
                stmt.close();
                RS.close();
            } catch (Exception e) {
            }
        }
    }

    public Account getAccountByPseudo(String pseudo) {
        if (accountsByPseudo.containsKey(pseudo.toLowerCase())) {
            return accountsByPseudo.get(pseudo.toLowerCase());
        }

        PreparedStatement stmt = null;
        ResultSet RS = null;

        try {
            stmt = database.prepare("SELECT * FROM " + TABLE_NAME + " WHERE pseudo = ?");
            stmt.setString(1, pseudo);
            RS = stmt.executeQuery();

            if (RS.next()) {
                return createByRS(RS);
            } else {
                return null;
            }
        } catch (SQLException e) {
            Peak.errorLog.addToLog(e);
            return null;
        } finally {
            try {
                stmt.close();
                RS.close();
            } catch (Exception e) {
            }
        }
    }

    public void save(Account account) {
        String baseQuery = "UPDATE accounts SET "
            + "`bankKamas` = ?,"
            + "`level` = ?,"
            + "`banned` = ?"
            + " WHERE `guid` = ?;";
        
        try(PreparedStatement p = database.prepare(baseQuery)){

            p.setLong(1, account.getBank().getKamas());
            p.setInt(2, account.get_gmLvl());
            p.setInt(3, (account.isBanned() ? 1 : 0));
            p.setInt(4, account.get_GUID());

            p.executeUpdate();
        } catch (SQLException e) {
            Peak.errorLog.addToLog(e);
        }
    }
    
    public Collection<Account> getAll(){
        if(!accountsById.isEmpty())
            return accountsById.values();
        
        try(ResultSet RS = database.query("SELECT * FROM accounts")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return accountsById.values();
    }
}
