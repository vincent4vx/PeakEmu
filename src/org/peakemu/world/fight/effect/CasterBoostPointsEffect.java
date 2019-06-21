/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.common.util.Util;
import org.peakemu.game.out.game.GameActionResponse;
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
public class CasterBoostPointsEffect implements FightEffect{
    final private Effect effect;

    public CasterBoostPointsEffect(Effect effect) {
        this.effect = effect;
    }

    @Override
    public Effect getEffect() {
        return effect;
    }

    @Override
    public EffectType getType() {
        return EffectType.BUFF;
    }

    @Override
    public void applyToFight(Fight fight, SpellEffect spellEffect, MapCell cell, Fighter caster) {
        int value = Util.rand(spellEffect.getMin(), spellEffect.getMax());
        
        switch(effect){
            case ADD_PA2:
                caster.setCurPA(caster.getCurPA() + value);
                break;
            case ADD_PM2:
                caster.setCurPM(caster.getCurPM() + value);
                break;
        }
        
        fight.sendToFight(GameActionResponse.fightEffect(effect, caster, caster, value));
    }

    @Override
    public void applyBuffOnStartTurn(Buff buff) {}

    @Override
    public void applyBuffOnEndTurn(Buff buff) {}

    @Override
    public void applyBuffOnDammages(Buff buff, EffectScope scope) {}

    @Override
    public void onBuffEnd(Buff buff) {}
}
