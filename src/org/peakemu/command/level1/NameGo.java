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
public class NameGo implements Command {
    final private SessionHandler sessionHandler;
    final private PlayerHandler playerHandler;

    public NameGo(SessionHandler sessionHandler, PlayerHandler playerHandler) {
        this.sessionHandler = sessionHandler;
        this.playerHandler = playerHandler;
    }

    @Override
    public String name() {
        return "NAMEGO";
    }

    @Override
    public String shortDescription() {
        return "Téléporte un joueur vers la cible (ou vers soi)";
    }

    @Override
    public String help() {
        return "NAMEGO [qui] {cible}";
    }

    @Override
    public int minLevel() {
        return 1;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        Player target = sessionHandler.searchPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            String str = "Le personnage n'existe pas";
            performer.displayError(str);
            return;
        }
        if (target.getFight() != null) {
            String str = "La cible est en combat";
            performer.displayError(str);
            return;
        }
        
        Player P = performer.getPlayer();
        if (args.length > 1)//Si un nom de perso est spï¿œcifiï¿œ
        {
            P = sessionHandler.searchPlayer(args[1]);
            if (P == null || !P.isOnline()) {
                String str = "Le personnage n'a pas ete trouve ou n'est pas connecté";
                performer.displayError(str);
                return;
            }
        }
        if (P.isOnline()) {
            short mapID = P.getMap().getId();
            int cellID = P.getCell().getID();
            playerHandler.teleport(target, mapID, cellID);
            String str = "Le joueur a ete teleporte";
            performer.displayMessage(str);
        } else {
            String str = "Le joueur n'est pas en ligne";
            performer.displayError(str);
        }
    }

}
