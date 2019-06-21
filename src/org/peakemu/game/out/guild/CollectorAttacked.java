/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.guild;

import org.peakemu.objects.Collector;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CollectorAttacked {
    private Collector collector;
    private char event;
    
    final static public char TAX_ATTACKED = 'A';
    final static public char TAX_ATTACKED_SUVIVED = 'S';
    final static public char TAX_ATTACKED_DIED = 'D';

    public CollectorAttacked(Collector collector, char event) {
        this.collector = collector;
        this.event = event;
    }

    @Override
    public String toString() {
        return "gA" + event + "" + collector.get_N1() + "," + collector.get_N2() + "|" + collector.getGuid() + "|" + collector.getMap().getX() + "|" + collector.getMap().getY();
    }
}
