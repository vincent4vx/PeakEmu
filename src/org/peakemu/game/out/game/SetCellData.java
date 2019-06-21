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
public class SetCellData {
    final private MapCell cell;
    final private String data;
    final private String arg3;

    public SetCellData(MapCell cell, String data, String arg3) {
        this.cell = cell;
        this.data = data;
        this.arg3 = arg3;
    }

    @Override
    public String toString() {
        return "GDC" + cell.getID() + ";" + data + ";" + arg3;
    }
    
    
}
