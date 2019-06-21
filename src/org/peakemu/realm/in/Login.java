/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.realm.in;

import org.peakemu.common.CryptManager;
import org.peakemu.database.dao.AccountDAO;
import org.peakemu.network.DofusClient;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Account;
import org.peakemu.realm.RealmClient;
import org.peakemu.realm.out.Answer;
import org.peakemu.realm.out.Community;
import org.peakemu.realm.out.GMLevel;
import org.peakemu.realm.out.HostList;
import org.peakemu.realm.out.LoginError;
import org.peakemu.realm.out.Pseudo;
import org.peakemu.world.handler.SessionHandler;
import org.peakemu.world.World;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Login implements InputPacket<RealmClient>{
    final private SessionHandler sessionHandler;
    final private AccountDAO accountDAO;
    final private World world;

    public Login(SessionHandler sessionHandler, AccountDAO accountDAO, World world) {
        this.sessionHandler = sessionHandler;
        this.accountDAO = accountDAO;
        this.world = world;
    }

    @Override
    public void parse(String args, RealmClient client) {
        String[] logins = args.split("\n");
        
        String account = logins[0].trim();
        String pass = logins[1].trim();
        pass = CryptManager.decryptPass(pass, client.getHashKey());
        
        DofusClient other = sessionHandler.getSessionByAccountName(account);
        
        if(other != null){
            client.send(new LoginError(LoginError.ALREADY_LOGGED_GAME_SERVER));
            sessionHandler.closeSession(client);
            
            other.send(new LoginError(LoginError.U_DISCONNECT_ACCOUNT));
            sessionHandler.closeSession(other);
            return;
        }
        
        Account accountObj = accountDAO.getAccountByName(account);
        
        if(accountObj == null || !accountObj.get_pass().equals(CryptManager.CryptSHA512(pass))){
            client.send(new LoginError(LoginError.LOGIN_ERROR));
            sessionHandler.closeSession(client);
            return;
        }
        
        if(accountObj.isBanned()){
            client.send(new LoginError(LoginError.BANNED));
            sessionHandler.closeSession(client);
            return;
        }
        
        client.setAccount(accountObj);
        
        client.send(new Pseudo(accountObj.get_pseudo()));
        client.send(new Community());
        client.send(HostList.createByWorld(world));
        client.send(new GMLevel(accountObj.get_gmLvl() > 0));
        client.send(new Answer(accountObj.get_question()));
    }

    @Override
    public String header() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
