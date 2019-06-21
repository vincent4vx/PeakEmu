/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.maputil;

import java.util.ArrayList;
import java.util.List;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PathValidator {
    static public List<MapCell> validatePath(List<MapCell> path, CellChecker checker){
        List<MapCell> validatedPath = new ArrayList<>();
        
        boolean start = true;
        
        for(MapCell cell : path){
            if(!start && !checker.checkCell(cell)){
                Peak.worldLog.addToLog(Logger.Level.DEBUG, "Invalid cell %d. Stop path", cell.getID());
                break;
            }
            
            start = false;
            
            validatedPath.add(cell);
        }
        
        return validatedPath;
    }
    
    static public List<MapCell> validateRolePlayMovePath(List<MapCell> path){
        path = validatePath(path, CellChecker.WALKABLE);
        
        if(path.size() < 2)
            return path;
        
        MapCell destination = path.get(path.size() - 1);
        
        if(destination.isInteractive() && destination.getObject().isInteractive()){ //the destination is an IO => stop before
            path.remove(path.size() - 1);
            
            MapCell before = path.get(path.size() - 1);
            
            if(!MapUtil.isAdjacent(before, destination)){ //not adjacent : select a better destination
                for(MapCell newDest : MapUtil.getAdjacentCells(before)){
                    if(!newDest.isWalkable(true))
                        continue;
                    
                    if(!MapUtil.isAdjacent(destination, newDest))
                        continue;
                    
                    path.add(newDest); //newDest is walkable and adjacent from before and destination
                    break;
                }
            }
        }
        
        return path;
    }
}
