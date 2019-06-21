/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import org.peakemu.world.enums.Effect;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SpellEffect {
    final private Effect effect;
    final private SpellLevel spell;
    final private int min;
    final private int max;
    final private int arg3;
    final private int turns; //for buff
    final private int chance;
    final private String area;
    final private int target;

    public SpellEffect(Effect effect, SpellLevel spell, int min, int max, int arg3, int turns, int chance, String area, int target) {
        this.effect = effect;
        this.spell = spell;
        this.min = min;
        this.max = max;
        this.arg3 = arg3;
        this.turns = turns;
        this.chance = chance;
        this.area = area;
        this.target = target;
    }

    public Effect getEffect() {
        return effect;
    }

    public int getMin() {
        return min;
    }
    
    public int getArg1(){
        return min;
    }

    public int getMax() {
        return max;
    }
    
    public int getArg2(){
        return max;
    }

    public int getArg3() {
        return arg3;
    }

    public int getTurns() {
        return turns;
    }

    public int getChance() {
        return chance;
    }

    public String getArea() {
        return area;
    }

    public int getTarget() {
        return target;
    }

    public SpellLevel getSpell() {
        return spell;
    }
    
    public int getAverageJet(){
        if(max < min)
            return min;
        
        return (min + max) / 2;
    }
}
