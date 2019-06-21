/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.conquest;

import java.util.Collection;
import org.peakemu.world.SubArea;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ConquestWorldData {
    final private int conquestedAreas;
    final private int conquestableAreas;
    final private int freeAreas;
    final private Collection<SubArea> areas;

    public ConquestWorldData(int conquestedAreas, int conquestableAreas, int freeAreas, Collection<SubArea> areas) {
        this.conquestedAreas = conquestedAreas;
        this.conquestableAreas = conquestableAreas;
        this.freeAreas = freeAreas;
        this.areas = areas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("CW");
        sb.append(conquestedAreas).append('|')
          .append(conquestableAreas).append('|')
          .append(freeAreas).append('|');
        
        boolean b = false;
        
        for (SubArea area : areas) {
            if(b)
                sb.append(';');
            else
                b = true;
            
            sb.append(area.getId()).append(',')
              .append(area.getAlignement()).append(',')
              .append("0,") //fighting ?
              .append(area.getPrism() != null ? area.getPrism().getMap().getId() : 0).append(',')
              .append(1); //attackable
        }
        
        sb.append('|');
        
        sb.append(0).append('|') //ownedVillages
          .append(0).append('|') //totalVillages
          .append(""); //villages data
        
        return sb.toString();
    }
    
    
}
