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
public class CraftLoopProgress {
    private int count;

    public CraftLoopProgress(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "EA" + count;
    }
    
    
}
