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
public class PercentLifeAttack extends AbstractFightEffect{
    final private Effect effect;

    public PercentLifeAttack(Effect effect) {
        this.effect = effect;
    }

    @Override
    protected void applyScope(EffectScope scope) {
        double dmg = (double)Util.rand(scope.getSpellEffect().getMin(), scope.getSpellEffect().getMax()) / 100;
        dmg *= scope.getCaster().getPDV();
        scope.setValue((int)dmg);
        
        scope.getFight().applyBuffsOnDammages(scope);
        
        int dammages = (int)EffectUtil.applyResistances(scope.getValue(), effect.getRelatedElement(), scope.getCaster(), scope.getTarget());
        
        scope.setValue(dammages);
        
        scope.getTarget().removePDV(dammages, scope.getCaster());
    }

    @Override
    public Effect getEffect() {
        return effect;
    }

    @Override
    public EffectType getType() {
        return EffectType.ATTACK;
    }
    
}
