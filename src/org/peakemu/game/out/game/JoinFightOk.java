/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import org.peakemu.world.fight.Fight;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class JoinFightOk {
    private Fight fight;
    private boolean isSpectator;

    public JoinFightOk(Fight fight, boolean isSpectator) {
        this.fight = fight;
        this.isSpectator = isSpectator;
    }

    public Fight getFight() {
        return fight;
    }

    public void setFight(Fight fight) {
        this.fight = fight;
    }

    public boolean isIsSpectator() {
        return isSpectator;
    }

    public void setIsSpectator(boolean isSpectator) {
        this.isSpectator = isSpectator;
    }
    
    

    @Override
    public String toString() {
        return "GJK" + fight.getState() + "|" + (fight.canCancel() && !isSpectator ? "1" : "0") + "|" + (!isSpectator ? "1" : "0") + "|" + (isSpectator ? "1" : "0") + "|" + fight.getRemaningTime() + "|" + fight.get_type().ordinal();
    }
}
