/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.common.SocketManager;
import org.peakemu.game.out.exchange.ExchangeCreated;
import org.peakemu.game.out.exchange.ExchangeList;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.enums.ExchangeType;
import org.peakemu.world.exchange.BankExchange;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class OpenBank implements ActionPerformer {

    @Override
    public int actionId() {
        return -1;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        if (caster.getDeshonor() >= 1) {
            SocketManager.GAME_SEND_Im_PACKET(caster, "183");
            return false;
        }
        
        org.peakemu.objects.Bank bank = caster.getAccount().getBank();
        final int cost = bank.getCost();
        if (cost > 0) {

            final long playerKamas = caster.getKamas();
            final long kamasRemaining = playerKamas - cost;
            final long bankKamas = bank.getKamas();
            final long totalKamas = bankKamas + playerKamas;

            if (kamasRemaining < 0)//Si le joueur n'a pas assez de kamas SUR LUI pour ouvrir la banque
            {
                if (bankKamas >= cost) {
                    bank.setKamas(bankKamas - cost); //On modifie les kamas de la banque
                } else if (totalKamas >= cost) {
                    caster.setKamas(playerKamas - (cost - bankKamas));
                    bank.setKamas(bankKamas);
                    SocketManager.GAME_SEND_STATS_PACKET(caster);
                    SocketManager.GAME_SEND_Im_PACKET(caster, "020;" + playerKamas);
                } else {
                    SocketManager.GAME_SEND_MESSAGE_SERVER(caster, "10|" + cost);
                    return false;
                }
            } else //Si le joueur a les kamas sur lui on lui retire directement
            {
                caster.setKamas(kamasRemaining);
                SocketManager.GAME_SEND_STATS_PACKET(caster);
                SocketManager.GAME_SEND_Im_PACKET(caster, "020;" + cost);
            }
        }
        
        caster.send(new ExchangeCreated(ExchangeType.BANK));
        caster.send(ExchangeList.fromBank(bank));
        caster.setAway(true);
        caster.setCurExchange(new BankExchange(caster, bank));
        
        return true;
    }

}
