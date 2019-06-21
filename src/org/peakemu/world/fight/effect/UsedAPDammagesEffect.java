/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.world.Stats;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;
import org.peakemu.world.enums.Element;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class UsedAPDammagesEffect extends AbstractFightEffect{

    @Override
    protected void applyScope(EffectScope scope) {}

    @Override
    public Effect getEffect() {
        return Effect.DOMA_PER_PA;
    }

    @Override
    public EffectType getType() {
        return EffectType.ATTACK;
    }

    @Override
    public void applyBuffOnEndTurn(Buff buff) {
        double ap = (double)buff.getFight().getCurFighterUsedPA() / (double)buff.getValue();
        double dmg = ap * buff.getArg2();
        Stats stats = buff.getCaster().getTotalStats();
        dmg = EffectUtil.applyBoostStats(dmg, stats, Element.FIRE);
        dmg = EffectUtil.applyPerDom(dmg, stats);
        
        int dammages = (int)dmg;
        
        if(dammages <= 0)
            return;
        
        buff.getTarget().removePDV(dammages, buff.getCaster());
    }
}
