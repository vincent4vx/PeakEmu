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
public class ExchangeDistantMove {

    private char type;
    private long kamas;
    private Item item;
    private int itemQte;

    public ExchangeDistantMove(long kamas) {
        this.kamas = kamas;
        type = 'G';
    }

    public ExchangeDistantMove(Item item, int itemQte) {
        this.item = item;
        this.itemQte = itemQte;
        type = 'O';
    }

    @Override
    public String toString() {
        if (type == 'G') {
            return "EmKG" + kamas;
        }

        if (itemQte > 0) {
            return "EmKO+" + item.getGuid() + "|" + itemQte + "|" + item.getTemplate().getID() + "|" + StatsParser.statsTemplateToString(item.getStatsTemplate());
        } else {
            return "EmKO-" + item.getGuid();
        }
    }
}
