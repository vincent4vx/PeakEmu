/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.friend;

import org.peakemu.objects.Account;
import org.peakemu.objects.FriendList;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class EnemyListPacket {
    private FriendList friendList;

    public EnemyListPacket(FriendList friendList) {
        this.friendList = friendList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("iL");
        
        for(Account friend : friendList.getEnemies()){
            sb.append('|').append(friendList.parseToFriendPacket(friend));
        }
        
        return sb.toString();
    }
    
    
}
