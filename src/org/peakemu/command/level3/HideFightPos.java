/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.SocketManager;
import org.peakemu.game.in.fight.LeaveFight;
import org.peakemu.game.out.game.FightLeaved;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class HideFightPos implements Command{

    @Override
    public String name() {
        return "HIDEFIGHTPOS";
    }

    @Override
    public String shortDescription() {
        return "Cache les positions de combats";
    }

    @Override
    public String help() {
        return "HIDEFIGHTPOS\nVoir aussi SHOWFIGHTPOS";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        if(performer.getPlayer().getFight() != null){
            performer.displayError("Commande invalide en combat");
            return;
        }
        
        performer.getClient().send(new FightLeaved());
    }
    
}
