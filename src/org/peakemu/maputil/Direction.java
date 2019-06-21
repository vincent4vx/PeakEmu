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
public enum Direction {
    EAST('a'){
        @Override
        public MapCell nextCell(MapCell cell) {
            return cell.getMap().getCell(cell.getID() + 1);
        }

        @Override
        public double angle() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    },
    SOUTH_EAST('b'){
        @Override
        public MapCell nextCell(MapCell cell) {
            int cellId = cell.getID() + cell.getMap().get_w();
            return cell.getMap().getCell(cellId);
        }

        @Override
        public double angle() {
            return 0;
        }
    },
    SOUTH('c'){
        @Override
        public MapCell nextCell(MapCell cell) {
            int cellId = cell.getID() + (2 * cell.getMap().get_w() - 1);
            return cell.getMap().getCell(cellId);
        }  

        @Override
        public double angle() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    },
    SOUTH_WEST('d'){
        @Override
        public MapCell nextCell(MapCell cell) {
            int cellId = cell.getID() + (cell.getMap().get_w() - 1);
            return cell.getMap().getCell(cellId);
        }

        @Override
        public double angle() {
            return Math.PI / 2;
        }
    },
    WEST('e'){
        @Override
        public MapCell nextCell(MapCell cell) {
            return cell.getMap().getCell(cell.getID() - 1);
        }

        @Override
        public double angle() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    },
    NORTH_WEST('f'){
        @Override
        public MapCell nextCell(MapCell cell) {
            int cellId = cell.getID() - cell.getMap().get_w();
            return cell.getMap().getCell(cellId);
        }

        @Override
        public double angle() {
            return Math.PI;
        }
    },
    NORTH('g'){
        @Override
        public MapCell nextCell(MapCell cell) {
            int cellId = cell.getID() - (2 * cell.getMap().get_w() - 1);
            return cell.getMap().getCell(cellId);
        }

        @Override
        public double angle() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    },
    NORTH_EAST('h'){
        @Override
        public MapCell nextCell(MapCell cell) {
            int cellId = cell.getID() - (cell.getMap().get_w() - 1);
            return cell.getMap().getCell(cellId);
        }

        @Override
        public double angle() {
            return -(Math.PI / 2);
        }
        
    };
    
    final static public Direction[] RESTRICTED_DIRECTIONS = new Direction[]{NORTH_EAST, NORTH_WEST, SOUTH_WEST, SOUTH_EAST};
    
    final private char direction;

    private Direction(char direction) {
        this.direction = direction;
    }
    
    public char toChar(){
        return direction;
    }
    
    abstract public MapCell nextCell(MapCell cell);
    abstract public double angle();
    
    public Direction getOppositeDirection(){
        return Direction.values()[(ordinal() + 4) % 8];
    }
    
    static public Direction getDirectionByChar(char c, boolean allDirection){
        for(Direction d : allDirection ? values() : RESTRICTED_DIRECTIONS){
            if(d.direction == c)
                return d;
        }
        return null;
    }
    
    static public Direction getDirectionByChar(char c){
        return getDirectionByChar(c, true);
    }
    
    static public Direction getDirectionByAngle(double angle){
        for(Direction d : RESTRICTED_DIRECTIONS){
            //0 <= a1 & a2 <= 2 PI
            double a1 = d.angle() + 2 * Math.PI;
            double a2 = angle + 2 * Math.PI;
            a1 %= 2 * Math.PI;
            a2 %= 2 * Math.PI;
            
            if(Math.abs(a1 - a2) <= Math.PI / 4)
                return d;
        }
        
        return null;
    }
}
