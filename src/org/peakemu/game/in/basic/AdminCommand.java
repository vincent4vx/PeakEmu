/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.basic;

import org.peakemu.command.CommandHandler;
import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AdminCommand implements InputPacket<GameClient>{
    final private CommandHandler commandHandler;

    public AdminCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getAccount() == null || client.getAccount().get_gmLvl() < 1){
            return;
        }
        
        commandHandler.perform(client.getAdminConsoleCommandPerformer(), args);
    }

    @Override
    public String header() {
        return "BA";
    }
    
}
