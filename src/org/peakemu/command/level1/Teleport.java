/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level1;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.MapHandler;
import org.peakemu.world.handler.PlayerHandler;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Teleport implements Command {
    final private SessionHandler sessionHandler;
    final private MapHandler mapHandler;
    final private PlayerHandler playerHandler;

    public Teleport(SessionHandler sessionHandler, MapHandler mapHandler, PlayerHandler playerHandler) {
        this.sessionHandler = sessionHandler;
        this.mapHandler = mapHandler;
        this.playerHandler = playerHandler;
    }

    @Override
    public String name() {
        return "TELEPORT";
    }

    @Override
    public String shortDescription() {
        return "Téléport un joueur selon une mapID/cellID";
    }

    @Override
    public String help() {
        return "TELEPORT [mapID] [cellID] {qui}\nVoir aussi GOMAP";
    }

    @Override
    public int minLevel() {
        return 1;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        short mapID = -1;
        int cellID = -1;
        try {
            mapID = Short.parseShort(args[0]);
            cellID = Integer.parseInt(args[1]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        };
        
        if (mapHandler.getMap(mapID) == null) {
            performer.displayError("Map inexistante");
            return;
        }
        if (mapHandler.getMap(mapID).getCell(cellID) == null) {
            String str = "cellID invalide";
            performer.displayError(str);
            return;
        }
        
        Player target = performer.getPlayer();
        if (args.length > 2)//Si un nom de perso est spÃ©cifiÃ©
        {
            target = sessionHandler.searchPlayer(args[2]);
            if (target == null || target.getFight() != null) {
                String str = "Le personnage n'a pas ete trouve ou est en combat";
                performer.displayError(str);
                return;
            }
        }
        playerHandler.teleport(target, mapID, cellID);
        String str = "Le joueur a ete teleporte";
        performer.displayMessage(str);
    }

}
