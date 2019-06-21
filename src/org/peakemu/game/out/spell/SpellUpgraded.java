/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.spell;

import org.peakemu.world.SpellLevel;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SpellUpgraded {
    final private SpellLevel spell;

    public SpellUpgraded(SpellLevel spell) {
        this.spell = spell;
    }

    @Override
    public String toString() {
        return "SUK" + spell.getSpellID() + "~" + spell.getLevel();
    }
}
