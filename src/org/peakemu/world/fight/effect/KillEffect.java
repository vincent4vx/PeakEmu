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
public class KillEffect extends AbstractFightEffect{

    @Override
    protected void applyScope(EffectScope scope) {
        scope.getTarget().removePDV(Integer.MAX_VALUE, scope.getCaster());
    }

    @Override
    public Effect getEffect() {
        return Effect.KILL;
    }

    @Override
    public EffectType getType() {
        return EffectType.ATTACK;
    }
    
}
