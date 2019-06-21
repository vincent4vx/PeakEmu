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
public class EnemyRemoved {
    private boolean success;

    public EnemyRemoved(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        if(success)
            return "iDK";
        
        return "iDEf";
    }
    
    
}
