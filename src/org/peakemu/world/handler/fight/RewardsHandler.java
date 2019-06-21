/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler.fight;

import java.util.Collection;
import org.peakemu.common.util.Pair;
import org.peakemu.game.out.mount.MountEquiped;
import org.peakemu.objects.Collector;
import org.peakemu.objects.Prism;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.fight.FightRewards;
import org.peakemu.world.fight.fighter.CollectorFighter;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.fighter.PlayerFighter;
import org.peakemu.world.fight.fighter.PrismFighter;
import org.peakemu.world.handler.AlignementHandler;
import org.peakemu.world.handler.GuildHandler;
import org.peakemu.world.handler.ItemHandler;
import org.peakemu.world.handler.MountHandler;
import org.peakemu.world.handler.PlayerHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class RewardsHandler {
    final private PlayerHandler playerHandler;
    final private GuildHandler guildHandler;
    final private MountHandler mountHandler;
    final private ItemHandler itemHandler;
    final private AlignementHandler alignementHandler;

    public RewardsHandler(PlayerHandler playerHandler, GuildHandler guildHandler, MountHandler mountHandler, ItemHandler itemHandler, AlignementHandler alignementHandler) {
        this.playerHandler = playerHandler;
        this.guildHandler = guildHandler;
        this.mountHandler = mountHandler;
        this.itemHandler = itemHandler;
        this.alignementHandler = alignementHandler;
    }
    
    public void giveRewards(Collection<FightRewards> rewardses){
        for (FightRewards rewards : rewardses) {
            Fighter fighter = rewards.getFighter();
            
            if(fighter instanceof PlayerFighter){
                givePlayerRewards((PlayerFighter)fighter, rewards);
            }else if(fighter instanceof PrismFighter){
                givePrismRewards((PrismFighter)fighter, rewards);
            }else if(fighter instanceof CollectorFighter){
                giveCollectorRewards((CollectorFighter)fighter, rewards);
            }
        }
    }
    
    protected void givePlayerRewards(PlayerFighter fighter, FightRewards rewards){
        Player player = fighter.getPlayer();
        
        if(rewards.getWinXp() > 0)
            fighter._hasLevelUp = playerHandler.addXp(player, rewards.getWinXp());
        
        if(rewards.getWinKamas() > 0)
            player.addKamas(rewards.getWinKamas());
        
        if(rewards.getGuildXp() > 0 && player.getGuildMember() != null)
            guildHandler.addXp(player.getGuildMember(), rewards.getGuildXp());
        
        if(rewards.getMountXp() > 0 && player.isOnMount()){
            mountHandler.addXp(player.getMount(), rewards.getMountXp());
            player.send(new MountEquiped(player.getMount()));
        }
        
        if(rewards.getWinHonour() != 0){
            playerHandler.addHonor(player, rewards.getWinHonour());
        }
        
        if(rewards.getWinDisgrace() != 0)
            player.setDisgrace(player.getDisgrace() + rewards.getWinDisgrace());
        
        for(Pair<Integer, Integer> drop : rewards.getDrops()){
            ItemTemplate it = itemHandler.getTemplateById(drop.getFirst());
            
            if(it == null)
                continue;
            
            Item item = itemHandler.createNewItem(it, drop.getSecond(), false);
            player.getItems().addItem(item);
        }
    }
    
    protected void givePrismRewards(PrismFighter fighter, FightRewards rewards){
        Prism prism = fighter.getPrism();
        
        if(rewards.getWinHonour() > 0) //prism can only win honor
            alignementHandler.prismAddHonor(prism, rewards.getWinHonour());
    }
    
    protected void giveCollectorRewards(CollectorFighter fighter, FightRewards rewards){
        Collector collector = fighter.getCollector();
        
        for(Pair<Integer, Integer> drop : rewards.getDrops()){
            ItemTemplate it = itemHandler.getTemplateById(drop.getFirst());
            
            if(it == null)
                continue;
            
            Item item = itemHandler.createNewItem(it, drop.getSecond(), false);
            collector.addItem(item);
        }
        
        if(rewards.getGuildXp() > 0)
            collector.setXp(collector.getXp() + rewards.getGuildXp());
        
        if(rewards.getWinKamas() > 0)
            collector.setKamas(collector.getKamas() + rewards.getWinKamas());
    }
}
