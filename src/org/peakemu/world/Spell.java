/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Spell {
    final static public int MIN_SPELL_LEVEL = 1;
    final static public int MAX_SPELL_LEVEL = 6;
    
    final private int id;
    final private String name;
    final private int sprite;
    final private String spriteInfos;
    final private int[] effectTarget;
    final private List<SpellLevel> levels = new ArrayList<>(MAX_SPELL_LEVEL);

    public Spell(int id, String name, int sprite, String spriteInfos, int[] effectTarget) {
        this.id = id;
        this.name = name;
        this.sprite = sprite;
        this.spriteInfos = spriteInfos;
        this.effectTarget = effectTarget;
        levels.add(createLevelZero());
    }
    
    private SpellLevel createLevelZero(){
        return new SpellLevel(this, 0, 0, 0, 0, 0, 0, false, false, false, false, 0, 0, 0, 0, "", Collections.EMPTY_SET, Collections.EMPTY_SET, 0, false);
    }
    
    public int getMaxLevel(){
        return levels.size() - 1;
    }
    
    public SpellLevel getLevel(int level){
        return levels.get(level);
    }
    
    public int getEffectTarget(int index){
        if(index >= effectTarget.length)
            return 0;
        
        return effectTarget[index];
    }
    
    public int[] getEffectTargets(){
        return effectTarget;
    }
    
    public void addSpellLevel(SpellLevel level){
        levels.add(level);
    }

    public int getId() {
        return id;
    }
    
    public int getSpellID(){
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSprite() {
        return sprite;
    }
    
    /**
     * @see Spell#getSprite() 
     * @return 
     */
    public int getSpriteID(){
        return sprite;
    }

    public String getSpriteInfos() {
        return spriteInfos;
    }
    
    public int getMinPlayerLevel(){
        return levels.get(1).getMinPlayerLevel();
    }
}
