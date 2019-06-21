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
public class HealOnDammageEffect extends AbstractFightEffect{

    @Override
    protected void applyScope(EffectScope scope) {}

    @Override
    public Effect getEffect() {
        return Effect.HEAL_ON_ATTACK;
    }

    @Override
    public EffectType getType() {
        return EffectType.BUFF;
    }

    @Override
    public void applyBuffOnDammages(Buff buff, EffectScope scope) {
        scope.getCaster().addLifePoints(scope.getValue(), scope.getCaster());
    }
    
}
