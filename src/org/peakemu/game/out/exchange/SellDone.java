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
public class SellDone {
    private boolean success;

    public SellDone(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ES" + (success ? "K" : "E");
    }
    
    
}
