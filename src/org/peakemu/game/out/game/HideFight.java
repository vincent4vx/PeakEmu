/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import org.peakemu.world.fight.Fight;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class HideFight {
    private Fight fight;

    public HideFight(Fight fight) {
        this.fight = fight;
    }

    @Override
    public String toString() {
        return "Gc-" + fight.get_id();
    }
    
    
}
