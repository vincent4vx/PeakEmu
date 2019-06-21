/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.CryptManager;
import org.peakemu.database.Database;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class DeleteFightPos implements Command {

    @Override
    public String name() {
        return "DELFIGHTPOS";
    }

    @Override
    public String shortDescription() {
        return "Supprimme une position de combat";
    }

    @Override
    public String help() {
        return "DELFIGHTPOS {cellID}";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int cell;
        Player target = performer.getPlayer();
        try {
            cell = Integer.parseInt(args[0]);
        } catch (Exception e) {
            performer.displayError("COmmande invalide");
            return;
        }
        
        if (cell < 0 || target.getMap().getCell(cell) == null) {
            cell = target.getCell().getID();
        }
        String places = target.getMap().get_placesStr();
        String[] p = places.split("\\|");
        String newPlaces = "";
        String team0 = "", team1 = "";
        
        try {
            team0 = p[0];
        } catch (Exception e) {
        }
        try {
            team1 = p[1];
        } catch (Exception e) {
        }

        for (int a = 0; a <= team0.length() - 2; a += 2) {
            String c = p[0].substring(a, a + 2);
            if (cell == CryptManager.cellCode_To_ID(c)) {
                continue;
            }
            newPlaces += c;
        }
        newPlaces += "|";
        for (int a = 0; a <= team1.length() - 2; a += 2) {
            String c = p[1].substring(a, a + 2);
            if (cell == CryptManager.cellCode_To_ID(c)) {
                continue;
            }
            newPlaces += c;
        }
        target.getMap().setPlaces(newPlaces);
        if (!Database.SAVE_MAP_DATA(target.getMap())) {
            return;
        }
        performer.displayMessage("Les places ont ete modifiees (" + newPlaces + ")");
    }

}
