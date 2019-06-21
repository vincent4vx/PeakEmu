/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import org.peakemu.world.MapCell;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FightCellShown {
    private Fighter fighter;
    private MapCell cell;

    public FightCellShown(Fighter fighter, MapCell cell) {
        this.fighter = fighter;
        this.cell = cell;
    }

    @Override
    public String toString() {
        return "Gf" + fighter.getSpriteId() + "|" + cell.getID();
    }
    
    
}
