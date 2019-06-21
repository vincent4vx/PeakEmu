/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.database.dao.AccountDAO;
import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Unban implements Command {
    
    private final SessionHandler session;
    final private AccountDAO accountDAO;
    final private PlayerDAO playerDAO;

    public Unban(SessionHandler session, AccountDAO accountDAO, PlayerDAO playerDAO) {
        this.session = session;
        this.accountDAO = accountDAO;
        this.playerDAO = playerDAO;
    }

    @Override
    public String name() {
        return "UNBAN";
    }

    @Override
    public String shortDescription() {
        return "Debanni un joueur";
    }

    @Override
    public String help() {
        return "UNBAN [qui]";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        Player target = session.searchPlayer(args[0]);
        /*if (target == null) {
            performer.displayError("Personnage non trouve");
            return;
        }*/

        if(playerDAO.getCharactersByAccount(target.getAccount()) == null){
            performer.displayError("Erreur, personnage n'existe pas");
            return;
        }
        
        /*if (target.getAccount()== null) {
            performer.displayError("Erreur");
            return;
        }*/
        target.getAccount().setBanned(false);
        accountDAO.save(target.getAccount());
        performer.displayMessage("Vous avez debanni " + target.getName());
    }

}
