/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.guild;

import org.peakemu.objects.Collector;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CollectorInfoMessage {
    final static public char REMOVE = 'R';
    final static public char ADD    = 'S';
    final static public char RECOLT = 'G';
    
    private Collector collector;
    private char action;
    private Player performer;

    public CollectorInfoMessage(Collector collector, char action, Player performer) {
        this.collector = collector;
        this.action = action;
        this.performer = performer;
    }

    @Override
    public String toString() {
        String str = "gT" + action + "" + collector.get_N1() + "," + collector.get_N2() + "|" + collector.getMap().getId() + "|" + collector.getMap().getX() + "|" + collector.getMap().getY() + "|" + performer.getName();
        
        if(action == RECOLT)
            str += "|" + collector.getXp() + collector.get_LogItems();
        
        return str;
    }
    
    
}
