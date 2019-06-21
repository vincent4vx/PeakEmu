/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.peakemu.world.enums.PlayerStat;
import org.peakemu.world.enums.PlayerRace;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ClassData {
    static public class BoostInterval{
        final public int minStats;
        final public int cost;
        final public int statsPerPoints;

        public BoostInterval(int minStats, int cost, int statsPerPoints) {
            this.minStats = minStats;
            this.cost = cost;
            this.statsPerPoints = statsPerPoints;
        }
    }
    
    final private PlayerRace race;
    final private String className;
    final private List<Spell> spells;
    final private Map<PlayerStat, List<BoostInterval>> statsBoosts;

    public ClassData(PlayerRace race, String className, List<Spell> spells, Map<PlayerStat, List<BoostInterval>> statsBoosts) {
        this.race = race;
        this.className = className;
        Collections.sort(spells, (a, b) -> a.getMinPlayerLevel() - b.getMinPlayerLevel());
        this.spells = Collections.unmodifiableList(spells);
        this.statsBoosts = statsBoosts;
    }

    public PlayerRace getRace() {
        return race;
    }

    public List<Spell> getSpells() {
        return spells;
    }
    
    public BoostInterval getRequiredBoostStatsPoints(PlayerStat stats, int curStats){
        List<BoostInterval> intervals = statsBoosts.get(stats);
        
        BoostInterval last = intervals.get(0);
        
        for(BoostInterval interval : intervals){
            if(curStats < interval.minStats)
                break;
            
            last = interval;
        }
        
        return last;
    }
    
    public Collection<Spell> getSpellsByLevel(int level){
        Collection<Spell> spells = new ArrayList<>();
        
        for(Spell spell : this.spells){
            if(level < spell.getMinPlayerLevel())
                break;
            
            if(spell.getMinPlayerLevel() > 0)
                spells.add(spell);
        }
        
        return spells;
    }
}
