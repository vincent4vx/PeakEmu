/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.friend;

import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Account;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.AccountHandler;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AddEnemy implements InputPacket<GameClient>{
    final private SessionHandler sessionHandler;
    final private AccountHandler accountHandler;

    public AddEnemy(SessionHandler sessionHandler, AccountHandler accountHandler) {
        this.sessionHandler = sessionHandler;
        this.accountHandler = accountHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getAccount() == null)
            return;
        
        Account toAdd = null;
        
        switch(args.charAt(0)){
            case '*':
                toAdd = accountHandler.getAccountByPseudo(args.substring(1));
                break;
            case '%':
                args = args.substring(1);
            default:{
                Player player = sessionHandler.searchPlayer(args);
                
                if(player != null){
                    toAdd = player.getAccount();
                }
            }
        }
        
        accountHandler.addToEnemies(client.getAccount(), toAdd);
    }

    @Override
    public String header() {
        return "iA";
    }
    
}
