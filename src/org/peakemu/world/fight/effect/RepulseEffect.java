/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.Peak;
import org.peakemu.common.util.Util;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.maputil.CellChecker;
import org.peakemu.maputil.PathException;
import org.peakemu.maputil.PathStopException;
import org.peakemu.maputil.PathUtil;
import org.peakemu.world.MapCell;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class RepulseEffect extends AbstractFightEffect{

    @Override
    protected void applyScope(EffectScope scope) {
        int min = scope.getSpellEffect().getMin();
        int max = scope.getSpellEffect().getMax();
        int value;
        
        if(max <= min)
            value = min;
        else
            value = Util.rand(min, max);
        
        if(scope.getCaster().getCell().equals(scope.getTarget().getCell()))
            return;
        
        MapCell targetCell;
        int dammages = 0;
        
        try{
            targetCell = PathUtil.getNextAlignedCell(scope.getCaster().getCell(), scope.getTarget().getCell(), value, CellChecker.forFightPathing(scope.getFight()));
        }catch(PathStopException e){
            targetCell = e.getStop();
            
            double dmg = Util.rand(1, 8);
            dmg *= (double)scope.getCaster().get_lvl() / 50;
            dmg += 8;
            dmg *= e.getRemainingSteps();
            
            dammages = (int)dmg;
        }catch(PathException e){
            Peak.errorLog.addToLog(e);
            return;
        }
        
        scope.getFight().sendToFight(GameActionResponse.fightEffect(getEffect(), scope.getCaster(), scope.getTarget(), targetCell.getID()));
        scope.getFight().onPlaceChange(scope.getTarget(), targetCell);
        
        if(dammages > 0){
            if(dammages > scope.getTarget().getPDV())
                dammages = scope.getTarget().getPDV();
            
            scope.getTarget().removePDV(dammages, scope.getCaster());
        }
    }

    @Override
    public Effect getEffect() {
        return Effect.REPULSE;
    }

    @Override
    public EffectType getType() {
        return EffectType.REPULSE;
    }
    
}
