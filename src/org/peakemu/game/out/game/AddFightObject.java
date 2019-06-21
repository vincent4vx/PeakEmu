/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.world.fight.object.FightObject;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AddFightObject {
    final private Collection<FightObject> fightObjects;

    public AddFightObject(Collection<FightObject> fightObjects) {
        this.fightObjects = fightObjects;
    }
    
    public AddFightObject(FightObject... objects){
        fightObjects = new ArrayList<>();
        
        for (FightObject object : objects) {
            fightObjects.add(object);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("GDZ");
        
        boolean b = false;
        
        for (FightObject fightObject : fightObjects) {
            if(b)
                sb.append('|');
            else
                b = true;
            
            sb.append('+').append(fightObject.getCell().getID()).append(';')
                .append(fightObject.getSize()).append(';')
                .append(fightObject.getColor());
        }
        
        return sb.toString();
    }
    
    
}
