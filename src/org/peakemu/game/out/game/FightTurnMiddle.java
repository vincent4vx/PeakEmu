/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FightTurnMiddle {
    final private Fight fight;

    public FightTurnMiddle(Fight fight) {
        this.fight = fight;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GTM");
        
        for(Fighter fighter : fight.getAllFighters()){
            sb.append('|').append(fighter.getSpriteId()).append(';');
            
            if(fighter.isDead()){
                sb.append(1);
                continue;
            }
            
            sb.append("0;").append(fighter.getPDV()).append(';')
                .append(fighter.getPA()).append(';')
                .append(fighter.getPM()).append(';');
            
            if(fighter.isHidden())
                sb.append("-1;");
            else
                sb.append(fighter.getCell().getID()).append(';');
            
            sb.append(';').append(fighter.getPDVMAX());
        }
        
        return sb.toString();
    }
    
    
}
