/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight;

import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public interface OutputFilter {
    static public OutputFilter ALLOW_ALL = new OutputFilter() {
        @Override
        public boolean canSendToFighter(Fighter fighter) {
            return true;
        }

        @Override
        public boolean canSendToSpectator() {
            return true;
        }
    };
    
    public boolean canSendToFighter(Fighter fighter);
    public boolean canSendToSpectator();
}
