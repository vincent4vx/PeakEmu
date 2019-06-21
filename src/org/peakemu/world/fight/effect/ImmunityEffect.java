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
public class ImmunityEffect extends AbstractFightEffect{

    @Override
    protected void applyScope(EffectScope scope) {}

    @Override
    public Effect getEffect() {
        return Effect.IMMUNITY;
    }

    @Override
    public EffectType getType() {
        return EffectType.BUFF;
    }

    @Override
    public void applyBuffOnDammages(Buff buff, EffectScope scope) {
        Element element = scope.getSpellEffect().getEffect().getRelatedElement();
        
        Stats stats = scope.getTarget().getTotalStats();
        double intel = stats.getEffect(Effect.ADD_INTE);
        double carac = stats.getEffect(element.getBoost());
        
        int armor = buff.getValue();
        armor *= 1 + (double)(intel / 2 + carac / 2) / 100;
        
        int dammages = scope.getValue();
        dammages -= armor;
        
        if(dammages < 0)
            dammages = 0;
        
        scope.setValue(dammages);
        scope.setReducedDammages(scope.getReducedDammages() + armor);
    }
    
    
}
