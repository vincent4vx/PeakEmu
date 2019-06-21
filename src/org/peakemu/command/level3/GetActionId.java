/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.world.action.ActionPerformer;
import org.peakemu.world.handler.ActionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GetActionId implements Command{
    final private ActionHandler actionHandler;

    public GetActionId(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }
    
    @Override
    public String name() {
        return "GETACTIONID";
    }

    @Override
    public String shortDescription() {
        return "Récupère l'action id en fonction de son nom";
    }

    @Override
    public String help() {
        return "GETACTIONID [nom action]";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        if(args.length < 1){
            performer.displayError("Commande invalide");
            return;
        }
         
        boolean found = false;
            
        for(ActionPerformer ap : actionHandler.getActions()){
            if(ap.getClass().getSimpleName().equalsIgnoreCase(args[0])){
                performer.displayMessage("Action : " + ap.getClass().getSimpleName() + " action id = " + ap.actionId());
                found = true;
            }
        }
        
        if(!found)
            performer.displayError("Action introuvable");
    }
    
}
