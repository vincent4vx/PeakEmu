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
public class HPPer implements Command {

    private final SessionHandler session;

    public HPPer(SessionHandler session) {
        this.session = session;
    }

    @Override
    public String name() {
        return "HPPER"; //a return type of a created method
    }

    @Override
    public String shortDescription() {
        return "Donnez un %age de vie à un joueur"; //a return type of a created method
    }

    @Override
    public String help() {
        return "HPPER [%] {qui}"; //a return type of a created method
    }

    @Override
    public int minLevel() {
        return 2; //a return type of a created method
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int newPercent = 0;
        try {
            newPercent = Integer.parseInt(args[0]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }

        if (newPercent < 0 || newPercent > 100) {
            performer.displayError("Fixez une valeur entre 0 & 100");
            return;
        }

        Player target = performer.getPlayer();

        if (args.length > 1)//Si le nom du perso est spécifié
        {
            target = session.searchPlayer(args[1]);
            if (target == null || target.getFight() != null) {
                performer.displayError("Erreur cible non trouvée ou en combat");
                return;
            }

        }

        int newHP = target.getMaxLifePoints() * newPercent / 100;
        target.setLifePoints(newHP);
        if (target.isOnline()) {
            SocketManager.GAME_SEND_STATS_PACKET(target);
        }

        String mess = "Vous avez fixé le pourcentage de pdv de " + target.getName() + " a " + newPercent;
        performer.displayMessage(mess);

    }

}
