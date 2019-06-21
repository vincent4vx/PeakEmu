/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.friend;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FriendRemoved {
    private boolean success;

    public FriendRemoved(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        if(success)
            return "FDK";
        
        return "FDEf";
    }
    
    
}
