/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.exchange;

import org.peakemu.database.parser.StatsParser;
import org.peakemu.objects.NPC;
import org.peakemu.world.ItemTemplate;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class NPCStoreList {
    private NPC npc;

    public NPCStoreList(NPC npc) {
        this.npc = npc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("EL");
        
        boolean b = false;
        
        for(ItemTemplate tpl : npc.getTemplate().getStore()){
            if(b)
                sb.append('|');
            else
                b = true;
            
            sb.append(tpl.getID()).append(';').append(StatsParser.statsTemplateToString(tpl.getStatsTemplate()));
        }
        
        return sb.toString();
    }
    
    
}
