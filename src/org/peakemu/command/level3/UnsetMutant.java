/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class UnsetMutant implements Command{
    final private SessionHandler sessionHandler;

    public UnsetMutant(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public String name() {
        return "UNSETMUTANT";
    }

    @Override
    public String shortDescription() {
        return "Retire le mode mutant d'un joueur";
    }

    @Override
    public String help() {
        return "UNSETMUTANT {qui}";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        Player target = performer.getPlayer();
        
        if(args.length > 0){
            target = sessionHandler.searchPlayer(args[0]);
        }
        
        if(target == null){
            performer.displayError("Joueur introuvable");
            return;
        }
        
        target.unsetMutant();
        performer.displayMessage("Le mode mutant a bien était enlevé");
    }
    
}
