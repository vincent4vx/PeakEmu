/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.maputil;

import java.util.ArrayList;
import java.util.List;
import org.peakemu.Peak;
import org.peakemu.common.CryptManager;
import org.peakemu.common.Logger;
import org.peakemu.world.GameMap;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Compressor {
    static public String compressPath(List<MapCell> path) throws PathException{
        StringBuilder sb = new StringBuilder();
        
        MapCell last = null;
        for(MapCell cell : path){
            char d = 'a';
            
            if(last != null){
                Direction tmp = getDirectionBetweenTwoCells(last, cell, true);
                
                if(tmp == null){
                    throw new PathException("Cannot find direction");
                }
                
                d = tmp.toChar();
            }
            
            sb.append(d);
            sb.append(CryptManager.cellID_To_Code(cell.getID()));
            last = cell;
        }
        
        return sb.toString();
    }
    
    static public Direction getDirectionBetweenTwoCells(MapCell cell1, MapCell cell2, boolean allDirections){
        Direction[] directions = allDirections ? Direction.values() : Direction.RESTRICTED_DIRECTIONS;
        
        Direction best = null;
        int nbSteps = Integer.MAX_VALUE;
        
        for(Direction direction : directions){
            MapCell tmp = cell1;
            for(int i = 0; i < 64; ++i){
                tmp = direction.nextCell(tmp);
                
                if(tmp == null)
                    break;
                
                if(tmp.equals(cell2)){
                    if(i < nbSteps){
                        best = direction;
                        nbSteps = i;
                    }
                    break;
                }
            }
        }
        
        return best;
    }
    
    static public List<MapCell> uncompressPath(String sPath, GameMap map, MapCell startCell, boolean allDirections) throws PathException{
        List<MapCell> path = new ArrayList<>();
        
        MapCell lastCell = startCell;
        
        for(int i = 0; i < sPath.length(); i += 3){
            String sPathPart = sPath.substring(i, i + 3);
            Direction direction = Direction.getDirectionByChar(sPathPart.charAt(0), allDirections);
            
            if(direction == null){
                throw new PathException("Invalid direction " + sPathPart.charAt(0));
            }
            
            MapCell destination = map.getCell(CryptManager.cellCode_To_ID(sPathPart.substring(1, 3)));
            
            if(destination == null){
                throw new PathException("Invalid cell " + sPathPart.substring(1, 3));
            }
            
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Destination : %d", destination.getID());
            
            int c = 0;
            while(!lastCell.equals(destination) && c++ < 64){
                path.add(lastCell);
                MapCell next = direction.nextCell(lastCell);
                
                if(next == null)
                    throw new PathException("Invalid next cell of " + lastCell.getID() + " with direction " + direction);
                
                lastCell = next;
            }
            
            if(c >= 64){
                throw new PathException("Too many steps to reach destination");
            }
        }
        
        path.add(lastCell); //add destination
        
        return path;
    }
}
