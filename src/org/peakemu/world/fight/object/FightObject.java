/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.object;

import org.peakemu.maputil.Coordinate;
import org.peakemu.world.MapCell;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.OutputFilter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
abstract public class FightObject {
    final private MapCell cell;
    final private int size;
    final private Fighter caster;
    final private int color;
    final private SpellLevel layingSpell;

    public FightObject(MapCell cell, int size, Fighter caster, int color, SpellLevel layingSpell) {
        this.cell = cell;
        this.size = size;
        this.caster = caster;
        this.color = color;
        this.layingSpell = layingSpell;
    }
    
    abstract public String getCellData();

    public MapCell getCell() {
        return cell;
    }

    public int getSize() {
        return size;
    }

    public Fighter getCaster() {
        return caster;
    }

    public int getColor() {
        return color;
    }
    
    public boolean isOnArea(MapCell c){
        int distance = Coordinate.getDistanceBetweenCells(cell, c);
        return distance <= size;
    }

    public SpellLevel getLayingSpell() {
        return layingSpell;
    }
    
    abstract public OutputFilter getOutputFilter();
}
