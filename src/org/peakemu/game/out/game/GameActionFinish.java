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
public class GameActionFinish {
    private int actionId;
    private int spriteId;

    public GameActionFinish(int actionId, int spriteId) {
        this.actionId = actionId;
        this.spriteId = spriteId;
    }

    @Override
    public String toString() {
        return "GAF" + actionId + "|" + spriteId;
    }
    
    
}
