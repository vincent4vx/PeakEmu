/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level1;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.objects.player.Player;
import org.peakemu.world.GameMap;
import org.peakemu.world.handler.MapHandler;
import org.peakemu.world.handler.PlayerHandler;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GoMap implements Command {

    final private SessionHandler sessionHandler;
    final private MapHandler mapHandler;
    final private PlayerHandler playerHandler;

    public GoMap(SessionHandler sessionHandler, MapHandler mapHandler, PlayerHandler playerHandler) {
        this.sessionHandler = sessionHandler;
        this.mapHandler = mapHandler;
        this.playerHandler = playerHandler;
    }

    @Override
    public String name() {
        return "GOMAP";
    }

    @Override
    public String shortDescription() {
        return "Téléporte un joueur à une géoposition";
    }

    @Override
    public String help() {
        return "GOMAP [X] [Y] {cellID} {continent} {qui}\nVoir aussi TELEPORT";
    }

    @Override
    public int minLevel() {
        return 1;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int mapX;
        int mapY;
        int cellID = performer.getPlayer().getCell().getID();
        int contID = performer.getPlayer().getMap().getSubArea().getArea().getSuperArea().get_id();
        
        try {
            mapX = Integer.parseInt(args[0]);
            mapY = Integer.parseInt(args[1]);
        } catch (Exception e) {
            performer.displayError("Veuillez spécifier une position x,y valide");
            return;
        }
        
        if(args.length > 2 && !args[2].equals("-1")){
            try{
                cellID = Integer.parseInt(args[2]);
            }catch(NumberFormatException e){}
        }
        
        if(args.length > 3 && !args[3].equals("-1")){
            try{
                contID = Integer.parseInt(args[3]);
            }catch(NumberFormatException e){}
        }
        
        GameMap map = mapHandler.getMapByPos(mapX, mapY, contID);
        if (map == null) {
            String str = "Position ou continent invalide";
            performer.displayError(str);
            return;
        }
        if (map.getCell(cellID) == null) {
            String str = "CellID invalide";
            performer.displayError(str);
            return;
        }
        Player target = performer.getPlayer();

        if (args.length > 4)//Si un nom de perso est spÃ©cifiÃ©
        {
            target = sessionHandler.searchPlayer(args[4]);
            if (target == null || target.getFight() != null) {
                String str = "Le personnage n'a pas ete trouve ou est en combat";
                performer.displayError(str);
                return;
            }
        }
        
        playerHandler.teleport(target, map.getId(), cellID);
        String str = "Le joueur a ete teleporte";
        performer.displayMessage(str);
    }

}
