/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.HashMap;
import java.util.Map;
import org.peakemu.database.parser.StatsParser;
import org.peakemu.world.enums.Effect;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Stats {

    final private Map<Effect, Integer> effects = new HashMap<>();

    public Stats() {
    }

    public Stats(Stats other) {
        effects.putAll(other.effects);
    }

    public int addOneStat(Effect stat, int val) {
        effects.put(stat, effects.getOrDefault(stat, 0) + val);
        return effects.get(stat);
    }

    public boolean equals(Stats other) {
        for(Map.Entry<Effect, Integer> entry : effects.entrySet()){
            if(other.effects.get(entry.getKey()) != entry.getValue())
                return false;
        }
        
        for(Map.Entry<Effect, Integer> entry : other.effects.entrySet()){
            if(effects.get(entry.getKey()) != entry.getValue())
                return false;
        }
        
        return true;
    }

    public int getEffect(Effect stat) {
        if(!effects.containsKey(stat))
            return 0;
        
        int val = effects.get(stat);
        
        for(Effect se : stat.getOppositeStats()){
            val -= effects.getOrDefault(se, 0);
        }
        
        for(Effect se : stat.getEquivalentStats()){
            val += effects.getOrDefault(se, 0);
        }
        
        return val;
    }
    
    public Stats addAll(Stats stats){
        for(Map.Entry<Effect, Integer> entry : stats.effects.entrySet()){
            addOneStat(entry.getKey(), entry.getValue());
        }
        
        return this;
    }

    @Deprecated
    public static Stats cumulStat(Stats s1, Stats s2) {
        Stats stats = new Stats(s1);
        return stats.addAll(s2);
    }

    public Map<Effect, Integer> getMap() {
        return effects;
    }

    @Deprecated
    public String parseToItemSetStats() {
        return StatsParser.serializeItemStats(this);
    }

    static public Stats parseStringStats(String strStats) {
        Stats stats = new Stats();

        String[] split = strStats.split(",");
        for (String s : split) {
            try {
                String[] statsData = s.split("#");
                int statID = Integer.parseInt(statsData[0], 16);
                int value = Integer.parseInt(statsData[1], 16);
                stats.addOneStat(Effect.valueOf(statID), value);
            } catch (Exception e) {}
        }

        return stats;
    }
}
