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
public class AttractEffect extends AbstractFightEffect{

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
        
        try{
            targetCell = PathUtil.getNextAlignedCell(scope.getCaster().getCell(), scope.getTarget().getCell(), -value, CellChecker.forFightPathing(scope.getFight()));
        }catch(PathStopException e){
            targetCell = e.getStop();
        }catch(PathException e){
            Peak.errorLog.addToLog(e);
            return;
        }
        
        scope.getTarget().getCell().removeFighter(scope.getTarget());
        scope.getTarget().set_fightCell(targetCell);
        targetCell.addFighter(scope.getTarget());
        
        scope.getFight().sendToFight(GameActionResponse.fightEffect(Effect.REPULSE, scope.getCaster(), scope.getTarget(), targetCell.getID()));
    }

    @Override
    public Effect getEffect() {
        return Effect.ATTRACT;
    }

    @Override
    public EffectType getType() {
        return EffectType.REPULSE;
    }
    
}
