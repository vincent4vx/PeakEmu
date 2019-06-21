/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.conquest;

import org.peakemu.objects.Prism;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PrismAttacked {
    final private Prism prism;

    public PrismAttacked(Prism prism) {
        this.prism = prism;
    }

    @Override
    public String toString() {
        return "CA" + prism.getMap().getId() + "|" + prism.getX() + "|" + prism.getY();
    }
}
