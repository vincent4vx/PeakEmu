/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.team;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.peakemu.world.MapCell;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
abstract public class AbstractTeam implements FightTeam{
    private int number = -1;
    final private List<MapCell> startCells;
    final private int startCellsIndex;
    
    final private Set<Fighter> fighters = new HashSet<>();

    public AbstractTeam(List<MapCell> startCells, int startCellsIndex) {
        this.startCells = startCells;
        this.startCellsIndex = startCellsIndex;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public void setNumber(int number) {
        if(this.number != -1)
            throw new IllegalStateException("A Team number is already defined");
        
        this.number = number;
    }

    @Override
    public int getStartCellsIndex() {
        return startCellsIndex;
    }

    @Override
    public Set<Fighter> getFighters() {
        return Collections.unmodifiableSet(fighters);
    }

    @Override
    public List<MapCell> getStartCells() {
        return startCells;
    }

    @Override
    public void addFighter(Fighter fighter) {
        fighters.add(fighter);
    }

    @Override
    public boolean isAllDead() {
        for(Fighter fighter : fighters){
            if(!fighter.isDead())
                return false;
        }
        
        return true;
    }

    @Override
    public void removeFighter(Fighter fighter) {
        fighters.remove(fighter);
    }

    @Override
    public boolean containsFighter(Fighter fighter) {
        return fighters.contains(fighter);
    }

    @Override
    public void sendToTeam(Object packet) {
        for(Fighter fighter : fighters){
            if(fighter.getPlayer() != null)
                fighter.getPlayer().send(packet);
        }
    }

    @Override
    public boolean isReady() {
        for(Fighter fighter : fighters){
            if(fighter.getPlayer() != null && !fighter.isReady())
                return false;
        }
        
        return true;
    }

    @Override
    public int getSize() {
        return fighters.size();
    }
}
