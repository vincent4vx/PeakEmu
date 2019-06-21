/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level2;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.SocketManager;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Kamas implements Command {

    private final SessionHandler session;

    public Kamas(SessionHandler session) {
        this.session = session;
    }

    @Override
    public String name() {
        return "KAMAS"; //a return type of a created method
    }

    @Override
    public String shortDescription() {
        return "Ajouter des Kamas à un personnage"; //a return type of a created method
    }

    @Override
    public String help() {
        return "KAMAS [qte] {qui}"; //a return type of a created method
    }

    @Override
    public int minLevel() {
        return 2; //a return type of a created method
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        long newSum = 0;
        try {
            newSum = Long.parseLong(args[0]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }

        Player target = performer.getPlayer();
        if (args.length > 1)//Si le nom du perso est spécifié
        {
            target = session.searchPlayer(args[1]);
            if (target == null) {
                performer.displayError("Erreur la cible n'a pas été trouvée");
                return;
            }
        }

        long curKamas = target.getKamas();
        long newKamas = curKamas + newSum;
        if (newKamas < 0) {
            newKamas = 0;
        }

        target.setKamas(newKamas);
        if (target.isOnline()) {
            SocketManager.GAME_SEND_STATS_PACKET(target);
        }
        String mess = "Vous avez ";
        mess += (newSum < 0 ? "retiré" : "ajouté") + " ";
        mess += Math.abs(newSum) + " kamas à " + target.getName();
        performer.displayMessage(mess);
    }

}
