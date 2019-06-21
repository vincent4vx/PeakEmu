/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.database.dao.AccountDAO;
import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class BankExchangePerformer implements ExchangePerformer{
    final private AccountDAO accountDAO;
    final private PlayerDAO playerDAO;

    public BankExchangePerformer(AccountDAO accountDAO, PlayerDAO playerDAO) {
        this.accountDAO = accountDAO;
        this.playerDAO = playerDAO;
    }

    @Override
    public ExchangeType getExchangeType() {
        return ExchangeType.BANK;
    }

    @Override
    public void request(Player player, String[] args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void leave(Player player, ExchangeBase exchange) {
        exchange.cancel();
        accountDAO.save(player.getAccount());
        playerDAO.save(player);
    }

    @Override
    public void validate(Player player, ExchangeBase exchange) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
