/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import org.peakemu.world.fight.effect.Buff;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AddFightBuff {
    private Buff buff;

    public AddFightBuff(Buff buff) {
        this.buff = buff;
    }

    @Override
    public String toString() {
        return "GIE" + buff.getSpellEffect().getEffect().getId() + ";" + buff.getTarget().getSpriteId() + ";" 
            + buff.getValue() + ";" + (buff.getArg2() == -1 ? "" : buff.getArg2()) + ";" + buff.getArg3() + ";" + buff.getArg4() + ";" + buff.getRemainingTurns() + ";" + buff.getSpellEffect().getSpell().getSpellID();
    }
    
    
}
