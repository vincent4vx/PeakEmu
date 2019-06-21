/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level2;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.Constants;
import org.peakemu.game.out.game.AlterSprite;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.PlayerHandler;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Honor implements Command {

    private final SessionHandler sessionHandler;
    final private PlayerHandler playerHandler;

    public Honor(SessionHandler sessionHandler, PlayerHandler playerHandler) {
        this.sessionHandler = sessionHandler;
        this.playerHandler = playerHandler;
    }

    @Override
    public String name() {
        return "HONOR";
    }

    @Override
    public String shortDescription() {
        return "Ajouter des points d'honneur à un joueur";
    }

    @Override
    public String help() {
        return "HONOR [point] {qui}";
    }

    @Override
    public int minLevel() {
        return 2;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int honor = 0;
        try {
            honor = Integer.parseInt(args[0]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }

        Player target = performer.getPlayer();

        if (args.length > 2)//Si un nom de perso est spÃ©cifiÃ©
        {
            target = sessionHandler.searchPlayer(args[1]);
            if (target == null) {
                String str = "Le personnage n'a pas ete trouve";
                performer.displayError(str);
                return;
            }
        }

        if (target.getAlignement() == Constants.ALIGNEMENT_NEUTRE) {
            String str = "Le joueur est neutre ...";
            performer.displayError(str);
            return;
        }
        String str = "Vous avez ajouter " + honor + " honneur a " + target.getName();
        playerHandler.addHonor(target, honor);
        performer.displayMessage(str);
        target.getMap().sendToMap(new AlterSprite(target));
    }

}
