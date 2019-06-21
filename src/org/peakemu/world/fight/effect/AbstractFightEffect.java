/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.maputil.OldPathfinding;
import org.peakemu.world.MapCell;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
abstract public class AbstractFightEffect implements FightEffect {

    @Override
    public void applyToFight(Fight fight, SpellEffect spellEffect, MapCell cell, Fighter caster) {
        ArrayList<MapCell> cells = OldPathfinding.getCellListFromAreaString(fight.get_map(), cell.getID(), caster.getCell().getID(), spellEffect.getArea(), 0, false);

        Collection<Fighter> targets = new ArrayList<>();

        int TE = spellEffect.getTarget();

        for (MapCell C : cells) {
            if (C == null) {
                continue;
            }
            Fighter F = C.getFirstFighter();
            if (F == null) {
                continue;
            }
            
            if(!FightEffect.isValidTarget(caster, F, TE)){
                continue;
            }
            
            //Si pas encore eu de continue, on ajoute la case
            targets.add(F);
        }
        //Si le sort n'affecte que le lanceur et que le lanceur n'est pas dans la zone
        if ((TE & FightEffect.TARGET_ONLY_CASTER) == TARGET_ONLY_CASTER) {
            if(!targets.contains(caster))
                targets.add(caster);
        }
        
        for(Fighter target : targets){
            if(spellEffect.getTurns() <= 0){
                EffectScope scope = new EffectScope(spellEffect, fight, caster, target);
                applyScope(scope);
            }else{
                applyBuff(fight, spellEffect, caster, target);
            }
        }
    }

    abstract protected void applyScope(EffectScope scope);
    
    protected void applyBuff(Fight fight, SpellEffect spellEffect, Fighter caster, Fighter target){
        target.addBuff(new Buff(spellEffect, caster, target, fight, spellEffect.getTurns()));
    }

    @Override
    public void applyBuffOnStartTurn(Buff buff) {
        applyScope(EffectScope.fromBuff(buff));
    }

    @Override
    public void applyBuffOnEndTurn(Buff buff) {}

    @Override
    public void applyBuffOnDammages(Buff buff, EffectScope scope) {}

    @Override
    public void onBuffEnd(Buff buff) {}

}
