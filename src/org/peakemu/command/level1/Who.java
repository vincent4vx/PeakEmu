/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level1;

import java.util.Collection;
import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.game.GameClient;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Who implements Command {

    final private SessionHandler sessionHandler;

    public Who(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public String name() {
        return "WHO";
    }

    @Override
    public String shortDescription() {
        return "Qui est en ligne";
    }

    @Override
    public String help() {
        return "";
    }

    @Override
    public int minLevel() {
        return 1;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        String mess = "==========\n"
                + "Liste des joueurs en ligne:";
        performer.displayMessage(mess);

        Collection<GameClient> clients = sessionHandler.getGameClients();

        int diff = clients.size() - 30;
        int i = 0;
        for (GameClient GT : clients) {
            if (i >= 30) {
                break;
            }

            ++i;

            Player P = GT.getPlayer();
            if (P == null) {
                continue;
            }
            mess = P.getName() + "(" + P.getSpriteId() + ") ";
            mess += P.getRace();
            mess += " ";
            mess += (P.getGender() == 0 ? "M" : "F") + " ";
            mess += P.getLevel() + " ";
            mess += P.getMap().getId() + "(" + P.getMap().getX() + "/" + P.getMap().getY() + ") ";
            mess += P.getFight() == null ? "" : "Combat ";
            mess += "\nip:" + GT.getIP();
            mess += "\n";
            performer.displayMessage(mess);
        }

        if (diff > 0) {
            mess = "Et " + diff + " autres personnages";
            performer.displayMessage(mess);
        }

        mess = "==========\n";
        performer.displayMessage(mess);
    }

}
