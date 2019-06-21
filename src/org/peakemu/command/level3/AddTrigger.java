/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.database.Database;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class AddTrigger implements Command {

    @Override
    public String name() {
        return "ADDTRIGGER";
    }

    @Override
    public String shortDescription() {
        return "Ajoute un plot de teleportation";
    }

    @Override
    public String help() {
        return "ADDTRIGGER [actionID] [argument] {condition}";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        Player target = performer.getPlayer();
        int actionID = -1;
        String argument = "", cond = "";
        try {
            actionID = Integer.parseInt(args[0]);
            argument = args[1];
            cond = args[2];
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }

        if (argument.equals("") || actionID <= -3) {
            performer.displayError("Valeur invalide");
            return;
        }

        //target.getCell().addOnCellStopAction(actionID, argument, cond);
        boolean success = Database.SAVE_TRIGGER(target.getMap().getId(), target.getCell().getID(), actionID, 1, argument, cond);
        String str;
        if (success) {
            str = "Le trigger a ete ajoute";
        } else {
            str = "Le trigger n'a pas ete ajoute";
        }
        performer.displayMessage(str);
    }

}
