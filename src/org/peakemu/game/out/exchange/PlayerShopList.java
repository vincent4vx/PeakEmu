/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.exchange;

import org.peakemu.common.util.Pair;
import org.peakemu.database.parser.StatsParser;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.PlayerStore;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PlayerShopList {
    private PlayerStore store;

    public PlayerShopList(PlayerStore store) {
        this.store = store;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("EL");
        
        boolean b = false;
        
        for(Pair<Item, Long> item : store.getItems()){
            if(b)
                sb.append('|');
            else
                b = true;
            
            sb.append(item.getFirst().getGuid()).append(';')
              .append(item.getFirst().getQuantity()).append(';')
              .append(item.getFirst().getTemplate().getID()).append(';')
              .append(StatsParser.statsTemplateToString(item.getFirst().getStatsTemplate())).append(';')
              .append(item.getSecond());
        }
        
        return sb.toString();
    }
    
    
}
