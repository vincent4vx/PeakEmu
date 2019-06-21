/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

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
public class UnesquivablePointsLooseEffect extends AbstractFightEffect{
    final private Effect effect;

    public UnesquivablePointsLooseEffect(Effect effect) {
        this.effect = effect;
    }

    @Override
    protected void applyScope(EffectScope scope) {}

    @Override
    public Effect getEffect() {
        return effect;
    }

    @Override
    public EffectType getType() {
        return EffectType.DEBUFF;
    }

    @Override
    protected void applyBuff(Fight fight, SpellEffect spellEffect, Fighter caster, Fighter target) {
        int value = Util.rand(spellEffect.getMin(), spellEffect.getMax());
        Buff buff = new Buff(spellEffect, caster, target, fight, spellEffect.getTurns());
        buff.setValue(value);
        
        target.addBuff(buff);
        fight.sendToFight(GameActionResponse.fightEffect(effect, caster, target, -value));
        
        if(target.canPlay()){
            switch(effect){
                case REM_PA2:
                    target.setCurPA(target.getCurPA() - value);
                    break;
                case REM_PM2:
                    target.setCurPM(target.getCurPM() - value);
                    break;
            }
        }
    }
    
    
}
