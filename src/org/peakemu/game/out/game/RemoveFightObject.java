/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import org.peakemu.world.fight.object.FightObject;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class RemoveFightObject {
    final private FightObject fightObject;

    public RemoveFightObject(FightObject fightObject) {
        this.fightObject = fightObject;
    }

    @Override
    public String toString() {
        return "GDZ-" + fightObject.getCell().getID() + ";" + fightObject.getSize() + ";" + fightObject.getColor();
    }
    
    
}
