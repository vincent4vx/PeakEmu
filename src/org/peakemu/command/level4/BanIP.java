/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level4;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.Constants;
import org.peakemu.database.Database;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class BanIP implements Command {

    private final SessionHandler session;

    public BanIP(SessionHandler session) {
        this.session = session;
    }

    @Override
    public String name() {
        return "BANIP"; //a return type of a created method
    }

    @Override
    public String shortDescription() {
        return "BANNIR UN JOUEUR PAR IP"; //a return type of a created method
    }

    @Override
    public String help() {
        return "BANIP [qui]"; //a return type of a created method
    }

    @Override
    public int minLevel() {
        return 4; //a return type of a created method
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        Player target = null;
        /*try {
         target = session.searchPlayer(args[0]);
         } catch (Exception e) {
         performer.displayError("Commande invalide");
         return;
         }*/

        if (args.length > 0) {
            target = session.searchPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                String str = "Le personnage n'a pas ete trouve.";
                performer.displayError(str);
                return;
            }
        }

        if (!Constants.IPcompareToBanIP(target.getAccount().get_curIP())) {
            Constants.BAN_IP += "," + target.getAccount().get_curIP();
            if (Database.ADD_BANIP(target.getAccount().get_curIP())) {
                performer.displayMessage("L'IP a ete banni.");
            }
            if (target.isOnline()) {
                target.getAccount().getGameThread().kick();
                performer.displayMessage("Le joueur a ete kick.");
            }
        } else {
            String str = "L'IP existe deja.";
            performer.displayError(str);
        }
    }

}
