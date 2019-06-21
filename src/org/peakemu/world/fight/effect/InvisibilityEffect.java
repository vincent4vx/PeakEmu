/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.game.out.game.FighterPosition;
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
public class InvisibilityEffect extends AbstractFightEffect{

    @Override
    protected void applyScope(EffectScope scope) {}

    @Override
    protected void applyBuff(Fight fight, SpellEffect spellEffect, Fighter caster, Fighter target) {
        target.setHidden(true);
        super.applyBuff(fight, spellEffect, caster, target);
        fight.sendToFight(GameActionResponse.fightEffect(getEffect(), caster, target, 4));
    }

    @Override
    public Effect getEffect() {
        return Effect.INVISIBILITY;
    }

    @Override
    public EffectType getType() {
        return EffectType.BUFF;
    }

    @Override
    public void onBuffEnd(Buff buff) {
        buff.getTarget().setHidden(false);
        buff.getFight().sendToFight(GameActionResponse.fightEffect(getEffect(), buff.getTarget(), buff.getTarget(), 0));
        buff.getFight().sendToFight(new FighterPosition(buff.getTarget()));
    }
    
}
