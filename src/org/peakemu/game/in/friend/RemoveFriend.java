/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.friend;

import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Account;
import org.peakemu.objects.FriendList;
import org.peakemu.world.handler.AccountHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class RemoveFriend implements InputPacket<GameClient>{
    final private AccountHandler accountHandler;

    public RemoveFriend(AccountHandler accountHandler) {
        this.accountHandler = accountHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getAccount() == null)
            return;
        
        FriendList friendList = client.getAccount().getFriendList();
        char method = FriendList.SEARCH_BY_PLAYER_NAME;
        
        switch(args.charAt(0)){
            case FriendList.SEARCH_BY_ACCOUNT_PSEUDO:
                args = args.substring(1);
                method = FriendList.SEARCH_BY_ACCOUNT_PSEUDO;
                break;
            case FriendList.SEARCH_BY_PLAYER_NAME:
                args = args.substring(1);
                break;
        }
        
        Account toDelete = friendList.searchFriend(method, args);
        accountHandler.removeFromFriends(client.getAccount(), toDelete);
    }

    @Override
    public String header() {
        return "FD";
    }
    
}
