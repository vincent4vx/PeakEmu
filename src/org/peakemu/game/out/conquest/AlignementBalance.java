/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.conquest;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AlignementBalance {
    final private int worldBalance;
    final private int areaBalance;

    public AlignementBalance(int worldBalance, int areaBalance) {
        this.worldBalance = worldBalance;
        this.areaBalance = areaBalance;
    }

    @Override
    public String toString() {
        return "Cb" + worldBalance + ";" + areaBalance;
    }
    
    
}
