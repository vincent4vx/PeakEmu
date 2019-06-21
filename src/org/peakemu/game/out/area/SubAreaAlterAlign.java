/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.area;

import org.peakemu.world.Area;
import org.peakemu.world.SubArea;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SubAreaAlterAlign {
    private SubArea subArea;
    private boolean showMsg;

    public SubAreaAlterAlign(SubArea subArea, boolean showMsg) {
        this.subArea = subArea;
        this.showMsg = showMsg;
    }

    public SubAreaAlterAlign setShowMsg(boolean showMsg) {
        this.showMsg = showMsg;
        return this;
    }

    @Override
    public String toString() {
        return "am" + subArea.getId() + "|" + subArea.getAlignement() + "|" + (showMsg ? 0 : 1);
    }
    
    
}
