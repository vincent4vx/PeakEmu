/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level2;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.Constants;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class SetAlign implements Command {

    private final SessionHandler sessionHandler;

    public SetAlign(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public String name() {
        return "SETALIGN";
    }

    @Override
    public String shortDescription() {
        return "Attribuer un nouvel alignement";
    }

    @Override
    public String help() {
        return "SETALIGN [idAlign] {qui}";
    }

    @Override
    public int minLevel() {
        return 2;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        byte align = -1;
        try {
            align = Byte.parseByte(args[0]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }

        if (align < Constants.ALIGNEMENT_NEUTRE || align > Constants.ALIGNEMENT_MERCENAIRE) {
            String str = "Valeur invalide";
            performer.displayError(str);
            return;
        }
        Player target = performer.getPlayer();
        if (args.length > 1)//Si un nom de perso est spÃ©cifiÃ©
        {
            target = sessionHandler.searchPlayer(args[1]);
            if (target == null || target.getFight() != null) {
                String str = "Le personnage n'a pas été trouvé ou est en combat";
                performer.displayError(str);
                return;
            }
        }

        target.modifAlignement(align);

        String str = "L'alignement du joueur a ete modifie";
        performer.displayMessage(str);
    }

}
