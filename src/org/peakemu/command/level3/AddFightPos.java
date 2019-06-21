/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.CryptManager;
import org.peakemu.common.SocketManager;
import org.peakemu.database.Database;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class AddFightPos implements Command {

    @Override
    public String name() {
        return "ADDFIGHTPOS";
    }

    @Override
    public String shortDescription() {
        return "Ajoute une cellule de combat";
    }

    @Override
    public String help() {
        return "ADDFIGHTPOS [team] {cellId}";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int team = -1;
        int cell = -1;
        Player target = performer.getPlayer();
        try {
            team = Integer.parseInt(args[0]);
            cell = Integer.parseInt(args[1]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }
        
        if (team < 0 || team > 1) {
            performer.displayError("Team invalide");
            return;
        }
        
        if (cell < 0 || target.getMap().getCell(cell) == null || !target.getMap().getCell(cell).isWalkable(true)) {
            cell = target.getCell().getID();
        }
        
        String places = target.getMap().get_placesStr();
        String[] p = places.split("\\|");
        boolean already = false;
        String team0 = "", team1 = "";
        try {
            team0 = p[0];
        } catch (Exception e) {
        }
        try {
            team1 = p[1];
        } catch (Exception e) {
        }

        //Si case dÃ©jÃ  utilisÃ©e
       // System.out.println("0 => " + team0 + "\n1 =>" + team1 + "\nCell: " + CryptManager.cellID_To_Code(cell));
        for (int a = 0; a <= team0.length() - 2; a += 2) {
            if (cell == CryptManager.cellCode_To_ID(team0.substring(a, a + 2))) {
                already = true;
            }
        }
        for (int a = 0; a <= team1.length() - 2; a += 2) {
            if (cell == CryptManager.cellCode_To_ID(team1.substring(a, a + 2))) {
                already = true;
            }
        }
        if (already) {
            performer.displayMessage("La case est deja dans la liste");
            return;
        }
        if (team == 0) {
            team0 += CryptManager.cellID_To_Code(cell);
        } else if (team == 1) {
            team1 += CryptManager.cellID_To_Code(cell);
        }

        String newPlaces = team0 + "|" + team1;

        target.getMap().setPlaces(newPlaces);
        if (!Database.SAVE_MAP_DATA(target.getMap())) {
            return;
        }
        performer.displayMessage("Les places ont ete modifiees (" + newPlaces + ")");
    }

}
