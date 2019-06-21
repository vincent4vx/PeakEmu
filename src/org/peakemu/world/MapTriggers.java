/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.peakemu.world.action.Action;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MapTriggers {
    final private Map<Integer, Set<Action>> triggers = new HashMap<>();
    
    public Set<Action> getTriggersByCell(int cell){
        return triggers.getOrDefault(cell, Collections.EMPTY_SET);
    }
    
    public void addTrigger(int cell, Action action){
        if(!triggers.containsKey(cell)){
            triggers.put(cell, new HashSet<Action>());
        }
        
        triggers.get(cell).add(action);
    }
}
