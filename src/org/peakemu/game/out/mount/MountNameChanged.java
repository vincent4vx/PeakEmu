/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.mount;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountNameChanged {
    final private String name;

    public MountNameChanged(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Rn" + name;
    }
    
    
}
