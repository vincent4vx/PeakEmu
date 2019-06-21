/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.peakemu.common.util.Util;
import org.peakemu.objects.Monster;
import org.peakemu.objects.MonsterGroup;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FixedMonsterGroup {
    final private GameMap map;
    final private MapCell cell;
    final private int respawnTime;
    final private Collection<MonsterEntry> entries;

    public FixedMonsterGroup(GameMap map, MapCell cell, int respawnTime, Collection<MonsterEntry> entries) {
        this.map = map;
        this.cell = cell;
        this.respawnTime = respawnTime;
        this.entries = entries;
    }
    
    private long startFightTime = 0;
    
    static public class MonsterEntry{
        final private MonsterTemplate template;
        final private int minLevel;
        final private int maxLevel;

        public MonsterEntry(MonsterTemplate template, int minLevel, int maxLevel) {
            this.template = template;
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
        }

        public MonsterTemplate getTemplate() {
            return template;
        }

        public int getMinLevel() {
            return minLevel;
        }

        public int getMaxLevel() {
            return maxLevel;
        }
    }

    public GameMap getMap() {
        return map;
    }

    public MapCell getCell() {
        return cell;
    }
    
    public void setStartFightTime(){
        startFightTime = System.currentTimeMillis();
    }
    
    public void respawn(){
        if(startFightTime == -1)
            return;
        
        if(System.currentTimeMillis() < (startFightTime + respawnTime))
            return;
        
        startFightTime = -1;
        
        Collection<Monster> monsters = new ArrayList<>();
        
        for(MonsterEntry entry : entries){
            List<Monster> possible = new ArrayList<>();
            
            for(Monster monster : entry.template.getGrades()){
                if(monster.getLevel() >= entry.minLevel || monster.getLevel() <= entry.maxLevel)
                    possible.add(monster);
            }
            
            if(possible.isEmpty())
                continue;
            
            monsters.add(Util.rand(possible));
        }
        
        MonsterGroup group = new MonsterGroup(monsters, this);
        group.setSpriteId(map.getNextSpriteId());
        map.spawnGroup(group);
    }

    public Collection<MonsterEntry> getMonsters() {
        return entries;
    }

    public int getRespawnTime() {
        return respawnTime;
    }
}
