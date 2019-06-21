/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level0;

import org.peakemu.Ancestra;
import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Infos implements Command {
    final private SessionHandler sessionHandler;

    public Infos(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public String name() {
        return "INFOS";
    }

    @Override
    public String shortDescription() {
        return "Récupère des informations sur le serveur";
    }

    @Override
    public String help() {
        return "";
    }

    @Override
    public int minLevel() {
        return 0;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {

        long uptime = System.currentTimeMillis() - Ancestra.gameServer.getStartTime();
        int jour = (int) (uptime / (1000 * 3600 * 24));
        uptime %= (1000 * 3600 * 24);
        int hour = (int) (uptime / (1000 * 3600));
        uptime %= (1000 * 3600);
        int min = (int) (uptime / (1000 * 60));
        uptime %= (1000 * 60);
        int sec = (int) (uptime / (1000));

        String mess = "===========\n" + Ancestra.makeHeader()
                + "\nUptime: " + jour + "j " + hour + "h " + min + "m " + sec + "s\n"
                + "Joueurs en lignes: " + sessionHandler.getClientCount() + "\n"
                + "Record de connexion: " + sessionHandler.getMaxClientCount() + "\n"
                + "===========";

        performer.displayMessage(mess);
    }

}
