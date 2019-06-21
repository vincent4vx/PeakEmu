/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.maputil;

import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PathUtil {
    static public MapCell getNextAlignedCell(MapCell casterCell, MapCell targetCell, int distance, CellChecker cellChecker) throws PathException{
        if(casterCell.equals(targetCell))
            throw new PathException("Same cells can't have an alignement");
        
        double angle = Coordinate.fromCell(casterCell).getAngle(Coordinate.fromCell(targetCell));
        Direction direction = Direction.getDirectionByAngle(angle);
        
        Peak.worldLog.addToLog(Logger.Level.DEBUG, "Repulse direction : %s", direction);
        
        if(distance < 0){
            direction = direction.getOppositeDirection();
            distance = -distance;
        }
        
        MapCell cell = targetCell;
        
        for(;distance > 0; --distance){
            MapCell next = direction.nextCell(cell);
            
            if(next.equals(casterCell) || !cellChecker.checkCell(next))
                throw new PathStopException(cell, distance + 1);
            
            cell = next;
        }
        
        return cell;
    }
}
