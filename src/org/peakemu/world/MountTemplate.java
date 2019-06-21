/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.Map;
import org.peakemu.world.enums.Effect;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountTemplate {
    final private int id;
    final private String name;
    final private Stats finalStats;
    final private ItemTemplate parchment;

    public MountTemplate(int id, String name, Stats finalStats, ItemTemplate parchment) {
        this.id = id;
        this.name = name;
        this.finalStats = finalStats;
        this.parchment = parchment;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Stats getFinalStats() {
        return finalStats;
    }
    
    public Stats getStatsByLevel(int level){
        Stats stats = new Stats();
        
        for(Map.Entry<Effect, Integer> entry : finalStats.getMap().entrySet()){
            int value = entry.getValue() * level / 100;
            
            if(value == 0)
                continue;
            
            stats.addOneStat(entry.getKey(), value);
        }
        
        return stats;
    }

    public ItemTemplate getParchment() {
        return parchment;
    }
}
