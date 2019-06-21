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
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ChangeApparence extends AbstractFightEffect{

    @Override
    protected void applyScope(EffectScope scope) {}

    @Override
    public void onBuffEnd(Buff buff) {
        buff.getTarget().setGfxID(buff.getTarget().getDefaultGfx());
    }

    @Override
    protected void applyBuff(Fight fight, SpellEffect spellEffect, Fighter caster, Fighter target) {
        super.applyBuff(fight, spellEffect, caster, target);
        target.setGfxID(spellEffect.getArg3());
        fight.sendToFight(GameActionResponse.changeApparence(caster, target, target.getDefaultGfx(), spellEffect.getArg3(), spellEffect.getTurns()));
    }
    
    @Override
    public Effect getEffect() {
        return Effect.CHANGE_SKIN;
    }

    @Override
    public EffectType getType() {
        return EffectType.OTHER;
    }
    
}
