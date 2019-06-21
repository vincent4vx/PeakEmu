/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects.item;

import org.peakemu.objects.player.Mutant;
import org.peakemu.objects.player.Player;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.MonsterTemplate;
import org.peakemu.world.StatsTemplate;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.InventoryPosition;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MutationItem extends BuffItem{
    final private MonsterTemplate monsterTemplate;

    public MutationItem(MonsterTemplate monsterTemplate, String owner, int Guid, ItemTemplate template, int qua, InventoryPosition pos, StatsTemplate statsTemplate) {
        super(owner, Guid, template, qua, pos, statsTemplate);
        this.monsterTemplate = monsterTemplate;
    }

    public MonsterTemplate getMonsterTemplate() {
        return monsterTemplate;
    }
    
    public Mutant createMutant(Player player){
        if(monsterTemplate == null)
            return null;
        
        StatsTemplate.StatEntry entry = statsTemplate.findFirstEntry(Effect.TRANSFORM);
        
        return new Mutant(player, monsterTemplate, entry.getArg2(), entry.getSpecial());
    }
}
