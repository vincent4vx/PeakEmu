/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level1;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.PlayerHandler;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Unjail implements Command {
    final private SessionHandler sessionHandler;
    final private PlayerHandler playerHandler;

    public Unjail(SessionHandler sessionHandler, PlayerHandler playerHandler) {
        this.sessionHandler = sessionHandler;
        this.playerHandler = playerHandler;
    }

    @Override
    public String name() {
        return "UNJAIL";
    }

    @Override
    public String shortDescription() {
        return "Fait sortir de prison un joueur";
    }

    @Override
    public String help() {
        return "UNJAIL [qui]\nVoir aussi JAIL";
    }

    @Override
    public int minLevel() {
        return 1;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        short mapID = 164;
        int cellID = 193;
        String name = args[0];
        Player target = sessionHandler.searchPlayer(name);

        String mess = "Le joueur <b>" + target.getName() + "</b> est <b>sorti de prison!</b>";
        if (!target.isOnline() || target.getFight() != null) {
            mess = "(Le personnage " + target.getName() + " n'etait pas connecte ou est en combat)";
            performer.displayError(mess);
        } else {
            String str = "164,193";
            target.setSavePos(str);
            playerHandler.teleport(target, mapID, cellID);
            performer.displayMessage(mess);
        }
    }

}
