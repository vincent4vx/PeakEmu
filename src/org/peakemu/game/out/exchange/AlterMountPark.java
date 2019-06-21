/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.exchange;

import org.peakemu.game.out.mount.MountParser;
import org.peakemu.objects.Mount;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AlterMountPark {
    private char action;
    private Mount mount;
    
    final static public char ACTION_ADD    = '+';
    final static public char ACTION_REMOVE = '-';
    final static public char ACTION_ALTER  = '~';

    public AlterMountPark(char action, Mount mount) {
        this.action = action;
        this.mount = mount;
    }

    @Override
    public String toString() {
        return "Ee" + action + (action == ACTION_REMOVE ? mount.getId() : MountParser.parseMount(mount));
    }
    
    
}
