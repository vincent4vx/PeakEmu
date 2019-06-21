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
public class PathStopException extends PathException{
    final private MapCell stop;
    final private int remainingSteps;

    public PathStopException(MapCell stop, int remainingSteps) {
        super("Path stop at cell " + stop.getID() + ", remaining steps : " + remainingSteps);
        this.stop = stop;
        this.remainingSteps = remainingSteps;
    }

    public MapCell getStop() {
        return stop;
    }

    public int getRemainingSteps() {
        return remainingSteps;
    }
    
}
