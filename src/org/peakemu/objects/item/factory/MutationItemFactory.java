/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects.item.factory;

import org.peakemu.objects.item.Item;
import org.peakemu.objects.item.MutationItem;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.MonsterTemplate;
import org.peakemu.world.StatsTemplate;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.InventoryPosition;
import org.peakemu.world.handler.MonsterHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MutationItemFactory implements ItemFactory{
    final private MonsterHandler monsterHandler;

    public MutationItemFactory(MonsterHandler monsterHandler) {
        this.monsterHandler = monsterHandler;
    }
    
    @Override
    public Item createItem(String owner, int Guid, ItemTemplate template, int qua, InventoryPosition pos, StatsTemplate statsTemplate) {
        StatsTemplate.StatEntry entry = statsTemplate.findFirstEntry(Effect.TRANSFORM);
        
        MonsterTemplate monster = null;
        
        if(entry != null){
            monster = monsterHandler.getMonsterById(entry.getArg1());
        }
        
        return new MutationItem(monster, owner, Guid, template, qua, pos, statsTemplate);
    }
    
}
