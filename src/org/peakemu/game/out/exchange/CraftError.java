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
public class CraftError {
    final static public char NO_CRAFT_RESULT = 'I';
    final static public char CRAFT_FAILED = 'F';
    
    private char error;

    public CraftError(char error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "EcE" + error;
    }
    
    
}
