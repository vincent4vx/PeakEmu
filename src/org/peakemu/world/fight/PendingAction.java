/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PendingAction {
    final static public int MOVE = 2;
    final static public int CAST = 0;
    
    final private int actionId;
    final private int pa;
    final private int pm;

    public PendingAction(int actionId, int pa, int pm) {
        this.actionId = actionId;
        this.pa = pa;
        this.pm = pm;
    }

    public int getActionId() {
        return actionId;
    }

    public int getPa() {
        return pa;
    }

    public int getPm() {
        return pm;
    }
    
    
}
