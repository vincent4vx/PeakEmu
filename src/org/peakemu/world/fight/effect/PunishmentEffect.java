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
public class PunishmentEffect extends AbstractFightEffect{

    @Override
    protected void applyScope(EffectScope scope) {
        double val = ((double) Util.rand(scope.getSpellEffect().getMin(), scope.getSpellEffect().getMax()) / (double) 100);
        int pdvMax = scope.getCaster().getPdvMaxOutFight();
        double pVie = (double) scope.getCaster().getPDV() / (double) scope.getCaster().getPDVMAX();
        double rad = (double) 2 * Math.PI * (double) (pVie - 0.5);
        double cos = Math.cos(rad);
        double taux = (Math.pow((cos + 1), 2)) / (double) 4;
        double dgtMax = val * pdvMax;
        int dgt = (int) (taux * dgtMax);
        
        scope.setValue(dgt);
        
        scope.getFight().applyBuffsOnDammages(scope);
        
        scope.getTarget().removePDV(scope.getValue(), scope.getCaster());
    }

    @Override
    public Effect getEffect() {
        return Effect.PUNITION;
    }

    @Override
    public EffectType getType() {
        return EffectType.ATTACK;
    }
    
}
