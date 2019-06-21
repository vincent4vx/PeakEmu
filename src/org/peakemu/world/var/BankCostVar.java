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
public class BankCostVar implements Var{

    @Override
    public String name() {
        return "bankCost";
    }

    @Override
    public Object getValue(Player player) {
        return player.getAccount().getBank().getCost();
    }
    
}
