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
public class CollectorDefenders {
    private Collector collector;

    public CollectorDefenders(Collector collector) {
        this.collector = collector;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        
        sb.append("gITP+").append(collector.getGuid());
        
        for(Player player : collector.getFight().getDefenders()){
            sb.append("|");
            sb.append(Integer.toString(player.getSpriteId(), 36)).append(";");
            sb.append(player.getName()).append(";");
            sb.append(player.getGfxID()).append(";");
            sb.append(player.getLevel()).append(";");
            sb.append(Integer.toString(player.getColor1(), 36)).append(";");
            sb.append(Integer.toString(player.getColor2(), 36)).append(";");
            sb.append(Integer.toString(player.getColor3(), 36)).append(";");
            sb.append("0;");
        }
        
        return sb.toString();
    }
    
    
}
