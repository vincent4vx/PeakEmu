/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import java.util.EnumMap;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.game.out.exchange.ExchangeCreated;
import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ExchangeType;
import org.peakemu.world.exchange.BankExchangePerformer;
import org.peakemu.world.exchange.CollectorExchangePerformer;
import org.peakemu.world.exchange.CraftExchangePerformer;
import org.peakemu.world.exchange.ExchangeBase;
import org.peakemu.world.exchange.ExchangePerformer;
import org.peakemu.world.exchange.MountExchangePerformer;
import org.peakemu.world.exchange.MountParkExchangePerformer;
import org.peakemu.world.exchange.NPCStoreExchangePerformer;
import org.peakemu.world.exchange.PlayerExchangePerformer;
import org.peakemu.world.exchange.PlayerManageStoreExchangePerformer;
import org.peakemu.world.exchange.PlayerStoreBuyExchangePerformer;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
final public class ExchangeHandler {
    final private Peak peak;
    
    final private Map<ExchangeType, ExchangePerformer> performers = new EnumMap<>(ExchangeType.class);

    public ExchangeHandler(Peak peak) {
        this.peak = peak;
    }
    
    public void registerPerformer(ExchangePerformer performer){
        performers.put(performer.getExchangeType(), performer);
    }
    
    public void load(){
        registerPerformer(new PlayerExchangePerformer(peak.getDao().getPlayerDAO()));
        registerPerformer(new CollectorExchangePerformer(peak.getDao().getPlayerDAO(), peak.getWorld().getGuildHandler()));
        registerPerformer(new BankExchangePerformer(peak.getDao().getAccountDAO(), peak.getDao().getPlayerDAO()));
        registerPerformer(new PlayerManageStoreExchangePerformer());
        registerPerformer(new PlayerStoreBuyExchangePerformer(peak.getDao().getPlayerDAO()));
        registerPerformer(new NPCStoreExchangePerformer(peak.getDao().getPlayerDAO(), peak.getWorld().getItemHandler()));
        registerPerformer(new MountParkExchangePerformer());
        registerPerformer(new MountExchangePerformer());
        registerPerformer(new CraftExchangePerformer());
    }
    
    public void validate(Player player){
        ExchangeBase exchange = player.getCurExchange();
        
        if(exchange == null)
            return;
        
        ExchangePerformer performer = performers.get(exchange.getType());
        
        if(performer == null){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Exchange performer for %s doesn't exists", exchange.getType());
            return;
        }
        
        performer.validate(player, exchange);
    }
    
    public void leave(Player player){
        ExchangeBase exchange = player.getCurExchange();
        
        if(exchange == null)
            return;
        
        ExchangePerformer performer = performers.get(exchange.getType());
        
        if(performer == null){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Exchange performer for %s doesn't exists", exchange.getType());
            return;
        }
        
        performer.leave(player, exchange);
    }
    
    public void request(ExchangeType type, Player player, String[] args){
        ExchangePerformer performer = performers.get(type);
        
        if(performer == null){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Exchange performer for %s doesn't exists", type);
            player.send(new ExchangeCreated(false));
            return;
        }
        
        try{
            performer.request(player, args);
        }catch(Exception e){
            player.send(new ExchangeCreated(false));
        }
    }
}
