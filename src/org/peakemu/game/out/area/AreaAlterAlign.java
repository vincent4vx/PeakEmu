/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.area;

import org.peakemu.world.Area;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AreaAlterAlign {
    private Area area;

    public AreaAlterAlign(Area area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "aM" + area.getId() + "|" + area.getalignement();
    }
    
    
}
