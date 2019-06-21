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
public class Morph implements Command {

    private final SessionHandler session;

    public Morph(SessionHandler session) {
        this.session = session;
    }

    @Override
    public String name() {
        return "MORPH"; //a return type of a created method
    }

    @Override
    public String shortDescription() {
        return "Changer l'apparence d'un joueur"; //a return type of a created method
    }

    @Override
    public String help() {
        return "MORPH [id] {qui}"; //a return type of a created method
    }

    @Override
    public int minLevel() {
        return 2; //a return type of a created method
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int morphID = -1;
        try {
            morphID = Integer.parseInt(args[0]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }

        if (morphID == -1) {
            String str = "MorphID invalide";
            performer.displayError(str);
            return;
        }

        Player target = performer.getPlayer();

        if (args.length > 1)//Si un nom de perso est spÃ©cifiÃ©
        {
            target = session.searchPlayer(args[1]);
            if (target == null) {
                String str = "Le personnage n'a pas ete trouve";
                performer.displayError(str);
                return;
            }
        }
        target.setGfxID(morphID);
        target.getMap().sendToMap(new AlterSprite(target));
        String str = "Le joueur a ete transforme";
        performer.displayMessage(str);
    }

}
