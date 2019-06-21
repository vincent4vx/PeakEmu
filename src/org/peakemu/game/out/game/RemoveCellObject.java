/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class RemoveCellObject {
    private MapCell cell;

    public RemoveCellObject(MapCell cell) {
        this.cell = cell;
    }

    @Override
    public String toString() {
        return "GDO-" + cell.getID() + ";";
    }
    
    
}
