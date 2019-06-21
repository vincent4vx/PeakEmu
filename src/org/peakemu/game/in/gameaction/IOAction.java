/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import org.peakemu.common.util.StringUtil;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.world.MapCell;
import org.peakemu.world.handler.InteractiveObjectHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class IOAction implements GameAction{
    final static public int ACTION_ID = 500;
    
    final private InteractiveObjectHandler ioHandler;

    public IOAction(InteractiveObjectHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    @Override
    public void start(GameActionArg arg) {
        String[] args = StringUtil.split(arg.getArg(), ";", 2);
        int cellID = -1;
        int actionID = -1;
        
        try{
            cellID = Integer.parseInt(args[0]);
            actionID = Integer.parseInt(args[1]);
        }catch(NumberFormatException e){
            return;
        }
        
        //Si packet invalide, ou cellule introuvable
        if (cellID == -1 || actionID == -1 || arg.getClient().getPlayer() == null || arg.getClient().getPlayer().getMap() == null || arg.getClient().getPlayer().getMap().getCell(cellID) == null) {
            return;
        }
        
        arg.attach("IO_CELL", cellID);
        arg.attach("IO_ACTION", actionID);
        
        MapCell cell = arg.getClient().getPlayer().getMap().getCell(cellID);
        
        if(cell == null || !cell.isInteractive())
            return;
        
        ioHandler.startAction(cell, arg.getClient().getPlayer(), arg);
    }

    @Override
    public void end(GameActionArg arg, boolean success, String args) {
        MapCell cell = arg.getClient().getPlayer().getMap().getCell((int)arg.getAttachement("IO_CELL"));
        
        ioHandler.finishAction(cell, arg.getClient().getPlayer(), arg);
    }

    @Override
    public int actionId() {
        return IOAction.ACTION_ID;
    }
    
}
