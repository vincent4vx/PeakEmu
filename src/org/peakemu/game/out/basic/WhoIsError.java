/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.basic;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class WhoIsError {
    private String name;

    public WhoIsError(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BWE" + name;
    }
    
    
}
