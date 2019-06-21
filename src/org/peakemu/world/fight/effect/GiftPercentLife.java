/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.common.util.Util;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GiftPercentLife extends AbstractFightEffect{

    @Override
    protected void applyScope(EffectScope scope) {
        double percent = (double)Util.rand(scope.getSpellEffect().getMin(), scope.getSpellEffect().getMax()) / 100;
        
        int value = (int)(scope.getCaster().getPDV() * percent);
        scope.setValue(value);
        
        scope.getCaster().removePDV(value, scope.getCaster());
        scope.getTarget().addLifePoints(value, scope.getCaster());
    }

    @Override
    public Effect getEffect() {
        return Effect.DON_PERCENT_VIE;
    }

    @Override
    public EffectType getType() {
        return EffectType.HEAL;
    }
    
}
