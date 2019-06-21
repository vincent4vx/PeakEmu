/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level4;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.maputil.Coordinate;
import org.peakemu.maputil.Direction;
import org.peakemu.world.MapCell;
import org.peakemu.world.handler.PlayerHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class TestPath implements Command{
    final private PlayerHandler playerHandler;

    public TestPath(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    @Override
    public String name() {
        return "TESTPATH";
    }

    @Override
    public String shortDescription() {
        return "";
    }

    @Override
    public String help() {
        return "";
    }

    @Override
    public int minLevel() {
        return 4;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        switch(args[0].toLowerCase()){
            case "angle":{
                MapCell target = performer.getPlayer().getMap().getCell(Integer.parseInt(args[1]));
                MapCell current = performer.getPlayer().getCell();
                Coordinate c1 = Coordinate.fromCell(current);
                Coordinate c2 = Coordinate.fromCell(target);
                performer.displayMessage("Angle : " + c1.getAngle(c2));
            }break;
            case "direction":{
                MapCell target = performer.getPlayer().getMap().getCell(Integer.parseInt(args[1]));
                MapCell current = performer.getPlayer().getCell();
                Coordinate c1 = Coordinate.fromCell(current);
                Coordinate c2 = Coordinate.fromCell(target);
                double angle = c1.getAngle(c2);
                performer.displayMessage("Direction : " + Direction.getDirectionByAngle(angle));
            }break;
            case "next":{
                MapCell current = performer.getPlayer().getCell();
                Direction dir = Direction.valueOf(args[1].toUpperCase());
                MapCell next = dir.nextCell(current);
                playerHandler.teleport(performer.getPlayer(), next.getMap(), next);
            }break;
        }
    }
    
}
