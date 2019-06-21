/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class InteractiveObjectsState {
    final private Collection<MapCell> cells;

    public InteractiveObjectsState(Collection<MapCell> cells) {
        this.cells = cells;
    }

    public InteractiveObjectsState(MapCell cell) {
        this.cells = Collections.singleton(cell);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("GDF");
        
        for(MapCell cell : cells){
            if(!cell.isInteractive())
                continue;
            
            sb.append('|').append(cell.getID()).append(';').append(cell.getObject().getState().getValue()).append(';').append(cell.getObject().isInteractive() ? "1" : "0");
        }
        
        return sb.toString();
    }
    
    
}
