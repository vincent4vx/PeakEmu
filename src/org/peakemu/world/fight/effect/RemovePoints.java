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
public class RemovePoints extends AbstractFightEffect{
    final private Effect effect;
    final private Effect resis;
    final private int dodgeAction;

    public RemovePoints(Effect effect, Effect resis, int dodgeAction) {
        this.effect = effect;
        this.resis = resis;
        this.dodgeAction = dodgeAction;
    }

    @Override
    protected void applyScope(EffectScope scope) {}

    @Override
    protected void applyBuff(Fight fight, SpellEffect spellEffect, Fighter caster, Fighter target) {
        int value = Util.rand(spellEffect.getMin(), spellEffect.getMax());
        int removed = EffectUtil.computeRemovedPoints(value, effect, resis, caster, target);
        
        int dodge = value - removed;
        
        if(dodge > 0){
            fight.sendToFight(new GameActionResponse(dodgeAction, caster.getSpriteId(), target.getSpriteId() + "," + dodge));
        }
        
        if(removed > 0){
            Buff buff = new Buff(spellEffect, caster, target, fight, spellEffect.getTurns());
            buff.setValue(removed);
            target.addBuff(buff);
            fight.sendToFight(GameActionResponse.fightEffect(effect, caster, target, -removed));
        }
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
