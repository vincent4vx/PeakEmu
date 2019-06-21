/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level4;

import java.util.List;
import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.maputil.Compressor;
import org.peakemu.maputil.PathException;
import org.peakemu.maputil.Pathfinding;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Move implements Command{

    @Override
    public String name() {
        return "MOVE";
    }

    @Override
    public String shortDescription() {
        return "move cur player";
    }

    @Override
    public String help() {
        return "MOVE [cellid]";
    }

    @Override
    public int minLevel() {
        return 4;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int cellId = Integer.parseInt(args[0]);
        MapCell cell = performer.getPlayer().getMap().getCell(cellId);
        
        try{
            List<MapCell> path = Pathfinding.findPath(performer.getPlayer().getMap(), performer.getPlayer().getCell(), cell, true);

            String strPath = Compressor.compressPath(path);
            performer.getPlayer().setCell(cell);

            performer.getPlayer().getMap().sendToMap(new GameActionResponse(org.peakemu.game.in.gameaction.Move.ACTION_ID, performer.getPlayer().getId(), strPath));
        }catch(PathException e){
            performer.displayError("Path introuvable");
        }
    }
    
}
