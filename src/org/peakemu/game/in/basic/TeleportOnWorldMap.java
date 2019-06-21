/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.basic;

import org.peakemu.command.CommandHandler;
import org.peakemu.command.CommandPerformer;
import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class TeleportOnWorldMap implements InputPacket<GameClient>{
    final private CommandHandler commandHandler;

    public TeleportOnWorldMap(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getAccount().get_gmLvl() < 1)
            return;
        
        CommandPerformer performer = client.getAdminConsoleCommandPerformer();
        
        String command = "GOMAP " + args.replace(',', ' ');
        commandHandler.perform(performer, command);
    }

    @Override
    public String header() {
        return "BaM";
    }
    
}
