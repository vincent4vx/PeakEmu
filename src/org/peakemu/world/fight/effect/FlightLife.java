/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.world.enums.Effect;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FlightLife extends AttackEffect{

    public FlightLife(Effect effect) {
        super(effect);
    }

    @Override
    protected void applyScope(EffectScope scope) {
        super.applyScope(scope);
        
        int heal = scope.getValue() / 2;
        scope.getCaster().addLifePoints(heal, scope.getCaster());
    }
    
}
