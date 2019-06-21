/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.world.MapCell;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.object.FightObject;
import org.peakemu.world.fight.object.Trap;
import org.peakemu.world.handler.SpellHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AddTrapEffect extends AbstractAddFightObject{

    public AddTrapEffect(SpellHandler spellHandler) {
        super(spellHandler);
    }

    @Override
    protected FightObject createObject(SpellEffect spellEffect, MapCell cell, Fighter caster, SpellLevel triggerSpell, int size, int color, int duration) {
        return new Trap(triggerSpell, cell, size, caster, color, spellEffect.getSpell());
    }

    @Override
    public Effect getEffect() {
        return Effect.TRAP;
    }
    
}
