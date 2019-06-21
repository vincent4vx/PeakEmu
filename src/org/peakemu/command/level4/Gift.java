/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level4;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Gift implements Command {

    private final SessionHandler session;

    public Gift(SessionHandler session) {
        this.session = session;
    }

    @Override
    public String name() {
        return "GIFT"; //a return type of a created method
    }

    @Override
    public String shortDescription() {
        return "Offre un cadeau à un joueur"; //a return type of a created method
    }

    @Override
    public String help() {
        return "GIFT [idObjet] [qui]"; //a return type of a created method
    }

    @Override
    public int minLevel() {
        return 4; //a return type of a created method
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int present = 0;
        try {
            present = Integer.parseInt(args[0]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }
        
        Player target = performer.getPlayer();
        if (args.length > 1) {
            target = session.searchPlayer(args[1]);
            if (target == null) {
                performer.displayError("Personnage non trouvé");
                return;
            }
        }
        target.getAccount().setCadeau(present);
        performer.displayMessage("Don de " + present + " à  " + target.getName());
    }

}
