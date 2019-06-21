/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.var;

import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class NameVar implements Var{

    @Override
    public String name() {
        return "name";
    }

    @Override
    public Object getValue(Player player) {
        return player.getName();
    }
    
}
