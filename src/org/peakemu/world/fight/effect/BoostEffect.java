/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.Util;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class BoostEffect extends AbstractFightEffect{
    final private Effect effect;

    public BoostEffect(Effect effect) {
        this.effect = effect;
    }

    @Override
    protected void applyScope(EffectScope scope) {}

    @Override
    protected void applyBuff(Fight fight, SpellEffect spellEffect, Fighter caster, Fighter target) {
        int value;
        
        if(spellEffect.getMax() < spellEffect.getMin())
            value = spellEffect.getMin();
        else
            value = Util.rand(spellEffect.getMin(), spellEffect.getMax());
        
        Buff buff = new Buff(spellEffect, caster, target, fight, spellEffect.getTurns());
        buff.setValue(value);
        
        Peak.worldLog.addToLog(Logger.Level.DEBUG, "Add boost buff %s for %d turns", spellEffect.getEffect(), spellEffect.getTurns());
        
        target.addBuff(buff);
        
        if(target.canPlay() && caster.equals(target)){
            switch(effect){
                case ADD_PA:
                    target.setCurPA(target.getCurPA() + value);
                    break;
                case ADD_PM:
                    target.setCurPM(target.getCurPM() + value);
                    break;
            }
        }
        
        fight.sendToFight(GameActionResponse.fightEffect(effect, caster, target, value, spellEffect.getTurns()));
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
    public void onBuffEnd(Buff buff) {
        switch(buff.getSpellEffect().getEffect()){
            case ADD_PA:
                buff.getFight().sendToFight(GameActionResponse.loosePAOnDebuff(buff.getTarget(), buff.getValue()));
                break;
            case ADD_PM:
                buff.getFight().sendToFight(GameActionResponse.loosePMOnDebuff(buff.getTarget(), buff.getValue()));
                break;
        }
    }
 
    
}
