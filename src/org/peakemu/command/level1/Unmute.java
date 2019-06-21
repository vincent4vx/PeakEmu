/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level1;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Unmute implements Command {
    final private SessionHandler sessionHandler;

    public Unmute(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public String name() {
        return "UNMUTE";
    }

    @Override
    public String shortDescription() {
        return "Enlève le mute d'un joueur";
    }

    @Override
    public String help() {
        return "UNMUTE [qui]\nVoir aussi MUTE";
    }

    @Override
    public int minLevel() {
        return 1;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        if(args.length < 1){
            performer.displayError("Commande invalide");
            return;
        }
            
        Player perso = null;
        String name = args[0];

        perso = sessionHandler.searchPlayer(name);
        
        if (perso == null) {
            String mess = "Le personnage n'existe pas.";
            performer.displayError(mess);
            return;
        }

        perso.getAccount().mute(0);
        String mess = "Vous avez unmute " + perso.getName();
        performer.displayMessage(mess);
    }

}
