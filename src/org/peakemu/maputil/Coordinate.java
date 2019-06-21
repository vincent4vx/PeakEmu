/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.maputil;

import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Coordinate {
    final private int x;
    final private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getDistance(Coordinate c, boolean useAllDirection){
        if(!useAllDirection)
            return Math.abs(c.x - x) + Math.abs(c.y - y);
        return (int)(Math.sqrt((c.x - x) * (c.x - x) + (c.y - y) * (c.y - y)));
    }
    
    public int getDistance(Coordinate c){
        return getDistance(c, false);
    }
    
    public double getAngle(Coordinate c){
        return Math.atan2(c.y - y, c.x - x);
    }

    @Override
    public String toString() {
        return "Coordinate{" + "x=" + x + ", y=" + y + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.x;
        hash = 29 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coordinate other = (Coordinate) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
    
    static public Coordinate fromCell(MapCell cell){
        int w = cell.getMap().get_w();
		int loc5 = (int)(cell.getID()/ ((w*2) -1));
		int loc6 = cell.getID() - loc5 * ((w * 2) -1);
		int loc7 = loc6 % w;
        
        int y = loc5 - loc7;
        int x = ((cell.getID() - (w -1) * y) / w);
        
        return new Coordinate(x, y);
    }
    
    static public int getDistanceBetweenCells(MapCell cell1, MapCell cell2){
        Coordinate c1 = fromCell(cell1);
        Coordinate c2 = fromCell(cell2);
        return c1.getDistance(c2);
    }
}
