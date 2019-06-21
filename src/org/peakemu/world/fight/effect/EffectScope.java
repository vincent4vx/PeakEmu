/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class EffectScope {
    final private SpellEffect spellEffect;
    final private Fight fight;
    private int value;
    private Fighter caster;
    private Fighter target;
    
    private int reducedDammages = 0;

    public EffectScope(SpellEffect spellEffect, Fight fight, Fighter caster, Fighter target) {
        this.spellEffect = spellEffect;
        this.fight = fight;
        this.caster = caster;
        this.target = target;
    }

    public SpellEffect getSpellEffect() {
        return spellEffect;
    }

    public Fight getFight() {
        return fight;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Fighter getCaster() {
        return caster;
    }

    public void setCaster(Fighter caster) {
        this.caster = caster;
    }

    public Fighter getTarget() {
        return target;
    }

    public void setTarget(Fighter target) {
        this.target = target;
    }

    public int getReducedDammages() {
        return reducedDammages;
    }

    public void setReducedDammages(int reducedDammages) {
        this.reducedDammages = reducedDammages;
    }
    
    public void sendBuffResults(){
        if(reducedDammages > 0){
            fight.sendToFight(GameActionResponse.reducedDammages(caster, target, reducedDammages));
        }
    }
    
    static public EffectScope fromBuff(Buff buff){
        return new EffectScope(buff.getSpellEffect(), buff.getFight(), buff.getCaster(), buff.getTarget());
    }
}
