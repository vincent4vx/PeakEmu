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
import org.peakemu.world.World;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class SpellPoints implements Command {

    final private SessionHandler sessionHandler;

    public SpellPoints(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public String name() {
        return "SPELLPOINTS";
    }

    @Override
    public String shortDescription() {
        return "Ajouter des points de sorts";
    }

    @Override
    public String help() {
        return "SPELLPOINTS [qte] {qui}";
    }

    @Override
    public int minLevel() {
        return 2;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {

        if (args.length < 1) {
            performer.displayError("Commande invalide");
            return;
        }

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
        if (args.length > 1)//Si un nom de perso est spÃ©cifiÃ©
        {
            target = sessionHandler.searchPlayer(args[1]);
            if (target == null || target.getFight() != null) {
                String str = "Cible non trouvée ou en combat";
                performer.displayError(str);
                return;
            }
        }

        target.addSpellPoint(pts);
        SocketManager.GAME_SEND_STATS_PACKET(target);
        String str = "Le nombre de point de sort a été modifié";
        performer.displayError(str);
    }

}
