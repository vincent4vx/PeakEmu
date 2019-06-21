/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.world.Stats;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;
import org.peakemu.world.enums.Element;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ArmorEffect extends AbstractFightEffect {

    @Override
    protected void applyScope(EffectScope scope) {
    }

    @Override
    public Effect getEffect() {
        return Effect.FIX_RES_DOMA;
    }

    @Override
    public EffectType getType() {
        return EffectType.BUFF;
    }

    @Override
    public void applyBuffOnDammages(Buff buff, EffectScope scope) {
        Element element = scope.getSpellEffect().getEffect().getRelatedElement();
        
        switch (buff.getSpellEffect().getSpell().getSpellID()) {
            case 1://Armure incandescente
                if(element != Element.FIRE)
                    return;
                break;
            case 6://Armure Terrestre
                if(element != Element.EARTH && element != Element.NEUTRAL)
                    return;
                break;
            case 14://Armure Venteuse
                if(element != Element.AIR)
                    return;
                break;
            case 18://Armure aqueuse
                if(element != Element.WATER)
                    return;
                break;
            default:
                Peak.worldLog.addToLog(Logger.Level.ERROR, "Undefined armor spell %d", buff.getSpellEffect().getSpell().getSpellID());
                return;
        }
        
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
