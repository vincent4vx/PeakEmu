/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.world.SpellEffect;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Buff {
    final private SpellEffect spellEffect;
    final private Fighter caster;
    final private Fighter target;
    final private Fight fight;
    private int remainingTurns;
    private int value;
    private int arg2;
    private int arg3;
    private String arg4 = "";

    public Buff(SpellEffect spellEffect, Fighter caster, Fighter target, Fight fight, int remainingTurns) {
        this.spellEffect = spellEffect;
        this.caster = caster;
        this.target = target;
        this.fight = fight;
        this.remainingTurns = remainingTurns;
    }

    public SpellEffect getSpellEffect() {
        return spellEffect;
    }

    public Fighter getCaster() {
        return caster;
    }

    public Fighter getTarget() {
        return target;
    }

    public Fight getFight() {
        return fight;
    }

    public int getRemainingTurns() {
        return remainingTurns;
    }
    
    public void decrementTruns(){
        --remainingTurns;
    }
    
    public void incrementTurns(){
        ++remainingTurns;
    }

    public int getValue() {
        return value == 0 ? spellEffect.getMin() : value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getArg2() {
        return arg2 == 0 ? spellEffect.getMax() : arg2;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }

    public int getArg3() {
        return arg3 == 0 ? spellEffect.getArg3() : arg3;
    }

    public void setArg3(int arg3) {
        this.arg3 = arg3;
    }

    public String getArg4() {
        return arg4;
    }

    public void setArg4(String arg4) {
        this.arg4 = arg4;
    }
    
    public boolean isValid(){
        return remainingTurns > 0;
    }
}
