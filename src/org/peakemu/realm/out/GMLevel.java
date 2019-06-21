/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.realm.out;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GMLevel {
    private boolean gm;

    public GMLevel(boolean gm) {
        this.gm = gm;
    }

    public boolean isGm() {
        return gm;
    }

    public void setGm(boolean gm) {
        this.gm = gm;
    }

    @Override
    public String toString() {
        return "AlK" + (gm ? "1" : "0");
    }
    
    
}
