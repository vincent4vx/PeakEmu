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
public class WaypointList {
    private String savePos;
    private Collection<Pair<WaypointHandler.Waypoint, Integer>> waypoints;

    public WaypointList(String savePos, Collection<Pair<WaypointHandler.Waypoint, Integer>> waypoints) {
        this.savePos = savePos;
        this.waypoints = waypoints;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(12 * waypoints.size());
        
        sb.append("WC").append(savePos);
        
        for(Pair<WaypointHandler.Waypoint, Integer> pair : waypoints){
            sb.append('|').append(pair.getFirst().getMap().getId()).append(';').append(pair.getSecond());
        }
        
        return sb.toString();
    }
    
    
}
