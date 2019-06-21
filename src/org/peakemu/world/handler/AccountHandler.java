/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import org.peakemu.database.dao.AccountDAO;
import org.peakemu.database.dao.FriendListDAO;
import org.peakemu.game.out.friend.AddEnemyError;
import org.peakemu.game.out.friend.AddEnemyOk;
import org.peakemu.game.out.friend.AddFriendError;
import org.peakemu.game.out.friend.AddFriendOk;
import org.peakemu.game.out.friend.EnemyRemoved;
import org.peakemu.game.out.friend.FriendRemoved;
import org.peakemu.objects.Account;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AccountHandler {
    final private AccountDAO accountDAO;
    final private FriendListDAO friendListDAO;

    public AccountHandler(AccountDAO accountDAO, FriendListDAO friendListDAO) {
        this.accountDAO = accountDAO;
        this.friendListDAO = friendListDAO;
    }

    public Account getAccountById(int id) {
        return accountDAO.getAccountById(id);
    }

    public Account getAccountByName(String name) {
        return accountDAO.getAccountByName(name);
    }

    public Account getAccountByPseudo(String pseudo) {
        return accountDAO.getAccountByPseudo(pseudo);
    }
    
    public void load(){
        System.out.print("Chargement des comptes : ");
        System.out.println(accountDAO.getAll().size() + " comptes chargés");
        
        System.out.print("Chargement des listes d'amis : ");
        friendListDAO.load();
        System.out.println("Ok");
    }
    
    public void addToFriends(Account account, Account toAdd){
        if(toAdd == null){
            account.send(new AddFriendError(AddFriendError.CANT_ADD_FRIEND_NOT_FOUND));
            return;
        }
        
        if(account.equals(toAdd)){
            account.send(new AddFriendError(AddFriendError.CANT_ADD_YOU));
            return;
        }
        
        if(account.getFriendList().contains(toAdd)){
            account.send(new AddFriendError(AddFriendError.ALREADY_YOUR_FRIEND));
            return;
        }
        
        
        if(friendListDAO.insert(account, toAdd, false)){
            account.getFriendList().addFriend(toAdd);
            account.send(new AddFriendOk(account, toAdd));
        }
    }
    
    public void removeFromFriends(Account account, Account toRemove){
        if(toRemove == null || !account.getFriendList().isFriend(toRemove)){
            account.send(new FriendRemoved(false));
            return;
        }
        
        if(friendListDAO.delete(account, toRemove)){
            account.getFriendList().removeFriend(toRemove);
            account.send(new FriendRemoved(true));
        }
    }
    
    public void addToEnemies(Account account, Account toAdd){
        if(toAdd == null){
            account.send(new AddEnemyError(AddEnemyError.CANT_ADD_FRIEND_NOT_FOUND));
            return;
        }
        
        if(account.equals(toAdd)){
            account.send(new AddEnemyError(AddEnemyError.CANT_ADD_YOU));
            return;
        }
        
        if(account.getFriendList().contains(toAdd)){
            account.send(new AddEnemyError(AddEnemyError.ALREADY_YOUR_ENEMY));
            return;
        }
        
        
        if(friendListDAO.insert(account, toAdd, true)){
            account.getFriendList().addEnemy(toAdd);
            account.send(new AddEnemyOk(account, toAdd));
        }
    }
    
    public void removeFromEnemies(Account account, Account toRemove){
        if(toRemove == null || !account.getFriendList().isEnemy(toRemove)){
            account.send(new EnemyRemoved(false));
            return;
        }
        
        if(friendListDAO.delete(account, toRemove)){
            account.getFriendList().removeEnemy(toRemove);
            account.send(new EnemyRemoved(true));
        }
    }
}
