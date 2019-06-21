/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.exchange;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class BuyDone {
    private boolean success;

    public BuyDone(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "EB" + (success ? "K" : "E");
    }
    
    
}
