/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.world.MapCell;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class NoEffect implements FightEffect{

    @Override
    public Effect getEffect() {
        return Effect.NO_EFFECT;
    }

    @Override
    public EffectType getType() {
        return EffectType.OTHER;
    }

    @Override
    public void applyToFight(Fight fight, SpellEffect spellEffect, MapCell cell, Fighter caster) {}

    @Override
    public void applyBuffOnStartTurn(Buff buff) {}

    @Override
    public void applyBuffOnEndTurn(Buff buff) {}

    @Override
    public void applyBuffOnDammages(Buff buff, EffectScope scope) {}

    @Override
    public void onBuffEnd(Buff buff) {}
    
}
