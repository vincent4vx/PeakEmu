/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.maputil;

import java.util.List;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PartialPathException extends PathfindingException{
    final private List<MapCell> path;

    public PartialPathException(List<MapCell> path, String message) {
        super(message);
        this.path = path;
    }

    public List<MapCell> getPartialPath() {
        return path;
    }
}
