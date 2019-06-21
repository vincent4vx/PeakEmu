/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.account;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.account.TokenResult;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Account;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AccountToken implements InputPacket<GameClient>{
    final private SessionHandler sessionHandler;

    public AccountToken(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        Account account = sessionHandler.removePendingAccount(args);
        
        System.out.println(args);
        
        if(account == null){
            client.send(new TokenResult(false));
            sessionHandler.closeSession(client);
            return;
        }
        
        client.setAccount(account);
        account.setGameThread(client);
        account.setCurIP(client.getIP());
        client.send(new TokenResult(true));
    }

    @Override
    public String header() {
        return "AT";
    }
    
}
