/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.guild;

import org.peakemu.objects.Collector;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CollectorAttackers {
    private Collector collector;

    public CollectorAttackers(Collector collector) {
        this.collector = collector;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        
        sb.append("gITp+").append(collector.getGuid());
        
        for(Fighter fighter : collector.getFight().getAttackers()){
            sb.append('|').append(Integer.toString(fighter.getSpriteId(), 36)).append(';').append(fighter.getPacketsName()).append(';').append(fighter.get_lvl());
        }
        
        return sb.toString();
    }
    
    
}
