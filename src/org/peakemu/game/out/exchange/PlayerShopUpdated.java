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
public class PlayerShopUpdated {
    final public static char ACTION_ADD    = '+';
    final public static char ACTION_REMOVE = '-';

    private char action;
    private Item item;
    private long price;

    public PlayerShopUpdated(char action, Item item, long price) {
        this.action = action;
        this.item = item;
        this.price = price;
    }
    
    @Override
    public String toString() {
        return "EiK" + action + "" + item.getGuid() + "|" + item.getQuantity() + "|" + item.getTemplate().getID() + "|" + StatsParser.statsTemplateToString(item.getStatsTemplate()) + "|" + price;
    }
    
    
}
