/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FriendList {
    final private Account account;
    
    final private Set<Account> friends = new HashSet<>();
    final private Set<Account> enemies = new HashSet<>();
    
    final static public char SEARCH_BY_ACCOUNT_PSEUDO = '*';
    final static public char SEARCH_BY_PLAYER_NAME    = '%';

    public FriendList(Account account) {
        this.account = account;
    }
    
    public boolean contains(Account account){
        return friends.contains(account) || enemies.contains(account);
    }
    
    public void addFriend(Account account){
        friends.add(account);
    }
    
    public void addEnemy(Account account){
        enemies.add(account);
    }
    
    public boolean isFriend(Account account){
        return friends.contains(account);
    }
    
    public boolean isRealFriend(Account other){
        return friends.contains(other) && other.getFriendList().isFriend(account);
    }
    
    public boolean isEnemy(Account account){
        return enemies.contains(account);
    }

    public Set<Account> getFriends() {
        return friends;
    }

    public Set<Account> getEnemies() {
        return enemies;
    }
    
    /**
     * 
     * @param method * for account pseudo, % for player name
     * @param toSearch the name to search
     * @param scope
     * @return 
     */
    public Account search(char method, String toSearch, Collection<Account> scope){
        Account result = null;
        
        for(Account a : scope){
            if(method == SEARCH_BY_ACCOUNT_PSEUDO && a.get_pseudo().equalsIgnoreCase(toSearch)){
                result = a;
                break;
            }
            
            if(method == SEARCH_BY_PLAYER_NAME){
                Player player = a.getCurPlayer();
                
                if(player != null && player.getName().equalsIgnoreCase(toSearch)){
                    result = a;
                    break;
                }
            }
        }
        
        return result;
    }
    
    public Account searchFriend(char method, String toSearch){
        return search(method, toSearch, friends);
    }
    
    public Account searchEnemy(char method, String toSearch){
        return search(method, toSearch, enemies);
    }
    
    public void removeFriend(Account friend){
        friends.remove(friend);
    }
    
    public void removeEnemy(Account enemy){
        enemies.remove(enemy);
    }
    
    public String parseToFriendPacket(Account friend){
        if(friend.getCurPlayer() == null)
            return friend.get_pseudo();
        
        Player player = friend.getCurPlayer();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append(friend.get_pseudo()).append(';')
          .append("?;") //TODO: 1 : solo / 2 : multi / ? : unknow 
          .append(player.getName()).append(';');
        
        if(friend.getFriendList().isFriend(account)){
            sb.append(player.getLevel()).append(';')
              .append(player.getAlignement()).append(';');
        }else{
            sb.append("?;-1;");
        }
        
        sb.append(player.getRace().ordinal()).append(';')
          .append(player.getGender()).append(';')
          .append(player.getGfxID());
        
        return sb.toString();
    }
}
