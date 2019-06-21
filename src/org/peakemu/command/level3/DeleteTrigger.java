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
public class DeleteTrigger implements Command {

    @Override
    public String name() {
        return "DELTRIGGER";
    }

    @Override
    public String shortDescription() {
        return "Supprime un trigger (plot de téléportation)";
    }

    @Override
    public String help() {
        return "DELTRIGGER [cellID]";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        Player target = performer.getPlayer();
        int cellID = -1;
        try {
            cellID = Integer.parseInt(args[0]);
        } catch (Exception e) {
            performer.displayError("Erreur commande");
            return;
        }
        
        if (cellID == -1 || target.getMap().getCell(cellID) == null) {
            performer.displayError("CellID invalide");
            return;
        }

        //target.getMap().getCell(cellID).clearOnCellAction();
        boolean success = Database.REMOVE_TRIGGER(target.getMap().getId(), cellID);
        String str = "";
        if (success) {
            str = "Le trigger a ete retire";
        } else {
            str = "Le trigger n'a pas ete retire";
        }
        performer.displayMessage(str);
    }

}
