/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.mount;

import org.peakemu.objects.MountPark;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountParkStartBuy {
    private MountPark mountPark;

    public MountParkStartBuy(MountPark mountPark) {
        this.mountPark = mountPark;
    }

    @Override
    public String toString() {
        return "RD" + mountPark.getMap().getId() + "|" + mountPark.getPrice();
    }
    
    
}
