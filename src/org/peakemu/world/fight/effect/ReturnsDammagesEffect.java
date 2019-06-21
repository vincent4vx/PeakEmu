/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.common.util.Util;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ReturnsDammagesEffect extends AbstractFightEffect{

    @Override
    protected void applyScope(EffectScope scope) {}

    @Override
    public Effect getEffect() {
        return Effect.RETURNS_DOMA;
    }

    @Override
    public EffectType getType() {
        return EffectType.BUFF;
    }

    @Override
    public void applyBuffOnDammages(Buff buff, EffectScope scope) {
        if(scope.getCaster().equals(scope.getTarget()))
            return;
        
        int value = Util.rand(buff.getSpellEffect().getMin(), buff.getSpellEffect().getMax());
        value = (int)EffectUtil.applyPercentBoost(value, buff.getCaster().getTotalStats().getEffect(Effect.ADD_SAGE));
        
        if(value > scope.getValue())
            value = scope.getValue();
        
        scope.setValue(scope.getValue() - value);
        
        scope.getFight().sendToFight(GameActionResponse.fightEffect(getEffect(), scope.getCaster(), scope.getCaster(), value));
        scope.getCaster().removePDV(value, scope.getCaster());
    }
    
}
