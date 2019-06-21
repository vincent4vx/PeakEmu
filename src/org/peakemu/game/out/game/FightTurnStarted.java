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
public class FightTurnStarted {
    final private Fighter fighter;
    final private int time;

    public FightTurnStarted(Fighter fighter, int time) {
        this.fighter = fighter;
        this.time = time;
    }

    @Override
    public String toString() {
        return "GTS" + fighter.getSpriteId() + "|" + time;
    }
    
    
}
