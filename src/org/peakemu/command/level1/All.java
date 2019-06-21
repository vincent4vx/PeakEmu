/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level1;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.SocketManager;
import org.peakemu.common.util.StringUtil;
import org.peakemu.game.out.basic.ServerMessage;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class All implements Command{
    final private SessionHandler sessionHandler;

    public All(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public String name() {
        return "ALL";
    }

    @Override
    public String shortDescription() {
        return "Envoi un message à tout le monde";
    }

    @Override
    public String help() {
        return "ALL [message]";
    }

    @Override
    public int minLevel() {
        return 1;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        String prefix = "[" + performer.getPlayer().getName() + "]: ";
        sessionHandler.sendToOnline(ServerMessage.coloredMessage(ServerMessage.ERROR_CHAT_COLOR, prefix + StringUtil.join(args, " ")));
    }
    
}
