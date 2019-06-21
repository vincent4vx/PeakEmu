/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.maputil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.peakemu.common.util.Util;
import org.peakemu.world.GameMap;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MapUtil {
    static public MapCell getNearFreeCell(MapCell cell){
        for(Direction direction : Direction.RESTRICTED_DIRECTIONS){
            MapCell target = direction.nextCell(cell);
            
            if(target == null)
                continue;
            
            if(target.isWalkable(true) && target.getMap().isFreeCell(target))
                return target;
        }
        
        return null;
    }
    
    static public List<MapCell> getCellsAround(MapCell cell, int size){
        List<MapCell> cells = new ArrayList<>();
        Coordinate coordinate = Coordinate.fromCell(cell);
        
        for(MapCell c : cell.getMap().getCells()){
            Coordinate tmp = Coordinate.fromCell(c);
            
            if(coordinate.getDistance(tmp) <= size)
                cells.add(c);
        }
        
        return cells;
    }
    
    static public MapCell getRandomFreeCell(GameMap map, Collection<MapCell> cells){
        List<MapCell> free = new ArrayList<>(cells.size());
        
        for(MapCell cell : cells){
            if(!cell.isWalkable(true))
                continue;
            
            if(!map.isFreeCell(cell))
                continue;
            
            free.add(cell);
        }
        
        if(free.isEmpty())
            return null;
        
        return Util.rand(free);
    }
    
    static public boolean isAdjacent(MapCell cell1, MapCell cell2){
        if(cell1.equals(cell2))
            return true;
        
        for(Direction direction : Direction.RESTRICTED_DIRECTIONS){
            if(direction.nextCell(cell1).equals(cell2))
                return true;
        }
        
        return false;
    }
    
    static public Collection<MapCell> getAdjacentCells(MapCell cell){
        Collection<MapCell> cells = new ArrayList<>(4);
        
        for(Direction direction : Direction.RESTRICTED_DIRECTIONS){
            MapCell adj = direction.nextCell(cell);
            
            if(adj != null)
                cells.add(adj);
        }
        
        return cells;
    }
}
