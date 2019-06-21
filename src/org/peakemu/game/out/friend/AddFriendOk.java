/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.friend;

import org.peakemu.objects.Account;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AddFriendOk {
    private Account adder;
    private Account added;

    public AddFriendOk(Account adder, Account added) {
        this.adder = adder;
        this.added = added;
    }

    @Override
    public String toString() {
        return "FAK" + adder.getFriendList().parseToFriendPacket(added);
    }
    
    
}
