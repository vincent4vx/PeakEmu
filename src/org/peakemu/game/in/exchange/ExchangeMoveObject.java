/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.exchange;

import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.StringUtil;
import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.world.exchange.Exchange;
import org.peakemu.world.exchange.ExchangeBase;
import org.peakemu.world.exchange.ExchangeItem;
import org.peakemu.world.exchange.StoreSellExchange;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ExchangeMoveObject implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getCurExchange() == null)
            return;
        
        ExchangeBase exchange = client.getPlayer().getCurExchange();
        
        char action = args.charAt(0);
        String[] data = StringUtil.split(args.substring(1), "|");
        int objID = Integer.parseInt(data[0]);
        int qte = Integer.parseInt(data[1]);
        
        if(exchange instanceof ExchangeItem){
            ExchangeItem ei = (ExchangeItem)exchange;
            switch(action){
                case '+':
                    ei.addItem(objID, qte);
                    break;
                case '-':
                    ei.removeItem(objID, qte);
                    break;
                default:
                    Peak.gameLog.addToLog(Logger.Level.ERROR, "ExchangeMoveObject : invalid action %c", action);
            }
        }else if(exchange instanceof StoreSellExchange){
            StoreSellExchange es = (StoreSellExchange)exchange;
            
            switch(action){
                case '+':
                    long price = Long.parseLong(data[2]);
                    es.sellItem(objID, qte, price);
                    break;
                case '-':
                    es.removeSellItem(objID, qte);
                    break;
                default:
                    Peak.gameLog.addToLog(Logger.Level.ERROR, "ExchangeMoveObject : invalid action %c", action);
            }
        }
    }

    @Override
    public String header() {
        return "EMO";
    }
    
}
