/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level2;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.SocketManager;
import org.peakemu.game.out.game.AlterSprite;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Title implements Command {

    private final SessionHandler session;

    public Title(SessionHandler session) {
        this.session = session;
    }

    @Override
    public String name() {
        return "TITLE"; //a return type of a created method
    }

    @Override
    public String shortDescription() {
        return "Changez le titre d'un personnage"; //a return type of a created method
    }

    @Override
    public String help() {
        return "TITLE [idTitre] [quoi] {qui}"; //a return type of a created method
    }

    @Override
    public int minLevel() {
        return 2; //a return type of a created method
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        Player target = performer.getPlayer();
        byte TitleID = 0;

        try {
            TitleID = Byte.parseByte(args[0]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }

        if (args.length > 2) { // nom spécifié
            target = session.searchPlayer(args[2]);
            if (target == null || target.getFight() != null) {
                String str = "Le personnage n'a pas ete trouve";
                performer.displayError(str);
                return;
            }
        }

        if (TitleID > 0) {
            target.set_title("," + TitleID + "*" + (args.length > 1 ? args[1] : ""));
        } else {
            target.set_title("");
        }

        performer.displayMessage("Titre mis en place.");
        if (target.getFight() == null) {
            target.getMap().sendToMap(new AlterSprite(target));
        }
    }

}
