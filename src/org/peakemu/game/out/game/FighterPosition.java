/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import java.util.Collection;
import java.util.Collections;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FighterPosition {
    final private Collection<Fighter> fighters;

    public FighterPosition(Collection<Fighter> fighters) {
        this.fighters = fighters;
    }
    
    public FighterPosition(Fighter fighter){
        fighters = Collections.singleton(fighter);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("GIC");
        
        for(Fighter fighter : fighters){
            sb.append('|').append(fighter.getSpriteId()).append(';').append(fighter.getCell().getID());
        }
        
        return sb.toString();
    }
    
    
}
