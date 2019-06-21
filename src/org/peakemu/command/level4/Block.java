/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level4;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.game.GameClient;
import org.peakemu.objects.player.Player;
import org.peakemu.world.World;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Block implements Command {
    final private SessionHandler sessionHandler;

    public Block(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public String name() {
        return "BLOCK"; //a return type of a created method
    }

    @Override
    public String shortDescription() {
        return "Bloque le serveur à un niveau de GM"; //a return type of a created method
    }

    @Override
    public String help() {
        return "BLOCK [niveauGM] [expluseJoueur 1|0]"; //a return type of a created method
    }

    @Override
    public int minLevel() {
        return 4; //a return type of a created method
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        byte gmAccess = 0;
        byte kickPlayer = 1;
        try {
            gmAccess = Byte.parseByte(args[0]);
            kickPlayer = Byte.parseByte(args[1]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }

        World.setGmAccess(gmAccess);
        performer.displayMessage("Serveur bloque au GmLevel : " + gmAccess);
        if (kickPlayer > 0) {
            for (GameClient client : sessionHandler.getGameClients()) {
                if (client.getAccount() == null || client.getAccount().get_gmLvl() < gmAccess) {
                    client.close();
                }
            }
            performer.displayMessage("Les joueurs de GmLevel inferieur a " + gmAccess + " ont ete kicks.");
        }
    }

}
