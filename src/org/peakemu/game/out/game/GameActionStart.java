/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GameActionStart {
    private int spriteId;

    public GameActionStart(int spriteId) {
        this.spriteId = spriteId;
    }

    @Override
    public String toString() {
        return "GAS" + spriteId;
    }
    
    
}
