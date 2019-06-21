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
import org.peakemu.world.fight.fighter.PlayerFighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AttackEffect extends AbstractFightEffect{
    final private Effect effect;

    public AttackEffect(Effect effect) {
        this.effect = effect;
    }

    @Override
    public Effect getEffect() {
        return effect;
    }

    @Override
    public EffectType getType() {
        return EffectType.ATTACK;
    }
    
    @Override
    protected void applyScope(EffectScope scope){
        int dammages = Util.rand(scope.getSpellEffect().getMin(), scope.getSpellEffect().getMax());
        
        for(Buff buff : scope.getCaster().getBuffsByEffect(Effect.BOOST_SPELL_DOMA)){
            if(buff.getValue() == scope.getSpellEffect().getSpell().getSpellID())
                dammages += buff.getArg3();
        }
        
        Stats stats = scope.getCaster().getTotalStats();
        
        dammages *= effect.getRelatedElement().getBoostCoef(stats);
        dammages *= 1 + (double)stats.getEffect(Effect.ADD_PERDOM) / 100;
        
        if(stats.getEffect(Effect.MULTIPLY_DOMMAGE) > 0)
            dammages *= stats.getEffect(Effect.MULTIPLY_DOMMAGE);
        
        dammages += stats.getEffect(Effect.ADD_DOMA);
        
        scope.setValue(dammages);
        
        scope.getFight().applyBuffsOnDammages(scope);
        
        //resis
        dammages = scope.getValue();
        
        if(dammages <= 0)
            return;
        
        stats = scope.getTarget().getTotalStats();
        
        boolean pvp = (scope.getCaster() instanceof PlayerFighter) && (scope.getTarget() instanceof PlayerFighter);
        int totalResFix = effect.getRelatedElement().getResFix(stats, pvp);
        double resCoef = effect.getRelatedElement().getResCoef(stats, pvp);
        
        dammages -= totalResFix;
        dammages *= resCoef;
        
        if(dammages < 0)
            dammages = 0;
        
        scope.setValue(dammages);
        
        scope.getTarget().removePDV(scope.getValue(), scope.getCaster());
    }
}
