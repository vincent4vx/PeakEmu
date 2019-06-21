/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public interface ExchangePerformer {
    public ExchangeType getExchangeType();
    public void request(Player player, String[] args);
    public void leave(Player player, ExchangeBase exchange);
    public void validate(Player player, ExchangeBase exchange);
}
