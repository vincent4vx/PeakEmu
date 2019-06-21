/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.account;

import org.peakemu.world.Restrictions;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AlterRestrictions {
    private Restrictions restrictions;

    public AlterRestrictions(Restrictions restrictions) {
        this.restrictions = restrictions;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Restrictions restrictions) {
        this.restrictions = restrictions;
    }

    @Override
    public String toString() {
        return "AR" + Integer.toString(restrictions.toInt(), 36);
    }
    
    
}
