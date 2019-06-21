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
public class Jail implements Command {

    final private SessionHandler sessionHandler;
    final private PlayerHandler playerHandler;

    public Jail(SessionHandler sessionHandler, PlayerHandler playerHandler) {
        this.sessionHandler = sessionHandler;
        this.playerHandler = playerHandler;
    }

    @Override
    public String name() {
        return "JAIL";
    }

    @Override
    public String shortDescription() {
        return "Envoi un joueur en prison";
    }

    @Override
    public String help() {
        return "JAIL [qui]";
    }

    @Override
    public int minLevel() {
        return 1;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        if(args.length < 1){
            performer.displayMessage("Commande invalide");
            return;
        }
        
        short mapID = 1003;
        int cellID = 225;
        String name = args[0];
        Player target = sessionHandler.searchPlayer(name);

        String mess = "Le joueur <b>" + target.getName() + "</b> a ete <b>envoye en prison!</b>";
        if (!target.isOnline() || target.getFight() != null) {
            mess = "(Le personnage " + target.getName() + " n'etait pas connecte ou est en combat)";
            performer.displayError(mess);
        } else {
            String str = "1003,225";
            target.setSavePos(str);
            playerHandler.teleport(target, mapID, cellID);
            performer.displayMessage(mess);
        }

    }

}
