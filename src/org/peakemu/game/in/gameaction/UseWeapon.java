/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import org.peakemu.Peak;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class UseWeapon implements GameAction{
    final static public int ACTION_ID = 303;

    @Override
    public void start(GameActionArg arg) {
        Player player = arg.getClient().getPlayer();
        try {
            if (player.getFight() == null) {
                return;
            }
            int cellID = -1;
            try {
                cellID = Integer.parseInt(arg.getArg());
            } catch (Exception e) {
                return;
            }

            player.getFight().tryCaC(player, cellID);
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
        }
    }

    @Override
    public void end(GameActionArg arg, boolean success, String args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int actionId() {
        return ACTION_ID;
    }
    
}
