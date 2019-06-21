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
public class Size implements Command {

    private final SessionHandler sessionHandler;

    public Size(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public String name() {
        return "SIZE"; //a return type of a created method
    }

    @Override
    public String shortDescription() {
        return "Modifier la taille d'un joueur"; //a return type of a created method
    }

    @Override
    public String help() {
        return "SIZE [taille] {qui}"; //a return type of a created method
    }

    @Override
    public int minLevel() {
        return 2; //a return type of a created method
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int size = -1;
        try {
            size = Integer.parseInt(args[0]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }

        if (size == -1) {
            String str = "Taille invalide";
            performer.displayError(str);
            return;
        }

        Player target = performer.getPlayer();

        if (args.length > 2)//Si un nom de perso est spÃ©cifiÃ©
        {
            target = sessionHandler.searchPlayer(args[1]);
            if (target == null || target.getFight() != null) {
                String str = "Cible non trouvée ou en combat";
                performer.displayError(str);
                return;
            }
        }

        target.setSize(size);
        target.getMap().sendToMap(new AlterSprite(target));
        String str = "La taille du joueur a été modifiée";
        performer.displayMessage(str);
    }

}
