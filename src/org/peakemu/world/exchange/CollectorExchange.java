/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.objects.Collector;
import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CollectorExchange extends AbstractStorageExchange{
    final private Collector collector;

    public CollectorExchange(Collector collector, Player player) {
        super(player, collector);
        this.collector = collector;
    }

    @Override
    public ExchangeType getType() {
        return ExchangeType.COLLECTOR;
    }

    public Collector getCollector() {
        return collector;
    }
}
