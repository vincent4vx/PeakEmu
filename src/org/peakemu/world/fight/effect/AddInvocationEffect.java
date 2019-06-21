/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Ludo
 */
public class AddInvocationEffect extends AbstractFightEffect{

    @Override
    protected void applyScope(EffectScope scope) {}
    
    @Override
    protected void applyBuff(Fight fight, SpellEffect spellEffect, Fighter caster, Fighter target) {
        target._nbInvoc += spellEffect.getMin();
        super.applyBuff(fight, spellEffect, caster, target);
        fight.sendToFight(GameActionResponse.fightEffect(getEffect(), caster, target, spellEffect.getMin(), spellEffect.getTurns()));
    }

    @Override
    public Effect getEffect() {
        return Effect.CREATURE;
    }

    @Override
    public EffectType getType() {
        return EffectType.BUFF;
    }
    
    @Override
    public void onBuffEnd(Buff buff) {
        buff.getTarget()._nbInvoc -= buff.getSpellEffect().getMin();
    }
}
