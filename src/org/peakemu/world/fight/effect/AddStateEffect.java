/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;
import org.peakemu.world.enums.FighterState;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AddStateEffect extends AbstractFightEffect{

    @Override
    protected void applyScope(EffectScope scope) {
        int stateId = scope.getSpellEffect().getArg3();
        FighterState state = FighterState.fromId(stateId);
        
        if(state == null){
            Peak.gameLog.addToLog(Logger.Level.ERROR, "State %d is undefined", stateId);
            return;
        }
        
        scope.getTarget().addState(state);
    }

    @Override
    protected void applyBuff(Fight fight, SpellEffect spellEffect, Fighter caster, Fighter target) {
        int stateId = spellEffect.getArg3();
        FighterState state = FighterState.fromId(stateId);
        
        if(state == null){
            Peak.gameLog.addToLog(Logger.Level.ERROR, "State %d is undefined", stateId);
            return;
        }
        
        target.addState(state);
        
        super.applyBuff(fight, spellEffect, caster, target);
    }

    @Override
    public void onBuffEnd(Buff buff) {
        int stateId = buff.getArg3();
        FighterState state = FighterState.fromId(stateId);
        
        if(state == null){
            Peak.gameLog.addToLog(Logger.Level.ERROR, "State %d is undefined", stateId);
            return;
        }
        
        buff.getTarget().removeState(state);
    }

    @Override
    public Effect getEffect() {
        return Effect.ADD_STATE;
    }

    @Override
    public EffectType getType() {
        return EffectType.OTHER;
    }
}
