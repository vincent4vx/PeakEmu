/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.game.out.game.GameActionResponse;
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
public class TeleportEffect implements FightEffect{

    @Override
    public Effect getEffect() {
        return Effect.TELEPORT;
    }

    @Override
    public EffectType getType() {
        return EffectType.TELEPORT;
    }

    @Override
    public void applyToFight(Fight fight, SpellEffect spellEffect, MapCell cell, Fighter caster) {
        if(!fight.isFreeCell(cell))
            return;
        
        caster.getCell().removeFighter(caster);
        caster.set_fightCell(cell);
        cell.addFighter(caster);
        
        fight.sendToFight(GameActionResponse.fightEffect(getEffect(), caster, caster, cell.getID()));
    }

    @Override
    public void applyBuffOnStartTurn(Buff buff) {}

    @Override
    public void applyBuffOnEndTurn(Buff buff) {}

    @Override
    public void applyBuffOnDammages(Buff buff, EffectScope scope) {}

    @Override
    public void onBuffEnd(Buff buff) {}
}
