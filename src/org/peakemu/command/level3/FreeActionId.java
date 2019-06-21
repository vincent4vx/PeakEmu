/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.world.handler.ActionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FreeActionId implements Command{
    final private ActionHandler actionHandler;

    public FreeActionId(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }

    @Override
    public String name() {
        return "FREEACTIONID";
    }

    @Override
    public String shortDescription() {
        return "[DEV] Récupère un ActionID libre";
    }

    @Override
    public String help() {
        return "FREEACTIONID";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int i = 0;
        
        while(actionHandler.getActionPerformer(i) != null){
            ++i;
        }
        
        performer.displayMessage("ActionID libre : " + i);
    }
    
}
