/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.basic.ConsoleMessage;
import org.peakemu.objects.Account;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AdminConsoleCommandPerformer implements CommandPerformer{
    final private GameClient client;

    public AdminConsoleCommandPerformer(GameClient client) {
        this.client = client;
    }

    @Override
    public Player getPlayer() {
        return client.getPlayer();
    }

    @Override
    public Account getAccount() {
        return client.getAccount();
    }

    @Override
    public int getLevel() {
        return client.getAccount().get_gmLvl();
    }

    @Override
    public GameClient getClient() {
        return client;
    }

    @Override
    public void displayMessage(String message) {
        client.send(new ConsoleMessage(ConsoleMessage.INFO, message));
    }

    @Override
    public void displayError(String message) {
        client.send(new ConsoleMessage(ConsoleMessage.ERROR, message));
    }
    
}
