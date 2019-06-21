/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.common.SocketManager;
import org.peakemu.common.util.StringUtil;
import org.peakemu.game.out.object.InventoryWeight;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.item.MutationItem;
import org.peakemu.objects.player.Mutant;
import org.peakemu.objects.player.Player;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.MapCell;
import org.peakemu.world.Restrictions;
import org.peakemu.world.StatsTemplate;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.handler.ItemHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Mutation implements ActionPerformer{
    final static public int TEMPLATE_ID = 1722;
    
    final private ItemHandler itemHandler;

    public Mutation(ItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public int actionId() {
        return 3;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        if(caster.isMutant())
            return false;
        
        String[] aArgs = StringUtil.split(action.getArgs(), ";");
        
        int monsterId = Integer.parseInt(aArgs[0]);
        int turns = 10;
        int params = Mutant.PARAM_SHOW_PLAYER | Mutant.PARAM_USE_STUFF;
        int restrictions = Restrictions.ALLOW_ATTACK_MONSTERS_MUTANT | Restrictions.ALLOW_DUNGEON_MUTANT;
        
        ItemTemplate template = itemHandler.getTemplateById(TEMPLATE_ID);
        StatsTemplate statsTemplate = new StatsTemplate();
        statsTemplate.setEntry(new StatsTemplate.StatEntry(Effect.TRANSFORM, monsterId, params, restrictions, ""));
        statsTemplate.setEntry(new StatsTemplate.StatEntry(Effect.ADD_TOURS, 0, 0, turns, ""));
        
        MutationItem mutationItem = (MutationItem) itemHandler.createNewItem(template, 1, statsTemplate);
        caster.getItems().addBuffItem(mutationItem);
        caster.refreshStats();
        SocketManager.GAME_SEND_STATS_PACKET(caster);
        caster.send(new InventoryWeight(caster));
        
        return true;
    }
    
    
}
