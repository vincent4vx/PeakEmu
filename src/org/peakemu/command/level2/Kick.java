/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level2;

import java.util.Arrays;
import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.SocketManager;
import org.peakemu.common.util.StringUtil;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Kick implements Command {

    final private SessionHandler sessionHandler;

    public Kick(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public String name() {
        return "KICK";
    }

    @Override
    public String shortDescription() {
        return "Eclure un joueur";
    }

    @Override
    public String help() {
        return "KICK [qui]";
    }

    @Override
    public int minLevel() {
        return 2;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {

        if (args.length < 1) {
            performer.displayMessage("Commande invalide");
            return;
        }

        String name = "";
        String reason = "";
        try {
            name = args[0];
            try {
                reason = "\n" + StringUtil.join(Arrays.copyOfRange(args, 1, args.length), " ");
            } catch (Exception e) {
                reason = "";
            }
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }

        Player target = performer.getPlayer();

        if (args.length > 0) {
            target = sessionHandler.searchPlayer(args[1]);
        }
        if (target == null) {
            performer.displayError("Le personne n'a pas été trouvée");
            return;
        }

        if (target.isOnline()) {
            SocketManager.REALM_SEND_MESSAGE(target, 18, target.getName() + ";" + reason);
            target.getAccount().getGameThread().kick();
            //SocketManager.GAME_SEND_Im_PACKET_TO_ALL("116;(System) <i>System</i>~" + "Le joueur <b>" + target.getName() + "</b> a été exclu du jeu.");
        }
    }

}
