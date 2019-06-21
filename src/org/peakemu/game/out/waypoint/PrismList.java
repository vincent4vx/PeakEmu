/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.waypoint;

import java.util.Collection;
import org.peakemu.common.util.Pair;
import org.peakemu.objects.Prism;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PrismList {
    private Collection<Pair<Prism, Integer>> prisms;

    public PrismList(Collection<Pair<Prism, Integer>> prisms) {
        this.prisms = prisms;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        
        sb.append("Wp");
        
        for (Pair<Prism, Integer> prism : prisms) {
            sb.append('|').append(prism.getFirst().getMap().getId()).append(';');
            
            if(prism.getFirst().isOnFight()){
                sb.append('*');
            }else{
                sb.append(prism.getSecond());
            }
        }
        
        return sb.toString();
    }
    
    
}
