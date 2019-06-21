/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.world.MapCell;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.fighter.PlayerFighter;

/**
 *
 * @author Ludo
 */
public class CasterDammageEffect extends AbstractFightEffect{

    @Override
    public Effect getEffect() {
        return Effect.CASTER_DOMA;
    }

    @Override
    public EffectType getType() {
        return EffectType.DEBUFF;
    }
    
    @Override
    public void applyBuffOnDammages(Buff buff, EffectScope scope) {
        Effect boost = Effect.valueOf(buff.getValue());
        int max = buff.getArg2();
        int duration = buff.getArg3();
        
        for(Buff other : scope.getTarget().getBuffsByEffect(boost)){
            if(other.getRemainingTurns() == duration
                && other.getSpellEffect().getSpell().equals(buff.getSpellEffect().getSpell())) //other punishment effect on same turn
                max -= other.getValue();
        }
        
        int value = scope.getValue();
        
        if(scope.getCaster() instanceof PlayerFighter)
            value /= 2;
        
        if(value > max)
            value = max;
        
        if(value <= 0)
            return;
        
        SpellEffect fakeEffect = new SpellEffect(boost, buff.getSpellEffect().getSpell(), value, 0, 0, duration, 0, "Pa", 0);
        Buff boostBuff = new Buff(fakeEffect, buff.getTarget(), buff.getTarget(), buff.getFight(), duration);
        
        buff.getTarget().addBuff(boostBuff);
        scope.getFight().sendToFight(GameActionResponse.fightEffect(boost, buff.getTarget(), buff.getTarget(), value, duration));
    }

    @Override
    protected void applyScope(EffectScope scope) 
    {
        Fighter caster = scope.getCaster();
        caster.removePDV(scope.getSpellEffect().getMin(), caster);
    }

}
