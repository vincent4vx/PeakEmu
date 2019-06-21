/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import java.util.Collection;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FightTurnList {
    final private Collection<Fighter> turnList;

    public FightTurnList(Collection<Fighter> turnList) {
        this.turnList = turnList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GTL");
        
        for (Fighter fighter : turnList) {
            sb.append('|').append(fighter.getSpriteId());
        }
        
        return sb.toString();
    }
}
