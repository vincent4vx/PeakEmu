/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.area;

import java.util.Collection;
import org.peakemu.common.Constants;
import org.peakemu.world.SubArea;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SubAreasList {
    final private Collection<SubArea> subAreas;

    public SubAreasList(Collection<SubArea> subAreas) {
        this.subAreas = subAreas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("al");
        
        for (SubArea subArea : subAreas) {
            if(subArea.getAlignement() == Constants.ALIGNEMENT_NONE)
                continue;
            
            sb.append('|').append(subArea.getId()).append(';').append(subArea.getAlignement());
        }
        
        return sb.toString();
    }
    
    
}
