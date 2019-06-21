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
public class Capital implements Command {

    private final SessionHandler sessionHandler;

    public Capital(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public String name() {
        return "CAPITAL"; //a return type of a created method
    }

    @Override
    public String shortDescription() {
        return "Ajouter des points de capital"; //a return type of a created method
    }

    @Override
    public String help() {
        return "CAPITAL [nbPoint] {qui}"; //a return type of a created method
    }

    @Override
    public int minLevel() {
        return 2; //a return type of a created method
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int pts = -1;
        try {
            pts = Integer.parseInt(args[0]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }

        if (pts == -1) {
            String str = "Valeur invalide";
            performer.displayError(str);
            return;
        }
        Player target = performer.getPlayer();
        if (args.length > 2)//Si un nom de perso est specifie
        {
            target = sessionHandler.searchPlayer(args[1]);
            if (target == null || target.getFight() != null) {
                String str = "Le personnage n'a pas été trouvé";
                performer.displayError(str);
                return;
            }
        }
        
        target.addCapital(pts);
        SocketManager.GAME_SEND_STATS_PACKET(target);
        String str = "Points modifiés";
        performer.displayMessage(str);
    }

}
