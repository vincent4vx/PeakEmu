/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level4;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.database.dao.AccountDAO;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class SetAdmin implements Command {

    private final SessionHandler session;
    final private AccountDAO accountDAO;

    public SetAdmin(SessionHandler session, AccountDAO accountDAO) {
        this.session = session;
        this.accountDAO = accountDAO;
    }

    @Override
    public String name() {
        return "SETADMIN"; //a return type of a created method
    }

    @Override
    public String shortDescription() {
        return "Change le niveau d'un joueur"; //a return type of a created method
    }

    @Override
    public String help() {
        return "SETDMIN [niveau] [qui]"; //a return type of a created method
    }

    @Override
    public int minLevel() {
        return 4; //a return type of a created method
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int gmLvl = 0;
        try {
            gmLvl = Integer.parseInt(args[0]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }

        if (gmLvl < 0 || gmLvl > 4) {
            performer.displayError("Erreur niveau incorrect");
            return;
        }

        Player target = null;
        if (args.length > 1)//Si un nom de perso est spÃ©cifiÃ©
        {
            target = session.searchPlayer(args[1]);
        }

        if (target == null) {
            String str = "Le personnage n'a pas ete trouve";
            performer.displayError(str);
            return;
        }

        target.getAccount().setGmLvl(gmLvl);
        accountDAO.save(target.getAccount());
        String str = "Le niveau GM du joueur a ete modifie";
        performer.displayMessage(str);
    }

}