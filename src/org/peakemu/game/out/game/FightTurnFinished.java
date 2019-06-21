/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FightTurnFinished {
    final private Fighter fighter;

    public FightTurnFinished(Fighter fighter) {
        this.fighter = fighter;
    }

    @Override
    public String toString() {
        return "GTF" + fighter.getSpriteId();
    }
    
    
}
