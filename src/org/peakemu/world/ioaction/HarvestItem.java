/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.ioaction;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.peakemu.common.util.Util;
import org.peakemu.game.in.gameaction.GameActionArg;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.game.out.game.InteractiveObjectsState;
import org.peakemu.game.out.info.HarvestQuantity;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.JobSkill;
import org.peakemu.world.MapCell;
import org.peakemu.world.enums.IOState;
import org.peakemu.world.handler.ItemHandler;
import org.peakemu.world.handler.JobHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class HarvestItem extends BasicIOAction{
    final private JobHandler jobHandler;
    final private ItemHandler itemHandler;
    
    final private ScheduledExecutorService service = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    public HarvestItem(JobHandler jobHandler, ItemHandler itemHandler) {
        this.jobHandler = jobHandler;
        this.itemHandler = itemHandler;
    }

    @Override
    public void perform(JobSkill skill, MapCell cell, Player player, GameActionArg arg) {
        cell.getObject().setState(IOState.EMPTYING);
        cell.getObject().setInteractive(false);
        
        int duration;
        
        if(skill.getJob().isBaseJob())
            duration = cell.getObject().getUseDuration();
        else
            duration = jobHandler.getHarvestDuraction(skill, player.getJobStats(skill.getJob()));
        
        if(duration < 1)
            duration = 1;
        
        player.getMap().sendToMap(GameActionResponse.jobAction(player, cell, duration, skill.getJob().isBaseJob() ? cell.getObject().getUnknowValue() : -1));
        player.getMap().sendToMap(new InteractiveObjectsState(cell));
        arg.getClient().getGameActionHandler().setCurrentAction(arg);
        
        service.schedule(
            () -> arg.getClient().getGameActionHandler().endCurrent(true, ""), 
            duration, 
            TimeUnit.MILLISECONDS
        );
    }

    @Override
    public void finish(JobSkill skill, MapCell cell, Player player, GameActionArg arg) {
        cell.getObject().setState(IOState.EMPTY);
        player.getMap().sendToMap(new InteractiveObjectsState(cell));
        
        int qte;
        
        if(!skill.getJob().isBaseJob())
            qte = jobHandler.getHarvestDropInterval(skill, player.getJobStats(skill.getJob())).getRandomValue();
        else
            qte = Util.rand(1, 10);
        
        Item item = itemHandler.createNewItem(skill.getHarvest(), qte, false);
        player.getItems().addItem(item);
        player.send(new HarvestQuantity(player, qte));
        
        if(!skill.getJob().isBaseJob()){
            jobHandler.addXp(player, player.getJobStats(skill.getJob()), jobHandler.getHarvestWinXp(skill));
        }
        
        cell.getObject().setState(IOState.EMPTY2);
    }
    
    
}
