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
public class HelloConnection {
    private String key;

    public HelloConnection(String key) {
        this.key = key;
    }
    
    

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "HC" + key;
    }
}
