/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level1;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.SocketManager;
import org.peakemu.objects.player.Player;
import org.peakemu.world.World;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Mute implements Command {

    final private SessionHandler sessionHandler;

    public Mute(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public String name() {
        return "MUTE";
    }

    @Override
    public String shortDescription() {
        return "Empèche un joueur de parler";
    }

    @Override
    public String help() {
        return "MUTE [qui] [secondes]";
    }

    @Override
    public int minLevel() {
        return 1;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        if(args.length < 2){
            performer.displayError("Commande invalide");
            return;
        }
        
        Player perso = null;
        String name = args[0];
        int time = 0;
        try {
            time = Integer.parseInt(args[1]);
        } catch (Exception e) {
            performer.displayError("La durée doit être un nombre valide");
            return;
        };

        perso = sessionHandler.searchPlayer(name);
        
        if (perso == null || time < 0) {
            String mess = "Le personnage n'existe pas ou la duree est invalide.";
            performer.displayError(mess);
            return;
        }
        String mess = "Vous avez mute " + perso.getName() + " pour " + time + " secondes";
        
        perso.getAccount().mute(time);
        performer.displayMessage(mess);
        SocketManager.GAME_SEND_Im_PACKET(perso, "1124;" + time);
    }

}
