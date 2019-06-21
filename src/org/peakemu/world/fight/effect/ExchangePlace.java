/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.world.MapCell;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ExchangePlace extends AbstractFightEffect{

    @Override
    protected void applyScope(EffectScope scope) {
        if(scope.getCaster().equals(scope.getTarget()))
            return;
        
        MapCell casterCell = scope.getCaster().getCell();
        MapCell targetCell = scope.getTarget().getCell();
        
        scope.getFight().sendToFight(GameActionResponse.fightEffect(Effect.TELEPORT, scope.getCaster(), scope.getCaster(), targetCell.getID()));
        scope.getFight().sendToFight(GameActionResponse.fightEffect(Effect.TELEPORT, scope.getCaster(), scope.getTarget(), casterCell.getID()));
        
        scope.getFight().onPlaceChange(scope.getCaster(), targetCell);
        scope.getFight().onPlaceChange(scope.getTarget(), casterCell);
    }

    @Override
    public Effect getEffect() {
        return Effect.EXCHANGE_PLACE;
    }

    @Override
    public EffectType getType() {
        return EffectType.TELEPORT;
    }
    
}
