/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import java.util.ArrayList;
import java.util.List;
import org.peakemu.common.SocketManager;
import org.peakemu.common.util.Util;
import org.peakemu.game.out.object.InventoryWeight;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.item.MutationItem;
import org.peakemu.objects.player.Mutant;
import org.peakemu.objects.player.Player;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.MapCell;
import org.peakemu.world.MonsterTemplate;
import org.peakemu.world.Restrictions;
import org.peakemu.world.StatsTemplate;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.handler.ItemHandler;
import org.peakemu.world.handler.MonsterHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class RandomMutation implements ActionPerformer{
    final static public int TEMPLATE_ID = 1722;
    final private ItemHandler itemHandler;
    final private MonsterHandler monsterHandler;

    public RandomMutation(ItemHandler itemHandler, MonsterHandler monsterHandler) {
        this.itemHandler = itemHandler;
        this.monsterHandler = monsterHandler;
    }

    @Override
    public int actionId() {
        return 2;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        if(caster.isMutant()) //already a mutant
            return false;
        
        ItemTemplate template = itemHandler.getTemplateById(TEMPLATE_ID);
        
        List<MonsterTemplate> available = new ArrayList<>();
        
        for(MonsterTemplate mt : monsterHandler.getAllMonsters()){
            if(caster.getLevel() >= mt.getFirstGrade().getLevel()
                && mt.getFirstGrade().getSpells().size() > 0) //at least 1 spell
                available.add(mt);
        }
        
        MonsterTemplate monster = Util.rand(available);
        
        StatsTemplate statsTemplate = new StatsTemplate();
        statsTemplate.setEntry(new StatsTemplate.StatEntry(
            Effect.TRANSFORM, 
            monster.getID(), 
            Mutant.PARAM_SHOW_PLAYER | Mutant.PARAM_USE_STUFF, 
            Restrictions.ALLOW_ATTACK_MONSTERS_MUTANT | Restrictions.ALLOW_DUNGEON_MUTANT, 
            ""
        ));
        statsTemplate.setEntry(new StatsTemplate.StatEntry(Effect.ADD_TOURS, 0, 0, 10, ""));
        
        MutationItem mutationItem = (MutationItem) itemHandler.createNewItem(template, 1, statsTemplate);
        caster.getItems().addBuffItem(mutationItem);
        caster.refreshStats();
        SocketManager.GAME_SEND_STATS_PACKET(caster);
        caster.send(new InventoryWeight(caster));
        return true;
    }
    
}
