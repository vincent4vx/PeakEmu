/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ExchangePlaceOnDammages extends AbstractFightEffect{
    final private ExchangePlace exchangePlace;

    public ExchangePlaceOnDammages(ExchangePlace exchangePlace) {
        this.exchangePlace = exchangePlace;
    }

    @Override
    protected void applyScope(EffectScope scope) {}

    @Override
    public void applyBuffOnDammages(Buff buff, EffectScope scope) {
        if(buff.getCaster().equals(buff.getTarget()) || buff.getCaster().isDead())
            return;
        
        exchangePlace.applyScope(EffectScope.fromBuff(buff));
        scope.setTarget(buff.getCaster());
    }

    @Override
    public Effect getEffect() {
        return Effect.SACRIFICE;
    }

    @Override
    public EffectType getType() {
        return EffectType.BUFF;
    }
    
}
