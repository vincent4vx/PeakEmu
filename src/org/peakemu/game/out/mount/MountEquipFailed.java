/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.mount;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountEquipFailed {
    final static public char MOUNT_ERROR_INVENTORY_NOT_EMPTY = '-';
    final static public char MOUNT_ERROR_ALREADY_HAVE_ONE    = '+';
    final static public char MOUNT_ERROR_RIDE                = 'r';
    
    final private char error;

    public MountEquipFailed(char error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ReE" + error;
    }
    
    
}
