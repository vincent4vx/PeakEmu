/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class ToogleAggro implements Command {

    private final SessionHandler session;

    public ToogleAggro(SessionHandler session) {
        this.session = session;
    }

    @Override
    public String name() {
        return "TOOGLEAGGRO";
    }

    @Override
    public String shortDescription() {
        return "Empêche un joueur de se faire agresser";
    }

    @Override
    public String help() {
        return "TOOGLEAGGRO {qui}";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        Player target = performer.getPlayer();

        if (args.length > 1) {
            target = session.searchPlayer(args[0]);
            if (target == null) {
                performer.displayError("Le personnage n'existe pas.");
                return;
            }
        }

        target.set_canAggro(!target.canAggro());
        String mess = target.getName();
        if (target.canAggro()) {
            mess += " peut maintenant etre aggresser";
        } else {
            mess += " ne peut plus etre agresser";
        }
        performer.displayMessage(mess);

        if (!target.isOnline()) {
            performer.displayError("Le personnage " + target.getName() + " n'etait pas connecte");
        }
    }

}
