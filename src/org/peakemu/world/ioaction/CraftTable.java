/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.ioaction;

import org.peakemu.game.in.gameaction.GameActionArg;
import org.peakemu.game.out.exchange.CraftTableOpened;
import org.peakemu.game.out.game.InteractiveObjectsState;
import org.peakemu.objects.player.JobStats;
import org.peakemu.objects.player.Player;
import org.peakemu.world.JobSkill;
import org.peakemu.world.MapCell;
import org.peakemu.world.enums.IOState;
import org.peakemu.world.exchange.CraftExchange;
import org.peakemu.world.handler.ItemHandler;
import org.peakemu.world.handler.JobHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CraftTable extends BasicIOAction{
    final private JobHandler jobHandler;
    final private ItemHandler itemHandler;

    public CraftTable(JobHandler jobHandler, ItemHandler itemHandler) {
        this.jobHandler = jobHandler;
        this.itemHandler = itemHandler;
    }

    @Override
    public void perform(JobSkill skill, MapCell cell, Player player, GameActionArg arg) {
        cell.getObject().setState(IOState.EMPTYING);
        player.getMap().sendToMap(new InteractiveObjectsState(cell));
        player.setAway(true);
        
        JobStats jobStats = player.getJobStats(skill.getJob());
        
        player.setCurExchange(new CraftExchange(itemHandler, jobHandler, player, jobStats, skill, cell));
        player.send(new CraftTableOpened(jobHandler.getMaxCraftIngredients(jobStats), skill));
    }
}
