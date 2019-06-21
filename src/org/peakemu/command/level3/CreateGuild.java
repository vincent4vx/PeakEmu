/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.SocketManager;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class CreateGuild implements Command {

    private final SessionHandler session;

    public CreateGuild(SessionHandler session) {
        this.session = session;
    }

    @Override
    public String name() {
        return "CREATEGUILD";
    }

    @Override
    public String shortDescription() {
        return "Créer une guilde";
    }

    @Override
    public String help() {
        return "CREATEGUILD {qui}";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        Player target = performer.getPlayer();
        if (args.length > 1) {
            target = session.searchPlayer(args[0]);
        }
        if (target == null) {
           performer.displayError("Le personnage n'existe pas.");
            return;
        }

        if (!target.isOnline()) {
            performer.displayError("Le personnage " + target.getName() + " n'etait pas connecte");
            return;
        }
        if (target.getGuild() != null || target.getGuildMember() != null) {
            performer.displayError("Le personnage " + target.getName() + " a deja une guilde");
            return;
        }
        SocketManager.GAME_SEND_gn_PACKET(target);
        performer.displayMessage(target.getName() + ": Panneau de creation de guilde ouvert");
    }

}
