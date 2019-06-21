/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.waypoint;

import java.util.Collection;
import org.peakemu.common.util.Pair;
import org.peakemu.world.handler.WaypointHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ZaapiList {
    private Collection<Pair<WaypointHandler.Waypoint, Integer>> waypoints;

    public ZaapiList(Collection<Pair<WaypointHandler.Waypoint, Integer>> waypoints) {
        this.waypoints = waypoints;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(12 * waypoints.size());
        
        sb.append("Wc").append(0);
        
        for(Pair<WaypointHandler.Waypoint, Integer> pair : waypoints){
            sb.append('|').append(pair.getFirst().getMap().getId()).append(';').append(pair.getSecond());
        }
        
        return sb.toString();
    }
    
    
}
