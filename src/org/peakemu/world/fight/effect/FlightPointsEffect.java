/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.common.util.Util;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FlightPointsEffect extends AbstractFightEffect{
    final private Effect effect;
    final private Effect resis;
    final private int dodgeAction;
    final private Effect removeEffect;
    final private Effect addEffect;

    public FlightPointsEffect(Effect effect, Effect resis, int dodgeAction, Effect removeEffect, Effect addEffect) {
        this.effect = effect;
        this.resis = resis;
        this.dodgeAction = dodgeAction;
        this.removeEffect = removeEffect;
        this.addEffect = addEffect;
    }

    @Override
    protected void applyScope(EffectScope scope) {
        int value = Util.rand(scope.getSpellEffect().getMin(), scope.getSpellEffect().getMax());
        
        int removed = EffectUtil.computeRemovedPoints(value, removeEffect, resis, scope.getCaster(), scope.getTarget());
        
        int dodge = value - removed;
        
        if(dodge > 0){
            scope.getFight().sendToFight(new GameActionResponse(dodgeAction, scope.getCaster().getSpriteId(), scope.getTarget().getSpriteId() + "," + dodge));
        }
        
        scope.setValue(removed);
        
        if(removed > 0){
            Buff buff = new Buff(new SpellEffect(removeEffect, scope.getSpellEffect().getSpell(), removed, 0, 0, 1, 0, "Pa", 0), scope.getCaster(), scope.getTarget(), scope.getFight(), 1);
            scope.getTarget().addBuff(buff);
            scope.getFight().sendToFight(GameActionResponse.fightEffect(removeEffect, scope.getCaster(), scope.getTarget(), -removed));
            
            switch(addEffect){
                case ADD_PA:
                    scope.getCaster().setCurPA(scope.getCaster().getCurPA() + removed);
                    break;
                case ADD_PM:
                    scope.getCaster().setCurPM(scope.getCaster().getCurPM() + removed);
                    break;
            }
            
            scope.getFight().sendToFight(GameActionResponse.fightEffect(addEffect, scope.getCaster(), scope.getCaster(), removed));
        }
    }

    @Override
    protected void applyBuff(Fight fight, SpellEffect spellEffect, Fighter caster, Fighter target) {
        applyScope(new EffectScope(spellEffect, fight, caster, target));
    }

    @Override
    public Effect getEffect() {
        return effect;
    }

    @Override
    public EffectType getType() {
        return EffectType.DEBUFF;
    }
}
