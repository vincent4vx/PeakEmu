/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.exchange;

import org.peakemu.database.parser.StatsParser;
import org.peakemu.objects.item.Item;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ExchangeUpdateStorage {
    final public static char TYPE_OBJECT   = 'O';
    final public static char TYPE_KAMAS    = 'G';
    final public static char ACTION_ADD    = '+';
    final public static char ACTION_REMOVE = '-';
    
    private boolean success = true;
    private char type;
    private char action;
    private long kamas;
    private Item item;

    public ExchangeUpdateStorage() {
    }

    public ExchangeUpdateStorage(boolean success) {
        this.success = success;
    }

    public ExchangeUpdateStorage setAction(char action) {
        this.action = action;
        return this;
    }

    public ExchangeUpdateStorage setKamas(long kamas) {
        this.kamas = kamas;
        type = TYPE_KAMAS;
        return this;
    }

    public ExchangeUpdateStorage setItem(Item item) {
        this.item = item;
        type = TYPE_OBJECT;
        return this;
    }

    @Override
    public String toString() {
        if(!success)
            return "EsE";
        
        String packet = "EsK" + type;
        
        if(type == TYPE_KAMAS)
            return packet + kamas;
        
        packet += action;
        
        if(action == ACTION_ADD)
            packet += item.getGuid() + "|" + item.getQuantity() + "|" + item.getTemplate().getID() + "|" + StatsParser.statsTemplateToString(item.getStatsTemplate());
        else
            packet += item.getGuid();
        
        return packet;
    }
}
