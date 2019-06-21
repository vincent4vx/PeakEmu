/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.peakemu.world.action.Action;
import org.peakemu.world.enums.FightType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class EndFightAction {
    final private Map<FightType, Set<Action>> actions = new EnumMap<>(FightType.class);
    
    public void addAction(FightType type, Action action){
        if(!actions.containsKey(type))
            actions.put(type, new HashSet<Action>());
        
        actions.get(type).add(action);
    }
    
    public Set<Action> getActions(FightType type){
        return actions.getOrDefault(type, Collections.EMPTY_SET);
    }
}
