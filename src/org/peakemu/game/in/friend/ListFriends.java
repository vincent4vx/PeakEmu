/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.friend;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.friend.FriendListPacket;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ListFriends implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getAccount() == null)
            return;
        
        client.send(new FriendListPacket(client.getAccount().getFriendList()));
    }

    @Override
    public String header() {
        return "FL";
    }
    
}
