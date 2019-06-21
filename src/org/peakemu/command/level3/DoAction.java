/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.objects.player.Player;
import org.peakemu.world.action.Action;
import org.peakemu.world.handler.ActionHandler;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class DoAction implements Command {
    
    private final SessionHandler session;
    final private ActionHandler actionHandler;

    public DoAction(SessionHandler session, ActionHandler actionHandler) {
        this.session = session;
        this.actionHandler = actionHandler;
    }
    

    @Override
    public String name() {
        return "DOACTION";
    }

    @Override
    public String shortDescription() {
        return "Réaliser une action ID";
    }

    @Override
    public String help() {
        return "DOACTION [type] {args} {condition} {qui}";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        if (args.length < 1) {
            String mess = "Nombre d'argument de la commande incorect !";
            performer.displayError(mess);
            return;
        }
        
        int type;
        String argument = "";
        String cond = "";
        Player target = performer.getPlayer();
        
        try{
            type = Integer.parseInt(args[0]);
        }catch(NumberFormatException e){
            performer.displayError("Type invalide");
            return;
        }
        
        if(args.length >= 2)
            argument = args[1];
        
        if(args.length >= 3)
            cond = args[2];

        if(args.length > 3){
            target = session.searchPlayer(args[3]);
            if(target == null || target.getFight() != null){
                performer.displayError("Cible en combat ou non trouvée");
                return;
            }
        }
        
        Action action = new Action(type, argument, cond);
        actionHandler.apply(action, target, null, null, null);
        String mess = "Action effectuee !";
        performer.displayMessage(mess);
    }

}
