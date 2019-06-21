/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.maputil;

import org.peakemu.world.MapCell;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public interface CellChecker {
    final static public CellChecker ALLOW_ALL = cell -> true;
    final static public CellChecker WALKABLE  = cell -> cell.isWalkable(true);
    
    public boolean checkCell(MapCell cell);
    
    static public CellChecker forFight(Fight fight, Fighter fighter){
        return new CellChecker() {
            private boolean invalidateNext = false;

            @Override
            public boolean checkCell(MapCell cell) {
                if(invalidateNext)
                    return false;
                
                if(fight.isOnTrap(cell)){
                    invalidateNext = true;
                }
                
                if(!fight.isFreeCell(cell))
                    return false;
                
                for(MapCell adj : MapUtil.getAdjacentCells(cell)){
                    Fighter other = adj.getFirstFighter();
                    
                    if(other == null)
                        continue;
                    
                    if(!other.getTeam().equals(fighter.getTeam())){ //ennemy found
                        invalidateNext = true;
                        break;
                    }
                }
                
                return true;
            }
        };
    }
    
    static public CellChecker forFightPathing(Fight fight){
        return new CellChecker() {
            @Override
            public boolean checkCell(MapCell cell) {
                return fight.isFreeCell(cell);
            }
        };
    }
}
