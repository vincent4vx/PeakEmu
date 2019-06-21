/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.common.util.Util;
import org.peakemu.world.Stats;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class HealEffect extends AbstractFightEffect{

    @Override
    protected void applyScope(EffectScope scope) {
        int value = Util.rand(scope.getSpellEffect().getMin(), scope.getSpellEffect().getMax());
        
        Stats stats = scope.getCaster().getTotalStats();
        double coef = 1 + (double)stats.getEffect(Effect.ADD_INTE) / 100;
        value *= coef;
        value += stats.getEffect(Effect.ADD_SOIN);
        
        scope.setValue(value);
        
        int maxHeal = scope.getTarget().getPDVMAX() - scope.getTarget().getPDV();
        
        if(value > maxHeal)
            value = maxHeal;
        
        scope.getTarget().addLifePoints(value, scope.getCaster());
    }

    @Override
    public Effect getEffect() {
        return Effect.HEAL;
    }

    @Override
    public EffectType getType() {
        return EffectType.HEAL;
    }
    
}
