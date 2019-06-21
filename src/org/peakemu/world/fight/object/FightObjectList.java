/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.object;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.peakemu.game.out.game.AddFightObject;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.game.out.game.RemoveFightObject;
import org.peakemu.game.out.game.SetCellData;
import org.peakemu.game.out.game.UnsetCellData;
import org.peakemu.world.MapCell;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FightObjectList {
    final private Map<MapCell, FightObject> fightObjects = new HashMap<>();
    final private Fight fight;

    public FightObjectList(Fight fight) {
        this.fight = fight;
    }
    
    public void addFightObject(FightObject fightObject){
        fightObjects.put(fightObject.getCell(), fightObject);
        fight.sendToFight(GameActionResponse.fakePacket(fightObject.getCaster(), new AddFightObject(fightObject)), fightObject.getOutputFilter());
        fight.sendToFight(GameActionResponse.fakePacket(fightObject.getCaster(), new SetCellData(fightObject.getCell(), fightObject.getCellData(), "")), fightObject.getOutputFilter());
    }
    
    public void removeFightObject(FightObject fightObject){
        fightObjects.remove(fightObject.getCell());
        fight.sendToFight(GameActionResponse.fakePacket(fightObject.getCaster(), new RemoveFightObject(fightObject)), fightObject.getOutputFilter());
        fight.sendToFight(GameActionResponse.fakePacket(fightObject.getCaster(), new UnsetCellData(fightObject.getCell())), fightObject.getOutputFilter());
    }
    
    public boolean isFreeCell(MapCell cell){
        return !fightObjects.containsKey(cell);
    }
    
    public boolean isOnTrap(MapCell cell){
        for(FightObject object : fightObjects.values()){
            if(!(object instanceof Trap))
                continue;
            
            Trap trap = (Trap)object;
            
            if(trap.isOnArea(cell))
                return true;
        }
        
        return false;
    }

    public void clear() {
        fightObjects.clear();
    }
    
    public Collection<Trap> getTraps(MapCell cell){
        Collection<Trap> traps = new ArrayList<>();
        
        for(FightObject object : fightObjects.values()){
            if(!(object instanceof Trap))
                continue;
            
            if(!object.isOnArea(cell))
                continue;
            
            traps.add((Trap)object);
        }
        
        return traps;
    }
    
    public Collection<Glyphe> getGlyphes(MapCell cell){
        Collection<Glyphe> glyphes = new ArrayList<>();
        
        for(FightObject object : fightObjects.values()){
            if(!(object instanceof Glyphe))
                continue;
            
            if(!object.isOnArea(cell))
                continue;
            
            glyphes.add((Glyphe)object);
        }
        
        return glyphes;
    }
    
    public void removeObjectsOnFighterDeath(Fighter fighter){
        for(FightObject fightObject : new ArrayList<>(fightObjects.values())){
            if(fightObject.getCaster().equals(fighter))
                removeFightObject(fightObject);
        }
    }
    
    public void refreshGlyphes(Fighter fighter){
        for(FightObject fightObject : new ArrayList<>(fightObjects.values())){
            if(!(fightObject instanceof Glyphe))
               continue;
            
            if(!fightObject.getCaster().equals(fighter))
                continue;
            
            Glyphe glyphe = (Glyphe)fightObject;
            
            
            glyphe.decrementDuration();
            
            if(glyphe.getDuration() == 0)
                removeFightObject(fightObject);
        }
    }
}
