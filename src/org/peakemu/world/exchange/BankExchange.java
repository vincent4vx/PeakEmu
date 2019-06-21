/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.objects.Bank;
import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class BankExchange extends AbstractStorageExchange{
    final private Bank bank;
    
    public BankExchange(Player player, Bank bank) {
        super(player, bank);
        this.bank = bank;
    }

    @Override
    public ExchangeType getType() {
        return ExchangeType.BANK;
    }
    
}
