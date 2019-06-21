/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.basic.ServerMessage;
import org.peakemu.objects.Account;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ChatCommandPerformer implements CommandPerformer{
    final private Player player;

    public ChatCommandPerformer(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Account getAccount() {
        return player.getAccount();
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public GameClient getClient() {
        return player.getAccount().getGameThread();
    }

    @Override
    public void displayMessage(String message) {
        getClient().send(ServerMessage.coloredMessage(ServerMessage.ADMIN_CHAT_COLOR, message));
    }

    @Override
    public void displayError(String message) {
        getClient().send(ServerMessage.coloredMessage(ServerMessage.ERROR_CHAT_COLOR, message));
    }
    
}
