/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level2;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.SocketManager;
import org.peakemu.world.enums.Effect;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AddStat implements Command{
    
    @Override
    public String name() {
        return "ADDSTAT";
    }

    @Override
    public String shortDescription() {
        return "Modifie une caractéristique d'un joueur";
    }

    @Override
    public String help() {
        return "ADDSTAT [stat] [qte]";
    }

    @Override
    public int minLevel() {
        return 2;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        Effect effect = Effect.valueOf(args[0].toUpperCase());
        int qte = Integer.parseInt(args[1]);
        performer.getPlayer().getBaseStats().addOneStat(effect, qte);
        SocketManager.GAME_SEND_STATS_PACKET(performer.getPlayer());
        performer.displayMessage("La caractéristique a bien était changée");
    }
    
}
