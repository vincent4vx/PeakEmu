/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command;

import org.peakemu.game.GameClient;
import org.peakemu.objects.Account;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public interface CommandPerformer {
    public Player getPlayer();
    public Account getAccount();
    public int getLevel();
    public GameClient getClient();
    public void displayMessage(String message);
    public void displayError(String message);
}
