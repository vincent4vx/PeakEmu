/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.world.MapCell;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public interface FightEffect {
    final static public int TARGET_NOT_TEAM    = 1;
    final static public int TARGET_NOT_SELF    = 2;
    final static public int TARGET_NOT_ENEMY   = 4;
    final static public int TARGET_ONLY_INVOC  = 8;
    final static public int TARGET_NOT_INVOC   = 16;
    final static public int TARGET_ONLY_CASTER = 32;
    
    final static public Effect[] CHANGE_TARGET_EFFECTS = new Effect[]{
        Effect.RETURNS_SPELL, Effect.SACRIFICE
    };
    
    public Effect getEffect();
    public EffectType getType();
    
    public void applyToFight(Fight fight, SpellEffect spellEffect, MapCell cell, Fighter caster);
    
    public void applyBuffOnStartTurn(Buff buff);
    public void applyBuffOnEndTurn(Buff buff);
    public void applyBuffOnDammages(Buff buff, EffectScope scope);
    
    public void onBuffEnd(Buff buff);
    
    static public boolean isChangeTargetEffect(Effect effect){
        for(Effect e : CHANGE_TARGET_EFFECTS){
            if(e == effect)
                return true;
        }
        return false;
    }
    
    static public boolean isValidTarget(Fighter caster, Fighter target, int effectTarget){
        if((effectTarget & TARGET_NOT_TEAM) == TARGET_NOT_TEAM
            && caster.getTeam().equals(target.getTeam()))
            return false;
        
        if((effectTarget & TARGET_NOT_SELF) == TARGET_NOT_SELF
            && caster.equals(target))
            return false;
        
        if((effectTarget & TARGET_NOT_ENEMY) == TARGET_NOT_ENEMY
            && !caster.getTeam().equals(target.getTeam()))
            return false;
        
        if((effectTarget & TARGET_ONLY_INVOC) == TARGET_ONLY_INVOC
            && !target.isInvocation())
            return false;
        
        if((effectTarget & TARGET_NOT_INVOC) == TARGET_NOT_INVOC
            && target.isInvocation())
            return false;
        
        if((effectTarget & TARGET_ONLY_CASTER) == TARGET_ONLY_CASTER
            && !target.equals(caster))
            return false;
        
        return true;
    }
}
