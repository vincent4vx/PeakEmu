/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects.player;

import java.util.Map;
import org.peakemu.world.Spell;
import org.peakemu.world.SpellLevel;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SpellBook {
    final static public char FIRST_POSITION = 'b';
    final static public char POSITION_NONE  = '_';
    
    final private Map<Integer, SpellLevel> spells;
    final private Map<Integer, Character> positions;

    public SpellBook(Map<Integer, SpellLevel> spells, Map<Integer, Character> positions) {
        this.spells = spells;
        this.positions = positions;
    }

    public Map<Integer, SpellLevel> getSpells() {
        return spells;
    }
    
    public char getSpellPosition(int spellId){
        if(!positions.containsKey(spellId))
            return POSITION_NONE;
        
        return positions.get(spellId);
    }
    
    public boolean hasLearnedSpell(Spell spell){
        return hasLearnedSpell(spell.getId());
    }
    
    public boolean hasLearnedSpell(int spellId){
        SpellLevel spell = spells.get(spellId);
        return spell != null && spell.getLevel() > 0;
    }
    
    public SpellLevel getSpellById(int spellId){
        return spells.get(spellId);
    }
    
    public boolean setSpellPosition(int spellId, char newPosition){
        if(!hasLearnedSpell(spellId))
            return false;
        
        if(getSpellPosition(spellId) == newPosition)
            return false;
        
        if(newPosition != POSITION_NONE){
            for(Map.Entry<Integer, Character> entry : positions.entrySet()){
                if(entry.getValue() == newPosition)
                    entry.setValue(POSITION_NONE);
            }
        }
        
        positions.put(spellId, newPosition);
        return true;
    }
    
    public void autoSetPositions(){
        positions.clear();
        char curPos = FIRST_POSITION;
        
        for(SpellLevel spell : spells.values()){
            if(spell.getLevel() > 0)
                positions.put(spell.getSpellID(), curPos++);
        }
    }
    
    public boolean learnSpell(Spell spell){
        if(hasLearnedSpell(spell))
            return false;
        
        spells.put(spell.getId(), spell.getLevel(1));
        return true;
    }
    
    public boolean boostSpell(Spell spell){
        SpellLevel level = spells.get(spell.getId());
        
        if(level == null || level.isMaxLevel())
            return false;
        
        spells.put(spell.getId(), level.getNextLevel());
        return true;
    }
}
